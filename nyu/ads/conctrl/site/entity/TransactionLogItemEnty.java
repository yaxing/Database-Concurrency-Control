

package nyu.ads.conctrl.site.entity;

import nyu.ads.conctrl.entity.*;

/**
 * transaction log item entity
 * 
 * @author Yaxing Chen
 */
public class TransactionLogItemEnty {
	public int transactionId;
	public OpCode operation; //W/R 
	public String resource;
	public String value;
	public boolean abort;
	
	public TransactionLogItemEnty(int id, OpCode op, String res, String value, boolean abort) {
		this.transactionId = id;
		this.operation = op;
		this.resource = res;
		this.value = value;
		this.abort = abort;
	}
}
