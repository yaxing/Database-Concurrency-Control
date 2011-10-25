package nyu.ads.conctrl.entity;

public class ParsedInstrEnty {
	public OpCode opcode;
	public int transactionId;
	public String resource;
	public String value;
	
	public ParsedInstrEnty(OpCode opcode, int transactionId, String resource, String value) {
		this.opcode = opcode;
		this.transactionId = transactionId;
		this.resource = resource;
		this.value = value;
	}
	
	public ParsedInstrEnty() {
		
	}
}
