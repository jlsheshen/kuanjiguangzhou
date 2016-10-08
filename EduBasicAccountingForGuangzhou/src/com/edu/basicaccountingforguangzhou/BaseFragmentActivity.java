package com.edu.basicaccountingforguangzhou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.edu.library.EduBaseFragmentActivity;

/**
 * 自定义FragmentActivity基类
 * 
 * @author lucher
 * 
 */
public class BaseFragmentActivity extends EduBaseFragmentActivity {

	private static final String TAG = "BaseFragmentActivity";

	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 窗口全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mContext = this;
	}

	/**
	 * 界面跳转
	 * 
	 * @param cls
	 */
	protected void startActivity(Class cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	/**
	 * 界面跳转，带参数
	 * 
	 * @param context
	 * @param cls
	 * @param bundle
	 */
	protected void startActivity(Class cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
	}

}
