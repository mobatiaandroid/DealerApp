package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.adapter.SizeAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.customview.HorizontalListView;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.CaseModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.QuickOrderCategoryModel;
import com.mobatia.vkcsalesapp.model.QuickSizeModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

;

public class Quick_Order_Fragment extends Fragment implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants, VKCDbConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	ArrayList<String> listTemp, listTempCustom;
	private DataBaseManager databaseManager;
	Spinner mSpinnerDealer, mSpinnerSize, mSpinnerColor;
	AutoCompleteTextView mEditArticle;
	ImageView mImageSearch;
	private ImageView imageViewSubmit;
	private LinearLayout llDealer;
	Activity mActivity;
	private ArrayList<ColorModel> colorArrayList;
	private ArrayList<SizeModel> sizeArrayList;
	ArrayList<ProductModel> productModels;
	private ArrayList<CaseModel> caseArrayList;
	private ArrayList<ProductImages> newArrivalArrayList;
	ArrayList<String> listArticleNumbers;
	ArrayList<DealersShopModel> dealersShopModels;
	ArrayList<String> listDealer = new ArrayList<>();
	ArrayList<SizeModel> sizeArray;
	ProductModel productModel = new ProductModel();
	TableLayout tableLayout, tableLayoutCustomSize;
	ArrayList<EditText> editList;
	int count=0;
	ArrayList<EditText> editListCustom;
	private TextView txtNameText;
	private TextView txtViewPrice;
	private TextView txtDescription;
	private TextView txtCatName;
	int tableCount;
	private ImageView mImageAddToCart;
	private TextView textCreditValue;
	String creditValue;
	HorizontalListView listviewSize;
	String caseSize = "";
	String[] listArticle;
	float cartPrice;
	Spinner spinnerCategory;
	String selectedCatId;
	ArrayList<QuickOrderCategoryModel> arrayCategory;
	// String[] category;
	ArrayList<String> listCategory;
	private TextView txtCartValue;
	float priceTotal;
	boolean isShowMessage;
	static int creditPrice;
	private int cartValue;
	boolean isInserted;
	private CustomProgressBar pDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_quick_order, container,
				false);
		setHasOptionsMenu(true);
		AppController.isCart = false;
		isShowMessage = false;
		mActivity = getActivity();
		setDisplayParams();
		tableCount = 0;
		//getCartValue();
		//getPendingQuantity();
		final ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Quick Order");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		init(mRootView);
		
		return mRootView;
	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(View v) {

		llDealer = (LinearLayout) v.findViewById(R.id.llDealer);
		mEditArticle = (AutoCompleteTextView) v
				.findViewById(R.id.textArticleNo);
		mEditArticle.setText("");
		// mImageSearch = (ImageView) v.findViewById(R.id.imageSearch);
		tableLayout = (TableLayout) v.findViewById(R.id.matrixLayout);
		tableLayoutCustomSize = (TableLayout) v
				.findViewById(R.id.matrixLayoutCustomSize);
		txtNameText = (TextView) v.findViewById(R.id.textModel);
		txtViewPrice = (TextView) v.findViewById(R.id.textPrice);
		txtDescription = (TextView) v.findViewById(R.id.textDescription);
		textCreditValue = (TextView) v.findViewById(R.id.textCreditValue);
		txtCartValue = (TextView) v.findViewById(R.id.textCartValue);
		txtCatName = (TextView) v.findViewById(R.id.textBrand);
		colorArrayList = new ArrayList<ColorModel>();
		sizeArrayList = new ArrayList<SizeModel>();
		caseArrayList = new ArrayList<CaseModel>();
		spinnerCategory = (Spinner) v.findViewById(R.id.spinnerCategory);
		newArrivalArrayList = new ArrayList<ProductImages>();
		productModels = new ArrayList<ProductModel>();
		dealersShopModels = new ArrayList<DealersShopModel>();
		listviewSize = (HorizontalListView) v.findViewById(R.id.listviewSize);
		sizeArray = new ArrayList<SizeModel>();
		listArticleNumbers = new ArrayList<String>();
		editListCustom = new ArrayList<>();
		mImageAddToCart = (ImageView) v.findViewById(R.id.imageAddtoCart);
		arrayCategory = new ArrayList<>();
		listCategory = new ArrayList<>();
		// Set Cart Value
	//	updateCartValue();
		getArticleNumbers();
		//AppController.arrayListSize.clear();
		if (AppPrefenceManager.getUserType(getActivity()).equals("7")) {
			getMyDealers();
			llDealer.setVisibility(View.VISIBLE);
		} 
		else if(AppPrefenceManager.getUserType(getActivity()).equals("4"))
		{
			
			llDealer.setVisibility(View.GONE);
			if (!AppPrefenceManager.getSelectedUserId(getActivity()).equals("")) {
				getCreditValue(AppPrefenceManager.getCustomerId(getActivity()));
				getPendingQuantity();
			} else {
				CustomToast toast = new CustomToast(getActivity());
				toast.show(56);
			}
		}
		else {
			llDealer.setVisibility(View.GONE);
			getCreditValue(AppPrefenceManager.getCustomerId(getActivity()));
			getPendingQuantity();
		}

		mImageAddToCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInserted = false;
				AddToCart addToCart = new AddToCart();
				addToCart.execute();
			}
		});
		mEditArticle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				getCategory();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		mEditArticle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getCategory();
			}
		});

		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if (pos > 0) {
					selectedCatId = arrayCategory.get(pos - 1).getCategory_id();
					getCartPrice();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		editList = new ArrayList<EditText>();
		databaseManager = new DataBaseManager(getActivity());

		mSpinnerDealer = (Spinner) v.findViewById(R.id.spinnerDealer);

		// Dealer List Api Call
		// getMyDealers();
		/*
		 * imageViewSubmit = (ImageView) v.findViewById(R.id.imageViewSubmit);
		 * 
		 * imageViewSubmit.setOnClickListener(this);
		 */

		mSpinnerDealer.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) { // TODO Auto-generated method
												// stub
				if (position == 0) {

				} else {

					getCreditValue(dealersShopModels.get(position - 1)
							.getDealerId());

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { //
				// TODO Auto-generated method stub

			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		getActivity().invalidateOptionsMenu();

		return true;
	}

	@Override
	public void onClick(View v) {

		/*
		 * if (v == imageViewSubmit) {
		 * 
		 * } else if (v == mImageSearch) {
		 * 
		 * }
		 */

	}

	private void getMyDealers() {
		VKCInternetManager manager = null;
		dealersShopModels.clear();
		listDealer.clear();

		String name[] = { "subdealer_id" };
		String value[] = { AppPrefenceManager.getUserId(mActivity) };
		manager = new VKCInternetManager(LIST_MY_DEALERS_URL);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				// parseJSON(successResponse);
				Log.v("LOG", "06012015 " + successResponse);
				parseMyDealerJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) { // TODO
				// Auto-generated method stub

			}
		});

	}

	private void parseMyDealerJSON(String successResponse) { // TODO
		// Auto-generated method stub

		try {

			JSONObject respObj = new JSONObject(successResponse);
			JSONObject response = respObj.getJSONObject("response");
			String status = response.getString("status");
			listDealer.add("Select Dealer");
			if (status.equals("Success")) {

				JSONArray respArray = response.getJSONArray("dealers");
				for (int i = 0; i < respArray.length(); i++) {

					dealersShopModels
							.add(parseShop(respArray.getJSONObject(i)));

				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						mActivity, android.R.layout.simple_spinner_item,
						listDealer);

				// Drop down layout style - list view with radio button
				// dataAdapter
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// attaching data adapter to spinner
				mSpinnerDealer.setAdapter(dataAdapter);
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
		dealersShopModel.setCustomer_id(jsonObject.optString("customer_id"));
		dealersShopModel.setId(jsonObject.optString("id"));
		dealersShopModel.setName(jsonObject.optString("name"));
		dealersShopModel.setPhone(jsonObject.optString("phone"));
		dealersShopModel.setPincode(jsonObject.optString("pincode"));
		dealersShopModel.setState(jsonObject.optString("state"));
		dealersShopModel.setState_name(jsonObject.optString("state_name"));
		listDealer.add(jsonObject.optString("name"));
		return dealersShopModel;

	}

	private void getProducts() {
		productModels.clear();
		colorArrayList.clear();
		caseArrayList.clear();
		sizeArrayList.clear();
		newArrivalArrayList.clear();
		AppController.arrayListSize.clear();
		String name[] = { "productId", "categoryId" };
		String values[] = { mEditArticle.getText().toString().trim(),
				selectedCatId };

		final VKCInternetManager manager = new VKCInternetManager(
				GET_PRODUCT_DETAILS_URL);
		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) { //
						// TODO Auto-generated method stub
						Log.d("LOG Product", "product response2"
								+ successResponse);
						parseResponse(successResponse);
						if (!successResponse.equals("")) {
							txtNameText.setText(""
									+ productModel.getProductType().get(0)
											.getName());

							txtViewPrice.setText("Rs."
									+ productModel.getmProductPrize());
							txtDescription.setText(productModel
									.getProductdescription());
							txtCatName.setText(productModel.getCategoryName());

							// txtCatName.setText(productModel.);

							colorArrayList = productModel.getProductColor();
							sizeArrayList = productModel.getmProductSize();
							caseArrayList = productModel.getmProductCases();
							System.out.println("SizeArray Length");
							for (int i = 0; i < sizeArrayList.size(); i++) {
								QuickSizeModel model = new QuickSizeModel();
								model.setSizeName(sizeArrayList.get(i)
										.getName());
								model.setSizeId(sizeArrayList.get(i).getId());
								model.setSelected(false);
								AppController.arrayListSize.add(model);
								System.out.println("Size Name"
										+ sizeArrayList.get(i).getName());
							}

							if (AppController.cartArrayListSelected.size() > 0) {
								createTableWithData();
							} else {
								createTable();
							}

							SizeAdapter adapter = new SizeAdapter(mActivity,
									AppController.arrayListSize);
							listviewSize.setAdapter(adapter);
							AppController.product_id = "";

						} else {
							VKCUtils.showtoast(mActivity, 42);
						}

					}

					public void createTableWithData() {
						// TODO Auto-generated method stub
						tableLayout.removeAllViews();
						if (AppController.cartArrayListSelected.size() < caseArrayList
								.size()) {
							int diff = caseArrayList.size()
									- AppController.cartArrayListSelected
											.size();
							for (int i = 0; i <= diff; i++) {
								CartModel model = new CartModel();
								model.setPid("");
								model.setProdColor("");
								model.setProdName("");
								model.setProdQuantity("");
								model.setProdSize("");
								AppController.cartArrayListSelected.add(model);
							}
						}

						String caseName = "", colorName = "";
						@SuppressWarnings("deprecation")
						int width = getActivity().getWindowManager()
								.getDefaultDisplay().getWidth();
						// int column_width = width / 6;
						int column_width = 150;
						TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();

						tableLayout.setBackgroundColor(getResources().getColor(
								R.color.vkcred));

						// 2) create tableRow params
						TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
						tableRowParams.setMargins(1, 1, 1, 1);
						tableRowParams.weight = 1;
						editList.clear();
						for (int i = 0; i < caseArrayList.size() + 1; i++) {
							// 3) create tableRow
							/*
							 * if(caseArrayList.size()+1<i) {
							 */
							if (i == 0) {
								caseName = "";
							}

							else {
								caseName = caseArrayList.get(i - 1).getName();
							}

							System.out.println("Case name " + i + ": "
									+ caseName);
							// }

							TableRow tableRow = new TableRow(getActivity());
							tableRow.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
							tableRow.setBackgroundColor(getResources()
									.getColor(R.color.vkcred));
							tableRow.setPadding(0, 0, 0, 2);

							for (int j = 0; j < colorArrayList.size() + 1; j++) {

								if (j == 0) {
									colorName = "";
								} else {
									colorName = colorArrayList.get(j - 1)
											.getColorName();
								}
								System.out.println("Color name " + j + ": "
										+ colorName);
								// 4) create textView
								TextView textView = new TextView(getActivity());
								// textView.setText(String.valueOf(j));
								textView.setBackgroundColor(Color.WHITE);
								textView.setWidth(column_width);
								textView.setGravity(Gravity.CENTER
										| Gravity.CENTER_HORIZONTAL);
								textView.setLayoutParams(new ViewGroup.LayoutParams(
										ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.MATCH_PARENT));

								String s1 = Integer.toString(i);
								String s2 = Integer.toString(j);
								String s3 = s1 + s2;
								int id = Integer.parseInt(s3);
								Log.d("TAG", "-___>" + id);
								if (i == 0 && j == 0) {
									textView.setText("Test");
									textView.setBackgroundColor(Color.WHITE);
									textView.setVisibility(View.INVISIBLE);

								} else if (i == 0) {
									Log.d("TAAG", "set Column Headers");
									textView.setText(colorArrayList.get(j - 1)
											.getColorName());
								} else if (i == caseArrayList.size() + 2) {
									Log.d("TAAG", "set Column Headers");
									textView.setVisibility(View.GONE);
									EditText edit = new EditText(getActivity());

									edit.setInputType(InputType.TYPE_CLASS_NUMBER);
									// edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
									edit.setLayoutParams(new TableRow.LayoutParams(
											TableRow.LayoutParams.MATCH_PARENT,
											TableRow.LayoutParams.WRAP_CONTENT));
									edit.setHint("Case.");
									edit.setTextSize(15);
									edit.setWidth(column_width);
									edit.setBackgroundColor(Color.WHITE);
									edit.setGravity(Gravity.CENTER
											| Gravity.CENTER_HORIZONTAL);
									edit.setTag(i * 10 + j);
									edit.setSingleLine(true);
									edit.setId(i * 10 + j);

									tableRow.addView(edit, tableRowParams);

									editList.add(edit);
								} else if (j == 0) {
									Log.d("TAAG", "Set Row Headers");
									textView.setText(caseArrayList.get(i - 1)
											.getName());
								} else {
									textView.setVisibility(View.GONE);
									EditText edit = new EditText(getActivity());

									edit.setInputType(InputType.TYPE_CLASS_NUMBER);
									// edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
									edit.setLayoutParams(new TableRow.LayoutParams(
											TableRow.LayoutParams.MATCH_PARENT,
											TableRow.LayoutParams.WRAP_CONTENT));
									edit.setHint("Qty.");
									edit.setTextSize(15);
									edit.setWidth(column_width);
									edit.setBackgroundColor(Color.WHITE);
									edit.setGravity(Gravity.CENTER
											| Gravity.CENTER_HORIZONTAL);
									edit.setTag(i * 10 + j);
									edit.setSingleLine(true);
									edit.setId(i * 10 + j);

									tableRow.addView(edit, tableRowParams);

									editList.add(edit);

								}
								// 5) add textView to tableRow
								tableRow.addView(textView, tableRowParams);

							}

							// 6) add tableRow to tableLayout
							tableLayout.addView(tableRow, tableLayoutParams);

						}

						for (int i = 0; i < 2; i++) {
							// 3) create tableRow
							TableRow tableRow = new TableRow(getActivity());
							tableRow.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
							tableRow.setBackgroundColor(getResources()
									.getColor(R.color.vkcred));
							tableRow.setPadding(0, 0, 0, 2);

							for (int j = 0; j < colorArrayList.size() + 1; j++) {
								// 4) create textView
								TextView textView = new TextView(getActivity());
								// textView.setText(String.valueOf(j));
								textView.setBackgroundColor(Color.WHITE);
								textView.setWidth(column_width);
								textView.setGravity(Gravity.CENTER
										| Gravity.CENTER_HORIZONTAL);
								textView.setLayoutParams(new ViewGroup.LayoutParams(
										ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.MATCH_PARENT));

								String s1 = Integer.toString(i);
								String s2 = Integer.toString(j);
								String s3 = s1 + s2;
								int id = Integer.parseInt(s3);
								Log.d("TAG", "-___>" + id);
								if (i == 0 && j == 0) {
									textView.setText("Test");
									textView.setBackgroundColor(Color.WHITE);
									textView.setVisibility(View.GONE);

								} else if (i == 0) {
									textView.setText(colorArrayList.get(j - 1)
											.getColorName());
								} else if (i == 1 && j == 0) {
									textView.setBackgroundColor(getResources()
											.getColor(R.color.vkcred));
									textView.setVisibility(View.GONE);
								} else {
									textView.setVisibility(View.GONE);
									EditText edit = new EditText(getActivity());

									edit.setInputType(InputType.TYPE_CLASS_NUMBER);
									// edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
									edit.setLayoutParams(new TableRow.LayoutParams(
											TableRow.LayoutParams.MATCH_PARENT,
											TableRow.LayoutParams.WRAP_CONTENT));
									edit.setHint("Qty.");
									edit.setTextSize(15);
									edit.setWidth(column_width);
									edit.setBackgroundColor(Color.WHITE);
									edit.setGravity(Gravity.CENTER
											| Gravity.CENTER_HORIZONTAL);
									edit.setTag(i * 10 + j);
									edit.setSingleLine(true);
									edit.setId(i * 10 + j);
									tableRow.addView(edit, tableRowParams);
									editListCustom.add(edit);

								}
								// 5) add textView to tableRow
								tableRow.addView(textView, tableRowParams);

							}

							// 6) add tableRow to tableLayout
							tableLayoutCustomSize.addView(tableRow,
									tableLayoutParams);

						}
						setTableData();
					}

					public void setTableData() {
						String caseName = "", listCaseName;
						String colorName = "", listColorName;
						String Quantity = "";
						int selectedRow = 0;
						int selectedColumn = 0;
						int setPosition;

						for (int i = 0; i < AppController.cartArrayListSelected
								.size(); i++) {
							colorName = AppController.cartArrayListSelected
									.get(i).getProdColor();
							caseName = AppController.cartArrayListSelected.get(
									i).getProdSize();
							Quantity = AppController.cartArrayListSelected.get(
									i).getProdQuantity();
							AppController.size = caseName;
							AppController.color = colorName;
							for (int j = 0; j < caseArrayList.size(); j++) {
								listCaseName = caseArrayList.get(j).getName();
								if (listCaseName.equals(caseName)) {
									selectedRow = j;

								}

							}
							for (int k = 0; k < colorArrayList.size(); k++) {
								listColorName = colorArrayList.get(k)
										.getColorName();
								if (listColorName.equals(colorName)) {
									selectedColumn = k;

								}

							}
							if (caseArrayList.get(selectedRow).getName()
									.equals(caseName)
									&& colorArrayList.get(selectedColumn)
											.getColorName().equals(colorName)) {
								setPosition = selectedRow
										* colorArrayList.size()
										+ selectedColumn;
								editList.get(setPosition).setText(Quantity);
							}

						}

					}

					@Override
					public void responseFailure(String failureResponse) { //
						// TODO Auto-generated method stub
						// Log.d("LOG Product", "product response3");

						// Log.v("LOG", "08012015 Errror" + failureResponse);
					}
				});

	}

	private void parseResponse(String response) {

		if (!response.equals("")) {
			try {
				JSONObject jsonObjectresponse = new JSONObject(response);
				JSONObject obj = jsonObjectresponse.optJSONObject("response");
				String status = obj.getString("status");
				JSONArray jsonArrayresponse = obj.getJSONArray("details");

				for (int j = 0; j < jsonArrayresponse.length(); j++) {

					JSONObject jsonObjectZero = jsonArrayresponse
							.getJSONObject(j);
					ProductModel productModel = new ProductModel();
					productModel.setProductdescription(jsonObjectZero
							.optString("productdescription"));
					productModel.setCategoryName(jsonObjectZero
							.optString(JSON_CATEGORY_NAME));
					productModel.setCategoryId(jsonObjectZero
							.optString("categoryid"));
					productModel.setmSapId(jsonObjectZero
							.optString("productSapId"));
					productModel.setmProductPrize(jsonObjectZero
							.optString(JSON_CATEGORY_COST));
					productModel.setId(jsonObjectZero
							.optString(JSON_PRODUCT_ID));
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

					JSONArray productColorArray = jsonObjectZero
							.getJSONArray(JSON_PRODUCT_COLOR);
					JSONArray productImageArray = jsonObjectZero
							.getJSONArray(JSON_PRODUCT_IMAGE);
					System.out.println("size product image "
							+ productImageArray.length());
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
						JSONObject jsonObject = productColorArray
								.getJSONObject(i);
						colorModel.setId(jsonObject
								.optString(JSON_SETTINGS_COLORID));
						colorModel.setColorcode(jsonObject
								.optString(JSON_SETTINGS_COLORCODE));
						colorModel.setColorName(jsonObject
								.optString("color_name"));
						colorModels.add(colorModel);

					}
					productModel.setProductColor(colorModels);

					// ////////////
					ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
					for (int i = 0; i < productImageArray.length(); i++) {

						ProductImages images = new ProductImages();
						JSONObject jsonObject = productImageArray
								.getJSONObject(i);
						images.setId(jsonObject.optString("image_id"));
						images.setImageName(BASE_URL
								+ jsonObject.optString(JSON_COLOR_IMAGE));
						images.setProductName(jsonObject
								.optString("product_name"));
						ColorModel colorModel = new ColorModel();
						colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
						colorModel.setColorcode(jsonObject
								.optString(JSON_SETTINGS_COLORCODE));
						images.setColorModel(colorModel);
						productImages.add(images);

					}
					productModel.setProductImages(productImages);
					// ///

					ArrayList<ProductImages> newArrivals = new ArrayList<ProductImages>();
					for (int i = 0; i < productNewArrivalArray.length(); i++) {

						ProductImages images = new ProductImages();
						JSONObject jsonObject = productNewArrivalArray
								.getJSONObject(i);
						images.setId(jsonObject
								.optString(JSON_SETTINGS_COLORID));
						images.setImageName(BASE_URL
								+ jsonObject.optString(JSON_COLOR_IMAGE));
						images.setProductName(jsonObject
								.optString("product_name"));
						ColorModel colorModel = new ColorModel();
						colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
						colorModel.setColorcode(jsonObject
								.optString(JSON_SETTINGS_COLORCODE));
						images.setColorModel(colorModel);
						images.setCatId(jsonObject.optString("categoryid"));
						newArrivals.add(images);

					}
					productModel.setmNewArrivals(newArrivals);
					ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
					for (int i = 0; i < productSizeArray.length(); i++) {

						SizeModel sizeModel = new SizeModel();
						JSONObject jsonObject = productSizeArray
								.getJSONObject(i);
						sizeModel.setId(jsonObject
								.optString(JSON_SETTINGS_SIZEID));
						sizeModel.setName(jsonObject
								.optString(JSON_SETTINGS_SIZENAME));

						sizeModels.add(sizeModel);

					}
					productModel.setmProductSize(sizeModels);
					// /////
					ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
					for (int i = 0; i < productTypeArray.length(); i++) {

						BrandTypeModel typeModel = new BrandTypeModel();
						JSONObject jsonObject = productTypeArray
								.getJSONObject(i);
						typeModel.setId(jsonObject
								.optString(JSON_SETTINGS_BRANDID));
						typeModel.setName(jsonObject
								.optString(JSON_SETTINGS_BRANDNAME));
						typeModel.setImgUrl(jsonObject
								.optString(JSON_BRAND_IMAGENAME));

						brandTypeModels.add(typeModel);

					}
					productModel.setProductType(brandTypeModels);

					ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
					for (int i = 0; i < productCaseArray.length(); i++) {

						CaseModel caseModel = new CaseModel();
						JSONObject jsonObject = productCaseArray
								.getJSONObject(i);
						caseModel.setId(jsonObject
								.optString(JSON_SETTINGS_CASEID));
						caseModel.setName(jsonObject
								.optString(JSON_SETTINGS_CASENAME));

						caseModels.add(caseModel);

					}
					productModel.setmProductCases(caseModels);
					productModels.add(productModel);

				}
				productModel = productModels.get(0);

				int imageListSize = productModel.getProductImages().size();
				System.out.println("Size Image" + imageListSize);
				System.out.println("Product Model" + productModel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			getCreditValue(AppPrefenceManager.getCustomerId(getActivity()));
		} else {
			VKCUtils.showtoast(mActivity, 42);
		}

	}

	public void createTable() {
		tableLayout.removeAllViews();
		tableLayoutCustomSize.removeAllViews();
		@SuppressWarnings("deprecation")
		int width = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		// int column_width = width / 7;
		int column_width = 150;
		TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
		tableLayout.setBackgroundColor(getResources().getColor(R.color.vkcred));

		// 2) create tableRow params
		TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
		tableRowParams.setMargins(1, 1, 1, 1);
		tableRowParams.weight = 1;
		editList.clear();
		editListCustom.clear();
		if (caseArrayList.size() > 0 && colorArrayList.size() > 0) {

			for (int i = 0; i < caseArrayList.size() + 1; i++) {
				// 3) create tableRow
				TableRow tableRow = new TableRow(getActivity());
				tableRow.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				tableRow.setBackgroundColor(getResources().getColor(
						R.color.vkcred));
				tableRow.setPadding(0, 0, 0, 2);

				for (int j = 0; j < colorArrayList.size() + 1; j++) {
					// 4) create textView
					TextView textView = new TextView(getActivity());
					// textView.setText(String.valueOf(j));
					textView.setBackgroundColor(Color.WHITE);
					textView.setWidth(column_width);
					textView.setGravity(Gravity.CENTER
							| Gravity.CENTER_HORIZONTAL);
					textView.setLayoutParams(new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));

					String s1 = Integer.toString(i);
					String s2 = Integer.toString(j);
					String s3 = s1 + s2;
					int id = Integer.parseInt(s3);
					Log.d("TAG", "-___>" + id);
					if (i == 0 && j == 0) {
						textView.setText("Test");
						textView.setBackgroundColor(Color.WHITE);
						textView.setVisibility(View.INVISIBLE);

					} else if (i == 0) {
						Log.d("TAAG", "set Column Headers");

						textView.setText(colorArrayList.get(j - 1)

						.getColorName());
					} else if (j == 0) {
						Log.d("TAAG", "Set Row Headers");

						textView.setText(caseArrayList.get(i - 1).getName());

					} else {
						textView.setVisibility(View.GONE);
						EditText edit = new EditText(getActivity());

						edit.setInputType(InputType.TYPE_CLASS_NUMBER);
						// edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
						edit.setLayoutParams(new TableRow.LayoutParams(
								TableRow.LayoutParams.MATCH_PARENT,
								TableRow.LayoutParams.WRAP_CONTENT));
						edit.setHint("Qty.");
						edit.setTextSize(15);
						edit.setWidth(column_width);
						edit.setBackgroundColor(Color.WHITE);
						edit.setGravity(Gravity.CENTER
								| Gravity.CENTER_HORIZONTAL);
						edit.setTag(i * 10 + j);
						edit.setSingleLine(true);
						edit.setId(i * 10 + j);
						// edit.setBackgroundColor(Color.WHITE);
						// edit.setText(Double.toString(matrix[i][j]));

						tableRow.addView(edit, tableRowParams);
						editList.add(edit);

					}
					// 5) add textView to tableRow
					tableRow.addView(textView, tableRowParams);

				}

				// 6) add tableRow to tableLayout
				tableLayout.addView(tableRow, tableLayoutParams);

			}

			for (int i = 0; i < 2; i++) {
				// 3) create tableRow
				TableRow tableRow = new TableRow(getActivity());
				tableRow.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				tableRow.setBackgroundColor(getResources().getColor(
						R.color.vkcred));
				tableRow.setPadding(0, 0, 0, 2);

				for (int j = 0; j < colorArrayList.size() + 1; j++) {
					// 4) create textView
					TextView textView = new TextView(getActivity());
					// textView.setText(String.valueOf(j));
					textView.setBackgroundColor(Color.WHITE);
					textView.setWidth(column_width);
					textView.setGravity(Gravity.CENTER
							| Gravity.CENTER_HORIZONTAL);
					textView.setLayoutParams(new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));

					String s1 = Integer.toString(i);
					String s2 = Integer.toString(j);
					String s3 = s1 + s2;
					int id = Integer.parseInt(s3);
					Log.d("TAG", "-___>" + id);
					if (i == 0 && j == 0) {
						textView.setText("Test");
						textView.setBackgroundColor(Color.WHITE);
						textView.setVisibility(View.GONE);

					} else if (i == 0) {
						// Log.d("TAAG", "set Column Headers");
						textView.setText(colorArrayList.get(j - 1)
								.getColorName());
					} else if (i == 1 && j == 0) {
						textView.setBackgroundColor(getResources().getColor(
								R.color.vkcred));
						textView.setVisibility(View.GONE);
					} else {
						textView.setVisibility(View.GONE);
						EditText edit = new EditText(getActivity());

						edit.setInputType(InputType.TYPE_CLASS_NUMBER);
						// edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
						edit.setLayoutParams(new TableRow.LayoutParams(
								TableRow.LayoutParams.MATCH_PARENT,
								TableRow.LayoutParams.WRAP_CONTENT));
						edit.setHint("Qty.");
						edit.setTextSize(15);
						edit.setWidth(column_width);
						edit.setBackgroundColor(Color.WHITE);
						edit.setGravity(Gravity.CENTER
								| Gravity.CENTER_HORIZONTAL);
						edit.setTag(i * 10 + j);
						edit.setSingleLine(true);
						edit.setId(i * 10 + j);
						// edit.setBackgroundColor(Color.WHITE);
						// edit.setText(Double.toString(matrix[i][j]));

						tableRow.addView(edit, tableRowParams);
						editListCustom.add(edit);

					}
					// 5) add textView to tableRow
					tableRow.addView(textView, tableRowParams);

				}

				// 6) add tableRow to tableLayout
				tableLayoutCustomSize.addView(tableRow, tableLayoutParams);

			}

		} else {
			// Toast.makeText(mActivity, "", duration)
		}

	}

	private void getCartDataForTable() {
		// AppController.cartArrayList.clear();
		AppController.cartArrayListSelected.clear();
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE, "pid" };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");

		if (cursor.getCount() > 0) {

			while (!cursor.isAfterLast()) {
				// AppController.cartArrayList.add(setCartModel(cursor));
				String pid = cursor.getString(1);
				String pname = productModel.getmProductName();
				if (pid.equals(mEditArticle.getText().toString().trim())) {
					CartModel model = new CartModel();
					model.setPid(cursor.getString(8));
					model.setProdColor(cursor.getString(5));
					model.setProdName(cursor.getString(1));
					model.setProdQuantity(cursor.getString(6));
					model.setProdSize(cursor.getString(3));
					AppController.cartArrayListSelected.add(model);
				}
				cursor.moveToNext();
			}
			//getCartValue();
			getProducts();
		} else {
			//getCartValue();
			getProducts();

		}

	}

	private class AddToCart extends AsyncTask<Void, Void, Void> {

		final CustomProgressBar pDialog = new CustomProgressBar(mActivity,
				R.drawable.loading);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			priceTotal = 0;
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// getCartData();
			//getCartValue();

			if (productModel != null) {
				// Used for checking whether filled any data in table
				listTemp = new ArrayList<String>();
				listTempCustom = new ArrayList<>();
				if (editList.size() > 0) {

					for (int j = 0; j < editList.size(); j++) {
						String qty = editList.get(j).getText().toString()
								.trim();
						long itemCount = 0;

						try {
							// the String to int conversion happens here
							itemCount = Long.parseLong(qty);

							// print out the value after the conversion
						} catch (NumberFormatException nfe) {
							System.out.println("NumberFormatException: "
									+ nfe.getMessage());
						}
						priceTotal = priceTotal + (itemCount * cartPrice);
					}

					System.out.println("cartValue " + cartValue);
					System.out.println("priceTotal " + priceTotal);
					System.out.println("creditPrice " + creditPrice);

					if ((cartValue + priceTotal) > creditPrice) {
						isShowMessage = true;
					} else {
						isShowMessage = false;
						if (editList.size() > 0) {
							insertToDb();
						}
					}
				} else {

					if (AppController.cartArrayList.size() > 0) {

						int count = 0;
						for (int i = 0; i < AppController.cartArrayList.size(); i++) {
							SQLiteAdapter mAdapter = new SQLiteAdapter(
									mActivity, DBNAME);
							mAdapter.openToRead();
							count = mAdapter.getCountDuplicate(
									AppController.cartArrayList.get(i)
											.getProdName(),
									AppController.cartArrayList.get(i)
											.getProdSize(),
									AppController.cartArrayList.get(i)
											.getProdColor());

						}

					}

				}
				// }AddT
				if (editListCustom.size() > 0) {

					for (int j = 0; j < editListCustom.size(); j++) {
						String qty = editListCustom.get(j).getText().toString()
								.trim();
						long itemCount = 0;

						try {
							// the String to int conversion happens here
							itemCount = Long.parseLong(qty);

							// print out the value after the conversion
						} catch (NumberFormatException nfe) {
							System.out.println("NumberFormatException: "
									+ nfe.getMessage());
						}
						priceTotal = priceTotal + (itemCount * cartPrice);
					}
					if ((cartPrice + priceTotal) > creditPrice) {
						isShowMessage = true;
					} else {
						isShowMessage = false;
					}
						for (int i = 0; i < editListCustom.size(); i++) {
							String id = String.valueOf(editListCustom.get(i)
									.getId());

							String qty = editListCustom.get(i).getText()
									.toString().trim();
							Long numberofitems = null;
							if (qty.length() > 0) {
								try {
									// the String to int conversion happens here
									numberofitems = Long.parseLong(qty);

									// print out the value after the conversion
									System.out.println("int i = " + i);
								} catch (NumberFormatException nfe) {
									System.out
											.println("NumberFormatException: "
													+ nfe.getMessage());
								}
							}
							int idForColor;
							int idForSize;
							if (id.length() > 2) {
								idForColor = Integer.parseInt(id.substring(2)) - 1;
								idForSize = Integer
										.parseInt(id.substring(0, 2)) - 1;
							} else {

								idForColor = Integer.parseInt(id.substring(1)) - 1;
								// idForColor=colorId-1;
								idForSize = Integer
										.parseInt(id.substring(0, 1)) - 1;
							}
							if (qty.length() > 0) {

								getCartCount();

								for (int j = 0; j < AppController.arrayListSize
										.size(); j++) {

									if (AppController.arrayListSize.get(j)
											.isSelected()) {
										caseSize = caseSize
												+ AppController.arrayListSize
														.get(j).getSizeName()
												+ ",";
									}
								}
								System.out.println("Size" + caseSize);
								if (caseSize.length() > 0) {
									String[][] values = {
											{ PRODUCT_ID, productModel.getId() },
											{
													PRODUCT_NAME,
													productModel
															.getmProductName() },
											{ PRODUCT_SIZEID, " " },
											{
													PRODUCT_SIZE,
													caseSize.substring(
															0,
															caseSize.length() - 1) },
											{
													PRODUCT_COLORID,
													colorArrayList.get(
															idForColor).getId() },
											{
													PRODUCT_COLOR,
													colorArrayList.get(
															idForColor)
															.getColorName() },
											{ PRODUCT_QUANTITY, qty },
											{ PRODUCT_GRIDVALUE, "" },
											{
													"pid",
													String.valueOf(tableCount + 1) },
											{ "sapid", productModel.getmSapId() },
											{
													"catid",
													productModel
															.getCategoryId() },
											{ "status", "local" }
											 };//{
									/*"price",
									String.valueOf(numberofitems
											* cartPrice) }*/

									// ,{"price",String.valueOf(numberOfItems*cartPrice)}
									System.out.println(colorArrayList.get(
											idForColor).getId());

									SQLiteAdapter mAdapter = new SQLiteAdapter(
											mActivity, DBNAME);
									mAdapter.openToRead();
									int count = mAdapter.getCount(
											AppController.p_id, caseArrayList
													.get(idForSize).getName(),
											colorArrayList.get(idForColor)
													.getColorName(), qty);
									// System.out.print("Count---g" + count);
									if (count == 0) {

										mAdapter.deleteUser(AppController.p_id,
												caseArrayList.get(idForSize)
														.getName(),
												colorArrayList.get(idForColor)
														.getColorName(),
												productModel.getCategoryId());

										databaseManager.insertIntoDb(
												TABLE_SHOPPINGCART, values);

										Log.e("Values", "test " + values);
									}
									mAdapter.close();
								}
								// System.out.println("" + values);
								listTempCustom.add(qty);

							}

						}
					}

				
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		//	AppController.arrayListSize.clear();

		
			if (isShowMessage) {
				// VKCUtils.showtoast(mActivity, 46);

				showExceedDialog();
			}
			
			else
			{
				if (listTemp.size() > 0) {
					VKCUtils.showtoast(mActivity, 5);
					listTemp.clear();
					listTempCustom.clear();
					mEditArticle.setText("");
					caseSize = "";
					init(mRootView);
					AppController.p_id = "";
					
					  for (int i = 0; i < AppController.arrayListSize.size();
					  i++) {
					  AppController.arrayListSize.get(i).setSelected(false); }
					 
					 AppController.arrayListSize.clear();
					

					for (int i = 0; i < editList.size(); i++) {
						editList.get(i).setText("");
					}
					for (int i = 0; i < editListCustom.size(); i++) {
						editListCustom.get(i).setText("");
					}

					SizeAdapter adapter = new SizeAdapter(mActivity,
							AppController.arrayListSize);
					listviewSize.setAdapter(adapter);
					//updateCartValue();
					tableLayout.removeAllViews();
					tableLayoutCustomSize.removeAllViews();
					spinnerCategory.setAdapter(null);
					mSpinnerDealer.setAdapter(null);
					txtNameText.setText("");
					txtViewPrice.setText("");
					txtDescription.setText("");
					txtCatName.setText("");
					// listviewSize.setAdapter(null);
					// AppController.arrayListSize.clear();
				}

				else if (listTempCustom.size() > 0) {

					if (caseSize.length() > 0) {
						VKCUtils.showtoast(mActivity, 5);
						listTemp.clear();
						listTempCustom.clear();
						mEditArticle.setText("");
						caseSize = "";
						init(mRootView);
						AppController.p_id = "";
						
						  for (int i = 0; i <
						  AppController.arrayListSize.size(); i++) {
						  AppController
						  .arrayListSize.get(i).setSelected(false); }
						 
						 AppController.arrayListSize.clear();
					

						for (int i = 0; i < editList.size(); i++) {
							editList.get(i).setText("");
						}
						for (int i = 0; i < editListCustom.size(); i++) {
							editListCustom.get(i).setText("");
						}

						SizeAdapter adapter = new SizeAdapter(mActivity,
								AppController.arrayListSize);
						listviewSize.setAdapter(adapter);
						//updateCartValue();
						tableLayout.removeAllViews();
						tableLayoutCustomSize.removeAllViews();
						spinnerCategory.setAdapter(null);
						mSpinnerDealer.setAdapter(null);
						txtNameText.setText("");
						txtViewPrice.setText("");
						txtDescription.setText("");
						txtCatName.setText("");
					} else {
						VKCUtils.showtoast(mActivity, 47);
					}
				} else {
					VKCUtils.showtoast(mActivity, 40);
				}
			}
			
			
		
		}

	}

	private int getCartCount() {
		AppController.cartArrayList.clear();
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");

		if (cursor.getCount() > 0) {

			while (!cursor.isAfterLast()) {

				tableCount = tableCount + cursor.getCount();

				cursor.moveToNext();
			}

		}
		return cursor.getCount();
	}

	private void getCreditValue(String dealerId) {
		VKCInternetManagerWithoutDialog manager = null;
		String cust_id = "";
		if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
			cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
		} else {
			cust_id = AppPrefenceManager.getCustomerId(mActivity);
		}
		String name[] = { "dealerid" };
		String value[] = { cust_id };
		manager = new VKCInternetManagerWithoutDialog(GET_QUICK_ORDER_CREDIT_URL);
		manager.getResponsePOST(mActivity, name, value, new ResponseListenerWithoutDialog() {

			@Override
			public void responseSuccess(String successResponse) {

				try {
					JSONObject responseObj = new JSONObject(successResponse);
					JSONObject response = responseObj.getJSONObject("response");
					Log.i("Credit Response", "" + successResponse);
					String status = response.getString("status");
					if (status.equals("Success")) {
						creditValue = response.getString("creditvalue");
						if (!creditValue.equals("null")) {
							creditPrice = Integer.parseInt(response
									.getString("creditvalue"));
							textCreditValue.setText("Order Limit: ₹ "
									+ creditValue);
						} else {
							creditPrice = 0;
							textCreditValue.setText("Order Value: ₹ " + "0");
						}

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

	private void getArticleNumbers() {
		VKCInternetManager manager = null;
		listArticleNumbers.clear();
		String name[] = { "" };
		String value[] = { "" };
		manager = new VKCInternetManager(GET_QUICK_ARTICLE_NO_URL);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				try {
					JSONObject responseObj = new JSONObject(successResponse);
					JSONObject response = responseObj.getJSONObject("response");
					String status = response.getString("status");
					if (status.equals("Success")) {
						JSONArray articleArray = response
								.optJSONArray("articlenos");
						if (articleArray.length() > 0) {
							for (int i = 0; i < articleArray.length(); i++) {
								// listArticle[i]=articleArray.getString(i);
								listArticleNumbers.add(articleArray
										.getString(i).toString());
							}

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									mActivity,
									android.R.layout.simple_list_item_1,
									listArticleNumbers);
							mEditArticle.setAdapter(adapter);
						}
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

	private void getCartPrice() {
		VKCInternetManager manager = null;
		String name[] = { "productid", "userid", "categoryid" };
		String value[] = { mEditArticle.getText().toString().trim(),
				AppPrefenceManager.getUserId(mActivity), selectedCatId };
		manager = new VKCInternetManager(GET_CART_VALUE_URL);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				try {
					JSONObject responseObj = new JSONObject(successResponse);
					JSONObject response = responseObj.getJSONObject("response");
					String status = response.getString("status");
					if (status.equals("Success")) {

						try {
							cartPrice = Float.parseFloat(response
									.getString("cartvalue"));
						} catch (NumberFormatException e) {
							System.out.println("Exception " + e);
						}
						
						System.out.println("Cart Value" + cartPrice);
						getCartDataForTable();
						// getProducts();
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

	private void getCategory() {
		VKCInternetManager manager = null;
		arrayCategory.clear();
		listCategory.clear();
		String name[] = { "articleno" };
		String value[] = { mEditArticle.getText().toString().trim() };
		manager = new VKCInternetManager(GET_CATEGORY_URL);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				try {
					JSONObject responseObj = new JSONObject(successResponse);
					JSONObject response = responseObj.getJSONObject("response");

					String status = response.getString("status");
					if (status.equals("Success")) {

						JSONArray arrayCat = response
								.optJSONArray("categories");
						if (arrayCat.length() > 0) {
							for (int i = 0; i < arrayCat.length(); i++) {
								JSONObject obj = arrayCat.getJSONObject(i);
								QuickOrderCategoryModel model = new QuickOrderCategoryModel();
								model.setCategory_id(obj.getString("id"));
								model.setCategory_name(obj
										.getString("category_name"));
								arrayCategory.add(model);
							}
							listCategory.add("Select Category");
							for (int j = 0; j < arrayCategory.size(); j++) {
								listCategory.add(arrayCategory.get(j)
										.getCategory_name());
							}
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									mActivity,
									android.R.layout.simple_spinner_item,
									listCategory);
							spinnerCategory.setAdapter(dataAdapter);
						}
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

	public void updateCartValue() {
		SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
		mAdapter.openToRead();
		String sumValue = mAdapter.getCartSum();
		if (sumValue == null) {
			txtCartValue.setText("Cart Value: ₹" + 0);
		} else {
			txtCartValue.setText("Cart Value: ₹" + sumValue);
		}
	}

	public void getCartValue() {
		SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
		mAdapter.openToRead();
		String sumValue = mAdapter.getCartSum();
		if (sumValue == null) {

			cartValue = 0;
		} else {

			cartValue = Integer.parseInt(sumValue);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mEditArticle.setText("");
	}

	public class ExceedAlert extends Dialog implements
			android.view.View.OnClickListener, VKCDbConstants {

		public Activity mActivity;
		public Dialog d;
		CheckBox mCheckBoxDis;
		ImageView mImageView;
		// public Button yes, no;

		Button bUploadImage;
		String TEXTTYPE;

		ProgressBar mProgressBar;
		DataBaseManager databaseManager;
		int position;
		ArrayList<CartModel> cartList;

		public ExceedAlert(Activity a) {
			super(a);
			// TODO Auto-generated constructor stub
			this.mActivity = a;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_exceeds_limit);
			init();

		}

		private void init() {
			DisplayManagerScale disp = new DisplayManagerScale(mActivity);
			int displayH = disp.getDeviceHeight();
			int displayW = disp.getDeviceWidth();

			RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);

			// relativeDate.getLayoutParams().height = (int) (displayH * .65);
			// relativeDate.getLayoutParams().width = (int) (displayW * .90);

			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					insertToDb();
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

	private void showExceedDialog() {
		ExceedAlert appDeleteDialog = new ExceedAlert(mActivity);

		appDeleteDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		appDeleteDialog.setCancelable(true);
		appDeleteDialog.show();
	}

	public void insertToDb() {
		for (int i = 0; i < editList.size(); i++) {
			String id = String.valueOf(editList.get(i).getId());
			// int colorId = editList.get(i).getId() % 2;
			String qty = editList.get(i).getText().toString().trim();

			Long numberofitems = null;
			if (qty.length() > 0) {
				try {
					// the String to int conversion happens here
					numberofitems = Long.parseLong(qty);

					// print out the value after the conversion
					System.out.println("int i = " + i);
				} catch (NumberFormatException nfe) {
					System.out.println("NumberFormatException: "
							+ nfe.getMessage());
				}
			}
			System.out.println("Edit Qty-" + qty);
			System.out.println("id" + id);
			int idForColor;
			int idForSize;
			if (id.length() > 2) {
				idForColor = Integer.parseInt(id.substring(2)) - 1;
				idForSize = Integer.parseInt(id.substring(0, 2)) - 1;
			} else {

				idForColor = Integer.parseInt(id.substring(1)) - 1;
				// idForColor=colorId-1;
				idForSize = Integer.parseInt(id.substring(0, 1)) - 1;
			}
			if (qty.length() > 0) {

				getCartCount();

				String[][] values = {
						{ PRODUCT_ID, productModel.getId() },
						{ PRODUCT_NAME, productModel.getmProductName() },
						{ PRODUCT_SIZEID, caseArrayList.get(idForSize).getId() },
						{ PRODUCT_SIZE, caseArrayList.get(idForSize).getName() },
						{ PRODUCT_COLORID,
								colorArrayList.get(idForColor).getId() },
						{ PRODUCT_COLOR,
								colorArrayList.get(idForColor).getColorName() },
						{ PRODUCT_QUANTITY, qty }, { PRODUCT_GRIDVALUE, "" },
						{ "pid", String.valueOf(tableCount + 1) },
						{ "sapid", productModel.getmSapId() },
						{ "catid", productModel.getCategoryId() },
						
						  };
				//System.out.println(colorArrayList.get(idForColor).getId());
				// Remove edited data and insert into DB
				/*
				 * SQLiteAdapter mAdapter = new SQLiteAdapter( mActivity,
				 * DBNAME); mAdapter.openToRead(); int count =
				 * mAdapter.getCount(AppController.p_id,
				 * caseArrayList.get(idForSize).getName(),
				 * colorArrayList.get(idForColor) .getColorName(), qty);
				 * System.out.print("Count---g" + count); if (count == 0) {
				 * 
				 * mAdapter.deleteUser(AppController.p_id,
				 * caseArrayList.get(idForSize).getName(),
				 * colorArrayList.get(idForColor) .getColorName(), productModel
				 * .getCategoryId()); databaseManager.insertIntoDb(
				 * TABLE_SHOPPINGCART, values); } mAdapter.close();
				 */

				// System.out.println("" + values);

				SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
				mAdapter.openToRead();
				int count = mAdapter.getCount(AppController.p_id, caseArrayList
						.get(idForSize).getName(),
						colorArrayList.get(idForColor).getColorName(), qty);
				int countDuplicate = mAdapter.getCountDuplicateEntry(
						productModel.getmProductName(),
						caseArrayList.get(idForSize).getName(), colorArrayList
								.get(idForColor).getColorName());
				System.out.print("Count---g" + count);
				if (countDuplicate == 0) {
					if (AppController.p_id.length() > 0) {
						/*mAdapter.deleteUser(AppController.p_id, caseArrayList
								.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
						addCartApi(productModel.getmProductName(), qty,
								productModel.getCategoryId(), caseArrayList
										.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								"6", values);
						databaseManager
								.insertIntoDb(TABLE_SHOPPINGCART, values);
					} else {
						
						addCartApi(productModel.getmProductName(), qty,
								productModel.getCategoryId(), caseArrayList
										.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								"6", values);
						/*mAdapter.deleteUser(productModel.getmProductName(),
								caseArrayList.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
						databaseManager
								.insertIntoDb(TABLE_SHOPPINGCART, values);
					}
				} else if (countDuplicate > 0) {
					if (AppController.p_id.length() > 0) {
						/*mAdapter.deleteUser(AppController.p_id, caseArrayList
								.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
						addCartApi(productModel.getmProductName(), qty,
								productModel.getCategoryId(), caseArrayList
										.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								"6", values);
						databaseManager
								.insertIntoDb(TABLE_SHOPPINGCART, values);
					} else {
					/*	mAdapter.deleteUser(productModel.getmProductName(),
								caseArrayList.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
						addCartApi(productModel.getmProductName(), qty,
								productModel.getCategoryId(), caseArrayList
										.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								"6", values);
						databaseManager
								.insertIntoDb(TABLE_SHOPPINGCART, values);
					}
					/*
					 * String oldqty = mAdapter.getQty(productModel
					 * .getmProductName(), colorArrayList.get(idForColor)
					 * .getColorName(), caseArrayList .get(idForSize).getName(),
					 * productModel.getCategoryId()); int total =
					 * Integer.parseInt(oldqty) + Integer.parseInt(qty);
					 */
					// System.out.println("total" + total);
					/*
					 * mAdapter.updateData(productModel .getmProductName(),
					 * colorArrayList.get(idForColor) .getColorName(),
					 * caseArrayList .get(idForSize).getName(),
					 * String.valueOf(Integer.parseInt(oldqty) +
					 * Integer.parseInt(qty)));
					 */
				}

				mAdapter.close();
				listTemp.add(qty);
				
			}
	/*		mActivity.runOnUiThread(new Runnable() {
				public void run() {
					// Here your code that runs on UI Threads

					try {
						if (listTemp.size() > 0) {
							VKCUtils.showtoast(mActivity, 5);
							
							
						} else {
							VKCUtils.showtoast(mActivity, 40);
						}
					} catch (Exception e) {
						System.out.println(e);
					}
					for (int i = 0; i < editList.size(); i++) {
						editList.get(i).setText("");
					}

					
				}
			});*/
		}

		/*
		 * if (listTemp.size() > 0) { VKCUtils.showtoast(mActivity, 5);
		 * mEditArticle.setText(""); } else { VKCUtils.showtoast(mActivity, 40);
		 * }
		 */

		/*
		 * for (int i = 0; i < editList.size(); i++) {
		 * editList.get(i).setText(""); } for (int i = 0; i <
		 * editListCustom.size(); i++) { editListCustom.get(i).setText(""); }
		 */
		isInserted = true;


	}
	private void addCartApi(String product_id, String qty, String category_id,
			String size, String color, String role, final String[][] values) {
		VKCInternetManager manager = null;
		
		String cust_id = "";
		if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
		} else {
			cust_id = AppPrefenceManager.getCustomerId(mActivity);
		}
		String name[] = { "product_id", "quantity", "category_id", "size",
				"color", "role", "cust_id" };
		String value[] = { product_id, qty, category_id, size, color, role,
				cust_id };
		manager = new VKCInternetManager(ADD_TO_CART_API);
		manager.getResponsePOST(mActivity, name, value,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {

						try {
							JSONObject responseObj = new JSONObject(
									successResponse);
							JSONObject response = responseObj
									.getJSONObject("response");
							String status = response.getString("status");
							if (status.equals("Success")) {
								count++;
								txtCartValue.setText("Cart Value: "+response.optString("cart_value"));
							if(listTemp.size()==count)
							{
								if (listTemp.size() > 0) {
									for (int i = 0; i < editList.size(); i++) {
										editList.get(i).setText("");
									}
									VKCUtils.showtoast(mActivity, 5);
									for (int i = 0; i < editList.size(); i++) {
										editList.get(i).setText("");
									}
									
								} else {
									VKCUtils.showtoast(mActivity, 40);
								}
								
							
						}
							}
							
						}catch (JSONException e) {
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
	public void getPendingQuantity() {
		//AppController.cartArrayList.clear();
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

			
				txtCartValue.setText("Cart Value: ₹"
						+ response.optString("cart_value"));
			

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}