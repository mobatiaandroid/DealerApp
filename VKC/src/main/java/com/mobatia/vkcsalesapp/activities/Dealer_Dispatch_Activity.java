package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.DispatchedListAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dealer_Dispatch_Activity extends AppCompatActivity implements
        VKCUrlConstants {
    Activity mActivity;
    Bundle extras;
    ArrayList<SubDealerOrderListModel> subDealerModels;
    ListView mStatusList;
    DispatchedListAdapter adapter;
    String listType, status, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatched_list);
        mStatusList = (ListView) findViewById(R.id.dispatchedList);
        subDealerModels = new ArrayList<SubDealerOrderListModel>();
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                // Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Despatched Orders");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);

        setActionBar();
        extras = getIntent().getExtras();
        if (extras != null) {
            status = extras.getString("order_status");
            title = extras.getString("title");
            if (status.equals("pending")) {
                listType = "pending";
            } else if (status.equals("approved")) {
                listType = "approved";
            } else if (status.equals("reject")) {
                listType = "reject";
            } else if (status.equals("dispatch")) {
                listType = "dispatch";
            }

        }
        getSalesOrderStatus();
        mStatusList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new
                        Intent(Dealer_Dispatch_Activity.this, DispatchedListDetail.class);
                intent.putExtra("order_id", AppController.subDealerModels.get(position).getOrderid());
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
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

    private void getSalesOrderStatus() {
        AppController.subDealerModels.clear();
        String name[] = {"user_id"};
        String values[] = {AppPrefenceManager.getUserId(this)};

        final VKCInternetManager manager = new VKCInternetManager(
                GET_DISPATCH_ORDERS_URL);
        manager.getResponsePOST(this, name, values, new ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(Dealer_Dispatch_Activity.this, 17);

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
                arrayOrders = response.optJSONArray("dispatchorders");
                if (arrayOrders.length() > 0) {

                    for (int i = 0; i < arrayOrders.length(); i++) {
                        SubDealerOrderListModel orderModel = new SubDealerOrderListModel();
                        JSONObject obj = arrayOrders.optJSONObject(i);
                        orderModel.setName(obj.getString("name"));
                        orderModel.setOrderid(obj.getString("orderid"));
                        orderModel.setAddress(obj.getString("city"));
                        orderModel.setPhone(obj.getString("phone"));
                        orderModel.setTotalqty(obj.getString("total_qty"));
                        orderModel.setStatus(obj.getString("status"));
                        orderModel.setOrderDate(obj.getString("order_date"));
                        AppController.subDealerModels.add(orderModel);

                    }

                    adapter = new DispatchedListAdapter(this,
                            AppController.subDealerModels);
                    mStatusList.setAdapter(adapter);
                } else {
                    VKCUtils.showtoast(this, 44);
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
// TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onStop() {
// TODO Auto-generated method stub
        super.onStop();

    }

    @Override
    protected void onRestart() {
// TODO Auto-generated method stub
        super.onRestart();
        getSalesOrderStatus();
    }


}
