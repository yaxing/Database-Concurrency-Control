package nyu.ads.conctrl.tm.entity;

import java.util.*;

/**
 * Entity representing a Transaction
 * @author Matt
 *
 */
public class Transaction {
	public int transID;
	public Date timestamp;
	public int status;
	public Boolean readOnly;
	public Boolean shouldAbort;
	
	public Transaction() {}
	
	/**
	 * Constructor from fields
	 * @param transID
	 * @param timestamp
	 * @param status
	 * @param readOnly
	 */
	public Transaction(int transID, Date timestamp, int status, Boolean readOnly) {
		super();
		this.transID = transID;
		this.timestamp = timestamp;
		this.status = status;
		this.readOnly = readOnly;
		this.shouldAbort = false;
	}
}
