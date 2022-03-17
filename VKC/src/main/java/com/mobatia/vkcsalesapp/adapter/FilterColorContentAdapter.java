/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.ColorGridAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CategoryModel;
import com.mobatia.vkcsalesapp.model.ColorModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author archana.s
 * 
 */
public class FilterColorContentAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private String[] filterListString;
	private ArrayList<ColorModel> filterArrayList;
	public ArrayList<ColorModel> tempArrayList;

	public FilterColorContentAdapter(Activity mContext,
			ArrayList<ColorModel> filterArrayList,
			ArrayList<ColorModel> tempArrayList) {
		this.mContext = mContext;
		this.filterArrayList = filterArrayList;
		this.tempArrayList = tempArrayList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return filterListString.length;
		return filterArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filterArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.custom_filtercolor, null);

		} else {
			view = convertView;
		}

		GradientDrawable gd = new GradientDrawable();
		
		
		gd.setCornerRadius(50);
		gd.setAlpha(100);
		RelativeLayout relativeMain = (RelativeLayout) view
				.findViewById(R.id.viewColor);
		TextView hexColorTextView = (TextView) view
				.findViewById(R.id.hexColorTextView);

		relativeMain.setBackgroundDrawable(gd);
		final CheckBox chechBoxType = (CheckBox) view
				.findViewById(R.id.checkBoxType);
		if (filterArrayList.get(position).getColorcode().startsWith("#")) {
			gd.setColor(VKCUtils.parseColor((filterArrayList.get(position)
					.getColorcode())));
			hexColorTextView.setText(filterArrayList.get(position)
					.getColorName());
		} else {
			chechBoxType.setText("No color");
			hexColorTextView.setText("");
		}
		chechBoxType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final boolean isChecked = chechBoxType.isChecked();
				if (isChecked) {
					tempArrayList.add(filterArrayList.get(position));
					AppController.tempcolorCatArrayList.add(filterArrayList.get(position).getId());
				} else {
					removeColorModel(filterArrayList.get(position));
					AppController.tempcolorCatArrayList.remove(filterArrayList.get(position).getId());
				}
			}
		});

		if (tempArrayList.contains(filterArrayList.get(position))) {
			chechBoxType.setChecked(true);
		} else {
			chechBoxType.setChecked(false);
			// Log.v("LOG","04112014 "+filterArrayList.get(position).getName()
			// );
		}
//
		return view;
	}
	private void removeColorModel(ColorModel mColorModel){
			for(int i=0;i<tempArrayList.size();i++)
			
		{
			if(mColorModel.getColorcode().equals(tempArrayList.get(i).getColorcode())){
				tempArrayList.remove(i);
			}
		}
		
	}

}
