/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.CreditPaymentModel;

/**
 * @author mobatia-user
 * 
 */
public class CreditPaymentStatusAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<CreditPaymentModel> creditStatusModels;

	public CreditPaymentStatusAdapter(Activity mActivity,
			ArrayList<CreditPaymentModel> creditStatusModels) {
		this.mActivity = mActivity;
		this.creditStatusModels = creditStatusModels;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		return creditStatusModels.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView == null) {
			view = mInflater
					.inflate(R.layout.custom_creditpayment_status, null);
		} else {
			view = convertView;
		}
		WraperClass wraperClass = new WraperClass(view);
		wraperClass.getCustomerNameTxt().setText(
				"Customer name :  "
						+ creditStatusModels.get(position).getmName());
		wraperClass.getCustomerPlaceTxt().setText(
				"Place :  " + creditStatusModels.get(position).getCity());
		wraperClass.getPendingOrderOneTxt().setText(
				creditStatusModels.get(position).getmType1100());
		wraperClass.getPendingOrderTwoTxt().setText(
				creditStatusModels.get(position).getmType1200());
		wraperClass.getPendingOrderThreeTxt().setText(
				creditStatusModels.get(position).getmType1300());
		wraperClass.getPendingOrderFourTxt().setText(
				creditStatusModels.get(position).getmType1400());
		wraperClass.getPendingOrderFiveTxt().setText(
				creditStatusModels.get(position).getmType1500());
		wraperClass.getPendingOrderSixTxt().setText(
				creditStatusModels.get(position).getmType1600());
		wraperClass.getPendingOrderSevenTxt().setText(
				creditStatusModels.get(position).getmType2000());
		wraperClass.getTotalBalance().setText(
				creditStatusModels.get(position).getmTotalBalance());
		wraperClass.getTotalDue().setText(
				creditStatusModels.get(position).getmTotalDue());
		return view;
	}

	private class WraperClass {
		View view = null;

		public WraperClass(View view) {
			this.view = view;
			// TODO Auto-generated constructor stub
		}

		public TextView getCustomerNameTxt() {
			return (TextView) view.findViewById(R.id.textViewName);
		}

		public TextView getCustomerPlaceTxt() {
			return (TextView) view.findViewById(R.id.textViewPlace);
		}

		public TextView getPendingOrderOneTxt() {
			return (TextView) view.findViewById(R.id.textViewPendingOrdOneData);
		}

		public TextView getPendingOrderTwoTxt() {
			return (TextView) view.findViewById(R.id.textViewPendingOrdTwoData);
		}

		public TextView getPendingOrderThreeTxt() {
			return (TextView) view
					.findViewById(R.id.textViewPendingOrdThreeData);
		}

		public TextView getPendingOrderFourTxt() {
			return (TextView) view
					.findViewById(R.id.textViewPendingOrdFourData);
		}

		public TextView getPendingOrderFiveTxt() {
			return (TextView) view
					.findViewById(R.id.textViewPendingOrdFiveData);
		}

		public TextView getPendingOrderSixTxt() {
			return (TextView) view.findViewById(R.id.textViewPendingOrdSixData);
		}
		
		public TextView getPendingOrderSevenTxt() {
			return (TextView) view.findViewById(R.id.textViewPendingOrdSevenData);
		}

		public TextView getTotalBalance() {
			return (TextView) view.findViewById(R.id.textViewTotalBalanceData);
		}

		public TextView getTotalDue() {
			return (TextView) view.findViewById(R.id.textViewTotalDueData);
		}

	}
}
