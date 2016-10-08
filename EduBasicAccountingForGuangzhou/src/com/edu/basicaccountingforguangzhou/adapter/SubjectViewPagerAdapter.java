package com.edu.basicaccountingforguangzhou.adapter;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.fragment.SubjectContentPagerFragment;
import com.edu.basicaccountingforguangzhou.view.AutoJumpNextListener;
import com.edu.basicaccountingforguangzhou.view.SubjectViewListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

/**
 * 题目练习界面用的adapter
 * 
 * @author lucher
 * 
 */
public class SubjectViewPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "SubjectTestViewPagerAdapter";

	/**
	 * 题型对应界面集合
	 */
	private ArrayList<SubjectContentPagerFragment> mPagerList = new ArrayList<SubjectContentPagerFragment>();

	/**
	 * 各种题目的集合
	 */
	private ArrayList<TestData> mSubjectList = new ArrayList<TestData>();

	/**
	 * 答题监听
	 */
	private SubjectViewListener mListener;
	/**
	 * 自动跳转监听
	 */
	private AutoJumpNextListener mAutoListener;
	/**
	 * 答题模式，与BaseScrollView里的testMode对应
	 */
	private int testMode;

	public SubjectViewPagerAdapter(FragmentManager childFragmentManager, List<TestData> datas, AutoJumpNextListener autoListener, SubjectViewListener listener, int testMode) {
		super(childFragmentManager);
		mListener = listener;
		mAutoListener = autoListener;
		this.testMode = testMode;
		if (datas != null) {
			for (TestData subject : datas) {
				mPagerList.add(null);
				mSubjectList.add(subject);
			}
		}
	}

	public void setData(List<TestData> datas) {
		mSubjectList.clear();
		if (datas != null) {
			for (TestData subject : datas) {
				mPagerList.add(null);
				mSubjectList.add(subject);
			}
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		Log.d(TAG, "get item...." + position);
		if (mPagerList.get(position) == null) {
			mPagerList.set(position, SubjectContentPagerFragment.newInstance(mSubjectList.get(position), mAutoListener, mListener, testMode));
		}
		return mPagerList.get(position);
	}

	@Override
	public int getCount() {
		return mSubjectList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 此处的销毁方案为：只销毁基础题型以及分录题型，单据类题型不销毁（否则单据类型题目会引起卡顿）
		int subType = mSubjectList.get(position).getSubjectType();
		Log.d(TAG, "destroyItem，subType:" + subType);
		// if (subType != Constant.SUBJECT_TYPE_BILL && getCount() > 1) {
		Log.e(TAG, "destroy item:" + position + ",subType:" + subType);
		try {
			// 销毁fragment
			SubjectContentPagerFragment fragment = mPagerList.get(position);
			fragment = null;
			mPagerList.set(position, fragment);
			FragmentManager manager = ((Fragment) object).getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.remove((Fragment) object);
			trans.commit();
			// 销毁视图结构
			super.destroyItem(container, position, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	/**
	 * 重置所有题
	 */
	public void reset() {
		for (SubjectContentPagerFragment pager : mPagerList) {
			if (pager != null)
				pager.reset();
		}
	}

	/**
	 * 重置某题
	 * 
	 * @param index
	 *            题目索引
	 */
	public void reset(int index) {
		SubjectContentPagerFragment pager = mPagerList.get(index);
		if (pager != null)
			pager.reset();
	}

	/**
	 * 保存某题的用户答案
	 * 
	 * @param index
	 *            题目索引
	 */
	public void saveUserAnswer(int index) {
		SubjectContentPagerFragment pager = mPagerList.get(index);
		if (pager != null)
			pager.saveUserAnswer();
	}

	/**
	 * 显示印章
	 * 
	 * @param index
	 *            题目索引
	 */
	public void setVisbilte(int index) {
		SubjectContentPagerFragment pager = mPagerList.get(index);
		if (pager != null)
			pager.setSignVisbilte();
	}

	/**
	 * 显示印章
	 * 
	 * @param index
	 *            题目索引
	 */
	public void setPictureVisbilte(int index) {
		SubjectContentPagerFragment pager = mPagerList.get(index);
		if (pager != null)
			pager.setPicturesVisbilte();
	}
}
