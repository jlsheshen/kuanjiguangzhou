package com.edu.basicaccountingforguangzhou.subject.data;

import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;

/**
 * 基础题目数据：单多判
 * 
 * @author lucher
 * 
 */
public class SubjectBasicData extends BaseSubjectData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1233432L;

	/**
	 * 选项
	 */
	private String option;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return String.format("id:%s,question:%s", id, question);
	}
}
