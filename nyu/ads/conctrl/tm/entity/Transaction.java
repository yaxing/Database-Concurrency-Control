package nyu.ads.conctrl.tm.entity;

import java.util.*;

/**
 * Entity representing a Transaction
 * @author Matt
 *
 */
public class Transaction {
	public int transID;
	public Integer timestamp;
	public int status;
	
	/**
	 * Constructor from fields
	 * @param transID
	 * @param timestamp
	 * @param status
	 */
	public Transaction(int transID, Integer timestamp, int status) {
		super();
		this.transID = transID;
		this.timestamp = timestamp;
		this.status = status;
	}
}
