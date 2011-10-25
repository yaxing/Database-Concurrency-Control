package nyu.ads.conctrl.entity;

/**
 * An enumeration of the different Operation codes
 * @author Matt
 *
 */
public enum OpCode {
	Begin,
	BeginRO,
	End,
	Write,
	Read,
	Dump,
	Fail,
	Recover
}
