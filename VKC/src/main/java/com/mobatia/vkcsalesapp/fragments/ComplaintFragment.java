/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;

import org.json.JSONObject;

/**
 * @author mobatia-user
 * 
 */
public class ComplaintFragment extends Fragment implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	private TextView mTxtName;
	private EditText mEdtComplaint;
	private EditText mEdtName;
	private Button btnSend;
	private RelativeLayout relFeedbackType;
	LinearLayout searchLinear;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);
		setHasOptionsMenu(true);
		AppController.isCart=false;
		setDisplayParams();
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
		
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Complaints");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		init(mRootView, savedInstanceState);

		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(View v, Bundle savedInstanceState) {

		mTxtName = (TextView) v.findViewById(R.id.txtName);
		mEdtName = (EditText) v.findViewById(R.id.etName);
		searchLinear=(LinearLayout)v.findViewById(R.id.secSearchLL);
		searchLinear.setVisibility(View.GONE);
		mEdtName.setText(AppPrefenceManager.getUserName(getActivity()));
		mEdtComplaint = (EditText) v.findViewById(R.id.etMessage);
		btnSend = (Button) v.findViewById(R.id.imgSend);
		relFeedbackType = (RelativeLayout) v.findViewById(R.id.relFeedback);
		relFeedbackType.setVisibility(View.GONE);
		btnSend.setOnClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		getActivity().invalidateOptionsMenu();

		return true;
	}

	/**
	 * Method Name:ComplaintSubmitApi Return Type:void parameters:null Date:Feb
	 * 18, 2015 Author:Archana.S
	 * 
	 */
	public void ComplaintSubmitApi() {
		String name[] = { "user_id", "message" };
		String values[] = { AppPrefenceManager.getUserId(getActivity()),
				mEdtComplaint.getText().toString() };
		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_COMPLAINT);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
					}
				});
	}

	/**
	 * Method Name:parseResponse Return Type:void parameters:response Date:Feb
	 * 18, 2015 Author:Archana.S
	 * 
	 */
	public void parseResponse(String response) {

		try {

			JSONObject jsonObject = new JSONObject(response);
			String responseResult = jsonObject
					.getString(JSON_FEEDBACK_RESPONSE);
			if (responseResult.equals(JSON_FEEDBACK_SUCCESS)) {

				VKCUtils.showtoast(getActivity(), 12);
				clearEditText();

			} else if (responseResult.equals(JSON_FEEDBACK_FAILED)) {

				VKCUtils.showtoast(getActivity(), 13);
			}

		} catch (Exception e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		if (v == btnSend) {

			if (mEdtComplaint.getText().toString().equals("")) {
				VKCUtils.textWatcherForEditText(mEdtComplaint,
						"Mandatory field");
				VKCUtils.setErrorForEditText(mEdtComplaint, "Mandatory field");
			} else {
				ComplaintSubmitApi();
			}

		}

	}

	private void clearEditText() {
		mEdtComplaint.setText("");
	}
}
