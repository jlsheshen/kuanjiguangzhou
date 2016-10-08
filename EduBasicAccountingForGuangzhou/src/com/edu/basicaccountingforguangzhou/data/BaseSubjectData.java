package com.edu.basicaccountingforguangzhou.data;

import java.io.Serializable;

/**
 * 题目数据封装基类
 * 
 * @author lucher
 * 
 */
public class BaseSubjectData extends BaseData implements Serializable {
	// 问题显示id
	private int subjectIndex;
	private float score;// 分数
	private float uScore;
	private boolean isDone;
	private String indexName;//题卡
	public int getSubjectIndex() {
		return subjectIndex;
	}

	public void setSubjectIndex(int subjectIndex) {
		this.subjectIndex = subjectIndex;
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



}
