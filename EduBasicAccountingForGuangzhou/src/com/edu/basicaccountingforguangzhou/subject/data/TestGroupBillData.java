package com.edu.basicaccountingforguangzhou.subject.data;

import java.util.List;

import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;
import com.edu.basicaccountingforguangzhou.data.TestBillData;
import com.edu.basicaccountingforguangzhou.data.TestData;

/**
 * 分组单据测试数据
 * 
 * @author lucher
 * 
 */
public class TestGroupBillData extends TestData {

	// 对应单据的数据
	private List<TestBillData> testDatas;
	// 用户印章
	private String uSigns;

	private float score;// 分数
	private float uScore;
	private boolean isDone;
	private String indexName;//题卡
	private int indexNum;//提卡题目id
	
	
	
	

	public int getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getuScore() {
		return uScore;
	}

	public void setuScore(float uScore) {
		this.uScore = uScore;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public List<TestBillData> getTestDatas() {
		return testDatas;
	}

	public void setTestDatas(List<TestBillData> testData) {
		this.testDatas = testData;
	}

	@Override
	public BaseSubjectData getSubjectData() {
		return testDatas.get(0).getSubjectData();
	}

	public String getuSigns() {
		return uSigns;
	}

	public void setuSigns(String uSigns) {
		this.uSigns = uSigns;
	}

	@Override
	public void setSubjectData(BaseSubjectData subjectData) {

	}
}
