package com.edu.basicaccountingforguangzhou.data;


/**
 * tb_record
 * 
 * @author xd
 * 
 */
public class RecordData extends BaseData {
	private int  testId;
	private int type;
	private String basic_id;
	private String other_id;
	private String testName;
	private String testTime;
	private int totalTime;
	private int usedTime;
	private String testContfig;
	private int state;
	private int sendState;
	private int score;
	private int uScore;
	private boolean lookMode;


	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the basic_id
	 */
	public String getBasic_id() {
		return basic_id;
	}

	/**
	 * @param basic_id
	 *            the basic_id to set
	 */
	public void setBasic_id(String basic_id) {
		this.basic_id = basic_id;
	}

	/**
	 * @return the other_id
	 */
	public String getOther_id() {
		return other_id;
	}

	/**
	 * @param other_id
	 *            the other_id to set
	 */
	public void setOther_id(String other_id) {
		this.other_id = other_id;
	}

	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * @param testName
	 *            the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return the testTime
	 */
	public String getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 *            the testTime to set
	 */
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	/**
	 * @return the totalTime
	 */
	public int getTotalTime() {
		return totalTime;
	}

	/**
	 * @param totalTime
	 *            the totalTime to set
	 */
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * @return the usedTime
	 */
	public int getUsedTime() {
		return usedTime;
	}

	/**
	 * @param usedTime
	 *            the usedTime to set
	 */
	public void setUsedTime(int usedTime) {
		this.usedTime = usedTime;
	}

	/**
	 * @return the testContfig
	 */
	public String getTestContfig() {
		return testContfig;
	}

	/**
	 * @param testContfig
	 *            the testContfig to set
	 */
	public void setTestContfig(String testContfig) {
		this.testContfig = testContfig;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the sendState
	 */
	public int getSendState() {
		return sendState;
	}

	/**
	 * @param sendState
	 *            the sendState to set
	 */
	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the uScore
	 */
	public int getuScore() {
		return uScore;
	}

	/**
	 * @param uScore
	 *            the uScore to set
	 */
	public void setuScore(int uScore) {
		this.uScore = uScore;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public boolean isLookMode() {
		return lookMode;
	}

	public void setLookMode(boolean lookMode) {
		this.lookMode = lookMode;
	}

}
