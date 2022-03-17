package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.ProductListAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.adapter.SalesOrderAdapter.DeleteAlert;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.fragments.SalesOrderFragment;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.NotificationListModel;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter implements
		VKCUrlConstants {
	ArrayList<NotificationListModel> listNotification;
	LayoutInflater mInflater;
	Activity activity;

	public NotificationListAdapter(ArrayList<NotificationListModel> list,
			Activity activity) {
		// TODO Auto-generated constructor stub
		this.listNotification = list;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listNotification.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		View view;
		mInflater = LayoutInflater.from(activity);
		if (convertView == null) {

			view = mInflater.inflate(R.layout.item_notification_list, parent,
					false);

		} else {
			view = convertView;
		}

		holder = new ViewHolder();
		holder.imageDelete = (ImageView) view.findViewById(R.id.imageDelete);
		holder.txtMessage = (TextView) view.findViewById(R.id.textMessage);
		holder.txtDate = (TextView) view.findViewById(R.id.textDate);
		holder.txtMessage.setText(listNotification.get(position).getMessage());
		holder.txtDate.setText(listNotification.get(position).getMessageDate());
		holder.imageDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDeleteDialog(position);

			}
		});
		return view;
	}

	public class ViewHolder {
		ImageView imageDelete;
		TextView txtMessage, txtDate;

	}

	private void getNotificationList() {
		listNotification.clear();
		String name[] = { "userid", "role" };
		String values[] = { AppPrefenceManager.getUserId(activity),
				AppPrefenceManager.getUserType(activity) };

		final VKCInternetManager manager = new VKCInternetManager(
				NOTIFICATION_LIST_URL);
		/*
		 * for (int i = 0; i < name.length; i++) { Log.v("LOG",
		 * "12012015 name : " + name[i]); Log.v("LOG", "12012015 values : " +
		 * values[i]);
		 * 
		 * }
		 */

		manager.getResponsePOST(activity, name, values, new ResponseListener() {
			@Override
			public void responseSuccess(String successResponse) {
				parseResponse(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(activity, 17);

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

				for (int i = 0; i < arrayNotification.length(); i++) {
					JSONObject object = arrayNotification.getJSONObject(i);
					NotificationListModel model = new NotificationListModel();
					model.setMessage(object.getString("message"));
					model.setMessageDate(object.getString("date"));
					model.setMessageId(object.getString("id"));
					listNotification.add(model);

				}
				NotificationListAdapter adapter = new NotificationListAdapter(
						listNotification, activity);
				adapter.notifyDataSetChanged();
				AppController.mNotificationList.setAdapter(adapter);
			}
		} catch (Exception e) {

		}

	}

	private void deleteListItem(String id) {
		listNotification.clear();
		String name[] = { "id" };
		String values[] = { id };

		final VKCInternetManager manager = new VKCInternetManager(
				NOTIFICATION_DELETE_URL);

		manager.getResponsePOST(activity, name, values, new ResponseListener() {
			@Override
			public void responseSuccess(String successResponse) {
				parseResponseDelete(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(activity, 17);

			}
		});
	}


	private void parseResponseDelete(String result) {
		try {
			
			JSONObject jsonObjectresponse = new JSONObject(result);
			String status = jsonObjectresponse.getString("status");
			if (status.equals("200")) {
				getNotificationList();

			}

		} catch (Exception e) {

		}

	}

	private void showDeleteDialog(int position) {
		DeleteAlert appDeleteDialog = new DeleteAlert(activity, position);

		appDeleteDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		appDeleteDialog.setCancelable(true);
		appDeleteDialog.show();

	}

	public class DeleteAlert extends Dialog implements
			android.view.View.OnClickListener, VKCDbConstants {

		public Activity mActivity;
		public Dialog d;
		int position;

		public DeleteAlert(Activity a, int position) {
			super(a);
			// TODO Auto-generated constructor stub
			this.mActivity = a;
			this.position = position;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_delete_notification);
			init();

		}

		private void init() {
			RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);
			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					deleteListItem(listNotification.get(position)
							.getMessageId());

				}

			});
			Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
			buttonCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}

		@Override
		public void onClick(View v) {

			dismiss();
		}

	}
}
