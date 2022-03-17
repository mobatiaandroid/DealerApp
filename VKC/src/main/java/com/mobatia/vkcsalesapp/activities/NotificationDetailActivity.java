package com.mobatia.vkcsalesapp.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
public class NotificationDetailActivity extends AppCompatActivity {
	String mDate,mMessage;
	TextView textMessage,textDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_detail);
		final ActionBar abar = getSupportActionBar();
		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Notification Detail");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		setActionBar();
		Intent intent=getIntent();
		mMessage=intent.getExtras().getString("MESSAGE");
		mDate=intent.getExtras().getString("MESSAGE_DATE");
		
		initUI();
		
	}
	private void initUI() {
		// TODO Auto-generated method stub
		
		textMessage=(TextView)findViewById(R.id.textMessageDetail);
		textDate=(TextView)findViewById(R.id.textMessageDate);
		textMessage.setText(mMessage);
		textDate.setText(mDate);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// title/icon
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@SuppressLint("NewApi")
	public void setActionBar() {
		// Enable action bar icon_luncher as toggle Home Button
		ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

		actionBar.show();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

		getSupportActionBar().setHomeButtonEnabled(true);

	}
}
