package com.edu.basicaccountingforguangzhou.billview.subject.bill.listener;

import com.edu.basicaccountingforguangzhou.billview.subject.bill.view.SignView;

/**
 * 印章盖章监听
 * 
 * @author lucher
 * 
 */
public interface SignViewListener {

	/**
	 * 盖章开始
	 * @param view 印章视图
	 */
	void onDragStart(SignView view);

	/**
	 * 盖章结束
	 * @param view 印章视图
	 */
	void onDragEnd(SignView view);

	/**
	 * 盖章提示
	 * @param view 印章视图
	 * @param msg 提示信息
	 */
	void onDragHint(SignView view, String msg);
}
