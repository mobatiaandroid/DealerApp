/**
 *
 */
package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.FilterAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterBrandContentAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterCategoryContentAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterCategoryMainContentAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterColorContentAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterOfferAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterPriceContentAdapter;
import com.mobatia.vkcsalesapp.adapter.FilterSizeContentAdapter;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CategoryModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.OfferModel;
import com.mobatia.vkcsalesapp.model.PriceModel;
import com.mobatia.vkcsalesapp.model.SizeModel;
import com.mobatia.vkcsalesapp.model.SortCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FilterActivity extends AppCompatActivity implements VKCJsonTagConstants {//BaseActivity

    private ListView mListFilter;
    private ListView mListFilterContent;
    private String[] mTextString = {"Category", "SubCategory", "Size",
            "Brand", "Price", "Color", "Offers"};
    private Activity mActivity;
    private ArrayList<CategoryModel> tempArrayListCategory;
    private ArrayList<CategoryModel> tempArrayListMainCategory;
    private ArrayList<SizeModel> tempArrayListSize;
    private ArrayList<BrandTypeModel> tempArrayListBrand;
    private ArrayList<PriceModel> tempArrayListPrice;
    private ArrayList<ColorModel> tempArrayListColor;
    private RelativeLayout relApply;
    private RelativeLayout relClear;
    ArrayList<BrandTypeModel> typeArrayList;
    ArrayList<SizeModel> sizeArrayList;
    ArrayList<ColorModel> colorArrayList;
    ArrayList<PriceModel> priceArrayList;
    ArrayList<CategoryModel> categoryArrayList;
    ArrayList<OfferModel> offerModels;
    ArrayList<OfferModel> tempofferModels;
    boolean clearFlag = false;
    public List<String> content[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mActivity = this;
        AppController.tempmainCatArrayList.clear();
        AppController.tempsubCatArrayList.clear();
        AppController.tempsizeCatArrayList.clear();
        AppController.tempbrandCatArrayList.clear();
        AppController.temppriceCatArrayList.clear();
        AppController.tempcolorCatArrayList.clear();
        AppController.tempofferCatArrayList.clear();
        initialiseUI();
        setArrayLists();
        setCategory();
        setActionBar();
    }


    public void setArrayLists() {
        typeArrayList = new ArrayList<BrandTypeModel>();
        categoryArrayList = new ArrayList<CategoryModel>();
        sizeArrayList = new ArrayList<SizeModel>();
        tempArrayListSize = new ArrayList<SizeModel>();
        colorArrayList = new ArrayList<ColorModel>();
        priceArrayList = new ArrayList<PriceModel>();
        tempArrayListBrand = new ArrayList<BrandTypeModel>();
        tempArrayListPrice = new ArrayList<PriceModel>();
        tempArrayListColor = new ArrayList<ColorModel>();
        tempArrayListCategory = new ArrayList<CategoryModel>();
        tempArrayListMainCategory = new ArrayList<CategoryModel>();
        offerModels = new ArrayList<OfferModel>();
        tempofferModels = new ArrayList<OfferModel>();

        try {

            JSONArray offerObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonOfferResponse(FilterActivity.this));
            for (int i = 0; i < offerObjArray.length(); i++) {
                JSONObject responseObj = offerObjArray.getJSONObject(i);

                offerModels.add(getOffersObjectValues(responseObj));

            }

            JSONArray typeObjArray = new JSONArray(
                    AppPrefenceManager.getJsonTypeResponse(FilterActivity.this));
            for (int i = 0; i < typeObjArray.length(); i++) {
                JSONObject responseObj = typeObjArray.getJSONObject(i);

                typeArrayList.add(getTypeObjectValues(responseObj));

            }

            JSONArray sizeObjArray = new JSONArray(
                    AppPrefenceManager.getJsonSizeResponse(FilterActivity.this));
            for (int i = 0; i < sizeObjArray.length(); i++) {
                JSONObject responseObj = sizeObjArray.getJSONObject(i);

                sizeArrayList.add(getSizeObjectValues(responseObj));

            }

            JSONArray colorObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonColorResponse(FilterActivity.this));
            for (int i = 0; i < colorObjArray.length(); i++) {
                JSONObject responseObj = colorObjArray.getJSONObject(i);
                colorArrayList.add(getColorObjectValues(responseObj));
            }

            JSONArray priceObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonPriceResponse(FilterActivity.this));
            for (int i = 0; i < priceObjArray.length(); i++) {
                JSONObject responseObj = priceObjArray.getJSONObject(i);

                priceArrayList.add(getPriceObjectValues(responseObj));

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public OfferModel getOffersObjectValues(JSONObject object)
            throws JSONException {

        OfferModel offerModel = new OfferModel();
        offerModel.setId(object.getString(JSON_SETTINGS_OFFERID));
        offerModel.setName(object.getString(JSON_SETTINGS_OFFER));
        offerModel.setOfferBanner(object.getString(JSON_SETTINGS_OFFERIMAGE));

        return offerModel;

    }

    public BrandTypeModel getTypeObjectValues(JSONObject object)
            throws JSONException {

        BrandTypeModel brandModel = new BrandTypeModel();
        brandModel.setId(object.getString(JSON_SETTINGS_BRANDID));
        brandModel.setName(object.getString(JSON_SETTINGS_BRANDNAME));
        return brandModel;

    }

    public CategoryModel getCategoryObjectValues(JSONObject object)
            throws JSONException {

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(object.getString(JSON_SETTINGS_CATEGORYID));
        categoryModel.setName(object.getString(JSON_SETTINGS_CATEGORYNAME));
        categoryModel.setParentId(object.getString(JSON_SETTINGS_PRODUCTID));
        return categoryModel;

    }

    public SizeModel getSizeObjectValues(JSONObject object)
            throws JSONException {

        SizeModel sizeModel = new SizeModel();
        sizeModel.setId(object.getString(JSON_SETTINGS_SIZEID));
        sizeModel.setName(object.getString(JSON_SETTINGS_SIZENAME));
        return sizeModel;

    }

    public ColorModel getColorObjectValues(JSONObject object)
            throws JSONException {

        ColorModel colorModel = new ColorModel();
        colorModel.setId(object.getString(JSON_SETTINGS_COLORID));
        colorModel.setColorcode(object.getString(JSON_SETTINGS_COLORCODE));
        colorModel.setColorName(object.getString("color_name"));
        return colorModel;

    }

    public PriceModel getPriceObjectValues(JSONObject object)
            throws JSONException {

        PriceModel priceModel = new PriceModel();
        priceModel.setId(object.getString(JSON_SETTINGS_PRICEID));
        priceModel.setFrom_range(object.getString(JSON_SETTINGS_PRICEFROM));
        priceModel.setTo_range(object.getString(JSON_SETTINGS_PRICETO));
        return priceModel;

    }

    private void initialiseUI() {
        mListFilter = (ListView) findViewById(R.id.listFilter);
        relApply = (RelativeLayout) findViewById(R.id.relApply);
        relClear = (RelativeLayout) findViewById(R.id.relClear);

        relApply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getSelectedOptions();
                AppPrefenceManager.saveListingOption(mActivity, "2");
                finish();


            }
        });

        relClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                tempArrayListBrand.clear();
                tempArrayListCategory.clear();
                tempArrayListColor.clear();
                tempArrayListMainCategory.clear();
                tempArrayListPrice.clear();
                tempArrayListSize.clear();
                tempofferModels.clear();
                AppController.tempmainCatArrayList.clear();
                AppController.tempsubCatArrayList.clear();
                AppController.tempsizeCatArrayList.clear();
                AppController.tempbrandCatArrayList.clear();
                AppController.temppriceCatArrayList.clear();
                AppController.tempcolorCatArrayList.clear();
                AppController.tempofferCatArrayList.clear();
                AppPrefenceManager.saveFilterDataCategory(mActivity, "");
                AppPrefenceManager.saveFilterDataSubCategory(mActivity, "");
                AppPrefenceManager.saveFilterDataSize(mActivity, "");
                AppPrefenceManager.saveFilterDataBrand(mActivity, "");
                AppPrefenceManager.saveFilterDataColor(mActivity, "");
                AppPrefenceManager.saveFilterDataPrice(mActivity, "");
                AppPrefenceManager.saveFilterDataOffer(mActivity, "");
                clearFlag = true;
                setSubCategory();
                clearFlag = false;
                setSize();
                setBrand();
                setPrice();
                setColor();
                setOffers();
                setCategory();

            }
        });

        mListFilterContent = (ListView) findViewById(R.id.listFilterContent);
        mListFilter.setAdapter(new FilterAdapter(mActivity, mTextString));
        mListFilter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                if (position == 0) {

                    setCategory();

                } else if (position == 1) {

                    setSubCategory();

                } else if (position == 2) {
                    setSize();

                } else if (position == 3) {
                    setBrand();

                } else if (position == 4) {
                    setPrice();

                } else if (position == 5) {
                    setColor();

                } else if (position == 6) {

                    setOffers();

                }

            }

        });

        mListFilterContent.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selectedFromList = (String) (mListFilterContent
                        .getItemAtPosition(position));

            }
        });
    }


    public void getSelectedOptions() {
        String responseCategory = "";
        String responseSubCategory = "";
        String responseSize = "";
        String responseBrand = "";
        String responsePrice = "";
        String responseColor = "";
        String responseOffer = "";

        if (AppController.tempmainCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempmainCatArrayList.size(); i++) {
                responseCategory = responseCategory
                        + AppController.tempmainCatArrayList.get(i) + ",";
            }
            if (responseCategory.endsWith(",")) {

                responseCategory = responseCategory.substring(0,
                        responseCategory.length() - 1);
            }
            AppPrefenceManager.saveFilterDataCategory(mActivity,
                    responseCategory);

        }

        if (AppController.tempsubCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempsubCatArrayList.size(); i++) {
                responseSubCategory = responseSubCategory
                        + AppController.tempsubCatArrayList.get(i) + ",";
            }
            if (responseSubCategory.endsWith(",")) {

                responseSubCategory = responseSubCategory.substring(0,
                        responseSubCategory.length() - 1);
            }
            AppPrefenceManager.saveFilterDataSubCategory(mActivity,
                    responseSubCategory);
        }

        if (AppController.tempsizeCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempsizeCatArrayList.size(); i++) {
                responseSize = responseSize
                        + AppController.tempsizeCatArrayList.get(i) + ",";
            }
            if (responseSize.endsWith(",")) {
                responseSize = responseSize.substring(0,
                        responseSize.length() - 1);
            }
            AppPrefenceManager.saveFilterDataSize(mActivity, responseSize);

        }
        if (AppController.tempofferCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempofferCatArrayList.size(); i++) {
                responseOffer = responseOffer
                        + AppController.tempofferCatArrayList.get(i) + ",";
            }
            if (responseOffer.endsWith(",")) {

                responseOffer = responseOffer.substring(0,
                        responseOffer.length() - 1);
            }
            AppPrefenceManager.saveFilterDataOffer(mActivity, responseOffer);
        }

        if (AppController.tempbrandCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempbrandCatArrayList.size(); i++) {
                responseBrand = responseBrand
                        + AppController.tempbrandCatArrayList.get(i) + ",";
            }
            if (responseBrand.endsWith(",")) {

                responseBrand = responseBrand.substring(0,
                        responseBrand.length() - 1);
            }
            AppPrefenceManager.saveFilterDataBrand(mActivity, responseBrand);
        }

        if (AppController.temppriceCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.temppriceCatArrayList.size(); i++) {
                responsePrice = responsePrice
                        + AppController.temppriceCatArrayList.get(i) + ",";
            }
            if (responsePrice.endsWith(",")) {

                responsePrice = responsePrice.substring(0,
                        responsePrice.length() - 1);
            }
            AppPrefenceManager.saveFilterDataPrice(mActivity, responsePrice);
        }
        if (AppController.tempcolorCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempcolorCatArrayList.size(); i++) {
                responseColor = responseColor
                        + AppController.tempcolorCatArrayList.get(i) + ",";
            }
            if (responseColor.endsWith(",")) {

                responseColor = responseColor.substring(0,
                        responseColor.length() - 1);
            }
            AppPrefenceManager.saveFilterDataColor(mActivity, responseColor);
        }
        if (responseCategory.equals("1") || (responseCategory.equals("2"))
                || (responseCategory.equals("3"))
                || (responseCategory.equals("4"))
                || (responseCategory.equals("5"))
                || (responseCategory.equals("31"))
                || (responseCategory.equals("33"))) {
            System.out.println("alambana cat id---1");
            AppPrefenceManager.setCatId(mActivity, responseSubCategory);
            AppPrefenceManager.setParentCatId(mActivity, responseCategory);
        } else {
            AppPrefenceManager.setCatId(mActivity, responseSubCategory);
            AppPrefenceManager.setParentCatId(mActivity, responseCategory);
        }


    }


    private void setCategory() {
        // TODO Auto-generated method stub

        String respMainCategory = AppPrefenceManager
                .getMainCategory(FilterActivity.this);
        ArrayList<CategoryModel> mainCategoryArrayList = new ArrayList<CategoryModel>();
        JSONArray categoryObjArray = null;
        try {
            categoryObjArray = new JSONArray(respMainCategory);

            for (int i = 0; i < categoryObjArray.length(); i++) {
                JSONObject responseObj = categoryObjArray.getJSONObject(i);
                CategoryModel model = getCategoryObjectValues(responseObj);
                if (model.getParentId().equalsIgnoreCase("0")) {
                    mainCategoryArrayList.add(model);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        FilterCategoryMainContentAdapter adapter = new FilterCategoryMainContentAdapter(
                mActivity,
                /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */mainCategoryArrayList, tempArrayListMainCategory);
        mListFilterContent.setAdapter(adapter);
    }

    private void setSubCategory() {
        SortCategory sortCategory[] = VKCSplashActivity.sortCategoryGlobal
                .getSortCategorys();

        ArrayList<CategoryModel> filterArrayList = new ArrayList<CategoryModel>();
        filterArrayList.clear();
        if (tempArrayListMainCategory.size() == 0 && !clearFlag) {

            VKCUtils.showtoast(mActivity, 18);

        }
        for (int i = 0; i < sortCategory.length; i++) {
            for (CategoryModel categoryModel : sortCategory[i]
                    .getCategoryModels()) {
                for (CategoryModel categoryModelMain : tempArrayListMainCategory) {
                    if (categoryModel.getParentId().equals(
                            categoryModelMain.getId()))
                        filterArrayList.add(categoryModel);
                }
            }
        }

        FilterCategoryContentAdapter adapter = new FilterCategoryContentAdapter(
                mActivity,
                /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */filterArrayList, tempArrayListCategory);
        mListFilterContent.setAdapter(adapter);
    }

    private void setSize() {

        FilterSizeContentAdapter adapter = new FilterSizeContentAdapter(
                mActivity, sizeArrayList, tempArrayListSize);
        mListFilterContent.setAdapter(adapter);
    }

    private void setBrand() {

        FilterBrandContentAdapter adapter = new FilterBrandContentAdapter(
                mActivity, typeArrayList, tempArrayListBrand);
        mListFilterContent.setAdapter(adapter);
    }

    private void setPrice() {

        FilterPriceContentAdapter adapter = new FilterPriceContentAdapter(
                mActivity, priceArrayList, tempArrayListPrice);
        mListFilterContent.setAdapter(adapter);
    }

    private void setColor() {

        FilterColorContentAdapter adapter = new FilterColorContentAdapter(
                mActivity, colorArrayList, tempArrayListColor);
        mListFilterContent.setAdapter(adapter);
    }

    private void setOffers() {

        FilterOfferAdapter adapter = new FilterOfferAdapter(mActivity,
                offerModels, tempofferModels);
        mListFilterContent.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

}
