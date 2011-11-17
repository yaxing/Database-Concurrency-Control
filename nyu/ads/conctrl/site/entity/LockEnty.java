package nyu.ads.conctrl.site.entity;

/**
 * A single lock entity
 * this entity represents a single lock on a certain resource
 * @author Yaxing Chen
 *
 */
public class LockEnty {
	public int transacId;
	public LockType type;
	
	public LockEnty(int transacId, LockType type) {
		this.transacId = transacId;
		this.type = type;
	}
}
