package com.mobatia.vkcsalesapp.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.DispatchedListDetailAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.DispatchedDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DispatchedListDetail extends AppCompatActivity implements VKCUrlConstants {
	TextView mTextOrderNo;
	ListView mLstView;
	String mTranporterName, mTransporterContact, mOrderId, mOrderDate;
	ArrayList<DispatchedDetailModel> dispatchList;
	Activity mActivity;
	private String parentOrderId;
	private TextView mTotalQuantity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealer_dispatch_detail);
		final ActionBar abar = getSupportActionBar();
		mActivity = this;
		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Order Details");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		setActionBar();
		Intent intent = getIntent();

		mOrderId = intent.getExtras().getString("order_id");
		parentOrderId = intent.getExtras().getString("parent_order_id");
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		dispatchList = new ArrayList<DispatchedDetailModel>();
		mTotalQuantity = (TextView) findViewById(R.id.totalQty);
		mTextOrderNo = (TextView) findViewById(R.id.txtViewOrderDispatch);
		mTextOrderNo.setText("Order no. " + parentOrderId);
		mLstView = (ListView) findViewById(R.id.dispatchOrderList);
		getSalesOrderDetails(mOrderId);
	}

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}

	private void getSalesOrderDetails(String ordrNo) {

		dispatchList.clear();
		String name[] = { "order_id" };
		String values[] = { ordrNo };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBDEALER_NEW_ORDER_DETAILS);
		

		manager.getResponsePOST(this, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				Log.v("LOG", "19022015 success" + successResponse);
				parseResponse(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(DispatchedListDetail.this, 17);
				// isError = true;
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
					DispatchedDetailModel orderModel = new DispatchedDetailModel();

					orderModel.setCaseId(obj.getString("case_id"));
					orderModel.setColorId(obj.getString("color_id"));
					orderModel.setColor_name(obj.getString("color_name"));
					// orderModel.setCost(obj.getString("cost"));
					// orderModel.setDescription(obj.getString("description"));
					// orderModel.setName(obj.getString("name"));
					orderModel.setOrderDate(obj.getString("order_date"));
					orderModel.setProductId(obj.getString("product_id"));
					orderModel.setQuantity(obj.getString("quantity"));
					// orderModel.setSapId(obj.getString("sap_id"));
					orderModel.setCaseDetail(obj.getString("caseName"));
					orderModel.setDetailid(obj.getString("detailid"));
					orderModel.setColor_code(obj.getString("color_code"));
					orderModel.setApproved_qty(obj
							.getString("approved_quantity"));
					dispatchList.add(orderModel);
					/*
					 * sap_id product_id
					 * 
					 * cost description
					 */
				}

				/*
				 * mLstView.setAdapter(new SubDealerOrderDetailsAdapter(
				 * SubDealerOrderDetails.this,
				 * AppController.subDealerOrderDetailList));
				 */
				// getOrderData();

				DispatchedListDetailAdapter mSalesAdapter = new DispatchedListDetailAdapter(
						mActivity, dispatchList);
				mLstView.setAdapter(mSalesAdapter);

				int mTotalQty = 0;
				for (int j = 0; j < dispatchList
						.size(); j++) {

					mTotalQty = mTotalQty
							+ Integer
									.parseInt(dispatchList
											.get(j).getQuantity());
				}

				mTotalQuantity.setText("Total Qty : "
						+ String.valueOf(mTotalQty));

			}

		}

		catch (Exception e) {
			// isError = true;
		}
	}
}
