/**
 * 
 */
package com.mobatia.vkcsalesapp.model;

/**
 * @author mobatia-user
 * 
 */
public class BrandTypeModel extends ParentFilterModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	private String imgUrl;

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the brandImageUrl
	 */

}
