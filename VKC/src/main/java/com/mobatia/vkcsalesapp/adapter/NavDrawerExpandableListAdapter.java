package com.mobatia.vkcsalesapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;

public class NavDrawerExpandableListAdapter extends BaseExpandableListAdapter {
    ArrayList<NavDrawerItem> navDrawerItems;
    HashMap<NavDrawerItem, ArrayList<NavDrawerItem>> hashMapNavDrawerItemChild;
    Context mContext;
    OnExploreListener onExploreListener;

    public NavDrawerExpandableListAdapter(
            Context mContext,
            ArrayList<NavDrawerItem> navDrawerItems,
            HashMap<NavDrawerItem, ArrayList<NavDrawerItem>> hashMapNavDrawerItemChild,
            OnExploreListener onExploreListener) {
        // TODO Auto-generated constructor stub
        this.hashMapNavDrawerItemChild = hashMapNavDrawerItemChild;
        this.navDrawerItems = navDrawerItems;
        this.mContext = mContext;
        this.onExploreListener = onExploreListener;

    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return navDrawerItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        if (this.hashMapNavDrawerItemChild.get(this.navDrawerItems
                .get(groupPosition)) != null)
            return this.hashMapNavDrawerItemChild.get(
                    this.navDrawerItems.get(groupPosition)).size();
        else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return this.navDrawerItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return this.hashMapNavDrawerItemChild.get(
                this.navDrawerItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        NavDrawerItem headerTitle = (NavDrawerItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        final TextView explore = (TextView) convertView
                .findViewById(R.id.explore);
        RelativeLayout exploreRl = (RelativeLayout) convertView
                .findViewById(R.id.exploreRl);

        RelativeLayout textBaseRl = (RelativeLayout) convertView
                .findViewById(R.id.textBaseRl);

        if (groupPosition <= 9) {
            imgIcon.setImageResource(headerTitle.getIcon());
            imgIcon.setVisibility(View.VISIBLE);
        } else {
            imgIcon.setVisibility(View.INVISIBLE);
            if (headerTitle.getTitle().equalsIgnoreCase("Locate us")) {
                imgIcon.setVisibility(View.VISIBLE);
                imgIcon.setImageResource(R.drawable.location);
            } else if (headerTitle.getTitle().equalsIgnoreCase("Contact us")) {
                imgIcon.setVisibility(View.VISIBLE);
                imgIcon.setImageResource(R.drawable.brand);
            } else if (headerTitle.getTitle().contains("Version")) {

                if (groupPosition == 0) {
                    imgIcon.setVisibility(View.INVISIBLE);
                }
                // imgIcon.setVisibility(View.INVISIBLE);
            } else {

                imgIcon.setImageResource(R.drawable.brand);
            }
        }

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getTitle());
        if (Integer.parseInt((navDrawerItems.get(groupPosition).getId())) < 0) {
            exploreRl.setVisibility(View.INVISIBLE);
        } else {
            exploreRl.setVisibility(View.VISIBLE);
        }

        exploreRl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (explore.getText().toString().equals("+")) {
                    onExploreListener.onExpandGroup(groupPosition);
                    explore.setText("-");
                } else {
                    onExploreListener.onCollapseGrope(groupPosition);
                    explore.setText("+");
                }


            }
        });

        textBaseRl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onItemClickListener.onItemSelected(navDrawerItems.get(groupPosition).getId());

            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final NavDrawerItem childText = (NavDrawerItem) getChild(groupPosition,
                childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText.getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemSelected(hashMapNavDrawerItemChild
                        .get(navDrawerItems.get(groupPosition))
                        .get(childPosition).getId());

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemSelected(String id);
    }

    public interface OnExploreListener {
        public void onExpandGroup(int position);

        public void onCollapseGrope(int position);
    }

}
