package com.edu.basicaccountingforguangzhou.data;


/**
 * 重点难点
 * @author xd
 *
 */
public class PointData extends BaseData {
	private int chapter_id;
	/**
	 * 重难点内容
	 */
	private String content;
	
	private String remark;
	/**
	 * @return the chapter_id
	 */
	public int getChapter_id() {
		return chapter_id;
	}

	/**
	 * @param chapter_id the chapter_id to set
	 */
	public void setChapter_id(int chapter_id) {
		this.chapter_id = chapter_id;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the remark
	 */
	@Override
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	

}
