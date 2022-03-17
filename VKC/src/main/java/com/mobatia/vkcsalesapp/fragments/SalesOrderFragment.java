/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.activities.RetailersListViewOnSearch;
import com.mobatia.vkcsalesapp.adapter.SalesOrderAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.customview.CustomTextView;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.DealersListModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Bibin *
 */
public class SalesOrderFragment extends Fragment implements VKCDbConstants,
		VKCUrlConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;

	private RelativeLayout mRelDealer;
	private RelativeLayout mRelRetailer;
	private CustomTextView mTextViewDealer;
	private CustomTextView mTextViewRetailer;
	private ListView mDealersListView;
	private SalesOrderAdapter mSalesAdapter;
	private DataBaseManager databaseManager;
	private CartModel cartModel;

	private LinearLayout lnrTableHeaders;
	private ImageView imageViewSubmit, imageSearchCat, imageViewClear;
	private String salesOrderArray;
	private ArrayList<DealersListModel> dealersModel;
	public static Boolean isCart = false;
	private LinearLayout lnrOne, llCategory, llSearch;
	private LinearLayout llTop;
	private TextView txtRefr;
	private TextView txtDate;
	private TextView txtQty;
	private TextView txtValue;
	private TextView txtName;
	TextView labelText;
	private TextView txtPlace, textRetailer;
	EditText edtSearch;
	String testSearch;
	List<String> categories = new ArrayList<String>();
	Spinner spinner;
	private String item, type;
	ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
	CustomProgressBar pDialog;
	TextView txtTotalItem;
	TextView txtTotalQty;
	boolean isClicked = false;
	Bundle savedInstanceState;
	private int tableCount;
	boolean callPending;
	RelativeLayout rlCategory;
	private TextView txtHint, txtCartValue, textCreditValue;
	String creditValue;
	long creditPrice;
	long cartPrice;
	private final int FIVE_SECONDS = 10000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_salesorder, container,
				false);
		/*
		 * getActivity().getSupportActionBar().setLogo(R.drawable.back);
		 * getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		 * getActivity().getSupportActionBar().setHomeButtonEnabled(true);
		 */
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("My Cart");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setDisplayParams();
		AppController.isCart = true;
		AppController.isSelectedDealer = false;
		init(mRootView, savedInstanceState);
		callPending = true;
		String userType = AppPrefenceManager.getUserType(getActivity());

		/*
		 * setCartQuantity(); updateCartValue(); getCreditValue();
		 */
		if (!AppPrefenceManager.getUserType(getActivity()).equals("4")) {

			/*
			 * Date now = new Date(); Date alsoNow =
			 * Calendar.getInstance().getTime(); String nowAsString = new
			 * SimpleDateFormat("yyyy-MM-dd").format(now);
			 */
			/*
			 * for (int i = 0; i < AppController.cartArrayList.size(); i++) { if
			 * (AppController.cartArrayList.get(i).getStatus()
			 * .equals("pending")) { callPending = false; break; } else {
			 * callPending = true; } }
			 */

			// Don't Call Pending quantity if the cart contains pending orders
			// if (callPending) {

			// schedulePendingOrder();
			getCreditValue();

			getPendingQuantity();
			// } else if (AppController.isSelectedDealer)// In the case of
			// SubDealer check if he
			// selected dealer
			// {
			// getPendingQuantity();
			// }

			getCartData();
			/*
			 * } }
			 */
			// }
		} else {

			if (!AppPrefenceManager.getSelectedUserId(getActivity()).equals("")) {
				mTextViewDealer.setText(AppPrefenceManager
						.getSelectedUserName(getActivity()));
				getCreditValue();
				getPendingQuantity();
			} else {
				CustomToast toast = new CustomToast(getActivity());
				toast.show(56);
			}
			// getCartData();
		}
		return mRootView;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:
			// finish();
		}
		return (super.onOptionsItemSelected(item));
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(View v, Bundle savedInstanceState) {
		databaseManager = new DataBaseManager(getActivity());
		mRelDealer = (RelativeLayout) v.findViewById(R.id.rlDealer);
		mRelRetailer = (RelativeLayout) v.findViewById(R.id.rlRetailer);
		mTextViewDealer = (CustomTextView) v.findViewById(R.id.textViewDealer);
		mTextViewRetailer = (CustomTextView) v
				.findViewById(R.id.textViewRetailer);
		mDealersListView = (ListView) v.findViewById(R.id.dealersListView);
		lnrTableHeaders = (LinearLayout) v.findViewById(R.id.ll2);
		imageViewSubmit = (ImageView) v.findViewById(R.id.imageViewSearch);
		imageViewClear = (ImageView) v.findViewById(R.id.imageViewClear);
		imageSearchCat = (ImageView) v.findViewById(R.id.imageViewSearchCat);
		llTop = (LinearLayout) v.findViewById(R.id.llTop);
		lnrOne = (LinearLayout) v.findViewById(R.id.lnrOne);
		txtRefr = (TextView) v.findViewById(R.id.txtReferenceNumber);
		txtDate = (TextView) v.findViewById(R.id.txtDate);
		edtSearch = (EditText) v.findViewById(R.id.editSearch);
		textRetailer = (TextView) v.findViewById(R.id.textRetailer);
		rlCategory = (RelativeLayout) v.findViewById(R.id.rlCategory);
		llCategory = (LinearLayout) v.findViewById(R.id.secCatLL);
		llSearch = (LinearLayout) v.findViewById(R.id.secSearchLL);
		labelText = (TextView) v.findViewById(R.id.textView1);
		txtTotalItem = (TextView) v.findViewById(R.id.textTotalItem);
		txtTotalQty = (TextView) v.findViewById(R.id.textTotalQty);
		txtHint = (TextView) v.findViewById(R.id.hintText);
		edtSearch.setText("");
		txtCartValue = (TextView) v.findViewById(R.id.textCartValueCart);
		textCreditValue = (TextView) v.findViewById(R.id.textCreditValueCart);
		AppController.isClickedCartAdapter = false;
		spinner = (Spinner) v.findViewById(R.id.spinner);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		txtDate.setText("Date :  " + formattedDate);
		txtName = (TextView) v.findViewById(R.id.txtName);
		txtName.setText("Name : "
				+ AppPrefenceManager.getLoginCustomerName(getActivity()));
		txtPlace = (TextView) v.findViewById(R.id.txtPlace);
		txtPlace.setText("Place : "
				+ AppPrefenceManager.getLoginPlace(getActivity()));
		txtQty = (TextView) v.findViewById(R.id.txtTotalQty);
		txtValue = (TextView) v.findViewById(R.id.txtTotalValue);
		dealersModel = new ArrayList<DealersListModel>();
		imageViewSubmit.setEnabled(true);
		mDealersListView.setVisibility(View.VISIBLE);
		categories.clear();
		categories.add("Please Select");
		categories.add("Dealer");
		categories.add("Sub Dealer");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);
		imageViewClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeleteAlert appDeleteDialog = new DeleteAlert(getActivity());

				appDeleteDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				appDeleteDialog.setCancelable(true);
				appDeleteDialog.show();
			}
		});
		/*spinner.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				isClicked = true;
				return false;
			}
		});*/
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				item = arg0.getItemAtPosition(position).toString();
			//	if (isClicked) {
					if (item.equals("Dealer")) {
						type = "1";
						AppPrefenceManager.saveCustomerCategory(getActivity(),
								"1");
						labelText.setText("Dealer : ");
					
						if(AppPrefenceManager
								.getSelectedUserName(getActivity()).length()>0)
						{
							mTextViewDealer.setText(AppPrefenceManager
									.getSelectedUserName(getActivity()));
						}
						else
						{
							mTextViewDealer.setText("Dealer Name");
						}
						
					} else if (item.equals("Sub Dealer")) {
						type = "2";
						labelText.setText("Sub Dealer : ");
						mTextViewDealer.setText("Sub Dealer Name");
						AppPrefenceManager.saveCustomerCategory(getActivity(),
								"2");
					}
					
				//}
			//	isClicked = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mTextViewDealer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (isClicked) {
				//	clearDb();
					// AppController.cartArrayList.clear();
					//getPendingQuantity();
					//getCartData();
					mTextViewDealer.setText("");
					txtQty.setText("Total quantity :  " + "" + 0);
				} else if (AppController.isSelectedDealer) {
					getPendingQuantity();
					getCreditValue();
					getCartData();
				}
				/*else if(AppPrefenceManager
						.getSelectedUserName(getActivity()).length()>0)
				{
					mTextViewDealer.setText(AppPrefenceManager
							.getSelectedUserName(getActivity()));
				}*/

				/*
				 * else {
				 * 
				 * clearDb(); // AppController.cartArrayList.clear();
				 * getPendingQuantity(); getCreditValue();
				 * 
				 * getCartData();
				 * 
				 * //txtQty.setText("Total quantity :  " + "" + 0);
				 * 
				 * }
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*if (AppController.isSelectedDealer) {
					getPendingQuantity();
					getCreditValue();
					getCartData();
				}*/
			}
		});

	

		if (AppPrefenceManager.getCustomerCategory(getActivity()).equals("1")) {
			spinner.setSelection(1);
			labelText.setText("Dealer : ");
			mTextViewDealer.setText(AppPrefenceManager
					.getSelectedUserName(getActivity()));
		} else if (AppPrefenceManager.getCustomerCategory(getActivity())
				.equals("2")) {
			spinner.setSelection(2);
			labelText.setText("Sub Dealer : ");
			mTextViewDealer.setText(AppPrefenceManager
					.getSelectedUserName(getActivity()));
		} else {
			spinner.setSelection(0);
		}
		if (AppPrefenceManager.getUserType(getActivity()).equals("6")) { // UserType:Dealer
			mRelDealer.setClickable(false);
			mRelRetailer.setClickable(false);
			VKCObjectConstants.selectedDealerId = "";
			VKCObjectConstants.selectedSubDealerId = "";
			VKCObjectConstants.selectedRetailerId = "";
			mTextViewDealer.setText(AppPrefenceManager
					.getUserName(getActivity()));
			llTop.setVisibility(View.GONE);
			lnrOne.setVisibility(View.VISIBLE);
			llCategory.setVisibility(View.GONE);
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) { // UserType:Retailer
			llCategory.setVisibility(View.GONE);
			llSearch.setVisibility(View.GONE);
			mRelDealer.setClickable(true);
			mRelRetailer.setClickable(false);
			VKCObjectConstants.selectedSubDealerId = "";
			VKCObjectConstants.selectedRetailerId = "";
			mTextViewRetailer.setText(AppPrefenceManager
					.getUserName(getActivity()));
			mRelDealer.setOnClickListener(new OnClickView());
			llCategory.setVisibility(View.GONE);

		} else if (AppPrefenceManager.getUserType(getActivity()).equals("7")) { // UserType:Sub
																				// Dealer
			rlCategory.setVisibility(View.GONE);
			llCategory.setVisibility(View.GONE); //
			mRelRetailer.setVisibility(View.GONE);
			llSearch.setVisibility(View.GONE);
			textRetailer.setVisibility(View.GONE);
			txtHint.setVisibility(View.GONE);
			mTextViewDealer.setText("Select Dealer");
			VKCObjectConstants.selectedSubDealerId = "";
			VKCObjectConstants.selectedRetailerId = "";
			mRelDealer.setClickable(true);
			mRelDealer.setOnClickListener(new OnClickView());
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			// Dealer
			//llSearch.setVisibility(View.VISIBLE);
			//llCategory.setVisibility(View.VISIBLE);
			mRelRetailer.setVisibility(View.GONE);
			textRetailer.setVisibility(View.GONE);
			txtHint.setVisibility(View.GONE);
			// mTextViewDealer.setText("Dealer");

			VKCObjectConstants.selectedRetailerId = "";
			/*
			 * mRelDealer.setClickable(true); mRelDealer.setOnClickListener(new
			 * OnClickView());
			 */
		} else { // Usertype:SalesHead
			mRelDealer.setClickable(true);
			llCategory.setVisibility(View.VISIBLE);
			llSearch.setVisibility(View.VISIBLE);
			VKCObjectConstants.selectedSubDealerId = "";
			// mRelRetailer.setClickable(true);
			// mRelDealer.setOnClickListener(new OnClickView());
			// mRelRetailer.setOnClickListener(new OnClickView());
			llCategory.setOnClickListener(new OnClickView());
		}

		imageViewSubmit.setOnClickListener(new OnClickView());
		imageSearchCat.setOnClickListener(new OnClickView());

	}

	/**
	 * Method to retrieve items added to cart
	 * 
	 */
	private void getCartData() {
		AppController.cartArrayList.clear();
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE, "pid", "sapid", "catid", "status" };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");
		/*
		 * System.out.println("13022015:cursor.getCount()::" +
		 * cursor.getCount());
		 */

		if (cursor.getCount() > 0) {
			/*
			 * txtTotalItem.setText("Total Item : " +
			 * String.valueOf(cursor.getCount()));
			 */
			while (!cursor.isAfterLast()) {
				AppController.cartArrayList.add(setCartModel(cursor));

				cursor.moveToNext();
			}
			if (AppController.cartArrayList.size() > 0) {
				lnrTableHeaders.setVisibility(View.VISIBLE);
				mDealersListView.setVisibility(View.VISIBLE);
				/*
				 * int totalQty = 0; for (int i = 0; i <
				 * AppController.cartArrayList.size(); i++) { totalQty =
				 * totalQty + Integer.parseInt(AppController.cartArrayList.get(
				 * i).getProdQuantity()); }
				 */
				// txtQty.setText("Total quantity :  " + "" + totalQty);
				mSalesAdapter = new SalesOrderAdapter(getActivity(),
						AppController.cartArrayList, lnrTableHeaders,
						txtTotalQty, txtTotalItem, txtCartValue);
				mSalesAdapter.notifyDataSetChanged();
				mDealersListView.setAdapter(mSalesAdapter);
				mDealersListView.setSelection(AppController.listScrollTo);
			} else {
				// txtQty.setText("Total quantity :  " + "" + 0);
				lnrTableHeaders.setVisibility(View.GONE);
				mDealersListView.setVisibility(View.GONE);
			}

			SalesOrderFragment.isCart = true;
			// setCartQuantity();
		} else {
			VKCUtils.showtoast(getActivity(), 9);
			/*
			 * txtTotalItem.setText("Total Item : " +
			 * String.valueOf(cursor.getCount()));
			 */
			SalesOrderFragment.isCart = false;
			// txtQty.setText("Total quantity :  " + "" + 0);
			// setCartQuantity();
			lnrTableHeaders.setVisibility(View.GONE);
			mDealersListView.setVisibility(View.GONE);

			txtTotalQty.setText("Total Qty. :" + "0");
			txtCartValue.setText("Cart Value: ₹" + "0");
			txtTotalItem.setText("Total Item : " + "0");

		}

	}

	private JSONObject createJson() {
		getCartData();
		System.out.println("18022015:Within createJson ");

		JSONObject jsonObject = new JSONObject();

		try {
			/*
			 * jsonObject.putOpt("user_id",
			 * AppPrefenceManager.getUserId(getActivity())); if
			 * (AppPrefenceManager.getUserType(getActivity()).equals("")) {
			 * jsonObject.putOpt("state_code", ""); } else {
			 * jsonObject.putOpt("state_code",
			 * AppPrefenceManager.getStateCode(getActivity())); }
			 * 
			 * jsonObject.putOpt("dealer_id",
			 * VKCObjectConstants.selectedDealerId);
			 * jsonObject.putOpt("retailer_id",
			 * VKCObjectConstants.selectedRetailerId);
			 * jsonObject.putOpt("subdealer_id",
			 * VKCObjectConstants.selectedSubDealerId);
			 * jsonObject.putOpt("user_type",
			 * AppPrefenceManager.getUserType(getActivity()));
			 */
			jsonObject.putOpt("user_id",
					AppPrefenceManager.getUserId(getActivity()));
			if (AppPrefenceManager.getUserType(getActivity()).equals("")) {
				jsonObject.putOpt("state_code", "");
			} else {
				jsonObject.putOpt("state_code",
						AppPrefenceManager.getStateCode(getActivity()));
			}
			if (!AppPrefenceManager.getUserType(getActivity()).equals("4")) {
				jsonObject.putOpt("dealer_id",
						VKCObjectConstants.selectedDealerId);
				jsonObject.putOpt("retailer_id",
						VKCObjectConstants.selectedRetailerId);
				jsonObject.putOpt("subdealer_id",
						VKCObjectConstants.selectedSubDealerId);
			} else {
				if (AppPrefenceManager.getCustomerCategory(getActivity())
						.equals("1"))
					jsonObject
							.putOpt("dealer_id", AppPrefenceManager
									.getSelectedUserId(getActivity()));
				else
					jsonObject.putOpt("dealer_id",
							VKCObjectConstants.selectedDealerId);

				jsonObject.putOpt("retailer_id",
						VKCObjectConstants.selectedRetailerId);

				if (AppPrefenceManager.getCustomerCategory(getActivity())
						.equals("2"))
					jsonObject
							.putOpt("subdealer_id", AppPrefenceManager
									.getSelectedUserId(getActivity()));
				else

					jsonObject.putOpt("subdealer_id",
							VKCObjectConstants.selectedSubDealerId);
			}

			jsonObject.putOpt("user_type",
					AppPrefenceManager.getUserType(getActivity()));

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < AppController.cartArrayList.size(); i++) {
			JSONObject object = new JSONObject();
			try {

				/*
				 * object.putOpt("product_id",
				 * AppController.cartArrayList.get(i) .getProdId());
				 */
				object.putOpt("product_id", AppController.cartArrayList.get(i)
						.getProdName());
				object.putOpt("category_id", AppController.cartArrayList.get(i)
						.getCatId());
				object.putOpt("case_id", AppController.cartArrayList.get(i)
						.getProdSizeId());
				object.putOpt("color_id", AppController.cartArrayList.get(i)
						.getProdColorId());
				object.putOpt("quantity", AppController.cartArrayList.get(i)
						.getProdQuantity());
				Log.e("Size Value", "18022015 "
						+ AppController.cartArrayList.get(i).getProdSizeId());
				if (AppController.cartArrayList.get(i).getProdSizeId()
						.equals(" ")) {
					object.put("size", AppController.cartArrayList.get(i)
							.getProdSize());
				} else {
					object.put("size", "");
				}
				// object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
				jsonArray.put(i, object);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("18022015 Exception", e.toString());
			}

			// salesOrderArray=jsonArray.toString();
			Log.i("Cart Submit Length", " " + jsonArray.length());
			Log.e(" JSONArray length:", "18022015 " + jsonArray.toString());
		}

		try {
			jsonObject.put("order_details", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.v("LOG", "20022015 " + jsonObject);

		return jsonObject;

	}

	/**
	 * Method to set cart model
	 * 
	 */
	private CartModel setCartModel(Cursor cursor) {
		cartModel = new CartModel();
		cartModel.setProdId(cursor.getString(0));
		cartModel.setProdName(cursor.getString(1));
		cartModel.setProdSizeId(cursor.getString(2));
		cartModel.setProdSize(cursor.getString(3));
		cartModel.setProdColorId(cursor.getString(4));
		cartModel.setProdColor(cursor.getString(5));
		cartModel.setProdQuantity(cursor.getString(6));
		cartModel.setProdGridValue(cursor.getString(7));
		cartModel.setPid(cursor.getString(8));
		System.out.println("PID :" + cursor.getString(8));
		cartModel.setSapId(cursor.getString(9));
		cartModel.setCatId(cursor.getString(10));
		cartModel.setStatus(cursor.getString(11));
		return cartModel;
	}

	/**
	 * Post Api to submit sales order
	 * 
	 */
	public void submitSalesOrder() {
		imageViewSubmit.setEnabled(false);
		String name[] = { "salesorder" };
		String values[] = { createJson().toString() };
		pDialog = new CustomProgressBar(getActivity(), R.drawable.loading);
		pDialog.show();
		System.out.println("18022015:createJson:" + createJson());
		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_SALESORDER_SUBMISSION);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "17022015 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "17022015 Errror" + failureResponse);
						AppPrefenceManager.setIsCallPendingAPI(getActivity(),
								false);
					}
				});
	}

	public void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String responseObj = jsonObject.getString("response");
			if (responseObj.equals("1")) {
				pDialog.dismiss();
				VKCUtils.showtoast(getActivity(), 15);
				clearDb();
				// setCartQuantity();
				AppPrefenceManager.setFillTable(getActivity(), "false");
				// clearOrderDb();
				AppController.cartArrayList.clear();
				AppController.cartArrayListSelected.clear();
				mSalesAdapter = new SalesOrderAdapter(getActivity(),
						AppController.cartArrayList, lnrTableHeaders, txtQty,
						txtTotalQty, txtCartValue);
				AppPrefenceManager.setDate(getActivity(), "");
				// AppPrefenceManager.setIsCallPendingAPI(getActivity(), true);
				mSalesAdapter.notifyDataSetChanged();
				mDealersListView.setAdapter(mSalesAdapter);
				DashboardFActivity.dashboardFActivity.displayView(-1);
				// updateCartValue();
				getCreditValue();

			} else {
				pDialog.dismiss();
				VKCUtils.showtoast(getActivity(), 13);
			}
		} catch (Exception e) {

		}
	}

	public void clearDb() {
		DataBaseManager databaseManager = new DataBaseManager(getActivity());
		databaseManager.removeDb(TABLE_SHOPPINGCART);
	}

	public void clearOrderDb() {
		DataBaseManager databaseManager = new DataBaseManager(getActivity());
		databaseManager.removeDb(TABLE_ORDERLIST);
	}

	// private void getDealerApi() {
	// final VKCInternetManager manager = new VKCInternetManager(
	// "http://dev.mobatia.com/vkc/api/getstate");
	// Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
	//
	// manager.getResponse(getActivity(), new ResponseListener() {
	//
	// @Override
	// public void responseSuccess(String successResponse) {
	//
	// parseJSON(successResponse);
	//
	// }
	//
	// @Override
	// public void responseFailure(String failureResponse) {
	// // TODO Auto-generated method stub
	// Log.v("LOG", "04122014FAIL " + failureResponse);
	// // mIsError = true;
	//
	// }
	// });
	//
	// }
	//
	// private void parseJSON(String response)
	// {
	// try {
	// JSONObject respObj = new JSONObject(response);
	// JSONArray respArray = respObj.getJSONArray("states");
	// for (int i = 0; i < respArray.length(); i++) {
	// // JSONObject objState = respArray.getJSONObject(i);
	// dealersModel.add(parseDealerObject(respArray.getJSONObject(i)));
	// // dealersStateModels.add(parseSateObject(respArray
	// // .getJSONObject(i)));
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// private DealersListModel parseDealerObject(JSONObject objDealer) {
	// DealersListModel dealerModel = new DealersListModel();
	// dealerModel.setId("1");
	// dealerModel.setDealerName("Dealer1");
	//
	// return dealerModel;
	// // TODO Auto-generated method stub
	//
	// }
	/*
	 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
	 */
	public Boolean doUserCheck() {

		if (AppPrefenceManager.getUserType(getActivity()).equals("4")) { // Saleshead

			if (!(mTextViewDealer.getText().toString().equals(""))// &&
																	// !(mTextViewRetailer.getText().toString().equals(""))
			) {

				return true;
			} else {
				return false;
			}
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) { // Retailer
			if (!(mTextViewDealer.getText().toString().equals(""))) {
				return true;
			} else {
				return false;
			}
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("6")) { // Dealer
			if ((mTextViewRetailer.getText().toString().equals(""))) {
				return true;
			} else {
				return false;
			}
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("7")) { // Sub
																				// Dealer
			if (!(mTextViewDealer.getText().toString().equals(""))) {
				return true;
			} else {
				return false;
			}
		}
		// if(SalesOrderFragment.isCart==true){
		// return true;
		// }else{
		// return false;
		// }

		return null;

	}

	public class OnClickView implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v == imageViewSubmit) {

				if (AppController.cartArrayList.size() > 0) {
					if (doUserCheck()) {
						if (AppPrefenceManager.getUserType(getActivity())
								.equals("4")) {
							if (item.equals("Please Select")) {
								VKCUtils.showtoast(getActivity(), 36);
							} else {
								submitSalesOrder();

							}
						}

						else {
							/*
							 * if (cartPrice > creditPrice) {
							 * VKCUtils.showtoast(getActivity(), 46); } else {
							 */
							submitSalesOrder();
							// }
						}
					} else {

						VKCUtils.showtoast(getActivity(), 16);
					}
				} else {

					VKCUtils.showtoast(getActivity(), 16);
				}

			}

			if (v == mRelDealer) {
				if (AppPrefenceManager.getUserType(getActivity()).equals("7")) {
					Intent intent = new Intent(getActivity(),
							RetailersListViewOnSearch.class);
					intent.putExtra("mType", "SubDealer");
					VKCObjectConstants.textDealer = mTextViewDealer;
					startActivity(intent);
				} else if (AppPrefenceManager.getUserType(getActivity())
						.equals("4")) {
					Intent intent = new Intent(getActivity(),
							RetailersListViewOnSearch.class);
					intent.putExtra("mType", "SalesHead");
					VKCObjectConstants.textDealer = mTextViewDealer;
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							RetailersListViewOnSearch.class);
					intent.putExtra("mType", "Dealer");
					VKCObjectConstants.textDealer = mTextViewDealer;
					startActivity(intent);
				}

			}
			if (v == mRelRetailer) {

				Intent intent = new Intent(getActivity(),
						RetailersListViewOnSearch.class);
				intent.putExtra("mType", "Retailer");
				VKCObjectConstants.textRetailer = mTextViewRetailer;
				startActivity(intent);

			}

			if (v == imageSearchCat) {
				AppController.isSelectedDealer = false;
				testSearch = edtSearch.getText().toString();
				if (testSearch.length() > 0 && item.equals("Please Select")) {
					CustomToast toast = new CustomToast(getActivity());
					toast.show(39);
				} else if (testSearch.length() > 0
						&& !item.equals("Please Select")) {
					Intent intent = new Intent(getActivity(),
							RetailersListViewOnSearch.class);
					intent.putExtra("mType", "SalesHead");
					intent.putExtra("key", testSearch);
					intent.putExtra("type", type);
					edtSearch.setText("");
					VKCObjectConstants.textDealer = mTextViewDealer;
					startActivity(intent);

				} else {
					CustomToast toast = new CustomToast(getActivity());
					toast.show(38);
				}

			}

		}

		private void getMyDealersSalesHeadApi() {
			VKCInternetManager manager = null;
			dealersShopModels.clear();
			// Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
			String name[] = { "saleshead_id" };
			String value[] = { AppPrefenceManager.getUserId(getActivity()) };
			manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
			manager.getResponsePOST(getActivity(), name, value,
					new ResponseListener() {

						@Override
						public void responseSuccess(String successResponse) {

							// parseJSON(successResponse);
							Log.v("LOG", "06012015 " + successResponse);
							parseMyDealerSalesHeadJSON(successResponse);

						}

						@Override
						public void responseFailure(String failureResponse) {
							// TODO Auto-generated method stub
							Log.v("LOG", "04122014FAIL " + failureResponse);
							// mIsError = true;

						}
					});

		}

		private void parseMyDealerSalesHeadJSON(String successResponse) {
			// TODO Auto-generated method stub

			try {
				// ArrayList<DealersShopModel> dealersShopModels = new
				// ArrayList<DealersShopModel>();
				JSONObject respObj = new JSONObject(successResponse);
				JSONObject response = respObj.getJSONObject("response");
				String status = response.getString("status");
				if (status.equals("Success")) {
					JSONArray respArray = response.getJSONArray("dealers");
					for (int i = 0; i < respArray.length(); i++) {

						dealersShopModels.add(parseShop(respArray
								.getJSONObject(i)));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		private DealersShopModel parseShop(JSONObject jsonObject) {
			DealersShopModel dealersShopModel = new DealersShopModel();
			dealersShopModel.setAddress(jsonObject.optString("address"));

			dealersShopModel.setCity(jsonObject.optString("city"));
			dealersShopModel.setContact_person(jsonObject
					.optString("contact_person"));
			dealersShopModel.setDealerId(jsonObject.optString("dealerId"));
			dealersShopModel.setCountry(jsonObject.optString("country"));
			dealersShopModel
					.setCustomer_id(jsonObject.optString("customer_id"));
			dealersShopModel.setId(jsonObject.optString("id"));
			dealersShopModel.setName(jsonObject.optString("name"));
			dealersShopModel.setPhone(jsonObject.optString("phone"));
			dealersShopModel.setPincode(jsonObject.optString("pincode"));
			dealersShopModel.setState(jsonObject.optString("state"));
			dealersShopModel.setState_name(jsonObject.optString("state_name"));
			return dealersShopModel;

		}
	}

	public void setCartQuantity() {
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");
		int mCount = 0;
		int cartount = 0;
		if (cursor.moveToFirst()) {
			do {
				String count = cursor.getString(cursor
						.getColumnIndex("productqty"));
				cartount = Integer.parseInt(count);
				mCount = mCount + cartount;
				// do what ever you want here
			} while (cursor.moveToNext());
		}
		cursor.close();
		txtTotalQty.setText("Total Quantity : " + String.valueOf(mCount));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// getCartData();
		super.onResume();
		// init(mRootView, savedInstanceState);
		// getPendingQuantity();
		//getCartData();
	
		if(AppPrefenceManager.getUserType(getActivity()).equals("4")&&AppController.isSelectedDealer)
		{
			//getPendingQuantity();
			getCreditValue();
			mTextViewDealer.setText(AppPrefenceManager
					.getSelectedUserName(getActivity()));
			mDealersListView.setSelection(AppController.listScrollTo);
		}
		else if(AppPrefenceManager.getUserType(getActivity()).equals("4"))
		{
			mTextViewDealer.setText(AppPrefenceManager
					.getSelectedUserName(getActivity()));
		}
		else 
		
		{
		mDealersListView.setSelection(AppController.listScrollTo);
		}

		//

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		// getCartData();
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		// getPendingQuantity();
		super.onStop();

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		super.onDestroyView();

		// init(mRootView, savedInstanceState);
	}

	public void getPendingQuantity() {
		AppController.cartArrayList.clear();
		String userType = "";
		String cust_id = "";
		if (AppPrefenceManager.getUserType(getActivity()).equals("6")) {
			userType = "1";
			cust_id = AppPrefenceManager.getCustomerId(getActivity());
		}

		else if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			userType = "1";
			cust_id = AppPrefenceManager.getSelectedUserId(getActivity());
		} else {
			userType = "2";
			cust_id = AppPrefenceManager.getSelectedUserId(getActivity());
		}
		String name[] = { "customerId", "customerType" };

		String values[] = { cust_id, userType };
		// System.out.println("Values" + values);
		pDialog = new CustomProgressBar(getActivity(), R.drawable.loading);

		// pDialog.setMessage("Loading...");
		pDialog.show();
		// System.out.println("18022015:createJson:" + createJson());
		final VKCInternetManager manager = new VKCInternetManager(
				URL_GET_PENDING_ORDER_CART);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "17022015 success" + successResponse);
						parseResponseCart(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "17022015 Errror" + failureResponse);
						AppPrefenceManager.setIsCallPendingAPI(getActivity(),
								true);
					}
				});
	}

	private void parseResponseCart(String successResponse) {
		// TODO Auto-generated method stub
		try {
			JSONObject objResponse = new JSONObject(successResponse);
			JSONObject response = objResponse.getJSONObject("response");
			String status = response.optString("status");
			if (status.equals("Success")) {
				pDialog.dismiss();
				AppController.isCalledApiOnce = true;

				txtTotalQty.setText("Total Qty. :"
						+ response.optString("tot_qty"));
				txtCartValue.setText("Cart Value: ₹"
						+ response.optString("cart_value"));
				txtTotalItem.setText("Total Item : "
						+ response.optString("tot_items"));

				// initialiseUI();
				AppPrefenceManager.setIsCallPendingAPI(getActivity(), false);
				JSONArray pendingArray = response.optJSONArray("pendingQty");
				if (pendingArray.length() > 0) {
					// clearDb();
					clearDb();
					AppController.isSelectedDealer=false;
					// int cartCount = getCartCount();
					// for(int i=0;i<cartCount;i++)
					// {
					/*
					 * if (cartCount > 0) {
					 * 
					 * databaseManager .removeFromDb(TABLE_SHOPPINGCART,
					 * "status", "pending");
					 * 
					 * SQLiteAdapter mAdapter = new SQLiteAdapter(
					 * getActivity(), DBNAME); mAdapter.deletePending(); }
					 */
					// }
					for (int i = 0; i < pendingArray.length(); i++) {
						JSONObject objPending = pendingArray.getJSONObject(i);
						String product_id = objPending.optString("product_id");
						String size_name = objPending.optString("size_name");
						String size_id = objPending.optString("size_id");
						String color_name = objPending.optString("color_name");
						String color_id = objPending.optString("color_id");
						String quantity = objPending.optString("quantity");
						String sapId = objPending.optString("productSapId");
						String catId = objPending.optString("categoryid");
						// String price = objPending.optString("price");
						String id = objPending.optString("id");

						// getCartCount();

						String[][] values = {

						{ PRODUCT_ID, product_id },
								{ PRODUCT_NAME, product_id },
								{ PRODUCT_SIZEID, size_id },
								{ PRODUCT_SIZE, size_name },
								{ PRODUCT_COLORID, color_id },
								{ PRODUCT_COLOR, color_name },
								{ PRODUCT_QUANTITY, quantity },
								{ PRODUCT_GRIDVALUE, "" }, { "pid", id },// {
																			// "pid",
																			// String.valueOf(tableCount
																			// +
																			// 1)
																			// }
								{ "sapid", sapId }, { "catid", catId } };

						databaseManager
								.insertIntoDb(TABLE_SHOPPINGCART, values);
					}

					Date now = new Date();
					Date alsoNow = Calendar.getInstance().getTime();
					String nowAsString = new SimpleDateFormat("yyyy-MM-dd")
							.format(now);

					AppPrefenceManager.setDate(getActivity(), nowAsString);
				}
				getCartData();
				// setCartQuantity();
				// updateCartValue();
				// updateCartValue();
				// finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getCartCount() {
		AppController.cartArrayList.clear();
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");
		// System.out.println("13022015:cursor.getCount()::" +
		// cursor.getCount());

		if (cursor.getCount() > 0) {

			while (!cursor.isAfterLast()) {
				// AppController.cartArrayList.add(setCartModel(cursor));
				tableCount = tableCount + cursor.getCount();

				cursor.moveToNext();
			}

		}
		return cursor.getCount();
	}

	public void updateCartValue() {
		SQLiteAdapter mAdapter = new SQLiteAdapter(getActivity(), DBNAME);
		mAdapter.openToRead();
		String sumValue = mAdapter.getCartSum();

		if (sumValue == null) {
			txtCartValue.setText("Cart Value: ₹" + 0);

		} else {

			cartPrice = Integer.parseInt(sumValue);
			txtCartValue.setText("Cart Value: ₹" + sumValue);
		}
	}

	private void getCreditValue() {
		VKCInternetManagerWithoutDialog manager = null;

		String cust_id = "";
		if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			cust_id = AppPrefenceManager.getSelectedUserId(getActivity());
		} else {
			cust_id = AppPrefenceManager.getCustomerId(getActivity());
		}
		String name[] = { "dealerid" };
		String value[] = { cust_id };
		manager = new VKCInternetManagerWithoutDialog(
				GET_QUICK_ORDER_CREDIT_URL);
		manager.getResponsePOST(getActivity(), name, value,
				new ResponseListenerWithoutDialog() {

					@Override
					public void responseSuccess(String successResponse) {

						try {
							JSONObject responseObj = new JSONObject(
									successResponse);
							JSONObject response = responseObj
									.getJSONObject("response");
							String status = response.getString("status");
							if (status.equals("Success")) {
								creditValue = response.getString("creditvalue");
								creditPrice = Integer.parseInt(creditValue);

								textCreditValue.setText("Order Limit: ₹"
										+ creditValue);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void responseFailure(String failureResponse) { // TODO
						// Auto-generated method stub

					}
				});

	}

	public class DeleteAlert extends Dialog implements
			android.view.View.OnClickListener, VKCDbConstants {

		public Activity mActivity;

		public DeleteAlert(Activity a) {
			super(a);
			// TODO Auto-generated constructor stub
			this.mActivity = a;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_clear_cart);
			init();

		}

		private void init() {

			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] deleteList = new String[AppController.cartArrayList
							.size()];
					ArrayList<String> listDelete = new ArrayList<>();
					for (int i = 0; i < AppController.cartArrayList.size(); i++) {
						listDelete.add(AppController.cartArrayList.get(i)
								.getPid());
						/*
						 * deleteList[i]=AppController.cartArrayList.get(i)
						 * .getPid();
						 */
					}
					deleteApi(listDelete);
					clearDb();
					VKCUtils.showtoast(getActivity(), 55);
					// setCartQuantity();
					getCartData();
					AppPrefenceManager.setFillTable(getActivity(), "false");
					// clearOrderDb();
					// updateCartValue();
					AppController.cartArrayList.clear();
					AppController.cartArrayListSelected.clear();
					mSalesAdapter = new SalesOrderAdapter(getActivity(),
							AppController.cartArrayList, lnrTableHeaders,
							txtQty, txtTotalQty, txtCartValue);
					AppPrefenceManager.setDate(getActivity(), "");
					// AppPrefenceManager.setIsCallPendingAPI(getActivity(),
					// true);
					mSalesAdapter.notifyDataSetChanged();
					mDealersListView.setAdapter(mSalesAdapter);
					dismiss();
				}

			});
			Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
			buttonCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}

		@Override
		public void onClick(View v) {

			dismiss();
		}

	}

	public void deleteApi(ArrayList<String> listDelete) {
		String name[] = { "ids" };

		String values[] = { listDelete.toString() };
		final VKCInternetManager manager = new VKCInternetManager(
				DELETE_CART_ITEM_API);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 success" + successResponse);
						try {
							JSONObject obj = new JSONObject(successResponse);
							String status = obj.optString("status");
							if (status.equals("Success")) {

								txtTotalQty.setText("Total Qty. :"
										+ obj.optString("tot_qty"));
								txtCartValue.setText("Cart Value: ₹"
										+ obj.optString("cart_value"));
								txtTotalItem.setText("Total Item : "
										+ obj.optString("tot_items"));
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 Errror" + failureResponse);
					}
				});
	}
	/*
	 * public void schedulePendingOrder() { final Handler handler=new Handler();
	 * handler.postDelayed(new Runnable() { public void run() {
	 * getPendingQuantity(); handler.postDelayed(this, FIVE_SECONDS); } },
	 * FIVE_SECONDS); }
	 */
}
