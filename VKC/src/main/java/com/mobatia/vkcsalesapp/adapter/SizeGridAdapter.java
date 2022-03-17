/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.adapter.ColorGridAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CaseModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

/**
 * @author Archana S
 * 
 */
public class SizeGridAdapter extends BaseAdapter {

	Context mContext;

	ArrayList<CaseModel> caseModels;

	LayoutInflater mInflater;

	private View view;
	DisplayManagerScale mDisplayManager;
	int width, height;
	

	public SizeGridAdapter(Context mcontext, ArrayList<CaseModel> caseModels) {

		this.mContext = mcontext;
		this.caseModels = caseModels;
		mInflater = LayoutInflater.from(mContext);
		getDisplayScale();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(caseModels!=null){
		return caseModels.size();
		}else{
			return 0;
		}
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
	CheckBox checkBoxTemp;
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View holderView;

		if (convertView == null) {

			holderView = mInflater.inflate(R.layout.custom_size_grid, parent,
					false);

		} else {

			holderView = convertView;
		}

		RelativeLayout relativeMain = new ViewHolder().getColorView(holderView);
		TextView txtSize = new ViewHolder().getTextView(holderView);
		txtSize.setText(caseModels.get(position).getName());

		LinearLayout lnrHolder = new ViewHolder().getLinearView(holderView);
		lnrHolder.getLayoutParams().height = (int) (height * 1.8);

		// Toast.makeText(mContext,
		// "Height:"+height+","+lnrHolder.getLayoutParams().height,
		// 1000).show();
		final CheckBox checkBox = new ViewHolder().getCheckBox(holderView);
		
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(checkBoxTemp!=null){
					checkBoxTemp.setChecked(false);
				}
				
				checkBoxTemp=(CheckBox) v;
				
				if(checkBox.isChecked()){

				ProductDetailActivity.selectedFromSizeList=caseModels.get(position).getName();
				ProductDetailActivity.selectedIDFromSizeList=caseModels.get(position).getId();
				//AppController.size=caseModels.get(position).getName();
				}

				
				
				
			}
		});

		return holderView;
	}

	public void getDisplayScale() {
		mDisplayManager = new DisplayManagerScale(mContext);
		width = mDisplayManager.getDeviceWidth();
		height = mDisplayManager.getDeviceHeight();
	}

	public class ViewHolder {

		/**
		 * @return the view
		 */
		public RelativeLayout getColorView(View holderView) {
			return (RelativeLayout) holderView.findViewById(R.id.viewColor);
		}

		public CheckBox getCheckBox(View holderView) {
			return (CheckBox) holderView.findViewById(R.id.checkBoxColor);
		}

		public LinearLayout getLinearView(View holderView) {
			return (LinearLayout) holderView.findViewById(R.id.lnrHolder);
		}

		public TextView getTextView(View holderView) {
			return (TextView) holderView.findViewById(R.id.textView1);
		}
	}

}
