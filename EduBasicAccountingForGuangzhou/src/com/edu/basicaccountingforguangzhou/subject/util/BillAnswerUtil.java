package com.edu.basicaccountingforguangzhou.subject.util;

import java.util.List;

import android.util.Log;

import com.edu.basicaccountingforguangzhou.data.TestBillData;
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.view.BlankEditText;
import com.edu.basicaccountingforguangzhou.subject.bill.view.SignView;

/**
 * 单据题答案解析工具类
 * 
 * @author lucher
 * 
 */
public class BillAnswerUtil {
	private static final String TAG = "AnswerParseHelper";

	/**
	 * 提交用户答案
	 * @param testData
	 * @param mEtBlanks 所有空
	 * @param mSignViews
	 */
	public static void submit(TestBillData testData, List<BlankEditText> mEtBlanks, List<SignView> mSignViews) {
		// 算分方法：对每个空进行答案判断，如果答错，从总分里减去答错空的分值，直到总分小于或等于0
		float totalScore = testData.getSubjectData().getScore();
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (BlankEditText etBlank : mEtBlanks) {
			if (!etBlank.getData().isEditable()) {//该空不需要用户填写，直接跳过
				continue;
			}
			etBlank.submit();
			BlankInfo data = etBlank.getData();
			if (!data.isRight()) {
				totalScore -= data.getScore();
				Log.d(TAG, "totalscore:" + totalScore + ",score:" + data.getScore());
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
		Log.d(TAG, "user answer:" + builder);
		for (SignView signView : mSignViews) {// 暂不计分
			signView.submit();
		}

		testData.setuScore(Math.max(0, totalScore));
		testData.setuAnswer(builder.toString());
	}
}
