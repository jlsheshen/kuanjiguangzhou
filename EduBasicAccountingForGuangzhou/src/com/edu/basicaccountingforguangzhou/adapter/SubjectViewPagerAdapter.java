package com.edu.basicaccountingforguangzhou.adapter;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.R.integer;
import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.SubjectBillDataDao;
import com.edu.basicaccountingforguangzhou.data.TestBillData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.data.TestDataDao;
import com.edu.basicaccountingforguangzhou.fragment.SubjectContentPagerFragment;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.subject.SubjectListener;
import com.edu.basicaccountingforguangzhou.subject.SubjectState;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BaseElementInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.listener.WhileIsBillListener;
import com.edu.basicaccountingforguangzhou.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.testbill.data.SubjectTestDataDao;
import com.edu.basicaccountingforguangzhou.view.AutoJumpNextListener;
import com.edu.basicaccountingforguangzhou.view.SubjectViewListener;
import com.edu.library.util.ToastUtil;

import android.content.ContentValues;
import android.content.Context;
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
	
	private SubjectListener subjectListener;
	/**
	 * 自动跳转监听
	 */
	private AutoJumpNextListener mAutoListener;
	/**
	 * 答题模式，与BaseScrollView里的testMode对应
	 */
	private int testMode;
	
	private Context mContext;
	
	
	/**
	 * 禁止左右滑动的监听
	 * 
	 */
	private WhileIsBillListener whileIsBillListener;

	public SubjectViewPagerAdapter(FragmentManager childFragmentManager, List<TestData> datas,SubjectListener subjectListener, AutoJumpNextListener autoListener, SubjectViewListener listener, int testMode) {
		super(childFragmentManager);
		mListener = listener;
		mAutoListener = autoListener;
		this.subjectListener = subjectListener;
		this.testMode = testMode;
		if (datas != null) {
			for (TestData subject : datas) {
				mPagerList.add(null);
				mSubjectList.add(subject);
			}
		}
	}
	
	

	public void setWhileIsBillListener(WhileIsBillListener whileIsBillListener) {
		this.whileIsBillListener = whileIsBillListener;
	}
	





	public void setmContext(Context mContext) {
		this.mContext = mContext;
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
			mPagerList.set(position, SubjectContentPagerFragment.newInstance(mSubjectList.get(position),subjectListener ,mAutoListener, mListener, testMode));
		}
		
//		if(mSubjectList.get(position).getSubjectType() == Constant.SUBJECT_TYPE_BILL){
//			//whileIsBillListener.vpNoTouch();
//		}
		
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
				pager.reset(false);
		}
	//	SubjectTestDataDao.getInstance(mContext).updateTestDatas(mSubjectList);

	}
	/**
	 * 重置单据题
	 */
	public void resetBill(int pos,boolean Bill) {
		for (int i = pos; i < mPagerList.size(); i++) {
			resetSubject(i,Bill);
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
			pager.reset(false);
	}
	

	/**
	 * 重置指定索引的题
	 * 
	 * @param index
	 */
	private void resetSubject(int index,boolean bill) {
		mSubjectList.get(index).setState(SubjectState.STATE_INIT);
		// 界面重置
		SubjectContentPagerFragment pager = mPagerList.get(index);
		if (pager != null) {
			pager.reset(bill);
		}
		Log.e(TAG, "resetSubject"  + mSubjectList.get(index).getId() + "---" + index + "---" + mSubjectList.get(index).getSubjectId()  + "---" + mSubjectList.size());
		// 数据重置
		mSubjectList.get(index).getBillData().setuAnswer(null);
		mSubjectList.get(index).getBillData().setuScore(0);
		if (mSubjectList.get(index).getSubjectType() ==Constant.SUBJECT_TYPE_BILL) {// 对于单据题，需要把每个空的内容清空，需要把用户印章答案清空
			 mSubjectList.get(index).getBillData().setuSigns(null);
			List<BaseElementInfo> elements = (List<BaseElementInfo>) mSubjectList.get(index).getBillData().getTemplate().getElementDatas();
			for (BaseElementInfo element : elements) {
				if (element instanceof BlankInfo) {
					((BlankInfo) element).setuAnswer(null);
					((BlankInfo) element).setRight(false);
				}
			}
		}
		SubjectTestDataDao.getInstance(mContext).updateTestData(mSubjectList.get(index));

	}
	/**
	 * 提交
	 * 
	 * @return 得分
	 */
	public float submit( int pos) {	
	SubjectContentPagerFragment pager = mPagerList.get(pos);
	if (pager != null) {
		pager.submit();
	}
	float totalScore = mSubjectList.get(pos).getBillData().getuScore();
//	//判断正误
//			if (mSubjectList.get(pos).getSubjectData().getScore() == mSubjectList.get(pos).getuScore()) {
//				mSubjectList.get(pos).setState(SubjectState.STATE_CORRECT);
//			} else {
//				mSubjectList.get(pos).setState(SubjectState.STATE_WRONG);
//			}
			mSubjectList.get(pos).setState(SubjectState.STATE_FINISHED);
		SubjectTestDataDao.getInstance(mContext).updateTestData(mSubjectList.get(pos));
		if (mSubjectList.get(pos).getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
			
		
		if (totalScore < mSubjectList.get(pos).getBillData().getSubjectData().getScore()) {
		//	SubjectBillData sub = (SubjectBillData) SubjectTestDataDao.getInstance(mContext).getDataById(Integer.valueOf(mSubjectList.get(pos).getSubjectId()));
			
			ContentValues contentValues = new ContentValues();
			contentValues.put("ERROR_COUNT", mSubjectList.get(pos).getBillData().getSubjectData().getErrorCount() + 1);
			SubjectBillDataDao.getInstance(mContext,Constant.DATABASE_NAME).updateData(Integer.valueOf(mSubjectList.get(pos).getSubjectId()), contentValues);	
		}}

	return totalScore;
	}
	
	/**
	 * 获取指定数据
	 * 
	 * @param position
	 * @return
	 */
	public TestData getData(int position) {
		return mSubjectList.get(position);
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

	/**
	 * 
	 * 显示用户答案
	 * 
	 * @param index
	 */
	public void showUserAnswer(int index) {
		mPagerList.get(index).showUserAnswer();
	}
	/**
	 * 显示正确答案
	 * 
	 * @param index
	 */
	public void showCorrectAnswer(int index) {
		mPagerList.get(index).showDetails();
	}
	
	
	
}
