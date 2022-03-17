package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.adapter.ProductListAdapter.ViewHolder;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ColorModel;

public class ColorGridAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ColorModel> colorModels;
    LayoutInflater mInflater;
    DisplayManagerScale mDisplayManager;
    int width, height;
    int type;
    String colorValue;


    public ColorGridAdapter(Context mcontext, ArrayList<ColorModel> colorModels, int type) {

        this.mContext = mcontext;
        this.colorModels = colorModels;
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
        getDisplayScale();
    }

    public ColorGridAdapter(Context mcontext, String colorValue, int type) {

        this.mContext = mcontext;
        this.colorValue = colorValue;
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
        getDisplayScale();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (type == 0) {
            return 1;
        } else {
            return colorModels.size();
        }
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

    CheckBox checkBoxTemp;

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View holderView;

        if (convertView == null) {


            holderView = mInflater.inflate(R.layout.custom_color_grid, parent,
                    false);

        } else {

            holderView = convertView;
        }
        GradientDrawable gd = new GradientDrawable();
        // gd.setColor(Color.parseColor(colorModels.get(position).getColorcode()));
        if (type == 1) {
            gd.setColor(VKCUtils.parseColor(colorModels.get(position)
                    .getColorcode()));
        } else {
            gd.setColor(VKCUtils.parseColor(colorValue));
        }
        gd.setCornerRadius(50);
        RelativeLayout relativeMain = new ViewHolder()
                .getColorView(holderView);
        relativeMain.setBackground(gd);
        LinearLayout lnrHolder = new ViewHolder().getLinearView(holderView);
        lnrHolder.getLayoutParams().height = (int) (height * 1.8);


        final CheckBox checkBox = new ViewHolder().getCheckBox(holderView);
        if (type == 1) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (checkBoxTemp != null) {
                    checkBoxTemp.setChecked(false);
                }

                checkBoxTemp = (CheckBox) v;
                if (checkBox.isChecked()) {

                    ProductDetailActivity.selectedFromColorList = colorModels.get(position).getColorcode();
                    ProductDetailActivity.selectedIDFromColorList = colorModels.get(position).getId();
                }

            }
        });


        return holderView;
    }

    public void getDisplayScale() {
        mDisplayManager = new DisplayManagerScale(mContext);
        width = mDisplayManager.getDeviceWidth();
        height = mDisplayManager.getDeviceHeight();
    }

    public class ViewHolder {

        /**
         * @return the view
         */
        public RelativeLayout getColorView(View holderView) {
            return (RelativeLayout) holderView.findViewById(R.id.viewColor);
        }

        public CheckBox getCheckBox(View holderView) {
            return (CheckBox) holderView.findViewById(R.id.checkBoxColor);
        }

        public LinearLayout getLinearView(View holderView) {
            return (LinearLayout) holderView.findViewById(R.id.lnrHolder);
        }


    }
}