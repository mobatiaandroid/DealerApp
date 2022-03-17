/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ProductImages;

/**
 * @author mobatia-user
 * 
 */
public class ListImageAdapter extends BaseAdapter {

	Activity mActivity;
	ArrayList<ProductImages> mImageArrayList;
	LayoutInflater mInflater;
	DisplayManagerScale displayManagerScale;
	int viewWidth;
	int viewHeight;
	float dimension;
	int imageWidthHeight;

	public ListImageAdapter(Activity mActivity,
			ArrayList<ProductImages> mImageArrayList) {

		this.mActivity = mActivity;
		this.mImageArrayList = mImageArrayList;
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(mActivity); 
		imageWidthHeight= displayManagerScale.getDeviceWidth();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImageArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.custom_griditem, null);
		} else {
			view = convertView;
		}

		ImageView img = (ImageView) view.findViewById(R.id.imageView);
		img.setScaleType(ScaleType.CENTER_CROP);
		final ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar1);

		VKCUtils.setImageFromUrl(mActivity, mImageArrayList.get(position)
				.getImageName(), img, progressBar);
		TextView textName=(TextView)view.findViewById(R.id.textProductName);
		textName.setText(mImageArrayList.get(position).getProductName());
		

		return view;
	}

}
