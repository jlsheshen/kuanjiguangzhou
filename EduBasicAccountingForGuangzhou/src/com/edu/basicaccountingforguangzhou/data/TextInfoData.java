package com.edu.basicaccountingforguangzhou.data;


public class TextInfoData extends BaseData {

	private String name;// 试题名称
	private int lock;// 状态是否解锁 1为解锁，0为未解锁
	private int done;// 是否完成, 0未做，1完成，2未完成
	private int score;// 分数

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public int getDone() {
		return done;
	}

	public void setDone(int done) {
		this.done = done;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
