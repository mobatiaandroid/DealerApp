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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;

public class SortDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity mActivity;
	public Dialog d;
	RadioGroup optionRadioGroup;
	String TEXTTYPE;

	public SortDialog(Activity a, String TEXTTYPE,
			SortOptionSelectionListener optionSelectionListener) {
		super(a);
		// TODO Auto-generated constructor stub
		this.mActivity = a;
		this.optionSelectionListener = optionSelectionListener;
		this.TEXTTYPE = TEXTTYPE;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_sortby);
		init();

	}

	private void init() {

		optionRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
		RelativeLayout relativeSort = (RelativeLayout) findViewById(R.id.SortByBase);
		RadioButton radioPopularity = (RadioButton) findViewById(R.id.radioPopularity);
		RadioButton radioPriceLow = (RadioButton) findViewById(R.id.radioPriceLow);
		RadioButton radioPriceHigh = (RadioButton) findViewById(R.id.radioPriceHigh);
		RadioButton radioArrivals = (RadioButton) findViewById(R.id.radioArrivals);
		RadioButton radioDiscount = (RadioButton) findViewById(R.id.radioDiscount);
		RadioButton radioMostOrder = (RadioButton) findViewById(R.id.radioMostOrder);

		String option = AppPrefenceManager.getProductListSortOption(mActivity);
		if(option.equals("0")){
			radioPopularity.setChecked(true);
		}else if(option.equals("1")){
			radioPriceLow.setChecked(true);
		}else if(option.equals("2")){
			radioPriceHigh.setChecked(true);
		}else if(option.equals("3")){
			radioArrivals.setChecked(true);
		}else if(option.equals("4")){
			radioDiscount.setChecked(true);
		}else if(option.equals("5")){
			radioMostOrder.setChecked(true);
		}
		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(TEXTTYPE);
		Button buttonSet = (Button) findViewById(R.id.buttonSet);
		buttonSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int selectedId = optionRadioGroup.getCheckedRadioButtonId();
				RadioButton radioButton = (RadioButton) findViewById(selectedId);
				optionSelectionListener.selectedOption(radioButton.getText()
						.toString());
				dismiss();

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

	SortOptionSelectionListener optionSelectionListener;

	@Override
	public void onClick(View v) {

		dismiss();
	}

	public void setSortOptionSelection(String[] options,
			SortOptionSelectionListener optionSelectionListener) {
		this.optionSelectionListener = optionSelectionListener;
	}

	public static interface SortOptionSelectionListener {
		public void selectedOption(String option);
	}
}
