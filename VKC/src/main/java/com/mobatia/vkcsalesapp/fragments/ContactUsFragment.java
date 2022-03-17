package com.mobatia.vkcsalesapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter;
import com.mobatia.vkcsalesapp.adapter.SubDealersListViewAdapter;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog.OnDialogItemSelectListener;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.DealersCityModel;
import com.mobatia.vkcsalesapp.model.DealersDistrictModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;
import com.mobatia.vkcsalesapp.model.DealersStateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactUsFragment extends Fragment implements VKCUrlConstants {

	private View mRootView;
	RelativeLayout rlState;
	RelativeLayout rlDistrict;
	RelativeLayout rlPlace;
	RelativeLayout submitLayout;
	LinearLayout llTop;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	ListView dealersListView;
	FragmentTransaction mFragmentTransaction;
	boolean mIsError = false;
	TextView textViewState;
	TextView textViewDistrict;
	TextView textViewPlace;
	FragmentManager mFragmentManager;
	ImageView imageViewSearch;
	ArrayList<DealersStateModel> dealersStateModels = new ArrayList<DealersStateModel>();
	ArrayList<DealersDistrictModel> districtModels = new ArrayList<DealersDistrictModel>();
	ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
	ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
	
	String roleId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_dealers_listview,
				container, false);
		roleId = AppPrefenceManager.getUserType(getActivity());
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		AppController.isCart=false;
		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_imageview, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		
		
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		

		
		setDisplayParams();
		init(mRootView, savedInstanceState);

		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void setLayoutAlignment(View v) {
		int height = (int) (mDisplayHeight * .35);
		llTop.getLayoutParams().height = (int) height;
		LinearLayout secOneLL = (LinearLayout) v.findViewById(R.id.secOneLL);
		LinearLayout secTwoLL = (LinearLayout) v.findViewById(R.id.secTwoLL);
		LinearLayout secThreeLL = (LinearLayout) v
				.findViewById(R.id.secThreeLL);
		RelativeLayout secFourRL = (RelativeLayout) v
				.findViewById(R.id.secFourRL);
		secOneLL.getLayoutParams().height = ((int) height) / 4;
		secTwoLL.getLayoutParams().height = ((int) height) / 4;
		secThreeLL.getLayoutParams().height = ((int) height) / 4;
		secFourRL.getLayoutParams().height = ((int) height) / 4;
	}

	private void init(View v, Bundle savedInstanceState) {
		imageViewSearch = (ImageView) v.findViewById(R.id.imageViewSearch);
		submitLayout=(RelativeLayout)v.findViewById(R.id.submitMyDealer);
		textViewState = (TextView) v.findViewById(R.id.textViewState);
		textViewDistrict = (TextView) v.findViewById(R.id.textViewDistrict);
		textViewPlace = (TextView) v.findViewById(R.id.textViewPlace);
		rlState = (RelativeLayout) v.findViewById(R.id.rlState);
		rlDistrict = (RelativeLayout) v.findViewById(R.id.rlDistrict);
		rlPlace = (RelativeLayout) v.findViewById(R.id.rlPlace);
		llTop = (LinearLayout) v.findViewById(R.id.llTop);
		dealersListView = (ListView) v.findViewById(R.id.dealersListView);

		// Dealer List Click
		dealersListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String id = dealersShopModels.get(arg2).getId();
				if (AppController.listDealers.size() == 0) {
					if (AppController.listDealers.size() < 7)
						AppController.listDealers.add(id);
					else
						Toast.makeText(getActivity(),
								"Only six dealers allowed to add",
								Toast.LENGTH_SHORT).show();
				}

			}
		});
		setLayoutAlignment(v);
		rlState.setOnClickListener(new ClickOnView());
		rlDistrict.setOnClickListener(new ClickOnView());
		rlPlace.setOnClickListener(new ClickOnView());
		imageViewSearch.setOnClickListener(new ClickOnView());
		
		if (VKCUtils.checkInternetConnection(getActivity())) {

			getStateApi();
		} else {
			mIsError = true;
			// CustomToast toast = new CustomToast(getActivity());
			// toast.show(0);
			VKCUtils.showtoast(getActivity(), 0);
		}
	}

	private void parseJSON(String successResponse) {
		// TODO Auto-generated method stub

		try {
			JSONObject respObj = new JSONObject(successResponse);
			JSONArray respArray = respObj.getJSONArray("states");
			for (int i = 0; i < respArray.length(); i++) {
				// JSONObject objState = respArray.getJSONObject(i);

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

	private void getStateApi() {
		
		final VKCInternetManager manager = new VKCInternetManager(
				DEALERS_GETSTATE);

		manager.getResponse(getActivity(), new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				parseJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				mIsError = true;

			}
		});

	}

	private void getDistrictApi(final String stateCode) {
		final VKCInternetManager manager = new VKCInternetManager(GET_RETAILERS);
		String name[] = { "state" };
		String value[] = { stateCode };

		manager.getResponsePOST(getActivity(), name, value,
				new ResponseListener() {

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
		dealersShopModels = new ArrayList<DealersShopModel>();
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

	private class ClickOnView implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == rlState) {
				dealersListView.setVisibility(View.INVISIBLE);
				String[] states = new String[dealersStateModels.size()];
				for (int i = 0; i < dealersStateModels.size(); i++) {
					states[i] = dealersStateModels.get(i).getStateName();
				}
				StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
						getActivity(), states, textViewState, "States",
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
				textViewPlace.setText("");
				textViewDistrict.setText("");

			}
			if (v == rlDistrict) {
				dealersListView.setVisibility(View.INVISIBLE);
				if (textViewState.getText().length() != 0) {
					String[] district = new String[districtModels.size()];
					for (int i = 0; i < districtModels.size(); i++) {
						district[i] = districtModels.get(i).getDistrictName();
					}
					StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
							getActivity(), district, textViewDistrict,
							"District", new OnDialogItemSelectListener() {

								@Override
								public void itemSelected(int position) {
									// TODO Auto-generated method stub
									cityModels.clear();
									cityModels.addAll(districtModels.get(
											position).getCityModels());

								}
							});
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.setCancelable(true);
					dialog.show();
					textViewPlace.setText("");
				} else {
					// CustomToast toast = new CustomToast(getActivity());
					// toast.show(20);
					VKCUtils.showtoast(getActivity(), 20);
				}
			}
			if (v == rlPlace) {
				dealersListView.setVisibility(View.INVISIBLE);
				if (textViewDistrict.getText().length() != 0) {
					String[] city = new String[cityModels.size()];
					for (int i = 0; i < cityModels.size(); i++) {
						city[i] = cityModels.get(i).getCityName();
					}
					StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
							getActivity(), city, textViewPlace, "Place",
							new OnDialogItemSelectListener() {

								@Override
								public void itemSelected(int position) {
									dealersShopModels.clear();
									dealersShopModels.addAll(cityModels.get(
											position).getDealersShopModels());

								}

							});
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.setCancelable(true);
					dialog.show();
				} else {

					VKCUtils.showtoast(getActivity(), 21);
				}
			}
			if (v == imageViewSearch) {
				if (textViewPlace.getText().length() != 0) {
					setDealerShopList(dealersShopModels);
				} else {

					VKCUtils.showtoast(getActivity(), 22);
				}
			}

		}

	}

	private void setDealerShopList(ArrayList<DealersShopModel> dealersShopModels) {
		dealersListView.setVisibility(View.VISIBLE);
		String dealerCount=AppPrefenceManager.getDealerCount(getActivity());
		int Count_Dealer=Integer.parseInt(dealerCount);
		if (roleId.equals("7")&&Count_Dealer==0)
		{
			if(dealersShopModels.size()>0)
			{
			dealersListView.setAdapter(new SubDealersListViewAdapter(
					dealersShopModels, getActivity()));
			submitLayout.setVisibility(View.VISIBLE);
			}
			else
			{
				submitLayout.setVisibility(View.GONE);
			}
		}
		else
		{
			
		dealersListView.setAdapter(new DealersListViewAdapter(
				dealersShopModels, getActivity()));
		}

	}

	private void submitMyDealersApi() {
		final VKCInternetManager manager = new VKCInternetManager(
				SUBMIT_MY_DEALER_URL);
		// Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
		String name[] = { "subdealer_id", "dealers_id" };
		String value[] = { AppPrefenceManager.getUserId(getActivity()),
				AppController.listDealers.toString() };

		manager.getResponsePOST(getActivity(), name, value,
				new ResponseListener() {

					private HomeFragment mHomeFragment;

					@Override
					public void responseSuccess(String successResponse) {

						// parseJSON(successResponse);
						try {
							JSONObject responseObj = new JSONObject(
									successResponse);
							String response = responseObj.getString("response");
							JSONObject resultObj = new JSONObject(response);
							String status = resultObj.getString("status");
							Toast.makeText(getActivity(), status,
									Toast.LENGTH_SHORT).show();
							if(status.equals("Success"))
							{

								AppController.listDealers.clear();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						mIsError = true;

					}
				});

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
