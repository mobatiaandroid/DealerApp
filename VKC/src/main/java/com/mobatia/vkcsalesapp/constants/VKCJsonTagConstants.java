/**
 * 
 */
package com.mobatia.vkcsalesapp.constants;

/**
 * @author mobatia-user
 * 
 */
public interface VKCJsonTagConstants {

	/* Settings Api Constants */

	public String JSON_TAG_SETTINGS_RESPONSE = "response";
	public String JSON_TAG_SETTINGS_CATEGORY = "category";
	public String JSON_TAG_SETTINGS_OFFER = "offer";
	public String JSON_TAG_SETTINGS_TYPE = "type";
	public String JSON_TAG_SETTINGS_SIZE = "size";
	public String JSON_TAG_SETTINGS_COLOR = "color";
	public String JSON_TAG_SETTINGS_PRICE = "price_range";
	public String JSON_TAG_SETTINGS_ARRIVALS = "newarrivals";
	public String JSON_TAG_SETTINGS_POPULAR = "popular";
	public String JSON_TAG_SETTINGS_BRAND = "brand_banner";

	public String JSON_SETTINGS_CATEGORYID = "id";
	public String JSON_SETTINGS_CATEGORYNAME = "name";
	public String JSON_SETTINGS_PRODUCTID = "p_id";

	public String JSON_SETTINGS_BRANDID = "id";
	public String JSON_SETTINGS_BRANDNAME = "type_name";

	public String JSON_SETTINGS_SIZEID = "id";
	public String JSON_SETTINGS_SIZENAME = "size_name";

	public String JSON_SETTINGS_COLORID = "id";
	public String JSON_SETTINGS_COLORCODE = "color_code";

	public String JSON_SETTINGS_PRICEID = "id";
	public String JSON_SETTINGS_PRICEFROM = "from";
	public String JSON_SETTINGS_PRICETO = "to";

	public String JSON_SETTINGS_OFFERID = "id";
	public String JSON_SETTINGS_OFFER = "offer";
	public String JSON_SETTINGS_OFFERIMAGE = "img_name";

	public String JSON_SETTINGS_CASEID = "id";
	public String JSON_SETTINGS_CASENAME = "name";

	public String JSON_ARRIVAL_RESPONSE = "product";
	public String JSON_ARRIVAL_BANNER = "banner";
	public String JSON_ARRIVAL_BANNERID = "banner_id";
	public String JSON_ARRIVAL_BANNER_IMAGE = "banner_image";

	public String JSON_BRAND_RESPONSE = "brand_details";
	public String JSON_BRAND_IMAGE = "brand_image";
	public String JSON_BRAND_ID = "id";
	public String JSON_BRAND_BANNERIMAGE = "brand_banner_image";
	public String JSON_BRAND_IMAGENAME = "img_name";

	public String JSON_CATEGORY_ID = "categoryid";
	public String JSON_CATEGORY_NAME = "categoryname";
	public String JSON_CATEGORY_COST = "productcost";
	public String JSON_PRODUCT_ID = "productid";
	public String JSON_PRODUCT_NAME = "productname";
	public String JSON_PRODUCT_QTY = "productquantity";
	public String JSON_PRODUCT_DESCRIPTION = "productdescription";
	public String JSON_PRODUCT_VIEWS = "productviews";
	public String JSON_PRODUCT_OFFER = "productoffer";
	public String JSON_PRODUCT_ORDER = "ordercount";
	public String JSON_PRODUCT_TIMESTAMP = "timestamp";

	public String JSON_PRODUCT_COLOR = "product_color";
	public String JSON_PRODUCT_IMAGE = "product_image";
	public String JSON_PRODUCT_SIZE = "product_size";
	public String JSON_PRODUCT_TYPE = "product_type";
	public String JSON_PRODUCT_CASE = "product_case";

	public String JSON_COLOR_IMAGE = "image_name";
	public String JSON_COLOR_ID = "color_id";

	public String JSON_LOGINREQ_RESPONSE = "response";
	public String JSON_LOGINREQ_SUCCESS = "1";
	public String JSON_LOGINREQ_FAILED = "0";

	public String jSON_LOGINREQ_EMAILEXISTS = "2";
/*-----------------------Login Response-------------------------*/
	public String JSON_LOGIN_RESPONSE = "response";
	public String JSON_LOGIN_SUCCESS = "success";
	public String JSON_LOGIN_FAILED = "failed";
	public String JSON_LOGIN_LOGIN = "login";
	public String JSON_LOGIN_ROLEID = "role_id";
	public String JSON_LOGIN_USERID = "user_id";
	public String JSON_LOGIN_CUSTOMER_ID = "cust_id";
	public String JSON_LOGIN_USERNAME = "user_name";
	public String JSON_LOGIN_ROLENAME = "role_name";
	public String JSON_LOGIN_STATECODE = "state_code";
	public String JSON_LOGIN_STATE_NAME = "state_name";
	public String JSON_LOGIN_DIST_NAME = "dist_name";
	public String JSON_LOGIN_CITY_NAME = "city_name";
	public String JSON_LOGIN_CUSTOMER_NAME = "customer_name";
	public String JSON_LOGIN_DEALER_COUNT = "dealer_count";
	public String JSON_LOGIN_IS_CREDIT = "credit_view";
	public String JSON_FEEDBACK_RESPONSE = "response";
	public String JSON_FEEDBACK_SUCCESS = "1";
	public String JSON_FEEDBACK_FAILED = "0";

	public String JSON_ORDERSTATUS_NO = "OrderNo";
	public String JSON_ORDERSTATUS_QTY = "OrderQty";
	public String JSON_ORDERSTATUS_DATE = "OrderDate";
	public String JSON_ORDERSTATUS_PEN_QTY = "PendingQty";
	public String JSON_ORDERSTATUS_NAME = "CusName";
	public String JSON_ORDERSTATUS_COMPANY = "Company";
	public String JSON_ORDERSTATUS_PRDCT_NAME = "product_name";
	public String JSON_ORDER_ID = "id";
	public String JSON_ORDER_CUS_ID = "CusId";
	public String JSON_PRDT_SAP_ID = "productSapId";
	public String JSON_ORDER_MATRL_NO = "MaterialNo";
	public String JSON_ORDER_MATRL_GRID = "MaterialGrid";
	public String JSON_ORDER_MATRL_DESC = "MaterialDesc";
	public String JSON_ORDER_PRDCT_ID = "product_id";
	public String JSON_ORDER_PRDCT_DESC = "product_description";
}
