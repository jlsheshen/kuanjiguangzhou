package com.edu.basicaccountingforguangzhou.view;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.SubjectEntryData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.model.BillDataModel;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.util.PreferenceHelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class FenLuContentView extends RelativeLayout implements OnClickListener {
	// 问题
	private TextView tvQuestion;
	// 问题
	private LinearLayout llyoutQuestion;
	// ,题号
	private RadioGroup rGroupSub;
	// 确定按钮;添加一组
	private Button btnFinish;
	// 正确答案
	private String[] rAnswear;
	// 用户答案
	private StringBuilder myAsw;
	private Context mContext;

	private SubjectEntryData mData;
	private int mTestMode;
	private int mState; // 题目状态 0未答，1正确，2错误
	private TestData mTestData;// 当前题的信息
	// 问题,获得的积分
	private TextView tvScore;
	private List<EntryView> ltQuestionItemView;
	private String[] subjectIds;
	/**
	 * 是否全部正确
	 */
	private int rightStatic = 1;
	// /**
	// * 金额输入控件
	// */
	private KeyboardView keyboard;

	private TextView tvSubjectType;

	public FenLuContentView(Context context, TestData data, int testMode) {
		super(context);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_calculation_secend, this);
		mData = (SubjectEntryData) data.getData();
		mState = data.getState();
		this.mTestMode = testMode;
		mTestData = data;
		ltQuestionItemView = new ArrayList<EntryView>();
		initView();
		refreshState(2);
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		tvQuestion = (TextView) findViewById(R.id.tv_question);
		// tvQuestion.setOnClickListener(this);
		llyoutQuestion = (LinearLayout) findViewById(R.id.llyout_choice);
		rGroupSub = (RadioGroup) findViewById(R.id.sub);
		btnFinish = (Button) findViewById(R.id.btn_finish);
		btnFinish.setOnClickListener(this);
		// tvQuestion.append(Html.fromHtml(mData.getIndexName() + ". " +
		// mData.getQuestion(), imageGetter, null));
		keyboard = (KeyboardView) this.findViewById(R.id.keyboard);
		tvSubjectType = (TextView) findViewById(R.id.tv_subject_type);
		tvSubjectType.setText("错误" + mTestData.getErrorCount() + "次");
	}

	/**
	 * 图片解析类
	 */
	protected ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			try {
				int id = getContext().getResources().getIdentifier(source, "drawable", getContext().getPackageName());
				// 根据id从资源文件中获取图片对象
				Drawable d = getContext().getResources().getDrawable(id);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			} catch (Exception e) {
				return getContext().getResources().getDrawable(R.drawable.ic_launcher);
			}
		}
	};

	/**
	 * 更新状态
	 */
	private void refreshState(int flag) {
		// tvQuestion.setText(mData.getIndexName() + ". " +
		// mData.getQuestion());

		rGroupSub.removeAllViews();
		llyoutQuestion.removeAllViews();
		subjectIds = mTestData.getSubjectId().split(">>>");
		String[] questions = mData.getQuestion().split(">>>");
		tvQuestion.setText(Html.fromHtml(questions[1], imageGetter, null));
		for (int index = 0; index < subjectIds.length; index++) {
			RadioButton rbtn = (RadioButton) View.inflate(getContext(), R.layout.rbtn_cal_sencend, null);
			rbtn.setText("第" + (index + 1) + "题");
			rbtn.setId(index);
			rbtn.setOnClickListener(this);
			rGroupSub.addView(rbtn);
			EntryView entryView = new EntryView(mContext);

			// 模式1答题模式、2模式显示答案
			if (mState == 0 && mTestMode == 1) {
				entryView.setKeyboard(keyboard);
				entryView.setOnClickListener(this);
				entryView.init(Integer.valueOf(subjectIds[index]), EntryView.MODE_INITIAL, false);
				ltQuestionItemView.add(entryView);

			} else if (mTestMode == 2) {
				entryView.setKeyboard(keyboard);
				entryView.setOnClickListener(this);
				entryView.init(Integer.valueOf(subjectIds[index]), EntryView.MODE_JUDE_ANSWER, true);
				ltQuestionItemView.add(entryView);
			} else if (mState != 0) {
				entryView.setKeyboard(keyboard);
				entryView.setOnClickListener(this);
				entryView.init(Integer.valueOf(subjectIds[index]), EntryView.MODE_JUDE_ANSWER, true);
				ltQuestionItemView.add(entryView);
			}
		}
		llyoutQuestion.addView(ltQuestionItemView.get(0));
		((RadioButton) rGroupSub.getChildAt(0)).setChecked(true);
		if (mTestMode == 2) {
			btnFinish.setVisibility(View.GONE);
			tvSubjectType.setVisibility(View.VISIBLE);
		} else {
			if (flag != 1) {
				if (subjectIds.length == 1) {
					btnFinish.setVisibility(View.VISIBLE);
				}
			}
			tvSubjectType.setVisibility(View.GONE);
		}

		// if (mTestMode == BaseScrollView.TEST_MODE_NORMAL) {// 普通模式
		// if (mState == 0) {// 未做
		// EntryView entryView = new EntryView(mContext);
		// entryView.init(mData, EntryView.MODE_INITIAL, false);
		// } else if (mState == 1 || mState == 2) {// 正确&错误
		// EntryView entryView = new EntryView(mContext);
		// entryView.init(mData, EntryView.MODE_JUDE_ANSWER, true);
		// // entryView.judgeAnswer();
		// int score = (int) entryView.judgeAnswerSubject();
		// if (score >= 0) {
		// tvScore.setText(score + "分");
		// tvScore.setVisibility(View.VISIBLE);
		// }
		// }
		// }
		// if (mData.getPicName() == null || mData.getPicName().equals("")) {
		// gifRope.setVisibility(View.GONE);
		// } else {
		// if
		// (PreferenceHelper.getInstance(getContext()).isFirstTime(PreferenceHelper.KEY_ROPE_INDICATOR))
		// {// 如果第一次进入该界面，则显示提示图片
		// showIndicator();
		// }
		// gifRope.setVisibility(View.VISIBLE);
		// showPicture(mData.getPicName());
		// }
		if (keyboard.getVisibility() == View.VISIBLE) {
			keyboard.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示图片
	 * 
	 * @param picName
	 */
	private void showPicture(String picName) {
		List<String> picList = new ArrayList<String>();
		picList = getPicList(picName);
		if (picList != null && picList.size() > 0) {
			// picDialog = new FillInPictureDialog(getContext(), picList);
		}
	}

	/**
	 * 截取图片名加入list
	 * 
	 * @param picName
	 * @return
	 */
	private List<String> getPicList(String picName) {
		List<String> picList = new ArrayList<String>();
		String[] picNames = picName.split("&");
		for (int i = 0; i < picNames.length; i++) {
			picList.add(picNames[i]);
		}
		return picList;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_finish:
			judeAnswer();
			// btnSend.setVisibility(View.VISIBLE);
			break;

		default:
			subDo(v);
			break;
		}
	}

	/**
	 * 题干点击处理
	 * 
	 * @param v
	 */
	private void subDo(View v) {
		llyoutQuestion.removeAllViews();
		llyoutQuestion.addView(ltQuestionItemView.get(v.getId()));
		if (v.getId() == subjectIds.length - 1 && mState == 0 && mTestMode != 2) {
			btnFinish.setVisibility(View.VISIBLE);
		} else {
			btnFinish.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示图片提示
	 */
	private void showIndicator() {
		// rlIndicator.setVisibility(View.VISIBLE);
		PreferenceHelper.getInstance(getContext()).setFirstTimeFalse(PreferenceHelper.KEY_ROPE_INDICATOR);
	}

	// /**
	// * 切换计算器的可见性
	// */
	// private void switchCalculator() {
	// if (calculator.getVisibility() == View.VISIBLE) {
	// calculator.setVisibility(View.GONE);
	// } else {
	// calculator.setVisibility(View.VISIBLE);
	// }
	// }
	/**
	 * 更新单据的状态
	 * 
	 * @param state
	 *            状态
	 */
	private void updateTestState(int state) {
		TestDataModel.getInstance(getContext()).updateState(mTestData.getId(), state);
	}

	/**
	 * 更新分录表的USCORE和IS_CORRECT
	 * 
	 * @param
	 */
	private void updateBillScore(int score, int isCorrect) {
		BillDataModel.getInstance(getContext()).updataUScoreAndIsCorrect(mData.getId(), score, isCorrect);
	}

	/**
	 * 更新分录表的uBorrow和uLoan
	 * 
	 * @param
	 */
	private void updateBillUborrowAndUloan(String uBorrow, String uLoan) {
		BillDataModel.getInstance(getContext()).updateUserAnswer(mData.getId(), uBorrow, uLoan);
	}

	/**
	 * 判断答案
	 */
	private void judeAnswer() {
		for (int i = 0; i < subjectIds.length; i++) {
			ltQuestionItemView.get(i).init(Integer.valueOf(subjectIds[i]), EntryView.MODE_SHOW_USER_ANSWER, true);
			ltQuestionItemView.get(i).saveUserAnswerToDb();
			mState = (int) ltQuestionItemView.get(i).judgeAnswerSubject(Integer.valueOf(subjectIds[i])) == ltQuestionItemView.get(i).FULL_SCORE ? 1 : 2;
			mTestData.setState(mState);
			if (mState == 2) {
				rightStatic = 2;
			}
		}
		TestDataModel.getInstance(mContext).updateState(Integer.valueOf(mTestData.getId()), rightStatic);
		btnFinish.setVisibility(View.GONE);
		refreshState(1);
	}

	public void reset() {

		mData.setBorrowUser("");
		mData.setLoanUser("");
		mData.setuScore(0);
		mTestData.setState(0);
		mState = mTestData.getState();
		mTestData.setSendState(0);
		restart();
		btnFinish.setVisibility(View.VISIBLE);
	}

	/**
	 * 重来
	 */
	private void restart() {
		updateTestState(0);// 清答题状态 置零
		updateBillScore(0, 0);// 清空分数和正误
		updateBillUborrowAndUloan("", "");// 借方，贷方 清空
		refreshState(2);
	}

}
