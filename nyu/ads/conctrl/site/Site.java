
package nyu.ads.conctrl.site;

import nyu.ads.conctrl.entity.*;

import nyu.ads.conctrl.site.entity.*;
/**
 * Class Site
 * 
 * @author Yaxing Chen (N16929794)
 *
 */
public class Site{
	
	public String buffer;// message buffer, containing instructions for different transactions
	
	public LockManager lockMng;// lock manager obj, to handle locks in a certain site
	
	public DataManager dataMng;// data manager obj, to manage all data in this site, including r/w operations
	
	public int status = 1; //0: failed, 1: running
	
	/**
	 * site processor,
	 * parse messages from buffer and execute,
	 * require locks from lock manager,
	 * execute W/R operation through data manager,
	 * 
	 * able to distinguish between read-only transaction and normal transaction and process differently
	 * @return String containing execution result that can be parsed by TM
	 */
	public String process() {
		String[] msg = buffer.split(" ");
		InstrCode opcode = InstrCode.valueOf(msg[0]);
		StringBuilder result = new StringBuilder();
		switch(opcode) {
		case INSTR:
			op_instr(msg, result);
			break;
		}
		System.out.println(result.toString());
		return null;
	}
	
	/**
	 * Fail this site
	 * clear lock manager & data manager
	 * change site status
	 */
	public void fail() {
		this.status = 0;
	}
	
	/**
	 * recover this site
	 * change status
	 * recover data through data manager
	 */
	public void recover() {
		this.status = 1;
		this.dataMng.recover();
	}
	
	public int getStatus() {
		return this.status;
	}
	
	/**
	 * dump all information on this site, 
	 * including all locks, resources, status, etc.
	 * encapsulated as an object and return to TM
	 * @return SiteQueryEnty query result obj
	 */
	public SiteQueryEnty query() {
		SiteQueryEnty enty = new SiteQueryEnty();
		/*
		 * query code
		 */
		return enty;
	}
	
	/**
	 * pass message to buffer
	 * @param String instr
	 */
	public void setBuffer(String instr) {
		this.buffer = instr;
	}
	
	/**
	 * abort certain transaction T
	 * clear all T's info in transaction log, lock table
	 * @param transacId
	 * @return boolean
	 */
	public boolean abortT(int transacId) {
		this.lockMng.unlockT(transacId);
		return true;
	}
	
	public void initResources(String[] resFull, String[] resUniq) {
		for(String item : resFull) {
			this.dataMng.newRes(item);
		}
		this.dataMng.setUniqRes(resUniq);
	}
	
	public void op_instr(String[] msg, StringBuilder result) {
		//result.append(msg[0]);
	}
	
	/*
	 * test
	 */
	public static void main(String[] args) {
		Site s = new Site();
		s.setBuffer("INSTR 1 0 W X1 19");
		s.process();
	}
}
