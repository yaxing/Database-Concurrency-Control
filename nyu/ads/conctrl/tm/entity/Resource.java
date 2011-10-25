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
	
	/**
	 * Constructor from fields
	 * @param name
	 * @param value
	 */
	public Resource(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}	
}
