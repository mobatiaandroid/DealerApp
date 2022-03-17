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

public class OrderDetailAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	static String value;

	public OrderDetailAdapter(Activity mActivity) {
		this.mActivity = mActivity;
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
			if (AppPrefenceManager.getUserType(mActivity).equals("7")) {
				view = mInflater.inflate(R.layout.custom_order_list_4_item,
						null);
				TextView prodId = (TextView) view.findViewById(R.id.txtProdId);
				TextView size = (TextView) view.findViewById(R.id.txtSize);
				TextView qty = (TextView) view.findViewById(R.id.txtQuantity);
				HorizontalListView relColor = (HorizontalListView) view
						.findViewById(R.id.listViewColor);
				prodId.setText(AppController.subDealerOrderDetailList.get(position)
						.getProductId());
				size.setText(AppController.subDealerOrderDetailList.get(position)
						.getCaseDetail());
				qty.setText(AppController.subDealerOrderDetailList.get(position)
						.getQuantity());
				relColor.setAdapter(new ColorGridAdapter(mActivity,
						AppController.subDealerOrderDetailList.get(position)
								.getColor_code(), 0));
				
			} else {
				view = mInflater.inflate(R.layout.custom_order_list, null);
				
				final EditText edtQty = (EditText) view.findViewById(R.id.edtQty);
				TextView prodId = (TextView) view.findViewById(R.id.txtProdId);
				TextView size = (TextView) view.findViewById(R.id.txtSize);
				TextView qty = (TextView) view.findViewById(R.id.txtQuantity);
				TextView textColor = (TextView) view.findViewById(R.id.txtColor);
				if (AppController.isEditable) {
					edtQty.setEnabled(true);
				} else {
					edtQty.setEnabled(false);
				}

				prodId.setText(AppController.subDealerOrderDetailList.get(position)
						.getProductId());
				size.setText(AppController.subDealerOrderDetailList.get(position)
						.getCaseDetail());
				if (!AppPrefenceManager.getUserType(mActivity).equals("7")) 
				{
				edtQty.setText(AppController.subDealerOrderDetailList.get(position)
						.getQuantity());
				}
				qty.setText(AppController.subDealerOrderDetailList.get(position)
						.getQuantity());

				textColor.setText(AppController.subDealerOrderDetailList.get(position).getColor_name());
				edtQty.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before,
							int count) {
						// TODO Auto-generated method stub
						AppController.status = 2;
						
						try {

							if (edtQty.getText().toString().equals("")) {
								value = "0";
							} else {
								value =edtQty.getText().toString();
							}
							if (Integer.valueOf(value) > Integer
									.valueOf(AppController.subDealerOrderDetailList
											.get(position).getQuantity())) {
								toast.show(27);
								AppController.isSubmitError = true;
								AppController.listErrors.add(String
										.valueOf(AppController.isSubmitError));

							} else if (Integer.valueOf(value) < Integer
									.valueOf(AppController.subDealerOrderDetailList
											.get(position).getQuantity())
									&& Integer.valueOf(value) >= 0) {
								if (Integer.valueOf(value) > 0) {
									AppController.subDealerOrderDetailList.get(
											position).setQuantity(value);
									AppController.listErrors.clear();
								} else {
									toast.show(29);
									AppController.isSubmitError = true;
									AppController.listErrors.add(String
											.valueOf(AppController.isSubmitError));
								}
							} else {
								AppController.isSubmitError = false;
								AppController.listErrors.clear();
							}

						} catch (Exception e) {

						}

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						AppController.status = 2;

					}

				});
			}

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
