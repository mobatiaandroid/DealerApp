package com.mobatia.vkcsalesapp.activities;

import android.R;
import android.app.Activity;
import android.os.Bundle;

import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;

public class OrderListActivity extends Activity implements VKCUrlConstants,VKCJsonTagConstants{
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	}
	
	/*private void getSalesOrderStatus() {
		//salesRepOrderModels.clear();
		String name[] = { "user_id" };
		String values[] = { AppPrefenceManager.getUserId(this) };

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_SALESORDER_STATUS);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(this, name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						Log.v("LOG", "19022015 success" + successResponse);
						parseResponse(successResponse);
						if (salesRepOrderModels.size() > 0 && !isError) {
							setAdapter();
						} else {
							VKCUtils.showtoast(getActivity(), 17);
						}

					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);
						isError = true;
					}
				});
	}

	private void setAdapter() {
		mStatusList.setAdapter(new SalesRepOrderListViewAdapter(
				salesRepOrderModels, getActivity()));
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
			for (int j = 0; j < jsonArrayresponse.length(); j++) {
				SalesRepOrderModel salesRepOrderModel = new SalesRepOrderModel();
				JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);

				salesRepOrderModel.setmOrderNo(jsonObjectZero
						.optString(JSON_ORDERSTATUS_NO));
				salesRepOrderModel.setmOrderQty(jsonObjectZero
						.optString(JSON_ORDERSTATUS_QTY));
				salesRepOrderModel.setmPendingQty(jsonObjectZero
						.optString(JSON_ORDERSTATUS_PEN_QTY));
				salesRepOrderModel.setmOrderDate(jsonObjectZero
						.optString(JSON_ORDERSTATUS_DATE));
				salesRepOrderModel.setmCompany(jsonObjectZero
						.optString(JSON_ORDERSTATUS_COMPANY));
				// salesRepOrderModel.setmPrdctName(jsonObjectZero
				// .optString(JSON_ORDERSTATUS_PRDCT_NAME));
				salesRepOrderModel.setmCustomerName(jsonObjectZero
						.optString(JSON_ORDERSTATUS_NAME));
				salesRepOrderModels.add(salesRepOrderModel);
			}

		} catch (Exception e) {
			isError = true;
		}
	}

*/
}
