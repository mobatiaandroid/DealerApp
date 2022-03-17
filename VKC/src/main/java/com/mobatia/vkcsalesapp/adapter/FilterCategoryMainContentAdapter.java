package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.model.CategoryModel;

public class FilterCategoryMainContentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private String[] filterListString;
    private ArrayList<CategoryModel> filterArrayList;
    public ArrayList<CategoryModel> tempArrayList;

    public FilterCategoryMainContentAdapter(Activity mContext,
                                            ArrayList<CategoryModel> filterArrayList,
                                            ArrayList<CategoryModel> tempArrayList) {
        this.mContext = mContext;
        this.filterArrayList = filterArrayList;
        this.tempArrayList = tempArrayList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // return filterListString.length;
        return filterArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return filterArrayList.get(position);
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

        final CheckBox chechBoxType = (CheckBox) view
                .findViewById(R.id.checkBoxType);
        chechBoxType.setText(filterArrayList.get(position).getName());
        chechBoxType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = chechBoxType.isChecked();
                if (isChecked) {


                    tempArrayList.add(filterArrayList.get(position));
                    AppController.tempmainCatArrayList.add(filterArrayList.get(position).getId());

                } else {

                    //tempArrayList.remove(filterArrayList.get(position));
                    removeCategoryModel(filterArrayList.get(position));
                    AppController.tempmainCatArrayList.remove(filterArrayList.get(position).getId());
                }
            }
        });

        if (tempArrayList.contains(filterArrayList.get(position))) {
            chechBoxType.setChecked(true);
        } else {
            chechBoxType.setChecked(false);
        }


        return view;
    }

    private void removeCategoryModel(CategoryModel categoryModel) {

        for (int i = 0; i < tempArrayList.size(); i++) {
            if (categoryModel.getName().equals(tempArrayList.get(i).getName())) {
                tempArrayList.remove(i);
            }
        }

    }


}
