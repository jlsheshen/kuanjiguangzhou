package com.edu.basicaccountingforguangzhou.view;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicDataDao;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.model.SubjectModel;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 多项选择题 视图
 * 
 * @author lucher
 * 
 */
public class SubjectMultiSelectView extends BaseScrollView implements OnClickListener, OnCheckedChangeListener {

	private static final String TAG = "SubjectMultiSelectView";

	/**
	 * 多选题对应的选项，最多有五个选项
	 */
	private static final String[] OPTIONS = { "A", "B", "C", "D", "E" };

	/**
	 * 问题，正确答案文本控件
	 */
	private TextView tvQestion, tvAnswer;

	// 解析
	private TextView tvAnalysis;

	/**
	 * 提交本题按钮
	 */
	private Button btnSubmit;

	/**
	 * 发送成绩按钮
	 */
	// private Button btnSend;

	/**
	 * 选项容器
	 */
	private LinearLayout llOptionContent;
	// 题目类型，点击时弹出答题卡
	private TextView tvSubjectType;

	private RelativeLayout layout;
	/**
	 * 处理延时自动翻页
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mAutoJumpNextListener != null) {
				mAutoJumpNextListener.autoJumpNext();
			}
		}
	};

	public SubjectMultiSelectView(Context context, TestData data, int testMode) {
		super(context, data, testMode);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_subject_multi_select, this);
		init(mData);
		refreshAnswerState();
	}

	/**
	 * 初始化控件
	 * 
	 * @param
	 */
	private void init(SubjectBasicData data) {
		layout = (RelativeLayout) this.findViewById(R.id.layout_parent);
		layout.setOnClickListener(this);
		tvQestion = (TextView) this.findViewById(R.id.tv_question);
		tvAnswer = (TextView) this.findViewById(R.id.tv_answer);
		llOptionContent = (LinearLayout) this.findViewById(R.id.ll_option_content);
		btnSubmit = (Button) this.findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		tvSubjectType = (TextView) this.findViewById(R.id.tv_subject_type);
		tvSubjectType.setOnClickListener(this);
		tvSubjectType.setText("错误" + mTestData.getErrorCount() + "次");
		tvAnalysis = (TextView) this.findViewById(R.id.tv_analysis);

		// tvQestion.setText(data.getIndexName() + ". " + data.getQuestion());

		String[] options = data.getOption().split(">>>");// 选项
		String[] rightAns = data.getAnswer().split(">>>");// 正确答案
		String rightStr = "";
		for (int i = 0; i < options.length; i++) {
			for (int j = 0; j < rightAns.length; j++) {
				if (options[i].substring(0, options[i].indexOf(".")).equals(rightAns[j])) {
					rightStr += (char) (i + 65);
					break;
				}
			}
		}

		tvAnswer.setText("正确答案：" + rightStr);
		tvAnalysis.setText("解析：" + data.getAnalysis());

		parseOption(data.getOption());
	}

	/**
	 * 按照固定格式解析答案选项，并显示在选项里 此处根据>>>解析
	 * 
	 * @param option
	 */
	private void parseOption(String option) {
		if (testMode == TEST_MODE_TEST) {// 乡试考试没有提交按钮
			btnSubmit.setVisibility(View.GONE);
		}

		String[] options = option.split(">>>");
		int size = options.length;
		if (size > OPTIONS.length) {
			return;
		}
		// 动态加入选项checkbox
		for (int i = 0; i < size; i++) {
			CheckBox cbOption = (CheckBox) View.inflate(getContext(), R.layout.option_checkbox, null);
			cbOption.setText((char) (i + 65) + options[i].substring(options[i].indexOf("."), options[i].length()));
			cbOption.setTag(options[i].substring(0, options[i].indexOf(".")));
			cbOption.setOnCheckedChangeListener(this);
			llOptionContent.addView(cbOption);
		}
	}

	/**
	 * 更新正确答案的显示状态以及用户的选择状态，普通模式下选择后就显示正确答案，乡试模式查看详细的时候显示正确答案
	 */
	private void refreshAnswerState() {
		String userAnswer = mData.getUserAnswer();
		// String remark = mData.getRemark();// 为1代表该题已经提交，为0表示还未提交
		int state = mTestData.getState();// 答题状态 0未答，1/2正误，3未完成
		Log.d(TAG, "userAnswer:" + userAnswer + "," + userAnswer.equals(""));

		// if (testMode == TEST_MODE_NORMAL) {
		// if (state == 1 || state == 2) {// 已完成
		// showCorrectAnswer(mData.isRight());
		// tvSubjectType.setVisibility(View.VISIBLE);
		// disableOption();
		// } else {// 未完成或是尚未做
		// // btnSend.setVisibility(View.GONE);
		// btnSubmit.setVisibility(View.VISIBLE);
		// }
		// }

		if (testMode == TEST_MODE_NORMAL) {
			if (state == 1 || state == 2) {// 已完成 // 用户选择答案后显示正确答案，且不能进行修改
				showCorrectAnswer(mData.isRight());
				tvSubjectType.setVisibility(View.GONE);
				disableOption();
			} else {// 未完成或是尚未做
				// btnSend.setVisibility(View.GONE);
				btnSubmit.setVisibility(View.VISIBLE);
			}
		} else {
			showCorrectAnswer(mData.isRight());
			tvSubjectType.setVisibility(View.VISIBLE);
			disableOption();
		}

		// 设置指定答案的按钮为选中状态
		int count = llOptionContent.getChildCount();
		for (int i = 0; i < count; i++) {
			CheckBox view = (CheckBox) llOptionContent.getChildAt(i);
			String tag = (String) view.getTag();
			if (userAnswer.contains(tag)) {
				view.setChecked(true);
			}
		}
		if (userAnswer.equals("")) {
			userAnswer = "";
		}
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
		int count = llOptionContent.getChildCount();
		for (int i = 0; i < count; i++) {
			llOptionContent.getChildAt(i).setEnabled(false);
		}

		btnSubmit.setVisibility(View.GONE);
	}

	/**
	 * 获取用户选择的答案,//按照字母顺序排序
	 * 
	 * @return
	 */
	private String getUserAnswer() {
		StringBuilder builder = new StringBuilder();
		int count = llOptionContent.getChildCount();
		for (int i = 0; i < count; i++) {
			CheckBox view = (CheckBox) llOptionContent.getChildAt(i);
			if (view.isChecked()) {
				String tag = (String) view.getTag();
				builder.append(tag + ">>>");
			}
		}
		String s = builder.toString();
		if (!s.equals("")) {
			String buildersString = s.substring(s.length() - 3, s.length());
			if (buildersString.equals(">>>")) {
				String sb = s.substring(0, s.length() - 3);
				return sb;
			} else {

				return builder.toString(); // StringFormatUtil1.sortAnswer(builder.toString());
			}
		}
		return "";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_parent:
			handler.removeMessages(0);
			break;
		case R.id.btn_submit:
			String answer = getUserAnswer();
			Log.e(TAG, "answer是" +answer );

			if (answer.equals("")) {
				answer = "-1";
			}
			handleOnClick(answer);
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 300);
			// btnSend.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void reset() {
		mData.setUserAnswer("");
		// SubjectModel.getInstance(getContext()).updateRemark(mData.getId(),
		// "0");
		int count = llOptionContent.getChildCount();
		for (int i = 0; i < count; i++) {
			llOptionContent.getChildAt(i).setEnabled(true);
			((CheckBox) llOptionContent.getChildAt(i)).setChecked(false);
		}
		tvAnswer.setVisibility(View.GONE);
		tvAnalysis.setVisibility(View.GONE);
		btnSubmit.setVisibility(View.VISIBLE);
		// btnSend.setVisibility(View.GONE);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println(isChecked + "-----------");

		mData.setDone(true);
		String answer = getUserAnswer();
		if (answer.equals("")) {
			answer = "";
		} else {
			if (mTestData.getState() == 0) { // 已经打完的 就不改状态了
				// 状态标记为未完成
				TestDataModel.getInstance(getContext()).updateUnFinishState(mTestData.getId(), 3);
			}
			// 将答案实时存入数据库
			SubjectModel.getInstance(getContext()).updateUserAnswer(mData.getId(), answer);
		}
		gradeAnswer(answer);
	}

}
