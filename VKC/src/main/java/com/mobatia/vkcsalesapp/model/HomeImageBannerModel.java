package com.mobatia.vkcsalesapp.model;

public class HomeImageBannerModel extends ParentFilterModel {
	private String mBannerUrl;
	private String mSlideId;

	/**
	 * @return the mBannerUrl
	 */
	public String getBannerUrl() {
		return mBannerUrl;
	}

	/**
	 * @param mBannerUrl the mBannerUrl to set
	 */
	public void setBannerUrl(String mBannerUrl) {
		this.mBannerUrl = mBannerUrl;
	}

	/**
	 * @return the mSlideId
	 */
	public String getSlideId() {
		return mSlideId;
	}

	/**
	 * @param mSlideId the mSlideId to set
	 */
	public void setSlideId(String mSlideId) {
		this.mSlideId = mSlideId;
	}
	

}
