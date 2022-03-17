package com.mobatia.vkcsalesapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.RedeemListAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.GiftListModel;
import com.mobatia.vkcsalesapp.model.GiftUserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedeemedListFragment extends Fragment implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	ExpandableListView listViewHistory;
	// ArrayList<HistoryModel> ;
	ArrayList<GiftListModel> listGift;
	private int lastExpandedPosition = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_redeem_list, container,
				false);
		setDisplayParams();
		init(mRootView, savedInstanceState);

		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Redeemed Gift List");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		getRedeemList();
		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(final View v, Bundle savedInstanceState) {
		listGift = new ArrayList<>();
		listViewHistory = (ExpandableListView) v
				.findViewById(R.id.listViewRedeem);
		listViewHistory
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						if (lastExpandedPosition != -1
								&& groupPosition != lastExpandedPosition) {
							listViewHistory.collapseGroup(lastExpandedPosition);
						}
						lastExpandedPosition = groupPosition;
					}
				});
		
	}

	/**
	 * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
	 * 2015 Author:Archana.S
	 * 
	 */
	public void getRedeemList() {
		listGift.clear();
		String name[] = { "cust_id"};
		String values[] = { AppPrefenceManager.getCustomerId(getActivity())
				};
		final VKCInternetManager manager = new VKCInternetManager(
				GET_REDEEM_LIST);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						 Log.v("LOG", "18022015 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						// Log.v("LOG", "18022015 Errror" + failureResponse);
					}
				});
	}

	@SuppressLint("NewApi")
	public void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject objResponse = jsonObject.optJSONObject("response");
			String status = objResponse.optString("status");
			
			if (status.equals("Success")) {

				JSONArray arrayData = objResponse.optJSONArray("data");
				if (arrayData.length() > 0) {

					for (int i = 0; i < arrayData.length(); i++) {

						GiftListModel model = new GiftListModel();
						JSONObject obj = arrayData.optJSONObject(i);
						JSONArray arrayDetail = obj.optJSONArray("details");
						/*
						 * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */
						System.out.println("Detail Array " + arrayDetail);
						model.setName(obj.getString("name"));
						model.setPhone(obj.getString("phone"));
						ArrayList<GiftUserModel> listHist = new ArrayList<>();
						for (int j = 0; j < arrayDetail.length(); j++) {
							JSONObject obj1 = arrayDetail.optJSONObject(j);
							GiftUserModel model1 = new GiftUserModel();
							model1.setGift_title(obj1.getString("gift_title"));
							model1.setGift_image(obj1.getString("gift_image"));
							model1.setGift_type(obj1.getString("gift_type"));
							model1.setQuantity(obj1.getString("quantity"));
							// System.out.println("Date Value "+model1.getDateValue());
							listHist.add(model1);
						}

						// System.out.println("Parsed " +
						// listHist.get(i).getDateValue());
						model.setListGiftUser(listHist);

						listGift.add(model);
						// System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
					}
					/*
					 * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
					RedeemListAdapter adapter = new RedeemListAdapter(
							getActivity(), listGift);
					listViewHistory.setAdapter(adapter);
				}

				else {
					VKCUtils.showtoast(getActivity(), 44);
				}

			} else {

				VKCUtils.showtoast(getActivity(), 13);
			}

		} catch (Exception e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * if (v == imageSubmit) { if
		 * (edtSearch.getText().toString().trim().equals("")) { CustomToast
		 * toast = new CustomToast(getActivity()); toast.show(49); } else if
		 * (userType.equals("")) { CustomToast toast = new
		 * CustomToast(getActivity()); toast.show(49); } else if
		 * (mEditPoint.getText().toString().equals("")) {
		 * VKCUtils.textWatcherForEditText(mEditPoint, "Mandatory field");
		 * 
		 * } else if (Integer.parseInt(mEditPoint.getText().toString().trim()) >
		 * myPoint) { // FeedbackSubmitApi(); CustomToast toast = new
		 * CustomToast(getActivity()); toast.show(48); } else {
		 * //submitPoints(); }
		 * 
		 * }
		 */
		
	}

	@Override
	public void onResume() {

		super.onResume();
	}

}
