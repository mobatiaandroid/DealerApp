/**
 *
 */
package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog.OnDialogItemSelectListener;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.DealersCityModel;
import com.mobatia.vkcsalesapp.model.DealersDistrictModel;
import com.mobatia.vkcsalesapp.model.DealersStateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class SignUpActivity extends AppCompatActivity implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants {

	private EditText mUserName;
	private EditText mEmail;
	private EditText mPhoneNumber;
	private EditText mPassword;
	private EditText mConfirmPassword;
	private EditText mShopName;
	private EditText mAddress;
	TextView mState;
	TextView mDistrict;
	private Button btnRegister;
	private Activity mActivity;
	ArrayList<DealersStateModel> dealersStateModels = new ArrayList<DealersStateModel>();
	ArrayList<DealersDistrictModel> districtModels = new ArrayList<DealersDistrictModel>();
	ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
	CustomToast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		toast = new CustomToast(SignUpActivity.this);
		mActivity = this;
		final ActionBar abar = getSupportActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_imageview, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		initialiseUI();
		setActionBar();
	}

	private void initialiseUI() {
		mUserName = (EditText) findViewById(R.id.etUserName);
		mEmail = (EditText) findViewById(R.id.etEmail);
		mPhoneNumber = (EditText) findViewById(R.id.etPhone);
		mPassword = (EditText) findViewById(R.id.etPassword);
		mConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
		mShopName = (EditText) findViewById(R.id.etShopName);
		mAddress = (EditText) findViewById(R.id.etAddress);
		mState = (TextView) findViewById(R.id.etState);
		mDistrict = (TextView) findViewById(R.id.etDistrict);
		btnRegister = (Button) findViewById(R.id.btRegister);
		btnRegister.setOnClickListener(this);
		if (VKCUtils.checkInternetConnection(SignUpActivity.this)) {

			getStateApi();
		} else {

			VKCUtils.showtoast(SignUpActivity.this, 0);
		}
		mState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] states = new String[dealersStateModels.size()];
				for (int i = 0; i < dealersStateModels.size(); i++) {
					states[i] = dealersStateModels.get(i).getStateName();
				}

				StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
						SignUpActivity.this, states, mState, "States",
						new OnDialogItemSelectListener() {

							@Override
							public void itemSelected(int position) {
								// TODO Auto-generated method stub
								getDistrictApi(dealersStateModels.get(position)
										.getStateCode());
							}
						});
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				dialog.setCancelable(true);
				dialog.show();
				mDistrict.setText("");

			}
		});
		mDistrict.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mState.getText().length() != 0) {
                    String[] district = new String[districtModels.size()];
                    for (int i = 0; i < districtModels.size(); i++) {
                        district[i] = districtModels.get(i).getDistrictName();
                    }
                    StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
                            SignUpActivity.this, district, mDistrict,
                            "District", new OnDialogItemSelectListener() {

                        @Override
                        public void itemSelected(int position) {

                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);
                    dialog.show();
                    mDistrict.setText("");
                } else {
                    VKCUtils.showtoast(SignUpActivity.this, 20);
                }

            }
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (mPhoneNumber.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mPhoneNumber, "Mandatory field");
			VKCUtils.setErrorForEditText(mPhoneNumber, "Mandatory field");
		} else if (mUserName.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mUserName, "Mandatory field");
			VKCUtils.setErrorForEditText(mUserName, "Mandatory field");

		} else if (mShopName.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mShopName, "Mandatory field");
			VKCUtils.setErrorForEditText(mShopName, "Mandatory field");

		} else if (mAddress.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mAddress, "Mandatory field");
			VKCUtils.setErrorForEditText(mAddress, "Mandatory field");

		}

		else if (!VKCUtils.isValidEmail(mEmail.getText().toString())) {
			VKCUtils.textWatcherForEditText(mEmail, "Enter valid Email ID");
			VKCUtils.setErrorForEditText(mEmail, "Enter valid Email ID");

		} else if (mPassword.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mPassword, "Mandatory field");
			VKCUtils.setErrorForEditText(mPassword, "Mandatory field");

		} else if (mConfirmPassword.getText().toString().equals("")) {
			VKCUtils.textWatcherForEditText(mConfirmPassword, "Mandatory field");
			VKCUtils.setErrorForEditText(mConfirmPassword, "Mandatory field");

		} else if (!mPassword.getText().toString()
				.equals(mConfirmPassword.getText().toString())) {
			VKCUtils.textWatcherForEditText(mConfirmPassword,
					"Passwords mismatch");
			VKCUtils.setErrorForEditText(mConfirmPassword, "Passwords mismatch");

		} else if (mState.getText().toString().equals("")) {

			toast.show(33);

		} else if (mDistrict.getText().toString().equals("")) {
			toast.show(34);

		} else {

			loginRequestApi();

		}

	}

	public void loginRequestApi() {
		String name[] = { "name", "email", "phone", "password", "imei_no",
				"shopName", "address", "state", "district" };

		String device_id="";

		if(Build.VERSION.SDK_INT >= 27)
		{
			device_id="";
		}

		else
		{
			device_id=VKCUtils.getDeviceID(mActivity);
		}
		String values[] = { mUserName.getText().toString(),
				mEmail.getText().toString(), mPhoneNumber.getText().toString(),
				mPassword.getText().toString(),
				device_id,
				mShopName.getText().toString(), mAddress.getText().toString().trim(),
				mState.getText().toString(), mDistrict.getText().toString().trim() };
		final VKCInternetManager manager = new VKCInternetManager(
				LOGIN_REQUEST_URL);

		manager.getResponsePOST(SignUpActivity.this, name, values,
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

	public void parseResponse(String response) {

		try {

			JSONObject jsonObject = new JSONObject(response);
			String responseResult = jsonObject
					.getString(JSON_LOGINREQ_RESPONSE);
			if (responseResult.equals(JSON_LOGINREQ_SUCCESS)) {

				VKCUtils.showtoast(mActivity, 6);
				finish();
			} else if (responseResult.equals(JSON_LOGINREQ_FAILED)) {

				VKCUtils.showtoast(mActivity, 7);
			} else if (responseResult.equals(jSON_LOGINREQ_EMAILEXISTS)) {

				VKCUtils.showtoast(mActivity, 8);
			}

		} catch (Exception e) {

		}

	}

	@SuppressLint("NewApi")
	public void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
		actionBar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}

	private void getStateApi() {
		final VKCInternetManager manager = new VKCInternetManager(
				DEALERS_GETSTATE);

		manager.getResponse(this, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				parseJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				// mIsError = true;

			}
		});

	}

	private void getDistrictApi(final String stateCode) {
		final VKCInternetManager manager = new VKCInternetManager(DEALERS_GETDISTRICT);

		String name[] = { "state" };
		String value[] = { stateCode };
		System.out.println("State Code:" + value[0]);
		manager.getResponsePOST(this, name, value, new ResponseListener() {
			@Override
			public void responseSuccess(String successResponse) {
				parseDistrictJSON(successResponse, stateCode);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void parseDistrictJSON(String successResponse, String stateCode) {
		// TODO Auto-generated method stub
		districtModels = new ArrayList<DealersDistrictModel>();
		try {
			JSONObject jsonObject = new JSONObject(successResponse);
			JSONArray respArray = jsonObject.getJSONArray("response");
			for (int i = 0; i < respArray.length(); i++) {
				districtModels.add(parseDistrict(respArray.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private DealersDistrictModel parseDistrict(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
		DealersDistrictModel dealersDistrictModel = new DealersDistrictModel();
		dealersDistrictModel.setDistrictName(jsonObject.optString("district"));

		return dealersDistrictModel;

	}

	private void parseJSON(String successResponse) {
		// TODO Auto-generated method stub

		try {
			JSONObject respObj = new JSONObject(successResponse);
			JSONArray respArray = respObj.getJSONArray("states");
			for (int i = 0; i < respArray.length(); i++) {
				dealersStateModels.add(parseSateObject(respArray
						.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private DealersStateModel parseSateObject(JSONObject objState) {
		DealersStateModel stateModel = new DealersStateModel();
		stateModel.setStateCode(objState.optString("state"));
		stateModel.setStateName(objState.optString("state_name"));

		return stateModel;
		// TODO Auto-generated method stub

	}

}
