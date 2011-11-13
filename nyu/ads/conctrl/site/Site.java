
package nyu.ads.conctrl.site;

import java.util.ArrayList;

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
		String result = null;
		switch(opcode) {
		case INSTR:
			result = op_instr(msg);
			break;
		case PREPARE_COMMIT:
			result = op_commit_query(Integer.parseInt(msg[1]));
			break;
		case COMMIT:
			result = op_commit(Integer.parseInt(msg[1]));
			break;
		case ABORT:
			result = op_abort(Integer.parseInt(msg[1]));
			break;
		case DUMP:
			String resName = "";
			if(msg.length >= 2) {
				resName = msg[1];
			}
			result = op_dump(resName);
			break;
		case FAIL:
			op_fail();
			break;
		case RECOVER:
			op_recover();
			break;
		case INIT:
			op_init_res(msg);
			break;
		default:
			break;
		}
		System.out.println(result);
		return null;
	}
	
	/**
	 * Fail this site
	 * clear lock manager & data manager
	 * change site status
	 */
	public void op_fail() {
		this.status = 0;
	}
	
	/**
	 * recover this site
	 * change status
	 * recover data through data manager
	 */
	public void op_recover() {
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
	 * resList containing opcode INIT, so res list starts from resList[1]
	 * @param resList
	 */
	public void op_init_res(String[] resList) {
		
	}
	
	public String op_instr(String[] msg) {
		
		return null;
	}
	
	public String op_commit_query(int transactionId) {
		return null;
	}
	
	/**
	 * write log to db,
	 * clear recover locks
	 * @param trasactionId
	 * @return
	 */
	public String op_commit(int trasactionId) {
		return null;
	}
	
	/**
	 * abort certain transaction T
	 * clear all T's info in transaction log, lock table
	 * @param transacId
	 * @return boolean
	 */
	public String op_abort(int transactionId) {
		return null;
	}
	
	public String op_dump(String resName) {
		
		return null;
	}
	/*
	 * test
	 */
	public static void main(String[] args) {
		ArrayList<String> n = new ArrayList<String>();
		n.add("lalala");
		System.out.println(n.toString());
//		
//		Site s = new Site();
//		s.setBuffer("INSTR 1 0 W X1 19");
//		s.process();
	}
}
