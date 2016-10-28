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
	private int type ;
	//private broadCastReceiveByXml receiveBroadCast;  //广播实例


	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BroadcastReciverViewPager(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	    // 注册广播接收
//	    receiveBroadCast = new broadCastReceiveByXml();
//	    IntentFilter filter = new IntentFilter();
//	    filter.addAction("com.edu.basicaccountingforguangzhou.view.broadCastReceiveByXml");    //只有持有相同的action的接受者才能接收此广播
//	    context.registerReceiver(receiveBroadCast, filter);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (type == 5||type == 9) {
			return false;		
		}		
		return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (type == 5||type == 9) {
			return false;		
		}
		return super.onTouchEvent(arg0);
	}

}

