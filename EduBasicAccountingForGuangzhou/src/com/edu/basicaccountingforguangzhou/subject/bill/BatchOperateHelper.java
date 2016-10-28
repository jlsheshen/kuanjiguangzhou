package com.edu.basicaccountingforguangzhou.subject.bill;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.edu.basicaccountingforguangzhou.subject.bill.view.BlankEditText;
import com.edu.basicaccountingforguangzhou.subject.bill.view.FlashView;
import com.edu.basicaccountingforguangzhou.subject.bill.view.SignView;
import com.edu.basicaccountingforguangzhou.subject.view.ZoomableBillView;


/**
 * 批量处理帮助类
 * 
 * @author lucher
 * 
 */
public class BatchOperateHelper {

	// 所有空对应的编辑框
	private List<BlankEditText> mEtBlanks;
	// 所有印章对应的视图
	private List<SignView> mSignViews;
	// 所有闪电符对应的视图
	private List<FlashView> mFlashViews;

	public BatchOperateHelper() {
	}

	/**
	 * 设置元素
	 * 
	 * @param blanks
	 * @param signViews
	 * @param flashViews
	 */
	public void setElements(List<BlankEditText> blanks, List<SignView> signViews, List<FlashView> flashViews) {
		mEtBlanks = blanks;
		mSignViews = signViews;
		mFlashViews = flashViews;
	}

	/**
	 * 缩放
	 * 
	 * @param scale
	 *            缩放比例
	 * @param scaleTimes
	 *            缩放次数
	 */
	public void zoom(float scale, int scaleTimes) {
		// 空缩放
		for (BlankEditText etBlank : mEtBlanks) {
			etBlank.postScale(scale, scaleTimes);
		}
		// 印章缩放
		for (SignView signView : mSignViews) {
			signView.postScale(scale, scaleTimes);
		}
		// 闪电符缩放
		for (FlashView flashView : mFlashViews) {
			flashView.postScale(scale, scaleTimes);
		}
	}

	/**
	 * 切换用户/正确答案
	 * 
	 * @param user
	 */
	public void switchAnswer(boolean user) {
		Log.e("查看sgin专用","切换用户/正确答案" + user );

		for (BlankEditText etBlank : mEtBlanks) {
			etBlank.showAnswer(user);
		}
		for (SignView signView : mSignViews) {
			Log.e("查看sgin专用","查看印章个数" + mSignViews.size());
			signView.showUAnswer(user);
		}
	}

	/**
	 * 控件重置
	 */
	public void reset(ZoomableBillView zoomableBillView) {
		for (BlankEditText etBlank : mEtBlanks) {
			etBlank.reset();
		}
		List<SignView> tmp = new ArrayList<SignView>(mSignViews.size());//临时保存需要移除的印章
		for (SignView signView : mSignViews) {
			if (signView.getData().isUser()) {
				tmp.add(signView);
				zoomableBillView.removeView(signView);
			} else {
				signView.reset();
			}
		}
		mSignViews.removeAll(tmp);
		// for (FlashView flashView : mFlashViews) {
		// }
	}

}
