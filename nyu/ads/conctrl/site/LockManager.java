/**
 * Class LockManager
 * 
 * @author Yaxing Chen (16929794)
 */
package nyu.ads.conctrl.site;

import java.util.*;
import nyu.ads.conctrl.entity.*;

public class LockManager {
	
	public HashMap<String, Integer> locks;	// current locks: "resource"=>"transaction id"
	
	public HashMap<String, Integer> recoverLocks; // replicated resources that are locked from reading when site recover
													// delete when certain resource is committed written
	
	public LockManager() {
		locks = new HashMap<String, Integer>(); 
	}
	
	/**
	 * request a lock on res for transaction with transacId
	 * @param transacId
	 * @param res
	 * @return String NULL: lock retrieved; "conflict: T1, T2": T1 is conflict with T2, T2 holds the lock
	 */
	public String lock(int transacId, String res) {
		if(locks.containsKey(res)) {
			/*
			 * return conflict commands
			 */
			return null;
		}
		else{
			locks.put(res, transacId);
			return null;
		}
	}
	
	/**
	 * unlock a certain resource
	 * @param res resource name
	 */
	public void unlockRes(String res) {
		locks.remove(res);
	}
	
	/**
	 * unlock all resources locked by a transaction T
	 * @param int transacId
	 */
	public void unlockT(int transacId) {
		/*
		 * traverse lock table, remove related items
		 */
	}
	
	/**
	 * lock certain resource for reading
	 * @param String res resource Name
	 */
	public void recoverLock(String res) {
		/*
		 * add res to recoverLocks table
		 */
	}
	
	/**
	 * clear locks held by a certain transaction
	 * @param transacId
	 */
	public void clearTransacLocks(int transacId) {
		/*
		 * remove all locks of certain transaction 
		 */
	}
	
	/**
	 * clear all lock information when site failed
	 */
	public void clearLocks() {
		locks.clear();
	}
}
