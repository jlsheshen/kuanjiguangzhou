package com.edu.basicaccountingforguangzhou.info;

import java.util.ArrayList;
import java.util.List;

import com.edu.ime.ViewInfo;

/**
 * 审核模块题目封装
 * 
 * @author lucher
 * 
 */
public class AuditQuesionInfo {
	private int id;
	private String content; // 问题内容
	private byte isCompleted; // 问题完成进度（0.默认值,1.未完成,2.已完成）
	private int bsId; // 底图id
	private String bsName; // 底图图片名称（eg:"x.png"）
	private List<AuditViewInfo> lsET = new ArrayList<AuditViewInfo>(); // 问题对应的EditText
	private List<ViewInfo> lsCB = new ArrayList<ViewInfo>(); // 问题对应的CheckBox
	private int selectedCount = 0;// 选中的题数
	private int totalCount = 0;// 总题数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBsId() {
		return bsId;
	}

	public void setBsId(int bsId) {
		this.bsId = bsId;
	}

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public List<AuditViewInfo> getLsET() {
		return lsET;
	}

	public void setLsET(List<AuditViewInfo> lsET) {
		this.lsET = lsET;
	}

	public List<ViewInfo> getLsCB() {
		return lsCB;
	}

	public void setLsCB(List<ViewInfo> lsCB) {
		this.lsCB = lsCB;
	}

	public byte getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(byte isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
