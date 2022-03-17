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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.SubDealerOrderDetails;
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

public class SubDealerOrderList extends Fragment implements VKCUrlConstants {

	Activity mActivity;
	ArrayList<SubDealerOrderListModel> subDealerModels;
	private View mRootView;
	ListView mStatusList;
	SubDealerOrderListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_salesorderstatus,
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
		textviewTitle.setText("Subdealer Orders");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		AppController.isDealerList = false;
		/*
		 * abar.setDisplayShowTitleEnabled(false);
		 * //abar.setDisplayHomeAsUpEnabled(true);
		 * abar.setIcon(R.color.transparent); abar.setHomeButtonEnabled(true);
		 */
		setHasOptionsMenu(true);
		AppController.isCart = false;
		init(mRootView, savedInstanceState);
		getSalesOrderStatus();

		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		subDealerModels = new ArrayList<SubDealerOrderListModel>();
		mStatusList = (ListView) v.findViewById(R.id.salesOrderList);
		mStatusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						SubDealerOrderDetails.class);
				intent.putExtra("ordr_no", subDealerModels.get(position)
						.getOrderid());
				intent.putExtra("position", position);
				intent.putExtra("flag", subDealerModels.get(position)
						.getStatus().toString());
				intent.putExtra("status", subDealerModels.get(position)
						.getStatus().toString());
				startActivity(intent);

			}
		});

	}

	private void getSalesOrderStatus() {
		subDealerModels.clear();
		String name[] = { "dealer_id", "list_type" };
		String values[] = { AppPrefenceManager.getCustomerId(getActivity()),
				"all" };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBDEALER_ORDER_URL);


		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
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
					// System.out.println("Name:"+orderModel.getName());
					orderModel.setOrderid(obj.getString("orderid"));
					orderModel.setAddress(obj.getString("city"));
					orderModel.setPhone(obj.getString("phone"));
					orderModel.setTotalqty(obj.getString("total_qty"));
					orderModel.setOrderDate(obj.getString("order_date"));

					orderModel.setStatus(obj.getString("status"));
					subDealerModels.add(orderModel);

				}

				adapter = new SubDealerOrderListAdapter(getActivity(),
						subDealerModels);
				// adapter.notifyDataSetChanged();
				mStatusList.setAdapter(adapter);

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
}
