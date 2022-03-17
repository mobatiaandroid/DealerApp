package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.model.DealersShopModel;

public class SubDealersListViewAdapter extends BaseAdapter {
	Activity mActivity;
	int position;

	LayoutInflater mLayoutInflater;

	public SubDealersListViewAdapter(ArrayList<DealersShopModel> dealersModels,
			Activity mActivity) {
		AppController.dealersModels = dealersModels;
		this.mActivity = mActivity;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AppController.dealersModels.size();
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

	static class ViewHolder {
		TextView name;
		TextView address;
		TextView phone;
		CheckBox check_box;
	}

	@Override
	public View getView(final int position, View Convertview, ViewGroup parent) {
		ViewHolder viewHolder = null;

		LayoutInflater mInflater = (LayoutInflater) mActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = Convertview;
		if (view == null) {
			view = mInflater
					.inflate(R.layout.custom_subdealers_list_view, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.textViewName);
			viewHolder.address = (TextView) view
					.findViewById(R.id.textViewAddress);
			viewHolder.phone = (TextView) view.findViewById(R.id.textViewPhone);
			viewHolder.check_box = (CheckBox) view
					.findViewById(R.id.checkbox_dealer);

			viewHolder.check_box.setOnCheckedChangeListener(null);

			viewHolder.check_box
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							String idValue = AppController.dealersModels.get(
									position).getId();

							int getPosition = (Integer) buttonView.getTag();
							AppController.dealersModels.get(getPosition)
									.setSelected(isChecked);

						}
					});

			view.setTag(viewHolder);
			view.setTag(R.id.textViewName, viewHolder.name);
			view.setTag(R.id.textViewAddress, viewHolder.address);
			view.setTag(R.id.textViewPhone, viewHolder.phone);
			view.setTag(R.id.checkbox_dealer, viewHolder.check_box);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.check_box.setTag(position);
		viewHolder.name.setText(AppController.dealersModels.get(position)
				.getName());
		viewHolder.address.setText(AppController.dealersModels.get(position)
				.getAddress());
		viewHolder.phone.setText(AppController.dealersModels.get(position)
				.getPhone());
		viewHolder.check_box.setChecked(AppController.dealersModels.get(
				position).isSelected());

		return view;
	}

}
