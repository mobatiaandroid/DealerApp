/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.fragments.SalesOrderFragment;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.ColorModel;

/**
 * @author Bibin Johnson
 * 
 */
public class SalesOrderAdapter extends BaseAdapter implements VKCDbConstants,
		VKCUrlConstants {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<CartModel> cartArrayList;
	ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
	ImageView imgClose;
	LinearLayout linearLayout;
	private TextView mTxtViewQty, mTxtTotalItem, mTxtCartValue;
	private DataBaseManager databaseManager;
	int cartPrice;

	public SalesOrderAdapter(Activity mActivity,
			ArrayList<CartModel> cartArrayList, LinearLayout linearLayout,
			TextView txtViewQty, TextView txtTotalItem, TextView cartValue) {
		this.mActivity = mActivity;
		this.cartArrayList = cartArrayList;
		this.linearLayout = linearLayout;
		mTxtViewQty = txtViewQty;
		mTxtTotalItem = txtTotalItem;
		mTxtCartValue = cartValue;
		mInflater = LayoutInflater.from(mActivity);
		databaseManager = new DataBaseManager(mActivity);
	}

	public SalesOrderAdapter(Activity cartActivity,
			ArrayList<CartModel> cartArrayList2, LinearLayout lnrTableHeaders,
			TextView txtQty) {

		this.mActivity = cartActivity;
		this.cartArrayList = cartArrayList2;
		this.linearLayout = lnrTableHeaders;
		mTxtViewQty = txtQty;
		mInflater = LayoutInflater.from(mActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AppController.cartArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.custom_salesorder_list, null);
			holder = new ViewHolder();
			holder.txtprodId = (TextView) view.findViewById(R.id.txtProdId);
			holder.txtsize = (TextView) view.findViewById(R.id.txtSize);
			holder.txtcolourValue = (TextView) view.findViewById(R.id.txtColor);

			holder.Edtqty = (EditText) view.findViewById(R.id.txtQuantity);
			
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();

		}
		holder.Edtqty.setImeOptions(EditorInfo.IME_ACTION_DONE);
		holder.Edtqty.setOnEditorActionListener(new OnEditorActionListener() {
			 
	            @Override
	            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	                if(actionId==EditorInfo.IME_ACTION_DONE){
	                    // Creating uri to be opened
	                	SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity,
								DBNAME);
						mAdapter.openToRead();
						mAdapter.updateQuantity(
								AppController.cartArrayList.get(position)
										.getProdName(), AppController.cartArrayList
										.get(position).getProdColor(),
								AppController.cartArrayList.get(position)
										.getProdSize(), holder.Edtqty.getText()
										.toString().trim());
						mAdapter.close();
						AppController.cartArrayList.get(position).setProdQuantity(
								holder.Edtqty.getText().toString().trim());
						editApi(AppController.cartArrayList.get(position).getPid(),
								holder.Edtqty.getText().toString()); 
	                }
	                return false;
	            }

				
	        });
		
		holder.qtyWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					if (holder.Edtqty.getText().toString().trim().equals("")
							|| holder.Edtqty.getText().toString().trim()
									.equals("0")) {

					} else {
						// String prodId, String prodColor, String prodSize,
						SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity,
								DBNAME);
						mAdapter.openToRead();
						mAdapter.updateQuantity(
								AppController.cartArrayList.get(position)
										.getProdName(), AppController.cartArrayList
										.get(position).getProdColor(),
								AppController.cartArrayList.get(position)
										.getProdSize(), holder.Edtqty.getText()
										.toString().trim());
						mAdapter.close();
						AppController.cartArrayList.get(position).setProdQuantity(
								holder.Edtqty.getText().toString().trim());
						editApi(AppController.cartArrayList.get(position).getPid(),
								holder.Edtqty.getText().toString());

					}
				} catch (Exception e) {

				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// Update the quantity

				

			}
		};
		if (holder.qtyWatcher != null) {
			holder.Edtqty.removeTextChangedListener(holder.qtyWatcher);
		}
		else
		{
			holder.Edtqty.addTextChangedListener(holder.qtyWatcher);
		}

		/*
		 * holder.Edtqty.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { // TODO Auto-generated method stub try {
		 * AppController.cartArrayList.get(position).setProdQuantity(
		 * holder.Edtqty.getText().toString()); } catch (Exception e) {
		 * 
		 * } }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable s) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */
		// TextView color=(TextView)view.findViewById(R.id.txtColor);
		/*
		 * HorizontalListView relColor = (HorizontalListView) view
		 * .findViewById(R.id.listViewColor);
		 */
		holder.imgClose = (ImageView) view.findViewById(R.id.imgClose);
		holder.txtprodId.setText(AppController.cartArrayList.get(position)
				.getProdName());
		holder.txtsize.setText(AppController.cartArrayList.get(position)
				.getProdSize());
		holder.Edtqty.setText(AppController.cartArrayList.get(position)
				.getProdQuantity());
		holder.txtcolourValue.setText(AppController.cartArrayList.get(position)
				.getProdColor());

		holder.txtprodId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// AppController.product_id =
				// AppController.cartArrayList.get(position).getProdId();
				AppController.product_id = AppController.cartArrayList.get(
						position).getProdName();
				AppController.p_id = AppController.cartArrayList.get(position)
						.getProdName();
				AppController.category_id = AppController.cartArrayList.get(
						position).getCatId();
				// AppController.dashboard_activity.displayView(-1);
				/*
				 * String pid=AppController.cartArrayList.get(
				 * position).getPid(); String
				 * sapId=AppController.cartArrayList.get( position).getSapId();
				 * String prodId=AppController.cartArrayList.get(
				 * position).getProdId();
				 */
				AppController.delPosition = position;
				AppController.listScrollTo = position;
				AppController.isClickedCartAdapter = true;
				// System.out.print("Del Posi :" + AppController.delPosition);
				mActivity.startActivity(new Intent(mActivity,
						ProductDetailActivity.class));
				// getCartData();
			}
		});
		holder.txtcolourValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// AppController.product_id =
				// AppController.cartArrayList.get(position).getProdId();
				AppController.product_id = AppController.cartArrayList.get(
						position).getProdName();
				AppController.category_id = AppController.cartArrayList.get(
						position).getCatId();
				/*
				 * AppController.p_id =
				 * AppController.cartArrayList.get(position) .getPid();
				 */
				AppController.p_id = AppController.cartArrayList.get(position)
						.getProdName();
				AppController.delPosition = position;
				AppController.listScrollTo = position;
				AppController.isClickedCartAdapter = true;
				mActivity.startActivity(new Intent(mActivity,
						ProductDetailActivity.class));
			}
		});

		holder.txtsize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppController.product_id = AppController.cartArrayList.get(
						position).getProdName();
				AppController.category_id = AppController.cartArrayList.get(
						position).getCatId();
				AppController.delPosition = position;
				AppController.listScrollTo = position;
				AppController.p_id = AppController.cartArrayList.get(position)
						.getProdName();
				AppController.isClickedCartAdapter = true;
				mActivity.startActivity(new Intent(mActivity,
						ProductDetailActivity.class));

			}
		});
		// color.setText(cartArrayList.get(position).getProdColor());
		/*
		 * relColor.setAdapter(new ColorGridAdapter(mActivity,
		 * cartArrayList.get( position).getProdColor(), 0));
		 */
		System.out.print("Color "
				+ AppController.cartArrayList.get(position).getProdColor());
		RelativeLayout rel1 = (RelativeLayout) view.findViewById(R.id.rel1);
		RelativeLayout rel2 = (RelativeLayout) view.findViewById(R.id.rel2);
		RelativeLayout rel3 = (RelativeLayout) view.findViewById(R.id.rel3);
		RelativeLayout rel4 = (RelativeLayout) view.findViewById(R.id.rel4);
		RelativeLayout rel5 = (RelativeLayout) view.findViewById(R.id.rel5);

		if (position % 2 == 0) {
			rel1.setBackgroundColor(Color.rgb(219, 188, 188));
			rel2.setBackgroundColor(Color.rgb(219, 188, 188));
			rel3.setBackgroundColor(Color.rgb(219, 188, 188));
			rel4.setBackgroundColor(Color.rgb(219, 188, 188));
			rel5.setBackgroundColor(Color.rgb(219, 188, 188));
		} else {
			rel1.setBackgroundColor(Color.rgb(208, 208, 208));
			rel2.setBackgroundColor(Color.rgb(208, 208, 208));
			rel3.setBackgroundColor(Color.rgb(208, 208, 208));
			rel4.setBackgroundColor(Color.rgb(208, 208, 208));
			rel5.setBackgroundColor(Color.rgb(208, 208, 208));
		}

		holder.imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDeleteDialog(position);

			}
		});


		return view;
	}

	private void showDeleteDialog(int position) {
		DeleteAlert appDeleteDialog = new DeleteAlert(mActivity, position);

		appDeleteDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		appDeleteDialog.setCancelable(true);
		appDeleteDialog.show();

	}

	public class DeleteAlert extends Dialog implements
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

		public DeleteAlert(Activity a, int position) {
			super(a);
			// TODO Auto-generated constructor stub
			this.mActivity = a;
			this.position = position;

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_delete_alert);
			init();

		}

		private void init() {

			Button buttonSet = (Button) findViewById(R.id.buttonOk);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<String> listDelete = new ArrayList<>();
					listDelete.add(AppController.cartArrayList.get(position)
							.getPid());
					deleteApi(listDelete);
					databaseManager = new DataBaseManager(mActivity);
					databaseManager.removeFromDb(TABLE_SHOPPINGCART, "pid",
							AppController.cartArrayList.get(position).getPid());
					AppController.cartArrayList.remove(position);

					notifyDataSetChanged();
					/*
					 * int totQty = 0; for (int i = 0; i <
					 * AppController.cartArrayList.size(); i++) { totQty =
					 * totQty + Integer.parseInt(cartArrayList.get(i)
					 * .getProdQuantity()); } if
					 * (AppController.cartArrayList.size() > 0) {
					 * mTxtViewQty.setText("Total quantity :  " + "" + totQty);
					 * mTxtTotalItem.setText("Total Item : " +
					 * AppController.cartArrayList.size()); }
					 */

					if (AppController.cartArrayList.size() == 0) {
						/*
						 * mTxtViewQty.setText("Total quantity :  " + "" + 0);
						 * mTxtTotalItem.setText("Total Item : " + 0);
						 */
						VKCUtils.showtoast(mActivity, 9);
						linearLayout.setVisibility(View.GONE);
						SalesOrderFragment.isCart = false;
					}

					// updateCartValue();
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

	private void getCartData() {
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
				if (pid.equals(AppController.product_id)) {
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
			cursor.close();
			/*
			 * if (AppController.cartArrayList.size() > 0) {
			 * 
			 * mTxtTotalItem.setText("Total Item :  " + "" +
			 * AppController.cartArrayList.size());
			 * 
			 * } else { mTxtTotalItem.setText("Total Item : :  " + "" + 0);
			 * 
			 * }
			 */
		}

	}

	public void updateCartValue() {
		SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
		mAdapter.openToRead();
		String sumValue = mAdapter.getCartSum();

		if (sumValue == null) {
			mTxtCartValue.setText("Cart Value: ₹" + 0);

		} else {

			cartPrice = Integer.parseInt(sumValue);
			mTxtCartValue.setText("Cart Value: ₹" + sumValue);
		}
		mAdapter.close();
	}

	static class ViewHolder {
		TextView txtprodId;
		TextView txtsize;
		TextView txtcolourValue;
		EditText Edtqty;
		ImageView imgClose;
		TextWatcher qtyWatcher;
	}

	public void deleteApi(ArrayList<String> listDelete) {
		String name[] = { "ids" };
		String values[] = { listDelete.toString() };
		final VKCInternetManager manager = new VKCInternetManager(
				DELETE_CART_ITEM_API);

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 success" + successResponse);
						// parseResponse(successResponse);
						// setCartQuantity();

						try {
							JSONObject obj = new JSONObject(successResponse);
							String status = obj.optString("status");
							if (status.equals("Success")) {

								mTxtViewQty.setText("Total Qty. :"
										+ obj.optString("tot_qty"));
								mTxtCartValue.setText("Cart Value: ₹"
										+ obj.optString("cart_value"));
								mTxtTotalItem.setText("Total Item : "
										+ obj.optString("tot_items"));
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 Errror" + failureResponse);
					}
				});
	}

	public void editApi(String id, String qty) {
		String name[] = { "id", "quantity" };
		String values[] = { id, qty };
		final VKCInternetManager manager = new VKCInternetManager(
				EDIT_CART_ITEM_API);

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 success" + successResponse);
						// parseResponse(successResponse);
						// updateCartValue();
						// setCartQuantity();

						try {
							JSONObject obj = new JSONObject(successResponse);
							String status = obj.optString("status");
							if (status.equals("Success")) {

								mTxtViewQty.setText("Total Qty. :"
										+ obj.optString("tot_qty"));
								mTxtCartValue.setText("Cart Value: ₹"
										+ obj.optString("cart_value"));
								mTxtTotalItem.setText("Total Item : "
										+ obj.optString("tot_items"));
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getCartData();
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 Errror" + failureResponse);
					}
				});
	}

	public void setCartQuantity() {
		String cols[] = { PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
				PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
				PRODUCT_GRIDVALUE };
		Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
				"");
		int mCount = 0;
		int cartount = 0;
		if (cursor.moveToFirst()) {
			do {
				String count = cursor.getString(cursor
						.getColumnIndex("productqty"));
				cartount = Integer.parseInt(count);
				mCount = mCount + cartount;
				// do what ever you want here
			} while (cursor.moveToNext());
		}
		cursor.close();
		mTxtViewQty.setText("Total Quantity : " + String.valueOf(mCount));
	}
}
