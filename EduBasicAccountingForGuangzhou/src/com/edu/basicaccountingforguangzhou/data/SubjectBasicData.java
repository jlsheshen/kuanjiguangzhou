package com.edu.basicaccountingforguangzhou.data;

/**
 * 基础题型（单多判）数据封装，每个字段的详细信息见doc/EduBasicAccounting_database.xls.xls
 * 
 * @author lucher
 * 
 */
public class SubjectBasicData extends BaseSubjectData {

	private static final long serialVersionUID = 1L;
	// 题型，与EduConstant中的题型对应
	private int type;
	// 题目
	private String question;
	// 选项
	private String option;
	// 正确答案
	private String answer;
	// 用户答案
	private String userAnswer;
	// 解析
	private String analysis;
	// 章节id
	private int chapterId;
	private boolean isRight;// 是否正确
	private int serverId;
	public String getUserAnswer() {
		if (userAnswer==null) {
			return "";
			
		}
		return userAnswer.trim();
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getAnswer() {
		return answer.trim();
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	@Override
	public String toString() {
		return String.format("%s~%s~%s~%s", type, question, option, answer);
	}

	public int getChapter_id() {
		return chapterId;
	}

	public void setChapter_id(int chapter_id) {
		this.chapterId = chapter_id;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}


}
