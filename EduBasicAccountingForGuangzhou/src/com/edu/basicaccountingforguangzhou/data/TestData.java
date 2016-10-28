package com.edu.basicaccountingforguangzhou.data;

import java.io.Serializable;

import com.edu.basicaccountingforguangzhou.subject.SubjectState;
import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.TestMode;
import com.edu.basicaccountingforguangzhou.subject.data.TestGroupBillData;


/**
 * 课后练习，随堂练习数据封装
 * 
 * @author lucher
 * 
 */
public  class TestData extends BaseData implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1323L;
	
	// 服务器端id号
	protected int serverId;
	// 类别，1-课后练习，2-随堂练习
	protected int type;
	// 章节id
	protected int chapterId;
	// 题目类型
	protected int subjectType;
	// 题目的id
	public String subjectId;
	// 答题状态，0-未做，1-正确，2-错误
	protected int state;
	// 得分
	protected int sendState;// 发送成绩状态 1未发送、2已发送

	// 练习题目列表中显示的标题
	protected String title;
	
	protected int errorCount;
	/**
	 * @return the errorCount
	 */
	protected TestBillData billData;
	
	private TestGroupBillData testGroupBillData;
	
	
	
	public TestGroupBillData getTestGroupBillData() {
		return testGroupBillData;
	}

	public void setTestGroupBillData(TestGroupBillData testGroupBillData) {
		this.testGroupBillData = testGroupBillData;
	}

	public TestBillData getBillData() {
		return billData;
	}

	public void setBillData(TestBillData billData) {
		this.billData = billData;
	}

	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * @param errorCount the errorCount to set
	 */
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
//	public abstract BaseSubjectData getSubjectData();
//
//	public abstract void setSubjectData(BaseSubjectData subjectData);
	

	// 对应的题目信息
	protected BaseSubjectData data;
	protected boolean isDone;//是否做过
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectId() {
		
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BaseSubjectData getData() {
		return data;
	}

	public void setData(BaseSubjectData data) {
		this.data = data;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}


	

	// 问题显示的index
	protected String subjectIndex;
	// 预置标识
	protected int flag;
	

	// 用户答案
	protected String uAnswer;
	// 用户得分
	protected float uScore;
	BaseSubjectData subjectData;


	/**
	 * 测试模式，与{@link TestMode}对应
	 */
	protected int testMode;



	public BaseSubjectData getSubjectData() {
		return subjectData;
	}

	public void setSubjectData(BaseSubjectData subjectData) {
		this.subjectData = subjectData;
	}

	public int getTestMode() {
		return testMode;
	}

	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	public void setSubjectIndex(String subjectIndex) {
		this.subjectIndex = subjectIndex;
	}

	public String getSubjectIndex() {
		return subjectIndex;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}



	public String getuAnswer() {
		return uAnswer;
	}

	public void setuAnswer(String uAnswer) {
		this.uAnswer = uAnswer;
	}

	public float getuScore() {
		return uScore;
	}

	public void setuScore(float uScore) {
		this.uScore = uScore;
	}




}
