package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.NotificationDetailActivity;
import com.mobatia.vkcsalesapp.adapter.NotificationListAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.NotificationListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationListFragment extends Fragment implements
		VKCUrlConstants {
	private Activity mActivity;
	private View view;

	ArrayList<NotificationListModel> listNotification;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.activity_notification_list, null);
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
		textviewTitle.setText("Notifications");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		initialiseUI();

		// System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));

		return view;

		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initialiseUI() {
		// TODO Auto-generated method stub

		AppController.mNotificationList = (ListView) view
				.findViewById(R.id.listNotification);
		listNotification = new ArrayList<>();
		getNotificationList();
		AppController.mNotificationList
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent(getActivity(),
								NotificationDetailActivity.class);
						intent.putExtra("MESSAGE",
								listNotification.get(position).getMessage());
						intent.putExtra("MESSAGE_DATE",
								listNotification.get(position).getMessageDate());
						/*
						 * intent.putExtra("orderNumber",
						 * AppController.subDealerModels.get(position)
						 * .getOrderid()); intent.putExtra("dealerName",
						 * AppController.subDealerModels
						 * .get(position).getName());
						 * intent.putExtra("dealerId",
						 * AppController.subDealerModels
						 * .get(position).getDealerId());
						 */

						startActivity(intent);

					}
				});

	}

	// AppPrefenceManager.getUserType(this)
	private void getNotificationList() {
		listNotification.clear();
		String name[] = { "userid", "role" };
		String values[] = { AppPrefenceManager.getUserId(getActivity()),
				AppPrefenceManager.getUserType(getActivity()) };

		final VKCInternetManager manager = new VKCInternetManager(
				NOTIFICATION_LIST_URL);
		

		manager.getResponsePOST(getActivity(), name, values,
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
			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			// JSONObject response =
			// jsonObjectresponse.getJSONObject("response");
			String status = jsonObjectresponse.getString("status");
			if (status.equals("200")) {

				JSONArray arrayNotification = jsonObjectresponse
						.getJSONArray("data");
				if (arrayNotification.length() > 0) {
					for (int i = 0; i < arrayNotification.length(); i++) {
						JSONObject object = arrayNotification.getJSONObject(i);
						NotificationListModel model = new NotificationListModel();
						model.setMessage(object.getString("message"));
						model.setMessageDate(object.getString("date"));
						model.setMessageId(object.getString("id"));
						listNotification.add(model);

					}
					NotificationListAdapter adapter = new NotificationListAdapter(
							listNotification, mActivity);
					adapter.notifyDataSetChanged();
					AppController.mNotificationList.setAdapter(adapter);
				} else {
					VKCUtils.showtoast(mActivity, 44);
				}
			}
		} catch (Exception e) {

		}

	}

}
