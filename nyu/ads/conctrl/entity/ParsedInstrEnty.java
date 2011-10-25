package nyu.ads.conctrl.entity;

public class ParsedInstrEnty {
	public String opcode;
	public int transactionId;
	public String resource;
	public String value;
	
	public ParsedInstrEnty(String opcode, int transactionId, String resource, String value) {
		this.opcode = opcode;
		this.transactionId = transactionId;
		this.resource = resource;
		this.value = value;
	}
	
	public ParsedInstrEnty() {
		
	}
}
