/**
 * 
 */
package com.mobatia.vkcsalesapp.manager;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author mobatia-user
 * 
 */
public class SearchHeaderManager {

	private Context context;
	/** The relative params. */
	private RelativeLayout.LayoutParams relativeParams;

	private View headerView;

	private LayoutInflater inflater;

	private ImageView imgSearch;

	private EditText edtSearch;

	public SearchActionInterface searchInterface;
	
	public int height,width;

	public SearchHeaderManager(Context context) {
		this.context = context;
		
		getDeviceHeights();
	}

	
	public void getSearchHeader(RelativeLayout layout) {
		initialiseUI();
		relativeParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layout.addView(headerView, relativeParams);
	}

	public void initialiseUI() {
		inflater = LayoutInflater.from(context);
		headerView = inflater.inflate(R.layout.layout_searchmanager, null);
		RelativeLayout relHeader = (RelativeLayout) headerView
				.findViewById(R.id.relSearchHeader);
		
		relHeader.getLayoutParams().height=(int) (height *0.09);
		relHeader.getLayoutParams().width=(int)height;
		imgSearch = (ImageView) headerView.findViewById(R.id.imgSearch);
		edtSearch = (EditText) headerView.findViewById(R.id.edtSearch);
		edtSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "ontouch", 1000).show();
				edtSearch.setFocusableInTouchMode(true);
				
	
				
			}
		});
     edtSearch.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			edtSearch.setFocusableInTouchMode(true);
			return false;
		}
	});
	}

	public void getDeviceHeights()
	{
		DisplayManagerScale displayScale=new DisplayManagerScale(context);
		height=displayScale.getDeviceHeight();
		width=displayScale.getDeviceWidth();
	}
	public void searchAction(Context context,
			SearchActionInterface searchInterface, String key) {
		this.context = context;
		this.searchInterface = searchInterface;
		searchWithKey(key);
		// searchInterface.searchOnTextChange(key);
	}

	public ImageView getSearchImage() {
		return imgSearch;

	}

	public EditText getEditText() {
		return edtSearch;

	}

	public void searchWithKey(final String key) {
		edtSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub

						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							searchInterface.searchOnTextChange(key);
							return true;
						}
						return false;
					}
				});
	}

	public static interface SearchActionInterface {

		public void searchOnTextChange(String key);
	}
}
