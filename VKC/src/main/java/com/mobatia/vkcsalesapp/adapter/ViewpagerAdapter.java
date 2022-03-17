package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ProductImages;

/**
 * @author archana.s
 * 
 */
public class ViewpagerAdapter extends PagerAdapter {

	Activity mActivity;
	ArrayList<ProductImages> mImageList;

	LayoutInflater mInflater;

	private View view;

	public ViewpagerAdapter(Activity mActivity,
			ArrayList<ProductImages> mImageList) {
		this.mActivity = mActivity;
		this.mImageList = mImageList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// TODO Auto-generated method stub

		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = mInflater.inflate(R.layout.custom_viepagerlayout,
				container, false);

		ImageView img = (ImageView) itemView.findViewById(R.id.imageView);
		img.setScaleType(ScaleType.CENTER_CROP);
		final ProgressBar progressBar = (ProgressBar) itemView
				.findViewById(R.id.progressBar1);


		VKCUtils.setImageFromUrl(mActivity, mImageList.get(position)
				.getImageName(), img, progressBar);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
				LayoutInflater inflater = mActivity.getLayoutInflater();
				View dialogView = inflater.inflate(R.layout.layout_image_zoom, null);
				dialogBuilder.setView(dialogView);
				WebView webView = (WebView) dialogView.findViewById(R.id.imageLarge);
				ImageView imageClose = (ImageView) dialogView.findViewById(R.id.imageClose);
				webView.getSettings().setBuiltInZoomControls(true);
				webView.getSettings().setDisplayZoomControls(false);
				webView.setInitialScale(250);
				webView.setScrollbarFadingEnabled(true);
				webView.loadUrl(mImageList.get(position)
					.getImageName());
				final AlertDialog alertDialog = dialogBuilder.create();
				alertDialog.show();
				
				imageClose.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
					}
				});
			}
		});
		((ViewPager) container).addView(itemView);
		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}

}
