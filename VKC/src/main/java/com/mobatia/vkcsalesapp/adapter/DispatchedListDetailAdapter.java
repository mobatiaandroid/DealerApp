package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.PendingOrderAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.DispatchedDetailModel;

public class DispatchedListDetailAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<DispatchedDetailModel> listDespatch;
	static String value;

	public DispatchedListDetailAdapter(Activity mActivity,
			ArrayList<DispatchedDetailModel> listDespatch) {
		this.mActivity = mActivity;
		this.listDespatch = listDespatch;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDespatch.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {

			view = mInflater.inflate(R.layout.item_dispatched_detail, null);
			holder = new ViewHolder();
			holder.despatchedQty = (TextView) view
					.findViewById(R.id.txtDespatchedQuantity);
			holder.prodId = (TextView) view.findViewById(R.id.txtProdId);
			holder.size = (TextView) view.findViewById(R.id.txtSize);
			holder.approved_qty = (TextView) view
					.findViewById(R.id.txtApprovedQty);
			holder.textColor = (TextView) view.findViewById(R.id.txtColor);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.despatchedQty.setText(listDespatch.get(position).getQuantity());
		holder.prodId.setText(listDespatch.get(position).getProductId());
		holder.size.setText(listDespatch.get(position).getCaseDetail());
		holder.approved_qty.setText(listDespatch.get(position).getApproved_qty());
		holder.textColor.setText(listDespatch.get(position).getColor_name());
		RelativeLayout rel1 = (RelativeLayout) view.findViewById(R.id.rel1);
		RelativeLayout rel2 = (RelativeLayout) view.findViewById(R.id.rel2);
		RelativeLayout rel3 = (RelativeLayout) view.findViewById(R.id.rel3);
		RelativeLayout rel4 = (RelativeLayout) view.findViewById(R.id.rel4);
		RelativeLayout rel5 = (RelativeLayout) view.findViewById(R.id.rel5);

		/* Bibin Edited */
		if (AppPrefenceManager.getUserType(mActivity).equals("7")) {
			rel5.setVisibility(View.GONE);
		} else {
			rel5.setVisibility(View.VISIBLE);
		}

		if (position % 2 == 0) {
			rel1.setBackgroundColor(Color.rgb(219, 188, 188));
			rel2.setBackgroundColor(Color.rgb(219, 188, 188));
			rel3.setBackgroundColor(Color.rgb(219, 188, 188));
			rel4.setBackgroundColor(Color.rgb(219, 188, 188));
			rel5.setBackgroundColor(Color.rgb(219, 188, 188));
		} else {
			rel1.setBackgroundColor(Color.rgb(208, 208, 208));
			rel2.setBackgroundColor(Color.rgb(208, 208, 208));
			rel3.setBackgroundColor(Color.rgb(208, 208, 208));
			rel4.setBackgroundColor(Color.rgb(208, 208, 208));
			rel5.setBackgroundColor(Color.rgb(208, 208, 208));
		}

		return view;
	}

	static class ViewHolder {
		TextView despatchedQty;
		TextView prodId;
		TextView size;
		TextView approved_qty;
		TextView textColor;
	}
}