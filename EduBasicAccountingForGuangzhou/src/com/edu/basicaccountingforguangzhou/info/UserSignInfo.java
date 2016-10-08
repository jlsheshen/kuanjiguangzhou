package com.edu.basicaccountingforguangzhou.info;

import java.io.Serializable;

import android.widget.LinearLayout;

public class UserSignInfo implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = 1893053165477192181L;
	private int questionID; // 问题ID
	private int userXAxis; // 用户选择印章的横坐标
	private int userYAxis; // 用户选择印章的纵坐标
	private String userSignName; // 用户选择的印章名称
	private LinearLayout sign; // 用于显示的章
	private boolean inserted; // 标记数据是否经插入数据库
	private boolean isRight; // 标记所盖印章是否正确

	public int getUserXAxis() {
		return userXAxis;
	}

	public void setUserXAxis(int userXAxis) {
		this.userXAxis = userXAxis;
	}

	public int getUserYAxis() {
		return userYAxis;
	}

	public void setUserYAxis(int userYAxis) {
		this.userYAxis = userYAxis;
	}

	public String getUserSignName() {
		return userSignName;
	}

	public void setUserSignName(String userSignName) {
		this.userSignName = userSignName;
	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public LinearLayout getSign() {
		return sign;
	}

	public void setSign(LinearLayout sign) {
		this.sign = sign;
	}

	public boolean isInserted() {
		return inserted;
	}

	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

}
