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
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderListAdapter;
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

public class SubDealerStatusListActivity extends AppCompatActivity implements
        VKCUrlConstants {
    Activity mActivity;
    Bundle extras;
    ArrayList<SubDealerOrderListModel> subDealerModels;
    ListView mStatusList;
    SubDealerOrderListAdapter adapter;
    String listType, status, title, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_salesorderstatus);
        mStatusList = (ListView) findViewById(R.id.salesOrderList);
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

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);

        setActionBar();
        extras = getIntent().getExtras();
        if (extras != null) {
            status = extras.getString("order_status");
            title = extras.getString("title");
            flag = extras.getString("flag");
            if (status.equals("new")) {

                listType = "all";
            } else if (status.equals("pending")) {
                listType = "all";
            }

            textviewTitle.setText(title.toString());
            getSalesOrderStatus(listType);
        }
        mStatusList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(SubDealerStatusListActivity.this,
                        SubDealerOrderDetails.class);
                intent.putExtra("ordr_no",
                        AppController.subDealerModels.get(position)
                                .getOrderid());
                intent.putExtra("listtype", listType);
                if (listType.equals("pending")) {
                    intent.putExtra("flag", "pending");
                } else {
                    intent.putExtra("flag", "");
                }

                startActivity(intent);

            }
        });

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

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        actionBar.setTitle("");
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void getSalesOrderStatus(String listType) {
        AppController.subDealerModels.clear();
        String name[] = {"dealer_id", "list_type"};
        String values[] = {AppPrefenceManager.getUserId(this), listType};// AppPrefenceManager.getCustomerId(this)

        final VKCInternetManager manager = new VKCInternetManager(
                SUBDEALER_ORDER_URL);

        manager.getResponsePOST(this, name, values, new ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SubDealerStatusListActivity.this, 17);

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

                arrayOrders = response.optJSONArray("orders");
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

                adapter = new SubDealerOrderListAdapter(this,
                        AppController.subDealerModels);
                mStatusList.setAdapter(adapter);
                if (AppController.subDealerModels.size() > 0) {

                } else {
                    VKCUtils.showtoast(mActivity, 44);
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
        getSalesOrderStatus(listType);
    }


}
