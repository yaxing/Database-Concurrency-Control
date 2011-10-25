/**
 * Class Site
 * 
 * @author Yaxing Chen (N16929794)
 *
 */
package nyu.ads.conctrl.site;

import nyu.ads.conctrl.entity.*;

import nyu.ads.conctrl.site.entity.*;

public class Site{
	
	private ParsedInstrEnty buffer;// message buffer, containing instructions for different transactions
	
	private LockManager lockMng;// lock manager obj, to handle locks in a certain site
	
	private DataManager dataMng;// data manager obj, to manage all data in this site, including r/w operations
	
	private int status = 1; //0: failed, 1: running
	
	/**
	 * site processor
	 * 
	 * control logic
	 */
	public ConflictRespEnty process() {
		/*
		 * 1. execute:
		 * 
		 *   W: 
		 *   1) lockMng.lock()
		 *   2) if success, dataMng.write()
		 *   
		 *   
		 */
		return null;
	}
	
	public void fail() {
		/*
		 * clear lock manager, data manager
		 */
		this.status = 0;
	}
	
	public void startup() {
		this.status = 1;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public SiteQueryEnty query() {
		SiteQueryEnty enty = new SiteQueryEnty();
		/*
		 * query code
		 * query lockManager
		 */
		return enty;
	}
	
	public void setBuffer(ParsedInstrEnty instr) {
		this.buffer = instr;
	}
	
	private void clearBuffer() {
		this.buffer = null;
	}
	
	public boolean abortT(int transacId) {
		return true;
	}
	
	public void initResources(String[] res) {
		for(String item : res) {
			this.dataMng.newRes(item);
		}
	}
}
