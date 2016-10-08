package com.edu.basicaccountingforguangzhou.info;

public class RandomBlankEntity {
	private int id;
	private int viewLinkId;
	private int count;
	private int quesId;

	public RandomBlankEntity(int id, int viewLinkId, int count, int quesId) {
		this.id = id;
		this.viewLinkId = viewLinkId;
		this.count = count;
		this.quesId = quesId;
	}

	public RandomBlankEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getViewLinkId() {
		return viewLinkId;
	}

	public void setViewLinkId(int viewLinkId) {
		this.viewLinkId = viewLinkId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

}
