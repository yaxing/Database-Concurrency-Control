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
	
	public TransactionTable() {
		TransactionList = new ArrayList<Transaction>();
	}
	
	public void addTransaction(Transaction t) {
		TransactionList.add(t);
	}
	
	public void updateTransactionStatus(Integer number, int status)
	{
		for (Transaction t : TransactionList) {
			if (t.getNumber() == number) {
				t.setStatus(status);
			}
		}
	}
	
	public Integer getTimestamp(Integer number)
	{
		for (Transaction t : TransactionList) {
			if (t.getNumber() == number) {
				return t.getTimestamp();
			}
		}
		return null;
	}
	
}
