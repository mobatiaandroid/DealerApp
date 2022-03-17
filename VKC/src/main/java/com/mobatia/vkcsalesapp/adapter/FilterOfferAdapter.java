package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.FilterActivity;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.OfferModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author archana
 */
public class FilterOfferAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] filterListString;
    private ArrayList<OfferModel> offerModels;
    public ArrayList<OfferModel> tempOfferModels;

    public FilterOfferAdapter(Activity mContext, ArrayList<OfferModel> offerModels, ArrayList<OfferModel> tempArrayList) {
        this.mContext = mContext;
        this.offerModels = offerModels;
        this.tempOfferModels = tempArrayList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return filterListString.length;
        return offerModels.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return offerModels.get(position);
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
            view = mInflater.inflate(R.layout.custom_filtercontent, null);


        } else {
            view = convertView;
        }

        final CheckBox chechBoxType = (CheckBox) view.findViewById(R.id.checkBoxType);
        chechBoxType.setText(offerModels.get(position).getName() + "%");
        chechBoxType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = chechBoxType.isChecked();
                if (isChecked) {

                    tempOfferModels.add(offerModels.get(position));
                    AppController.tempofferCatArrayList.add(offerModels.get(position).getId());
                } else {
                    removeOfferModel(offerModels.get(position));
                    AppController.tempofferCatArrayList.remove(offerModels.get(position).getId());
                }
            }
        });


        if (tempOfferModels.contains(offerModels.get(position))) {
            chechBoxType.setChecked(true);
        } else {
            chechBoxType.setChecked(false);

        }

        return view;
    }

    private void removeOfferModel(OfferModel mOfferModel) {

        for (int i = 0; i < tempOfferModels.size(); i++) {
            if (mOfferModel.getName().equals(tempOfferModels.get(i).getName())) {
                tempOfferModels.remove(i);
            }
        }

    }
}
