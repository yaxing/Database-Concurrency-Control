/**
 * commit log item entity
 * 
 * @author Yaxing Chen
 */

package nyu.ads.conctrl.site.entity;

public class CommitLogItemEnty {
	int transactionId;
	String operation; //W/R
	int resourceId;
	String value;
	
	public CommitLogItemEnty(int id, String op, int resId, String value) {
		this.transactionId = id;
		this.operation = op;
		this.resourceId = resId;
		this.value = value;
	}
}
