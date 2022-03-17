package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel;

public class SubDealerOrderDetailsAdapter extends BaseAdapter {
	Activity mActivity;
	// ArrayList<SubDealerOrderDetailModel> subdealersModels;
	LayoutInflater mLayoutInflater;
	String value;

	public SubDealerOrderDetailsAdapter(Activity mActivity,
			ArrayList<SubDealerOrderDetailModel> subdealersModels) {
		// this.subdealersModels = subdealersModels;
		this.mActivity = mActivity;
		// System.out.println("Sub Dealer Details" + subdealersModels.size());
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

		View view = null;
		final CustomToast toast = new CustomToast(mActivity);
		LayoutInflater mLayoutInflater = ((Activity) mActivity)
				.getLayoutInflater();
		if (convertView == null) {
			view = mLayoutInflater.inflate(R.layout.subdealer_order_detail,
					null);
			TextView approvedqty = (TextView) view
					.findViewById(R.id.textViewApprovedQuantity);
			TextView txtProductId = (TextView) view
					.findViewById(R.id.textViewProductId);
			TextView txtDescription = (TextView) view
					.findViewById(R.id.textViewDescription);
			TextView txtQuantity = (TextView) view
					.findViewById(R.id.textViewQuantity);
			TextView txtDate = (TextView) view
					.findViewById(R.id.textViewSubOrderDate);
			final EditText edtQuantity = (EditText) view
					.findViewById(R.id.edtQuantity);
			TextView txtCase = (TextView) view.findViewById(R.id.textViewCase);
			if (AppPrefenceManager.getUserType(mActivity).equals("7")) {
				edtQuantity.setVisibility(View.GONE);
				approvedqty.setVisibility(View.GONE);
				txtCase.setVisibility(View.GONE);
			} else {
				edtQuantity.setVisibility(View.VISIBLE);
				approvedqty.setVisibility(View.VISIBLE);
				txtCase.setVisibility(View.VISIBLE);
			}

			RelativeLayout updateButton = (RelativeLayout) view
					.findViewById(R.id.updateButton);
			txtProductId.setText("Product Id : "
					+ AppController.subDealerOrderDetailList.get(position)
							.getProductId());
			txtDescription.setText("Description : "
					+ AppController.subDealerOrderDetailList.get(position)
							.getDescription());
			txtQuantity.setText("Order Quantity :"
					+ AppController.subDealerOrderDetailList.get(position)
							.getQuantity());
			txtCase.setText("Case : "+AppController.subDealerOrderDetailList.get(position).getCaseDetail());
			edtQuantity.setText(AppController.subDealerOrderDetailList.get(
					position).getQuantity());
			txtDate.setText("Order Date : "
					+ AppController.subDealerOrderDetailList.get(position)
							.getOrderDate());

			/*
			 * VKCUtils.formatDateWithInput(
			 * AppController.subDealerOrderDetailList
			 * .get(position).getOrderDate(), "dd MMM yyyy",
			 * "yyyy-MM-dd hh:mm:ss"));
			 */
			edtQuantity.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					AppController.status = 2;

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					AppController.status = 2;

					try {

						if (edtQuantity.getText().toString().trim().equals("")) {
							value = "0";
						} else {
							value = edtQuantity.getText().toString().trim();
						}
						if (Integer.valueOf(value) > Integer
								.valueOf(AppController.subDealerOrderDetailList
										.get(position).getQuantity())) {
							Log.i("Edit Valuee", ""
									+ edtQuantity.getText().toString());
							Log.i("List Values", ""
									+ AppController.subDealerOrderDetailList
											.get(position).getQuantity());

							toast.show(27);
							AppController.isSubmitError = true;
							AppController.listErrors.add(String
									.valueOf(AppController.isSubmitError));

						} else if (Integer.valueOf(value) < Integer
								.valueOf(AppController.subDealerOrderDetailList
										.get(position).getQuantity())
								&& Integer.valueOf(value) >= 0) {
							if (Integer.valueOf(value) > 0) {
								AppController.TempSubDealerOrderDetailList.get(
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

			});
			/*
			 * updateButton.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub
			 * 
			 * } });
			 */

		}

		else {
			view = convertView;
		}

		/*
		 * setText( "Order Date : " + VKCUtils.formatDateWithInput(
		 * subdealersModels.get(position).getOrderDate(), "dd MMM yyyy",
		 * "yyyy-MM-dd hh:mm:ss"));
		 */
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
