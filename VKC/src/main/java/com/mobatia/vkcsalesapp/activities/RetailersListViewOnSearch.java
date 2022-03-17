/**
 *
 */
package com.mobatia.vkcsalesapp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog;
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog.OnDialogItemSelectListener;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
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

/**
 * @author mobatia-user
 */
public class RetailersListViewOnSearch extends AppCompatActivity implements
        VKCUrlConstants, VKCDbConstants {
    private DataBaseManager databaseManager;
    RelativeLayout rlState;
    RelativeLayout rlDistrict;
    RelativeLayout rlPlace;
    LinearLayout llTop;
    int mDisplayWidth = 0;
    int mDisplayHeight = 0;
    ListView dealersListView;
    boolean mIsError = false;
    TextView textViewState;
    TextView textViewDistrict;
    TextView textViewPlace;
    ImageView imageViewSearch;
    private Activity mActivity;
    private String mType;
    ArrayList<DealersStateModel> dealersStateModels = new ArrayList<DealersStateModel>();
    ArrayList<DealersDistrictModel> districtModels = new ArrayList<DealersDistrictModel>();
    ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
    ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
    String key, userType;
    private int tableCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dealers_listview);
        databaseManager = new DataBaseManager(this);
        mActivity = this;
        AppController.isSelectedDealer = false;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mType = extras.getString("mType");

            if (mType.equals("SalesHead")) {

                key = extras.getString("key");
                userType = AppPrefenceManager.getCustomerCategory(mActivity);
            }

        }

        initialiseUI();
    }

    private void initialiseUI() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        imageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);
        textViewState = (TextView) findViewById(R.id.textViewState);
        textViewDistrict = (TextView) findViewById(R.id.textViewDistrict);
        textViewPlace = (TextView) findViewById(R.id.textViewPlace);

        rlState = (RelativeLayout) findViewById(R.id.rlState);
        rlDistrict = (RelativeLayout) findViewById(R.id.rlDistrict);
        rlPlace = (RelativeLayout) findViewById(R.id.rlPlace);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        dealersListView = (ListView) findViewById(R.id.dealersListView);
        rlState.setOnClickListener(new ClickOnView());
        rlDistrict.setOnClickListener(new ClickOnView());
        rlPlace.setOnClickListener(new ClickOnView());
        imageViewSearch.setOnClickListener(new ClickOnView());
        dealersListView.setOnItemClickListener(new ClickOnItemView());
        if (VKCUtils.checkInternetConnection(mActivity)) {
            if (AppPrefenceManager.getUserType(this).equals("7")) {
                llTop.setVisibility(View.GONE);
                getMyDealersApi();
            } else if (AppPrefenceManager.getUserType(this).equals("4")) {
                llTop.setVisibility(View.GONE);
                // getMyDealersApi();
                getMyDealersSalesHeadApi();
            } else {
                getStateApi();
            }
        } else {
            mIsError = true;
            VKCUtils.showtoast(mActivity, 0);
        }
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

    private void getStateApi() {
        final VKCInternetManager manager = new VKCInternetManager(
                DEALERS_GETSTATE);
        manager.getResponse(mActivity, new ResponseListener() {

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
        VKCInternetManager manager = null;

        if (mType.equals("Retailer")) {
            manager = new VKCInternetManager(GET_RETAILERS);
        } else if (mType.equals("Dealer")) {
            manager = new VKCInternetManager(GET_DEALERS);
        } else if (mType.equals("SalesHead")) {
            manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
        }

        String name[] = {"state"};
        String value[] = {stateCode};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

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
        dealersShopModel.setDealerId(jsonObject.optString("dealerId"));
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
                        mActivity, states, textViewState, "States",
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
                            mActivity, district, textViewDistrict, "District",
                            new OnDialogItemSelectListener() {

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

                    VKCUtils.showtoast(mActivity, 20);
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
                            mActivity, city, textViewPlace, "Place",
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

                    VKCUtils.showtoast(mActivity, 21);
                }
            }
            if (v == imageViewSearch) {
                if (textViewPlace.getText().length() != 0) {
                    setDealerShopList(dealersShopModels);
                } else {

                    VKCUtils.showtoast(mActivity, 22);
                }
            }
        }

    }

    private void setDealerShopList(ArrayList<DealersShopModel> dealersShopModels) {
        dealersListView.setVisibility(View.VISIBLE);
        dealersListView.setAdapter(new DealersListViewAdapter(
                dealersShopModels, mActivity));

    }

    private class ClickOnItemView implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub

            if (mType.equals("Retailer")) {
                VKCObjectConstants.textRetailer.setText(dealersShopModels.get(
                        position).getName());
                VKCObjectConstants.selectedRetailerId = dealersShopModels.get(
                        position).getId();
                finish();
            } else if (mType.equals("Dealer")) {
                VKCObjectConstants.textDealer.setText(dealersShopModels.get(
                        position).getName());
                VKCObjectConstants.selectedDealerId = dealersShopModels.get(
                        position).getId();
                finish();
            } else if (mType.equals("SubDealer")) {

                VKCObjectConstants.textDealer.setText(dealersShopModels.get(
                        position).getName());
                String did = dealersShopModels.get(position).getDealerId();
                Log.i("dis", "" + did);
                VKCObjectConstants.selectedDealerId = dealersShopModels.get(
                        position).getDealerId();
                AppPrefenceManager.saveSelectedUserId(
                        RetailersListViewOnSearch.this,
                        dealersShopModels.get(position).getDealerId());
                AppController.isSelectedDealer = true;
                finish();
            } else if (mType.equals("SalesHead")) {

                VKCObjectConstants.textDealer.setText(dealersShopModels.get(
                        position).getName());
                AppPrefenceManager.saveSelectedUserName(
                        RetailersListViewOnSearch.this,
                        dealersShopModels.get(position).getName());
                AppPrefenceManager.saveSelectedUserId(
                        RetailersListViewOnSearch.this,
                        dealersShopModels.get(position).getDealerId());
                String did = dealersShopModels.get(position).getDealerId();
                Log.i("dis", "" + did);
                VKCObjectConstants.selectedDealerId = dealersShopModels.get(
                        position).getId();
                AppController.isSelectedDealer = true;
                clearDb();
                finish();

            }

        }

    }

    private void getMyDealersApi() {
        VKCInternetManager manager = null;
        dealersShopModels.clear();
        String name[] = {"subdealer_id"};
        String value[] = {AppPrefenceManager.getUserId(this)};
        manager = new VKCInternetManager(LIST_MY_DEALERS_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                parseMyDealerJSON(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                mIsError = true;

            }
        });

    }

    private void parseMyDealerJSON(String successResponse) {
        // TODO Auto-generated method stub

        try {

            JSONObject respObj = new JSONObject(successResponse);
            JSONObject response = respObj.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                JSONArray respArray = response.getJSONArray("dealers");
                for (int i = 0; i < respArray.length(); i++) {

                    dealersShopModels
                            .add(parseShop(respArray.getJSONObject(i)));
                }
                dealersListView.setVisibility(View.VISIBLE);
                dealersListView.setAdapter(new DealersListViewAdapter(
                        dealersShopModels, mActivity));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //	To Get The Subdealer or Dealers List


    private void getMyDealersSalesHeadApi() {
        VKCInternetManager manager = null;
        dealersShopModels.clear();
        String name[] = {"saleshead_id", "customer_type", "search_value"};
        String value[] = {AppPrefenceManager.getUserId(this), userType, key};
        manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                parseMyDealerSalesHeadJSON(successResponse);
                if (dealersShopModels.size() == 0) {
                    CustomToast toast = new CustomToast(mActivity);
                    toast.show(37);
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                mIsError = true;

            }
        });

    }

    private void parseMyDealerSalesHeadJSON(String successResponse) {
        // TODO Auto-generated method stub

        try {

            JSONObject respObj = new JSONObject(successResponse);
            JSONObject response = respObj.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                JSONArray respArray = response.getJSONArray("dealers");
                for (int i = 0; i < respArray.length(); i++) {

                    dealersShopModels
                            .add(parseShop(respArray.getJSONObject(i)));
                }
                dealersListView.setVisibility(View.VISIBLE);
                dealersListView.setAdapter(new DealersListViewAdapter(
                        dealersShopModels, mActivity));
                if (dealersShopModels.size() == 0) {
                    CustomToast toast = new CustomToast(mActivity);
                    toast.show(43);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }


    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(
                RetailersListViewOnSearch.this);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

}
