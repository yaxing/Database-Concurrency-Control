package nyu.ads.conctrl.tm;

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
	
	public List<WaitingQueue> waitingQueueList; 
	
	public List<Site> siteList; // list of sites
	
	public List<Resource> varList; // List of variables, and latest timestamp 
	
	public Map<String, List<Integer>> varLocations;
	
	public TimeStamp currentTimestamp;
	
	public List<Integer> commitLog; // List of committed transactions
	
	public static Boolean DEBUG = true; 
	
	/**
	 * Default constructor. Initializes all member variables.
	 */
	public TransactionManager() {
		transTable = new TransactionTable();
		siteList = new ArrayList<Site>();
		varList = new ArrayList<Resource>();
		waitingQueueList = new ArrayList<WaitingQueue>();
		varLocations = new HashMap<String, List<Integer>>();
		commitLog = new ArrayList<Integer>();
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
			
			// check WaitingQueues
			if (transManager.waitingQueueList.size() > 0) {
				// check to see if the transactions are still blocked
			}
			
			for (ParsedInstrEnty i : instructionList)
			{
				// process each instruction sequentially
				transManager.process(i);
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
		Site s = new Site();
		String[] variables = {"X2", "X8"};
		String[] uniqueVariables = {"X2", "X8"};
		s.setBuffer("INIT X2:2:UNIQ X8:8:UNIQ");
		s.process();
		siteList.add(s);
		
		addVariableLocations(variables, 1);
	
		s = new Site();
		String[] variables2 = {"X1"};
		String[] uniqueVariables2= {"X1"};
		s.setBuffer("INIT X1:1:UNIQ");
		s.process();
		siteList.add(s);
		
		addVariableLocations(variables2, 2);
		
		s = new Site();
		String[] variables3 = {"X3"};
		String[] uniqueVariables3= {"X3"};
		s.setBuffer("INIT X3:3:UNIQ");
		s.process();
		siteList.add(s);
		
		addVariableLocations(variables3, 3);
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
	public void process(ParsedInstrEnty i) {
		switch(i.opcode) {
			case BEGIN:
				// update trans table
				Transaction t = new Transaction();
				t.transID = i.transactionId;
				t.timestamp = currentTimestamp;
				t.status = 1;
				t.readOnly = false;
				
				transTable.addTransaction(t);
				break;
			case BEGINRO:
				// update trans table
				Transaction tro = new Transaction();
				tro.transID = i.transactionId;
				tro.timestamp = currentTimestamp;
				tro.status = 1;
				tro.readOnly = true;
				
				transTable.addTransaction(tro);
				
				sendAllSites("SNAPSHOT " + tro.timestamp.getTime());
				break;
			case END:
				// Two-phase commit:
				// send message to all sites, get receipts
				
				sendAllSites("COMMIT " + i.transactionId);
				
				commitLog.add(new Integer(i.transactionId));
				// if all are good to go, send message to commit
				
				// update trans table
				break;
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
				break;
			case FAIL:
				// send message to applicable site
				sendToSite(i.site, "FAIL");
				break;
			case RECOVER:
				// send message to applicable site
				sendToSite(i.site, "RECOVER");
				break;
			case RO:
			case R:
			case W:
				// send message to applicable sites
				// recieve receipt
				int site = -1;
				if(varLocations.containsKey(i.resource)) {
					// TODO: check sitelist to make sure site  is up
					List<Integer> siteIndexList = varLocations.get(i.resource);
					for (Integer siteIndex : siteIndexList)
					{
						if(siteList.get(siteIndex-1).getStatus() == 1) {
							site = varLocations.get(i.resource).get(0);
						}
					}
					if (site == -1) {
						// all sites with the variable are failed
						System.err.println("All sites are failed for resource: " + i.resource);
						System.exit(-1);
					}
				}
				else {
					System.err.println("No site holds the resource: " + i.resource);
					System.exit(-1);
				}
				
				String command = "INSTR " + i.transactionId + " " + transTable.getTimestamp(i.transactionId).getTime() +
					" " + i.opcode + " " + i.resource + " " + (i.value!=null ?i.value:"");
				String response = sendToSite(site, command);
				
								
				// parse response
				String resp[] = response.split(" ");
				String result = resp[1];
				
				if (result.equals("1")) {  
					// write successful
				}
				else if (result.equals("-1")) {
					// site has failed
				}
				else if (result.equals("0"))
				{
					// could not acquire lock
					
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
					} else {
						// abort holder(s)
						for(int hol : holderID) {
							if(hol != reqID) {
								System.out.println("ABORT " + hol);
								sendAllSites("ABORT " + hol);
							}
						}
					}					
				}
				else {
					// successful read operation
					System.out.println(response);
				}
				
				break;
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
				if(transTable.TransactionList.get(pie.transactionId-1).readOnly) {
					pie.opcode = OpCode.RO;
				}
				break;
			case W:
				pie.transactionId = new Integer(msg[1].substring(1));
				pie.resource = msg[2];
				pie.value = msg[3];
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
			instrList.add(pie);
		}
		
		
		return instrList;
	}
}
