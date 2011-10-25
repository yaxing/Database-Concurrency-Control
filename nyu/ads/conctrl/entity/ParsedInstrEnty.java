package nyu.ads.conctrl.entity;

/**
 * An entity to represent the parsed Instruction
 * @author Matt
 *
 */
public class ParsedInstrEnty {
	public OpCode opcode;
	public int transactionId;
	public String resource;
	public String value;
	public String originalInstruction;
	
	/**
	 * Constructor with parameters.
	 * @param opcode
	 * @param transactionId
	 * @param resource
	 * @param value
	 * @param originalInstruction
	 */
	public ParsedInstrEnty(OpCode opcode, int transactionId, String resource, String value, String originalInstruction) {
		this.opcode = opcode;
		this.transactionId = transactionId;
		this.resource = resource;
		this.value = value;
		this.originalInstruction = originalInstruction;
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
