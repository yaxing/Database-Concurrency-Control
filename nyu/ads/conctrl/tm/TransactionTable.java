package nyu.ads.conctrl.tm;

import java.util.*;

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
	private List<Transaction> TransactionList;
	
	/**
	 * Default constructor
	 */
	public TransactionTable() {
		TransactionList = new ArrayList<Transaction>();
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
	public int getTimestamp(int number)
	{
		for (Transaction t : TransactionList) {
			if (t.transID == number) {
				return t.timestamp;
			}
		}
		return -1;
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
	
}
