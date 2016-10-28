package com.edu.basicaccountingforguangzhou.data;


/**
 * 产品类别封装
 * 
 * @author lucher
 * 
 */


public class ChapterData extends BaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 父id
	private int parentId;
	// 名字
	private String name;
	// 随堂练习对应的paperid
	private long paperIdPractice;
	// 课后练习对应的paperid
	private long paperIdExercise;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return String.format("%s", name);
	}

	public long getPaperIdPractice() {
		return paperIdPractice;
	}

	public void setPaperIdPractice(long paperIdPractice) {
		this.paperIdPractice = paperIdPractice;
	}

	public long getPaperIdExercise() {
		return paperIdExercise;
	}

	public void setPaperIdExercise(long paperIdExercise) {
		this.paperIdExercise = paperIdExercise;
	}

}
