package com.mobatia.vkcsalesapp.activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.adapter.SalesOrderActivityAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.customview.CustomTextView;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.fragments.SalesOrderFragment;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.DealersListModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements VKCDbConstants,
        VKCUrlConstants {

    private View mRootView;
    int mDisplayWidth = 0;
    int mDisplayHeight = 0;

    private RelativeLayout mRelDealer;
    private RelativeLayout mRelRetailer;
    private CustomTextView mTextViewDealer;
    private CustomTextView mTextViewRetailer;
    private ListView mDealersListView;
    private SalesOrderActivityAdapter mSalesAdapter;
    private DataBaseManager databaseManager;
    private CartModel cartModel;
    CustomProgressBar pDialog;
    private LinearLayout lnrTableHeaders;
    private ImageView imageViewSubmit, imageSearchCat, imageViewClear;
    private String salesOrderArray;
    private ArrayList<DealersListModel> dealersModel;
    public static Boolean isCart = false;
    private LinearLayout lnrOne, llCategory, llSearch;
    private LinearLayout llTop;
    private TextView txtRefr;
    private TextView txtDate;
    private TextView txtQty;
    private TextView txtValue, txtTotalItem, txtTotalQty;
    private TextView txtName, hintText, txtCartValue, textCreditValue;
    String creditValue;
    TextView labelText;
    private TextView txtPlace, textRetailer;
    EditText edtSearch;
    boolean isClicked = false;
    String testSearch;
    List<String> categories = new ArrayList<String>();
    Spinner spinner;
    private String item, type;
    ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
    Activity mActivity;
    boolean isFirstTime;
    private int tableCount;
    boolean callPending;
    int creditPrice;
    int cartPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_salesorder);
        AppController.isCalledApiOnce = false;
        AppController.isClickedCartItem = false;
        mActivity = this;
        isFirstTime = true;
        callPending = true;
        initialiseUI();


        if (!AppPrefenceManager.getUserType(this).equals("4")) {

            getCreditValue();
            getPendingQuantity();

        } else {

            if (!AppPrefenceManager.getSelectedUserId(mActivity).equals("")) {
                getCreditValue();
                getPendingQuantity();
            } else {
                CustomToast toast = new CustomToast(mActivity);
                toast.show(56);
            }

        }
        // getCartData();
        // getPendingQuantity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                AppController.isClickedCartAdapter = true;
                finish();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void setDisplayParams() {
        DisplayManagerScale displayManagerScale = new DisplayManagerScale(
                mActivity);
        mDisplayHeight = displayManagerScale.getDeviceHeight();
        mDisplayWidth = displayManagerScale.getDeviceWidth();

    }

    private void initialiseUI() {
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("My Cart");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setActionBar();
        setDisplayParams();
        databaseManager = new DataBaseManager(this);
        mRelDealer = (RelativeLayout) findViewById(R.id.rlDealer);
        mRelRetailer = (RelativeLayout) findViewById(R.id.rlRetailer);
        mTextViewDealer = (CustomTextView) findViewById(R.id.textViewDealer);
        mTextViewRetailer = (CustomTextView) findViewById(R.id.textViewRetailer);
        mDealersListView = (ListView) findViewById(R.id.dealersListView);
        lnrTableHeaders = (LinearLayout) findViewById(R.id.ll2);
        imageViewSubmit = (ImageView) findViewById(R.id.imageViewSearch);
        imageViewClear = (ImageView) findViewById(R.id.imageViewClear);
        imageSearchCat = (ImageView) findViewById(R.id.imageViewSearchCat);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        lnrOne = (LinearLayout) findViewById(R.id.lnrOne);
        txtRefr = (TextView) findViewById(R.id.txtReferenceNumber);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTotalItem = (TextView) findViewById(R.id.textTotalItem);
        txtTotalQty = (TextView) findViewById(R.id.textTotalQty);
        txtCartValue = (TextView) findViewById(R.id.textCartValueCart);
        hintText = (TextView) findViewById(R.id.hintText);
        edtSearch = (EditText) findViewById(R.id.editSearch);
        textRetailer = (TextView) findViewById(R.id.textRetailer);
        llCategory = (LinearLayout) findViewById(R.id.secCatLL);
        llSearch = (LinearLayout) findViewById(R.id.secSearchLL);
        labelText = (TextView) findViewById(R.id.textView1);
        edtSearch.setText("");

        textCreditValue = (TextView) findViewById(R.id.textCreditValueCart);
        spinner = (Spinner) findViewById(R.id.spinner);
        imageViewSubmit.setEnabled(true);
        mDealersListView.setVisibility(View.VISIBLE);

        spinner.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                isClicked = true;
                return false;
            }
        });
        imageViewClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DeleteAlert appDeleteDialog = new DeleteAlert(mActivity);

                appDeleteDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                appDeleteDialog.setCancelable(true);
                appDeleteDialog.show();
            }
        });
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                item = arg0.getItemAtPosition(position).toString();
                if (isClicked) {
                    if (item.equals("Dealer")) {
                        type = "1";
                        AppPrefenceManager.saveCustomerCategory(mActivity, "1");
                        labelText.setText("Dealer : ");
                        mTextViewDealer.setText("Dealer Name");
                    } else if (item.equals("Sub Dealer")) {
                        type = "2";
                        AppPrefenceManager.saveCustomerCategory(mActivity, "2");
                        labelText.setText("Sub Dealer : ");
                        mTextViewDealer.setText("Sub Dealer Name");
                    }
                    isFirstTime = false;
                }
                isClicked = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        mTextViewDealer.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (isClicked) {
                    clearDb();
                    getCartData();
                    txtQty.setText("Total quantity :  " + "" + 0);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate.setText("Date :  " + formattedDate);
        txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText("Name : "
                + AppPrefenceManager.getLoginCustomerName(this));
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtPlace.setText("Place : " + AppPrefenceManager.getLoginPlace(this));
        txtQty = (TextView) findViewById(R.id.txtTotalQty);
        txtValue = (TextView) findViewById(R.id.txtTotalValue);
        dealersModel = new ArrayList<DealersListModel>();
        getCartData();
        setCartQuantity();
        categories.clear();
        categories.add("Please Select");
        categories.add("Dealer");
        categories.add("Sub Dealer");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (AppPrefenceManager.getCustomerCategory(mActivity).equals("1")) {
            spinner.setSelection(1);
            labelText.setText("Dealer : ");
            mTextViewDealer.setText(AppPrefenceManager
                    .getSelectedUserName(mActivity));
        } else if (AppPrefenceManager.getCustomerCategory(mActivity)
                .equals("2")) {
            spinner.setSelection(2);
            labelText.setText("Sub Dealer : ");
            mTextViewDealer.setText(AppPrefenceManager
                    .getSelectedUserName(mActivity));
        } else {
            spinner.setSelection(0);
        }
        if (AppPrefenceManager.getUserType(mActivity).equals("6")) { // UserType:Dealer
            mRelDealer.setClickable(false);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedDealerId = "";
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewDealer.setText(AppPrefenceManager.getUserName(mActivity));
            llTop.setVisibility(View.GONE);
            lnrOne.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.GONE);
        } else if (AppPrefenceManager.getUserType(mActivity).equals("5")) { // UserType:Retailer
            llCategory.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);

            mRelDealer.setClickable(true);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewRetailer
                    .setText(AppPrefenceManager.getUserName(mActivity));
            mRelDealer.setOnClickListener(new OnClickView());
            llCategory.setVisibility(View.GONE);

        } else if (AppPrefenceManager.getUserType(this).equals("7")) { // UserType:Sub
            llCategory.setVisibility(View.GONE); // Dealer
            mRelRetailer.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);
            hintText.setVisibility(View.GONE);
            textRetailer.setVisibility(View.GONE);
            mTextViewDealer.setText("Select Dealer");
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mRelDealer.setClickable(true);
            mRelDealer.setOnClickListener(new OnClickView());
        } else if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
            // Sales Head
            llSearch.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.VISIBLE);
            mRelRetailer.setVisibility(View.GONE);
            textRetailer.setVisibility(View.GONE);

            VKCObjectConstants.selectedRetailerId = "";

        } else {
            mRelDealer.setClickable(true);
            llCategory.setVisibility(View.VISIBLE);
            llSearch.setVisibility(View.VISIBLE);
            VKCObjectConstants.selectedSubDealerId = "";
            llCategory.setOnClickListener(new OnClickView());
        }

        imageViewSubmit.setOnClickListener(new OnClickView());
        imageSearchCat.setOnClickListener(new OnClickView());

    }

    public void setActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void getCartData() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE, "pid", "sapid", "catid"};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");

        if (cursor.getCount() > 0) {
            txtTotalItem.setText("Total Items : "
                    + String.valueOf(cursor.getCount()));
            while (!cursor.isAfterLast()) {
                AppController.cartArrayList.add(setCartModel(cursor));

                cursor.moveToNext();
            }
            if (AppController.cartArrayList.size() > 0) {
                lnrTableHeaders.setVisibility(View.VISIBLE);
                mDealersListView.setVisibility(View.VISIBLE);
                int totalQty = 0;
                for (int i = 0; i < AppController.cartArrayList.size(); i++) {
                    totalQty = totalQty
                            + Integer.parseInt(AppController.cartArrayList.get(
                            i).getProdQuantity());
                }
                mSalesAdapter = new SalesOrderActivityAdapter(
                        CartActivity.this, AppController.cartArrayList,
                        lnrTableHeaders, txtTotalQty, txtTotalItem,
                        txtCartValue);
                mSalesAdapter.notifyDataSetChanged();
                mDealersListView.setAdapter(mSalesAdapter);
                mDealersListView.setSelection(AppController.listScrollTo);

                txtQty.setText("Total quantity :  " + "" + totalQty);
                // setCartQuantity();
            } else {
                txtQty.setText("Total quantity :  " + "" + 0);
                lnrTableHeaders.setVisibility(View.GONE);
                mDealersListView.setVisibility(View.GONE);
            }

            SalesOrderFragment.isCart = true;
        } else {

            txtTotalItem.setText("Total Items : " + 0);
            VKCUtils.showtoast(CartActivity.this, 9);
            SalesOrderFragment.isCart = false;
            lnrTableHeaders.setVisibility(View.GONE);
            mDealersListView.setVisibility(View.GONE);

        }

    }

    private JSONObject createJson() {
        getCartData();
        System.out.println("18022015:Within createJson ");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("user_id",
                    AppPrefenceManager.getUserId(mActivity));
            if (AppPrefenceManager.getUserType(mActivity).equals("")) {
                jsonObject.putOpt("state_code", "");
            } else {
                jsonObject.putOpt("state_code",
                        AppPrefenceManager.getStateCode(mActivity));
            }
            if (!AppPrefenceManager.getUserType(mActivity).equals("4")) {
                jsonObject.putOpt("dealer_id",
                        VKCObjectConstants.selectedDealerId);
                jsonObject.putOpt("retailer_id",
                        VKCObjectConstants.selectedRetailerId);
                jsonObject.putOpt("subdealer_id",
                        VKCObjectConstants.selectedSubDealerId);
            } else {
                if (AppPrefenceManager.getCustomerCategory(mActivity).equals(
                        "1"))
                    jsonObject.putOpt("dealer_id",
                            AppPrefenceManager.getSelectedUserId(mActivity));
                else
                    jsonObject.putOpt("dealer_id",
                            VKCObjectConstants.selectedDealerId);

                jsonObject.putOpt("retailer_id",
                        VKCObjectConstants.selectedRetailerId);

                if (AppPrefenceManager.getCustomerCategory(mActivity).equals(
                        "2"))
                    jsonObject.putOpt("subdealer_id",
                            AppPrefenceManager.getSelectedUserId(mActivity));
                else

                    jsonObject.putOpt("subdealer_id",
                            VKCObjectConstants.selectedSubDealerId);
            }

            jsonObject
                    .putOpt("user_type", AppPrefenceManager.getUserType(this));
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < AppController.cartArrayList.size(); i++) {
            JSONObject object = new JSONObject();
            try {

                object.putOpt("product_id", AppController.cartArrayList.get(i)
                        .getProdName());
                object.putOpt("category_id", AppController.cartArrayList.get(i)
                        .getCatId());
                object.putOpt("case_id", AppController.cartArrayList.get(i)
                        .getProdSizeId());
                object.putOpt("color_id", AppController.cartArrayList.get(i)
                        .getProdColorId());
                object.putOpt("quantity", AppController.cartArrayList.get(i)
                        .getProdQuantity());
                jsonArray.put(i, object);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

            // salesOrderArray=jsonArray.toString();

        }

        try {
            jsonObject.put("order_details", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return jsonObject;

    }

    /**
     * Method to set cart model
     */
    private CartModel setCartModel(Cursor cursor) {
        cartModel = new CartModel();
        cartModel.setProdId(cursor.getString(0));
        cartModel.setProdName(cursor.getString(1));
        cartModel.setProdSizeId(cursor.getString(2));
        cartModel.setProdSize(cursor.getString(3));
        cartModel.setProdColorId(cursor.getString(4));
        cartModel.setProdColor(cursor.getString(5));
        cartModel.setProdQuantity(cursor.getString(6));
        cartModel.setProdGridValue(cursor.getString(7));
        cartModel.setPid(cursor.getString(8));
        cartModel.setSapId(cursor.getString(9));
        cartModel.setCatId(cursor.getString(10));
        // cartModel.setStatus(cursor.getString(11));
        return cartModel;
    }

    /**
     * Post Api to submit sales order
     */
    public void submitSalesOrder() {
        imageViewSubmit.setEnabled(false);
        String name[] = {"salesorder"};
        String values[] = {createJson().toString()};
        pDialog = new CustomProgressBar(CartActivity.this, R.drawable.loading);
        pDialog.show();
        final VKCInternetManager manager = new VKCInternetManager(
                PRODUCT_SALESORDER_SUBMISSION);

        manager.getResponsePOST(mActivity, name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub

                        AppPrefenceManager
                                .setIsCallPendingAPI(mActivity, false);
                    }
                });
    }

    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseObj = jsonObject.getString("response");
            if (responseObj.equals("1")) {
                pDialog.dismiss();
                VKCUtils.showtoast(CartActivity.this, 15);
                clearDb();
                setCartQuantity();
                AppPrefenceManager.setFillTable(mActivity, "false");
                AppController.cartArrayList.clear();
                AppController.cartArrayListSelected.clear();
                AppPrefenceManager.setIsCallPendingAPI(mActivity, true);
                AppPrefenceManager.setDate(this, "");
                getCreditValue();
                mActivity.finish();
            } else {
                pDialog.dismiss();
                VKCUtils.showtoast(CartActivity.this, 13);
            }
        } catch (Exception e) {

        }
    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

    public void clearOrderDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_ORDERLIST);
    }

	/*
	 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
	 */
    public Boolean doUserCheck() {

        if (AppPrefenceManager.getUserType(mActivity).equals("4")) { // Saleshead

            if (!(mTextViewDealer.getText().toString().equals(""))// &&
                // !(mTextViewRetailer.getText().toString().equals(""))
                    ) {

                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("5")) { // Retailer
            if (!(mTextViewDealer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("6")) { // Dealer
            if ((mTextViewRetailer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("7")) { // Sub
            // Dealer
            if (!(mTextViewDealer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        }


        return null;

    }

    public class OnClickView implements OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == imageViewSubmit) {

                if (AppController.cartArrayList.size() > 0) {
                    if (doUserCheck()) {
                        if (AppPrefenceManager.getUserType(mActivity).equals(
                                "4")) {
                            if (item.equals("Please Select")) {
                                VKCUtils.showtoast(CartActivity.this, 36);
                            } else {
                                submitSalesOrder();
                            }
                        } else {


                            submitSalesOrder();

                        }
                    } else {

                        VKCUtils.showtoast(CartActivity.this, 16);
                    }
                } else {

                    VKCUtils.showtoast(CartActivity.this, 16);
                }

            }

            if (v == mRelDealer) {
                if (AppPrefenceManager.getUserType(mActivity).equals("7")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SubDealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else if (AppPrefenceManager.getUserType(mActivity)
                        .equals("4")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SalesHead");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "Dealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                }

            }
            if (v == mRelRetailer) {

                Intent intent = new Intent(mActivity,
                        RetailersListViewOnSearch.class);
                intent.putExtra("mType", "Retailer");
                VKCObjectConstants.textRetailer = mTextViewRetailer;
                startActivity(intent);

            }

            if (v == imageSearchCat) {
                testSearch = edtSearch.getText().toString();
                if (testSearch.length() > 0 && item.equals("Please Select")) {
                    CustomToast toast = new CustomToast(CartActivity.this);
                    toast.show(39);
                } else if (testSearch.length() > 0
                        && !item.equals("Please Select")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SalesHead");
                    intent.putExtra("key", testSearch);
                    intent.putExtra("type", type);
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);

                } else {
                    CustomToast toast = new CustomToast(CartActivity.this);
                    toast.show(38);
                }

            }

        }

        private void getMyDealersSalesHeadApi() {
            VKCInternetManager manager = null;
            dealersShopModels.clear();
            // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
            String name[] = {"saleshead_id"};
            String value[] = {AppPrefenceManager.getUserId(mActivity)};
            manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
            manager.getResponsePOST(mActivity, name, value,
                    new ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {

                            parseMyDealerSalesHeadJSON(successResponse);

                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            // TODO Auto-generated method stub

                        }
                    });

        }

        private void parseMyDealerSalesHeadJSON(String successResponse) {
            // TODO Auto-generated method stub

            try {

                JSONObject respObj = new JSONObject(successResponse);
                JSONObject response = respObj.getJSONObject("response");
                String status = response.getString("status");
                if (status.equals("Success")) {
                    JSONArray respArray = response.getJSONArray("dealers");
                    for (int i = 0; i < respArray.length(); i++) {

                        dealersShopModels.add(parseShop(respArray
                                .getJSONObject(i)));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private DealersShopModel parseShop(JSONObject jsonObject) {
            DealersShopModel dealersShopModel = new DealersShopModel();
            dealersShopModel.setAddress(jsonObject.optString("address"));

            dealersShopModel.setCity(jsonObject.optString("city"));
            dealersShopModel.setContact_person(jsonObject
                    .optString("contact_person"));
            dealersShopModel.setDealerId(jsonObject.optString("dealerId"));
            dealersShopModel.setCountry(jsonObject.optString("country"));
            dealersShopModel
                    .setCustomer_id(jsonObject.optString("customer_id"));
            dealersShopModel.setId(jsonObject.optString("id"));
            dealersShopModel.setName(jsonObject.optString("name"));
            dealersShopModel.setPhone(jsonObject.optString("phone"));
            dealersShopModel.setPincode(jsonObject.optString("pincode"));
            dealersShopModel.setState(jsonObject.optString("state"));
            dealersShopModel.setState_name(jsonObject.optString("state_name"));
            return dealersShopModel;

        }
    }

    public void setCartQuantity() {
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        int mCount = 0;
        int cartount = 0;
        if (cursor.moveToFirst()) {
            do {
                String count = cursor.getString(cursor
                        .getColumnIndex("productqty"));
                cartount = Integer.parseInt(count);
                mCount = mCount + cartount;
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        cursor.close();
        txtTotalQty.setText("Total Quantity : " + String.valueOf(mCount));
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        isFirstTime = false;
        getCartData();
        mDealersListView.setSelection(AppController.listScrollTo);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        isFirstTime = false;
        super.onResume();
        // getCartData();

        mDealersListView.setSelection(AppController.listScrollTo);
    }

    public void getPendingQuantity() {
        AppController.cartArrayList.clear();
        String userType = "";
        String cust_id = "";
        if (AppPrefenceManager.getUserType(mActivity).equals("6")) {
            userType = "1";
            cust_id = AppPrefenceManager.getCustomerId(mActivity);
        } else if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
            userType = "1";
            cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
        } else {
            userType = "2";
            cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
        }
        String name[] = {"customerId", "customerType"};

        String values[] = {cust_id, userType};

        pDialog = new CustomProgressBar(this, R.drawable.loading);

        pDialog.show();
        final VKCInternetManager manager = new VKCInternetManager(
                URL_GET_PENDING_ORDER_CART);

        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                // TODO Auto-generated method stub
                parseResponseCart(successResponse);
            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                AppPrefenceManager.setIsCallPendingAPI(mActivity, true);

            }
        });
    }

    private void parseResponseCart(String successResponse) {
        // TODO Auto-generated method stub
        try {
            JSONObject objResponse = new JSONObject(successResponse);
            JSONObject response = objResponse.getJSONObject("response");
            String status = response.optString("status");
            if (status.equals("Success")) {
                pDialog.dismiss();
                AppController.isCalledApiOnce = true;
                clearDb();
                AppPrefenceManager.setIsCallPendingAPI(mActivity, false);
                JSONArray pendingArray = response.optJSONArray("pendingQty");
                if (pendingArray.length() > 0) {
                    clearDb();
                    txtTotalQty.setText("Total Qty. :"
                            + response.optString("tot_qty"));
                    txtCartValue.setText("Cart Value: ₹"
                            + response.optString("cart_value"));
                    txtTotalItem.setText("Total Item : "
                            + response.optString("tot_items"));
                    for (int i = 0; i < pendingArray.length(); i++) {
                        JSONObject objPending = pendingArray.getJSONObject(i);
                        String product_id = objPending.optString("product_id");
                        String size_name = objPending.optString("size_name");
                        String size_id = objPending.optString("size_id");
                        String color_name = objPending.optString("color_name");
                        String color_id = objPending.optString("color_id");
                        String quantity = objPending.optString("quantity");
                        String sapId = objPending.optString("productSapId");
                        String catId = objPending.optString("categoryid");
                        String price = objPending.optString("price");
                        String id = objPending.optString("id");
                        String[][] values = {

                                {PRODUCT_ID, product_id},
                                {PRODUCT_NAME, product_id},
                                {PRODUCT_SIZEID, size_id},
                                {PRODUCT_SIZE, size_name},
                                {PRODUCT_COLORID, color_id},
                                {PRODUCT_COLOR, color_name},
                                {PRODUCT_QUANTITY, quantity},
                                {PRODUCT_GRIDVALUE, ""}, {"pid", id},// {
                                // "pid",
                                // String.valueOf(tableCount
                                // +
                                // 1)
                                // },
                                {"sapid", sapId}, {"catid", catId},};

                        databaseManager
                                .insertIntoDb(TABLE_SHOPPINGCART, values);
                    }

                    Date now = new Date();
                    Date alsoNow = Calendar.getInstance().getTime();
                    String nowAsString = new SimpleDateFormat("yyyy-MM-dd")
                            .format(now);

                    AppPrefenceManager.setDate(this, nowAsString);

                }
                getCartData();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int getCartCount() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                tableCount = tableCount + cursor.getCount();

                cursor.moveToNext();
            }

        }
        return cursor.getCount();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        AppController.isClickedCartAdapter = true;
        finish();

    }

    public void updateCartValue() {
        SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
        mAdapter.openToRead();
        String sumValue = mAdapter.getCartSum();
        if (sumValue == null) {
            cartPrice = 0;
            txtCartValue.setText("Cart Value: ₹" + cartPrice);

        } else {

            cartPrice = Integer.parseInt(sumValue);
            txtCartValue.setText("Cart Value: ₹" + sumValue);
        }
    }

    private void getCreditValue() {
        VKCInternetManager manager = null;
        String cust_id = "";
        if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
            cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
        } else {
            cust_id = AppPrefenceManager.getCustomerId(mActivity);
        }
        String name[] = {"dealerid"};
        String value[] = {cust_id};
        manager = new VKCInternetManager(GET_QUICK_ORDER_CREDIT_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                try {
                    JSONObject responseObj = new JSONObject(successResponse);
                    JSONObject response = responseObj.getJSONObject("response");
                    String status = response.getString("status");
                    if (status.equals("Success")) {
                        creditValue = response.getString("creditvalue");
                        textCreditValue.setText("Order Limit: ₹" + creditValue);
                        creditPrice = Integer.parseInt(creditValue);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) { // TODO


            }
        });

    }

    public class DeleteAlert extends Dialog implements
            android.view.View.OnClickListener, VKCDbConstants {

        public Activity mActivity;

        public DeleteAlert(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_clear_cart);
            init();

        }

        private void init() {

            Button buttonSet = (Button) findViewById(R.id.buttonOk);
            buttonSet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ArrayList<String> listDelete = new ArrayList<>();
                    for (int i = 0; i < AppController.cartArrayList.size(); i++) {
                        listDelete.add(AppController.cartArrayList.get(i)
                                .getPid());
                    }
                    deleteApi(listDelete);
                    clearDb();
                    VKCUtils.showtoast(mActivity, 55);
                    setCartQuantity();
                    getCartData();
                    AppPrefenceManager.setFillTable(mActivity, "false");
                    AppController.cartArrayList.clear();
                    AppController.cartArrayListSelected.clear();
                    mSalesAdapter = new SalesOrderActivityAdapter(
                            CartActivity.this, AppController.cartArrayList,
                            lnrTableHeaders, txtQty, txtTotalQty, txtCartValue);
                    AppPrefenceManager.setDate(mActivity, "");
                    mSalesAdapter.notifyDataSetChanged();
                    mDealersListView.setAdapter(mSalesAdapter);
                    // updateCartValue();
                    dismiss();
                }

            });
            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public void deleteApi(ArrayList<String> listDelete) {
        String name[] = {"ids"};
        String values[] = {listDelete.toString()};
        final VKCInternetManager manager = new VKCInternetManager(
                DELETE_CART_ITEM_API);

        manager.getResponsePOST(mActivity, name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String status = obj.optString("status");
                            if (status.equals("Success")) {

                                txtTotalQty.setText("Total Qty. :"
                                        + obj.optString("tot_qty"));
                                txtCartValue.setText("Cart Value: ₹"
                                        + obj.optString("cart_value"));
                                txtTotalItem.setText("Total Item : "
                                        + obj.optString("tot_items"));
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                    }
                });
    }
}
