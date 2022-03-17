package com.mobatia.vkcsalesapp.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CaseModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.HomeImageBannerModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

public class ProductListNew_Adapter extends BaseAdapter implements VKCUrlConstants,VKCJsonTagConstants {
	Context mContext;
	static ArrayList<ProductModel> mProductsList;
	static ArrayList<ProductModel> mProductsListTemp;
	LayoutInflater mInflater;
	private View view;
	private int type;
	//ArrayList<ProductImages> imageUrls;
	private int currentPage;
	private int totalPage;
	ArrayList<ProductModel> productModels;
	
	private ScrollingAdapterinterface adapterinterface;
	
	//private int type;
	public ProductListNew_Adapter(Context mcontext) {
		this.mContext = mcontext;

	}

	/*public ProductListAdapterNew(Context mcontext,
			ArrayList<ProductModel> mProductsList, int type) {

		this.mContext = mcontext;
		this.mProductsList = mProductsList;
		mProductsListTemp = new ArrayList<ProductModel>();
		this.type = type;
		mInflater = LayoutInflater.from(mContext);
		mProductsListTemp.addAll(mProductsList);
		imageUrls = new ArrayList<ProductImages>();
	}*/
	
	public ProductListNew_Adapter(Context mcontext,
			ArrayList<ProductModel> mProductsList, int type,int currentPage, int totalPage, ScrollingAdapterinterface adapterinterface) {

		this.mContext = mcontext;
		this.mProductsList = mProductsList;
		mProductsListTemp = new ArrayList<ProductModel>();
		this.type = type;
		//mInflater = LayoutInflater.from(mContext);
		mProductsListTemp.addAll(mProductsList);
		//imageUrls = new ArrayList<ProductImages>();
		this.adapterinterface = adapterinterface;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
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
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		//imageUrls = mProductsList.get(position).getProductImages();
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

		if (mProductsList.size() != 0&&!mProductsList.get(position).getImage_name().equals("")) {
			VKCUtils.setImageFromUrl((Activity) mContext, BASE_URL+mProductsList.get(position).getImage_name(), holder.imageView, progressBar);
		} else {
			// holder.imageView.setBackgroundResource(R.drawable.transparent_bg);
		}
		holder.txtProductName.setText(mProductsList.get(position)
				.getmProductName());
		holder.txtProductSize.setText("Size: "
				+ mProductsList.get(position).getSize());
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
			/*	Intent intent = new Intent(mContext,
						ProductDetailsActivityNew.class);
				AppController.selectedProductPosition = position;
				intent.putExtra("MODEL", mProductsList);
intent.putExtra("position", position);
				mContext.startActivity(intent);*/
				AppController.product_id = mProductsList.get(position).getmProductName();//
				AppController.selectedProductPosition = position;
				AppController.category_id=mProductsList.get(position).getCategoryId();

				Intent intent = new Intent(mContext,
						ProductDetailActivity.class);
				
				//intent.putExtra("MODEL", mProductsList.get(position));


				mContext.startActivity(intent);
				
			}
		});
		if (position == ((currentPage*20)-1) && currentPage <= totalPage){
			adapterinterface.calledInterface(currentPage);
		}
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

				if ((product.getmProductName().contains(charText))
						|| (product.getmProductPrize().contains(charText))
						|| (product.getSize().contains(charText))
						/*|| (product.getProductColor().contains(charText))*/
						|| (product.getType().contains(charText))
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
		/*for (int i = 0; i < mProductsList.size(); i++) {
			System.out.println("23032015 "
					+ mProductsList.get(i).getmProductPrize());
		}*/
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
						Log.e("LOG 03232015", "" + ex.getMessage());
						return obj1.getmProductPrize().compareTo(
								obj2.getmProductPrize());
					}
				}
				case 2: {

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
					/*return obj1.getProductquantity().compareTo(
							obj2.getProductquantity());*/
					return obj2.getTimeStampP().compareTo(
							obj1.getTimeStampP());

				}
				case 4: {
					return obj2.getmProductOff().compareTo(
							obj1.getmProductOff());

				}
				case 5: {					/*int reVal = 0;
					if (obj1.getmProductOrder() == obj2.getmProductOrder()) {
						reVal = 0;
					}
					if (obj1.getmProductOrder() < obj2.getmProductOrder()) {
						reVal = +1;
					}
					if (obj1.getmProductOrder() > obj2.getmProductOrder()) {
						reVal = -1;
					}

					return reVal;*/
					return obj2.getProductquantity().compareTo(
							obj1.getProductquantity());

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
	public interface ScrollingAdapterinterface{
		void calledInterface(int position);
	}
	
	
	
	
	
}
