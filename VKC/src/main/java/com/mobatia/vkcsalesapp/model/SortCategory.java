package com.mobatia.vkcsalesapp.model;

import java.util.ArrayList;

public class SortCategory {

	private ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
	private SortCategory sortCategorys[];

	/**
	 * @return the categoryModels
	 */
	public ArrayList<CategoryModel> getCategoryModels() {
		return categoryModels;
	}

	/**
	 * @param categoryModels the categoryModels to set
	 */
	public void setCategoryModels(ArrayList<CategoryModel> categoryModels) {
		this.categoryModels = categoryModels;
	}

	/**
	 * @return the sortCategorys
	 */
	public SortCategory[] getSortCategorys() {
		return sortCategorys;
	}

	/**
	 * @param sortCategorys the sortCategorys to set
	 */
	public void setSortCategorys(SortCategory sortCategorys[]) {
		this.sortCategorys = sortCategorys;
	}

}
