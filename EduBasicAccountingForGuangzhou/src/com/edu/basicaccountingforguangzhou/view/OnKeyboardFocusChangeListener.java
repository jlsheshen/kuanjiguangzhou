package com.edu.basicaccountingforguangzhou.view;

import android.view.View;

/**
 * 输入法焦点改变监听
 * 
 * @author lucher
 * 
 */
public interface OnKeyboardFocusChangeListener {

	 /**
	  * 输入法对应焦点改变
	 * @param view 焦点改变的view
	 * @param hasFocus 当前是否有焦点
	 */
	void onFocusChange(View view, boolean hasFocus);
}
