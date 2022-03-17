package com.mobatia.vkcsalesapp.model;

public class OfferModel extends ParentFilterModel {
	

	
	public String name;
	private String offerBanner;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the offerBanner
	 */
	public String getOfferBanner() {
		return offerBanner;
	}

	/**
	 * @param offerBanner the offerBanner to set
	 */
	public void setOfferBanner(String offerBanner) {
		this.offerBanner = offerBanner;
	}
	
	

}