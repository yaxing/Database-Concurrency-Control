package nyu.ads.conctrl.tm;

import java.util.*;

public class Transaction {
	private Integer number;
	private Integer timestamp;
	private int status;
	
	public Transaction(Integer number, Integer timestamp, int status) {
		super();
		this.number = number;
		this.timestamp = timestamp;
		this.status = status;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
