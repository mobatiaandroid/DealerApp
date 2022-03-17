package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author archana
 *
 */
public class FilterAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	private String[] filterListString;
	int[] mFilterImageList;
	
	public FilterAdapter(Context mContext,String[] filterListString)
	{
		this.mContext=mContext;
		this.filterListString=filterListString;
		int ImageList[] = { R.drawable.category,R.drawable.category,R.drawable.men,
				R.drawable.brand, R.drawable.price,R.drawable.men,R.drawable.offers };
		mFilterImageList=ImageList;
		mInflater=LayoutInflater.from(mContext); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filterListString.length;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		
		
		if(convertView==null)
		{
			view=mInflater.inflate(R.layout.custom_filterlayout, null);
			
			
		}else{
			view=convertView;
		}
		
		ImageView imgFilter=(ImageView)view.findViewById(R.id.imgOption);
		TextView txtFilter=(TextView)view.findViewById(R.id.txtOption);
		
		imgFilter.setImageResource(mFilterImageList[position]);
		txtFilter.setText(filterListString[position]);
		return view;
	}

}
