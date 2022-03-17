/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.SubDealerDispatchListActivity;
import com.mobatia.vkcsalesapp.activities.SubDealerListByCategory;
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderListAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Archana.S
 * 
 */
public class SubdealerOrderStatusFragment extends Fragment implements
		VKCUrlConstants, VKCJsonTagConstants, OnClickListener {
	Activity mActivity;
	boolean isError = false;
	private View mRootView;
	ListView mStatusList;
	ArrayList<SubDealerOrderListModel> subDealerModels;
	Button buttonNew, buttonPending, buttonRejected, buttonDispatched;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.subdealer_order_status_fragment,
				container, false);
		mActivity = getActivity();
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Order Status");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setHasOptionsMenu(true);
		AppController.isCart = false;
		init(mRootView, savedInstanceState);
		//
		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		buttonNew = (Button) v.findViewById(R.id.btnPending);
		buttonPending = (Button) v.findViewById(R.id.btnApproved);
		buttonRejected = (Button) v.findViewById(R.id.btnRejected);
		buttonDispatched = (Button) v.findViewById(R.id.btnDispatch);
		buttonNew.setOnClickListener(this);
		buttonPending.setOnClickListener(this);
		buttonRejected.setOnClickListener(this);
		buttonDispatched.setOnClickListener(this);
		subDealerModels = new ArrayList<SubDealerOrderListModel>();
		// getSalesOrderStatus();
		mStatusList = (ListView) v.findViewById(R.id.subdealerOrderList);

		mStatusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * SubDealerListByCategory.class);
				 * 
				 * intent.putExtra("ordr_no", salesRepOrderModels.get(position)
				 * .getmOrderNo()); intent.putExtra("ordr_date",
				 * salesRepOrderModels.get(position) .getmOrderDate());
				 * 
				 * String status;
				 * 
				 * intent.putExtra("order_status", subDealerModels.get(position)
				 * .getStatus()); if
				 * (subDealerModels.get(position).getStatus().equals("0")) {
				 * intent.putExtra("title", "Pending Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("1")) {
				 * intent.putExtra("title", "Approved Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("3")) {
				 * intent.putExtra("title", "Rejected Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("4")) {
				 * intent.putExtra("title", "Dispatched Orders"); }
				 * startActivity(intent);
				 */
				/*
				 * String listType = ""; if
				 * (subDealerModels.get(position).getStatus().equals("3")) {
				 * Intent intent2 = new Intent(getActivity(),
				 * ReSubmitOrderActivity.class); intent2.putExtra("orderNumber",
				 * subDealerModels.get(position) .getOrderid());
				 * intent2.putExtra("dealerName", subDealerModels
				 * .get(position).getName()); intent2.putExtra("dealerId",
				 * subDealerModels .get(position).getDealerId());
				 * 
				 * startActivity(intent2);
				 * 
				 * } else { Intent intent1 = new Intent(getActivity(),
				 * CategoryOrderListDetails.class); if
				 * (subDealerModels.get(position).getStatus().equals("0")) {
				 * intent1.putExtra("title", "Pending Orders");
				 * listType="pending"; } else if
				 * (subDealerModels.get(position).getStatus().equals("1")) {
				 * intent1.putExtra("title", "Approved Orders");
				 * listType="approved"; } else if
				 * (subDealerModels.get(position).getStatus().equals("3")) {
				 * intent1.putExtra("title", "Rejected Orders");
				 * listType="reject"; } else if
				 * (subDealerModels.get(position).getStatus().equals("4")) {
				 * intent1.putExtra("title", "Dispatched Orders");
				 * listType="dispatched"; } intent1.putExtra("ordr_no",
				 * subDealerModels .get(position).getOrderid());
				 * intent1.putExtra("listtype", listType); if
				 * (listType.equals("pending")) { intent1.putExtra("flag",
				 * "pending"); } else if (listType.equals("approved")) {
				 * intent1.putExtra("flag", "approved"); } else if
				 * (listType.equals("reject")) { intent1.putExtra("flag",
				 * "reject"); } else if (listType.equals("dispatched")) {
				 * intent1.putExtra("flag", "dispatched"); }
				 * startActivity(intent1); }
				 */
			}

		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPending:
			Intent intent = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent.putExtra("order_status", "pending");
			intent.putExtra("title", "Pending Orders");
			startActivity(intent);
			break;
		case R.id.btnApproved:
			Intent intent1 = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent1.putExtra("order_status", "approved");
			intent1.putExtra("title", "Approved Orders");
			startActivity(intent1);
			break;
		case R.id.btnRejected:
			Intent intent2 = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent2.putExtra("order_status", "reject");
			intent2.putExtra("title", "Rejected Orders");
			startActivity(intent2);
			break;
		case R.id.btnDispatch:
			Intent intent3 = new Intent(getActivity(),
					SubDealerDispatchListActivity.class);
			intent3.putExtra("order_status", "dispatch");
			intent3.putExtra("title", "Dispatched Orders");
			startActivity(intent3);
			break;

		}
	}

	private void getSalesOrderStatus() {
		subDealerModels.clear();
		String name[] = { "user_id" };
		String values[] = { AppPrefenceManager.getUserId(getActivity()) };

		final VKCInternetManager manager = new VKCInternetManager(
				GET_SUBDEALER_RECENT_ORDERS);

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
						// Log.v("LOG", "19022015 success" + successResponse);
						parseResponse(successResponse);

						/*
						 * if (subDealerModels.size() > 0) { //setAdapter(); }
						 * else { VKCUtils.showtoast(getActivity(), 17); }
						 */
					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);

					}
				});
	}

	/*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */

	private void parseResponse(String result) {
		try {
			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			JSONObject response = jsonObjectresponse.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {

				/* if (response.has("orders")) { */
				arrayOrders = response.optJSONArray("orders");
				// }

				// int len = arrayOrders.length();

				for (int i = 0; i < arrayOrders.length(); i++) {
					SubDealerOrderListModel orderModel = new SubDealerOrderListModel();
					JSONObject obj = arrayOrders.optJSONObject(i);
					orderModel.setName(obj.getString("name"));
					orderModel.setOrderid(obj.getString("orderid"));
					orderModel.setAddress(obj.getString("city"));
					orderModel.setPhone(obj.getString("phone"));
					orderModel.setTotalqty(obj.getString("total_qty"));
					orderModel.setOrderDate(obj.getString("order_date"));

					orderModel.setStatus(obj.getString("status"));
					subDealerModels.add(orderModel);

				}

				SubDealerOrderListAdapter adapter = new SubDealerOrderListAdapter(
						getActivity(), subDealerModels);
				// adapter.notifyDataSetChanged();
				mStatusList.setAdapter(adapter);

			}

		} catch (Exception e) {

		}
	}
}
