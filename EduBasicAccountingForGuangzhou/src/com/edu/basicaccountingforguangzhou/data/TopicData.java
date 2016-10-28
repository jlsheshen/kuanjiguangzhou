package com.edu.basicaccountingforguangzhou.data;


/**
 * Topic的List数据封装
 * 
 * @author edu_lx
 * 
 */
public class TopicData extends BaseData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4292843776111456084L;
	// 章节ID
	private int dataid;
	// Topic内容 将List转换为String的内容
	private String topic;
	

	public int getDataid() {
		return dataid;
	}


	public void setDataid(int dataid) {
		this.dataid = dataid;
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	@Override
	public String toString() {
		return String.format("--->", dataid, topic);
	}

	

}
