package com.edu.basicaccountingforguangzhou.view;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.library.common.PreferenceHelper;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 判断题 视图
 * 
 * @author lucher
 * 
 */
public class SubjectJudgeView extends BaseScrollView implements OnClickListener {

	private static final String TAG = "SubjectJudgeView";
	/**
	 * 问题，正确答案文本控件
	 */
	private TextView tvQestion, tvAnswer;
	// 解析
	private TextView tvAnalysis;

	/**
	 * 对，错选项按钮
	 */
	private RadioButton rbTrue, rbFalse;
	// 题目类型，点击时弹出答题卡
	private TextView tvSubjectType;
	private RelativeLayout layout;

	/**
	 * 发送按钮
	 */
	private Button btnSend;

	/**
	 * 处理延时自动翻页
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (mAutoJumpNextListener != null) {
				mAutoJumpNextListener.autoJumpNext();
			}
		}
	};

	public SubjectJudgeView(Context context, TestData data, int testMode) {
		super(context, data, testMode);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_subject_judge, this);
		init(mData);
		refreshAnswerState();
	}

	/**
	 * 初始化控件
	 * 
	 * @param mData
	 */
	private void init(SubjectBasicData data) {
		layout = (RelativeLayout) this.findViewById(R.id.layout_parent);
		layout.setOnClickListener(this);
		tvQestion = (TextView) this.findViewById(R.id.tv_question);
		tvAnswer = (TextView) this.findViewById(R.id.tv_answer);
		tvSubjectType = (TextView) this.findViewById(R.id.tv_subject_type);
		tvSubjectType.setOnClickListener(this);
		tvAnalysis = (TextView) this.findViewById(R.id.tv_analysis);
		tvSubjectType.setText("错误" + mTestData.getErrorCount() + "次");
		rbTrue = (RadioButton) this.findViewById(R.id.rb_A);
		rbFalse = (RadioButton) this.findViewById(R.id.rb_B);

		rbTrue.setOnClickListener(this);
		rbFalse.setOnClickListener(this);

		// tvQestion.setText(data.getIndexName() + ". " + data.getQuestion());

		preHelper = PreferenceHelper.getInstance(getContext());

		// 初始化正确答案
		String answer = null;
		if (data.getAnswer().equals("0")) {
			answer = "错";
		} else {
			answer = "对";
		}
		tvAnswer.setText("正确答案：" + answer);
		tvAnalysis.setText("解析：" + data.getAnalysis());
	}

	/**
	 * 更新正确答案的显示状态以及用户的选择状态，普通模式下选择后就显示正确答案，乡试模式查看详细的时候显示正确答案
	 */
	private void refreshAnswerState() {
		String userAnswer = mData.getUserAnswer();
		Log.d(TAG, "userAnswer:" + userAnswer + "," + userAnswer.equals(""));
		int state = mTestData.getState();// 答题状态 0未答，1/2正误，3未完成
		int sendState = mTestData.getSendState();// 发送状态 1已发送，0未发送

		// if (testMode == TEST_MODE_NORMAL) {
		// if (state == 1 || state == 2) {// 已完成 // 用户选择答案后显示正确答案，且不能进行修改
		// showCorrectAnswer(mData.isRight());
		// tvSubjectType.setVisibility(View.VISIBLE);
		// disableOption();
		// }
		// }
		if (testMode == TEST_MODE_NORMAL) {
			if (state == 1 || state == 2) {// 已完成 // 用户选择答案后显示正确答案，且不能进行修改
				showCorrectAnswer(mData.isRight());
				tvSubjectType.setVisibility(View.GONE);
				disableOption();
			}
		} else {
			showCorrectAnswer(mData.isRight());
			tvSubjectType.setVisibility(View.VISIBLE);
			disableOption();
		}

		// 设置指定答案的按钮为选中状态
		if (userAnswer.equals("1")) {
			rbTrue.setChecked(true);
		} else if (userAnswer.equals("0")) {
			rbFalse.setChecked(true);
		} else {
			userAnswer = "";
		}
		// handleOnClick(userAnswer);

	}

	@Override
	public void showCorrectAnswer(boolean correct) {
		tvAnswer.setVisibility(View.VISIBLE);
		tvAnalysis.setVisibility(View.VISIBLE);
		if (correct) {
			tvAnswer.setTextColor(Color.parseColor("#6766cc"));
		} else {
			tvAnswer.setTextColor(Color.parseColor("#cc0000"));
		}
	}

	@Override
	public void disableOption() {
		rbTrue.setEnabled(false);
		rbFalse.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_parent:
			handler.removeMessages(0);
			break;
		case R.id.rb_A:
			rbTrue.setTextColor(getResources().getColor(R.color.wathet_blue));
			rbFalse.setTextColor(Color.BLACK);
			String answerA = v.getTag().toString();
			Log.e(TAG, "answer是" +answerA );

			handleOnClick(answerA);
			// btnSend.setVisibility(View.VISIBLE);
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 300);
			break;
		case R.id.rb_B:
			rbFalse.setTextColor(getResources().getColor(R.color.wathet_blue));
			rbTrue.setTextColor(Color.BLACK);
			String answerB = v.getTag().toString();
			handleOnClick(answerB);
			// btnSend.setVisibility(View.VISIBLE);
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 300);
			break;
		default:
			// String answer = v.getTag().toString();
			// handleOnClick(answer);
			// btnSend.setVisibility(View.VISIBLE);
			// handler.removeMessages(0);
			// handler.sendEmptyMessageDelayed(0, 300);
			break;
		}
	}

	@Override
	public void reset() {
		mData.setUserAnswer("");
		rbTrue.setTextColor(Color.BLACK);
		rbFalse.setTextColor(Color.BLACK);
		rbTrue.setChecked(false);
		rbFalse.setChecked(false);
		rbTrue.setEnabled(true);
		rbFalse.setEnabled(true);
		tvAnswer.setVisibility(View.GONE);
		tvAnalysis.setVisibility(View.GONE);
		btnSend.setVisibility(View.GONE);
		mTestData.setRemark("0");
		mTestData.setSendState(0);
		mTestData.setState(0);
		mData.setUserAnswer("");
		mData.setuScore(0);
		mData.setRight(false);
		// 需要将对应的数据库中的内容也修改掉
		// TestDataModel.getInstance(getContext()).updateStateAndSendState(mTestData.getId(),
		// 0, 0);
		// SubjectModel.getInstance(getContext()).cleanUserAnswerAndUscore(mData.getId(),
		// "", 0, false);
	}

}
