/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
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
import com.mobatia.vkcsalesapp.appdialogs.SortDialog;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.adapter.ProductListNew_Adapter;

/**
 * @author archana.s
 * 
 */
@SuppressLint("NewApi")
public class SearchListFragment extends Fragment implements VKCUrlConstants,
		VKCJsonTagConstants {

	private static ListView listView;
	static ArrayList<ProductModel> productModels=new ArrayList<ProductModel>();
	static String total_records = "0";
	static String tolal_pages = "0";
	static String current_page = "0";

	// private ProductModel mProductModel;

	// private ArrayList<ProductModel> mProductList;

	private static Activity mActivity;

	private RelativeLayout mRelFilter;

	private RelativeLayout mRelSortBy;
	private RelativeLayout mRelList;
	private View view;
	private View viewList;
	private View viewFilter;
	private View viewSortBy;
	private RelativeLayout relShare;
	private static GridView gridProductList;
	static Boolean flag = false;

	private static TextView tvList;
	private static ImageView imgList;
	private ImageView imgSearch;
	private static EditText edtSearch;

	String mLabel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_productlist, null);
		mActivity = getActivity();
		productModels.clear();
		initialiseUI();

		AppPrefenceManager.saveListType(getActivity(), "SearchList");
		
		System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));

		Log.v("LOG", "9122014 " + "onCreateView");

		return view;

		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initialiseUI() {

		listView = (ListView) view.findViewById(R.id.list);
		gridProductList = (GridView) view.findViewById(R.id.gridProducts);
		mRelFilter = (RelativeLayout) view.findViewById(R.id.relFilter);
		//mRelFilter.setVisibility(View.GONE);
		mRelSortBy = (RelativeLayout) view.findViewById(R.id.relSortBy);
		viewList = (View) view.findViewById(R.id.viewList);
		mRelList = (RelativeLayout) view.findViewById(R.id.relList);
		tvList = (TextView) view.findViewById(R.id.tvList);
		imgList = (ImageView) view.findViewById(R.id.imgList);
		viewFilter = (View) view.findViewById(R.id.viewFilter);
		viewSortBy = (View) view.findViewById(R.id.viewSortBy);
		relShare = (RelativeLayout) view.findViewById(R.id.relShare);
		RelativeLayout relSearchHeader = (RelativeLayout) view
				.findViewById(R.id.relSearchHeader);
		SearchHeaderManager manager = new SearchHeaderManager(getActivity());
		manager.getSearchHeader(relSearchHeader);
		imgSearch = manager.getSearchImage();
		edtSearch = manager.getEditText();
		relShare.setVisibility(View.GONE);
		
		manager.searchAction(getActivity(), new SearchActionInterface() {

			@Override
			public void searchOnTextChange(String key) {
				
				// TODO Auto-generated method stub
				if (!edtSearch.getText().toString().equals("")) {
				/*	AppPrefenceManager.saveFilterDataBrand(mActivity, "");
					AppPrefenceManager.saveFilterDataColor(mActivity, "");
					AppPrefenceManager.saveFilterDataOffer(mActivity, "");
					AppPrefenceManager.saveFilterDataPrice(mActivity, "");
					AppPrefenceManager.saveFilterDataSize(mActivity, "");
					AppPrefenceManager.setCatId(mActivity, "");
					AppPrefenceManager.setParentCatId(mActivity, "");*/

					SearchListFragment.getSearchAPI(edtSearch.getText().toString());
					VKCUtils.hideKeyBoard(mActivity);
				}
				
				

			}
		}, edtSearch.getText().toString());

		
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
				Intent intent = new Intent(getActivity(), FilterActivity.class);
				startActivity(intent);

			}
		});

		mRelSortBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFilter.setVisibility(View.GONE);
				viewSortBy.setVisibility(View.VISIBLE);
				showDialog("Sort By");

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
				if (productModels != null) {
					setList();
				} else {

				}
			}
		});
		// getProducts();
	}

	public static void getSearchAPI(String strKey) {
		Log.v("LOG", "15122014 " + "getSearchAPI " + strKey);

		if (VKCUtils.checkInternetConnection(mActivity)) {
			productModels.clear();
			// getSearchProducts(strKey);
			getProducts(strKey,"1");
		} else {
			CustomToast toast = new CustomToast(mActivity);
			toast.show(0);
		}
		// GetProductSearch getProductSearch=new GetProductSearch(strKey);
		// getProductSearch.execute();
	}

	private void shareIntent(String link) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("text/plain");

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
		startActivity(Intent.createChooser(emailIntent, getActivity()
				.getResources().getString(R.string.app_name)));
	}

//	private static void getSearchProducts(String key) {
//
//		String name[] = { "content" };
//		String values[] = { key };
//
//		final VKCInternetManager manager = new VKCInternetManager(
//				SEARCH_PRODUCT_URL);
//
//		manager.getResponsePOST(mActivity, name, values,
//				new ResponseListener() {
//
//					@Override
//					public void responseSuccess(String successResponse) {
//						// TODO Auto-generated method stub
//						Log.v("LOG", "12122014 success" + successResponse);
//						parseResponse(successResponse);
//
//					}
//
//					@Override
//					public void responseFailure(String failureResponse) {
//						// TODO Auto-generated method stub
//						Log.v("LOG", "9122014 Errror" + failureResponse);
//					}
//				});
//	}

	private static void getProducts(String key,String pageno) {
		Log.v("LOG", "9122014 " + "getProducts" + DashboardFActivity.categoryId);
		String dataCategory = "";
		String dataColor = "";
		String dataSize = "";
		String dataType = "";
		String dataOffer = "";

		System.out.println("11122104:"
				+ AppPrefenceManager.getFilterDataColor(mActivity));
		System.out.println("05012015:listing option"
				+ AppPrefenceManager.getListingOption(mActivity));

		if (AppPrefenceManager.getListingOption(mActivity).equals("0")) {
			dataCategory = DashboardFActivity.categoryId;
			//Toast.makeText(mActivity, "if listingoption:0"+DashboardFActivity.categoryId, 1000).show();
		} else if (AppPrefenceManager.getListingOption(mActivity).equals("3")) {
			dataCategory = AppPrefenceManager.getIDsForOffer(mActivity);
			//Toast.makeText(mActivity, "if listingoption:1"+DashboardFActivity.categoryId, 1000).show();
		} else if (AppPrefenceManager.getListingOption(mActivity).equals("2")) {
			dataCategory = AppPrefenceManager.getFilterDataCategory(mActivity);
			
			//Toast.makeText(mActivity, "if listingoption:2"+DashboardFActivity.categoryId, 1000).show();
		}

		if (!AppPrefenceManager.getFilterDataSize(mActivity).equals("")) {
			dataSize = AppPrefenceManager.getFilterDataSize(mActivity);
		} else {
			dataSize = "";
		}
		if (!AppPrefenceManager.getFilterDataColor(mActivity).equals("")) {
			dataColor = AppPrefenceManager.getFilterDataSize(mActivity);
		} else {
			dataColor = "";
		}
		if (!AppPrefenceManager.getFilterDataBrand(mActivity).equals("")) {
			dataType = AppPrefenceManager.getFilterDataBrand(mActivity);
		} else {
			dataType = "";
		}
		if (!AppPrefenceManager.getFilterDataOffer(mActivity).equals("")) {
			dataOffer = AppPrefenceManager.getFilterDataOffer(mActivity);
		} else {
			dataOffer = "";
		}

		/*String name[] = { "category_id", "color_id", "size_id", "type_id",
				"content", "offer_id" };
		String values[] = { dataCategory, dataColor, dataSize, dataType, key,
				dataOffer };*/
		String name[] = { "parent_category_id","category_id", "color_id", "size_id", "type_id",
				"content", "offer_id",
				"currentpage" };
		String values[] = { "","", dataColor, dataSize, dataType, key,
				dataOffer,  String.valueOf(pageno) };
		
		// String values[]={dataCategory,"","",""};

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_DETAIL_NEW_URL);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "12122014 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "9122014 Errror" + failureResponse);
					}
				});
	}

	/*@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		productModels = null;

	}*/

	/*private static void parseResponse(String response) {

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

		}

		setList();
		// listAdapter = new ProductListAdapter(mActivity, productModels,1);
		// listView.setAdapter(listAdapter);

	}
*/
	private static void parseResponse(String response) {

		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
		 total_records=jsonObjectresponse.getString("total_records");
			 tolal_pages=jsonObjectresponse.getString("tolal_pages");
			 current_page=jsonObjectresponse.getString("current_page");
			Log.d("TAG", "Response of Get Products---"+total_records);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
			///productModels=new ArrayList<ProductNewModel>();
			//productModels.clear();
			ArrayList<ProductModel> tempProductArrayList = new ArrayList<ProductModel>();

			for (int j = 0; j < jsonArrayresponse.length(); j++) {
				JSONObject obj = jsonArrayresponse.getJSONObject(j);
				ProductModel model = new ProductModel();
				model.setmProductName(obj.getString("name"));
				model.setId(obj.getString("id"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				try {  
				    Date date = format.parse(obj.getString("timestamp"));  
				    System.out.println("converted date=-"+date);  
					model.setTimeStampP(date);

				} catch (ParseException e) {  
				    // TODO Auto-generated catch block  
				    e.printStackTrace();  
				}
				model.setProductDescription(obj.getString("description"));
				model.setParent_category(obj.getString("parent_category"));
				model.setCategoryId(obj.getString("category_id"));
				model.setType(obj.getString("type"));
				model.setSize(obj.getString("size"));
				model.setProductViews(obj.getString("views"));
				model.setmProductPrize(obj.getString("cost"));
				model.setmProductOff(obj.getString("offer"));
				model.setImage_name(obj.getString("image_name"));
				model.setProductquantity(obj.getString("orderqty"));
				productModels.add(model);
			}
			
			//productModels.addAll(tempProductArrayList);


		} catch (Exception e) {
			e.printStackTrace();
		}

		setList();

	}

	public static void setList() {

		System.out.println("30122014 Click function:flag::" + flag);
		if(productModels.size()==0){
			VKCUtils.showtoast(mActivity, 17);

		}else{
		if (flag == true) {

			gridProductList.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			tvList.setText("GRID");
			imgList.setImageResource(R.drawable.grid);
			
			final int currentPage = Integer.parseInt(current_page);
			final int totalPage = Integer.parseInt(tolal_pages);
			listAdapter = new ProductListNew_Adapter(mActivity,
					productModels, 1, currentPage, totalPage,
					new ProductListNew_Adapter.ScrollingAdapterinterface() {

						@Override
						public void calledInterface(int position) {
							// TODO Auto-generated method stub
							if (currentPage == totalPage) {

							} else {
								getProducts(edtSearch.getText().toString(),String.valueOf(position + 1));
							}
							// pos=2*position-1;

						}
					});
			listAdapter.notifyDataSetChanged();
			//listView.invalidateViews();
			listView.setAdapter(listAdapter);

			if (currentPage == 1) {
				listView.setSelection(0);

			} else {
				listView.setSelection((currentPage - 1) * 20);
			}

		} else {
			gridProductList.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			tvList.setText("LIST");
			imgList.setImageResource(R.drawable.list);
			/*listAdapter = new ProductListAdapter_New(mActivity, productModels, 2,"0");*/
			final int currentPage = Integer.parseInt(current_page);
			final int totalPage = Integer.parseInt(tolal_pages);
			listAdapter = new ProductListNew_Adapter(mActivity,
					productModels, 2, currentPage, totalPage,
					new ProductListNew_Adapter.ScrollingAdapterinterface() {

						@Override
						public void calledInterface(int position) {
							// TODO Auto-generated method stub
							if (currentPage == totalPage) {

							} else {
								getProducts(edtSearch.getText().toString(),String.valueOf(position + 1));
							}
							// pos=2*position-1;

						}
					});
			listAdapter.notifyDataSetChanged();
			//gridProductList.invalidateViews();

			gridProductList.setAdapter(listAdapter);

			if (currentPage == 1) {
				gridProductList.setSelection(0);

			} else {
				gridProductList.setSelection((currentPage - 1) * 20);
			}
		}
		}
	}

	static ProductListNew_Adapter listAdapter;
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
						if (listAdapter != null) {
							if (option.equals("Popularity")) {
								listAdapter.doSort(0);
							} else if (option.equals("Price(Low to High)")) {
								listAdapter.doSort(1);
							} else if (option.equals("Price(High to Low)")) {
								listAdapter.doSort(2);
							} else if (option.equals("New Arrivals")) {
								listAdapter.doSort(3);
							} else if (option.equals("Discount")) {
								listAdapter.doSort(4);
							}
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
	productModels.clear();
		/*if((AppController.flagForFilterApply.equals("1"))){
			productModels.clear();
		}*/
		if (!DashboardFActivity.dashboardFActivity.key.equals("")) {
			edtSearch.setText(DashboardFActivity.dashboardFActivity.key);
			SearchListFragment.getSearchAPI(edtSearch.getText().toString());
			
			DashboardFActivity.dashboardFActivity.key="";
			
		}else{
			/*AppPrefenceManager.saveFilterDataBrand(mActivity, "");
			AppPrefenceManager.saveFilterDataColor(mActivity, "");
			AppPrefenceManager.saveFilterDataOffer(mActivity, "");
			AppPrefenceManager.saveFilterDataPrice(mActivity, "");
			AppPrefenceManager.saveFilterDataSize(mActivity, "");*/
			SearchListFragment.getSearchAPI(edtSearch.getText().toString());
			edtSearch.setText("");

		}

	}

}