package com.mobatia.vkcsalesapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.TransactionHistoryAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.HistoryModel;
import com.mobatia.vkcsalesapp.model.TransactionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionHistoryFragment extends Fragment implements
		OnClickListener, VKCJsonTagConstants, VKCUrlConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	ExpandableListView listViewHistory;
	// ArrayList<HistoryModel> ;
	ArrayList<TransactionModel> listHistory;
	private int lastExpandedPosition = -1;
	TextView textEarned, textTransferred, textBalance, textCredit, textDebit;
	String historyType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_transaction_history,
				container, false);
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
		textviewTitle.setText("Transaction History");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(final View v, Bundle savedInstanceState) {
		listHistory = new ArrayList<>();
		listViewHistory = (ExpandableListView) v
				.findViewById(R.id.listViewHistory);
		textEarned = (TextView) v.findViewById(R.id.textEarned);
		textTransferred = (TextView) v.findViewById(R.id.textTransferred);
		textBalance = (TextView) v.findViewById(R.id.textBalance);
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
		textCredit = (TextView) v.findViewById(R.id.textCredit);
		textDebit = (TextView) v.findViewById(R.id.textDebit);
		textDebit.setOnClickListener(this);
		textCredit.setOnClickListener(this);

		historyType = "CREDIT";
		getMyHistory();
	}

	/**
	 * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
	 * 2015 Author:Archana.S
	 * 
	 */
	public void getMyHistory() {
		listHistory.clear();
		String name[] = { "userid", "role", "type" };
		String values[] = { AppPrefenceManager.getUserId(getActivity()),
				AppPrefenceManager.getUserType(getActivity()), historyType };
		final VKCInternetManager manager = new VKCInternetManager(
				TRANSACTION_HISTORY);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						// Log.v("LOG", "18022015 success" + successResponse);
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
			textEarned.setText(objResponse.optString("total_credits"));
			textTransferred.setText(objResponse.optString("total_debits"));
			textBalance.setText(objResponse.optString("balance_point"));
			if (status.equals("Success")) {

				JSONArray arrayData = objResponse.optJSONArray("data");
				if (arrayData.length() > 0) {

					for (int i = 0; i < arrayData.length(); i++) {

						TransactionModel model = new TransactionModel();
						JSONObject obj = arrayData.optJSONObject(i);
						JSONArray arrayDetail = obj.optJSONArray("details");
						/*
						 * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */
						System.out.println("Detail Array " + arrayDetail);
						model.setUserName(obj.getString("to_name"));
						model.setTotalPoints(obj.getString("tot_points"));
						ArrayList<HistoryModel> listHist = new ArrayList<>();
						for (int j = 0; j < arrayDetail.length(); j++) {
							JSONObject obj1 = arrayDetail.optJSONObject(j);
							HistoryModel model1 = new HistoryModel();
							model1.setPoints(obj1.getString("points"));
							model1.setType(obj1.getString("type"));
							model1.setTo_name(obj1.getString("to_name"));
							model1.setTo_role(obj1.getString("to_role"));
							model1.setDateValue(obj1.getString("date"));
							model1.setInvoiceNo(obj1.getString("invoice_no"));
							// System.out.println("Date Value "+model1.getDateValue());
							listHist.add(model1);
						}

						// System.out.println("Parsed " +
						// listHist.get(i).getDateValue());
						model.setListHistory(listHist);

						listHistory.add(model);
						// System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
					}
					/*
					 * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
					TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(
							getActivity(), listHistory);
					adapter.notifyDataSetChanged();
					listViewHistory.setAdapter(adapter);
				}

				else {
					VKCUtils.showtoast(getActivity(), 44);
					listHistory.clear();
					TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(
							getActivity(), listHistory);
					adapter.notifyDataSetChanged();
					listViewHistory.setAdapter(adapter);
				}

			} 
			else if(status.equals("scheme_error"))
			{
				VKCUtils.showtoast(getActivity(), 57);
			}
			
			else {

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
		if (v == textCredit) {
			historyType = "CREDIT";
			textCredit.setBackgroundResource(R.drawable.rounded_rect_green);
			textDebit.setBackgroundResource(R.drawable.rounded_rect_redline);
			getMyHistory();
		} else if (v == textDebit) {
			historyType = "DEBIT";
			textCredit.setBackgroundResource(R.drawable.rounded_rect_redline);
			textDebit.setBackgroundResource(R.drawable.rounded_rect_green);
			
			getMyHistory();

		}
	}

	@Override
	public void onResume() {

		super.onResume();
	}

}
