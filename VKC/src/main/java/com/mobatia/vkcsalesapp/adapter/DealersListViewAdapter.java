package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.DealersModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DealersListViewAdapter extends BaseAdapter {
	Activity mActivity;
	ArrayList<DealersShopModel> dealersModels;
	LayoutInflater mLayoutInflater;

	public DealersListViewAdapter(ArrayList<DealersShopModel> dealersModels,
			Activity mActivity) {
		this.dealersModels = dealersModels;
		this.mActivity = mActivity;
		mLayoutInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dealersModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dealersModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = mLayoutInflater.inflate(R.layout.custom_dealers_list_view,
					null);
		} else {
			view = convertView;
		}
		WraperClass wraperClass = new WraperClass(view);
		wraperClass.getNameText()
				.setText(dealersModels.get(position).getName());
		wraperClass.getAddressText().setText(
				dealersModels.get(position).getAddress());
		wraperClass.getPhoneText().setText(
				dealersModels.get(position).getPhone());
		return view;
	}

	private class WraperClass {
		View view = null;

		public WraperClass(View view) {
			this.view = view;
			// TODO Auto-generated constructor stub
		}

		public TextView getNameText() {
			return (TextView) view.findViewById(R.id.textViewName);
		}

		public TextView getAddressText() {
			return (TextView) view.findViewById(R.id.textViewAddress);
		}

		public TextView getPhoneText() {
			return (TextView) view.findViewById(R.id.textViewPhone);
		}

	}
}