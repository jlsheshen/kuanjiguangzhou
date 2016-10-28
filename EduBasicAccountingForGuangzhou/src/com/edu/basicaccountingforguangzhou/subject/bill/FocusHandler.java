package com.edu.basicaccountingforguangzhou.subject.bill;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.edu.library.util.ToastUtil;

/**
 * 单据焦点事件处理类
 * 
 * @author lucher
 * 
 */
public class FocusHandler implements OnKeyListener, OnFocusChangeListener, OnEditorActionListener {

	private static final String TAG = FocusHandler.class.getSimpleName();
	// 需要获取焦点的所有控件
	private List<View> mViews;

	// 当前获取焦点控件
	private View mCurrentView;
	// 当前获取焦点控件的index
	private int mCurrentIndex = -1;
	private Context mContext;

	public FocusHandler(Context context) {
		mContext = context;
	}

	/**
	 * 添加需要控制焦点的视图
	 * 
	 * @param blank
	 */
	public void add(EditText view) {
		view.setOnKeyListener(this);
		view.setOnFocusChangeListener(this);
		view.setOnEditorActionListener(this);
		if (mViews == null) {
			mViews = new ArrayList<View>();
		}
		mViews.add(view);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean handleKeyEvent = (event.getAction() != KeyEvent.ACTION_UP);
		if (mViews != null && handleKeyEvent && keyCode == KeyEvent.KEYCODE_ENTER) {
			Log.d(TAG, "onKey,current:" + mCurrentView);
			mCurrentIndex = mViews.indexOf(v);
			if (mCurrentIndex < 0) {// 首次获取焦点
				mCurrentView = getNextFocusView();
				if (mCurrentView != null) {
					mCurrentView.requestFocus();
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_NEXT) {
			mCurrentView = getNextFocusView();
			if (mCurrentView != null) {
				mCurrentView.requestFocus();
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取下一个焦点控件
	 * 
	 * @return
	 */
	View getNextFocusView() {
		View view = null;
		int size = mViews.size();
		if (size <= 1) {
			mCurrentIndex = 0;
		} else {
			if (mCurrentIndex == -1) {
				mCurrentIndex = 0;
			} else {
				if (mCurrentIndex >= mViews.size() - 1) {
					mCurrentIndex = 0;
				} else {
					mCurrentIndex += 1;
				}
			}
		}
		try {
			view = mViews.get(mCurrentIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			mCurrentIndex = mViews.indexOf(v);
			if (mCurrentIndex >= 0 && mCurrentIndex < mViews.size() - 1) {
				mCurrentView = mViews.get(mCurrentIndex);
			}
		}
	}

}
