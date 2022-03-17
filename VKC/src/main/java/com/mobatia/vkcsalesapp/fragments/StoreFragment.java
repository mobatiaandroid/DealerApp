package com.mobatia.vkcsalesapp.fragments;

import com.mobatia.vkcsalesapp.R;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreFragment extends Fragment {
	// this Fragment will be called from MainActivity
	public StoreFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.store_activity_fragment, container, false);
         
        return rootView;
    }
}