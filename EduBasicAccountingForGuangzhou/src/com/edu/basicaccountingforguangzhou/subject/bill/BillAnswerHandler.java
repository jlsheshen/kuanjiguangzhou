package com.edu.basicaccountingforguangzhou.subject.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.edu.basicaccountingforguangzhou.data.TestBillData;
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.bill.element.ElementType;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.SignInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.view.BlankEditText;
import com.edu.basicaccountingforguangzhou.subject.bill.view.SignView;
import com.edu.library.util.ToastUtil;


/**
 * 单据题答案处理类
 * 
 * @author lucher
 * 
 */
public class BillAnswerHandler {
	private static final String TAG = "BillAnswerHandler";

	// 存放所有填空控件
	private List<BlankEditText> mEtBlanks;
	// 存放需要分组的填空控件，key-分组id，value-对应组的组件
	private HashMap<Integer, List<BlankEditText>> mGroups;
	// 所有印章对应的视图
	private List<SignView> mSignViews;
	// 测试数据实体
	private TestBillData mTestData;

	public BillAnswerHandler() {
		mEtBlanks = new ArrayList<BlankEditText>();
		mGroups = new HashMap<Integer, List<BlankEditText>>();
	}

	/**
	 * 设置测试数据
	 * 
	 * @param testData
	 */
	public void setTestData(TestBillData testData) {
		mTestData = testData;
	}

	/**
	 * 添加需要算分的控件
	 * 
	 * @param view
	 */
	public void addBlank(BlankEditText view) {
		if (!view.getData().isEditable()) {// 不能编辑的空不需要添加
			return;
		}
		mEtBlanks.add(view);

		int type = view.getData().getType();
//		if (isGroupBlank(type)) {
//			try {
//				//String remark = view.getData().getRemark();
//
//			//	int groupId = Integer.valueOf(remark);
//				int groupId = 0;
//				if (mGroups.get(groupId) == null) {
//					List<BlankEditText> list = new ArrayList<BlankEditText>(3);
//					mGroups.put(groupId, list);
//					list.add(view);
//				} else {
//					mGroups.get(groupId).add(view);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				ToastUtil.showToast(view.getContext(), view + ",该空必须要有groupId");
//			}
//		}
	}

	/**
	 * 设置印章视图
	 * 
	 * @param signs
	 */
	public void setSignViews(List<SignView> signs) {
		mSignViews = signs;
	}

	/**
	 * 提交用户答案
	 */
	public void submit() {
		float totalScore = 0;
		totalScore += judgeBlanks();
		totalScore += judgeSigns();

		mTestData.setuScore(Math.max(0, totalScore));
	}

	/**
	 * 计算填空分数
	 * 
	 * @return
	 */
	private float judgeBlanks() {
		// 算分方法：对每个空进行答案判断，如果答错，从总分里减去答错空的分值，直到总分小于或等于0
		float totalScore = mTestData.getSubjectData().getScore();
		// 答案拼接
		StringBuilder builder = new StringBuilder();
		int index = 0;
		// 所有控件进行内容正误判断，并计算不分组类别控件的分数
		for (BlankEditText etBlank : mEtBlanks) {
			if (!etBlank.getData().isEditable()) {// 该空不需要用户填写，直接跳过
				continue;
			}
			etBlank.submit();
			BlankInfo data = etBlank.getData();

			if (!data.isRight() && !isGroupBlank(data.getType())) {// 对于分组类的分数不在此处计算
				totalScore -= data.getScore();
			}
			// 拼接用户答案
			String uAnswer = data.getuAnswer().trim();
			if (uAnswer.equals("")) {
				uAnswer = SubjectConstant.FLAG_NULL_STRING;
			}
			if (index == 0) {
				builder.append(uAnswer);
			} else {
				builder.append(SubjectConstant.SEPARATOR_ITEM + uAnswer);
			}
			index++;
		}
		// 计算分组类控件的分数
		Iterator<Integer> iterator = mGroups.keySet().iterator();
		while (iterator.hasNext()) {
			List<BlankEditText> group = mGroups.get(iterator.next());
			boolean right = true;// 对应组是否答对，只要有一个空答错，则算错
			float tmpScore = 0;// 对应组的总分
			for (BlankEditText blank : group) {
				if (right && !blank.getData().isRight()) {
					right = false;
				}
				tmpScore += blank.getData().getScore();
			}
			if (!right) {

				totalScore -= tmpScore;
			}
		}

		mTestData.setuAnswer(builder.toString());
		return totalScore;
	}

	/**
	 * 计算印章分数
	 * 
	 * @return
	 */
	private float judgeSigns() {
		float totalScore = 0;
		// 对印章控件进行判断
		if (mSignViews != null) {
			// 答案拼接
			StringBuilder builder = new StringBuilder();
			int index = 0;
			for (SignView sign : mSignViews) {
				SignInfo info = sign.getData();
				if (info.isUser()) {// 用户添加的印章
					String uAnswer = info.getId() + SubjectConstant.SEPARATOR_SIGN_INFO + info.getX() + SubjectConstant.SEPARATOR_SIGN_INFO + info.getY();
					Log.e("查看sgin专用","uAnswer"  + uAnswer);

					if (index == 0) {
						builder.append(uAnswer);
					} else {
						builder.append(SubjectConstant.SEPARATOR_ITEM + uAnswer);
					}
					index++;
				}
			}
			Log.d(TAG, "user sign:" + builder);
			mTestData.setuSigns(builder.toString());
		}

		return totalScore;
	}

	/**
	 * 是否需要分组的空
	 * 
	 * @param type
	 * @return
	 */
	private boolean isGroupBlank(int type) {
		return type == ElementType.TYPE_DATE_LOWER || type == ElementType.TYPE_DATE_UPPER || type == ElementType.TYPE_AMOUNT_LOWER_SEP;
	}
}
