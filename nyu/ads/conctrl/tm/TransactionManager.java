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
	
	public List<Resource> varList; // List of variables, locations, and latest timestamp  
	
	public Date currentTimestamp;
	
	/**
	 * Default constructor. Initializes all member variables.
	 */
	public TransactionManager() {
		transTable = new TransactionTable();
		siteList = new ArrayList<Site>();
		varList = new ArrayList<Resource>();
		waitingQueueList = new ArrayList<WaitingQueue>();
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
		
		do {
			// set the new current timestamp
			transManager.currentTimestamp = new Date();
			
			// get line of user input
			inputLine = inputScanner.next();
			
			// parse input into instruction list
			List<ParsedInstrEnty> instructionList = transManager.parse(inputLine);
			
			// check WaitingQueues
			if (transManager.waitingQueueList.size() > 0) {
				// check to see if the transactions are still blocked
			}
			
			for (ParsedInstrEnty i : instructionList)
			{
				// process each instruction sequencially
				transManager.process(i);
			}			
		} while (!inputLine.equalsIgnoreCase("exit")); 
	}

	/**
	 * Initialize the variable lists at the sites
	 */
	public void initSites() {
		// init sites
		for (int i = 0; i<10; i++)
		{
			Site s = new Site();
			String[] variables = {"x1", "x2", "x4", "x6"};
			String[] uniqueVariables = {"x1"};
			s.initResources(variables, uniqueVariables);
			siteList.add(s);
		}
	}

	/**
	 * Process method that processes the current instruction.
	 * @param: the instruction
	 */
	public void process(ParsedInstrEnty i) {
		switch(i.opcode) {
			case Begin:
				// update trans table
				Transaction t = new Transaction();
				t.transID = i.transactionId;
				t.timestamp = currentTimestamp;
				t.status = 1;
				t.readOnly = false;
				
				transTable.addTransaction(t);
				break;
			case BeginRO:
				// update trans table
				Transaction tro = new Transaction();
				tro.transID = i.transactionId;
				tro.timestamp = currentTimestamp;
				tro.status = 1;
				tro.readOnly = true;
				
				transTable.addTransaction(tro);
				break;
			case End:
				// Two-phase commit:
				// send message to all sites, get receipts
				
				// if all are good to go, send message to commit
				
				// update trans table
				break;
			case Dump:
				// send message to applicable sites
				siteList.get(0).query();
				break;
			case Fail:
				// send message to applicable site
				siteList.get(0).fail();
				break;
			case Recover:
				// send message to applicable site
				siteList.get(0).recover();
			case Read:
			case Write:
				// send message to applicable sites
				// recieve receipt
				siteList.get(0).setBuffer(i.toString());
				String response = siteList.get(0).process();
				
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
		List<ParsedInstrEnty> instrList = new ArrayList<ParsedInstrEnty>();
		
		instrList.add(new ParsedInstrEnty(OpCode.Begin, 1, null, null, "begin(T1)"));
		instrList.add(new ParsedInstrEnty(OpCode.Begin, 2, null, null, "begin(T2)"));
		
		return null;
	}
}
