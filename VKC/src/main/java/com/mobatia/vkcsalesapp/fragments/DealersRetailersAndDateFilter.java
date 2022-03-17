package com.mobatia.vkcsalesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.SalesRepOrderList;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;

public class DealersRetailersAndDateFilter extends Fragment implements
		VKCUrlConstants {
	// this Fragment will be called from MainActivity
	private View mRootView;
	ImageView imageViewSearch;

	public DealersRetailersAndDateFilter() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(
				R.layout.dealers_retailers_and_date_filter, container, false);
		init(mRootView, savedInstanceState);
		AppController.isCart=false;
		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		imageViewSearch = (ImageView) v.findViewById(R.id.imageViewSearch);
		imageViewSearch.setOnClickListener(new OnClickEvent());
		
	}

	class OnClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), SalesRepOrderList.class);
			startActivity(intent);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}
