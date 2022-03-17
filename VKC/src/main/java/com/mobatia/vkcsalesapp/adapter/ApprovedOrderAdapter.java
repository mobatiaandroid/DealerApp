package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.Pending_Order_Model;

public class ApprovedOrderAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<Pending_Order_Model> listPending;
	static String value;

	public ApprovedOrderAdapter(Activity mActivity,ArrayList<Pending_Order_Model> listPending) {
		this.mActivity = mActivity;
		this.listPending=listPending;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPending.size();
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
		final CustomToast toast = new CustomToast(mActivity);
		View view = null;

		if (convertView == null) {
			
				view = mInflater.inflate(R.layout.item_pending_order, null);
				
				final TextView approvedQty = (TextView) view.findViewById(R.id.txtApproved);
				TextView prodId = (TextView) view.findViewById(R.id.txtProdId);
				TextView size = (TextView) view.findViewById(R.id.txtSize);
				TextView qty = (TextView) view.findViewById(R.id.txtQuantity);
				TextView textColor = (TextView) view.findViewById(R.id.txtColor);
				prodId.setText(listPending.get(position)
						.getProductId());
				size.setText(listPending.get(position)
						.getCaseDetail());
				
				qty.setText(listPending.get(position)
						.getQuantity());
			
				textColor.setText(listPending.get(position).getColor_name());
		
				approvedQty.setText(listPending.get(position).getApproved_qty());
			

		} else {
			view = convertView;
		}

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
		if (!AppPrefenceManager.getUserType(mActivity).equals("7")) 
		{
		
		}
	
		return view;
	}
}
