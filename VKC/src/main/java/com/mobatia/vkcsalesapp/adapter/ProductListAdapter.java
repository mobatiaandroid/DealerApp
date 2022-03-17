package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.zip.Inflater;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ProductListAdapter extends BaseAdapter implements VKCUrlConstants {
	Context mContext;

	static ArrayList<ProductModel> mProductsList;

	static ArrayList<ProductModel> mProductsListTemp;

	LayoutInflater mInflater;

	private View view;

	private int type;
	ArrayList<ProductImages> imageUrls;

	public ProductListAdapter(Context mcontext) {

		this.mContext = mcontext;

	}

	public ProductListAdapter(Context mcontext,
			ArrayList<ProductModel> mProductsList, int type) {

		this.mContext = mcontext;
		this.mProductsList = mProductsList;
		mProductsListTemp = new ArrayList<ProductModel>();
		this.type = type;
		mInflater = LayoutInflater.from(mContext);
		mProductsListTemp.addAll(mProductsList);
		imageUrls = new ArrayList<ProductImages>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mProductsList.size();
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
		// TODO Auto-generated method stub

		ViewHolder holder = null;

		View view;

		if (convertView == null) {

			if (type == 1) {

				view = mInflater.inflate(R.layout.custom_listlayout, parent,
						false);
			} else {
				view = mInflater.inflate(R.layout.custom_gridlayout, parent,
						false);
			}

		} else {
			view = convertView;
		}

		imageUrls = mProductsList.get(position).getProductImages();
		/*System.out.println("img urls 21052015 size " + imageUrls.size());
		for (int i = 0; i < imageUrls.size(); i++) {
			System.out.println("img urls 21052015 "
					+ imageUrls.get(i).getImageName());
		}*/

		// if (convertView == null)
		{
			// view = mInflater.inflate(R.layout.custom_listlayout, parent,
			// false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.imageView);
			holder.txtProductName = (TextView) view
					.findViewById(R.id.txtProductName);
			holder.txtProductSize = (TextView) view
					.findViewById(R.id.txtProductSize);
			holder.txtProductItemNumber = (TextView) view
					.findViewById(R.id.txtProductItemNumber);
			holder.txtProductPrice = (TextView) view
					.findViewById(R.id.txtProductPrice);
			holder.txtProductOff = (TextView) view
					.findViewById(R.id.txtProductOff);
			// holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
			// view.setTag(holder);

		}
		// else
		{
			// holder = (ViewHolder) view.getTag();
			// view = convertView;
		}
		// holder.imageView.setBackgroundResource(R.drawable.sandal);
		holder.imageView.setScaleType(ScaleType.CENTER_CROP);
		final ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar1);

		if (imageUrls.size() > 0) {
			VKCUtils.setImageFromUrlGrid((Activity) mContext, imageUrls.get(0)
					.getImageName(), holder.imageView, progressBar);
		} else {
			// holder.imageView.setBackgroundResource(R.drawable.transparent_bg);
		}
		holder.txtProductName.setText(mProductsList.get(position)
				.getmProductName());
		holder.txtProductSize.setText("Size: "
				+ getProductSizeList(mProductsList.get(position)
						.getmProductSize()));
		// ₹
		holder.txtProductItemNumber.setText("Item Number: "
				+ mProductsList.get(position).getId());
		holder.txtProductPrice.setText(" ₹ "
				+ mProductsList.get(position).getmProductPrize());
		// holder.ratingBar.setRating(4);
		holder.txtProductOff.setText("Offer: "
				+ mProductsList.get(position).getmProductOff() + " %");

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//Bibin Edited 24/11/2016
				AppController.product_id = mProductsList.get(position).getmProductName();//
				AppController.selectedProductPosition = position;
				AppController.category_id=mProductsList.get(position).getCategoryId();

				Intent intent = new Intent(mContext,
						ProductDetailActivity.class);
				
				//intent.putExtra("MODEL", mProductsList.get(position));


				mContext.startActivity(intent);
				


			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mProductsList.clear();
		if (charText.length() == 0) {
			mProductsList.addAll(mProductsListTemp);

		} else {
			for (ProductModel product : mProductsListTemp) {

				/*if ((product.getmProductName().contains(charText))
						|| (product.getmProductPrize().contains(charText))
						|| (product.getmProductSize().contains(charText))
						|| (product.getProductColor().contains(charText))
						|| (product.getProductDescription().contains(charText))
						|| (product.getmProductOff().contains(charText))) {
					mProductsList.add(product);
				}*/
				if ((product.getmProductName().contains(charText))
						|| (product.getmProductPrize().contains(charText))
						|| (product.getmProductSize().contains(charText))						
						|| (product.getProductDescription().contains(charText))
						|| (product.getmProductOff().contains(charText))) {
					mProductsList.add(product);
				}
			}
		}
		notifyDataSetChanged();
	}

	private String getProductSizeList(ArrayList<SizeModel> sizeModels) {

		String size = "";

		for (SizeModel sizeModel : sizeModels) {

			size = size + sizeModel.getName() + ",";
		}
		if (size.length() != 0) {
			return size.substring(0, size.length() - 1);
		} else {
			return "";
		}

	}

	public class ViewHolder {
		ImageView imageView;
		TextView txtProductName, txtProductSize, txtProductItemNumber,
				txtProductPrice, txtProductOff, txtShop;
		// RatingBar ratingBar;
	}

	public void doSort(int sortOption) {
		inItComparator(sortOption);
		Collections.sort(mProductsList, myComparator);
		notifyDataSetChanged();
	}

	private void inItComparator(final int sortOption) {
		myComparator = new Comparator<ProductModel>() {
			public int compare(ProductModel obj1, ProductModel obj2) {
				// return
				// obj1.getmProductPrize().compareTo(obj2.getmProductPrize());

				switch (sortOption) {
				case 0: {

					return obj1.getProductViews().compareTo(
							obj2.getProductViews());
				}
				case 1: {
					// low to high

					try {
						return Integer.parseInt(obj1.getmProductPrize())
								- Integer.parseInt(obj2.getmProductPrize());
					} catch (Exception ex) {

						return obj1.getmProductPrize().compareTo(
								obj2.getmProductPrize());
					}
				}
				case 2: {

					/*
					 * return obj2.getmProductPrize().compareTo(
					 * obj1.getmProductPrize());
					 */
					try {
						return Integer.parseInt(obj2.getmProductPrize())
								- Integer.parseInt(obj1.getmProductPrize());
					} catch (Exception ex) {
						Log.e("LOG 03232015", "" + ex.getMessage());
						return obj2.getmProductPrize().compareTo(
								obj1.getmProductPrize());
					}
				}
				case 3: {
					// Want to remove
					return obj1.getProductquantity().compareTo(
							obj2.getProductquantity());

				}
				case 4: {
					return obj2.getmProductOff().compareTo(
							obj1.getmProductOff());

				}
				case 5: {
					// return obj1.getmProductOrder().compareTo(
					// obj2.getmProductOrder());
					int reVal = 0;
					if (obj1.getmProductOrder() == obj2.getmProductOrder()) {
						reVal = 0;
					}
					if (obj1.getmProductOrder() < obj2.getmProductOrder()) {
						reVal = +1;
					}
					if (obj1.getmProductOrder() > obj2.getmProductOrder()) {
						reVal = -1;
					}

					return reVal;

				}
				default: {
					return obj1.getmProductName().compareTo(
							obj2.getmProductName());
				}
				}

			}
		};
	}

	Comparator<ProductModel> myComparator;

}
