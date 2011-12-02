package nyu.ads.conctrl.entity;

/**
 * An enumeration of the different instruction codes
 * @author Matt
 * @author Yaxing
 */
public enum InstrCode {
	
	/**
	 * INSTR T_NAME TIMSTAMP W/R/RO VAR_NAME VALUE
	 */
	INSTR,
	
	/**
	 * PREPARE_COMMIT T_NAME
	 */
	PREPARE_COMMIT,
	
	/**
	 * COMMIT T_NAME
	 */
	COMMIT,
	
	/**
	 * ABORT T_NAME
	 */
	ABORT,
	
	/**
	 * DUMP [VAR_NAME]
	 */
	DUMP,
	
	FAIL,
	
	/**
	 * RECOVER [COMMITED_TRANSACID1,2,...]
	 * RECOVER 1,3,2,6
	 */
	RECOVER,
	
	/**
	 * execution response EXE_RESP 1/0 [T_NAME_HOLDER] [T_NAME_REQ] [V_NAME:V_VALUE]
	 *@example
	 *successful: 
	 *EXE_RESP 1
	 *
	 *conflict:
	 *EXE_RESP 0 10 2
	 *
	 *read:
	 *EXE_RESP 1 X3:22
	 */
	EXE_RESP,
	
	/**
	 * COMMIT_RESP 1/0 (successful or fail)
	 * 
	 */
	COMMIT_RESP,
	
	/**
	 * DUMP_RESP [V_NAME:V_VALUE]+
	 * @example 
	 * DUMP_RESP X1:0 X2:10 X3:22
	 */
	DUMP_RESP,
	
	/**
	 * INIT [VAR_NAME:V_VALUE:UNIQ]
	 * @example
	 * INIT X1:19 X2:12:UNIQ X6:15
	 */
	INIT,
	
	/**
	 * SNAPSHOT timestamp
	 * 
	 * SNAPSHOT 918361
	 */
	SNAPSHOT
}
