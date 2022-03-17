/**
 * 
 */
package com.mobatia.vkcsalesapp.model;

/**
 * @author mobatia-user
 *
 */
public class CategoryModel extends ParentFilterModel  {
	
	public String name;
	private String parentId;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
