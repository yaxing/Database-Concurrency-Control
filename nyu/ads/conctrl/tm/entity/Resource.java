package nyu.ads.conctrl.tm.entity;

import java.util.*;

/**
 * Entity representing a resource on a site
 * @author Matt
 *
 */

public class Resource {
	public String name;
	public String value;
	public Date timestamp;
	
	/**
	 * Constructor from fields
	 * @param name
	 * @param value
	 */
	public Resource(String name, String value, Date timestamp) {
		super();
		this.name = name;
		this.value = value;
		this.timestamp = timestamp;
	}	
}
