package com.edu.basicaccountingforguangzhou.data;

import java.io.Serializable;

/**
 * 单据题型数据封装
 * 
 * @author edu_lx
 * 
 */
public class SubjectBillData extends BaseSubjectData implements Serializable {
	

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
	
	
	/**
	 * 详细信息见doc/EduBasicAccounting_database.xls
//	 */
	private int FLAG;//预置标识，-1：预置，其他：服务器端id号
	private int CHAPTER_ID;//章节id
	private int TEMPLATE_ID;//模板id
	private String QUESTION;//问题
	private String PIC;//图片资源
//	private int billId;
//	private int billTypeId;
	private String LABELS;//标签名称
	private String BLANKS;//对应空的正确答案
	private float SCORE;//分数
	private int ERROR_COUNT;//错误次数
	private int FAVORITE;//是否收藏

	private String REMARK;//备注
	
	

	public int getFLAG() {
		return FLAG;
	}



	public void setFLAG(int fLAG) {
		FLAG = fLAG;
	}



	public int getCHAPTER_ID() {
		return CHAPTER_ID;
	}



	public void setCHAPTER_ID(int cHAPTER_ID) {
		CHAPTER_ID = cHAPTER_ID;
	}



	public int getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}



	public void setTEMPLATE_ID(int tEMPLATE_ID) {
		TEMPLATE_ID = tEMPLATE_ID;
	}



	public String getQUESTION() {
		return QUESTION;
	}



	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}



	public String getPIC() {
		return PIC;
	}



	public void setPIC(String pIC) {
		PIC = pIC;
	}



	public String getLABELS() {
		return LABELS;
	}



	public void setLABELS(String lABELS) {
		LABELS = lABELS;
	}



	public String getBLANKS() {
		return BLANKS;
	}



	public void setBLANKS(String bLANKS) {
		BLANKS = bLANKS;
	}



	public float getSCORE() {
		return SCORE;
	}



	public void setSCORE(float sCORE) {
		SCORE = sCORE;
	}



	public int getERROR_COUNT() {
		return ERROR_COUNT;
	}



	public void setERROR_COUNT(int eRROR_COUNT) {
		ERROR_COUNT = eRROR_COUNT;
	}



	public int getFAVORITE() {
		return FAVORITE;
	}



	public void setFAVORITE(int fAVORITE) {
		FAVORITE = fAVORITE;
	}



	public String getREMARK() {
		return REMARK;
	}



	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}



//	@Override
//	public String toString() {
//		return String.format("5,content:%s", QUESTION);
//	}

}
