package com.mobatia.vkcsalesapp.appdialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;

public class AppExitDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity mActivity;
	public Dialog d;
	String TEXTTYPE;

	public AppExitDialog(Activity a, String TEXTTYPE) {
		super(a);
		// TODO Auto-generated constructor stub
		this.mActivity = a;

		this.TEXTTYPE = TEXTTYPE;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_app_exit);
		init();

	}

	private void init() {

		RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);
		Button buttonSet = (Button) findViewById(R.id.buttonOk);
		buttonSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
				mActivity.finish();

			}
			// alrtDbldr.cancel();

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
