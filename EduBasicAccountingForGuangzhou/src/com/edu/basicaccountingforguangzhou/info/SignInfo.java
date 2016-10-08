package com.edu.basicaccountingforguangzhou.info;

import java.io.Serializable;

/**
 * 实体类：印章
 * 
 * @author 孙兴达
 * 
 */
public class SignInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917503070608258845L;
	private int id; // 印章ID
	private int xAxis; // 印章横坐标
	private int yAxis; // 印章纵坐标
	private String signName; // 印章对应的图片名称（eg:"x.png"）
	private String content; // 印章名称（eg:"财务专用章"）
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getxAxis() {
		return xAxis;
	}

	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}

	public int getyAxis() {
		return yAxis;
	}

	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
