package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.SalesRepOrderListViewAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel;

import java.util.ArrayList;

public class SalesRepOrderList extends AppCompatActivity implements VKCUrlConstants {
	ListView salesRepOrderListView;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesrep_order_list);
		setActionBar();
		init(savedInstanceState);
		
	}

	private void init(Bundle savedInstanceState) {
		salesRepOrderListView = (ListView) findViewById(R.id.salesRepOrderListView);
		salesRepOrderListView.setAdapter(new SalesRepOrderListViewAdapter(
				salesRepOrderModels, SalesRepOrderList.this));

		setItemSelectListener(salesRepOrderListView);
	}

	private void setItemSelectListener(ListView salesRepOrderListView2) {
		// TODO Auto-generated method stub
		salesRepOrderListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SalesRepOrderList.this, OrderedProductList.class);
						startActivity(intent);
					}
				});
	}

	@SuppressLint("NewApi")
	public void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
		actionBar.show();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
		getSupportActionBar().setHomeButtonEnabled(true);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}

}
