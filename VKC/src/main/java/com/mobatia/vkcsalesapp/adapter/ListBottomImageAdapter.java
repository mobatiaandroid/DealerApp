/**
 *
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ListImageModel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author mobatia-user
 */
public class ListBottomImageAdapter extends BaseAdapter {

    Activity mActivity;
    ArrayList<ListImageModel> mImageArrayList;
    LayoutInflater mInflater;
    DisplayManagerScale mDisplaymanager;
    int mViewHeight;

    public ListBottomImageAdapter(Activity mActivity, ArrayList<ListImageModel> mImageArrayList, int height) {
        mDisplaymanager = new DisplayManagerScale(mActivity);
        this.mActivity = mActivity;
        this.mImageArrayList = mImageArrayList;
        mInflater = LayoutInflater.from(mActivity);
        mViewHeight = height;
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
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.custom_bottomgriditem, null);
        } else {
            view = convertView;
        }

        RelativeLayout relContent = (RelativeLayout) view.findViewById(R.id.relContent);
        relContent.getLayoutParams().height = (int) (mViewHeight * 0.90);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        VKCUtils.setImageFromUrl(mActivity, mImageArrayList.get(position).getUrl(),
                img, progressBar);


        return view;
    }

}
