package nyu.ads.conctrl.entity;

import java.sql.*;
/**
 * Time stamp class in our system
 * @author Yaxing Chen
 *
 */
public class TimeStamp extends Timestamp{
	static final long serialVersionUID = 0;
	public TimeStamp() {
		super(System.currentTimeMillis());
	}
	public TimeStamp(long time) {
		super(time);
	}
}
