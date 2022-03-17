/**
 * 
 */
package com.mobatia.vkcsalesapp.model;

/**
 * @author mobatia-user
 *
 */
public class PriceModel extends ParentFilterModel{
	
	String from_range;
	
	String to_range;
	public String getFrom_range() {
		return from_range;
	}

	public void setFrom_range(String from_range) {
		this.from_range = from_range;
	}

	public String getTo_range() {
		return to_range;
	}

	public void setTo_range(String to_range) {
		this.to_range = to_range;
	}
	

}
