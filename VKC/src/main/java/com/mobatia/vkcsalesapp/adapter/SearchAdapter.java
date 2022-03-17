/**
 * 
 */
package com.mobatia.vkcsalesapp.adapter;

import java.util.List;

import com.mobatia.vkcsalesapp.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * @author Anshad
 * 
 */
public class SearchAdapter extends CursorAdapter {

	private List<String> items;

	private TextView text;
	private Cursor cursor;

	public SearchAdapter(Context context, Cursor cursor, List<String> items) {

		super(context, cursor, false);

		this.items = items;
		this.cursor = cursor;

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.seach_row_item, parent, false);

		text = (TextView) view.findViewById(R.id.tvsearchItemRow);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		text.setText(items.get(cursor.getPosition()));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}
}
