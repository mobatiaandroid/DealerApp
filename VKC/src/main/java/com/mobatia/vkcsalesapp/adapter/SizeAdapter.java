package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.model.QuickSizeModel;

public class SizeAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	private ArrayList<QuickSizeModel> sizeArrayList;
	public ArrayList<QuickSizeModel> tempArrayList;

	public SizeAdapter(Activity mContext,
			ArrayList<QuickSizeModel> sizeArrayList) {
		this.mContext = mContext;
		AppController.arrayListSize = sizeArrayList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return filterListString.length;
		return AppController.arrayListSize .size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return AppController.arrayListSize .get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		final CheckBox chechBoxType = null;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.item_size_adapter, null);
			holder.checkboxSize = (CheckBox) view
					.findViewById(R.id.checkBoxSize);
			holder.sizeName=(TextView) view
					.findViewById(R.id.textSizeName);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// gd.setStroke(strokeWidth, strokeColor);
//System.out.println("Size Name: "+sizeArrayList.get(position).getSizeName());
		//holder.checkboxSize.setText(sizeArrayList.get(position).getSizeName());
		holder.sizeName.setText(AppController.arrayListSize.get(position).getSizeName());
		holder.checkboxSize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					//AppController.sizeArrayList.get(position).setSelected(isChecked);
					AppController.arrayListSize.get(position).setSelected(true);
				}
				else
				{
					AppController.arrayListSize.get(position).setSelected(false);
				}
				
			}
		});

		/* To retain a checkbox selection state */
		/*
		 * if (tempArrayList.contains(sizeArrayList.get(position))) {
		 * chechBoxType.setChecked(true); //
		 * Log.v("LOG","04112014 "+filterArrayList.get(position).getName() // );
		 * } else { chechBoxType.setChecked(false); //
		 * Log.v("LOG","04112014 "+filterArrayList.get(position).getName() // );
		 * }
		 */

		return view;
	}

	static class ViewHolder {
		CheckBox checkboxSize;
		TextView sizeName;
	}

}
