package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.adapter.HomeBrandBannerOneAdapter;
import com.mobatia.vkcsalesapp.adapter.HomeBrandBannerTwoAdapter;
import com.mobatia.vkcsalesapp.adapter.HomeImageBannerAdapter;
import com.mobatia.vkcsalesapp.adapter.HomeImageBannerAdapterFirst;
import com.mobatia.vkcsalesapp.adapter.HomeOfferBannerAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandBannerModel;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CaseModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.HomeImageBannerModel;
import com.mobatia.vkcsalesapp.model.OfferModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;


public class HomeFragment extends Fragment implements VKCUrlConstants,
        VKCJsonTagConstants {
    // this Fragment will be called from MainActivity
    private View mRootView;
    private RelativeLayout mRelBanner;
    private RelativeLayout mRelArrivalBanner;
    private RelativeLayout mRelMiddleBanner;
    private RelativeLayout mRelFooter;
    private RelativeLayout mBottomBar;
    private LinearLayout mLnrFooter, LinearDisable;
    private DisplayManagerScale mDisplayManager;
    int height, width;
    private ArrayList<ProductModel> productModels;
    private ImageView imgSearch;
    private EditText edtSearch;
    public static String currentVersion, newVersion;
    private ArrayList<HomeImageBannerModel> homeImageBannerModels;
    ViewPager myPager;
    ViewPager offerPager;
    ViewPager brandBannerOne;
    boolean bannerShow = true;
    int sliderBannerOfferCount = 0;
    int sliderBannermyPager = 0;
    int sliderBannerbrandBannerOne = 0;


    public HomeFragment() {

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        bannerShow = true;

        startBannerShow();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // Log.v("LOG", "13012015 onPause");
        bannerShow = false;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AppPrefenceManager.saveOfferIDs(getActivity(), "");

        mRootView = inflater.inflate(R.layout.home_activity_fragment,
                container, false);
        final ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        AppController.isCart = false;
        View viewActionBar = getActivity().getLayoutInflater().inflate(
                R.layout.actionbar_imageview, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                // Center the textview in the ActionBar !H
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        abar.setCustomView(viewActionBar, params);

        abar.setDisplayShowHomeEnabled(true);

        abar.setDisplayShowCustomEnabled(true);
        abar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        AppPrefenceManager.setFillTable(getActivity(), "false");
        initialiseUI();
        currentVersion = getVersion();
        DatabaseHelper database = new DatabaseHelper(getActivity(),"VKC");


        try {
            database.createDataBase();

        } catch (IOException e) {
            e.printStackTrace();
        }


        new GetAppVersion().execute(BASE_URL + URL_GET_APP_VERSION);
        setImageBanner(mRootView);

        LinearDisable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateApp(getActivity());
            }
        });
        return mRootView;
    }

    private String getVersion() {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return packageinfo.versionName.toString();
    }

    private void setImageBanner(View v) {

        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String NewArrival = AppPrefenceManager
                .getNewArrivalBannerResponse(getActivity());

        productModels = new ArrayList<ProductModel>();

        try {

            JSONArray jsonArrayresponse = new JSONArray(NewArrival);
            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                System.out.println("16122014 setImageBanner IN loop" + j);
                HomeImageBannerModel bannerModel = new HomeImageBannerModel();
                JSONObject productJSONObject = jsonArrayresponse.getJSONObject(
                        j).getJSONObject(JSON_ARRIVAL_RESPONSE);
                ProductModel productModel = parseProductModelTemp(productJSONObject);

                productModels.add(productModel);
                JSONObject jsonArrayZero = jsonArrayresponse.getJSONObject(j);
                JSONObject bannerArray = jsonArrayZero
                        .getJSONObject(JSON_ARRIVAL_BANNER);
                bannerModel.setId(bannerArray.getString(JSON_ARRIVAL_BANNERID));
                bannerModel.setBannerUrl(BASE_URL
                        + bannerArray.getString(JSON_ARRIVAL_BANNER_IMAGE));
                homeImageBannerModels.add(bannerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HomeImageBannerAdapterFirst adapter = new HomeImageBannerAdapterFirst(
                getActivity(), homeImageBannerModels, productModels, 0);
        sliderBannermyPager = homeImageBannerModels.size();
        myPager = (ViewPager) v.findViewById(R.id.reviewpager);
        myPager.setVisibility(View.VISIBLE);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        setBannerPosition(v);
        setOfferBanners(v);
        setThreeImageBanner(v);
        setBrandBannersOneAndTwo(v);

    }

    private void setOfferBanner(View v) {

        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String hreeImaegResp = AppPrefenceManager
                .getNewArrivalBannerResponse(getActivity());

        HomeImageBannerModel bannerModel = null;
        try {
            JSONArray jsonArray = new JSONArray(hreeImaegResp);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public BrandBannerModel getBandBannerObjectValues(JSONObject object)
            throws JSONException {

        BrandBannerModel brandBannerModel = new BrandBannerModel();
        BrandTypeModel typeModel = getBrandTypeModel(object
                .getJSONObject(JSON_BRAND_RESPONSE));
        JSONObject jsonObjectBrandImage = object
                .getJSONObject(JSON_BRAND_IMAGE);

        brandBannerModel.setId(jsonObjectBrandImage.getString(JSON_BRAND_ID));

        brandBannerModel.setBrandBannerOne(BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_BANNERIMAGE));
        brandBannerModel.setBrandBannerTwo(BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_IMAGENAME));

        brandBannerModel.setTypeModel(typeModel);

        return brandBannerModel;

    }

    public OfferModel getOffersObjectValues(JSONObject object)
            throws JSONException {

        OfferModel offerModel = new OfferModel();
        offerModel.setId(object.getString(JSON_SETTINGS_OFFERID));
        offerModel.setName(object.getString(JSON_SETTINGS_OFFER));
        offerModel.setOfferBanner(BASE_URL
                + object.getString(JSON_SETTINGS_OFFERIMAGE));

        return offerModel;

    }

    private void setOfferBanners(View v) {
        ArrayList<OfferModel> offerModels = new ArrayList<OfferModel>();
        try {
            JSONArray offerObjArray = new JSONArray(
                    AppPrefenceManager.getJsonOfferResponse(getActivity()));

            for (int i = 0; i < offerObjArray.length(); i++) {
                JSONObject responseObj = offerObjArray.getJSONObject(i);

                offerModels.add(getOffersObjectValues(responseObj));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        offerPager = (ViewPager) v.findViewById(R.id.reviewpagerOffer);

        offerPager.setVisibility(View.VISIBLE);

        HomeOfferBannerAdapter adapter = new HomeOfferBannerAdapter(
                getActivity(), offerModels/* ,displayView */);
        sliderBannerOfferCount = offerModels.size();
        offerPager.setAdapter(adapter);
        offerPager.setCurrentItem(0);

    }

    private void setBrandBannersOneAndTwo(View v) {
        ArrayList<BrandBannerModel> brandBannerModels = new ArrayList<BrandBannerModel>();
        try {

            JSONArray bannerObjArray = new JSONArray(
                    AppPrefenceManager.getBrandBannerResponse(getActivity()));

            for (int i = 0; i < bannerObjArray.length(); i++) {
                JSONObject responseObj = bannerObjArray.getJSONObject(i);

                brandBannerModels.add(getBandBannerObjectValues(responseObj));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        brandBannerOne = (ViewPager) v.findViewById(R.id.pagerBrandBannerOne);
        brandBannerOne.setVisibility(View.VISIBLE);
        HomeBrandBannerOneAdapter bannerOneAdapter = new HomeBrandBannerOneAdapter(
                getActivity(), brandBannerModels/* ,displayView */);
        sliderBannerbrandBannerOne = brandBannerModels.size();
        brandBannerOne.setAdapter(bannerOneAdapter);
        brandBannerOne.setCurrentItem(0);

        ViewPager brandBannerTwo = (ViewPager) v
                .findViewById(R.id.pagerBrandBannerTwo);
        brandBannerTwo.setVisibility(View.VISIBLE);

        HomeBrandBannerTwoAdapter bannerTwoAdapter = new HomeBrandBannerTwoAdapter(
                getActivity(), brandBannerModels/* ,displayView */, 1);

        brandBannerTwo.setAdapter(bannerTwoAdapter);

    }

    private ProductModel parseProductModelTemp(JSONObject jsonObjectZero) {
        ProductModel productModel = new ProductModel();
        try {

            productModel.setProductdescription(jsonObjectZero
                    .optString("productdescription"));
            productModel.setCategoryName(jsonObjectZero
                    .optString(JSON_CATEGORY_NAME));
            productModel.setCategoryId(jsonObjectZero.optString("categoryid"));
            productModel.setmSapId(jsonObjectZero.optString("productSapId"));
            productModel.setCategoryId(jsonObjectZero
                    .optString(JSON_CATEGORY_ID));

            productModel.setmProductPrize(jsonObjectZero
                    .optString(JSON_CATEGORY_COST));
            productModel.setId(jsonObjectZero.optString(JSON_PRODUCT_ID));

            productModel.setmProductName(jsonObjectZero
                    .optString(JSON_PRODUCT_NAME));
            productModel.setProductquantity(jsonObjectZero
                    .optString(JSON_PRODUCT_QTY));

            productModel.setProductDescription(jsonObjectZero
                    .optString(JSON_PRODUCT_DESCRIPTION));

            productModel.setProductViews(jsonObjectZero
                    .optString(JSON_PRODUCT_VIEWS));

            productModel.setTimeStamp(jsonObjectZero
                    .optString(JSON_PRODUCT_OFFER));

            productModel.setmProductOff(jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP));


            JSONArray productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR);
            JSONArray productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE);
            JSONArray productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE);
            JSONArray productNewArrivalArray = jsonObjectZero
                    .getJSONArray("new_arrivals");
            JSONArray productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE);
            JSONArray productCaseArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_CASE);

            ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
            for (int i = 0; i < productColorArray.length(); i++) {

                ColorModel colorModel = new ColorModel();
                JSONObject jsonObject = productColorArray.getJSONObject(i);
                colorModel.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                colorModel.setColorName(jsonObject.optString("color_name"));
                colorModels.add(colorModel);

            }
            productModel.setProductColor(colorModels);
            ArrayList<ProductImages> newArrList = new ArrayList<ProductImages>();
            for (int i = 0; i < productNewArrivalArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productNewArrivalArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                images.setProductName(jsonObject.optString("product_name"));
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                images.setColorModel(colorModel);
                images.setCatId(jsonObject.optString("categoryid"));
                newArrList.add(images);

            }
            productModel.setmNewArrivals(newArrList);
            // ////////////
            ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
            for (int i = 0; i < productImageArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productImageArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                images.setColorModel(colorModel);
                productImages.add(images);

            }
            productModel.setProductImages(productImages);
            // ///
            ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
            for (int i = 0; i < productSizeArray.length(); i++) {

                SizeModel sizeModel = new SizeModel();
                JSONObject jsonObject = productSizeArray.getJSONObject(i);
                sizeModel.setId(jsonObject.optString(JSON_SETTINGS_SIZEID));
                sizeModel.setName(jsonObject.optString(JSON_SETTINGS_SIZENAME));

                sizeModels.add(sizeModel);

            }
            productModel.setmProductSize(sizeModels);
            // /////
            ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
            for (int i = 0; i < productTypeArray.length(); i++) {

                brandTypeModels.add(getBrandTypeModel(productTypeArray
                        .getJSONObject(i)));

            }
            productModel.setProductType(brandTypeModels);
            ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
            for (int i = 0; i < productCaseArray.length(); i++) {

                CaseModel caseModel = new CaseModel();
                JSONObject jsonObject = productCaseArray.getJSONObject(i);
                caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
                caseModel.setName(jsonObject.optString(JSON_SETTINGS_CASENAME));

                caseModels.add(caseModel);

            }
            productModel.setmProductCases(caseModels);

        } catch (Exception e) {


        }
        return productModel;
    }

    private BrandTypeModel getBrandTypeModel(JSONObject jsonObject) {
        BrandTypeModel typeModel = new BrandTypeModel();
        typeModel.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
        typeModel.setName(jsonObject.optString(JSON_SETTINGS_BRANDNAME));
        return typeModel;

    }

    private ProductModel parseProductModel(JSONObject jsonObjectZero) {
        ProductModel productModel = new ProductModel();
        try {

            productModel.setProductdescription(jsonObjectZero
                    .optString("productdescription"));
            productModel.setCategoryName(jsonObjectZero
                    .optString(JSON_CATEGORY_NAME));
            productModel.setCategoryId(jsonObjectZero.optString("categoryid"));
            productModel.setmSapId(jsonObjectZero.optString("productSapId"));
            productModel.setCategoryId(jsonObjectZero
                    .optString(JSON_CATEGORY_ID));

            productModel.setCategoryId(jsonObjectZero
                    .optString(JSON_CATEGORY_ID));
            productModel.setCategoryName(jsonObjectZero
                    .optString(JSON_CATEGORY_NAME));
            productModel.setmProductPrize(jsonObjectZero
                    .optString(JSON_CATEGORY_COST));
            productModel.setId(jsonObjectZero.optString(JSON_PRODUCT_ID));
            productModel.setmSapId(jsonObjectZero.optString("productSapId"));
            productModel.setmProductName(jsonObjectZero
                    .optString(JSON_PRODUCT_NAME));
            productModel.setProductquantity(jsonObjectZero
                    .optString(JSON_PRODUCT_QTY));

            productModel.setProductDescription(jsonObjectZero
                    .optString(JSON_PRODUCT_DESCRIPTION));

            productModel.setProductViews(jsonObjectZero
                    .optString(JSON_PRODUCT_VIEWS));

            productModel.setTimeStamp(jsonObjectZero
                    .optString(JSON_PRODUCT_OFFER));

            productModel.setmProductOff(jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP));
            JSONArray productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR);
            JSONArray productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE);
            JSONArray productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE);
            JSONArray productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE);
            JSONArray productCaseArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_CASE);
            JSONArray productNewArrivalArray = jsonObjectZero
                    .getJSONArray("new_arrivals");

            ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
            for (int i = 0; i < productColorArray.length(); i++) {

                ColorModel colorModel = new ColorModel();
                JSONObject jsonObject = productColorArray.getJSONObject(i);
                colorModel.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                colorModel.setColorName(jsonObject.optString("color_name"));
                colorModels.add(colorModel);

            }
            productModel.setProductColor(colorModels);
            ArrayList<ProductImages> newArrList = new ArrayList<ProductImages>();
            for (int i = 0; i < productNewArrivalArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productNewArrivalArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                images.setProductName(jsonObject.optString("product_name"));
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                images.setColorModel(colorModel);
                images.setCatId(jsonObject.optString("categoryid"));
                newArrList.add(images);

            }
            productModel.setmNewArrivals(newArrList);
            // ////////////
            ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
            for (int i = 0; i < productImageArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productImageArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                images.setColorModel(colorModel);
                productImages.add(images);

            }
            productModel.setProductImages(productImages);
            // ///
            ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
            for (int i = 0; i < productSizeArray.length(); i++) {

                SizeModel sizeModel = new SizeModel();
                JSONObject jsonObject = productSizeArray.getJSONObject(i);
                sizeModel.setId(jsonObject.optString(JSON_SETTINGS_SIZEID));
                sizeModel.setName(jsonObject.optString(JSON_SETTINGS_SIZENAME));

                sizeModels.add(sizeModel);

            }
            productModel.setmProductSize(sizeModels);
            // /////
            ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
            for (int i = 0; i < productTypeArray.length(); i++) {

                BrandTypeModel typeModel = new BrandTypeModel();
                JSONObject jsonObject = productTypeArray.getJSONObject(i);
                typeModel.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
                typeModel
                        .setName(jsonObject.optString(JSON_SETTINGS_BRANDNAME));

                brandTypeModels.add(typeModel);

            }
            productModel.setProductType(brandTypeModels);
            ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
            for (int i = 0; i < productCaseArray.length(); i++) {

                CaseModel caseModel = new CaseModel();
                JSONObject jsonObject = productCaseArray.getJSONObject(i);
                caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
                caseModel.setName(jsonObject.optString(JSON_SETTINGS_CASENAME));

                caseModels.add(caseModel);

            }
            productModel.setmProductCases(caseModels);

        } catch (Exception e) {

        }
        return productModel;
    }

    private void setThreeImageBanner(View v) {
        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String hreeImaegResp = AppPrefenceManager
                .getPopularProductSliderResponse(getActivity());

        productModels = new ArrayList<ProductModel>();

        try {

            JSONArray jsonArrayresponse = new JSONArray(
                    AppPrefenceManager
                            .getPopularProductSliderResponse(getActivity()));
            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                HomeImageBannerModel bannerModel = new HomeImageBannerModel();

                ProductModel productModel = parseProductModel(jsonArrayresponse
                        .getJSONObject(j));

                productModels.add(productModel);

                bannerModel.setId(bannerModel.getId());

                bannerModel.setBannerUrl(productModel.getProductImages().get(0)
                        .getImageName());

                bannerModel.setSlideId(productModel.getProductImages().get(0)
                        .getId());
                homeImageBannerModels.add(bannerModel);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        // Last Three image Banner
        HomeImageBannerAdapter adapter = new HomeImageBannerAdapter(
                getActivity(), homeImageBannerModels, productModels, 1);
        ViewPager myPager = (ViewPager) v
                .findViewById(R.id.reviewpagerThreeImage);
        myPager.setVisibility(View.VISIBLE);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
    }


    private void startBannerShow() {
        new AsyncTask<Void, Integer, Void>() {
            int count = 0;

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub

                while (bannerShow) {
                    try {
                        Thread.sleep(1000);
                        count++;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    publishProgress(count);
                }
                return null;
            }

            protected void onProgressUpdate(Integer[] values) {

                if (values[0] % 3 == 0) {
                    if (sliderBannermyPager - 1 == myPager.getCurrentItem()) {
                        myPager.setCurrentItem(0);
                    } else {
                        myPager.setCurrentItem(myPager.getCurrentItem() + 1);
                    }
                } else if (values[0] % 3 == 1) {
                    if (sliderBannerbrandBannerOne - 1 == brandBannerOne
                            .getCurrentItem()) {
                        brandBannerOne.setCurrentItem(0);
                    } else {
                        brandBannerOne.setCurrentItem(brandBannerOne
                                .getCurrentItem() + 1);
                    }
                } else if (values[0] % 3 == 2) {
                    if (sliderBannerOfferCount - 1 == offerPager
                            .getCurrentItem()) {
                        offerPager.setCurrentItem(0);

                    } else {
                        offerPager
                                .setCurrentItem(offerPager.getCurrentItem() + 1);
                    }
                }

            }
        }.execute();
    }

    private void setBannerPosition(View v) {
        RelativeLayout relArrivalBannerNext, relArrivalBannerPrevious;
        relArrivalBannerNext = (RelativeLayout) v
                .findViewById(R.id.relArrivalBannerNext);
        relArrivalBannerPrevious = (RelativeLayout) v
                .findViewById(R.id.relArrivalBannerPrevious);
        relArrivalBannerNext.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                myPager.setCurrentItem(myPager.getCurrentItem() - 1);
                return false;
            }
        });
        relArrivalBannerPrevious.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                myPager.setCurrentItem(myPager.getCurrentItem() + 1);
                return false;
            }
        });

    }

    public int getSupportActionBarHeight(Activity activity) {
        final TypedArray styledAttributes = activity.getTheme()
                .obtainStyledAttributes(
                        new int[]{android.R.attr.actionBarSize});
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initialiseUI() {

        RelativeLayout fragmentHomeBase = (RelativeLayout) mRootView
                .findViewById(R.id.fragmentHomeBase);

        RelativeLayout relBrandBannerOne = (RelativeLayout) mRootView
                .findViewById(R.id.relBrandBannerOne);

        RelativeLayout relBrandBannerTwo = (RelativeLayout) mRootView
                .findViewById(R.id.relBrandBannerTwo);

        RelativeLayout relSearchHeader = (RelativeLayout) mRootView
                .findViewById(R.id.relSearchHeader);

        mRelBanner = (RelativeLayout) mRootView.findViewById(R.id.relBanner);
        mRelArrivalBanner = (RelativeLayout) mRootView
                .findViewById(R.id.relArrivalBanner);
        mRelMiddleBanner = (RelativeLayout) mRootView
                .findViewById(R.id.relMiddleBanner);
        mRelFooter = (RelativeLayout) mRootView.findViewById(R.id.relFooter);
        mBottomBar = (RelativeLayout) mRootView.findViewById(R.id.bottomBar);
        mLnrFooter = (LinearLayout) mRootView.findViewById(R.id.lnrFooter);
        LinearDisable = (LinearLayout) mRootView.findViewById(R.id.llDisable);
        mDisplayManager = new DisplayManagerScale(getActivity());
        height = mDisplayManager.getDeviceHeight();
        width = mDisplayManager.getDeviceWidth();

        height = height
                - (getStatusBarHeight() + getSupportActionBarHeight(getActivity()));
        int bannerHeight = (int) (height * 0.45);
        int arrBannerHeight = (int) (height * 0.07);

        int relBrandBannerTwoHeight = width / 3;

        int footerBannerHeight = /* (int) (height * 0.245) */width / 3;
        int bootomBarHeight = (int) (height * 0.04);
        int middleBannerHeight = (int) /* (height * 0.245) */height
                - (bannerHeight + arrBannerHeight + bootomBarHeight + footerBannerHeight);
        int relBrandBannerOneHeight = height
                - (bannerHeight + arrBannerHeight + relBrandBannerTwoHeight);

        relBrandBannerOne.getLayoutParams().height = relBrandBannerOneHeight;// 1

        relBrandBannerTwo.getLayoutParams().height = relBrandBannerTwoHeight;// 1

        mRelBanner.getLayoutParams().height = bannerHeight;// 1

        mRelMiddleBanner.getLayoutParams().height = relBrandBannerOneHeight;// 3
        mRelFooter.getLayoutParams().height = footerBannerHeight;// 4
        mBottomBar.getLayoutParams().height = bootomBarHeight;// 5

        SearchHeaderManager manager = new SearchHeaderManager(getActivity());
        manager.getSearchHeader(relSearchHeader);

        imgSearch = manager.getSearchImage();
        edtSearch = manager.getEditText();

        manager.searchAction(getActivity(), new SearchActionInterface() {

            @Override
            public void searchOnTextChange(String key) {
                // TODO Auto-generated method stub
                if (!edtSearch.getText().toString().equals("")) {

                    DashboardFActivity.dashboardFActivity
                            .goToSearchWithKey(edtSearch.getText().toString());
                    AppPrefenceManager.saveListingOption(getActivity(), "3");

                }
                edtSearch.setText("");

            }
        }, edtSearch.getText().toString());


        mRelArrivalBanner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        mRelMiddleBanner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

    }

    public static void updateApp(final Activity act) {
        final String appPackageName = act.getPackageName();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Update Available !")// act.getString(R.string.dialog_title_update_app)
                .setMessage("Please Update the app to continue")//
                // .setNegativeButton(act.getString(R.string.dialog_default_cancel),
                // null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            act.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id="
                                            + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            act.startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id="
                                            + appPackageName)));
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private class GetAppVersion extends AsyncTask<String, Void, String> {
        final CustomProgressBar pDialog = new CustomProgressBar(getActivity(),
                R.drawable.loading);
        String NewVersion;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String weather = "UNDEFINED";
            JSONObject result;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream stream = new BufferedInputStream(
                        urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                result = new JSONObject(builder.toString());
                JSONObject response = result.getJSONObject("response");
                String status = response.getString("status");
                if (status.equals("Success")) {
                    NewVersion = response.getString("appversion");

                }
                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return NewVersion;
        }

        @Override
        protected void onPostExecute(String temp) {
            pDialog.dismiss();
            currentVersion = getVersion();
            if (currentVersion.length() > 3) {
                currentVersion = currentVersion.substring(0, 3);
            }
            if (currentVersion.equals(NewVersion)) {
                LinearDisable.setVisibility(View.GONE);

                deviceRegister();
            } else {
                LinearDisable.setVisibility(View.VISIBLE);
                updateApp(getActivity());
            }
        }
    }

    private void deviceRegister() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        AppPrefenceManager.saveFCMID(getActivity(), token);
                        // Log and toast
                      /*  String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();*/
                    }
                });

        String device_id="";

        if(Build.VERSION.SDK_INT >= 27)
        {
            device_id="";
        }

        else
        {
            device_id=VKCUtils.getDeviceID(getActivity());
        }
        String name[] = {"imei_no", "gcm_id"};
        String values[] = {device_id, AppPrefenceManager.getFCMID(getActivity())};
        final VKCInternetManager manager = new VKCInternetManager(
                GCM_INITIALISATION);

        manager.getResponsePOST(getActivity(), name, values, new VKCInternetManager.ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                // TODO Auto-generated method stub
                //  Log.v("LOG", "20022015 successappinit" + successResponse);
                //  parseResponse(successResponse);
            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "20022015 Errror" + failureResponse);
            }
        });
    }
}
