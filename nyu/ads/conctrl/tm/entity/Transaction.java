package nyu.ads.conctrl.tm.entity;

import java.util.*;
import nyu.ads.conctrl.entity.*;

/**
 * Entity representing a Transaction
 * @author Matt
 *
 */
public class Transaction {
	public int transID;
	public TimeStamp timestamp;
	public int status;	// 0 for committed, 1 for running, -1 for aborted
	public Boolean readOnly;
	public Boolean shouldAbort;
	public String failReason;
	
	public Transaction() {}
	
	/**
	 * Constructor from fields
	 * @param transID
	 * @param timestamp
	 * @param status
	 * @param readOnly
	 */
	public Transaction(int transID, TimeStamp timestamp, int status, Boolean readOnly, String failReason) {
		super();
		this.transID = transID;
		this.timestamp = timestamp;
		this.status = status;
		this.readOnly = readOnly;
		this.shouldAbort = false;
		this.failReason = failReason;
	}
}
