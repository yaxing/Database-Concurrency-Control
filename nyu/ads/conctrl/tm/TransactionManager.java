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
	
	public Date currentTimestamp;
	
	/**
	 * Default constructor. Initializes all member variables.
	 */
	public TransactionManager() {
		transTable = new TransactionTable();
		siteList = new ArrayList<Site>();
		varList = new ArrayList<Resource>();
		waitingQueueList = new ArrayList<WaitingQueue>();
		varLocations = new HashMap<String, List<Integer>>();
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
		
		//transManager.initSites();

		// get line of user input
		inputLine = inputScanner.next();
		
		while(!inputLine.equalsIgnoreCase("exit")) {
			// set the new current time stamp
			transManager.currentTimestamp = new Date();
			
			
			// parse input into instruction list
			List<ParsedInstrEnty> instructionList = transManager.parse(inputLine);
			
			// check WaitingQueues
			if (transManager.waitingQueueList.size() > 0) {
				// check to see if the transactions are still blocked
			}
			
			for (ParsedInstrEnty i : instructionList)
			{
				// process each instruction sequentially
				//transManager.process(i);
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
		String[] variables = {"X1", "X2", "X6"};
		String[] uniqueVariables = {"X2"};
		s.setBuffer("INIT X1:19 X2:12:UNIQ X6:15");
		s.process();
		siteList.add(s);
		
		addVariableLocations(variables, 1);
	
		s = new Site();
		String[] variables2 = {"X1", "X3", "X6"};
		String[] uniqueVariables2= {"X3"};
		s.setBuffer("INIT X1:19 X3:12:UNIQ X6:15");
		s.process();
		siteList.add(s);
		
		addVariableLocations(variables2, 2);
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
				break;
			case END:
				// Two-phase commit:
				// send message to all sites, get receipts
				
				// if all are good to go, send message to commit
				
				// update trans table
				break;
			case DUMP:
				// send message to applicable sites
				siteList.get(0).query();
				break;
			case FAIL:
				// send message to applicable site
				sendToSite(i.site, "FAIL");
				break;
			case RECOVER:
				// send message to applicable site
				sendToSite(i.site, "RECOVER");
			case R:
			case W:
				// send message to applicable sites
				// recieve receipt
				int site = varLocations.get(i.resource).indexOf(0);
				
				// TODO: send full timestamp?
				String command = "INSTR " + i.transactionId + " W " + i.resource + " " + i.value;
				String response = sendToSite(site, command);
				
				// parse response
				StringTokenizer st = new StringTokenizer(response);
				String op = st.nextToken();
				String result = st.nextToken();
				
				if (result.equals("0"))
				{
					// EXE_RESP 0 [T_NAME_HOLDER] [T_NAME_REQ]
					// perform wait-die protocol	
					int holderID = new Integer(st.nextToken());
					int reqID = new Integer(st.nextToken());
					Date holderTimestamp = transTable.getTimestamp(holderID);
					Date reqTimestamp = transTable.getTimestamp(reqID);
					
					if (holderTimestamp.before(reqTimestamp)) {
						// abort req
						sendAllSites("ABORT " + reqID);
					} else {
						// abort holder
						sendAllSites("ABORT " + reqID);
					}					
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
		siteList.get(site).setBuffer(command);
		return siteList.get(site).process();
	}
	
	/**
	 * Sends all sites the same command, and returns the list of the repsonses
	 * @param command
	 * @return response list
	 */
	public List<String> sendAllSites(String command) {
		List<String> responseList = new ArrayList<String>();
		
		for(int i = 0; i < siteList.size(); i++) {
			siteList.get(i).setBuffer(command);
			responseList.add(siteList.get(i).process());
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
			
			OpCode op = OpCode.valueOf(msg[0]);
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
				if (msg.length == 1) { break;}
				else {
					if(msg[1].startsWith("X")) {
						pie.resource = msg[1];
					}
					else {
						pie.site = new Integer(msg[1]);
					}
				}
			}
			instrList.add(pie);
		}
		
		
		return instrList;
	}
}
