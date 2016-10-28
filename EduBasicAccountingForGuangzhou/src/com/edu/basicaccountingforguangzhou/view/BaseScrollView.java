package com.edu.basicaccountingforguangzhou.view;

import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.model.SubjectModel;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.library.common.PreferenceHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * 自定义题型视图的基类
 * 
 * @author lucher
 * 
 */
public abstract class BaseScrollView extends RelativeLayout {

	/**
	 * 答题模式
	 */
	public static final int TEST_MODE_NORMAL = 1;
	/**
	 * 查看答案模式
	 */
	public static final int TEST_MODE_TEST = 2;

	/**
	 * 题目数据保存
	 */
	protected TestData mTestData;
	protected SubjectBasicData mData;

	// preference的帮助类
	protected PreferenceHelper preHelper;
	// // 信息设置对话框
	// protected InfoSetDialog infoDialog;

	// /**
	// * 发送考试成绩
	// */
	// protected OnLineSendSocreManager sendManager = new
	// OnLineSendSocreManager().newInstance();
	// /**
	// * 发送成绩组织的数据
	// */
	// protected List<TopicResult> mResults = new ArrayList<TopicResult>();

	protected String sendExamIdStr;

	/**
	 * 测试的模式，取值为TEST_MODE_NORMAL，TEST_MODE_TEST
	 */
	protected int testMode;
	/**
	 * 视图监听
	 */
	protected SubjectViewListener mListener;
	// 判断点击监听
	protected AutoJumpNextListener mAutoJumpNextListener;

	public BaseScrollView(Context context) {
		super(context);
	}

	public BaseScrollView(Context context, TestData data, int testMode) {
		super(context);
		mTestData = data;
		mData = (SubjectBasicData) mTestData.getData();
		this.testMode = testMode;
	}

	public BaseScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 更改数据库表TB_TEST中单多判答题状态
	 */
	private void updateState(String answer) {
		//Log.e("得分专用Log", "两个answer" +answer + "-------" + mData.getAnswer() );

		if (answer.equals(mData.getAnswer())) {
		//	Log.e("得分专用Log", "正确");

			
			// 1是正确
			mTestData.setState(1);
			mData.setRight(true);
			mData.setuScore(mData.getScore());
			TestDataModel.getInstance(getContext()).updateStateAndErrorCount(mTestData.getId(), 1, mTestData.getErrorCount());
			//Log.e("得分专用Log", "正确得分" + mData.getScore());

			SubjectModel.getInstance(getContext()).cleanUserAnswerAndUscore(mData.getId(), answer, (int) mData.getScore(), true);
		} else {
			//Log.e("得分专用Log", "错误");

			// 2是错误
			mTestData.setState(2);
			mData.setRight(false);
			mTestData.setErrorCount(mTestData.getErrorCount() + 1);
			TestDataModel.getInstance(getContext()).updateStateAndErrorCount(mTestData.getId(), 2, mTestData.getErrorCount());
			SubjectModel.getInstance(getContext()).cleanUserAnswerAndUscore(mData.getId(), answer, 0, false);
		}
	}

	/**
	 * 处理答案被选择的事件
	 * 
	 * @param answer
	 */
	protected void handleOnClick(String answer) {
		if (testMode != TEST_MODE_TEST) {// 选择答案后则显示正确答案，且不能进行修改,及时判分
			// 更新数据库答题状态
			updateState(answer);
			showCorrectAnswer(answer.equals(mData.getAnswer()));
			disableOption();
			gradeAnswer(answer);
		} else {
			gradeAnswer(answer);
		}
	};

	/**
	 * 对当前答案进行判分
	 * 
	 * @param answer
	 */
	protected void gradeAnswer(String answer) {
		if (testMode == TEST_MODE_NORMAL) {
			gradeNormalAnswer(answer);
		} else if (testMode == TEST_MODE_TEST) {
			gradeTestAnswer(answer);
		}
	}

	/**
	 * 显示正确答案
	 */
	protected abstract void showCorrectAnswer(boolean correct);

	/**
	 * 不能再选择
	 */
	protected abstract void disableOption();

	/**
	 * 重置
	 */
	public abstract void reset();

	/**
	 * 测试模式进行判分
	 * 
	 * @param answer
	 */
	private void gradeTestAnswer(String answer) {
		// mData.setUserAnswer(answer);
		// 逻辑待定
	}

	/**
	 * 对普通模式进行评分
	 * 
	 * @param answer
	 */
	private void gradeNormalAnswer(String answer) {// 单选、判断：每题1分；多选：每题1.5分；
		// 更新mData里以及数据库里的用户答案
		mData.setUserAnswer(answer);
		SubjectModel.getInstance(getContext()).updateUserAnswer(mData.getId(), answer);
		// answer.equals(mData.getAnswer())
		if (answer.equals("")) {
			mData.setRemark("0");
		} else {
			mData.setRemark("1");
		}
		SubjectModel.getInstance(getContext()).updateRemark(mData.getId(), mData.getRemark());
	}

	/**
	 * 设置跳转监听
	 * 
	 * @param listener
	 */
	public void setAutoJumpNextListener(AutoJumpNextListener listener) {
		mAutoJumpNextListener = listener;
	}

	/**
	 * // * 设置答题监听 // * // * @param listener //
	 */
	public void setSubjectViewListener(SubjectViewListener listener) {
		mListener = listener;
	}
}