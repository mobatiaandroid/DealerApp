package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.ArticleModel;

public class ArticleAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	List<ArticleModel> ArticleArrayList = new ArrayList<ArticleModel>();

	public ArticleAdapter(Activity mActivity,
			List<ArticleModel> articleArrayList) {
		this.mActivity = mActivity;

		this.ArticleArrayList = articleArrayList;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ArticleArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.article_list_item, null);
			TextView article = (TextView) view.findViewById(R.id.txt_articleNo);
			article.setText(ArticleArrayList.get(position).getArticle_no());
		} else {
			view = convertView;
		}

		return view;

	}
}