package nyu.ads.conctrl.entity;

import java.sql.*;
/**
 * Time stamp in our system
 * @author Yaxing Chen
 *
 */
public class TimeStamp extends Timestamp{
	static final long serialVersionUID = 0;
	public TimeStamp() {
		super(System.currentTimeMillis());
	}
}
