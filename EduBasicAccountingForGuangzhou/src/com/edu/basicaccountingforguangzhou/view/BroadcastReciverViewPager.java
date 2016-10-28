package com.edu.basicaccountingforguangzhou.view;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class BroadcastReciverViewPager extends ViewPager {
	int type ;
	private broadCastReceiveByXml receiveBroadCast;  //广播实例


	
	

	public BroadcastReciverViewPager(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	    // 注册广播接收
	    receiveBroadCast = new broadCastReceiveByXml();
	    IntentFilter filter = new IntentFilter();
	    filter.addAction("com.edu.basicaccountingforguangzhou.view.broadCastReceiveByXml");    //只有持有相同的action的接受者才能接收此广播
	    context.registerReceiver(receiveBroadCast, filter);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (type == 5) {
			return false;		
		}		
		return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (type == 5) {
			return false;		
		}
		return super.onTouchEvent(arg0);
	}
	
	
	public class broadCastReceiveByXml extends BroadcastReceiver
	{	 
		@Override
		public void onReceive(Context context, Intent intent) {
			  //得到广播中得到的数据，并显示出来
             type = intent.getIntExtra("type", 0);
             Log.e("wwwwwwwww", "收到广播" + type);
             
		}
	 
	}

}

