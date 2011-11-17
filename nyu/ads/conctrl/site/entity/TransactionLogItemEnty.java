package nyu.ads.conctrl.site.entity;

/**
 * transaction log item entity
 * 
 * @author Yaxing Chen
 */
public class TransactionLogItemEnty {
	public String resource;
	public String value;
	
	public TransactionLogItemEnty(String res, String value) {
		this.resource = res;
		this.value = value;
	}
}
