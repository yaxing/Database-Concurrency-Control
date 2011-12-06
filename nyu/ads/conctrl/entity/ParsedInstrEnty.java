package nyu.ads.conctrl.entity;

/**
 * An entity to represent the parsed Instruction
 * @author Matt Sarnak
 *
 */
public class ParsedInstrEnty {
	public OpCode opcode;
	public int transactionId;
	public String resource;
	public String value;
	public String originalInstruction;
	public int site;
	
	/**
	 * Constructor with parameters.
	 * @param opcode
	 * @param transactionId
	 * @param resource
	 * @param value
	 * @param originalInstruction
	 */
	public ParsedInstrEnty(OpCode opcode, int transactionId, String resource, String value, String originalInstruction, int site) {
		this.opcode = opcode;
		this.transactionId = transactionId;
		this.resource = resource;
		this.value = value;
		this.originalInstruction = originalInstruction;
		this.site = site;
	}
	
	/**
	 * Default constructor
	 */
	public ParsedInstrEnty() {
		
	}
	
	/**
	 * String representation of the instruction
	 */
	@Override
	public String toString() {
		return originalInstruction;
	}
}
