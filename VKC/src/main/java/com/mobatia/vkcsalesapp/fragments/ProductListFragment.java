/**
 * 
 */
package com.mobatia.vkcsalesapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

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
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

/**
 * @author archana.s
 * 
 */
@SuppressLint("NewApi")
public class ProductListFragment extends Fragment implements VKCUrlConstants,
		VKCJsonTagConstants
// , OnScrollListener

{

	private ListView listView;
	private GridView gridProductList;
	ArrayList<ProductModel> productModels;

	// private ProductModel mProductModel;

	// private ArrayList<ProductModel> mProductList;
	private int mPageNumber;
	private Activity mActivity;
	private View view;
	private RelativeLayout mRelFilter;

	private RelativeLayout mRelSortBy;
	private RelativeLayout mRelList;

	private View viewFilter;
	private View viewSortBy;
	private View viewList;
	private RelativeLayout relShare;
	private ImageView imgSearch;
	private EditText edtSearch;
	Boolean flag = false;
	private TextView tvList;
	private ImageView imgList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_productlist, null);
		mActivity = getActivity();
		initialiseUI();

		// System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));
		AppPrefenceManager.saveListType(getActivity(), "ProductList");

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
		AppController.isCart = false;
		// listView.setOnScrollListener(this);
		/*
		 * listView.setOnScrollListener(new OnScrollListener() {
		 * 
		 * @Override public void onScrollStateChanged(AbsListView view, int
		 * scrollState) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onScroll(AbsListView view, int
		 * firstVisibleItem, int visibleItemCount, int totalItemCount) { // TODO
		 * Auto-generated method stub System.out.println("Scroll detected");
		 * if(firstVisibleItem + visibleItemCount >= totalItemCount){
		 * getProducts(mPageNumber+1); } } });
		 */
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
		mPageNumber = 0;

		/*
		 * if (VKCUtils.checkInternetConnection(getActivity())) {
		 * //getProducts(mPageNumber); getProducts(); } else { CustomToast toast
		 * = new CustomToast(getActivity()); // toast.show(0);
		 * VKCUtils.showtoast(mActivity, 0); }
		 */
		// imgSearch.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// ProductListFragment.setFilter(edtSearch.getText().toString());
		//
		// }
		// });
		/*
		 * gridProductList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * pos, long arg3) { // TODO Auto-generated method stub //
		 * AppController.selectedProductPosition=pos; } });
		 */
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

	private void getProducts() // int pageNumber
	{

		// DashboardFActivity.categoryId);
		String dataCategory = "";
		String dataColor = "";
		String dataSize = "";
		String dataType = "";
		String dataOffer = "";
		String custId = "";
		String custType = "";
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
		} else {
			if (!AppPrefenceManager.getFilterDataBrand(getActivity())
					.equals("")) {
				dataType = AppPrefenceManager.getFilterDataBrand(getActivity());
			} else {
				dataType = "";
			}

		}
		if (!AppPrefenceManager.getFilterDataOffer(getActivity()).equals("")) {
			dataOffer = AppPrefenceManager.getFilterDataOffer(getActivity());
		} else {
			dataOffer = "";
		}
		if (AppPrefenceManager.getUserType(getActivity()).equals("7")) {
			custType = "2";
			custId = AppPrefenceManager.getSelectedUserId(getActivity());
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("6")) {
			custType = "1";
			custId = AppPrefenceManager.getCustomerId(getActivity());
		} else if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			custType = AppPrefenceManager.getCustomerCategory(getActivity());
			custId = AppPrefenceManager.getSelectedUserId(getActivity());
		}
		
		if(AppPrefenceManager.getListingOption(mActivity).equals("1")){
			dataOffer=AppPrefenceManager.getOfferIDs(mActivity);
		}
		String name[] = { "category_id", "color_id", "size_id", "type_id",
				"content", "offer_id", "customerId", "customerType" };// ,"currentpage"
		String values[] = { dataCategory, dataColor, dataSize, dataType, "",
				dataOffer, custId, custType };// , String.valueOf(pageNumber)
		System.out.println("11122014:values:Datacategory:" + dataCategory
				+ ",dataColor:" + dataColor + ",dataSize:" + dataSize
				+ ",dataType:" + dataType + ",dataOffer:" + dataOffer);
		

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_DETAIL_URL);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						// Log.v("LOG", "12012015 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						// Log.v("LOG", "08012015 Errror" + failureResponse);
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
				productModel.setProductdescription(jsonObjectZero
						.optString("productdescription"));
				productModel
						.setmSapId(jsonObjectZero.optString("productSapId"));
				productModel.setCategoryId(jsonObjectZero
						.optString("categoryid"));
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
				int orderCount = 0;
				try {
					orderCount = Integer.parseInt(jsonObjectZero
							.optString(JSON_PRODUCT_ORDER));
				} catch (Exception e) {
					orderCount = 0;
				}
				productModel.setmProductOrder(orderCount);
				// product_color
				// product_image
				// product_size
				// product_type
				JSONArray productColorArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_COLOR);
				JSONArray productNewArrivalArray = jsonObjectZero
						.getJSONArray("new_arrivals");
				JSONArray productPendingQtyArray = jsonObjectZero
						.getJSONArray("pendingQty");
				JSONArray productImageArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_IMAGE);
				/*System.out.println("size product image "
						+ productImageArray.length());*/
				JSONArray productSizeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_SIZE);
				JSONArray productTypeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_TYPE);
				JSONArray productCaseArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_CASE);
				/*ArrayList<PendingQuantityModel> pendingArray = new ArrayList<PendingQuantityModel>();
				for (int i = 0; i < productPendingQtyArray.length(); i++) {

					PendingQuantityModel pendingModel = new PendingQuantityModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					pendingModel.setColor(jsonObject.optString("color"));
					pendingModel.setColorid(jsonObject.optString("colorid"));
					pendingModel.setPendingQty(jsonObject
							.optString("pendingQty"));
					pendingModel.setSize(jsonObject.optString("size"));
					pendingModel.setSizeid(jsonObject.optString("sizeid"));
					pendingArray.add(pendingModel);

				}
				productModel.setmPendingQty(pendingArray);*/

				/*ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
				for (int i = 0; i < productColorArray.length(); i++) {

					ColorModel colorModel = new ColorModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					colorModel.setId(jsonObject
							.optString(JSON_SETTINGS_COLORID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));
					colorModel.setColorName(jsonObject.optString("color_name"));
					colorModels.add(colorModel);

				}
				productModel.setProductColor(colorModels);*/

				// ////////////
				ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
				for (int i = 0; i < productImageArray.length(); i++) {

					ProductImages images = new ProductImages();
					JSONObject jsonObject = productImageArray.getJSONObject(i);
					images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
					images.setImageName(BASE_URL
							+ jsonObject.optString(JSON_COLOR_IMAGE));
					images.setProductName(jsonObject.optString("product_name"));
					ColorModel colorModel = new ColorModel();
					colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));
					images.setColorModel(colorModel);
					productImages.add(images);

				}
				productModel.setProductImages(productImages);

				/*ArrayList<ProductImages> newArrivals = new ArrayList<ProductImages>();
				for (int i = 0; i < productNewArrivalArray.length(); i++) {

					ProductImages images = new ProductImages();
					JSONObject jsonObject = productNewArrivalArray
							.getJSONObject(i);
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
					System.out.println("Cat Id :"
							+ jsonObject.optString("categoryid"));
					newArrivals.add(images);

				}
				productModel.setmNewArrivals(newArrivals);*/
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
				/*ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
				for (int i = 0; i < productTypeArray.length(); i++) {

					BrandTypeModel typeModel = new BrandTypeModel();
					JSONObject jsonObject = productTypeArray.getJSONObject(i);
					typeModel
							.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
					typeModel.setName(jsonObject
							.optString(JSON_SETTINGS_BRANDNAME));
					typeModel.setImgUrl(jsonObject
							.optString(JSON_BRAND_IMAGENAME));

					brandTypeModels.add(typeModel);

				}
				productModel.setProductType(brandTypeModels);*/

				/*ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
				for (int i = 0; i < productCaseArray.length(); i++) {

					CaseModel caseModel = new CaseModel();
					JSONObject jsonObject = productCaseArray.getJSONObject(i);
					caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
					caseModel.setName(jsonObject
							.optString(JSON_SETTINGS_CASENAME));

					caseModels.add(caseModel);

				}
				productModel.setmProductCases(caseModels);*/
				productModels.add(productModel);

				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		setList();
		

	}

	public void setList() {
		if (productModels.size() == 0) {
			
			VKCUtils.showtoast(mActivity, 17);
		}
		if (flag == true) {

			gridProductList.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			tvList.setText("GRID");
			imgList.setImageResource(R.drawable.grid);
			listAdapter = new ProductListAdapter(getActivity(), productModels,
					1);
		
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
			gridProductList.setSelection(AppController.selectedProductPosition);
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
			} else if (option.equals("5")) {
				listAdapter.doSort(5);
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
						} else if (option.equals("Most Order")) {
							listAdapter.doSort(5);
							AppPrefenceManager.saveProductListSortOption(
									mActivity, "5");
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
		
		super.onResume();
		edtSearch.setText("");

		if (VKCUtils.checkInternetConnection(getActivity())) {
			// getProducts(mPageNumber);
			getProducts();
		} else {
			CustomToast toast = new CustomToast(getActivity()); // toast.show(0);
			VKCUtils.showtoast(mActivity, 0);
		}

	}

	/*
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) { // TODO Auto-generated method
	 * stub System.out.println("Moved Scroll"); if (firstVisibleItem +
	 * visibleItemCount >= totalItemCount) { getProducts(mPageNumber + 1); } }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) { // TODO Auto-generated method stub
	 * 
	 * }
	 */
	/*
	 * @SuppressLint("ClickableViewAccessibility")
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub int X = (int) event.getX(); int Y = (int)
	 * event.getY(); int eventaction = event.getAction(); switch (eventaction) {
	 * case MotionEvent.ACTION_DOWN: break; case MotionEvent.ACTION_UP:
	 * System.out.println("Moved up");
	 * 
	 * break; } return false; }
	 */

}
