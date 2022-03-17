package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.HomeImageBannerModel;
import com.mobatia.vkcsalesapp.model.ProductModel;

public class HomeImageBannerAdapterFirst extends PagerAdapter {

    Activity mActivity;
    View layout;
    TextView pagenumber;
    Button click;
    int mType;
    int displayHeight;
    int displayWidth;
    ArrayList<HomeImageBannerModel> mFeedbackFormModels;
    int mListSize;
    ArrayList<ProductModel> mProductsList;

    public HomeImageBannerAdapterFirst(Activity mainActivity,
                                       ArrayList<HomeImageBannerModel> arg1, int type) {
        mActivity = mainActivity;
        mFeedbackFormModels = arg1;
        this.mType = type;
        setDisplayParam(mActivity);
        setListSize();
    }

    public HomeImageBannerAdapterFirst(Activity mainActivity,
                                       ArrayList<HomeImageBannerModel> arg1,
                                       ArrayList<ProductModel> mProductsList, int type) {
        mActivity = mainActivity;
        mFeedbackFormModels = arg1;
        this.mType = type;
        this.mProductsList = mProductsList;

        setDisplayParam(mActivity);
        setListSize();
    }

    private void setListSize() {
        if (mType == 0) {
            mListSize = mFeedbackFormModels.size();
        } else {
            if (mFeedbackFormModels.size() % 3 == 0) {
                mListSize = mFeedbackFormModels.size() / 3;
            } else {
                mListSize = (mFeedbackFormModels.size() / 3) + 1;
            }
        }
    }

    private void setDisplayParam(Activity activity) {
        DisplayManagerScale mDisplayManager = new DisplayManagerScale(activity);
        displayHeight = mDisplayManager.getDeviceHeight();
        displayWidth = mDisplayManager.getDeviceWidth();
    }

    @Override
    public int getCount() {
        return mListSize;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mType == 0) {
            layout = inflater.inflate(R.layout.custom_home_banner_pager, null);

            setViews(layout, position);
            ((ViewPager) container).addView(layout, 0);
        } else {
            layout = inflater.inflate(
                    R.layout.custom_home_banner_pager_three_image, null);
            setBottomImages(layout, position);
            ((ViewPager) container).addView(layout, 0);
        }

        return layout;
    }

    private View setBottomImages(View collection, final int position) {
        LinearLayout images = (LinearLayout) collection
                .findViewById(R.id.linearImage);

        WraperClassFooter wraperClassFooter = new WraperClassFooter(collection);
        String imageUrl1 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";
        String imageUrl3 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";
        String imageUrl2 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";

        if (position == 0) {
            imageUrl1 = mFeedbackFormModels.get(position).getBannerUrl();
            imageUrl2 = mFeedbackFormModels.get(position + 1).getBannerUrl();
            imageUrl3 = mFeedbackFormModels.get(position + 2).getBannerUrl();
        } else {
            if (((position * 3)) <= mFeedbackFormModels.size()) {
                imageUrl1 = mFeedbackFormModels.get(3 * position).getBannerUrl();
            }
            if ((((position * 3) + 1)) <= mFeedbackFormModels.size()) {
                imageUrl2 = mFeedbackFormModels.get(3 * position + 1)
                        .getBannerUrl();
            }
            if ((((position * 3) + 2)) <= mFeedbackFormModels.size()) {
                imageUrl3 = mFeedbackFormModels.get(3 * position + 1)
                        .getBannerUrl();
            }


        }


        wraperClassFooter.getFooterImage1().setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (((position * 3) + 1) <= mProductsList.size()) {

                            if (position > 0) {

                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                AppController.product_id = mProductsList.get(position * 3).getmProductName();
                                AppController.category_id = mProductsList.get(position * 3).getCategoryId();
                                intent.putExtra("MODEL",
                                        mProductsList.get(position * 3));
                                System.out.println("prod wrap "
                                        + mProductsList.get(position * 3));
                                mActivity.startActivity(intent);

                            } else {
                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                AppController.product_id = mProductsList.get(position).getmProductName();
                                AppController.category_id = mProductsList.get(position).getCategoryId();
                                intent.putExtra("MODEL",
                                        mProductsList.get(position));

                                mActivity.startActivity(intent);
                            }

                        }

                    }
                });
        wraperClassFooter.getFooterImage2().setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (((position * 3) + 2) <= mProductsList.size()) {

                            if (position > 0) {

                                AppController.product_id = mProductsList.get((position * 3) + 1).getmProductName();
                                AppController.category_id = mProductsList.get((position * 3) + 1).getCategoryId();
                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                intent.putExtra("MODEL",
                                        mProductsList.get((position * 3) + 1));

                                mActivity.startActivity(intent);
                            } else {
                                AppController.product_id = mProductsList.get(position + 1).getmProductName();
                                AppController.category_id = mProductsList.get(position + 1).getCategoryId();

                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                intent.putExtra("MODEL",
                                        mProductsList.get(position + 1));

                                mActivity.startActivity(intent);
                            }

                        }

                    }
                });
        wraperClassFooter.getFooterImage3().setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (((position * 3) + 3) <= mProductsList.size()) {

                            if (position > 0) {
                                AppController.product_id = mProductsList.get((position * 3) + 2).getmProductName();
                                AppController.category_id = mProductsList.get((position * 3) + 2).getCategoryId();
                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                intent.putExtra("MODEL",
                                        mProductsList.get((position * 3) + 2));
                                mActivity.startActivity(intent);

                            } else {

                                AppController.product_id = mProductsList.get(position + 2).getmProductName();
                                AppController.category_id = mProductsList.get(position + 2).getCategoryId();
                                Intent intent = new Intent(mActivity,
                                        ProductDetailActivity.class);
                                intent.putExtra("MODEL",
                                        mProductsList.get(position + 2));
                                mActivity.startActivity(intent);
                            }


                        }

                    }
                });
        VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl1,
                wraperClassFooter.getFooterImage1(),
                wraperClassFooter.getProgressBar1());
        VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl2,
                wraperClassFooter.getFooterImage2(),
                wraperClassFooter.getProgressBar2());
        VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl3,
                wraperClassFooter.getFooterImage3(),
                wraperClassFooter.getProgressBar3());

        return images;
    }

    class WraperClassFooter {
        View view;

        public WraperClassFooter(View view) {
            this.view = view;
        }

        public ImageView getFooterImage1() {

            ImageView images1 = (ImageView) view
                    .findViewById(R.id.footerImage1);
            images1.getLayoutParams().width = displayWidth / 3;

            images1.setScaleType(ScaleType.CENTER_CROP);

            return images1;

        }

        public ImageView getFooterImage2() {

            ImageView images1 = (ImageView) view
                    .findViewById(R.id.footerImage2);
            images1.getLayoutParams().width = displayWidth / 3;

            images1.setScaleType(ScaleType.CENTER_CROP);

            return images1;

        }

        public ImageView getFooterImage3() {

            ImageView images1 = (ImageView) view
                    .findViewById(R.id.footerImage3);
            images1.getLayoutParams().width = displayWidth / 3;

            images1.setScaleType(ScaleType.CENTER_CROP);

            return images1;

        }

        public ProgressBar getProgressBar1() {
            return (ProgressBar) view.findViewById(R.id.progressBar1);

        }

        public ProgressBar getProgressBar2() {
            return (ProgressBar) view.findViewById(R.id.progressBar2);

        }

        public ProgressBar getProgressBar3() {
            return (ProgressBar) view.findViewById(R.id.progressBar3);

        }

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

        VKCUtils.setImageFromUrl(
                mActivity,
                mFeedbackFormModels.get(arg0).getBannerUrl()/* "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg" */,
                wraperClass.getImageBanner(), wraperClass.getProgressBar());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(mActivity,
                        ProductDetailActivity.class);
                intent.putExtra("MODEL", mProductsList.get(arg0));
                AppController.product_id = mProductsList.get(arg0).getmProductName();
                AppController.category_id = mProductsList.get(arg0).getCategoryId();
                mActivity.startActivity(intent);

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
