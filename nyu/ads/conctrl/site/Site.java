package nyu.ads.conctrl.site;

import nyu.ads.conctrl.entity.*;

/**
 * Class Site
 * 
 * @author Yaxing Chen (N16929794)
 *
 */
public class Site extends Thread{
	
	private String buffer;// message buffer, containing instructions for different transactions
	
	private Processor proc;// processor obj, to handle instructions
	
	private LockManager lockMng;// lock manager obj, to handle locks in a certain site
	
	private DataManager dataMng;// data manager obj, to manage all data in this site, including r/w operations
	
	private int status = 1; //0: failed, 1: running
	
	@Override
	public void run() {
		
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
	
	public int status() {
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
	
	public void setBuffer(String instr) {
		this.buffer = instr;
	}
	
	private void clearBuffer() {
		this.buffer = null;
	}
	
}
