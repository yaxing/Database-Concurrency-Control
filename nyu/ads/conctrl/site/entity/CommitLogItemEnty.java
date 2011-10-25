/**
 * commit log item entity
 * 
 * @author Yaxing Chen
 */

package nyu.ads.conctrl.site.entity;

public class CommitLogItemEnty {
	public int transactionId;
	public String operation; //W/R
	public int resourceId;
	public String value;
	
	public CommitLogItemEnty(int tid, String op, int resId, String value) {
		this.transactionId = tid;
		this.operation = op;
		this.resourceId = resId;
		this.value = value;
	}
}
