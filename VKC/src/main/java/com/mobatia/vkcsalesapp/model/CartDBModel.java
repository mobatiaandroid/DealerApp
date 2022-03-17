package com.mobatia.vkcsalesapp.model;


public class CartDBModel {
    private static final String TABLE_CART = "shoppingcart";
    private static final String PRODUCT_ID = "productid";
    private static final String PRODUCT_NAME = "productname";
    private static final String SIZE_ID = "sizeid";
    private static final String PRODUCT_SIZE = "productsize";
    private static final String COLOR_ID = "colorid";
    private static final String PRODUCT_COLOR = "productcolor";
    private static final String PRODUCT_QTY = "productqty";
    private static final String GRID_VALUE = "gridvalue";
    private static final String PID = "pid";
    private static final String SAP_ID = "sapid";
    private static final String CAT_ID = "catid";
    private static final String STATUS = "status";
    private static final String PRICE = "price";

    public CartDBModel() {
    }

    public static String getTableCart() {
        return TABLE_CART;
    }

    public static String getProductId() {
        return PRODUCT_ID;
    }

    public static String getProductName() {
        return PRODUCT_NAME;
    }

    public static String getSizeId() {
        return SIZE_ID;
    }

    public static String getProductSize() {
        return PRODUCT_SIZE;
    }

    public static String getColorId() {
        return COLOR_ID;
    }

    public static String getProductColor() {
        return PRODUCT_COLOR;
    }

    public static String getProductQty() {
        return PRODUCT_QTY;
    }

    public static String getGridValue() {
        return GRID_VALUE;
    }

    public static String getPID() {
        return PID;
    }

    public static String getSapId() {
        return SAP_ID;
    }

    public static String getCatId() {
        return CAT_ID;
    }

    public static String getSTATUS() {
        return STATUS;
    }

    public static String getPRICE() {
        return PRICE;
    }
}
