package nyu.ads.conctrl.tm;

import java.util.*;
import nyu.ads.conctrl.entity.*;

import nyu.ads.conctrl.tm.entity.Transaction;

/**
 * Transaction Table class
 * 
 * Holds starting timestamp, current status, and name
 * of all transactions.
 * 
 * @author Matt Sarnak
 *
 */
public class TransactionTable {
	public List<Transaction> TransactionList;
	
	/**
	 * Default constructor
	 */
	public TransactionTable() {
		TransactionList = new ArrayList<Transaction>();
	}
	
	/**
	 * Returns whether the transaction is held or not
	 * @param transID
	 * @return
	 */
	public boolean containsTransaction(int transID)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == transID) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a transaction to the table
	 * @param trans
	 */
	public void addTransaction(Transaction t) {
		TransactionList.add(t);
	}
	
	/**
	 * Update the status of a transaction
	 * @param transID
	 * @param status
	 */
	public void updateTransactionStatus(int transID, int status)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == transID) {
				t.status = status;
			}
		}
	}
	
	/**
	 * Get the timestamp of a transaction
	 * @param transID
	 * @return timestamp
	 */
	public TimeStamp getTimestamp(int number)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == number) {
				return t.timestamp;
			}
		}
		return null;
	}
	
	/**
	 * Get the status of a transaction
	 * @param transID
	 * @return timestamp
	 */
	public int getStatus(int transId)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == transId) {
				return t.status;
			}
		}
		return -1;
	}
	
	/**
	 * Get the reason that the transaction failed
	 * @param transId
	 * @return
	 */
	public String getReason(int transId)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == transId) {
				return t.failReason;
			}
		}
		return "";
	}
	
	/**
	 * Set the status for a transaction
	 * @param transId
	 * @param status
	 */
	public void setStatus(int transId, int status)
	{
		for (Transaction t: TransactionList) {
			if(t.transID == transId) {
				t.status = status;
			}
		}
	}
	
	/**
	 * Set the failure reason for the transaction
	 * @param transId
	 * @param reason
	 */
	public void setFailReason(int transId, String reason)
	{
		for (Transaction t: TransactionList) {
			if(t.transID == transId) {
				t.failReason = reason;
			}
		}
	}
	
}
