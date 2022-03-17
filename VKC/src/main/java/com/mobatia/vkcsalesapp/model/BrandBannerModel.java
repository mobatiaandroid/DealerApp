package com.mobatia.vkcsalesapp.model;

public class BrandBannerModel extends ParentFilterModel{
	private BrandTypeModel typeModel;
	
	private String brandBannerOne;
	private String brandBannerTwo;
	public BrandTypeModel getTypeModel() {
		return typeModel;
	}
	public void setTypeModel(BrandTypeModel typeModel) {
		this.typeModel = typeModel;
	}
	public String getBrandBannerOne() {
		return brandBannerOne;
	}
	public void setBrandBannerOne(String brandBannerOne) {
		this.brandBannerOne = brandBannerOne;
	}
	public String getBrandBannerTwo() {
		return brandBannerTwo;
	}
	public void setBrandBannerTwo(String brandBannerTwo) {
		this.brandBannerTwo = brandBannerTwo;
	}
		

}
