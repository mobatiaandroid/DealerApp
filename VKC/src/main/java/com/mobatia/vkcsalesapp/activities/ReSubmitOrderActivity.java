package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.OrderDetailAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.DealersShopModel;
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReSubmitOrderActivity extends AppCompatActivity implements
        VKCUrlConstants, OnClickListener {

    private Bundle extras;
    SubDealerOrderDetailModel orderModel;
    TextView productId, caseId, mColour, mDate, mTotalQuantity;
    EditText mQuantity;
    Button btnUpdate;
    static int quantity;
    int status = 1, mTotalQty = 0;
    String orderNumber, dealerName, dealerId;
    ListView listOrders;
    Spinner spinnerDealer;
    ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
    List<String> dealerNameList = new ArrayList<String>();

    protected int getLayoutResourceId() {
        // TODO Auto-generated method stub
        return R.layout.activity_subdealer_order_update;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        orderNumber = extras.getString("orderNumber");
        dealerName = extras.getString("dealerName");
        getSalesOrderDetails(orderNumber);
        initUi();
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Reorder Product");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setActionBar();

        if (extras != null) {

            productId.setText(" " + orderNumber);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initUi() {


        productId = (TextView) findViewById(R.id.textProductName);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        spinnerDealer = (Spinner) findViewById(R.id.spinnerDealer);
        listOrders = (ListView) findViewById(R.id.listOrders);
        mTotalQuantity = (TextView) findViewById(R.id.totalQty);
        getMyDealersApi();
        spinnerDealer.setPrompt(dealerName);
        spinnerDealer.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                // TODO Auto-generated method stub
                dealerId = dealersShopModels.get(pos).getDealerId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        btnUpdate.setOnClickListener(this);

    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void updateOrder() {
        String detail;
        detail = createJson().toString();
        String name[] = {"salesorder"};

        String values[] = {detail, AppPrefenceManager.getUserId(this),
                dealerId, orderNumber};

        final VKCInternetManager manager = new VKCInternetManager(
                SUBMIT_REORDER_URL);
        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(ReSubmitOrderActivity.this, 17);

            }
        });
    }

    private void parseResponse(String resultValue) {
        try {
            JSONArray arrayOrders = null;
            JSONObject jsonObjectresponse = new JSONObject(resultValue);

            String result = jsonObjectresponse.getString("response");
            if (result.equals("1")) {
                VKCUtils.showtoast(ReSubmitOrderActivity.this, 26);
                finish();
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnUpdate:
                updateOrder();
                break;

        }
    }

    private void getMyDealersApi() {
        VKCInternetManager manager = null;

        String name[] = {"subdealer_id"};
        String value[] = {AppPrefenceManager.getUserId(this)};
        manager = new VKCInternetManager(LIST_MY_DEALERS_URL);
        manager.getResponsePOST(ReSubmitOrderActivity.this, name, value,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {

                        parseMyDealerJSON(successResponse);

                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private void parseMyDealerJSON(String successResponse) {
        // TODO Auto-generated method stub

        try {
            // ArrayList<DealersShopModel> dealersShopModels = new
            // ArrayList<DealersShopModel>();
            JSONObject respObj = new JSONObject(successResponse);
            JSONObject response = respObj.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                JSONArray respArray = response.getJSONArray("dealers");
                for (int i = 0; i < respArray.length(); i++) {

                    dealersShopModels
                            .add(parseShop(respArray.getJSONObject(i)));

                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item,
                        dealerNameList);
                spinnerDealer.setAdapter(dataAdapter);
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
        dealersShopModel.setCustomer_id(jsonObject.optString("customer_id"));
        dealersShopModel.setId(jsonObject.optString("id"));
        dealersShopModel.setName(jsonObject.optString("name"));
        dealersShopModel.setPhone(jsonObject.optString("phone"));
        dealersShopModel.setPincode(jsonObject.optString("pincode"));
        dealersShopModel.setState(jsonObject.optString("state"));
        dealersShopModel.setState_name(jsonObject.optString("state_name"));
        dealerNameList.add(jsonObject.optString("name"));
        return dealersShopModel;

    }

    private JSONObject createJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("user_id", AppPrefenceManager.getUserId(this));
            jsonObject.putOpt("dealer_id", dealerId);
            jsonObject.putOpt("order_id", orderNumber);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < AppController.subDealerOrderDetailList.size(); i++) {
            JSONObject object = new JSONObject();
            try {

                object.putOpt("product_id",
                        AppController.subDealerOrderDetailList.get(i)
                                .getProductId());
                object.putOpt("color_id",
                        AppController.subDealerOrderDetailList.get(i)
                                .getColorId());
                object.putOpt("case_id", AppController.subDealerOrderDetailList
                        .get(i).getCaseId());
                object.putOpt("quantity",
                        AppController.subDealerOrderDetailList.get(i)
                                .getQuantity());
                // object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
                jsonArray.put(i, object);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        try {
            jsonObject.put("order_details", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;

    }

    private void getSalesOrderDetails(String ordrNo) {
        AppController.subDealerOrderDetailList.clear();
        String name[] = {"order_id"};
        String values[] = {ordrNo};

        final VKCInternetManager manager = new VKCInternetManager(
                SUBDEALER_ORDER_DETAILS);
        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseResponseOrder(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(ReSubmitOrderActivity.this, 17);

            }
        });
    }

    private void parseResponseOrder(String result) {
        try {

            JSONArray arrayOrders = null;
            JSONObject jsonObjectresponse = new JSONObject(result);
            JSONObject response = jsonObjectresponse.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                arrayOrders = response.optJSONArray("orderdetails");
                for (int i = 0; i < arrayOrders.length(); i++) {
                    JSONObject obj = arrayOrders.getJSONObject(i);
                    SubDealerOrderDetailModel orderModel = new SubDealerOrderDetailModel();
                    orderModel.setCaseId(obj.getString("case_id"));
                    orderModel.setColorId(obj.getString("color_id"));
                    orderModel.setCost(obj.getString("cost"));
                    orderModel.setDescription(obj.getString("description"));
                    orderModel.setName(obj.getString("name"));
                    orderModel.setOrderDate(obj.getString("order_date"));
                    orderModel.setProductId(obj.getString("product_id"));
                    orderModel.setQuantity(obj.getString("quantity"));
                    orderModel.setSapId(obj.getString("sap_id"));
                    orderModel.setDetailid(obj.getString("detailid"));
                    orderModel.setColor_code(obj.getString("color_code"));
                    orderModel.setCaseDetail(obj.getString("caseName"));
                    AppController.subDealerOrderDetailList.add(orderModel);
                    // AppController.TempSubDealerOrderDetailList.add(orderModel);
                }

                OrderDetailAdapter mSalesAdapter = new OrderDetailAdapter(
                        ReSubmitOrderActivity.this);
                listOrders.setAdapter(mSalesAdapter);
                for (int j = 0; j < AppController.subDealerOrderDetailList
                        .size(); j++) {

                    mTotalQty = mTotalQty
                            + Integer
                            .parseInt(AppController.subDealerOrderDetailList
                                    .get(j).getQuantity());
                }

                mTotalQuantity.setText(String.valueOf(mTotalQty));

            }

        } catch (Exception e) {

        }
    }


}
