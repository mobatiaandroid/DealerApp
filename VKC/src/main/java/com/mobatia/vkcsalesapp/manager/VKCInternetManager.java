package com.mobatia.vkcsalesapp.manager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;

public class VKCInternetManager {

	final String TAG = "VKCInternetManager";
	String mRequestUrl;
	ResponseListener mResponseListener;

	public VKCInternetManager(String requestUrl) {
		this.mRequestUrl = requestUrl;
	}

	public void getResponseGET(Activity activity) {
		String tag_string_req = "string_req";

		// String url =
		// "http://www.androidhive.info/2014/05/android-working-with-volley-library-1/";

		final CustomProgressBar pDialog = new CustomProgressBar(activity,R.drawable.loading);

		//pDialog.setMessage("Loading...");
		pDialog.show();

		StringRequest strReq = new StringRequest(Method.GET, mRequestUrl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						pDialog.dismiss();

						Log.v(TAG, TAG + " " + response.toString());
						mResponseListener.responseSuccess(response.toString());
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						pDialog.dismiss();
						VolleyLog.d(TAG,
								TAG + " " + "Error: " + error.getMessage());
						mResponseListener.responseFailure(error.getMessage());
						
						//mResponseListener.responseSuccess(getResponseCache());
						
					}
				});
		// set Volley cache enable true
		strReq.setShouldCache(true);
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	public void getResponse(Activity activity, ResponseListener responseListener) {
		this.mResponseListener = responseListener;
		getResponseGET(activity);
	}

	public void getResponsePOST(Activity activity, String[] name,
			String[] value, ResponseListener responseListener) {
		this.mResponseListener = responseListener;
		getResponsePOST(activity, name, value);
	}
	
	public void getResponsePOST(Context context, String[] name,
			String[] value, ResponseListener responseListener) {
		this.mResponseListener = responseListener;
		getResponsePOST(context, name, value);
	}
	
	public void getResponsePOST(Context context, final String[] name,
			final String[] value) {
//		final CustomProgressBar pDialog = new CustomProgressBar(activity,R.drawable.loading);

		//pDialog.setMessage("Loading...");
//		pDialog.show();
		String tag_json_obj = "string_req";
		// RequestQueue queue = Volley.newRequestQueue(activity);
		StringRequest sr = new StringRequest(Request.Method.POST, mRequestUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Log.v("LOG", "9122014 " + response);
						mResponseListener.responseSuccess(response.toString());
//						pDialog.dismiss();
						Log.v("LOG", "Response Sales Order " + response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.v("LOG", "9122014 " + error);
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						mResponseListener.responseFailure(error.getMessage());
//						pDialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < name.length; i++) {
					params.put(name[i], value[i]);
				}
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		// queue.add(sr);

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
	}

	public void getResponsePOST(Activity activity, final String[] name,
			final String[] value) {
		final CustomProgressBar pDialog = new CustomProgressBar(activity,R.drawable.loading);

		//pDialog.setMessage("Loading...");
		pDialog.show();
		String tag_json_obj = "string_req";
		System.out.println("URL PRODUCT"+mRequestUrl);
		// RequestQueue queue = Volley.newRequestQueue(activity);
		StringRequest sr = new StringRequest(Request.Method.POST, mRequestUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Log.v("LOG", "9122014 " + response);
						mResponseListener.responseSuccess(response.toString());
						pDialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.v("LOG", "9122014 " + error);
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						mResponseListener.responseFailure(error.getMessage());
						pDialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < name.length; i++) {
				
				try
				{
					params.put(name[i], value[i]);
				}
				catch(Exception e)
				{
					
				}
					//System.out.println("Names:"+name[i]);
					//System.out.println("Values:"+value[i]);
				}
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		//Setting Timout Parameter : Bibin
		sr.setRetryPolicy(new DefaultRetryPolicy(1800 * 1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	        
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
	}

	public String getResponseCache() {
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(mRequestUrl);
		String data = "";
		if (entry != null) {
			try {
				data = new String(entry.data, "UTF-8");
				// handle data, like converting it to xml, json, bitmap etc.,
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// Cached response doesn't exists. Make network call here
		}
		return data;
	}

	public interface ResponseListener {
		public void responseSuccess(String successResponse);

		public void responseFailure(String failureResponse);

	}
}
