package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.ReportDetailAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ReportDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftRewardReportFragment extends Fragment implements
		VKCUrlConstants {

	Activity mActivity;
	ArrayList<ReportDetailModel> listGiftReward;
	private View mRootView;
	ListView mListViewGift;
	ReportDetailAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_gift_reward_report,
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
		textviewTitle.setText("Gift & Reward Report");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setHasOptionsMenu(true);
		init(mRootView, savedInstanceState);
		getReport();

		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		listGiftReward = new ArrayList<ReportDetailModel>();
		mListViewGift = (ListView) v.findViewById(R.id.listViewGiftReward);

	}

	private void getReport() {
		AppController.listRedeemReport.clear();
		String name[] = { "dealerId" };
		String values[] = { AppPrefenceManager.getCustomerId(getActivity()), };

		final VKCInternetManager manager = new VKCInternetManager(
				GET_GIFT_REWARD_REPORT_APP);

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
						Log.v("LOG", "19022015 success" + successResponse);
						parseResponse(successResponse);

						
					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);

					}
				});
	}

	

	private void parseResponse(String result) {
		try {
			JSONArray arrayData = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			JSONObject objResponse = jsonObjectresponse
					.optJSONObject("response");
			String status = objResponse.getString("status");

			if (status.equals("Success")) {

				/* if (response.has("orders")) { */
				arrayData = objResponse.optJSONArray("data");
				// }

				// int len = arrayOrders.length();
				if (arrayData.length() > 0) {
					for (int i = 0; i < arrayData.length(); i++) {

						ReportDetailModel model = new ReportDetailModel();
						JSONObject objData = arrayData.optJSONObject(i);
						model.setGift_name(objData.optString("gift_name"));
						model.setGift_qty(objData.optString("gift_qty"));
						model.setRwd_points(objData.optString("rwd_points"));
						model.setTot_coupons(objData.optString("tot_coupons"));
						listGiftReward.add(model);

					}

				}

				adapter = new ReportDetailAdapter(getActivity(), listGiftReward);
				// adapter.notifyDataSetChanged();
				mListViewGift.setAdapter(adapter);

			} else if (status.equals("scheme_error")) {
				VKCUtils.showtoast(getActivity(), 58);
			}

			else {
				VKCUtils.showtoast(getActivity(), 43);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);

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