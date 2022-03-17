package com.mobatia.vkcsalesapp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;

public class DispatchedListAdapter extends BaseAdapter {
	Activity mActivity;

	LayoutInflater mLayoutInflater;
	List<SubDealerOrderListModel> listModel;

	public DispatchedListAdapter(Activity mActivity,
			List<SubDealerOrderListModel> listModel) {
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
		TextView textOrderNo;
		TextView textName;
		TextView textAddress;
		TextView textPhone;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;

		if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.custom_subdealer_list, null);

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

		viewHolder = new ViewHolder();
		viewHolder.textOrderNo = (TextView) v
				.findViewById(R.id.textViewOrderNO);
		viewHolder.textName = (TextView) v.findViewById(R.id.textViewName);
		viewHolder.textAddress = (TextView) v
				.findViewById(R.id.textViewAddress);
		viewHolder.textPhone = (TextView) v.findViewById(R.id.textViewPhone);

		v.setTag(viewHolder);

		SubDealerOrderListModel orderList = listModel.get(position);
		if (orderList != null) {
			String status_value = orderList.getStatus();
			String status = "";
			if (status_value.equals("0")) {
				status = "Pending";
			} else if (status_value.equals("1")) {
				status = "Pending dispatch";
			} else if (status_value.equals("2")) {
				status = "Pending dispatch";
			} else if (status_value.equals("3")) {
				status = "Rejected";
			} else if (status_value.equals("4")) {
				status = "Dispatched";
			}
			viewHolder.textOrderNo.setText(orderList.getOrderid());
			viewHolder.textName.setText(orderList.getOrderDate());

			viewHolder.textAddress.setText(orderList.getTotalqty());
			viewHolder.textPhone.setText(status);
		}

		return v;
	}

}
