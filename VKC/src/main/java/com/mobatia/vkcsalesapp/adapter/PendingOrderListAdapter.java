package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;

public class PendingOrderListAdapter extends BaseAdapter {
	Context mActivity;
	ArrayList<SubDealerOrderListModel> subdealersModels;
	LayoutInflater mLayoutInflater;
	String status;

	public PendingOrderListAdapter(Context mActivity,
			ArrayList<SubDealerOrderListModel> subdealersModels) {
		this.subdealersModels = subdealersModels;
		this.mActivity = mActivity;
		// mLayoutInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subdealersModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		LayoutInflater inflater = ((Activity) mActivity).getLayoutInflater();
		
		if (convertView == null) {
			view = inflater.inflate(R.layout.pending_list_row, null);
			TextView textOrderNo = (TextView) view
					.findViewById(R.id.textViewOrderNO);
			TextView textName = (TextView) view.findViewById(R.id.textViewName);
			TextView textAddress = (TextView) view
					.findViewById(R.id.textViewAddress);
			TextView textStatus = (TextView) view
					.findViewById(R.id.textViewStatus);
			textOrderNo.setText(subdealersModels.get(position).getOrderid());
			textName.setText("Dealer:  "
					+ subdealersModels.get(position).getName());
			textAddress.setText("Place:  "
					+ subdealersModels.get(position).getAddress());
			if(subdealersModels.get(position).getStatus().equals("0"))
			{
				status="Pending";
			}
			else if(subdealersModels.get(position).getStatus().equals("1"))
			{
				status="Approved";
			}
			else if(subdealersModels.get(position).getStatus().equals("2"))
			{
				status="Partially Approved";
			}
			else if(subdealersModels.get(position).getStatus().equals("3"))
			{
				status="Rejected";
			}

			textStatus.setText("Status: "
					+ status);

		} else {
			view = convertView;
		}
		if (position % 2 == 1) {
			// view.setBackgroundColor(Color.BLUE);
			view.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_grey));
		} else {
			view.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_white));
		}

		return view;
	}

}
