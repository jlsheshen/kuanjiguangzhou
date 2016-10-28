package com.edu.basicaccountingforguangzhou.data;




public class ExampleMixData extends BaseData {
	// 包含文字、图片名、视频地址
	private String content;
	// 内容的类型 1：文字，2：图片，3：视频
	private int type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
