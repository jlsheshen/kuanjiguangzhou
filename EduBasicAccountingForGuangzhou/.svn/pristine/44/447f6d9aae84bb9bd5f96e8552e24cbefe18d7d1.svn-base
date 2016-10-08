package com.edu.basicaccountingforguangzhou.fragment;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.view.AutoJumpNextListener;
import com.edu.basicaccountingforguangzhou.view.BaseScrollView;
import com.edu.basicaccountingforguangzhou.view.FenLuContentView;
import com.edu.basicaccountingforguangzhou.view.FillInContentView;
import com.edu.basicaccountingforguangzhou.view.SubjectJudgeView;
import com.edu.basicaccountingforguangzhou.view.SubjectMultiSelectView;
import com.edu.basicaccountingforguangzhou.view.SubjectSingleSelectView;
import com.edu.basicaccountingforguangzhou.view.SubjectViewListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * viewpager里存放的界面
 * 
 * @author lucher
 * 
 */
public class SubjectContentPagerFragment extends Fragment {

	/**
	 * 题目内容数据
	 */
	private TestData mData;
	/**
	 * 对应的视图
	 */
	private View mView;

	/**
	 * 答题监听
	 */
	private SubjectViewListener mListener;

	private int testMode;// 测试模式

	// 是否初始化
	private boolean prepared;
	/**
	 * 下一题跳转监听
	 */
	private AutoJumpNextListener mAutoListener;

	/**
	 * 新建实例
	 * 
	 * @param data
	 *            对应数据
	 * @param listener
	 *            题目监听
	 * @param more
	 *            是否有更多题，用于普通模式
	 * @return
	 */
	public static SubjectContentPagerFragment newInstance(TestData data, AutoJumpNextListener autoListener, SubjectViewListener listener, int testMode) {
		SubjectContentPagerFragment fragment = new SubjectContentPagerFragment();
		fragment.mData = data;
		fragment.mListener = listener;
		fragment.mAutoListener = autoListener;
		fragment.testMode = testMode;
		return fragment;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			onVisible();
		} else {
			onInvisible();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int delay = 0;
		if (mData.getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
			delay = 300;
		}
		mView.postDelayed(new Runnable() {

			@Override
			public void run() {
				onVisible();
			}
		}, delay);

	}

	/**
	 * fragment可见时回调
	 */
	private void onVisible() {
		if (getUserVisibleHint() && prepared) {
			Log.d("lucher", "onVisible  " + mData.getId());
			if (mData.getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
				((FillInContentView) mView).initView();
			}
		}
	}

	/**
	 * fragment不可见时回调
	 */
	private void onInvisible() {
		Log.e("lucher", "onInvisible " + mData.getId());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("lucher", "onCreateView:" + mData.getId());
		Context context = container.getContext();

		// 根据不同的题型创建对应的view
		int type = mData.getSubjectType();// 题型
		switch (type) {

		case Constant.SUBJECT_TYPE_SINGLE_SELECT:
			mView = new SubjectSingleSelectView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((SubjectSingleSelectView) mView).setAutoJumpNextListener(mAutoListener);
			}

			break;
		case Constant.SUBJECT_TYPE_MULTI_SELECT:
			mView = new SubjectMultiSelectView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((BaseScrollView) mView).setAutoJumpNextListener(mAutoListener);
			}

			break;
		case Constant.SUBJECT_TYPE_JUDGE:
			mView = new SubjectJudgeView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((BaseScrollView) mView).setAutoJumpNextListener(mAutoListener);
			}
			break;
		case Constant.SUBJECT_TYPE_ENTRY:
			mView = new FenLuContentView(context, mData, testMode);
			// ((BaseScrollView) mView).setSubjectViewListener(mListener);
			break;
		case Constant.SUBJECT_TYPE_BILL:
			mView = new FillInContentView(context, mData, testMode);
			break;

		default:
			break;
		}
		prepared = true;

		return mView;
	}

	/**
	 * 重置
	 */
	public void reset() {
		if (mView instanceof BaseScrollView) {
			((BaseScrollView) mView).reset();
		} else if (mView instanceof FenLuContentView) {
			((FenLuContentView) mView).reset();
		} else if (mView instanceof FillInContentView) {
			((FillInContentView) mView).reset();
		}
	}

	/**
	 * 保存用户答案
	 */
	public void saveUserAnswer() {
		if (mView instanceof FillInContentView) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					((FillInContentView) mView).saveUserAnswer();
				}
			}).start();
		}
	}

	public void setSignVisbilte() {
		if (mView instanceof FillInContentView) {
			((FillInContentView) mView).showSign();
		}
	}

	public void setPicturesVisbilte() {
		if (mView instanceof FillInContentView) {
			((FillInContentView) mView).showPictures();
		}
	}

}
