package com.mobatia.vkcsalesapp.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter implements Serializable {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		View divider = (View) convertView.findViewById(R.id.viewDivider);

		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		if (navDrawerItems.get(position).getTitle().contains("Version")) {
			
			divider.setVisibility(View.GONE);
			txtTitle.setTextColor(context.getResources().getColor(R.color.vkcred));
			txtTitle.setText(navDrawerItems.get(position).getTitle());
		} else {
			txtTitle.setText(navDrawerItems.get(position).getTitle());
			divider.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
