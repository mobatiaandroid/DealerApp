package com.mobatia.vkcsalesapp.adapter;
///**
// * 
// */
//package com.mobatia.vkcsalesapp.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.mobatia.vkcsalesapp.model.CartModel;
//import com.mobatia.vkcsalesapp.model.ColorModel;
//
//import android.app.Activity;
//import android.content.Context;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SimpleAdapter;
//
///**
// * @author mobatia-user
// *
// */
//public class CartAdapter extends SimpleAdapter{
//	Activity mActivity;
//	LayoutInflater mInflater;
//	ArrayList<CartModel> cartArrayList;
//	ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
//	ImageView imgClose;
//	LinearLayout linearLayout;
//	
//	private int[] colors = new int[] { 0x30ffffff, 0x30808080 };
//
//	/**
//	 * @param context
//	 * @param data
//	 * @param resource
//	 * @param from
//	 * @param to
//	 */
//	public CartAdapter(Context context, List<? extends Map<String, ?>> data,
//			int resource, String[] from, int[] to) {
//		super(context, data, resource, from, to);
//		// TODO Auto-generated constructor stub
//	}
//	
//
//	public CartAdapter(FragmentActivity mActivity,ArrayList<CartModel> cartArrayList,LinearLayout linearLayout)
//	{
//		this.mActivity=mActivity;
//		this.cartArrayList=cartArrayList;
//		this.linearLayout=linearLayout;
//		mInflater=LayoutInflater.from(mActivity);
//	}
//	
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return cartArrayList.size();
//	}
//	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//	  View view = super.getView(position, convertView, parent);
//
//	  int colorPos = position % colors.length;
//	  view.setBackgroundColor(colors[colorPos]);
//	  return view;
//	}
//
//}
