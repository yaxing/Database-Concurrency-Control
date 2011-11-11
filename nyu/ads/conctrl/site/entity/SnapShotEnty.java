package nyu.ads.conctrl.site.entity;

/**
 * Entity of snapshot items of a certain resource
 * @author Yaxing Chen (N16929794)
 */
public class SnapShotEnty {
	public String value;
	public String timestamp;
	
	public SnapShotEnty(String value, String timestamp) {
		this.value = value;
		this.timestamp = timestamp;
	}
}
