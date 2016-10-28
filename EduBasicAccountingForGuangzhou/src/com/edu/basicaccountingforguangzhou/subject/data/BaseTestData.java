//package com.edu.basicaccountingforguangzhou.subject.data;
//
//import com.edu.basicaccountingforguangzhou.data.BaseData;
//import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;
//import com.edu.basicaccountingforguangzhou.subject.SubjectState;
//import com.edu.basicaccountingforguangzhou.subject.SubjectType;
//import com.edu.basicaccountingforguangzhou.subject.TestMode;
//
///**
// * 题目测试数据基类
// * 
// * @author lucher
// * 
// */
//public abstract class BaseTestData extends BaseData {
//
//	// 问题显示的index
//	protected String subjectIndex;
//	// 预置标识
//	protected int flag;
//	/**
//	 * 题目类别,与{@link SubjectType}对应
//	 */
//	protected int subjectType;
//	// 题目id
//	protected String subjectId;
//	// 用户答案
//	protected String uAnswer;
//	// 用户得分
//	protected float uScore;
//	/**
//	 * 作答状态，与{@link SubjectState}对应
//	 */
//	protected int state;
//
//	/**
//	 * 测试模式，与{@link TestMode}对应
//	 */
//	protected int testMode;
//
//	public abstract BaseSubjectData getSubjectData();
//
//	public abstract void setSubjectData(BaseSubjectData subjectData);
//
//	public int getTestMode() {
//		return testMode;
//	}
//
//	public void setTestMode(int testMode) {
//		this.testMode = testMode;
//	}
//
//	public void setSubjectIndex(String subjectIndex) {
//		this.subjectIndex = subjectIndex;
//	}
//
//	public String getSubjectIndex() {
//		return subjectIndex;
//	}
//
//	public int getFlag() {
//		return flag;
//	}
//
//	public void setFlag(int flag) {
//		this.flag = flag;
//	}
//
//	public int getSubjectType() {
//		return subjectType;
//	}
//
//	public void setSubjectType(int subjectType) {
//		this.subjectType = subjectType;
//	}
//
//	public String  getSubjectId() {
//		return subjectId;
//	}
//
//	public void setSubjectId(String subjectId) {
//		this.subjectId = subjectId;
//	}
//
//	public String getuAnswer() {
//		return uAnswer;
//	}
//
//	public void setuAnswer(String uAnswer) {
//		this.uAnswer = uAnswer;
//	}
//
//	public float getuScore() {
//		return uScore;
//	}
//
//	public void setuScore(float uScore) {
//		this.uScore = uScore;
//	}
//
//	public int getState() {
//		return state;
//	}
//
//	public void setState(int state) {
//		this.state = state;
//	}
//
//}
