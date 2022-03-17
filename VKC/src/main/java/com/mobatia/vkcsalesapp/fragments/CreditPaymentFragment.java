/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.CreditPaymentStatusAdapter;
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CreditPaymentModel;
import com.mobatia.vkcsalesapp.model.DealersCityModel;
import com.mobatia.vkcsalesapp.model.DealersDistrictModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author mobatia-user
 * 
 */
public class CreditPaymentFragment extends Fragment implements VKCDbConstants,
		VKCUrlConstants, VKCJsonTagConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;

	private RelativeLayout mRelDealer;
	private RelativeLayout mRelRetailer;
//	private CustomTextView mTextViewDealer;
//	private CustomTextView mTextViewRetailer;
	private ListView mDealersListView;
//	private SalesOrderAdapter mSalesAdapter;
//	private DataBaseManager databaseManager;
//	private CartModel cartModel;
//	private ArrayList<CartModel> cartArrayList;
//	private LinearLayout lnrTableHeaders;
//	private String salesOrderArray;
//	private ArrayList<DealersListModel> dealersModel;
	LinearLayout linearTop;
	RelativeLayout rlSearchContainer;
	ArrayList<CreditPaymentModel> creditModels;
	ArrayList<DealersDistrictModel> districtModels = new ArrayList<DealersDistrictModel>();
	ArrayList<DealersShopModel> dealersShopModels;
	DealersListViewAdapter dealersListViewAdapter;
	private Boolean isError = false;
	private ImageView imageViewSearch;
	private String mType;
	private Activity mActivity;
	boolean mIsError = false;
	private EditText etSearchText;
	private ListView dealersListViewAutoPopulate;
//	private TextView mTxtRetailer, mTxtDealer;
//	private ImageView clrRetail, clrDealer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_paymentstatus,
				container, false);
		setDisplayParams();
		init(mRootView, savedInstanceState);
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_imageview, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		
		AppController.isCart=false;
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		return mRootView;

	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(View v, Bundle savedInstanceState) {
		creditModels = new ArrayList<CreditPaymentModel>();
		//databaseManager = new DataBaseManager(getActivity());
		mRelDealer = (RelativeLayout) v.findViewById(R.id.rlDealer);
		mRelRetailer = (RelativeLayout) v.findViewById(R.id.rlRetailer);
		//mTextViewDealer = (CustomTextView) v.findViewById(R.id.textViewDealer);
		//mTextViewRetailer = (CustomTextView) v
				//.findViewById(R.id.textViewRetailer);
		mDealersListView = (ListView) v.findViewById(R.id.dealersListView);
		linearTop = (LinearLayout) v.findViewById(R.id.llTop);
		rlSearchContainer=(RelativeLayout)v.findViewById(R.id.rlSearchContainer);
		imageViewSearch = (ImageView) v.findViewById(R.id.imageViewSearch);
		etSearchText=(EditText)v.findViewById(R.id.etSearchText);
		dealersListViewAutoPopulate=(ListView)v.findViewById(R.id.dealersListViewAutoPopulate);
		mActivity=getActivity();
//		mTxtRetailer = (TextView) v.findViewById(R.id.txtRetailer);
//		mTxtDealer = (TextView) v.findViewById(R.id.txtDealer);
		imageViewSearch.setOnClickListener(new OnClickView());
		//dealersModel = new ArrayList<DealersListModel>();
//		clrRetail = (ImageView) v.findViewById(R.id.imageViewCloseRet);
//		clrDealer = (ImageView) v.findViewById(R.id.imageViewCloseDea);
		if (AppPrefenceManager.getUserType(getActivity()).equals("6")||AppPrefenceManager.getUserType(getActivity()).equals("7")) { // UserType:Dealer | SubDealer
			linearTop.setVisibility(View.GONE);
			rlSearchContainer.setVisibility(View.GONE);
			getPaymentStatusApi();
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) { // UserType:Retailer
			linearTop.setVisibility(View.GONE);
			rlSearchContainer.setVisibility(View.GONE);
			getPaymentStatusApi();
		} else { // Usertype:SalesHead
			mRelDealer.setClickable(true);
			mRelRetailer.setClickable(true);
			mRelDealer.setOnClickListener(new OnClickView());
			mRelRetailer.setOnClickListener(new OnClickView());
			imageViewSearch.setOnClickListener(new OnClickView());
			dealersShopModels=new ArrayList<DealersShopModel>();
			dealersListViewAdapter=new DealersListViewAdapter(
					dealersShopModels, mActivity);
		
			dealersListViewAutoPopulate.setAdapter(dealersListViewAdapter);
			dealersListViewAutoPopulate.setOnItemClickListener(new ClickOnItemView());
			mType="Dealer";
			
			getDistrictApi(AppPrefenceManager.getStateCode(mActivity));
			etSearchText.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (0 != etSearchText.getText().length()) {
						String shopName = etSearchText.getText().toString();
						setDealerShopList(shopName);
					}
					
				}
			});
//			mTxtRetailer.setOnClickListener(new OnClickView());
//			mTxtDealer.setOnClickListener(new OnClickView());
//			clrRetail.setOnClickListener(new OnClickView());
//			clrDealer.setOnClickListener(new OnClickView());
		}

	}

	/*******************************************************
	 * Method name : getPaymentStatusApi Description : Api call Parameters : nil
	 * Return type : void Date : Feb 26, 2015 Author : Archana S
	 *****************************************************/
	private void getPaymentStatusApi() {
		creditModels.clear();
		String userId = "";
		String type = "";
		String typeUserId = "";
		System.out.println("login user id "
				+ AppPrefenceManager.getUserType(getActivity()));
		if (AppPrefenceManager.getUserType(getActivity()).equals("6")) {
			userId = AppPrefenceManager.getUserId(getActivity());
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) {
			userId = AppPrefenceManager.getUserId(getActivity());
		} else {
			userId = AppPrefenceManager.getUserId(getActivity());
		}
		if (VKCObjectConstants.selectedDealerId.equals("")) {
			typeUserId = VKCObjectConstants.selectedRetailerId;
			type = "2";
		} else {
			typeUserId = VKCObjectConstants.selectedDealerId;
			type = "1";
		}
		System.out.println("user id credit " + userId);
		String name[] = { "user_id", "role_id", "customer_id", "type" };
		String values[] = { userId,
				AppPrefenceManager.getUserType(getActivity()), typeUserId, type };

		final VKCInternetManager manager = new VKCInternetManager(
				GET_PAYMENT_STATUS);


		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						parseResponse(successResponse);
						if (creditModels.size() > 0 && !isError) {
							setAdapter();
						} else {
							VKCUtils.showtoast(getActivity(), 17);
						}

					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);
						// isError=true;
					}
				});
	}

	private void setAdapter() {
		mDealersListView.setAdapter(new CreditPaymentStatusAdapter(
				getActivity(), creditModels));
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
			for (int j = 0; j < jsonArrayresponse.length(); j++) {
				CreditPaymentModel creditPaymentModel = new CreditPaymentModel();
				JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);

				creditPaymentModel.setmName(jsonObjectZero
						.optString("Customer_Name"));
				creditPaymentModel.setmId(jsonObjectZero
						.optString("Customer_ID"));
				creditPaymentModel.setmTotalBalance(jsonObjectZero
						.optString("Total_Balance"));
				creditPaymentModel.setmTotalDue(jsonObjectZero
						.optString("Total_Due"));
				creditPaymentModel.setmType1100(jsonObjectZero
						.optString("1100"));
				creditPaymentModel.setmType1200(jsonObjectZero
						.optString("1200"));
				creditPaymentModel.setmType1300(jsonObjectZero
						.optString("1300"));
				creditPaymentModel.setmType1400(jsonObjectZero
						.optString("1400"));
				creditPaymentModel.setmType1500(jsonObjectZero
						.optString("1500"));
				creditPaymentModel.setmType1600(jsonObjectZero
						.optString("1600"));
				creditPaymentModel.setmType2000(jsonObjectZero
						.optString("2000"));
				creditPaymentModel.setCity(jsonObjectZero.optString("city"));
				creditPaymentModel.setDistrict(jsonObjectZero
						.optString("district"));
				creditPaymentModel.setState_name(jsonObjectZero
						.optString("state_name"));
				creditModels.add(creditPaymentModel);
			}

		} catch (Exception e) {
			isError = true;
		}
	}

	public class OnClickView implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v == imageViewSearch) {
				 if(etSearchText.getText().length()>0){
					 setDealerShopList(etSearchText.getText().toString());
					 //etSearchText.setText("");
				 }else{
					Toast.makeText(mActivity, "Please enter your search keyword", Toast.LENGTH_SHORT).show();
				 }

				//getPaymentStatusApi();
				
			}else if(v==mRelDealer){
				rlSearchContainer.setVisibility(View.VISIBLE);
				etSearchText.setText("");
				mDealersListView.setVisibility(View.GONE);
				
				mRelDealer.setBackgroundColor(mActivity.getResources().getColor(R.color.dark_red));
				mRelRetailer.setBackgroundColor(mActivity.getResources().getColor(R.color.light_red));
				mType="Dealer";
				getDistrictApi(AppPrefenceManager.getStateCode(mActivity));
				dealersShopModels.clear();
				dealersListViewAdapter.notifyDataSetChanged();
				VKCObjectConstants.selectedRetailerId = "";
//				ArrayList<DealersShopModel> dealersShopModels=new ArrayList<>();
//				mDealersListView.setAdapter(new DealersListViewAdapter(
//						dealersShopModels, mActivity));
				
				
			}else if(v==mRelRetailer){
				rlSearchContainer.setVisibility(View.VISIBLE);
				mDealersListView.setVisibility(View.GONE);
				etSearchText.setText("");
				mRelDealer.setBackgroundColor(mActivity.getResources().getColor(R.color.light_red));
				mRelRetailer.setBackgroundColor(mActivity.getResources().getColor(R.color.dark_red));
				mType="Retailer";
				getDistrictApi(AppPrefenceManager.getStateCode(mActivity));
				dealersShopModels.clear();
				dealersListViewAdapter.notifyDataSetChanged();
				VKCObjectConstants.selectedDealerId = "";
				
//				ArrayList<DealersShopModel> dealersShopModels=new ArrayList<>();
//				mDealersListView.setAdapter(new DealersListViewAdapter(
//						dealersShopModels, mActivity));
			}

//			if (v == mTxtDealer) {
//				mTxtRetailer.setText("Select Retailer");
//				Intent intent = new Intent(getActivity(),
//						RetailersListViewOnSearch.class);
//				if (!mTxtDealer.getText().toString()
//						.equalsIgnoreCase("Select Dealer")
//						&& !mTxtDealer.getText().toString()
//								.equalsIgnoreCase("")) {
//					System.out.println("dealer string1 "+mTxtDealer.getText().toString());
//					mTxtRetailer.setClickable(false);
//				} else {
//					System.out.println("dealer string2 "+mTxtDealer.getText().toString());
//					mTxtRetailer.setClickable(true);
//				}
//				intent.putExtra("mType", "Dealer");
//				VKCObjectConstants.selectedRetailerId = "";
//				VKCObjectConstants.textDealer = mTxtDealer;
//				startActivity(intent);
//			}
//			if (v == mTxtRetailer) {
//				mTxtDealer.setText("Select Dealer");
//				Intent intent = new Intent(getActivity(),
//						RetailersListViewOnSearch.class);
//				if (!mTxtRetailer.getText().toString()
//						.equalsIgnoreCase("Select Retailer")
//						&& !mTxtRetailer.getText().toString()
//								.equalsIgnoreCase("")) {
//					System.out.println("retailer string1 "+mTxtRetailer.getText().toString());
//					mTxtDealer.setClickable(false);
//				} else {
//					System.out.println("retailer string2 "+mTxtRetailer.getText().toString());
//					mTxtDealer.setClickable(true);
//				}
//				intent.putExtra("mType", "Retailer");
//				VKCObjectConstants.selectedDealerId = "";
//				VKCObjectConstants.textRetailer = mTxtRetailer;
//				startActivity(intent);
//			}
//			if (v == clrRetail) {
//				mTxtDealer.setClickable(true);
//				mTxtRetailer.setClickable(true);
//				mTxtRetailer.setText("Select Retailer");
//				mDealersListView.setAdapter(null);
//			}
//			if (v == clrDealer) {
//				mTxtDealer.setClickable(true);
//				mTxtRetailer.setClickable(true);
//				mTxtDealer.setText("Select Dealer");
//				mDealersListView.setAdapter(null);
//			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// getPaymentStatusApi();

	}

	private void getDistrictApi(final String stateCode) {
		VKCInternetManager manager = null;

		if (mType.equals("Retailer")) {
			manager = new VKCInternetManager(GET_RETAILERS);
		} else if (mType.equals("Dealer")) {
			manager = new VKCInternetManager(GET_DEALERS);
		}
		String name[] = { "state" };
		String value[] = { stateCode };

		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				// parseJSON(successResponse);
				parseDistrictJSON(successResponse, stateCode);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				mIsError = true;

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
		JSONArray arrayShops;
		try {
			arrayShops = jsonObject.getJSONArray("city");
			for (int i = 0; i < arrayShops.length(); i++) {
				cityModels.add(parseCity(arrayShops.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dealersDistrictModel.setCityModels(cityModels);
		return dealersDistrictModel;

	}

	private DealersCityModel parseCity(JSONObject jsonObject) {
		ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
		DealersCityModel cityModel = new DealersCityModel();
		cityModel.setCityName(jsonObject.optString("city"));
		JSONArray arrayShops;
		try {
			arrayShops = jsonObject.getJSONArray("shop");
			for (int i = 0; i < arrayShops.length(); i++) {
				dealersShopModels.add(parseShop(arrayShops.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cityModel.setDealersShopModels(dealersShopModels);
		return cityModel;

	}

	private DealersShopModel parseShop(JSONObject jsonObject) {
		DealersShopModel dealersShopModel = new DealersShopModel();
		dealersShopModel.setAddress(jsonObject.optString("address"));

		dealersShopModel.setCity(jsonObject.optString("city"));
		dealersShopModel.setContact_person(jsonObject
				.optString("contact_person"));
		dealersShopModel.setCountry(jsonObject.optString("country"));
		dealersShopModel.setCustomer_id(jsonObject.optString("customer_id"));
		dealersShopModel.setId(jsonObject.optString("id"));
		dealersShopModel.setName(jsonObject.optString("name"));
		dealersShopModel.setPhone(jsonObject.optString("phone"));
		dealersShopModel.setPincode(jsonObject.optString("pincode"));
		dealersShopModel.setState(jsonObject.optString("state"));
		dealersShopModel.setState_name(jsonObject.optString("state_name"));
		return dealersShopModel;

	}
	private void setDealerShopList(String searchText) {
		dealersShopModels.clear();
		
		
		for(int districtindex=0;districtindex<districtModels.size();districtindex++){
			ArrayList<DealersCityModel > cityModels=districtModels.get(districtindex).getCityModels();
			   for(int cityIndex=0;cityIndex<cityModels.size();cityIndex++){
				   ArrayList<DealersShopModel> shopModels=cityModels.get(cityIndex).getDealersShopModels();
				       for(int shopIndex=0;shopIndex<shopModels.size();shopIndex++){
				    	  
				    	      if(shopModels.get(shopIndex).getName().toLowerCase().contains(searchText.toLowerCase())){
				    	    	  dealersShopModels.add(shopModels.get(shopIndex));
				    	      }
				       }
				   
			   }
			   
		}
		if(dealersShopModels.size()<1){
			Toast.makeText(mActivity, "No Item found for this search criteria", Toast.LENGTH_SHORT).show();
		}
		dealersListViewAdapter.notifyDataSetChanged();

	}
	
	private class ClickOnItemView implements OnItemClickListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			rlSearchContainer.setVisibility(View.GONE);
			mDealersListView.setVisibility(View.VISIBLE);
			

			if (mType.equals("Retailer")) {
//				VKCObjectConstants.textRetailer.setText(dealersShopModels.get(
//						position).getName());
				VKCObjectConstants.selectedRetailerId = dealersShopModels.get(
						position).getId();
			} else if (mType.equals("Dealer")) {
//				VKCObjectConstants.textDealer.setText(dealersShopModels.get(
//						position).getName());
				VKCObjectConstants.selectedDealerId = dealersShopModels.get(
						position).getId();
			}
			//finish();
			getPaymentStatusApi();
		}

	}

}
