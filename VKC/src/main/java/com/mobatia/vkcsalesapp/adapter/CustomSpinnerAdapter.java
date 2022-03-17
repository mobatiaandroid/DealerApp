package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Contacts.People;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.model.UserModel;

/*public class CustomSpinnerAdapter extends BaseAdapter  {
	ArrayList<UserModel> listUsers,filteredList,mListAll;
	Context mContext;
    private ArrayFilter mFilter;

	public CustomSpinnerAdapter(ArrayList<UserModel> listUsers, Context context) {

		this.listUsers = listUsers;
		this.mContext = context;
		this.mListAll = listUsers;
		filteredList=new ArrayList<>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listUsers.size();
	}

	@Override
	public UserModel getItem(int position) {
		// TODO Auto-generated method stub
		return listUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = (LayoutInflater) mContext
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.item_custom_spinner, parent,
		            false);
		    TextView nameView = (TextView) rowView.findViewById(R.id.textUserName);
		 

		    nameView.setText(listUsers.get(position).getUserName());
		   

		    return rowView;
	}

	@Override
	public Filter getFilter() {

		 if (mFilter == null) {
	            mFilter = new ArrayFilter();
	        }
	        return mFilter;
	}
	

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {

            FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<String>(fullList);
				}
			}

			if (prefix == null || prefix.length() == 0) {
                final ArrayList<String> list;
                synchronized (mLock) {
					list = new ArrayList<String>(mOriginalValues);
				}
                results.values = list;
                results.count = list.size();
			} else {
				final String prefixString = prefix.toString().toLowerCase();

				final ArrayList<String> values;

                synchronized (mLock) {
                    values = new ArrayList<String>(mOriginalValues);
                }
                results.values = values;
                results.count = values.size();

				final int count = values.size();

				final ArrayList<String> newValues = new ArrayList<String>();

				for (int i = 0; i < count; i++) {
					String item = values.get(i);
					if (item.toLowerCase().contains(prefixString)) {
						newValues.add(item);
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            fullList = (ArrayList<String>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }


    }
}*/
