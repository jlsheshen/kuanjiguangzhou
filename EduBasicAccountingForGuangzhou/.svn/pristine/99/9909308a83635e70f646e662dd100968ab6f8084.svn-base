package com.edu.basicaccountingforguangzhou.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.edu.ime.ViewInfo;

/**
 * 实体类：问题
 * 
 * @author 孙兴达
 * 
 */
public class QuestionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4828385338730960043L;
	private int id;
	private String content; // 问题内容
	private byte isCompleted; // 问题完成进度（0.默认值,1.未完成,2.已完成）
	private int bsId; // 底图id
	private String bsName; // 底图图片名称（eg:"x.png"）
	private List<SignInfo> lsSign = new ArrayList<SignInfo>(); // 问题对应的印章
	private List<ViewInfo> lsET = new ArrayList<ViewInfo>(); // 问题对应的EditText
	private List<UserSignInfo> lsUserSign = new ArrayList<UserSignInfo>(); // 用户选择的印章
	private String picName;// 图片

	/**
	 * @return the picName
	 */
	public String getPicName() {
		return picName;
	}

	/**
	 * @param picName
	 *            the picName to set
	 */
	public void setPicName(String picName) {
		this.picName = picName;
	}

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

	public List<SignInfo> getLsSign() {
		return lsSign;
	}

	public void setLsSign(List<SignInfo> lsSign) {
		this.lsSign = lsSign;
	}

	public List<ViewInfo> getLsET() {
		return lsET;
	}

	public void setLsET(List<ViewInfo> lsET) {
		this.lsET = lsET;
	}

	public byte getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(byte isCompleted) {
		this.isCompleted = isCompleted;
	}

	public List<UserSignInfo> getLsUserSign() {
		return lsUserSign;
	}

	public void setLsUserSign(List<UserSignInfo> lsUserSign) {
		this.lsUserSign = lsUserSign;
	}

}
