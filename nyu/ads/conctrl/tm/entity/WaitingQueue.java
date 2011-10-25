package nyu.ads.conctrl.tm.entity;

import java.util.*;

import nyu.ads.conctrl.entity.ParsedInstrEnty;

/**
 * WaitingQueue
 * 
 * A list of instructions of a blocked transaction instructions
 * 
 * @author Matt
 *
 */

public class WaitingQueue {
	public int transId;
	public List<ParsedInstrEnty> waitingQueue;
	
	public WaitingQueue(int t) {
		transId = t;
		waitingQueue = new ArrayList<ParsedInstrEnty>();
	}
}
