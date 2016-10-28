package com.edu.basicaccountingforguangzhou.subject.data;

/**
 * 单据题目数据
 * 
 * @author lucher
 * 
 */
public class SubjectBillData1 extends BaseSubjectData1 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 123432L;
	// 模板id
	private int templateId;
	// 多组单据标签
	private String labels;
	// 是否有多组单据
	private boolean multi;

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	@Override
	public String toString() {
		return String.format("templateId:%s,question:%s", templateId, question);
	}
}
