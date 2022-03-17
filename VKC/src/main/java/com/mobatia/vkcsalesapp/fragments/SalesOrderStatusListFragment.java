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
import com.mobatia.vkcsalesapp.activities.Dealer_Dispatch_Activity;
import com.mobatia.vkcsalesapp.activities.DispatchedListDetail;
import com.mobatia.vkcsalesapp.activities.Order_Detail_Approved;
import com.mobatia.vkcsalesapp.activities.SubDealerOrderDetails;
import com.mobatia.vkcsalesapp.activities.SubDealerStatusListActivity;
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
public class SalesOrderStatusListFragment extends Fragment implements
		VKCUrlConstants, VKCJsonTagConstants, OnClickListener {
	Activity mActivity;
	boolean isError = false;
	private View mRootView;
	ListView mStatusList;
	ArrayList<SubDealerOrderListModel> subDealerModels;
	Button buttonNew, buttonPending;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater
				.inflate(R.layout.fragment_status, container, false);
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
		// getSalesOrderStatus();
		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		subDealerModels = new ArrayList<SubDealerOrderListModel>();
		buttonNew = (Button) v.findViewById(R.id.btnNewOrder);
		buttonPending = (Button) v.findViewById(R.id.btnPendingDespatch);
		buttonNew.setOnClickListener(this);
		buttonPending.setOnClickListener(this);
		mStatusList = (ListView) v.findViewById(R.id.dispatchedList);
		getSalesOrderStatus();
		// mStatusList = (ListView) v.findViewById(R.id.salesOrderList);

		mStatusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (subDealerModels.get(position).getStatus()
								.equals("0")) {
					Intent intent = new Intent(getActivity(),
							SubDealerOrderDetails.class);

					if (AppPrefenceManager.getUserType(mActivity).equals("5")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("6")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("7")) {
						intent.putExtra("flag", "1");
					} else {
						intent.putExtra("flag", "0");
					}
					intent.putExtra("status", subDealerModels.get(position)
							.getStatus());
					intent.putExtra("ordr_no", subDealerModels.get(position)
							.getOrderid());
					intent.putExtra("parent_order_id",
							subDealerModels.get(position).getParent_order_id());
					startActivity(intent);
				}
				else if(subDealerModels.get(position).getStatus().equals("1"))
				{
					
					Intent intent = new Intent(getActivity(),
							Order_Detail_Approved.class);
/*
					if (AppPrefenceManager.getUserType(mActivity).equals("5")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("6")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("7")) {
						intent.putExtra("flag", "1");
					} else {*/
						intent.putExtra("flag", "0");
				//	}
					intent.putExtra("status", subDealerModels.get(position)
							.getStatus());
					intent.putExtra("ordr_no", subDealerModels.get(position)
							.getOrderid());
					intent.putExtra("parent_order_id",
							subDealerModels.get(position).getParent_order_id());
					startActivity(intent);
				}
				else if (subDealerModels.get(position).getStatus()
						.equals("4")) {
					Intent intent1 = new Intent(getActivity(),
							DispatchedListDetail.class);
					intent1.putExtra("status", subDealerModels.get(position)
							.getStatus());
					intent1.putExtra("order_id", subDealerModels.get(position)
							.getOrderid());
					intent1.putExtra("parent_order_id",
							subDealerModels.get(position).getParent_order_id());
					/*
					 * intent1.putExtra("order_status","pending" );
					 * intent1.putExtra("title","Pending Dispatch" );
					 */
					startActivity(intent1);
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnNewOrder:
			Intent intent = new Intent(getActivity(),
					SubDealerStatusListActivity.class);
			intent.putExtra("order_status", "new");
			intent.putExtra("title", "New Orders");
			startActivity(intent);
			break;
		case R.id.btnPendingDespatch:
			Intent intent1 = new Intent(getActivity(),
					Dealer_Dispatch_Activity.class);
			/*
			 * intent1.putExtra("order_status","pending" );
			 * intent1.putExtra("title","Pending Dispatch" );
			 */
			startActivity(intent1);
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
					orderModel.setParent_order_id(obj
							.getString("parent_order_id"));
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// getSalesOrderStatus();
		super.onResume();

	}

}
