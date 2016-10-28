package com.edu.basicaccountingforguangzhou.data;


/**
 * 典型类题数据封装
 * 
 * @author edu_lx
 * 
 */
public class ExampleData extends BaseData {
	// 根据TB_CHAPTER中的ID 查询出符合条件的例题列表
	private String item_num;
	/**
	 * 例题名称
	 */
	private String item_name;
	/**
	 * 例题类型
	 */
	private String exam_type;
	/**
	 * 例题内容
	 */
	private String exam_content;
	/**
	 * 图片名称
	 */
	private String image_name;
	/**
	 * 图片说明
	 */
	private String image_content;
	
	public String getItem_name() {
		return item_name;
	}



	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}



	public String getExam_type() {
		return exam_type;
	}



	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}



	public String getExam_content() {
		return exam_content;
	}



	public void setExam_content(String exam_content) {
		this.exam_content = exam_content;
	}



	@Override
	public String toString() {
		return String.format("content:%s", item_name + "<>" + item_num + "<>" + exam_type + "<>" + exam_content);
	}



	public String getItem_num() {
		return item_num;
	}



	public void setItem_num(String item_num) {
		this.item_num = item_num;
	}



	public String getImage_name() {
		return image_name;
	}



	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}



	public String getImage_content() {
		return image_content;
	}



	public void setImage_content(String image_content) {
		this.image_content = image_content;
	}

}
