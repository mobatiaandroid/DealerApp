package com.mobatia.vkcsalesapp.model;

import java.util.ArrayList;

public class DealersDistrictModel {
	private String districtName;
	private ArrayList<DealersCityModel > cityModels;
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public ArrayList<DealersCityModel > getCityModels() {
		return cityModels;
	}
	public void setCityModels(ArrayList<DealersCityModel > cityModels) {
		this.cityModels = cityModels;
	}

}
