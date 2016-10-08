package com.edu.basicaccountingforguangzhou.data;

/**
 * 单据题型数据封装
 * 
 * @author edu_lx
 * 
 */
public class SubjectBillData extends BaseSubjectData {
	/**
	 * 详细信息见doc/EduBasicAccounting_database.xls
	 */
	private int serverId;
	private int chapterId;
	private int basemapId;
	private String content;
	private boolean isCompleted;
//	private int billId;
//	private int billTypeId;
	private int showFlag;
	private String picName;
	private int type;
	private String children;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getBasemapId() {
		return basemapId;
	}

	public void setBasemapId(int basemapId) {
		this.basemapId = basemapId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

//	public int getBillId() {
//		return billId;
//	}

//	public void setBillId(int billId) {
//		this.billId = billId;
//	}
//
//	public int getBillTypeId() {
//		return billTypeId;
//	}

//	public void setBillTypeId(int billTypeId) {
//		this.billTypeId = billTypeId;
//	}

	public int getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(int showFlag) {
		this.showFlag = showFlag;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return String.format("5,content:%s", content);
	}

}
