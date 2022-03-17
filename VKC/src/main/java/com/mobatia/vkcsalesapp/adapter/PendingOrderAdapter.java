package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.mobatia.vkcsalesapp.adapter.SalesOrderAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.customview.HorizontalListView;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.Pending_Order_Model;

public class PendingOrderAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<CartModel> cartArrayList;
	ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
	ImageView imgClose;
	LinearLayout linearLayout;
	ArrayList<Pending_Order_Model> listPending;
	private TextView mTxtViewQty;

	static String value;

	public PendingOrderAdapter(Activity mActivity,
			ArrayList<Pending_Order_Model> listPending) {
		this.mActivity = mActivity;
		// this.cartArrayList = cartArrayList;
		// this.linearLayout = linearLayout;
		// mTxtViewQty = txtViewQty;
		this.listPending = listPending;
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
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.items_pending_order, null);
/*
			final TextView approvedQty = (TextView) view
					.findViewById(R.id.txtApproved);*/
			holder.prodId = (TextView) view.findViewById(R.id.txtProdId);
			holder.size = (TextView) view.findViewById(R.id.txtSize);
			holder.qty = (TextView) view.findViewById(R.id.txtQuantity);
			holder.textColor = (TextView) view.findViewById(R.id.txtColor);

			// TextView color=(TextView)view.findViewById(R.id.txtColor);
			/*
			 * HorizontalListView relColor = (HorizontalListView) view
			 * .findViewById(R.id.listViewColor);
			 */
			// imgClose = (ImageView) view.findViewById(R.id.imgClose);
			

		//	approvedQty.setText(listPending.get(position).getApproved_qty());
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.prodId.setText(listPending.get(position).getProductId());
		holder.size.setText(listPending.get(position).getCaseDetail());

		holder.qty.setText(listPending.get(position).getQuantity());

		holder.textColor.setText(listPending.get(position).getColor_name());
		/*
		 * AppController.subDealerOrderDetailList.get(position) .getColorId(),
		 * 0));
		 */
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
		if (!AppPrefenceManager.getUserType(mActivity).equals("7")) {

		}

		return view;
	}

	static class ViewHolder {
		TextView prodId;
		TextView size;
		TextView qty;
		TextView textColor;

	}
}
