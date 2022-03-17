/**
 *
 */
package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.SalesOrderDetailAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Vandana Surendranath
 */
public class SalesOrderDetailActivity extends AppCompatActivity implements
        VKCUrlConstants, VKCJsonTagConstants {

    private ListView mLstView;
    private TextView mOrdrNumbr;
    private TextView mDate;
    private Bundle extras;
    private ArrayList<SalesRepOrderModel> salesRepOrderModels;
    private boolean isError;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_salesorderstatus);
        initUi();
        setActionBar();
        extras = getIntent().getExtras();
        if (extras != null) {
            mOrdrNumbr.setText("Order number : " + extras.getString("ordr_no"));
            mDate.setText("Order date : " + extras.getString("ordr_date"));
        }
        getSalesOrderDetails(extras.getString("ordr_no"));
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
        mLstView = (ListView) findViewById(R.id.salesOrderList);
        mOrdrNumbr = (TextView) findViewById(R.id.txtViewOrder);
        mOrdrNumbr.setVisibility(View.VISIBLE);
        mDate = (TextView) findViewById(R.id.txtViewDate);
        mDate.setVisibility(View.VISIBLE);
        mView = findViewById(R.id.view);
        mView.setVisibility(View.VISIBLE);
        salesRepOrderModels = new ArrayList<SalesRepOrderModel>();
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


    private void getSalesOrderDetails(String ordrNo) {
        salesRepOrderModels.clear();
        String name[] = {"OrderNo"};
        String values[] = {ordrNo};

        final VKCInternetManager manager = new VKCInternetManager(
                PRODUCT_SALESORDER_DETAILS);

        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseResponse(successResponse);
                if (salesRepOrderModels.size() > 0 && !isError) {
                    setAdapter();
                } else {
                    VKCUtils.showtoast(SalesOrderDetailActivity.this, 17);
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SalesOrderDetailActivity.this, 17);
                isError = true;
            }
        });
    }

    private void setAdapter() {
        mLstView.setAdapter(new SalesOrderDetailAdapter(salesRepOrderModels,
                this));
    }

    private void parseResponse(String response) {
        try {
            JSONObject jsonObjectresponse = new JSONObject(response);
            JSONArray jsonArrayresponse = jsonObjectresponse
                    .getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                SalesRepOrderModel salesRepOrderModel = new SalesRepOrderModel();
                JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);
                salesRepOrderModel.setmOrderNo(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_NO));
                salesRepOrderModel.setmOrderQty(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_QTY));
                salesRepOrderModel.setmPendingQty(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_PEN_QTY));
                salesRepOrderModel.setmOrderDate(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_DATE));
                salesRepOrderModel.setmCompany(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_COMPANY));
                salesRepOrderModel.setmPrdctName(jsonObjectZero
                        .optString(JSON_ORDERSTATUS_PRDCT_NAME));
                salesRepOrderModel.setmPrdctDesc(jsonObjectZero
                        .optString(JSON_ORDER_PRDCT_DESC));
                salesRepOrderModels.add(salesRepOrderModel);
            }

        } catch (Exception e) {
            isError = true;
        }
    }

}
