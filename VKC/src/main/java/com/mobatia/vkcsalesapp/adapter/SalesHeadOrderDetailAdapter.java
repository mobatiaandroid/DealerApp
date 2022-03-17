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
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.customview.HorizontalListView;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.ColorModel;

public class SalesHeadOrderDetailAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<CartModel> cartArrayList;
	ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
	ImageView imgClose;
	LinearLayout linearLayout;
	private TextView mTxtViewQty;
	
	static String value;

	public SalesHeadOrderDetailAdapter(Activity mActivity) {
		this.mActivity = mActivity;
		// this.cartArrayList = cartArrayList;
		// this.linearLayout = linearLayout;
		// mTxtViewQty = txtViewQty;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AppController.subDealerOrderDetailList.size();
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
			
				view = mInflater.inflate(R.layout.custom_order_list_4_item,
						null);
				TextView prodId = (TextView) view.findViewById(R.id.txtProdId);
				TextView size = (TextView) view.findViewById(R.id.txtSize);
				TextView qty = (TextView) view.findViewById(R.id.txtQuantity);
				TextView textColor = (TextView) view.findViewById(R.id.txtColor);
				HorizontalListView relColor = (HorizontalListView) view
						.findViewById(R.id.listViewColor);
				// imgClose = (ImageView) view.findViewById(R.id.imgClose);
				prodId.setText(AppController.subDealerOrderDetailList.get(position)
						.getProductId());
				size.setText(AppController.subDealerOrderDetailList.get(position)
						.getCaseDetail());
				qty.setText(AppController.subDealerOrderDetailList.get(position)
						.getQuantity());
				textColor.setText(AppController.subDealerOrderDetailList.get(position)
						.getColor_name());
				// color.setText(cartArrayList.get(position).getProdColor());
				/*relColor.setAdapter(new ColorGridAdapter(mActivity,
						AppController.subDealerOrderDetailList.get(position)
								.getColor_code(), 0));*/
			

					}

				
			

		else {
			view = convertView;
		}

		
		/*
		 * AppController.subDealerOrderDetailList.get(position) .getColorId(),
		 * 0));
		 */
		RelativeLayout rel1 = (RelativeLayout) view.findViewById(R.id.rel1);
		RelativeLayout rel2 = (RelativeLayout) view.findViewById(R.id.rel2);
		RelativeLayout rel3 = (RelativeLayout) view.findViewById(R.id.rel3);
		RelativeLayout rel4 = (RelativeLayout) view.findViewById(R.id.rel4);
		//RelativeLayout rel5 = (RelativeLayout) view.findViewById(R.id.rel5);

		
		if (position % 2 == 0) {
			rel1.setBackgroundColor(Color.rgb(219, 188, 188));
			rel2.setBackgroundColor(Color.rgb(219, 188, 188));
			rel3.setBackgroundColor(Color.rgb(219, 188, 188));
			rel4.setBackgroundColor(Color.rgb(219, 188, 188));
			//rel5.setBackgroundColor(Color.rgb(219, 188, 188));
		} else {
			rel1.setBackgroundColor(Color.rgb(208, 208, 208));
			rel2.setBackgroundColor(Color.rgb(208, 208, 208));
			rel3.setBackgroundColor(Color.rgb(208, 208, 208));
			rel4.setBackgroundColor(Color.rgb(208, 208, 208));
			//rel5.setBackgroundColor(Color.rgb(208, 208, 208));
		}
		
	
		return view;
	}
}
