package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.PendingOrderAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.Pending_Order_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubDealerOrderDetails extends AppCompatActivity implements VKCUrlConstants,
        VKCDbConstants, OnClickListener {
    private ListView mLstView;
    private TextView mOrdrNumbr;
    private TextView mDate, mTotalQuantity;
    private Bundle extras;

    private boolean isError;
    private View mView;
    int status = 1;
    String orderNumber, flag, listType, mStatus, parentOrderId;
    Button btnApprove, btnReject;
    CustomToast toast;
    int position;
    int mTotalQty = 0;
    ArrayList<Pending_Order_Model> listPendingOrder;

    // LinearLayout llPending,llApproved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subdealer_order_details);
        AppController.listErrors.clear();
        initUi();
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                // Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Orders Details");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);

        setActionBar();

        extras = getIntent().getExtras();
        if (extras != null) {
            orderNumber = extras.getString("ordr_no");
            parentOrderId = extras.getString("parent_order_id");
            flag = extras.getString("flag");
            mStatus = extras.getString("status");
            listType = extras.getString("listtype");

            if (mStatus.equals("0")) {
                mOrdrNumbr.setText("Order number : " + orderNumber);
            } else {
                mOrdrNumbr.setText("Order number : " + parentOrderId);

            }
            if (flag.equals("pending") || flag.equals("0")) {
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                AppController.isEditable = true;

            } else {
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                AppController.isEditable = false;
            }
            position = extras.getInt("position");

        }
        mTotalQty = 0;
        getSalesOrderDetails(orderNumber);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                mTotalQty = 0;
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initUi() {

        listPendingOrder = new ArrayList<Pending_Order_Model>();
        toast = new CustomToast(SubDealerOrderDetails.this);
        mLstView = (ListView) findViewById(R.id.salesOrderList);
        mOrdrNumbr = (TextView) findViewById(R.id.txtViewOrder);
        mOrdrNumbr.setVisibility(View.VISIBLE);
        mDate = (TextView) findViewById(R.id.txtViewDate);
        mTotalQuantity = (TextView) findViewById(R.id.totalQty);
        mView = findViewById(R.id.view);
        mView.setVisibility(View.VISIBLE);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnApprove.setOnClickListener(this);
        btnReject.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
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

    private void getSalesOrderDetails(String ordrNo) {
        AppController.TempSubDealerOrderDetailList.clear();
        AppController.subDealerOrderDetailList.clear();
        listPendingOrder.clear();
        String name[] = {"order_id"};
        String values[] = {ordrNo};

        final VKCInternetManager manager = new VKCInternetManager(
                SUBDEALER_NEW_ORDER_DETAILS);
        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SubDealerOrderDetails.this, 17);
                isError = true;
            }
        });
    }

    private void parseResponse(String result) {
        try {

            JSONArray arrayOrders = null;
            JSONObject jsonObjectresponse = new JSONObject(result);
            JSONObject response = jsonObjectresponse.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                arrayOrders = response.optJSONArray("orderdetails");
                for (int i = 0; i < arrayOrders.length(); i++) {
                    JSONObject obj = arrayOrders.getJSONObject(i);
                    Pending_Order_Model orderModel = new Pending_Order_Model();
                    orderModel.setCaseId(obj.getString("case_id"));
                    orderModel.setColorId(obj.getString("color_id"));
                    orderModel.setColor_name(obj.getString("color_name"));
                    orderModel.setOrderDate(obj.getString("order_date"));
                    orderModel.setProductId(obj.getString("product_id"));
                    orderModel.setQuantity(obj.getString("quantity"));
                    orderModel.setOrdered_quantity(obj
                            .getString("order_quantity"));
                    orderModel.setCaseDetail(obj.getString("caseName"));
                    orderModel.setDetailid(obj.getString("detailid"));
                    orderModel.setColor_code(obj.getString("color_code"));
                    orderModel.setApproved_qty(obj
                            .getString("approved_quantity"));
                    listPendingOrder.add(orderModel);
                }

                PendingOrderAdapter mSalesAdapter = new PendingOrderAdapter(
                        SubDealerOrderDetails.this, listPendingOrder);
                mLstView.setAdapter(mSalesAdapter);

                int mTotalQty = 0;
                for (int j = 0; j < listPendingOrder.size(); j++) {

                    mTotalQty = mTotalQty
                            + Integer.parseInt(listPendingOrder.get(j)
                            .getQuantity());
                }
                mTotalQuantity.setText("Total Qty : "
                        + String.valueOf(mTotalQty));

            }

        } catch (Exception e) {
            isError = true;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnApprove:
                if (AppController.status != 2) // Status is used to set the status
                { // of order
                    AppController.status = 1;
                }
                if (AppController.listErrors.size() == 0) {
                    showConfirmDialog();
                } else {

                    CustomToast toast = new CustomToast(this);
                    toast.show(30);
                }

                break;

            case R.id.btnReject:
                AppController.status = 3;
                showConfirmDialog();
                break;
            case R.id.btnDispatch:
                AppController.status = 4;
                showConfirmDialog();
                break;

        }
    }

    private void updateOrder() {
        String detail;
        if (AppController.status == 2) {
            detail = createJson().toString();
        } else {
            detail = "";
        }
        String name[] = {"order_id", "status", "orderDetails"};
        String values[] = {orderNumber, String.valueOf(AppController.status),
                detail};

        final VKCInternetManager manager = new VKCInternetManager(
                SET_ORDER_STATUS_API);
        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseResponseAfterUpdate(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SubDealerOrderDetails.this, 17);
                isError = true;
            }
        });
    }

    private void parseResponseAfterUpdate(String result) {
        try {

            JSONArray arrayOrders = null;
            JSONObject jsonObjectresponse = new JSONObject(result);
            JSONObject response = jsonObjectresponse.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {

                CustomToast toast = new CustomToast(this);
                toast.show(28);

                AppController.subDealerModels.clear();
                AppController.TempSubDealerOrderDetailList.clear();
                AppController.subDealerOrderDetailList.clear();
                finish();
            } else {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            isError = true;
        }
    }

    private JSONObject createJson() {

        System.out.println("18022015:Within createJson ");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("order_id", orderNumber);

            jsonObject.putOpt("status", 2);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < AppController.TempSubDealerOrderDetailList.size(); i++) {
            JSONObject object = new JSONObject();
            try {

                object.putOpt("order_detail_id",
                        AppController.TempSubDealerOrderDetailList.get(i)
                                .getDetailid());
                object.putOpt("product_id",
                        AppController.TempSubDealerOrderDetailList.get(i)
                                .getProductId());
                object.putOpt("new_quantity",
                        AppController.TempSubDealerOrderDetailList.get(i)
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

    private void showConfirmDialog() {
        Confirmation_Dialog appExitDialog = new Confirmation_Dialog(this);
        appExitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        appExitDialog.setCancelable(true);
        appExitDialog.show();

    }

    public class Confirmation_Dialog extends Dialog {

        public Confirmation_Dialog(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.confirm_dialog);
            init();

        }

        private void init() {

            Button buttonSet = (Button) findViewById(R.id.buttonYes);
            buttonSet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    dismiss();
                    updateOrder();

                }
                // alrtDbldr.cancel();

            });
            Button buttonCancel = (Button) findViewById(R.id.buttonNo);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        mTotalQty = 0;
    }

}
