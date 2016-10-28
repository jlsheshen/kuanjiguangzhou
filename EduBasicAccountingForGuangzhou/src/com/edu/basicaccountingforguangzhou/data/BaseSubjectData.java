package com.edu.basicaccountingforguangzhou.data;

import java.io.Serializable;




/**
 * 题目数据封装基类
 * 
 * @author lucher
 * 
 */
public class BaseSubjectData extends BaseData implements Serializable {
	
	
	
	
	
	
	// 问题显示id
	private int subjectIndex;
	private float score;// 分数
	private float uScore;
	private boolean isDone;
	private String indexName;//题卡
	public int getSubjectIndex() {
		return subjectIndex;
	}

	public void setSubjectIndex(int subjectIndex) {
		this.subjectIndex = subjectIndex;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getuScore() {
		return uScore;
	}

	public void setuScore(float uScore) {
		this.uScore = uScore;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	// 预置标识
		protected int flag;
		// 章节id
		protected int chapterId;
		// 题目内容
		protected String question;
		// 图片
		private String pic;

		// 错误次数
		protected int errorCount;
		// 正确答案
		protected String answer;
		// 解析
		protected String analysis;
		/**
		 * 题目类别,与{@link SubjectType}对应
		 */
		protected int subjectType;
		// 是否收藏，0-不收藏，1-收藏
		protected int favorite;

		public int getChapterId() {
			return chapterId;
		}

		public void setChapterId(int chapterId) {
			this.chapterId = chapterId;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		
		public int getErrorCount() {
			return errorCount;
		}

		public void setErrorCount(int errorCount) {
			this.errorCount = errorCount;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getAnalysis() {
			return analysis;
		}

		public void setAnalysis(String analysis) {
			this.analysis = analysis;
		}

		public int getSubjectType() {
			return subjectType;
		}

		public void setSubjectType(int subjectType) {
			this.subjectType = subjectType;
		}

		public int getFavorite() {
			return favorite;
		}

		public void setFavorite(int favorite) {
			this.favorite = favorite;
		}



}
