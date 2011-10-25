/**
 * transaction log item entity
 * 
 * @author Yaxing Chen
 */

package nyu.ads.conctrl.site.entity;

public class TransactionLogItemEnty {
	int transactionId;
	String operation; //W/R 
	int resourceId;
	String value;
	boolean abort;
	
	public TransactionLogItemEnty(int id, String op, int resId, String value, boolean abort) {
		this.transactionId = id;
		this.operation = op;
		this.resourceId = resId;
		this.value = value;
		this.abort = abort;
	}
}
