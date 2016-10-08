//package com.edu.basicaccountingforguangzhou.view;
//
//import java.util.ArrayList;
//
//import com.edu.basicaccountingforguangzhou.R;
//
//import android.content.Context;
//import android.text.Html;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
///**
// * 计算题类型三
// * 
// * @author GJT
// *
// */
//public class CalculationThirdView extends BaseScrollView {
//	// 问题
//	private TextView tvQuestion;
//	// 问题
//	private LinearLayout llyoutQuestion;
//	// ,题号
//	private RadioGroup rGroupSub;
//	// 确定按钮;添加一组
//	private Button btnFinish;
//	// 正确答案,每一个小题答案
//	private String[] preRAswear;
//	// 正确答案
//	private String[] rAnswear;
//	// 用户答案
//	private StringBuilder myAsw;
//	private Context mContext;
//
//	public CalculationThirdView(Context context, QuestionEntity cEntity) {
//		super(context, cEntity);
//		mContext = context;
//		this.cEntity = cEntity;
//		View view = View.inflate(mContext, R.layout.view_calculation_secend, this);
//	}
//
//	@Override
//	protected void initResource() {
//		rule = MyApplication.rule;
//		preRAswear = cEntity.getAnswear().split(">>>>");
//		rAnswear = cEntity.getChoice().split("<<<");
//		ltQuestionItemView = new ArrayList<BaseRelativeLayout>();
//		myAsw = new StringBuilder();
//	}
//
//	@Override
//	protected void initWidget() {
//		initConstantWidget();
//		initSubUi();
//	}
//
//	@Override
//	protected void initConstantWidget() {
//		tvQuestion = (TextView) findViewById(R.id.tv_question);
//		llyoutQuestion = (LinearLayout) findViewById(R.id.llyout_choice);
//		rGroupSub = (RadioGroup) findViewById(R.id.sub);
//		btnFinish = (Button) findViewById(R.id.btn_finish);
//		btnFinish.setOnClickListener(this);
//		tvQuestion.append(Html.fromHtml(cEntity.getQuestionNumber() + "." + cEntity.getQuestion(), imageGetter, null));
//		super.initConstantWidget();
//	}
//
//	/**
//	 * 初始化题干题号视图
//	 */
//	private void initSubUi() {
//		rGroupSub.removeAllViews();
//		llyoutQuestion.removeAllViews();
//		for (int index = 0; index < preRAswear.length + rAnswear.length; index++) {
//			RadioButton rbtn = (RadioButton) View.inflate(getContext(), R.layout.rbtn_cal_sencend, null);
//			rbtn.setText("第" + (index + 1) + "题");
//			rbtn.setId(index);
//			rbtn.setOnClickListener(this);
//			rGroupSub.addView(rbtn);
//			if (index < preRAswear.length) {
//				BaseRelativeLayout layout = new CalculationSecendViewPre(getContext(), preRAswear[index]);
//				ltQuestionItemView.add(layout);
//			} else {
//				try {
//					BaseRelativeLayout layout = new CalculationFisrtViewPre(mContext, index - preRAswear.length, cEntity);
//					layout.setClickListion(new onClickListion() {
//
//						@Override
//						public void onClick() {
//							judgeResulte();
//							saveMyanswear();
//						}
//					});
//					ltQuestionItemView.add(layout);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		llyoutQuestion.addView(ltQuestionItemView.get(0));
//		((RadioButton) rGroupSub.getChildAt(0)).setChecked(true);
//	}
//
//	@Override
//	public void updateMyanswear() {
//		String[] myAsw = cEntity.getMyAnswear().split(">>>>");
//		for (int index = 0; index < ltQuestionItemView.size(); index++) {
//			BaseRelativeLayout layout = ltQuestionItemView.get(index);
//			layout.updateAsConfir(myAsw[index]);
//		}
//		super.updateMyanswear();
//	}
//
//	@Override
//	public void judge() {
//		super.judge();
//		for (int index = 0; index < ltQuestionItemView.size(); index++) {
//			BaseRelativeLayout layout = ltQuestionItemView.get(index);
//			layout.judge();
//			String ans = index + 1 == ltQuestionItemView.size() ? layout.getMyAsItem() : layout.getMyAsItem() + ">>>>";
//			myAsw.append(ans);
//		}
//		int reasulte = cEntity.getAnswear().equals(myAsw) ? 1 : 2;
//		cEntity.setRight(reasulte);
//		cEntity.setMyAnswear(myAsw.toString());
//	}
//
//	@Override
//	public void judgeResulte() {
//		for (int index = 0; index < ltQuestionItemView.size(); index++) {
//			BaseRelativeLayout layout = ltQuestionItemView.get(index);
//			layout.judgeResulte();
//			String ans = index + 1 == ltQuestionItemView.size() ? layout.getMyAsItem() : layout.getMyAsItem() + ">>>>";
//			myAsw.append(ans);
//		}
//		int reasulte = cEntity.getAnswear().equals(myAsw) ? 1 : 2;
//		cEntity.setRight(reasulte);
//		cEntity.setMyAnswear(myAsw.toString());
//		Log.e("tag", "2->" + cEntity.getMyAnswear());
//		super.judgeResulte();
//	}
//
//	@Override
//	public void reAgin() {
//		for (int index = 0; index < ltQuestionItemView.size(); index++) {
//			BaseRelativeLayout layout = ltQuestionItemView.get(index);
//			layout.reAgin();
//		}
//		myAsw.setLength(0);
//		// 判断是否是最后一个题选择重做
//		RadioButton rbtn = (RadioButton) rGroupSub.getChildAt(rGroupSub.getChildCount() - 1);
//		if (rbtn.isChecked()) {
//			btnFinish.setVisibility(View.VISIBLE);
//		}
//		// 回到第一题
//		RadioButton rButton = (RadioButton) rGroupSub.getChildAt(0);
//		rButton.setChecked(true);
//		subDo(rButton);
//		super.reAgin();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_finish:
//			judgeResulte();
//			saveMyanswear();
//			break;
//		default:
//			subDo(v);
//			break;
//		}
//	}
//
//	@Override
//	public void initResulteView() {
//		btnFinish.setVisibility(View.GONE);
//		super.initResulteView();
//	}
//
//	/**
//	 * 题干点击处理
//	 * 
//	 * @param v
//	 */
//	private void subDo(View v) {
//		llyoutQuestion.removeAllViews();
//		llyoutQuestion.addView(ltQuestionItemView.get(v.getId()));
//		if (v.getId() == (preRAswear.length + rAnswear.length - 1) && 4 > rule.getTestModel() && cEntity.getRight() == -1) {
//			btnFinish.setVisibility(View.VISIBLE);
//		} else {
//			btnFinish.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	protected void showCorrectAnswer(boolean correct) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected void disableOption() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void reset() {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
