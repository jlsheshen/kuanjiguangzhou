package com.edu.basicaccountingforguangzhou.billview.testbill.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.edu.basicaccountingforguangzhou.billview.subject.common.SubjectViewPagerFragment;
import com.edu.basicaccountingforguangzhou.billview.subject.data.BaseTestData;
import com.edu.basicaccountingforguangzhou.billview.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.billview.testbill.data.SubjectTestDataDao;
import com.edu.subject.SubjectState;

/**
 * 题目练习界面用的adapter
 * 
 * @author lucher
 * 
 */
public class SubjectViewPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "SubjectTestViewPagerAdapter";

	/**
	 * 对应界面集合
	 */
	private ArrayList<SubjectViewPagerFragment> mPagerList = new ArrayList<SubjectViewPagerFragment>();

	/**
	 * 题目的集合
	 */
	private ArrayList<BaseTestData> mSubjectList = new ArrayList<BaseTestData>();
	private Context mContext;

	public SubjectViewPagerAdapter(FragmentManager childFragmentManager, List<BaseTestData> datas, Context context) {
		super(childFragmentManager);
		mContext = context;
		if (datas != null) {
			for (BaseTestData subject : datas) {
				mPagerList.add(null);
				mSubjectList.add(subject);
			}
		}
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		Log.d(TAG, "get item...." + position);
		if (mPagerList.get(position) == null) {
			mPagerList.set(position, SubjectViewPagerFragment.newInstance(mSubjectList.get(position)));
		}
		return mPagerList.get(position);
	}

	@Override
	public int getCount() {
		if (mSubjectList == null) {
			return -1;
		}
		return mSubjectList.size();
	}

	/**
	 * 获取指定数据
	 * 
	 * @param position
	 * @return
	 */
	public BaseTestData getData(int position) {
		return mSubjectList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		try {
			// 销毁fragment
			SubjectViewPagerFragment fragment = mPagerList.get(position);
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
	}

	/**
	 * 重置所有题
	 */
	public void reset() {
		for (SubjectViewPagerFragment pager : mPagerList) {
			if (pager != null)
				pager.reset();
		}
	}

	/**
	 * 盖章
	 * 
	 * @param index
	 *            题目索引
	 * @param sign
	 *            印章数据
	 */
	public void sign(int index, SignData signData) {
		mPagerList.get(index).sign(signData);
	}

	/**
	 * 显示闪电符
	 * 
	 * @param index
	 *            题目索引
	 */
	public void showFlash(int index) {
		mPagerList.get(index).showFlash();
	}

	/**
	 * 提交
	 * 
	 * @return 得分
	 */
	public float submit() {
		float totalScore = 0;
		for (int i = 0; i < mPagerList.size(); i++) {
			SubjectViewPagerFragment pager = mPagerList.get(i);
			if (pager != null) {
				pager.submit();
			}
			totalScore += mSubjectList.get(i).getuScore();
			mSubjectList.get(i).setState(SubjectState.STATE_FINISHED);
		}
		Log.d(TAG, "totalScore:" + totalScore);
		SubjectTestDataDao.getInstance(mContext).updateTestDatas(mSubjectList);

		return totalScore;
	}

	/**
	 * 显示正确答案
	 * 
	 * @param index
	 */
	public void showCorrectAnswer(int index) {
		mPagerList.get(index).showDetails();
	}

	/**
	 * 显示用户答案
	 * 
	 * @param index
	 */
	public void showUserAnswer(int index) {
		mPagerList.get(index).showUserAnswer();
	}
}
