/**
 * 
 */
package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Archana S
 * 
 */

public class LoginActivity extends AppCompatActivity implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants, VKCDbConstants {


	private TextView mTxtSignUp, mTxtForgot;
	private EditText mUserName;
	private EditText mPassword;
	private Button mBtnLogin;
	private Button mBtnSignUp;
	private Activity mActivity;
	String userType;
	private int dealerCount;
	private int roleID;
	private String[] navMenuTitles;
	private String[] categoryIdList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginscreen);
		mActivity = this;
		initialiseUI();
		final ActionBar abar = getSupportActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_imageview, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		abar.setCustomView(viewActionBar, params);

		abar.setDisplayShowCustomEnabled(true);
		getPassedValues();

		setActionBar();
		// checkDatabase();
	}
/*
	protected void checkDatabase() {
		DatabaseHelper myDbHelper = new DatabaseHelper(LoginActivity.this,
				DBNAME);
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
			myDbHelper.close();
		} catch (SQLException sqle) {
			Log.v("", "Exception in thread");
			throw sqle;
		}
	}*/

	private void getPassedValues() {

		AppController.navMenuTitles = getIntent().getExtras().getStringArray(
				"MAINCATEGORYNAMELIST");

		AppController.categoryIdList = getIntent().getExtras().getStringArray(
				"MAINCATEGORYIDLIST");
	}

	private void initialiseUI() {
		mUserName = (EditText) findViewById(R.id.etUserName);
		mPassword = (EditText) findViewById(R.id.etPassword);
		mBtnLogin = (Button) findViewById(R.id.btLogin);
		mBtnSignUp = (Button) findViewById(R.id.btLoginReq);
		mTxtSignUp = (TextView) findViewById(R.id.txtSignUp);
		mTxtForgot = (TextView) findViewById(R.id.textForgot);
		mBtnLogin.setOnClickListener(this);
		// mTxtSignUp.setOnClickListener(this);
		mBtnSignUp.setOnClickListener(this);
		mTxtForgot.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == mBtnLogin) {

			if ((mUserName.getText().toString().equalsIgnoreCase(""))
					|| (mPassword.getText().toString().equalsIgnoreCase(""))) {

				// CustomToast customToast = new CustomToast(mActivity);
				// customToast.show(10);
				VKCUtils.showtoast(mActivity, 10);
			} else {

				if (VKCUtils.checkInternetConnection(mActivity)) {
					LoginApi();
				} else {
					VKCUtils.showtoast(mActivity, 2);
				}
				clearValues();

			}
		}
		// else if (v == mTxtSignUp) {
		// Intent mIntent = new Intent(LoginActivity.this,
		// SignUpActivity.class);
		// startActivity(mIntent);
		// }
		else if (v == mBtnSignUp) {
			Intent mIntent = new Intent(LoginActivity.this,
					SignUpActivity.class);
			startActivity(mIntent);
		} else if (v == mTxtForgot) {
			Intent mIntent = new Intent(LoginActivity.this,
					ForgotPasswordActivity.class);
			startActivity(mIntent);
		}

	}

	public void clearValues() {
		mUserName.setText("");
		mPassword.setText("");
	}

	@SuppressLint("NewApi")
	public void setActionBar() {
		// Enable action bar icon_luncher as toggle Home Button
		ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle("");
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
		actionBar.setTitle("");
		actionBar.show();

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

	}

	private void LoginApi() {


		String device_id="";

		if(Build.VERSION.SDK_INT >= 27)
		{
			device_id="";
		}

		else
		{
			device_id=VKCUtils.getDeviceID(mActivity);
		}
		final VKCInternetManager manager = new VKCInternetManager(LOGIN__URL);
		String name[] = { "email", "password", "imei_no" };
		String value[] = { mUserName.getText().toString(),
				mPassword.getText().toString(), device_id };

		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				parseResponseJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub


			}
		});

	}

	/* RoleTypes:4->Sales Head,5->Retailers,6->Dealer 7->SubDealer */
	private void parseResponseJSON(String successResponse) {

		if (successResponse != null) {
			try {
				JSONObject jsonObject = new JSONObject(successResponse);

				JSONArray jsonArray = jsonObject
						.getJSONArray(JSON_LOGIN_RESPONSE);
				JSONObject resObject = jsonArray.getJSONObject(0);
				String loginResponse = resObject.getString(JSON_LOGIN_LOGIN);
				userType = resObject.getString(JSON_LOGIN_ROLEID);
				roleID = Integer.parseInt(userType);
				@SuppressWarnings("unused")
				String roleName = resObject.getString(JSON_LOGIN_ROLENAME);
				if (loginResponse.equals(JSON_LOGIN_SUCCESS)) {
					VKCUtils.showtoast(mActivity, 1);
					AppPrefenceManager.saveUserType(mActivity,
							resObject.getString(JSON_LOGIN_ROLEID));
					AppPrefenceManager.saveCustomerId(mActivity,
							resObject.getString(JSON_LOGIN_CUSTOMER_ID));

					if (resObject.getString(JSON_LOGIN_DIST_NAME) != null) {
						AppPrefenceManager.setLoginPlace(mActivity,
								resObject.getString(JSON_LOGIN_DIST_NAME));
					}

					if (resObject.getString(JSON_LOGIN_CUSTOMER_NAME) != null) {
						AppPrefenceManager.setLoginCustomerName(mActivity,
								resObject.getString(JSON_LOGIN_CUSTOMER_NAME));
					}
					AppPrefenceManager.saveUserId(mActivity,
							resObject.getString(JSON_LOGIN_USERID));
					// Save Dealer Count
					String mDealerCount = resObject
							.getString(JSON_LOGIN_DEALER_COUNT);
					AppPrefenceManager.saveDealerCount(mActivity, mDealerCount);
					String isCredit = resObject.getString(JSON_LOGIN_IS_CREDIT);
					AppPrefenceManager.saveIsCredit(mActivity, isCredit);
					dealerCount = Integer.parseInt(mDealerCount);
					String mUserName = resObject.getString(JSON_LOGIN_USERNAME);
					if (!AppPrefenceManager.getUserName(mActivity).equals("")) {
						if (!AppPrefenceManager.getUserName(mActivity).equals(
								mUserName)) {
							clearDb();
						} else {
						}

					}
					
					AppPrefenceManager.saveUserName(mActivity,
							resObject.getString(JSON_LOGIN_USERNAME));
					AppPrefenceManager.saveStateCode(mActivity,
							resObject.getString(JSON_LOGIN_STATECODE));
					AppPrefenceManager.saveLoginStatusFlag(mActivity, "true");
					if (jsonObject.getString("isgrpmember").equals("1")) {
						AppPrefenceManager.saveIsGroupMember(mActivity, "1");
						startActivity(new Intent(LoginActivity.this,
								GroupMemberActivity.class));
						finish();
					} else {
						AppPrefenceManager.saveIsGroupMember(mActivity, "0");
						gotoDashBoard();
					}
				} else if (loginResponse.equals(JSON_LOGIN_FAILED)) {
					VKCUtils.showtoast(mActivity, 23);
				}
			} catch (Exception e) {

			}
		}
	}

	public void clearDb() {
		DataBaseManager databaseManager = new DataBaseManager(mActivity);
		databaseManager.removeDb(TABLE_SHOPPINGCART);
	}

	public void gotoDashBoard() {
		Intent mIntent = new Intent(LoginActivity.this,
				DashboardFActivity.class);
		mIntent.putExtra("MAINCATEGORYNAMELIST", navMenuTitles);
		mIntent.putExtra("MAINCATEGORYIDLIST", categoryIdList);
		mIntent.putExtra("USERTYPE", userType);
		mIntent.putExtra("DEALERCOUNT", dealerCount);
		mIntent.putExtra("ROLEID", roleID);
		startActivity(mIntent);
		finish();
	}

}
