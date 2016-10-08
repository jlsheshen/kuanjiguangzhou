package com.edu.basicaccountingforguangzhou.common;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnFocusChangeListener;

public class MyOnFocusChangeListener implements OnFocusChangeListener {

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if (arg1) {
			arg0.setBackgroundColor(0x990066FF);
		} else {
			arg0.setBackgroundColor(Color.parseColor("#00000000"));
		}
	}

}
