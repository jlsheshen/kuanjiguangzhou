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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 单项选择题 视图
 * 
 * @author lucher
 * 
 */
public class SubjectSingleSelectView extends BaseScrollView implements OnClickListener {

	private static final String TAG = "SubjectS ingleSelectView";
	// * 单选视图对应的数据
	// private QuestionEntity selectionEntity;
	// 题干* 正确答案
	// private TextView tvQuestion, tvResulte, tvExplain;
	// // 选择框
	// private RadioGroup radioGroup;
	/**
	 * 问题，正确答案文本控件
	 */
	private TextView tvQestion, tvAnswer;
	// 解析
	private TextView tvAnalysis;

	/**
	 * 选项组
	 */
	private RadioGroup rgOption;

	/**
	 * 四个选项按钮
	 */
	private RadioButton rbA, rbB, rbC, rbD;
	// 题目类型，点击时弹出答题卡
	private TextView tvSubjectType;

	// 考试信息
	// private TestRule testRule;
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
	private Context mContext;
	private RelativeLayout layout;

	public SubjectSingleSelectView(Context context, TestData data, int testMode) {
		super(context, data, testMode);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		// inflater.inflate(R.layout.view_singleselection, this);
		inflater.inflate(R.layout.view_subject_single_select, this);
		init(mData);
		refreshAnswerState(mData);
	}

	/**
	 * 初始化控件
	 * 
	 * @param mData
	 */
	private void init(SubjectBasicData data) {
		layout = (RelativeLayout) this.findViewById(R.id.layout_parent);
		layout.setOnClickListener(this);
		tvSubjectType = (TextView) this.findViewById(R.id.tv_subject_type);
		tvSubjectType.setOnClickListener(this);
		tvSubjectType.setText("错误" + mTestData.getErrorCount() + "次");

		tvQestion = (TextView) this.findViewById(R.id.tv_question);
		tvAnswer = (TextView) this.findViewById(R.id.tv_answer);
		tvAnalysis = (TextView) this.findViewById(R.id.tv_analysis);

		rgOption = (RadioGroup) this.findViewById(R.id.rg_option);

		rbA = (RadioButton) this.findViewById(R.id.rb_A);
		rbB = (RadioButton) this.findViewById(R.id.rb_B);
		rbC = (RadioButton) this.findViewById(R.id.rb_C);
		rbD = (RadioButton) this.findViewById(R.id.rb_D);
		rbA.setOnClickListener(this);
		rbB.setOnClickListener(this);
		rbC.setOnClickListener(this);
		rbD.setOnClickListener(this);

		// tvQestion.setText(data.getSubjectIndex() + ". " +
		// data.getQuestion());
		String[] options = data.getOption().split(">>>");
		for (int i = 0; i < options.length; i++) {
			if (options[i].substring(0, options[i].indexOf(".")).equals(data.getAnswer())) {
				tvAnswer.setText("正确答案：" + (char) (i + 65));
			}
		}
		// tvAnswer.setText("正确答案：" + data.getAnswer());
		tvAnalysis.setText("解析：" + data.getAnalysis());

		parseOption(data.getOption());
	}

	/**
	 * 按照固定格式解析答案选项，并显示在选项里 此处根据>>>解析
	 * 
	 * @param option
	 */
	private void parseOption(String option) {
		String[] options = option.split(">>>");
		if (options.length != 4) {
			// ToastUtil.showDbProblem(getContext(), "SingleSelect-id:" +
			// mData.getId() + "," + option);
			return;
		}

		rbA.setText("A" + options[0].substring(options[0].indexOf("."), options[0].length()));
		rbA.setTag(options[0].substring(0, options[0].indexOf(".")));
		rbB.setText("B" + options[1].substring(options[1].indexOf("."), options[1].length()));
		rbB.setTag(options[1].substring(0, options[1].indexOf(".")));
		rbC.setText("C" + options[2].substring(options[2].indexOf("."), options[2].length()));
		rbC.setTag(options[2].substring(0, options[2].indexOf(".")));
		rbD.setText("D" + options[3].substring(options[3].indexOf("."), options[3].length()));
		rbD.setTag(options[3].substring(0, options[3].indexOf(".")));
	}

	/**
	 * 更新正确答案的显示状态以及用户的选择状态，普通模式下选择后就显示正确答案，乡试模式查看详细的时候显示正确答案
	 */
	private void refreshAnswerState(SubjectBasicData data) {
		String userAnswer = mData.getUserAnswer();
		Log.d(TAG, "userAnswer:" + userAnswer + "," + userAnswer.equals(""));
		int state = mTestData.getState();// 答题状态 0未答，1/2正误，3未完成
		int sendState = mTestData.getSendState();// 发送状态 1已发送，0未发送
		Log.e(TAG, "showCorrectAnswer:" + mData.isRight() + "," + mData.getId() + "---" + testMode);

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
		String[] tempAns = data.getOption().split(">>>");
		if (userAnswer.equals(tempAns[0].substring(0, tempAns[0].indexOf(".")))) {
			rbA.setChecked(true);
		} else if (userAnswer.equals(tempAns[1].substring(0, tempAns[1].indexOf(".")))) {
			rbB.setChecked(true);
		} else if (userAnswer.equals(tempAns[2].substring(0, tempAns[2].indexOf(".")))) {
			rbC.setChecked(true);
		} else if (userAnswer.equals(tempAns[3].substring(0, tempAns[3].indexOf(".")))) {
			rbD.setChecked(true);
		} else {
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
		rbA.setEnabled(false);
		rbB.setEnabled(false);
		rbC.setEnabled(false);
		rbD.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_parent:
			handler.removeMessages(0);

			break;
		default:
			String answer = v.getTag().toString();
			Log.e(TAG, "answer是" +answer );
			handleOnClick(answer);
			SubjectModel.getInstance(getContext()).updateRemark(mData.getId(), "1");
			SubjectBasicData data = (SubjectBasicData) SubjectBasicDataDao.getInstance(getContext()).getDataById(mData.getId());
			if (data.getRemark().equals("1")) {
				mData.setDone(true);
			}
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 300);
			break;
		}
	}

	@Override
	public void reset() {
		mData.setUserAnswer("");
		rbA.setChecked(false);
		rbB.setChecked(false);
		rbC.setChecked(false);
		rbD.setChecked(false);
		rbA.setEnabled(true);
		rbB.setEnabled(true);
		rbC.setEnabled(true);
		rbD.setEnabled(true);
		tvAnswer.setVisibility(View.GONE);
		tvAnalysis.setVisibility(View.GONE);
		// 需要将对应的数据库中的内容也修改掉
		TestDataModel.getInstance(getContext()).updateAllState(mTestData.getId(), 0);
		SubjectModel.getInstance(getContext()).cleanUserAnswerAndUscore(mData.getId(), "", 0, false);
	}

	// /**
	// * 初始化控件
	// *
	// * @param mData
	// */
	// private void init(SubjectBasicData data) {
	// tvQuestion = (TextView) findViewById(R.id.tv_qustion);
	// tvExplain = (TextView) findViewById(R.id.tv_explian);
	// tvQuestion.setText(data.getIndexName() + ". " + data.getQuestion());
	// radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	// tvResulte = (TextView) findViewById(R.id.tv_resulte);
	// tvResulte.setText("【正确答案】：" + data.getAnswer());
	// ((Button) findViewById(R.id.btn_finish)).setOnClickListener(this);
	// tvExplain.setText("解析:\n" + data.getAnalysis());
	//
	// parseOption(data.getOption());
	// }
	//
	// /**
	// * 按照固定格式解析答案选项，并显示在选项里 此处根据>>>解析
	// *
	// * @param option
	// */
	// private void parseOption(String option) {
	// String[] options = option.split(">>>");
	// radioGroup.removeAllViews();
	// String[] strChoice = option.split(">>>");
	// String[] choice = { "A", "B", "C", "D", "E", "F" };
	// for (int i = 0; i < strChoice.length; i++) {
	// RadioButton radioButton = (RadioButton) View.inflate(getContext(),
	// R.layout.singleselection_view_item, null);
	// radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// radioButton.setText(choice[i] + "." + strChoice[i]);
	// radioButton.setTag(choice[i]);
	// radioButton.setOnCheckedChangeListener(this);
	// radioGroup.addView(radioButton);
	// }
	// }
	//
	// /**
	// * 更新正确答案的显示状态以及用户的选择状态，普通模式下选择后就显示正确答案，乡试模式查看详细的时候显示正确答案
	// */  
	// private void refreshAnswerState(SubjectBasicData data) {
	// String userAnswer = mData.getUserAnswer();
	// Log.d(TAG, "userAnswer:" + userAnswer + "," + userAnswer.equals(""));
	// int state = mTestData.getState();// 答题状态 0未答，1/2正误，3未完成
	//
	// if (testMode == TEST_MODE_NORMAL) {
	// if (state == 1 || state == 2) {// 已完成 // 用户选择答案后显示正确答案，且不能进行修改
	// showCorrectAnswer(mData.isRight());
	//
	// disableOption();
	// }
	//
	// }
	// if (!"".equals(mData.getUserAnswer())) {
	// for (int i = 0; i < radioGroup.getChildCount(); i++) {
	// RadioButton rbtn = (RadioButton) radioGroup.getChildAt(i);
	// if (rbtn.getTag().toString().equals(mData.getUserAnswer())) {
	// rbtn.setChecked(true);
	// }
	// }
	// }
	// // handleOnClick(userAnswer);
	// }
	//
	// @Override
	// public void showCorrectAnswer(boolean correct) {
	// tvResulte.setVisibility(View.VISIBLE);
	// tvExplain.setVisibility(View.VISIBLE);
	// if (correct) {
	// tvResulte.setTextColor(Color.parseColor("#6766cc"));
	// } else {
	// tvResulte.setTextColor(Color.parseColor("#cc0000"));
	// }
	// }
	//
	// @Override
	// public void disableOption() {
	// for (int i = 0; i < radioGroup.getChildCount(); i++) {
	// RadioButton rButton = (RadioButton) radioGroup.getChildAt(i);
	//
	// rButton.setEnabled(false);
	// }
	//
	// }
	//
	// @Override
	// public void onClick(View v) {
	// for (int i = 0; i < radioGroup.getChildCount(); i++) {
	// RadioButton rButton = (RadioButton) radioGroup.getChildAt(i);
	// if (rButton.getTag().toString().equals(mData.getAnswer().trim())) {
	// if (mData.isRight()) {
	// rButton.setBackgroundResource(R.drawable.bg_p_right);// bg_p_right
	// } else {
	// rButton.setBackgroundResource(R.drawable.bg_right);
	// }
	// }
	// rButton.setEnabled(false);
	// }
	// int color = mData.isRight() ? R.color.right : R.color.error;
	// tvResulte.setTextColor(getResources().getColor(color));
	//
	// String answer = v.getTag().toString();
	// handleOnClick(answer);
	//
	// }
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView, boolean
	// isChecked) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void reset() {
	//
	// }

}
