package nyu.ads.conctrl.tm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import nyu.ads.conctrl.site.Site;
import nyu.ads.conctrl.entity.*;
import nyu.ads.conctrl.tm.entity.*;

/**
 * Transaction Manager class
 * 
 * @author Matt
 *
 */
public class TransactionManager {
	public TransactionTable transTable;  // List of transactions, statuses, and timestamp of origin
	
	public Map<Integer, WaitingQueue> waitingQueueList; 
	
	public List<Site> siteList; // list of sites
	
	public List<Resource> varList; // List of variables, and latest timestamp 
	
	public Map<String, List<Integer>> varLocations;
	
	public TimeStamp currentTimestamp;
	
	public List<ArrayList<Integer>> visitorList;
	
	public List<Integer> commitLog; // List of committed transactions
	
	public static Boolean DEBUG = true; 
	
	/**
	 * Default constructor. Initializes all member variables.
	 */
	public TransactionManager() {
		transTable = new TransactionTable();
		siteList = new ArrayList<Site>();
		varList = new ArrayList<Resource>();
		waitingQueueList = new HashMap<Integer, WaitingQueue>();
		varLocations = new HashMap<String, List<Integer>>();
		commitLog = new ArrayList<Integer>();
		visitorList = new ArrayList<ArrayList<Integer>>();
	}
	
	/**
	 * Entry function to the program.
	 * @param [the input file of instructions]
	 */
	public static void main(String[] args) {
		TransactionManager transManager = new TransactionManager();
		
		Scanner inputScanner = new Scanner(System.in);
		inputScanner.useDelimiter(System.getProperty("line.separator"));
		String inputLine = "";
		
		transManager.initSites();

		// get line of user input
		inputLine = inputScanner.next();
		
		while(!inputLine.equalsIgnoreCase("exit")) {
			// set the new current time stamp
			transManager.currentTimestamp = new TimeStamp();
			
			
			// parse input into instruction list
			List<ParsedInstrEnty> instructionList = transManager.parse(inputLine);
			
			List<Integer> transactionsProcessed = new ArrayList<Integer>();
			
			// execute any fail, recover, dump commands
			List<ParsedInstrEnty> deleteList = new ArrayList<ParsedInstrEnty>();
			for (ParsedInstrEnty i : instructionList)
			{
				if(i.opcode == OpCode.FAIL || i.opcode == OpCode.RECOVER || i.opcode == OpCode.DUMP)
				{
					transManager.process(i);
					deleteList.add(i);
				}
			}
			
			for (ParsedInstrEnty d : deleteList) {
				instructionList.remove(d);			
			}
			
			// check WaitingQueues
			Iterator it = transManager.waitingQueueList.entrySet().iterator();
			while(it.hasNext() ){
				Map.Entry<Integer, WaitingQueue> pairs = (Map.Entry<Integer, WaitingQueue>)it.next();
				// check to see if the transactions are still blocked
				boolean workToDo = true;
				while (pairs.getValue().isBlocked() && workToDo) {
					workToDo = false;
					// try to process the first one
					if(transManager.process(pairs.getValue().viewFirst())){
						int i = pairs.getValue().viewFirst().transactionId;
						pairs.getValue().dequeue();
						transactionsProcessed.add(i);
						workToDo = true;
					}
				}		
			}
			
			for (ParsedInstrEnty i : instructionList)
			{
				if (transManager.waitingQueueList.containsKey(i.transactionId) && 
						transManager.waitingQueueList.get(i.transactionId).isBlocked()){
					transManager.waitingQueueList.get(i.transactionId).enqueue(i);
					System.err.println("Buffering command: " + i.originalInstruction);
				}
				else 
				{
					// process each instruction sequentially
					if(transManager.process(i))
					{
						// instruction processed correctly
					}
					else if(transManager.transTable.containsTransaction(i.transactionId)) {
						transManager.waitingQueueList.get(i.transactionId).enqueue(i);
					}
				}
			}			
			// get the next line of user input
			inputLine = inputScanner.next();
		}  
	}

	/**
	 * Initialize the variable lists at the sites
	 */
	public void initSites() {
		// init sites
		try {
			File sitesFile = new File("src/sites");
			Scanner sc = new Scanner(sitesFile);
			sc.useDelimiter(System.getProperty("line.separator"));
			while(sc.hasNext()){
				String siteTextFull = sc.next();
				String[] siteText = siteTextFull.split(" ");
				int site = new Integer(siteText[0]);
				
				Site s = new Site();
				String[] variables = new String[siteText.length-1];
				String buffer = "INIT ";
				for(int i = 1; i < siteText.length; i++){
					buffer += siteText[i] + " ";
					variables[i-1] = siteText[i].split(":")[0]; 
				}
				s.setBuffer(buffer);
				s.process();
				siteList.add(s);
				
				addVariableLocations(variables, site);
				visitorList.add(new ArrayList<Integer>());
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void addVariableLocations(String[] variables, int site) {
		for (int i = 0; i < variables.length; i++) {
			if (varLocations.containsKey(variables[i])) {
				List<Integer> curr = varLocations.get(variables[i]);
				curr.add(new Integer(site));
			}
			else {
				List<Integer> curr = new ArrayList<Integer>();
				curr.add(new Integer(site));
				varLocations.put(variables[i], curr);
			}
		}
	}

	/**
	 * Process method that processes the current instruction.
	 * @param: the instruction
	 */
	public boolean process(ParsedInstrEnty i) {
		switch(i.opcode) {
			case BEGIN:
				// update trans table
				Transaction t = new Transaction();
				t.transID = i.transactionId;
				t.timestamp = currentTimestamp;
				t.status = 1;
				t.readOnly = false;
				
				if(!transTable.containsTransaction(i.transactionId)) {
					transTable.addTransaction(t);
					waitingQueueList.put(i.transactionId, new WaitingQueue());
					return true;
				} else {
					System.err.println("Already have a transaction: " + i.transactionId);
					return false;
				}
			case BEGINRO:
				// update trans table
				Transaction tro = new Transaction();
				tro.transID = i.transactionId;
				tro.timestamp = currentTimestamp;
				tro.status = 1;
				tro.readOnly = true;
				
				if(!transTable.containsTransaction(i.transactionId)) {
					transTable.addTransaction(tro);				
					waitingQueueList.put(i.transactionId, new WaitingQueue());
					sendAllSites("SNAPSHOT " + tro.timestamp.getTime());
					return true;
				} else {
					System.err.println("Already have a transaction: " + i.transactionId);
					return false;
				}				
			case END:
				// Two-phase commit:
				// send message to all sites, get receipts
				if(transTable.containsTransaction(i.transactionId)){
					sendAllSites("COMMIT " + i.transactionId);
					
					commitLog.add(new Integer(i.transactionId));
					// if all are good to go, send message to commit
					
					// 	update trans table					
					return true;
				} else {
					System.err.println("Do not have a record for transaction: " + i.transactionId);
					return false;
				}
			case DUMP:
				// send message to applicable sites
				String msg = "DUMP";
				if(i.resource != null) {  msg += " " + i.resource; }
				if(i.site != -1) {
					System.out.println(sendToSite(i.site, msg));
				}
				else {
					List<String> result = sendAllSites(msg);
					for(String r: result)
					{
						System.out.println(r);
					}
				}
				return true;
			case FAIL:
				// send message to applicable site
				sendToSite(i.site, "FAIL");
				failVisitors(i.site);
				return true;
			case RECOVER:
				// send message to applicable site
				sendToSite(i.site, "RECOVER");
				return true;
			case W:
				// send message to applicable sites
				// recieve receipt
				if(!transTable.containsTransaction(i.transactionId)){
					System.err.println("Do not have a record for transaction: " + i.transactionId);
					return false;
				}
				boolean canWrite = false;
				if(varLocations.containsKey(i.resource)) {
					// TODO: check sitelist to make sure a site is up
					List<Integer> siteIndexList = varLocations.get(i.resource);
					for (Integer siteIndex : siteIndexList)
					{
						if(siteList.get(siteIndex-1).getStatus() == 1) {
							canWrite = true;
							break;
						}
					}
					if (!canWrite) {
						// all sites with the variable are failed
						System.err.println("All sites are failed for resource: " + i.resource);
						System.err.println("Buffering command: " + i.originalInstruction);
						return false;
					}
				}
				else {
					System.err.println("No site holds the resource: " + i.resource);
					return false;
				}
				
				String command = "INSTR " + i.transactionId + " " + transTable.getTimestamp(i.transactionId).getTime() +
					" " + i.opcode + " " + i.resource + " " + (i.value!=null ?i.value:"");
				List<String> responses = sendAllSites(command);
				
				int siteCounter = -1;
				for (String response : responses)
				{
					siteCounter++;
					// parse response
					String resp[] = response.split(" ");
					String result = resp[1];
					
					if (result.equals("1")) {  
						// write successful
						// add transaction to visitor list for site
						if(!visitorList.get(siteCounter).contains(i.transactionId))
								visitorList.get(siteCounter).add(i.transactionId);
						continue;
					}
					else if (result.equals("-2")) {
						// site does not have the variable
						continue;
					}
					else if (result.equals("-1")) {
						// site has failed
						continue;
					}
					else if (result.equals("0"))
					{	
						// EXE_RESP 0 [{T_NAME_HOLDER,T_NAME_HOLDER...}] [T_NAME_REQ] [V_NAME:V_VALUE]
						// perform wait-die protocol	
						String holdersFull = resp[2].substring(1, resp[2].length()-1);
						String holders[] = holdersFull.split(",");
						
						int[] holderID = new int[holders.length];
						
						int j = 0;
						for(String h : holders) {
							holderID[j] = new Integer(h);
							j++;
						}
						
						int reqID = new Integer(resp[3]);
						TimeStamp oldestHolderTimestamp = transTable.getTimestamp(holderID[0]);
						for(int ho : holderID) {
							TimeStamp holderTimestamp = transTable.getTimestamp(ho);
							if (holderTimestamp.before(oldestHolderTimestamp)) {
								oldestHolderTimestamp = holderTimestamp;
							}
						}
						TimeStamp reqTimestamp = transTable.getTimestamp(reqID);
						
						if (oldestHolderTimestamp.before(reqTimestamp)) {
							// abort req
							System.out.println("ABORT " + reqID);
							sendAllSites("ABORT " + reqID);
							transTable.setStatus(reqID, -1);
							return true;
						} else {
							// Buffer request
							System.err.println("Buffering command: " + i.originalInstruction);
							return false;						
						}
					}
				}
					
				return true;
			case RO:
			case R:
				// send message to applicable sites
				// recieve receipt
				if(!transTable.containsTransaction(i.transactionId)){
					System.err.println("Do not have a record for transaction: " + i.transactionId);
					return false;
				}
				int site = -1;
				if(varLocations.containsKey(i.resource)) {
					// TODO: check sitelist to make sure site  is up
					List<Integer> siteIndexList = varLocations.get(i.resource);
					for (Integer siteIndex : siteIndexList)
					{
						if(siteList.get(siteIndex-1).getStatus() == 1) {
							site = siteIndex;
							command = "INSTR " + i.transactionId + " " + transTable.getTimestamp(i.transactionId).getTime() +
							" " + i.opcode + " " + i.resource + " " + (i.value!=null ?i.value:"");
							String response = sendToSite(site, command);				
											
							// parse response
							String resp[] = response.split(" ");
							String result = resp[1];
							
							if (result.equals("-1")) {
								// site has failed
							}
							else if (result.equals("0"))
							{
								// could not acquire lock
								if(resp.length == 2) {
									System.err.println("Could not read, there is a recover lock: " + i.originalInstruction);
									continue;
								}
								else {
								
									// EXE_RESP 0 [{T_NAME_HOLDER,T_NAME_HOLDER...}] [T_NAME_REQ] [V_NAME:V_VALUE]
									// perform wait-die protocol	
									String holdersFull = resp[2].substring(1, resp[2].length()-1);
									String holders[] = holdersFull.split(",");
									
									int[] holderID = new int[holders.length];
									
									int j = 0;
									for(String h : holders) {
										holderID[j] = new Integer(h);
										j++;
									}
									
									int reqID = new Integer(resp[3]);
									TimeStamp oldestHolderTimestamp = transTable.getTimestamp(holderID[0]);
									for(int ho : holderID) {
										TimeStamp holderTimestamp = transTable.getTimestamp(ho);
										if (holderTimestamp.before(oldestHolderTimestamp)) {
											oldestHolderTimestamp = holderTimestamp;
										}
									}
									TimeStamp reqTimestamp = transTable.getTimestamp(reqID);
									
									if (oldestHolderTimestamp.before(reqTimestamp)) {
										// abort req
										System.out.println("ABORT " + reqID);
										sendAllSites("ABORT " + reqID);
										transTable.setStatus(reqID, -1);
										return true;
									} else {
										// Buffer request
										System.err.println("Buffering command: " + i.originalInstruction);
										return false;						
									}					
								}
							}
							else {
								// successful read operation
								System.out.println(response);
								if(!visitorList.get(site-1).contains(i.transactionId))
									visitorList.get(site-1).add(i.transactionId);
							}
							
							return true;
						}
					}
					if (site == -1) {
						// all sites with the variable are failed
						System.err.println("All sites are failed for resource: " + i.resource);
						System.err.println("Buffering command: " + i.originalInstruction);
						return false;
					}
				}
				else {
					System.err.println("No site holds the resource: " + i.resource);
					return false;
				}
			default:
				return false;
		}		
	}
	
	private void failVisitors(int site) {
		for(Integer i : visitorList.get(site-1)) {
			sendAllSites("ABORT " + i);
		}
	}

	/**
	 * 
	 * @param site
	 * @param command
	 * @return
	 */
	public String sendToSite(int site, String command) {
		
		siteList.get(site-1).setBuffer(command);
		
		String result = siteList.get(site-1).process();
		
		if (DEBUG) {
			System.out.println("**DEBUG** Site={" + site + "} Command=\"" + command + "\"");			
			System.out.println("**DEBUG** Result=\"" + result + "\"");
		}
		return result;
	}
	
	/**
	 * Sends all sites the same command, and returns the list of the repsonses
	 * @param command
	 * @return response list
	 */
	public List<String> sendAllSites(String command) {
		List<String> responseList = new ArrayList<String>();
		
		String result = "";
		
		for(int i = 0; i < siteList.size(); i++) {
			siteList.get(i).setBuffer(command);
			responseList.add(siteList.get(i).process());
			result += responseList.get(i);
		}
		
		if (DEBUG) {
			System.out.println("**DEBUG** Site={all} Command=\"" + command + "\"");			
			System.out.println("**DEBUG** Result=\"" + result + "\"");
		}
		
		return responseList;
	}
	

	/**
	 * Parse the input string and create an list of instructions
	 * @param input to be parsed
	 * @return the list of instructions
	 */
	public List<ParsedInstrEnty> parse(String input) {
		String msgs[] = input.split(";");
		
		List<ParsedInstrEnty> instrList = new ArrayList<ParsedInstrEnty>();
		for(String m : msgs){
			boolean misformed = false;
			String[] msg = m.trim().split("\\(|,|\\)");
			
			// clean whitespace, make each token upper case
			for(int i = 0; i < msg.length; i++) {
				msg[i] = msg[i].trim().toUpperCase();
			}
			
			OpCode op  = OpCode.valueOf(msg[0]);
			ParsedInstrEnty pie = new ParsedInstrEnty();
			pie.opcode = op;
			pie.originalInstruction = m;
			
			switch(op) {
			case BEGIN:
			case BEGINRO:
			case END:
				pie.transactionId = new Integer(msg[1].substring(1));
				break;
			case R:
				pie.transactionId = new Integer(msg[1].substring(1));
				pie.resource = msg[2];
				if(transTable.containsTransaction(pie.transactionId) &&
					transTable.TransactionList.get(pie.transactionId-1).readOnly) {				
					pie.opcode = OpCode.RO;
				}
				break;
			case W:
				pie.transactionId = new Integer(msg[1].substring(1));
				pie.resource = msg[2];
				if(msg.length==4)
					pie.value = msg[3];
				else 
					misformed = true;
				break;
			case FAIL:
			case RECOVER:
				pie.site = new Integer(msg[1]);
				break;
			case DUMP:
				if (msg.length == 1) {
					pie.site = -1;
					break;
				}
				else {
					if(msg[1].startsWith("X")) {
						pie.resource = msg[1];
						pie.site = -1;
					}
					else {
						pie.site = new Integer(msg[1]);
						pie.resource = (msg.length == 3 ? msg[2] : "");
					}
				}
			}
			if (!misformed)
				instrList.add(pie);
			else
				System.err.println("Misformed command: " + pie.originalInstruction);
		}
		
		
		return instrList;
	}
}
