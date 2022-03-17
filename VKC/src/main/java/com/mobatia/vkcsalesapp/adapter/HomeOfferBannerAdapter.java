package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.OfferModel;

public class HomeOfferBannerAdapter extends PagerAdapter {

    Activity mActivity;
    View layout;
    int displayHeight;
    int displayWidth;
    ArrayList<OfferModel> offerModels;

    public HomeOfferBannerAdapter(Activity mActivity,
                                  ArrayList<OfferModel> offerModels/*, DisplayView displayView*/) {
        this.mActivity = mActivity;
        this.offerModels = offerModels;
        setDisplayParam(mActivity);

    }

    private void setDisplayParam(Activity activity) {
        DisplayManagerScale mDisplayManager = new DisplayManagerScale(activity);
        displayHeight = mDisplayManager.getDeviceHeight();
        displayWidth = mDisplayManager.getDeviceWidth();
    }

    @Override
    public int getCount() {
        return offerModels.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout = inflater
                .inflate(R.layout.custom_home_offer_banner_pager, null);

        setViews(layout, position);
        ((ViewPager) container).addView(layout, 0);

        return layout;
    }

    class WraperClass {
        View view;

        public WraperClass(View view) {
            this.view = view;
        }

        public ImageView getImageBanner() {
            ImageView imageView;
            imageView = (ImageView) view.findViewById(R.id.bannerImageView);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            return imageView;

        }

        public ProgressBar getProgressBar() {
            return (ProgressBar) view.findViewById(R.id.progressBar);

        }

    }

    private void setViews(View view, final int arg0) {
        final WraperClass wraperClass = new WraperClass(view);

        VKCUtils.setImageFromUrl(mActivity, offerModels.get(arg0)
                .getOfferBanner(), wraperClass.getImageBanner(), wraperClass
                .getProgressBar());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DashboardFActivity.dashboardFActivity.setDisplayView();
                AppPrefenceManager.saveListingOption(mActivity, "1");
                AppPrefenceManager.saveOfferIDs(mActivity, offerModels.get(arg0).getId());
            }
        });

    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
