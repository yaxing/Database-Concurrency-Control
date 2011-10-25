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
import nyu.ads.conctrl.site.entity.*;

public class DataManager {

	HashMap<String, String> db;// actual db on this server, "resource"=>"value"
	
	ArrayList<TransactionLogItemEnty> transactionLog; // transaction log: String[5] : 
										// [0]: Transactin No
										// [1]: op (W/R)
										// [2]: source index
										// [3]: operation value
										// [4]: operation result(successful or not): 1/0
	
	ArrayList<CommitLogItemEnty> commitLog; // commit log: String[4]:
								   // [0]: Transactin No
								   // [1]: op (W/R)
								   // [2]: source index
								   // [3]: operation value
	
	DataManager() {
		this.db = new HashMap<String, String>();
		this.transactionLog = new ArrayList<TransactionLogItemEnty>();
		this.commitLog = new ArrayList<CommitLogItemEnty>();
	}
	
	private void logTransaction(int transacId, String op, int resourceId, String value, boolean abort) {
		transactionLog.add(new TransactionLogItemEnty(transacId, op, resourceId, value, abort));
	}
	
	/**
	 * write resource, write into log
	 * @param resId
	 * @return 
	 */
	public void write(int resId, int transacId, String value) {
		logTransaction(transacId, "W", resId, value, false);
	}
	
	public void read(int resId, int transacId, String value) {
		logTransaction(transacId, "R", resId, value, true);
	}
	
	public boolean commitT(int transacId) {
		for(TransactionLogItemEnty item : transactionLog) {
			commitLog.add(new CommitLogItemEnty(item.transactionId, item.operation, item.resourceId, item.value));
		}
		return true;
	}
}
