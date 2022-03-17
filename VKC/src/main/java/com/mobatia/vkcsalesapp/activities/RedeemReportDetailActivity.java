package com.mobatia.vkcsalesapp.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.ReportDetailAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;

public class RedeemReportDetailActivity extends AppCompatActivity implements
		OnClickListener, VKCUrlConstants {

	Activity mContext;
	ListView listViewDetails;
	int position;
	String cust_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem_report_detail);
		mContext = this;
		initialiseUI();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	private void initialiseUI() {

		final ActionBar abar = getSupportActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Report Detail");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setActionBar();
		Intent intent = getIntent();
		cust_id = intent.getExtras().getString("cust_id");
		listViewDetails = (ListView) findViewById(R.id.listViewRedeemReportDetail);
		for (int i = 0; i < AppController.listRedeemReport.size(); i++) {
			if (AppController.listRedeemReport.get(i).getCustId()
					.equals(cust_id)) {
				position = i;
				break;
			}
		}
		ReportDetailAdapter adapter = new ReportDetailAdapter(mContext,
				AppController.listRedeemReport.get(position)
						.getListReportDetail());
		listViewDetails.setAdapter(adapter);
	}

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
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();

	}

	@Override
	public void onClick(View v) {

	}

}
