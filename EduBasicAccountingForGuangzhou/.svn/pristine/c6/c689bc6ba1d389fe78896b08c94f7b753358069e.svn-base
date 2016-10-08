package com.edu.basicaccountingforguangzhou;

import com.edu.library.EduBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/***
 * 欢迎界面
 * 
 */
public class WelcomActivity extends EduBaseActivity {

	private Handler handler;

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			jumpActivity(MainActivity.class, true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcom);
		handler = new Handler();
		handler.postDelayed(runnable, 2000);

	}

	/**
	 * 跳转到指定的activity
	 * 
	 * @param context
	 * @param cls
	 */
	public void jumpActivity(Class<?> cls, boolean closeFlag) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		if (closeFlag) {
			// 关闭当前Activity
			this.finish();
		}
	}

	@Override
	protected void onStop() {
		handler.removeCallbacks(runnable);
		super.onStop();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {

	}

}
