package com.mobatia.vkcsalesapp.constants;

public interface VKCUrlConstants {
    /*
     * Bibin Comment VKC USERS 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
     */
    // Live AWS------------------------------------------------
    public String BASE_URL = "https://mobile.walkaroo.in/vkc/";

    // Dev URL
    //public String BASE_URL = "http://dev.mobatia.com/vkc2.5/";

    /* Settings Api */

    public String SETTINGS_URL = BASE_URL + "apiv2/getsettings";

    /* Popularity count Api */

    public String POPULARITY_COUNT_URL = BASE_URL + "apiv2/popular";

    /* Product Detail Api */

    public String PRODUCT_DETAIL_URL = BASE_URL + "apiv2/getdetails";

    public String PRODUCT_DETAIL_NEW_URL = BASE_URL + "apiv2/getdetails_new";

    public String PRODUCT_DETAIL__PAGINATED_URL = BASE_URL
            + "apiv2/getdetailsPaginated";
    /* Search Api */

    public String SEARCH_PRODUCT_URL = BASE_URL + "apiv2/searchcontent";

    /* Login Req Api */

    public String LOGIN_REQUEST_URL = BASE_URL + "apiv2/signup";

    /* SignIn Api */

    public String LOGIN__URL = BASE_URL + "apiv2/login";

    /* Submit My Dealer */

    public String SUBMIT_MY_DEALER_URL = BASE_URL + "apiv2/addMyDealers";

    /* List My Dealers */

    public String LIST_MY_DEALERS_URL = BASE_URL + "apiv2/listMyDealers";

    /* Salesorder Api */

    public String LIST_MY_DEALERS_SALES_HEAD_URL = BASE_URL
            + "apiv2/listSalesHeadDealers";

    public String PRODUCT_SALESORDER_SUBMISSION = BASE_URL + "apiv2/salesOrder";

    /* SubDealer Order Details */
    public String SUBDEALER_ORDER_DETAILS = BASE_URL + "apiv2/getOrderDetails";

    public String SUBDEALER_ORDER_URL = BASE_URL + "apiv2/getOrders";

    public String SUBDEALER_ORDER_URL_LIST = BASE_URL
            + "apiv2/getSubdealerOrders";
    /* SalesorderStatus Api */

    public String PRODUCT_SALESORDER_STATUS = BASE_URL
            + "apiv2/salesOrderStatus";
    /* Feedback Api */

    public String PRODUCT_FEEDBACK = BASE_URL + "apiv2/feedback";
    /* Complaint Api */

    public String PRODUCT_COMPLAINT = BASE_URL + "apiv2/compliant";

    /* State Api */

    public String DEALERS_GETSTATE = BASE_URL + "apiv2/getstate";
    /* Retailers Api */
    public String DEALERS_GETDISTRICT = BASE_URL + "apiv2/getdistrict";
    public String GET_RETAILERS = BASE_URL + "apiv2/getretailers";
    /* Dealers Api */

    public String GET_DEALERS = BASE_URL + "apiv2/getdealers";

    /* Approve,Reject Order */
    public String SET_ORDER_STATUS_API = BASE_URL + "apiv2/setOrderStatus";

    /* GCMIDregistration Api */

    public String GCM_INITIALISATION = BASE_URL + "apiv2/appinit";

    /* PaymentStatus Api */

    public String GET_PAYMENT_STATUS = BASE_URL + "apiv2/creditstatus";

    public String GET_SALES_ORDER_STATUS = BASE_URL
            + "apiv2/productSalesOrderStatus";

    public String PRODUCT_SALESORDER_DETAILS = BASE_URL
            + "apiv2/salesOrderStatusDetails";

    public String UPDATE_ORDER_STATUS = BASE_URL + "apiv2/setOrderStatus";
    /* Get Subdealer Orders */
    public String GET_SUBDEALER_ORDER_LIST = BASE_URL
            + "apiv2/getSubdealerOrders";
    /* Reorder Product */
    public String SUBMIT_REORDER_URL = BASE_URL + "apiv2/salesReorder";
    /* Like Product API */
    public String LIKE_PRODUCT_URL = BASE_URL + "apiv2/likeproduct";
    /* Get Like Count */
    public String LIKE_COUNT_URL = BASE_URL + "apiv2/getproductlikes";

    /* Recent Orders List */
    public String GET_RECENT_ORDERS = BASE_URL + "apiv2/getRecentOrders";
    /* Sales Head Orders List */
    public String SALES_HEAD_ORDERS_URL = BASE_URL
            + "apiv2/getSalesExecutiveOrders";

    public String URL_ARTICLE_SEARCH_PRODUCT = BASE_URL + "apiv2/searchProduct";

    public String URL_GET_PRODUCT_DETAIL = BASE_URL + "apiv2/getProductDetail?";
    public String URL_GET_PENDING_ORDER_CART = BASE_URL
            + "apiv2/getPendingCartItems";

    public String URL_GET_APP_VERSION = "apiv2/appversion";
    /* Notification List */
    public String NOTIFICATION_LIST_URL = BASE_URL + "apiv2/push";
    public String NOTIFICATION_DELETE_URL = BASE_URL + "apiv2/pushdelete";
    public String GET_ARTICLE_NUMBERS_URL = BASE_URL
            + "apiv2/getarticlenumbers";
    public String GET_PRODUCT_DETAILS_URL = BASE_URL
            + "apiv2/getQuickOrderProductDetails";
    public String GET_DISPATCH_ORDERS_URL = BASE_URL
            + "apiv2/getDispatchorders";
    public String GET_QUICK_ORDER_CREDIT_URL = BASE_URL
            + "apiv2/getcreditvalue";
    public String GET_QUICK_ARTICLE_NO_URL = BASE_URL
            + "apiv2/getquickorderarticlenumbers";
    public String GET_CART_VALUE_URL = BASE_URL + "apiv2/getcartvalue";
    public String GET_CATEGORY_URL = BASE_URL + "apiv2/getcategorylist";
    public String GET_SUBDEALER_RECENT_ORDERS = BASE_URL
            + "apiv2/getRecentOrderDetails";
    public String SUBDEALER_NEW_ORDER_DETAILS = BASE_URL
            + "apiv2/getOrderDetails_new";
    public String GET_DEALER_POINTS = BASE_URL + "apiv2/getLoyalityPoints";
    public String GET_USERS = BASE_URL + "apiv2/getUsers";
    public String ISSUE_POINTS = BASE_URL + "apiv2/issueLoyalityPoints";
    public String TRANSACTION_HISTORY = BASE_URL + "apiv2/transaction_history";
    public String GET_DATA = BASE_URL + "apiv2/fetchUserData";
    public String GET_CUSTOMERS = BASE_URL + "apiv2/getMyCustomers";
    public String GET_REDEEM_LIST = BASE_URL + "apiv2/Redeemed_gifts";
    public String GET_MEMBERS_LIST = BASE_URL + "apiv2/getGroupMembers";
    public String GET_REDEEM_REPORT_APP = BASE_URL + "apiv2/RedeemReportForApp";
    public String GET_GIFT_REWARD_REPORT_APP = BASE_URL
            + "apiv2/GiftRewardReportForApp";
    public String ADD_TO_CART_API = BASE_URL + "apiv2/saveCartitems";
    public String DELETE_CART_ITEM_API = BASE_URL + "apiv2/deleteCartitems";
    public String EDIT_CART_ITEM_API = BASE_URL + "apiv2/editCartitems";

}
