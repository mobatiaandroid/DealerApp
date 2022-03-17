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

public class SubDealerListByCategory extends AppCompatActivity implements
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
        subDealerModels = new ArrayList<>();
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
            if (status.equals("pending")) {
                listType = "pending";
            } else if (status.equals("approved")) {
                listType = "approved";
            } else if (status.equals("reject")) {
                listType = "reject";
            } else if (status.equals("dispatch")) {
                listType = "dispatch";
            }
            textviewTitle.setText(title.toString());
            getSalesOrderStatus(listType);
        }
        mStatusList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (status.equals("reject")) {
                    Intent intent = new Intent(SubDealerListByCategory.this,
                            ReSubmitOrderActivity.class);
                    intent.putExtra("orderNumber",
                            AppController.subDealerModels.get(position)
                                    .getOrderid());
                    intent.putExtra("dealerName", AppController.subDealerModels
                            .get(position).getName());
                    intent.putExtra("dealerId", AppController.subDealerModels
                            .get(position).getDealerId());

                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SubDealerListByCategory.this,
                            CategoryOrderListDetails.class);
                    intent.putExtra("ordr_no", AppController.subDealerModels
                            .get(position).getOrderid());
                    intent.putExtra("listtype", listType);
                    if (listType.equals("pending")) {
                        intent.putExtra("flag", "pending");
                    } else if (listType.equals("approved")) {
                        intent.putExtra("flag", "approved");
                    } else if (listType.equals("reject")) {
                        intent.putExtra("flag", "reject");
                    }
                    startActivity(intent);
                }
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true
        );
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void getSalesOrderStatus(String listType) {
        AppController.subDealerModels.clear();
        String name[] = {"subdealer_id", "list_type"};
        String values[] = {AppPrefenceManager.getUserId(this), listType};

        final VKCInternetManager manager = new VKCInternetManager(
                SUBDEALER_ORDER_URL_LIST);

        manager.getResponsePOST(this, name, values, new ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);
            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SubDealerListByCategory.this, 17);

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
                if (arrayOrders.length() > 0) {

                    for (int i = 0; i < arrayOrders.length(); i++) {
                        SubDealerOrderListModel orderModel = new SubDealerOrderListModel();
                        JSONObject obj = arrayOrders.optJSONObject(i);
                        // JSONArray arrayDetail=obj.getJSONArray("orderDetails");
                        orderModel.setName(obj.getString("name"));
                        // System.out.println("Name:"+orderModel.getName());
                        orderModel.setOrderid(obj.getString("orderid"));
                        orderModel.setAddress(obj.getString("city"));
                        orderModel.setPhone(obj.getString("phone"));
                        orderModel.setTotalqty(obj.getString("total_qty"));
                        orderModel.setStatus(obj.getString("status"));
                        AppController.subDealerModels.add(orderModel);

                    }

                    adapter = new SubDealerOrderListAdapter(this,
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
        getSalesOrderStatus(listType);
    }


}
