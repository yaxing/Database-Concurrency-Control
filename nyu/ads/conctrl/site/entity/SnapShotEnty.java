package nyu.ads.conctrl.site.entity;

import nyu.ads.conctrl.entity.*;
/**
 * Entity of snapshot items of a certain resource
 * @author Yaxing Chen (N16929794)
 */
public class SnapShotEnty {
	public String value;
	public TimeStamp timestamp;
	
	public SnapShotEnty(String value, TimeStamp timestamp) {
		this.value = value;
		this.timestamp = timestamp;
	}
}
