package com.edu.subject.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.edu.subject.ISubject;
import com.edu.subject.SubjectConstant;
import com.edu.subject.SubjectListener;
import com.edu.subject.SubjectState;
import com.edu.subject.data.BaseTestData;
import com.edu.subject.data.SignData;
import com.edu.subject.data.TestBillData;
import com.edu.subject.data.TestGroupBillData;
import com.edu.testbill.R;

/**
 * 分组单据题型视图
 * 
 * @author lucher
 * 
 */
public class GroupBillView extends RelativeLayout implements ISubject, OnCheckedChangeListener {

	// 缩放按钮
	private Context mContext;
	private List<BillView> billViews;
	// 单据相关数据
	private TestGroupBillData mData;

	// 当前单据索引
	private int mCurrentIndex;
	// 标签容器
	private RadioGroup tabs;
	// 单据容器
	private RelativeLayout billContent;

	public GroupBillView(Context context, TestGroupBillData data) {
		super(context);
		View.inflate(context, R.layout.layout_group_bill_view, this);
		mContext = context;
		mData = data;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		tabs = (RadioGroup) findViewById(R.id.tabs);
		tabs.setOnCheckedChangeListener(this);
		billContent = (RelativeLayout) findViewById(R.id.billContent);
		billViews = new ArrayList<BillView>(mData.getTestDatas().size());

		initContent();
	}

	/**
	 * 初始化单据控件
	 */
	private void initContent() {
		for (int i = 0; i < mData.getTestDatas().size(); i++) {
			// 初始化标签
			RadioButton radio = new RadioButton(mContext);
			TestBillData data = mData.getTestDatas().get(i);
			radio.setId(i);
			radio.setText(data.getSubjectData().getLabel());
			tabs.addView(radio);
			// 初始化单据
			BillView billView = new BillView(mContext, data);
			billView.applyData(mData.getTestDatas().get(i));
			billViews.add(billView);
		}
		((RadioButton) tabs.getChildAt(0)).setChecked(true);
	}

	/**
	 * 盖章操作
	 * 
	 * @param signData
	 */
	public void sign(SignData signData) {
		billViews.get(mCurrentIndex).sign(signData);
	}

	/**
	 * 显示闪电符
	 */
	public void showFlashes() {
		billViews.get(mCurrentIndex).showFlashes();
	}

	/**
	 * 显示用户答案
	 */
	public void showUserAnswer() {
		billViews.get(mCurrentIndex).showUserAnswer();
	}

	@Override
	public void applyData(BaseTestData data) {
	}

	@Override
	public void saveAnswer() {
		judge(false);
	}

	@Override
	public void showDetails() {
		billViews.get(mCurrentIndex).showDetails();
	}

	@Override
	public float submit() {
		judge(true);
		return mData.getuScore();
	}

	/**
	 * 判断答案等操作
	 * 
	 * @param submit
	 *            是否提交
	 */
	private void judge(boolean submit) {
		float totalScore = mData.getScore();//总的得分，对单据进行算分之前把当前总分设置到单据中去进行计算，最后一张单据计算完便是总得分
		StringBuilder answerBuilder = new StringBuilder();
		StringBuilder signBuilder = new StringBuilder();
		int index = 0;
		// 拼接答案，累加分数
		for (int i = 0; i < billViews.size(); i++) {
			BillView billView = billViews.get(i);
			mData.getTestDatas().get(i).getSubjectData().setScore(totalScore);
			// 进行提交或保存操作
			if (submit) {
				billView.submit();
			} else {
				billView.saveAnswer();
			}
			// 用户答案印章拼接保存
			String uAnswer = billView.getTestData().getuAnswer();
			String uSign = billView.getTestData().getuSigns();
			if (uAnswer.equals("")) {
				uAnswer = SubjectConstant.FLAG_NULL_STRING;
			}
			if (uSign == null || uSign.equals("")) {
				uSign = SubjectConstant.FLAG_NULL_STRING;
			}
			if (index == 0) {
				answerBuilder.append(uAnswer);
				signBuilder.append(uSign);
			} else {
				answerBuilder.append(SubjectConstant.SEPARATOR_GROUP + uAnswer);
				signBuilder.append(SubjectConstant.SEPARATOR_GROUP + uSign);
			}
			totalScore = billView.getTestData().getuScore();
			index++;
			// 状态修改
			if (submit) {
				// 判断正误，在此之前需要把对应的题得分设置到data中
				if (mData.getTestDatas().get(i).getSubjectData().getScore() == mData.getTestDatas().get(i).getuScore()) {
					mData.getTestDatas().get(i).setState(SubjectState.STATE_CORRECT);
				} else {
					mData.getTestDatas().get(i).setState(SubjectState.STATE_WRONG);
				}
			} else {
				if (billView.getTestData().getState() == SubjectState.STATE_INIT) {
					billView.getTestData().setState(SubjectState.STATE_UNFINISH);
				}
			}

		}
		mData.setuAnswer(answerBuilder.toString());
		mData.setuSigns(signBuilder.toString());
		mData.setuScore(totalScore);
	}

	@Override
	public void reset() {
		for (int i = 0; i < billViews.size(); i++) {
			BillView billView = billViews.get(i);
			billView.reset();
		}
	}

	@Override
	public void setSubjectListener(SubjectListener listener) {
		billViews.get(mCurrentIndex).setSubjectListener(listener);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mCurrentIndex = checkedId;
		billContent.removeAllViews();
		billContent.addView(billViews.get(checkedId));
		billViews.get(checkedId).requestDefaultFocus();
	}

	/**
	 * 获取默认焦点
	 */
	public void requestDefaultFocus() {
		billViews.get(mCurrentIndex).requestDefaultFocus();
	}
}
