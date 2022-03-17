package com.mobatia.vkcsalesapp.model;

import java.util.ArrayList;
import java.util.Date;

public class ProductModel extends ParentFilterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String mProductName;

	private String mCategoryId;

	private String mCategoryName;

	String mProductPrize;
	private String mProductquantity;
private String mSapId;
	// /////////////
private String productdescription;
	String mProductOff;

	
	private String mProductDescription;
	private String mProductViews;
	private String mTimeStamp,parent_category;
	
	private int mProductOrder;;

	private Date mDate;
	private String type,size,image_name;

	// ///////////
	private ArrayList<ColorModel> mProductColor;

	private ArrayList<BrandTypeModel> mProductType;

	private ArrayList<SizeModel> mProductSize;
	
	private ArrayList<CaseModel> mProductCases;
	
	private ArrayList<ProductImages> mProductImages;
	private ArrayList<ProductImages> mNewArrivals;

	private ArrayList<PendingQuantityModel> mPendingQty;
	public int getmProductOrder() {
		return mProductOrder;
	}

	public void setmProductOrder(int mProductOrder) {
		this.mProductOrder = mProductOrder;
	}

	public ArrayList<CaseModel> getmProductCases() {
		return mProductCases;
	}

	public void setmProductCases(ArrayList<CaseModel> mProductCases) {
		this.mProductCases = mProductCases;
	}

	

	public String getmProductName() {
		return mProductName;
	}

	public void setmProductName(String mProductName) {
		this.mProductName = mProductName;
	}

	public ArrayList<SizeModel> getmProductSize() {
		return mProductSize;
	}

	public void setmProductSize(ArrayList<SizeModel> mProductSize) {
		this.mProductSize = mProductSize;
	}

	

	public String getmProductPrize() {
		return mProductPrize;
	}

	public void setmProductPrize(String mProductPrize) {
		this.mProductPrize = mProductPrize;
	}

	public String getmProductOff() {
		return mProductOff;
	}

	public void setmProductOff(String mProductOff) {
		this.mProductOff = mProductOff;
	}

	

	/**
	 * @return the mCategoryId
	 */
	public String getCategoryId() {
		return mCategoryId;
	}

	/**
	 * @param mCategoryId
	 *            the mCategoryId to set
	 */
	public void setCategoryId(String mCategoryId) {
		this.mCategoryId = mCategoryId;
	}

	/**
	 * @return the mCategoryName
	 */
	public String getCategoryName() {
		return mCategoryName;
	}

	/**
	 * @param mCategoryName
	 *            the mCategoryName to set
	 */
	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	/**
	 * @return the mProductquantity
	 */
	public String getProductquantity() {
		return mProductquantity;
	}

	/**
	 * @param mProductquantity
	 *            the mProductquantity to set
	 */
	public void setProductquantity(String mProductquantity) {
		this.mProductquantity = mProductquantity;
	}

	/**
	 * @return the mProductColor
	 */
	public ArrayList<ColorModel> getProductColor() {
		return mProductColor;
	}

	/**
	 * @param mProductColor
	 *            the mProductColor to set
	 */
	public void setProductColor(ArrayList<ColorModel> mProductColor) {
		this.mProductColor = mProductColor;
	}

	/**
	 * @return the mProductType
	 */
	public ArrayList<BrandTypeModel> getProductType() {
		return mProductType;
	}

	/**
	 * @param mProductType
	 *            the mProductType to set
	 */
	public void setProductType(ArrayList<BrandTypeModel> mProductType) {
		this.mProductType = mProductType;
	}

	/**
	 * @return the mProductImages
	 */
	public ArrayList<ProductImages> getProductImages() {
		return mProductImages;
	}

	/**
	 * @param mProductImages
	 *            the mProductImages to set
	 */
	public void setProductImages(ArrayList<ProductImages> mProductImages) {
		this.mProductImages = mProductImages;
	}

	public String getProductDescription() {
		return mProductDescription;
	}

	public void setProductDescription(String mProductDescription) {
		this.mProductDescription = mProductDescription;
	}

	public String getProductViews() {
		return mProductViews;
	}

	public void setProductViews(String mProductViews) {
		this.mProductViews = mProductViews;
	}

	public String getTimeStamp() {
		return mTimeStamp;
	}

	public void setTimeStamp(String mTimeStamp) {
		this.mTimeStamp = mTimeStamp;
	}

	public ArrayList<ProductImages> getmNewArrivals() {
		return mNewArrivals;
	}

	public void setmNewArrivals(ArrayList<ProductImages> mNewArrivals) {
		this.mNewArrivals = mNewArrivals;
	}

	public ArrayList<PendingQuantityModel> getmPendingQty() {
		return mPendingQty;
	}

	public void setmPendingQty(ArrayList<PendingQuantityModel> mPendingQty) {
		this.mPendingQty = mPendingQty;
	}

	public String getmSapId() {
		return mSapId;
	}

	public void setmSapId(String mSapId) {
		this.mSapId = mSapId;
	}

	public String getProductdescription() {
		return productdescription;
	}

	public void setProductdescription(String productdescription) {
		this.productdescription = productdescription;
	}
	/*public String getTimeStamp() {
		return mTimeStamp;
	}

	public void setTimeStamp(String mTimeStamp) {
		this.mTimeStamp = mTimeStamp;
	}*/
	
	public Date getTimeStampP() {
		return mDate;
	}

	public void setTimeStampP(Date mTimeStamp) {
		this.mDate = mTimeStamp;
	}

	public String getParent_category() {
		return parent_category;
	}

	public void setParent_category(String parent_category) {
		this.parent_category = parent_category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

}
