/**
 * DataManager class
 * 
 * hold and manage all data on this site
 * 
 * @author Yaxing Chen(N16929794)
 *
 */
package nyu.ads.conctrl.site;

import java.util.*;

import nyu.ads.conctrl.entity.OpCode;
import nyu.ads.conctrl.site.entity.*;

public class DataManager {

	private HashMap<String, String> db;// actual db on this server, "resource"=>"value"
	private String[] uniqueRes; // used when recover, to lock 
	
	private ArrayList<TransactionLogItemEnty> transactionLog; // transaction log: String[5] : 
										// [0]: Transactin No
										// [1]: op (W/R)
										// [2]: source index
										// [3]: operation value
										// [4]: operation result(successful or not): 1/0
	
	private ArrayList<CommitLogItemEnty> commitLog; // commit log: String[4]:
								   // [0]: Transactin No
								   // [1]: op (W/R)
								   // [2]: source index
								   // [3]: operation value
	
	DataManager() {
		this.db = new HashMap<String, String>();
		this.transactionLog = new ArrayList<TransactionLogItemEnty>();
		this.commitLog = new ArrayList<CommitLogItemEnty>();
	}
	
	private void logTransaction(int transacId, OpCode op, String resource, String value, boolean abort) {
		transactionLog.add(new TransactionLogItemEnty(transacId, op, resource, value, abort));
	}
	
	public void newRes(String resFull) {
		this.db.put(resFull, null);
	}
	
	public void setUniqRes(String[] uniqueRes) {
		this.uniqueRes = uniqueRes;
	}
	
	/**
	 * write resource, write into log
	 * @param resId
	 * @return 
	 */
	public void write(int transacId, String res, String value) {
		logTransaction(transacId, OpCode.Write, res, value, false);
	}
	
	public void read(int transacId, String res) {
		logTransaction(transacId, OpCode.Read, res, null, true);
	}
	
	public boolean commitT(int transacId) {
		/*
		 * write db
		 * 
		 * delete corresponding transaction log item
		 */
		
		int count = 0;
		for(TransactionLogItemEnty item : transactionLog) {
			if(item.operation.equals(OpCode.Write)) {
				if(this.db.containsKey(item.resource)) {
					this.db.put(item.resource, item.value);
				}
			}
		}
		for(TransactionLogItemEnty item : transactionLog) {
			commitLog.add(new CommitLogItemEnty(item.transactionId, item.operation, item.resource, item.value));
			transactionLog.remove(count ++);
		}
		return true;
	}
	
	public boolean recover() {
		//recover transactions after server failed and restarted
		// recover from transaction log
		// lock all replicated data as Site id -1
		return true;
	}
}
