/**
 * transaction log item entity
 * 
 * @author Yaxing Chen
 */

package nyu.ads.conctrl.site.entity;

public class TransactionLogItemEnty {
	public int transactionId;
	public String operation; //W/R 
	public int resourceId;
	public String value;
	public boolean abort;
	
	public TransactionLogItemEnty(int id, String op, int resId, String value, boolean abort) {
		this.transactionId = id;
		this.operation = op;
		this.resourceId = resId;
		this.value = value;
		this.abort = abort;
	}
}
