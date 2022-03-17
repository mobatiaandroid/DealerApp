/**
 * 
 */
package com.mobatia.vkcsalesapp.customview;

import java.io.Serializable;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author mobatia-user
 * 
 */
public class CustomTextView extends TextView implements Serializable {

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		//super.setTextColor(Color.WHITE);
		init();
	}

	public void init() {

		//setTypeface(null, Typeface.BOLD);
	}

}
