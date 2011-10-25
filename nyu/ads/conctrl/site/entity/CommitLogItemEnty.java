/**
 * commit log item entity
 * 
 * @author Yaxing Chen
 */

package nyu.ads.conctrl.site.entity;

import nyu.ads.conctrl.entity.*;
public class CommitLogItemEnty {
	public int transactionId;
	public OpCode operation; //W/R
	public String resource;
	public String value;
	
	public CommitLogItemEnty(int tid, OpCode op, String res, String value) {
		this.transactionId = tid;
		this.operation = op;
		this.resource = res;
		this.value = value;
	}
}
