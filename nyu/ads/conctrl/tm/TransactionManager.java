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
	private TransactionTable transTable;  // List of transactions, statuses, and timestamp of origin
	
	private List<WaitingQueue> waitingQueueList; 
	
	private List<Site> siteList; // list of sites
	
	private List<Resource> varList; // List of variables, locations, and latest timestamp  
	
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
	private void initSites() {
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
	private void process(ParsedInstrEnty i) {
		switch(i.opcode) {
			case Begin:
			case BeginRO:
				// update trans table
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
				String conflict = siteList.get(0).process();
				
				if (!conflict.isEmpty())
				{
					// perform wait-die protocol
					
				}
				
				break;
		}		
	}

	/**
	 * Parse the input string and create an list of instructions
	 * @param input to be parsed
	 * @return the list of instructions
	 */
	private List<ParsedInstrEnty> parse(String input) {
		
		return null;
	}
}
