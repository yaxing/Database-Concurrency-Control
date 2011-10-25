package nyu.ads.conctrl.entity;

public class ConflictRespEnty {
	public int transacHold;
	public int transacReq;
	public String resource;
	
	public ConflictRespEnty(int transacHold, int transacReq, String resource) {
		this.transacHold = transacHold;
		this.transacReq = transacReq;
		this.resource = resource;
	}
}
