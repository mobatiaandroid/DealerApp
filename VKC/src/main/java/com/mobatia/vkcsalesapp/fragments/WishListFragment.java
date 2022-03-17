/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.activities.FilterActivity;
import com.mobatia.vkcsalesapp.adapter.ProductListAdapter;
import com.mobatia.vkcsalesapp.appdialogs.SortDialog;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author mobatia-user
 * 
 */

// Currently not used....WishListFragment
public class WishListFragment extends Fragment implements VKCUrlConstants,
		VKCJsonTagConstants {

	private ListView listView;
	private GridView gridProductList;
	ArrayList<ProductModel> productModels;

	// private ProductModel mProductModel;

	// private ArrayList<ProductModel> mProductList;

	private Activity mActivity;

	private RelativeLayout mRelFilter;

	private RelativeLayout mRelSortBy;
	private RelativeLayout mRelList;
	private View view;
	private View viewFilter;
	private View viewSortBy;
	private View viewList;
	private RelativeLayout relShare;
	private ImageView imgSearch;
	private EditText edtSearch;
	Boolean flag = true;

	private TextView tvList;
	private ImageView imgList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_wishlist, null);
		mActivity = getActivity();
		initialiseUI();

		// System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));
		AppPrefenceManager.saveListType(getActivity(), "ProductList");
		Log.v("LOG", "22122014 onCreateView in ProductListFragment ");
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
		return view;

		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initialiseUI() {

		listView = (ListView) view.findViewById(R.id.list);
		gridProductList = (GridView) view.findViewById(R.id.gridProducts);
		mRelFilter = (RelativeLayout) view.findViewById(R.id.relFilter);
		mRelSortBy = (RelativeLayout) view.findViewById(R.id.relSortBy);
		mRelList = (RelativeLayout) view.findViewById(R.id.relList);
		viewFilter = (View) view.findViewById(R.id.viewFilter);
		viewSortBy = (View) view.findViewById(R.id.viewSortBy);
		viewList = (View) view.findViewById(R.id.viewList);
		tvList = (TextView) view.findViewById(R.id.tvList);
		imgList = (ImageView) view.findViewById(R.id.imgList);
		relShare = (RelativeLayout) view.findViewById(R.id.relShare);

		RelativeLayout relSearchHeader = (RelativeLayout) view
				.findViewById(R.id.relSearchHeader);
		SearchHeaderManager manager = new SearchHeaderManager(getActivity());
		manager.getSearchHeader(relSearchHeader);
		imgSearch = manager.getSearchImage();
		edtSearch = manager.getEditText();

		manager.searchAction(getActivity(), new SearchActionInterface() {

			@Override
			public void searchOnTextChange(String key) {
				// TODO Auto-generated method stub
				if (!edtSearch.getText().toString().equals("")) {

					ProductListFragment.setFilter(edtSearch.getText()
							.toString());
					VKCUtils.hideKeyBoard(mActivity);
				}

			}
		}, edtSearch.getText().toString());
		// imgSearch.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// ProductListFragment.setFilter(edtSearch.getText().toString());
		//
		// }
		// });
		relShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareIntent("http://vkcgroup.com/");

			}
		});
		mRelFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFilter.setVisibility(View.VISIBLE);
				viewSortBy.setVisibility(View.GONE);
				viewList.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(), FilterActivity.class);
				startActivity(intent);

			}
		});

		mRelList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewFilter.setVisibility(View.GONE);
				viewSortBy.setVisibility(View.GONE);
				viewList.setVisibility(View.VISIBLE);
				if (tvList.getText().equals("LIST")) {
					flag = true;
				} else {
					flag = false;
				}
				setList();

			}
		});

		mRelSortBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFilter.setVisibility(View.GONE);
				viewSortBy.setVisibility(View.VISIBLE);
				viewList.setVisibility(View.GONE);
				showDialog("Sort By");

			}
		});

	}

	public static void setFilter(String key) {
		// Toast.makeText(DashboardFActivity.dashboardFActivity, key,
		// 1000).show();
		listAdapter.filter(key);
	}

	private void shareIntent(String link) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("text/plain");

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
		startActivity(Intent.createChooser(emailIntent, getActivity()
				.getResources().getString(R.string.app_name)));
	}

	private void getProducts() {
		Log.v("LOG", "9122014 " + "getProducts" + DashboardFActivity.categoryId);
		String dataCategory = "";
		String dataColor = "";
		String dataSize = "";
		String dataType = "";
		String dataOffer = "";

		System.out.println("11122104:"
				+ AppPrefenceManager.getFilterDataColor(getActivity()));

		if (AppPrefenceManager.getListingOption(getActivity()).equals("0")) {
			dataCategory = DashboardFActivity.categoryId;
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"1")) {
			dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"2")) {
			dataCategory = AppPrefenceManager
					.getFilterDataCategory(getActivity());
			if (dataCategory.length() == 0) {
				// CustomToast toast = new CustomToast(mActivity);
				// toast.show(19);
				dataCategory = DashboardFActivity.categoryId;
			}
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"4")) {
			dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
			// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
			// .show();
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"5")) {
			dataCategory = AppPrefenceManager.getSubCategoryId(getActivity());
			// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
			// .show();
		}

		if (!AppPrefenceManager.getFilterDataSize(getActivity()).equals("")) {
			dataSize = AppPrefenceManager.getFilterDataSize(getActivity());
		} else {
			dataSize = "";
		}
		if (!AppPrefenceManager.getFilterDataColor(getActivity()).equals("")) {
			dataColor = AppPrefenceManager.getFilterDataSize(getActivity());
		} else {
			dataColor = "";
		}
		if (AppPrefenceManager.getListingOption(getActivity()).equals("4")) {
			dataType = AppPrefenceManager.getBrandIdForSearch(getActivity());
			// Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
			// .show();

		} else {
			if (!AppPrefenceManager.getFilterDataBrand(getActivity())
					.equals("")) {
				dataType = AppPrefenceManager.getFilterDataBrand(getActivity());
			} else {
				dataType = "";
			}

			// Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
			// .show();
		}
		if (!AppPrefenceManager.getFilterDataOffer(getActivity()).equals("")) {
			dataOffer = AppPrefenceManager.getFilterDataOffer(getActivity());
		} else {
			dataOffer = "";
		}

		String name[] = { "category_id", "color_id", "size_id", "type_id",
				"content", "offer_id" };
		String values[] = { dataCategory, dataColor, dataSize, dataType, "",
				dataOffer };
		System.out.println("11122014:values:Datacategory:" + dataCategory
				+ ",dataColor:" + dataColor + ",dataSize:" + dataSize
				+ ",dataType:" + dataType + ",dataOffer:" + dataOffer);
		// String values[]={dataCategory,"","",""};

		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_DETAIL_URL);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "12012015 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "08012015 Errror" + failureResponse);
					}
				});
	}

	private void parseResponse(String response) {

		productModels = new ArrayList<ProductModel>();
		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);

			for (int j = 0; j < jsonArrayresponse.length(); j++) {

				JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);
				ProductModel productModel = new ProductModel();
				// categoryname
				// productcost
				// productid
				// productname
				// productquantity
				// productoffer
				productModel.setmSapId(jsonObjectZero
						.optString("productSapId"));
				productModel.setCategoryId(jsonObjectZero
						.optString(JSON_CATEGORY_ID));
				productModel.setCategoryName(jsonObjectZero
						.optString(JSON_CATEGORY_NAME));
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
						.optString(JSON_PRODUCT_TIMESTAMP));

				productModel.setmProductOff(jsonObjectZero
						.optString(JSON_PRODUCT_OFFER));

				// product_color
				// product_image
				// product_size
				// product_type
				JSONArray productColorArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_COLOR);
				JSONArray productImageArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_IMAGE);
				JSONArray productSizeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_SIZE);
				JSONArray productTypeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_TYPE);

				ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
				for (int i = 0; i < productColorArray.length(); i++) {

					ColorModel colorModel = new ColorModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					colorModel.setId(jsonObject
							.optString(JSON_SETTINGS_COLORID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));

					colorModels.add(colorModel);

				}
				productModel.setProductColor(colorModels);

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
					sizeModel.setName(jsonObject
							.optString(JSON_SETTINGS_SIZENAME));

					sizeModels.add(sizeModel);

				}
				productModel.setmProductSize(sizeModels);
				// /////
				ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
				for (int i = 0; i < productTypeArray.length(); i++) {

					BrandTypeModel typeModel = new BrandTypeModel();
					JSONObject jsonObject = productTypeArray.getJSONObject(i);
					typeModel
							.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
					typeModel.setName(jsonObject
							.optString(JSON_SETTINGS_BRANDNAME));

					brandTypeModels.add(typeModel);

				}
				productModel.setProductType(brandTypeModels);
				// /////////
				Log.v("LOG", "9122014 " + "productModels.add(productModel)");
				productModels.add(productModel);

				// if (j % 2 == 1) {
				// productModels.add(model);
				// }
				// model = productModel;
				// if (j % 2 == 0) {
				// productModels.add(model);
				// }
				// model = productModel;
				// productModels.add(productModel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		setList();
		/*
		 * type=1;list type=2;grid
		 */
		// if(flag==true){
		//
		// gridProductList.setVisibility(View.GONE);
		// listView.setVisibility(View.VISIBLE);
		// tvList.setText("GRID");
		// System.out.println("23122014:Flag::"+flag);
		// listAdapter = new ProductListAdapter(getActivity(), productModels,1);
		// /*type=1;list
		// type=2;grid*/
		// listView.setAdapter(listAdapter);
		//
		// }else{
		// gridProductList.setVisibility(View.VISIBLE);
		// listView.setVisibility(View.GONE);
		// tvList.setText("LIST");
		// System.out.println("23122014:Flag::"+flag);
		// listAdapter = new ProductListAdapter(getActivity(), productModels,2);
		//
		// gridProductList.setAdapter(listAdapter);
		// }

	}

	public void setList() {
		if (productModels.size() == 0) {
			// CustomToast toast = new CustomToast(mActivity);
			// toast.show(17);
			VKCUtils.showtoast(mActivity, 17);
		}
		if (flag == true) {

			gridProductList.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			tvList.setText("GRID");
			imgList.setImageResource(R.drawable.grid);
			System.out.println("23122014:Flag::" + flag);
			listAdapter = new ProductListAdapter(getActivity(), productModels,
					1);
			/*
			 * type=1;list type=2;grid
			 */
			listView.setAdapter(listAdapter);

		} else {
			gridProductList.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			tvList.setText("LIST");
			imgList.setImageResource(R.drawable.list);
			System.out.println("23122014:Flag::" + flag);
			listAdapter = new ProductListAdapter(getActivity(), productModels,
					2);

			gridProductList.setAdapter(listAdapter);
		}
		if (listAdapter != null) {
			String option = AppPrefenceManager
					.getProductListSortOption(mActivity);
			if (option.equals("0")) {
				listAdapter.doSort(0);
			} else if (option.equals("1")) {
				listAdapter.doSort(1);
			} else if (option.equals("2")) {
				listAdapter.doSort(2);
			} else if (option.equals("3")) {
				listAdapter.doSort(3);
			} else if (option.equals("4")) {
				listAdapter.doSort(4);
			}
		}

	}

	static ProductListAdapter listAdapter;
	ProductModel model = null;
	SortDialog sortDialog;

	private void showDialog(String str) {
		sortDialog = new SortDialog(getActivity(), str,
				new SortDialog.SortOptionSelectionListener() {

					@Override
					public void selectedOption(String option) {
						// TODO Auto-generated method stub
						// Toast.makeText(getActivity(),option,
						// Toast.LENGTH_SHORT).show();
						if (option.equals("Popularity")) {
							listAdapter.doSort(0);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "0");
						} else if (option.equals("Price(Low to High)")) {
							listAdapter.doSort(1);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "1");
						} else if (option.equals("Price(High to Low)")) {
							listAdapter.doSort(2);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "2");
						} else if (option.equals("New Arrivals")) {
							listAdapter.doSort(3);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "3");
						} else if (option.equals("Discount")) {
							listAdapter.doSort(4);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "4");
						}

					}
				});
		sortDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		sortDialog.setCancelable(true);
		sortDialog.show();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// System.out.println("11122104:"+AppPrefenceManager.getFilterDataCategory(mActivity));
		super.onResume();
		edtSearch.setText("");

		if (VKCUtils.checkInternetConnection(getActivity())) {
			getProducts();
		} else {
			// CustomToast toast = new CustomToast(getActivity());
			// toast.show(0);
			VKCUtils.showtoast(getActivity(), 0);
		}
	}

}
