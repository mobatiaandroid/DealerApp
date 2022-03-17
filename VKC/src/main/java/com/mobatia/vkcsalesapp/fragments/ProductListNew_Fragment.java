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
import androidx.fragment.app.FragmentTransaction;

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
import com.mobatia.vkcsalesapp.adapter.ProductListNew_Adapter;
import com.mobatia.vkcsalesapp.appdialogs.SortDialog;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager;
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ProductModel;

/**
 * @author archana.s
 * 
 */
@SuppressLint("NewApi")
public class ProductListNew_Fragment extends Fragment implements
		VKCUrlConstants, VKCJsonTagConstants {
	private int mContainerId;
	 CustomProgressBar pDialog;
	private ListView listView;
	private GridView gridProductList;
	ArrayList<ProductModel> productModels = new ArrayList<ProductModel>();
	ProductListFragment fragmenListFragment;
	private Activity mActivity;
	FragmentTransaction mFragmentTransaction;
	private boolean exitFlag = true;
	String total_records = "0", tolal_pages = "0", current_page = "0";

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
	Boolean flag = false;
	private TextView tvList;
	private ImageView imgList;

	private int count = 0;
	String cat_id = "", parent_cat_id = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_productlist, null);
		mActivity = getActivity();
		productModels.clear();
		initialiseUI();
		AppPrefenceManager.saveProductListSortOption(mActivity, "");
		AppPrefenceManager.saveListType(getActivity(), "ProductList");
		return view;
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
		relSearchHeader.setVisibility(View.VISIBLE);
		SearchHeaderManager manager = new SearchHeaderManager(getActivity());
		manager.getSearchHeader(relSearchHeader);
		imgSearch = manager.getSearchImage();
		edtSearch = manager.getEditText();
		AppController.isCart = false;
		AppController.isClickedCartAdapter=false;
		if (VKCUtils.checkInternetConnection(getActivity())) {
			// getProducts(mPageNumber);
			getProducts(1, "");
			edtSearch.setText("");
		} else {
			CustomToast toast = new CustomToast(getActivity()); // toast.show(0);
			VKCUtils.showtoast(mActivity, 0);
		}
		manager.searchAction(getActivity(), new SearchActionInterface() {

			@Override
			public void searchOnTextChange(String key) {
				// TODO Auto-generated method stub
				if (!edtSearch.getText().toString().equals("")) {

					/*
					 * ProductListFragment.setFilter(edtSearch.getText()
					 * .toString());
					 */
					/*
					 * getProducts(1, edtSearch.getText().toString());
					 * VKCUtils.hideKeyBoard(mActivity);
					 */
					listAdapter.filter(edtSearch.getText().toString());
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

	private void getProducts(int pageNumber, String key) // int pageNumber
	{
		String dataCategory = "";
		String dataColor = "";
		String dataSize = "";
		String dataType = "";
		String dataOffer = "";
		String dataTypePrice = "";

		System.out.println("11122104:"
				+ AppPrefenceManager.getFilterDataColor(getActivity()));

		if (AppPrefenceManager.getListingOption(getActivity()).equals("0")) {
			dataCategory = DashboardFActivity.categoryId;
			if (dataCategory.equals("1") || (dataCategory.equals("2"))
					|| (dataCategory.equals("3")) || (dataCategory.equals("4"))
					|| (dataCategory.equals("5"))
					|| (dataCategory.equals("31"))
					|| (dataCategory.equals("33")) || dataCategory.equals("67")) {
				/*
				 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
				parent_cat_id = dataCategory;
				cat_id = "";
				/*
				 * AppPrefenceManager.setParentCatId(mActivity, pa);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
			} else {
				/*
				 * AppPrefenceManager.setParentCatId(mActivity, "");
				 * AppPrefenceManager.setCatId(mActivity, dataCategory);
				 */
				parent_cat_id = "";
				cat_id = dataCategory;
			}
			/*
			 * parent_cat_id=DashboardFActivity.categoryId;
			 * System.out.println("Id---"+8989+parent_cat_id);
			 */
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"1")) {
			dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
			/*
			 * if(dataCategory.equals("1")||(dataCategory.equals("2"))||
			 * (dataCategory
			 * .equals("3"))||(dataCategory.equals("4"))||(dataCategory
			 * .equals("5"))||
			 * (dataCategory.equals("31"))||(dataCategory.equals(
			 * "33"))||dataCategory.equals("67")){
			 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
			 * AppPrefenceManager.setCatId(mActivity, "");
			 * parent_cat_id=dataCategory; cat_id=""; }else{
			 * AppPrefenceManager.setParentCatId(mActivity, "");
			 * AppPrefenceManager.setCatId(mActivity, dataCategory);
			 * parent_cat_id=""; cat_id=dataCategory; }
			 */
			parent_cat_id = dataCategory;
			cat_id = "";
		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"2")) {
			dataCategory = AppPrefenceManager
					.getFilterDataCategory(getActivity());
			parent_cat_id = AppPrefenceManager.getParentCatId(getActivity());
			cat_id = AppPrefenceManager.getCatId(getActivity());
			System.out.println(parent_cat_id + ":demo1:" + cat_id);

		} else if (AppPrefenceManager.getListingOption(getActivity()).equals(
				"4")) {
			dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
			parent_cat_id = dataCategory;
			cat_id = "";
			/*
			 * if(dataCategory.equals("1")||(dataCategory.equals("2"))||
			 * (dataCategory
			 * .equals("3"))||(dataCategory.equals("4"))||(dataCategory
			 * .equals("5"))||
			 * (dataCategory.equals("31"))||(dataCategory.equals(
			 * "33"))||dataCategory.equals("67")){
			 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
			 * AppPrefenceManager.setCatId(mActivity, "");
			 * parent_cat_id=dataCategory; cat_id=""; }else{
			 * parent_cat_id=dataCategory;
			 * 
			 * } // Toast.makeText(getActivity(), "Data : " + dataCategory,
			 * 1000) // .show();
			 */} else if (AppPrefenceManager.getListingOption(getActivity())
				.equals("5")) {
			dataCategory = AppPrefenceManager.getSubCategoryId(getActivity());
			if (dataCategory.equals("1") || (dataCategory.equals("2"))
					|| (dataCategory.equals("3")) || (dataCategory.equals("4"))
					|| (dataCategory.equals("5"))
					|| (dataCategory.equals("31"))
					|| (dataCategory.equals("33")) || dataCategory.equals("67")) {
				/*
				 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
				parent_cat_id = dataCategory;
				cat_id = "";
			} else {
				/*
				 * AppPrefenceManager.setParentCatId(mActivity, "");
				 * AppPrefenceManager.setCatId(mActivity, dataCategory);
				 */
				parent_cat_id = "";
				cat_id = dataCategory;
			}
			// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
			// .show();
		}

		if (!AppPrefenceManager.getFilterDataSize(getActivity()).equals("")) {
			dataSize = AppPrefenceManager.getFilterDataSize(getActivity());
		} else {
			dataSize = "";
		}
		if (!AppPrefenceManager.getFilterDataColor(getActivity()).equals("")) {
			dataColor = AppPrefenceManager.getFilterDataColor(getActivity());
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
		System.out.println("Datatype-" + dataType);
		if (!AppPrefenceManager.getFilterDataPrice(getActivity()).equals("")) {
			dataTypePrice = AppPrefenceManager
					.getFilterDataPrice(getActivity());
		} else {
			dataTypePrice = "";
		}

		if (!AppPrefenceManager.getFilterDataOffer(getActivity()).equals("")) {
			dataOffer = AppPrefenceManager.getFilterDataOffer(getActivity());
		} else if ((!AppPrefenceManager.getOfferIDs(getActivity()).equals(""))
				&& (AppPrefenceManager.getFilterDataOffer(getActivity())
						.equals(""))) {
			dataOffer = AppPrefenceManager.getOfferIDs(getActivity());
			parent_cat_id = "";
			cat_id = "";
		} else {
			dataOffer = "";
		}
		System.out.println("parent_cat_id::" + parent_cat_id);
		System.out.println("cat_id::" + cat_id);

		String name[] = { "parent_category_id", "category_id", "color_id",
				"size_id", "type_id", "content", "offer_id", "currentpage",
				"price_range" };
		String values[] = { parent_cat_id, cat_id, dataColor, dataSize,
				dataType, key, dataOffer, String.valueOf(pageNumber),
				dataTypePrice };// ,
		// String.valueOf(pageNumber)
		for (int i = 0; i < name.length; i++) {
			System.out.println("values---" + name[i] + "-" + values[i]);
		}

		final VKCInternetManagerWithoutDialog manager = new VKCInternetManagerWithoutDialog(
				PRODUCT_DETAIL_NEW_URL);
	 pDialog = new CustomProgressBar(getActivity(),
				R.drawable.loading);

		pDialog.show();
		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListenerWithoutDialog() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						// Log.v("LOG", "12012015 success" + successResponse);
						parseResponse(successResponse);
						pDialog.dismiss();
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						pDialog.dismiss();
					}
				});
	}

	private void parseResponse(String response) {

		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			total_records = jsonObjectresponse.getString("total_records");
			tolal_pages = jsonObjectresponse.getString("tolal_pages");
			current_page = jsonObjectresponse.getString("current_page");
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
			// /productModels=new ArrayList<ProductNewModel>();
			// productModels.clear();
			ArrayList<ProductModel> tempProductArrayList = new ArrayList<ProductModel>();
			if (jsonArrayresponse.length() > 0) {
				for (int j = 0; j < jsonArrayresponse.length(); j++) {
					JSONObject obj = jsonArrayresponse.getJSONObject(j);
					ProductModel model = new ProductModel();
					model.setmProductName(obj.getString("name"));
					model.setId(obj.getString("id"));
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					try {
						Date date = format.parse(obj.getString("timestamp"));
						System.out.println("converted date=-" + date);
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
				
			} else {
				VKCUtils.showtoast(mActivity, 17);
				productModels.clear();
			}

			// productModels.addAll(tempProductArrayList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		setList();

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
			final int currentPage = Integer.parseInt(current_page);
			final int totalPage = Integer.parseInt(tolal_pages);
			listAdapter = new ProductListNew_Adapter(getActivity(),
					productModels, 1, currentPage, totalPage,
					new ProductListNew_Adapter.ScrollingAdapterinterface() {

						@Override
						public void calledInterface(int position) {
							// TODO Auto-generated method stub
							if (currentPage == totalPage) {

							} else {
								getProducts(position + 1, "");
							}
							// pos=2*position-1;

						}
					});
			listAdapter.notifyDataSetChanged();

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
			System.out.println("23122014:Flag::" + flag);
			/*
			 * listAdapter = new ProductListAdapterNew(getActivity(),
			 * productModels, 2);
			 * 
			 * gridProductList.setAdapter(listAdapter);
			 * gridProductList.setSelection
			 * (AppController.selectedProductPosition);
			 */
			final int currentPage = Integer.parseInt(current_page);
			final int totalPage = Integer.parseInt(tolal_pages);
			listAdapter = new ProductListNew_Adapter(getActivity(),
					productModels, 2, currentPage, totalPage,
					new ProductListNew_Adapter.ScrollingAdapterinterface() {

						@Override
						public void calledInterface(int position) {
							// TODO Auto-generated method stub
							if (currentPage == totalPage) {

							} else {
								getProducts(position + 1, "");
							}
							// pos=2*position-1;

						}
					});
			listAdapter.notifyDataSetChanged();

			gridProductList.setAdapter(listAdapter);
			if (currentPage == 1) {
				gridProductList.setSelection(0);

			} else {
				gridProductList.setSelection((currentPage - 1) * 20);
			}

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

	static ProductListNew_Adapter listAdapter;
	ProductModel model = null;
	SortDialog sortDialog;

	private void showDialog(String str) {
		sortDialog = new SortDialog(getActivity(), str,
				new SortDialog.SortOptionSelectionListener() {

					@Override
					public void selectedOption(String option) {
						// TODO Auto-generated method stub

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

	/*
	 * @Override public void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); edtSearch.setText(""); if
	 * (VKCUtils.checkInternetConnection(getActivity())) { getProducts(); } else
	 * { // CustomToast toast = new CustomToast(getActivity()); //
	 * toast.show(0); VKCUtils.showtoast(mActivity, 0); } }
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// edtSearch.setText("");
		//productModels.clear();

		/*if (VKCUtils.checkInternetConnection(getActivity())) {
			// getProducts(mPageNumber);
			getProducts(1, "");
			edtSearch.setText("");
		} else {
			CustomToast toast = new CustomToast(getActivity()); // toast.show(0);
			VKCUtils.showtoast(mActivity, 0);
		}*/

	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(pDialog.isShowing())
		{
			pDialog.dismiss();
		}
	}

}
