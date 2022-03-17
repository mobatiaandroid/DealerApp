/**
 * 
 */
package com.mobatia.vkcsalesapp.appdialogs;

import com.mobatia.vkcsalesapp.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Archana.S
 *
 */
public class DealerRetailerDialog extends Dialog{
	
	public Activity mActivity;
	public Dialog d;
	TextView text;
	String[] stringArray;
	String dialogTitle;
	OnDialogItemSelectListener onItemSelectListener;

	
	
	public DealerRetailerDialog(FragmentActivity a, String[] strArray,
			TextView text, String Title,OnDialogItemSelectListener onItemSelectListener) {
		super(a);
		// TODO Auto-generated constructor stub
		this.mActivity = a;
		this.text = text;
		this.stringArray = strArray;
		this.dialogTitle = Title;
		this.onItemSelectListener=onItemSelectListener;

	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_state_district_place);
		init();

	}

	private void init() {
		TextView txtTitle= (TextView) findViewById(R.id.txtTitle);
		ListView list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new DealerRetailer(stringArray, mActivity));
		txtTitle.setText(dialogTitle);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				text.setText(stringArray[position]);
				onItemSelectListener.itemSelected(position);
				dismiss();
			}
		});

	}
	public interface OnDialogItemSelectListener{
		public void itemSelected(int position);
	}

	public class DealerRetailer extends BaseAdapter {
		Activity mActivity;
		String[] states;
		LayoutInflater mLayoutInflater;

		public DealerRetailer(String[] states, Activity mActivity) {
			this.states = states;
			this.mActivity = mActivity;
			mLayoutInflater = LayoutInflater.from(mActivity);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return states.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return states;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = mLayoutInflater.inflate(
						R.layout.state_district_place_single_text, null);
			} else {
				view = convertView;
			}
			WraperClass wraperClass = new WraperClass(view);
			wraperClass.getNameText().setText(states[position]);

			return view;
		}

		private class WraperClass {
			View view = null;

			public WraperClass(View view) {
				this.view = view;
				// TODO Auto-generated constructor stub
			}

			public TextView getNameText() {
				return (TextView) view.findViewById(R.id.textViewName);
			}
		}
	}


}
