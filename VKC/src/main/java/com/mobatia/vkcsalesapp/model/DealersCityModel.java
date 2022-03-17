package com.mobatia.vkcsalesapp.model;

import java.util.ArrayList;

public class DealersCityModel {
	private String cityName;
	private ArrayList<DealersShopModel> dealersShopModels ;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public ArrayList< DealersShopModel > getDealersShopModels() {
		return dealersShopModels;
	}
	public void setDealersShopModels(ArrayList< DealersShopModel > dealersShopModels) {
		this.dealersShopModels = dealersShopModels;
	}

}
