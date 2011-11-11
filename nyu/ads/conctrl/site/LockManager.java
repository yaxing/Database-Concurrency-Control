
package nyu.ads.conctrl.site;

import java.util.*;
import nyu.ads.conctrl.entity.*;
/**
 * Class LockManager
 * 
 * @author Yaxing Chen (16929794)
 */
public class LockManager {
	
	private HashMap<String, HashMap<Integer, Integer>> locks; // lock table
													//"resource"=>HashMap<transactionID, lockType>
													//(1: lock exclusive, 0: shared)
	
	private HashMap<String, Integer> recoverLocks; // replicated resources that are locked from reading when site recover
												  // delete when certain resource is committed written
												  // locked as -1
	
	public LockManager() {
		locks = new HashMap<String, HashMap<Integer, Integer>>(); 
		recoverLocks = new HashMap<String, Integer>();
	}
	
	private String conflictRespGen(String res, int transacId) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(InstrCode.EXE_RESP + " 0 ");
		HashMap<Integer, Integer> lockInfo = locks.get(res);
		Set<Map.Entry<Integer, Integer>> entries = lockInfo.entrySet();
		buffer.append("{");
		int counter = 0;
		for(Map.Entry<Integer, Integer> entry : entries) {
			buffer.append(entry.getKey());
			counter ++;
			if(counter < entries.size()) {
				buffer.append(",");
			}
		}
		buffer.append("} ");
		buffer.append(transacId);
		return buffer.toString();
	}
	
	/**
	 * first time lock a resource
	 * @return void
	 */
	private void newLock(int transacId, String res, boolean isExclusive) {
		HashMap<Integer, Integer> lockInfo = new HashMap<Integer, Integer>();
		lockInfo.put(transacId, isExclusive ? 1 : 0);
		locks.put(res, lockInfo);
		return;
	}
	
	/**
	 * request a lock on res for transaction with transacId
	 * @param transacId
	 * @param res
	 * @return String NULL: lock retrieved; "conflict: T1, T2": T1 is conflict with T2, T2 holds the lock
	 */
	public String lock(int transacId, String res, boolean isExclusive) {
		//if there're locks on this resource
		if(locks.containsKey(res)) {
			int lockType = 0;
			HashMap<Integer, Integer> lockInfo = locks.get(res);
			Set<Map.Entry<Integer, Integer>> entries = lockInfo.entrySet();
			for(Map.Entry<Integer, Integer> entry : entries) {
				lockType = entry.getValue();
				break;
			}										 
			if(lockInfo.containsKey(transacId)) {
				if((lockInfo.get(transacId) == 0 && lockInfo.size() == 1) || lockInfo.get(transacId) == 1) {
					lockInfo.put(transacId, isExclusive ? 1:0);
					return null;
				}
				else if(!isExclusive){
					return null;
				}
				else {
					return conflictRespGen(res, transacId);
				}
			}
			else if(lockType == 1 || isExclusive) {
				return conflictRespGen(res, transacId);
			}
			else {
				lockInfo.put(transacId, 0);
				return null;
			}
		}
		else if(recoverLocks.containsKey(res)) {
			if(isExclusive) {
				newLock(transacId, res, isExclusive);
				return null;
			}
			else {
				return InstrCode.EXE_RESP + "-1";
			}
		}
		else {
			newLock(transacId, res, isExclusive);
			return null;
		}
	}
	
	/**
	 * unlock all resources locked by a transaction T
	 * @param int transacId
	 */
	public void unlockTransac(int transacId) {
		Set<Map.Entry<String, HashMap<Integer, Integer>>> entries = locks.entrySet();
		for(Map.Entry<String, HashMap<Integer, Integer>> entry : entries) {
			entry.getValue().remove(entry.getKey());
			if(entry.getValue().isEmpty()) {
				locks.remove(entry.getKey());
			}
		}
	}
	
	/**
	 * lock certain resource for reading
	 * @param String res resource Name
	 */
	public void recoverLock(String res) {
		recoverLocks.put(res, -1);
	}
	
	/**
	 * clear all lock information when site failed
	 */
	public void clearLocks() {
		locks.clear();
		recoverLocks.clear();
	}
	

	
	/**
	 * test main
	 */
	public static void main(String[] args) {
		LockManager lm = new LockManager();
		System.out.println(lm.lock(0, "X1", false));
		System.out.println(lm.lock(1, "X1", false));
		System.out.println(lm.lock(0, "X1", true));
	}
}
