package com.edu.basicaccountingforguangzhou.model;

import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.data.BaseDataDao;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicDataDao;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * 题库数据处理模型封装
 * 
 * @author lucher
 * 
 */
public class SubjectModel {

	private static final String TAG = "SubjectModel";

	private static Context mContext;

	// 自身实例
	private static SubjectModel instance;

	public static synchronized SubjectModel getInstance(Context context) {
		if (instance == null) {
			instance = new SubjectModel(context);
		}
		return instance;
	}

	private SubjectModel(Context context) {
		mContext = context;
	}

	/**
	 * test，修改数据
	 */
	public void testUpdateDatas() {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.ANSWER, "A");
		SubjectBasicDataDao.getInstance(mContext).updateData(1, values);
	}

	/**
	 * 根据ids获取对应题目数据
	 * 
	 * @param ids
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectIds(String ids) {
		return SubjectBasicDataDao.getInstance(mContext).getDatasBySubjectIds(ids);
	}

	/**
	 * 根据ids获取基础题目数据
	 * 
	 * @param ids
	 * @return
	 */
	public List<SubjectBasicData> getBasicDatasBySubjectIds(String ids) {
		return SubjectBasicDataDao.getInstance(mContext).getBasicDatasBySubjectIds(ids);
	}

	/**
	 * 根据ids获取基础题目数据
	 * 
	 * @param ids
	 * @return
	 */
	public List<SubjectBasicData> getBasicDatasBySubjectIdsForFenLu(String ids) {
		return SubjectBasicDataDao.getInstance(mContext).getBasicDatasBySubjectIdsForFenLu(ids);
	}

	/**
	 * 根据ids获取对应题目数据
	 * 
	 * @param ids
	 * @return
	 */
	public SubjectBasicData getDatasBySubjectId(int id) {
		return (SubjectBasicData) SubjectBasicDataDao.getInstance(mContext).getDataById(id);
	}

	/**
	 * 更新用户答案到数据库
	 * 
	 * @param id
	 * @param userAnswer
	 */
	public void updateUserAnswer(int id, String userAnswer) {
		ContentValues values = new ContentValues();
		
		Log.e("得分专用Log", "answer输入数据库前" + userAnswer);
		values.put(SubjectBasicDataDao.UANSWER, userAnswer);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新用户分数到数据库
	 * 
	 * @param id
	 * @param userAnswer
	 */
	public void updateUserScore(int id, String userScore) {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.UANSWER, userScore);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新用户答案到数据库
	 * 
	 * @param id
	 * @param userAnswer
	 */
	public void cleanUserAnswer(int id, String userAnswer) {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.UANSWER, userAnswer);
		values.put(BaseDataDao.REMARK, "0");
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新用户答案和用户分数到数据库
	 * 
	 * @param id
	 * @param userAnswer
	 * @param uscore
	 */
	public void cleanUserAnswerAndUscore(int id, String userAnswer, int uScore, boolean isCorrect) {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.UANSWER, userAnswer);
		values.put(SubjectBasicDataDao.USCORE, uScore);
		values.put(BaseDataDao.REMARK, "0");
		values.put(SubjectBasicDataDao.IS_CORRECT, isCorrect);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新remark
	 * 
	 * @param id
	 * @param remark
	 */
	public void updateRemark(int id, String remark) {
		ContentValues values = new ContentValues();
		values.put(BaseDataDao.REMARK, remark);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新正误
	 * 
	 * @param id
	 * @param remark
	 */
	public void updateRight(int id, Boolean isRight) {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.IS_CORRECT, isRight);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 根据题型获取每个题的分值
	 * 
	 * @param type
	 *            题型
	 * @param answer
	 *            针对与案例分析，用来计算有多少个空
	 * @return
	 */
	private float getPointByType(int type, String answer) {
		float point = 0;
		switch (type) {
		case Constant.SUBJECT_TYPE_SINGLE_SELECT:
		case Constant.SUBJECT_TYPE_JUDGE:
			point = 0.5f;

			break;
		case Constant.SUBJECT_TYPE_MULTI_SELECT:
			point = 1f;

			break;
		case Constant.SUBJECT_TYPE_ENTRY:
			point = 5f / answer.split("<<<").length;

			break;
		default:
			break;
		}

		return point;
	}

	/**
	 * 是否答完所有题
	 * 
	 * @param datas
	 * @return
	 */
	public boolean doneSubjects(List<SubjectBasicData> datas) {
		boolean done = true;
		for (SubjectBasicData data : datas) {
			int type = data.getType();
			String userAnswer = data.getUserAnswer();
			String remark = data.getRemark();

			if (type == Constant.SUBJECT_TYPE_SINGLE_SELECT || type == Constant.SUBJECT_TYPE_JUDGE) {
				if (userAnswer.equals("")) {// 未答
					done = false;
					break;
				}
			} else if (type == Constant.SUBJECT_TYPE_MULTI_SELECT || type == Constant.SUBJECT_TYPE_ENTRY) {
				if (remark.equals("0")) {// 未答
					done = false;
					break;
				}
			}
		}

		return done;
	}

	/**
	 * 根据parent_chapter_id,type获得本章各类型题
	 * 
	 * @param parent_chapter_id
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectParentIds(int parent_chapter_id, int type) {
		return SubjectBasicDataDao.getInstance(mContext).getDatasBySubjectParentIds(parent_chapter_id, type);

	}

	/**
	 * 根据randword抽取数据
	 * 
	 * @param randWord
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectRandWord(int parent_chapter_id, String randWord) {
		return SubjectBasicDataDao.getInstance(mContext).getDatasBySubjectRandWord(parent_chapter_id, randWord);

	}

	public List<SubjectBasicData> getRandWord(int parent_chapter_id) {
		return SubjectBasicDataDao.getInstance(mContext).getDatasRandWord(parent_chapter_id);

	}

	/**
	 * 获得当前章节测试所有题
	 * 
	 * @return
	 */
	public List<SubjectBasicData> getChapterData(int parent_chapter_id) {
		return SubjectBasicDataDao.getInstance(mContext).getChapterData(parent_chapter_id);
	}

	/**
	 * 复制基础数据
	 */
	public void insertBasicData(int server_id, int chapter_id, int type, String question, String option, String answer, String uanswer, String analysis, int is_correct, Float score, Float uscore) {
		ContentValues values = new ContentValues();
		values.put(SubjectBasicDataDao.SERVER_ID, server_id);
		values.put(SubjectBasicDataDao.CHAPTER_ID, chapter_id);
		values.put(SubjectBasicDataDao.TYPE, type);
		values.put(SubjectBasicDataDao.QUESTION, question);
		values.put(SubjectBasicDataDao.OPTION, option);
		values.put(SubjectBasicDataDao.ANSWER, answer);
		values.put(SubjectBasicDataDao.UANSWER, uanswer);
		values.put(SubjectBasicDataDao.ANALYSIS, analysis);
		values.put(SubjectBasicDataDao.IS_CORRECT, is_correct);
		values.put(SubjectBasicDataDao.SCORE, score);
		values.put(SubjectBasicDataDao.USCORE, uscore);
		SubjectBasicDataDao.getInstance(mContext).insertData(values);
	}

	/**
	 * 根据id获取数据
	 * 
	 * @param id
	 * @return
	 */
	public SubjectBasicData getAllDataById(int id) {
		return SubjectBasicDataDao.getInstance(mContext).getAllDataById(id);

	}

	/**
	 * 获取最后一条数据
	 * 
	 * @param chapterId
	 * @param type
	 * @param server_id
	 * @return
	 */
	public SubjectBasicData getBasicLastData(int chapterId, int type, int server_id) {
		return SubjectBasicDataDao.getInstance(mContext).getBasicDataLast(chapterId, type, server_id);
	}

	/**
	 * 将本表的数据复制到本表
	 * 
	 * @param ids
	 * @return
	 */
	public List<SubjectBasicData> getCopyDatas(String ids, int createServerId) {
		return SubjectBasicDataDao.getInstance(mContext).getCopyDatas(ids, createServerId);
	}

	/**
	 * 获取拷贝到的新数据
	 * 
	 * @param limit
	 *            新数据范围
	 * @return
	 */
	public String[] getNewIds(int limit) {
		return SubjectBasicDataDao.getInstance(mContext).getNewIds(limit);
	}

	// /**
	// * 插入基础题型的题目数据到数据库
	// *
	// * @param chapterId
	// * @param topic
	// * @return
	// */
	// public int insertBasicSubject(int chapterId, TopicMVO topic) {
	// Log.i(TAG, "insertBasicSubject:" + topic);
	// ContentValues values = new ContentValues();
	// if (topic.getRichAnalysis() != null &&
	// !topic.getRichAnalysis().equals("")) {
	// values.put(SubjectBasicDataDao.ANALYSIS,
	// Img64TagHandler.getFromHtmlValues(mContext,
	// topic.getRichAnalysis().getText()));
	// }
	// values.put(SubjectBasicDataDao.CHAPTER_ID, chapterId);
	// List<AnswerMVO> richAnswerMVO = topic.getRichAnswerMVO();
	// StringBuffer option = new StringBuffer();
	// StringBuffer answer = new StringBuffer();
	// for (int i = 0; i < richAnswerMVO.size(); i++) {
	// option.append(richAnswerMVO.get(i).getAnswerId() + "." +
	// Img64TagHandler.getFromHtmlValues(mContext,
	// richAnswerMVO.get(i).getRichContent().getText()) + ">>>");
	// if (topic.getOldTopicType() != 3 && richAnswerMVO.get(i).getIsCorrect()
	// == 1) {
	// answer.append(richAnswerMVO.get(i).getAnswerId() + ">>>");
	// } else if (topic.getOldTopicType() == 3) {
	// answer.append(richAnswerMVO.get(i).getIsCorrect() + ">>>");
	// }
	// }
	//
	// if (answer != null && answer.length() > 3) {
	// values.put(SubjectBasicDataDao.ANSWER, answer.substring(0,
	// answer.length() - 3));
	// }
	// if (option != null && option.length() > 3) {
	// values.put(SubjectBasicDataDao.OPTION, option.substring(0,
	// option.length() - 3));
	// }
	// values.put(SubjectBasicDataDao.QUESTION,
	// Img64TagHandler.getFromHtmlValues(mContext,
	// topic.getRichQuestion().getText()));
	// values.put(SubjectBasicDataDao.SCORE, topic.getScore());
	// values.put(SubjectBasicDataDao.SERVER_ID, topic.getTopicId().intValue());
	// values.put(SubjectBasicDataDao.TYPE, topic.getOldTopicType());
	// values.put(SubjectBasicDataDao.IS_CORRECT, 0);
	// values.put(SubjectBasicDataDao.UANSWER, "");
	// values.put(BaseDataDao.REMARK, "");
	// values.put(SubjectBasicDataDao.USCORE, "");
	//
	// SubjectBasicDataDao.getInstance(mContext).insertData(values);
	//
	// return (int) SubjectBasicDataDao.getInstance(mContext).getMaxItemId();
	// }

	/**
	 * 更新此题是否做过
	 */
	public void updateRemark(String isDone, int id) {
		ContentValues values = new ContentValues();
		values.put(BaseDataDao.REMARK, isDone);
		SubjectBasicDataDao.getInstance(mContext).updateData(id, values);
	}
}
