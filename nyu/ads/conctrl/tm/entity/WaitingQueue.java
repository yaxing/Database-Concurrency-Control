package nyu.ads.conctrl.tm.entity;

import java.util.*;

import nyu.ads.conctrl.entity.ParsedInstrEnty;

/**
 * WaitingQueue
 * A list of instructions of a blocked transaction instructions
 * @author Matt
 *
 */

public class WaitingQueue {
	public List<ParsedInstrEnty> waitingQueue;

	/**
	 * Constructor using a transaction ID
	 * @param transID
	 */
	public WaitingQueue() {
		waitingQueue = new ArrayList<ParsedInstrEnty>();
	}
	
	public boolean isBlocked() {
		return !waitingQueue.isEmpty();
	}
	
	public void enqueue(ParsedInstrEnty i)  {
		waitingQueue.add(i);
	}
	
	public ParsedInstrEnty viewFirst() {
		if(this.isBlocked()) {
			return null;
		}
		else {
			return waitingQueue.get(0);
		}
	}
	
	public void dequeue() {
		waitingQueue.remove(0);
	}
	
}
