package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.android.volley.toolbox.ClearCacheRequest;
import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.activities.LoginActivity;
import com.mobatia.vkcsalesapp.adapter.DispatchedListAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.appdialogs.AppExitDialog;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.model.MembersModel;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;

public class MemberAdapter extends BaseAdapter implements VKCDbConstants {
	Activity mActivity;

	LayoutInflater mLayoutInflater;
	ArrayList<MembersModel> listModel;

	public MemberAdapter(Activity mActivity, ArrayList<MembersModel> listModel) {

		this.mActivity = mActivity;
		this.listModel = listModel;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listModel.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class ViewHolder {

		TextView textName;
		TextView textSwitch;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;

		if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_group_member, null);

			viewHolder = new ViewHolder();
			viewHolder.textName = (TextView) v.findViewById(R.id.textName);
			viewHolder.textSwitch = (TextView) v.findViewById(R.id.textSwitch);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();

		}

		if (position % 2 == 1) {
			// view.setBackgroundColor(Color.BLUE);
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_grey));
		} else {
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_white));
		}

		viewHolder.textName.setText(listModel.get(position).getCustomer_name());
		viewHolder.textSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (listModel.get(position).getLogin().equals("failed")) {
					UnableSwitchDialog appExitDialog = new UnableSwitchDialog(
							mActivity);
					appExitDialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					appExitDialog.setCancelable(true);
					appExitDialog.show();
				} else {
					SwitchDialog appExitDialog = new SwitchDialog(mActivity,
							position);
					appExitDialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					appExitDialog.setCancelable(true);
					appExitDialog.show();
				}

			}
		});

		return v;
	}

	/**/

	public class SwitchDialog extends Dialog implements
			android.view.View.OnClickListener {

		int position;

		public SwitchDialog(Activity a, final int position) {
			super(a);
			// TODO Auto-generated constructor stub

			this.position = position;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_switch);
			init();

		}

		private void init() {
			DisplayManagerScale disp = new DisplayManagerScale(mActivity);
			int displayH = disp.getDeviceHeight();
			int displayW = disp.getDeviceWidth();

			RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);

			// relativeDate.getLayoutParams().height = (int) (displayH * .65);
			// relativeDate.getLayoutParams().width = (int) (displayW * .90);

			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					AppPrefenceManager.saveUserType(mActivity,
							listModel.get(position).getRole_id());

					AppPrefenceManager.saveCustomerId(mActivity,
							listModel.get(position).getCust_id());
					if (listModel.get(position).getDist_name() != null) {
						AppPrefenceManager.setLoginPlace(mActivity, listModel
								.get(position).getDist_name());
					}

					if (listModel.get(position).getCustomer_name() != null) {
						AppPrefenceManager.setLoginCustomerName(mActivity,
								listModel.get(position).getCustomer_name());
					}
					AppPrefenceManager.saveUserId(mActivity,
							listModel.get(position).getUser_id());
					// Save Dealer Count
					String mDealerCount = listModel.get(position)
							.getDealer_count();
					AppPrefenceManager.saveDealerCount(mActivity, mDealerCount);
					String isCredit = listModel.get(position).getCredit_view();
					AppPrefenceManager.saveIsCredit(mActivity, isCredit);
					// dealerCount = Integer.parseInt(mDealerCount);
					String mUserName = listModel.get(position).getUser_name();
					// if(AppPrefenceManager.getLoginStatusFlag(mActivity).equals("false")){

					if (!AppPrefenceManager.getUserName(mActivity).equals("")) {
						if (!AppPrefenceManager.getUserName(mActivity).equals(
								mUserName)) {

							clearDb();
						} else {
						}

					}
					// }

					AppPrefenceManager.saveUserName(mActivity,
							listModel.get(position).getUser_name());
					AppPrefenceManager.saveStateCode(mActivity,
							listModel.get(position).getState_code());
					AppPrefenceManager.saveLoginStatusFlag(mActivity, "true");
					dismiss();
					mActivity.startActivity(new Intent(mActivity,
							DashboardFActivity.class));
					mActivity.finish();
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

	public class UnableSwitchDialog extends Dialog implements
			android.view.View.OnClickListener {

		int position;

		public UnableSwitchDialog(Activity a) {
			super(a);
			// TODO Auto-generated constructor stub

			this.position = position;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_unable_to_switch);
			init();

		}

		private void init() {
			DisplayManagerScale disp = new DisplayManagerScale(mActivity);
			int displayH = disp.getDeviceHeight();
			int displayW = disp.getDeviceWidth();

			RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);

			// relativeDate.getLayoutParams().height = (int) (displayH * .65);
			// relativeDate.getLayoutParams().width = (int) (displayW * .90);

			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

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

	public void clearDb() {
		DataBaseManager databaseManager = new DataBaseManager(mActivity);
		databaseManager.removeDb(TABLE_SHOPPINGCART);
	}

}