package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.RedeemReportsAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.RedeemReportModel;
import com.mobatia.vkcsalesapp.model.ReportDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedeemReportFragment extends Fragment implements VKCUrlConstants {

	Activity mActivity;
	ArrayList<RedeemReportModel> tempRedeemReportList;
	private View mRootView;
	ListView mStatusList;
	RedeemReportsAdapter adapter;
	EditText editSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_redeem_report_list,
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
		textviewTitle.setText("Redeem Report");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		/*
		 * abar.setDisplayShowTitleEnabled(false);
		 * //abar.setDisplayHomeAsUpEnabled(true);
		 * abar.setIcon(R.color.transparent); abar.setHomeButtonEnabled(true);
		 */
		setHasOptionsMenu(true);
		init(mRootView, savedInstanceState);
		getReport();

		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		tempRedeemReportList = new ArrayList<RedeemReportModel>();
		mStatusList = (ListView) v.findViewById(R.id.listViewRedeemReportList);
		editSearch = (EditText) v.findViewById(R.id.editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (s.length() > 0) {
					tempRedeemReportList.clear();
					for (int i = 0; i < AppController.listRedeemReport.size(); i++) {
						if (AppController.listRedeemReport.get(i).getCustId()
								.contains(s)
								|| AppController.listRedeemReport.get(i)
										.getCustName().toLowerCase().contains(s)
								|| AppController.listRedeemReport.get(i)
										.getCustMobile().contains(s)
								|| AppController.listRedeemReport.get(i)
										.getCustPlace().toLowerCase().contains(s)) {
							tempRedeemReportList
									.add(AppController.listRedeemReport.get(i));
						} else {
							mStatusList.setAdapter(null);
						}
					}
					adapter = new RedeemReportsAdapter(getActivity(),
							tempRedeemReportList);
					adapter.notifyDataSetChanged();
					mStatusList.setAdapter(adapter);

				} else {
					adapter = new RedeemReportsAdapter(getActivity(),
							AppController.listRedeemReport);
					adapter.notifyDataSetChanged();
					mStatusList.setAdapter(adapter);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mStatusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/*
				 * Intent intent = new Intent(getActivity(),
				 * SubDealerOrderDetails.class); intent.putExtra("ordr_no",
				 * subDealerModels.get(position) .getOrderid());
				 * intent.putExtra("position", position);
				 * intent.putExtra("flag", subDealerModels.get(position)
				 * .getStatus().toString()); intent.putExtra("status",
				 * subDealerModels.get(position) .getStatus().toString());
				 * startActivity(intent);
				 */

			}
		});

	}

	private void getReport() {
		AppController.listRedeemReport.clear();
		String name[] = { "dealerId" };
		String values[] = { AppPrefenceManager.getCustomerId(getActivity()) };

		final VKCInternetManager manager = new VKCInternetManager(
				GET_REDEEM_REPORT_APP);

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
						Log.v("LOG", "19022015 success" + successResponse);
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
						RedeemReportModel redeemModel = new RedeemReportModel();
						JSONObject obj = arrayData.optJSONObject(i);
						redeemModel.setCustId(obj.getString("cust_id"));
						redeemModel.setCustName(obj.getString("name"));
						redeemModel.setCustPlace(obj.getString("place"));
						redeemModel.setCustMobile(obj.getString("phone"));
						JSONArray objArray = obj.optJSONArray("details");
						ArrayList<ReportDetailModel> listDetail = new ArrayList<>();
						for (int j = 0; j < objArray.length(); j++) {
							ReportDetailModel model = new ReportDetailModel();
							JSONObject objData = objArray.optJSONObject(j);
							model.setGift_name(objData.optString("gift_name"));
							model.setGift_qty(objData.optString("gift_qty"));
							model.setRwd_points(objData.optString("rwd_points"));
							model.setTot_coupons(objData
									.optString("tot_coupons"));
							listDetail.add(model);

						}
						redeemModel.setListReportDetail(listDetail);
						AppController.listRedeemReport.add(redeemModel);

					}

					adapter = new RedeemReportsAdapter(getActivity(),
							AppController.listRedeemReport);
					// adapter.notifyDataSetChanged();
					mStatusList.setAdapter(adapter);

				} else {
					VKCUtils.showtoast(getActivity(), 43);
				}
			}
			else if(status.equals("scheme_error"))
			{
				VKCUtils.showtoast(getActivity(), 58);
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
