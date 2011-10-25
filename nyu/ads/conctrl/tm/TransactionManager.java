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
	
	private TMProcessor proc; // processor to decide wait-die procedure
	
	private List<WaitingQueue> accumulatedQueueList; 
	
	private List<Site> siteList; // list of sites
	
	private List<Resource> varList; // List of variables, locations, and latest timestamp  
	
	public TransactionManager() {
		transTable = new TransactionTable();
		proc = new TMProcessor();
		siteList = new ArrayList<Site>();
		varList = new ArrayList<Resource>();
	}
	
	public static void main(String[] args) {
		TransactionManager transManager = new TransactionManager();
		
		Scanner inputScanner = new Scanner(System.in);
		inputScanner.useDelimiter(System.getProperty("line.separator"));
		String inputLine = "";
		
		do {
			// get line of user input
			inputLine = inputScanner.next();
			
			// parse input into instruction list
			List<ParsedInstrEnty> instructionList = transManager.parse(inputLine);
			
			for (ParsedInstrEnty i : instructionList)
			{
				// process each instruction sequencially
				transManager.process(i);
			}			
		} while (!inputLine.equalsIgnoreCase("exit")); 
	}

	/**
	 * Process method
	 * Process the current instruction.
	 * 
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
				break;
			case Fail:
				// send message to applicable site
				break;
			case Recover:
				// send message to applicable site
			case Read:
			case Write:
				// send message to applicable sites
				// recieve receipt
				// perform wait-die protocol
				break;
		}		
	}

	/**
	 * 
	 * @param input to be parsed
	 * @return the list of instructions
	 */
	private List<ParsedInstrEnty> parse(String input) {
		
		return null;
	}
}
