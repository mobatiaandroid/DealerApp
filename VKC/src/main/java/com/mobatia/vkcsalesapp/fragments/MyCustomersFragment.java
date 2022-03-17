package com.mobatia.vkcsalesapp.fragments;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.CustomerAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCustomersFragment extends Fragment implements VKCUrlConstants {

	private View mRootView;
	RelativeLayout rlState;
	RelativeLayout rlDistrict;
	RelativeLayout rlPlace;
	RelativeLayout submitLayout;
	LinearLayout llTop;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	ListView customersList;
	ArrayList<CustomerModel> listCustomers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("My Customers");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		mRootView = inflater.inflate(R.layout.fragment_my_customers, container,
				false);

		setDisplayParams();
		init(mRootView, savedInstanceState);

		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(View v, Bundle savedInstanceState) {
		listCustomers = new ArrayList<CustomerModel>();
		customersList = (ListView) v.findViewById(R.id.listMyCustomer);

		// Dealer List Click
		customersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});

		getCustomersApi();
	}

	private void getCustomersApi() {
		listCustomers.clear();
		String name[] = { "cust_id" };
		String values[] = { AppPrefenceManager.getCustomerId(getActivity()) };
		final VKCInternetManager manager = new VKCInternetManager(GET_CUSTOMERS);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
						try {
							JSONObject obj = new JSONObject(successResponse);
							JSONObject objResponse = obj
									.optJSONObject("response");
							String status = objResponse.optString("status");
							if (status.equals("Success")) {
								JSONArray arrayData = objResponse
										.optJSONArray("data");
								if (arrayData.length() > 0) {

									for (int i = 0; i < arrayData.length(); i++) {
										JSONObject objItem = arrayData
												.optJSONObject(i);
										CustomerModel model = new CustomerModel();
										model.setName(objItem.optString("name"));
										model.setPhone(objItem
												.optString("phone"));
										model.setRole(objItem.optString("role"));
										listCustomers.add(model);
									}
									CustomerAdapter adapter = new CustomerAdapter(
											getActivity(), listCustomers);
									customersList.setAdapter(adapter);

								} else {

									VKCUtils.showtoast(getActivity(), 44);
								}

							} else {

								VKCUtils.showtoast(getActivity(), 13);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);

					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}
