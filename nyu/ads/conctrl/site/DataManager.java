
package nyu.ads.conctrl.site;

import java.util.*;

import nyu.ads.conctrl.entity.OpCode;
import nyu.ads.conctrl.site.entity.*;
/**
 * DataManager class
 * 
 * hold and manage all data on this site
 * perform data operations, after locks are retrieved by site
 * 
 * @author Yaxing Chen(N16929794)
 *
 */
public class DataManager {

	public HashMap<String, String> db;//stable storage, actual db on this server, resource=>value
	public HashMap<String, String> tmpDb; //tmp storage, containing un-committed data
	public String[] uniqueRes; // used when recover, to lock 
	
	public ArrayList<TransactionLogItemEnty> transactionLog; // transaction log: String[5] : 
										// [0]: Transactin No
										// [1]: op (W/R)
										// [2]: source index
										// [3]: operation value
										// [4]: operation result(successful or not): 1/0
	
	private ArrayList<Integer> commitLog; // commit log: commited transactions
	
	DataManager() {
		this.db = new HashMap<String, String>();
		this.transactionLog = new ArrayList<TransactionLogItemEnty>();
		this.commitLog = new ArrayList<Integer>();
	}
	
	/**
	 * write transaction log
	 * @param transacId
	 * @param op
	 * @param resource
	 * @param value
	 * @param abort
	 */
	private void logTransaction(int transacId, OpCode op, String resource, String value, boolean abort) {
		transactionLog.add(new TransactionLogItemEnty(transacId, op, resource, value, abort));
	}
	
	/**
	 * write commit log
	 * @param transaction id
	 */
	private void logCommit(int transactionId) {
		this.commitLog.add(transactionId);
	}
	
	/**
	 * add new resource when initiating site
	 * @param resFull
	 */
	public void newRes(String resFull) {
		this.db.put(resFull, null);
	}
	
	/**
	 * define which resources are unique on this site
	 * @param uniqueRes
	 */
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
	
	/**
	 * read resource, return read value
	 * @param transacId
	 * @param res
	 * @return String read value
	 */
	public String read(int transacId, String res) {
		logTransaction(transacId, OpCode.Read, res, null, true);
		return this.db.get(res);
	}
	
	/**
	 * commit transaction T
	 * write log data into real db.
	 * write commit Log
	 * clear corresponding recovery locks, if exist
	 * @param transacId
	 * @return
	 */
	public boolean commitT(int transacId) {
		/*
		 * write db
		 * write commit Log
		 * clear corresponding recovery locks, if exist  
		 */
		
		int count = 0;
		for(TransactionLogItemEnty item : transactionLog) {
			if(item.operation.equals(OpCode.Write)) {
				if(this.db.containsKey(item.resource)) {
					this.db.put(item.resource, item.value);
				}
			}
		}
		return true;
	}
	/**
	 * recover transactions after server failed and restarted
	 * recover from transaction log and commit log, recover only committed transactions
	 * require recover lock for all replicated resources
	 * @return boolean
	 */
	public boolean recover() {
		/*
		 * 
		 */
		return true;
	}
	
	/**
	 * return all db resources, that is, committed values
	 * @return String a structured String that can be parsed by TM
	 */
	public String dump() {
		return null;
	}
	
	/**
	 * return designated resource's committed value
	 * @param traget resource name
	 * @return String a structured String that can be parsed by TM
	 */
	public String dump(String traget) {
		return null;
	}
	
	/**
	 * prepare to commit a certain transaction
	 * return true of false to TM
	 * @param transacId
	 * @return
	 */
	public boolean prepareCommitT(int transacId) {
		
		return true;
	}
}
