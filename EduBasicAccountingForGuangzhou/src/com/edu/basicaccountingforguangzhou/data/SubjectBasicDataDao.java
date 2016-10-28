package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

/**
 * 基础题型（单多判）数据库操作类
 * 
 * @author lucher
 * 
 */
public class SubjectBasicDataDao extends BaseDataDao {

	private static final String TAG = "SubjectBasicDataDao";

	/**
	 * 详细信息见doc/EduBasicAccounting_database.xls
	 */
	public final static String SERVER_ID = "SERVER_ID";
	public final static String CHAPTER_ID = "CHAPTER_ID";
	public final static String TYPE = "TYPE";
	public final static String QUESTION = "QUESTION";
	public final static String OPTION = "OPTION";
	public final static String ANSWER = "ANSWER";
	public final static String UANSWER = "UANSWER";
	public final static String ANALYSIS = "ANALYSIS";
	public static final String IS_CORRECT = "IS_CORRECT";
	public static final String SCORE = "SCORE";
	public static final String USCORE = "USCORE";
	// public static final String ID = "ID";

	/**
	 * 自身实例
	 */
	private static SubjectBasicDataDao instance = null;

	private SubjectBasicDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_SUBJECT_BASIC";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SubjectBasicDataDao getInstance(Context context) {
		if (instance == null)
			instance = new SubjectBasicDataDao(context);
		return instance;
	}
	
	

	/**
	 * 根据题库ids获取数据
	 * 
	 * @param ids
	 *            包含的题库id号们
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectIds(String ids) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID IN (" + ids + ")";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();

				int index = 1;
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					data.setSubjectIndex(index++);
					datas.add(data);

					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 根据题库ids获取基础题目数据
	 * 
	 * @param ids
	 *            包含的题库id号们
	 * @return
	 */
	public List<SubjectBasicData> getBasicDatasBySubjectIds(String ids) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID IN (" + ids + ") ";
			// String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID IN (" +
			// ids + ") AND TYPE !=4 ";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();

				int index = 1;
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					data.setSubjectIndex(index++);
					datas.add(data);

					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 根据题库ids获取基础题目数据
	 * 
	 * @param ids
	 *            包含的题库id号们
	 * @return
	 */
	public List<SubjectBasicData> getBasicDatasBySubjectIdsForFenLu(String ids) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID IN (" + ids + ") AND TYPE =4 ";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();

				int index = 1;
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					data.setSubjectIndex(index++);
					datas.add(data);

					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 根据题库chapterId获取数据
	 * 
	 * @param id
	 *            包含的题库id
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectParentIds(int chapterId, int type) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " SELECT  TB_SUBJECT_BASIC.ID ,TB_SUBJECT_BASIC.CHAPTER_ID,TB_SUBJECT_BASIC.QUESTION,TB_SUBJECT_BASIC.ANALYSIS,TB_SUBJECT_BASIC.ANSWER,TB_SUBJECT_BASIC.IS_CORRECT,TB_SUBJECT_BASIC.OPTION,TB_SUBJECT_BASIC.SCORE,TB_SUBJECT_BASIC.SERVER_ID, TB_SUBJECT_BASIC.TYPE,TB_SUBJECT_BASIC.UANSWER,TB_SUBJECT_BASIC.USCORE, TB_SUBJECT_BASIC.REMARK  FROM "
					+ TABLE_NAME + " LEFT JOIN TB_CHAPTER ON TB_SUBJECT_BASIC.CHAPTER_ID  = TB_CHAPTER.ID WHERE PARENT_ID  = " + chapterId + " AND TYPE  =  " + type; //+ " AND SERVER_ID = " + server_id;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 获得所有数据
	 * 
	 * @param chapterId
	 * @param type
	 * @return
	 */
	public List<SubjectBasicData> getAllDatas() {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);

					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 修改题目id在ids里的用户答案为answer 还需要把remark的值变为0
	 * 
	 * @param ids
	 * @param answer
	 */
	public void updateUserAnswer(String ids, String answer) {
		String sql = "UPDATE " + TABLE_NAME + " SET " + UANSWER + "= '" + answer + "',REMARK=0 WHERE ID IN (" + ids + ")";
		Log.d(TAG, "sql:" + sql);
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.execSQL(sql);
	}

	/**
	 * 根据题库parent_id获取数据
	 * 
	 * @param id
	 *            包含的题库id
	 * @return
	 */
	public List<SubjectBasicData> getDatasBySubjectRandWord(int chapterId, String randWord) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  RAND_WORD =? " + "AND CHAPTER_ID= " + chapterId + " ORDER BY TYPE";
			Log.d(TAG, "sql:" + sql);
			String[] bStrings = { randWord };
			curs = mDb.rawQuery(sql, bStrings);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 查询rand_word字段
	 * 
	 * @param randWord
	 * @return
	 */
	public List<SubjectBasicData> getDatasRandWord(int chapterId) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE CHAPTER_ID = " + chapterId + " GROUP BY RAND_WORD HAVING RAND_WORD IS NOT NULL and RAND_WORD <> \"\" ";
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 获得当前章的所有题
	 * 
	 * @param chapterId
	 * @return
	 */
	public List<SubjectBasicData> getChapterData(int chapterId) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " select * from " + TABLE_NAME + " left join TB_CHAPTER on TB_SUBJECT_BASIC.chapter_id = TB_CHAPTER.id where parent_id = " + chapterId;
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 获得当前章的所有题
	 * 
	 * @param chapterId
	 * @return
	 */
	public SubjectBasicData getAllDataById(int id) {
		Cursor curs = null;
		SubjectBasicData datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " select * from " + TABLE_NAME + " where id = " + id;
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				datas = parseCursor(curs);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 获得最后一条数据
	 * 
	 */
	public SubjectBasicData getBasicDataLast(int chapterId, int type, int server_id) {

		SubjectBasicData data = null;
		Cursor curs = null;
		String sql = " SELECT  *  FROM " + TABLE_NAME + " WHERE CHAPTER_ID  = " + chapterId + " AND TYPE  =  " + type + " AND SERVER_ID = " + server_id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
	}

	/**
	 * 将本表的数据复制到本表
	 * 
	 * @return
	 */
	public List<SubjectBasicData> getCopyDatas(String ids, int createServerId) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " INSERT INTO " + TABLE_NAME + " ( SERVER_ID, CHAPTER_ID, TYPE, QUESTION, OPTION, ANSWER, UANSWER, ANALYSIS, IS_CORRECT, SCORE, USCORE, REMARK ) SELECT  " + createServerId
					+ ", CHAPTER_ID,TYPE,QUESTION,OPTION,ANSWER,UANSWER,ANALYSIS,IS_CORRECT,SCORE,USCORE,REMARK  FROM  TB_SUBJECT_BASIC WHERE ID IN " + " ( " + ids + " )";
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String[] getNewIds(int limit) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		String[] ids = new String[limit];
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " SELECT ID FROM  ( SELECT ID FROM  " + TABLE_NAME + " ORDER BY ID DESC LIMIT " + limit + " ) ORDER BY RANDOM()";
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				int index = 0;
				while (curs.moveToNext()) {
					// SubjectBasicData data = parseCursor(curs);
					SubjectBasicData data = new SubjectBasicData();
					data.setId(curs.getInt(curs.getColumnIndex(ID)));
					ids[index++] = String.valueOf(data.getId());
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return ids;
	}

	/**
	 * 将本表的数据复制到本表,单条数据
	 * 
	 * @return
	 */
	public void getCopyOneDatas(int ids, int createServerId) {
		Cursor curs = null;
		List<SubjectBasicData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " INSERT INTO " + TABLE_NAME + " ( SERVER_ID, CHAPTER_ID, TYPE, QUESTION, OPTION, ANSWER, UANSWER, ANALYSIS, IS_CORRECT, SCORE, USCORE, REMARK ) SELECT  " + createServerId
					+ ", CHAPTER_ID,TYPE,QUESTION,OPTION,ANSWER,' ',ANALYSIS,0,SCORE,0,0  FROM  TB_SUBJECT_BASIC WHERE ID =" + ids;
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBasicData>();
				while (curs.moveToNext()) {
					SubjectBasicData data = parseCursor(curs);
					datas.add(data);
					Log.i(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		// return datas;
	}

	/**
	 * 获取最后一条数据
	 * 
	 * @return
	 */
	public SubjectBasicData getDataLast() {

		SubjectBasicData data = null;
		Cursor curs = null;
		String sql = "SELECT ID FROM " + TABLE_NAME;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				// data = parseCursor(curs);
				data = new SubjectBasicData();
				int idIndex = curs.getColumnIndex(ID);
				data.setId(curs.getInt(idIndex));
				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
	}

	/**
	 * 获取基础题答案
	 * 
	 * @return
	 * @param isWord
	 *            判断是否是套题的标志
	 * @param id
	 *            题目id号
	 */
	public int computeGrade(int id, boolean isWord) {
		int totalScore = 0;
		SubjectBasicData data = null;
		Cursor curs = null;
		String sql = "SELECT *  FROM " + TABLE_NAME + " WHERE ID = " + id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return 0;
				curs.moveToFirst();
				data = new SubjectBasicData();
				int idIndex = curs.getColumnIndex(ID);
				data.setId(curs.getInt(idIndex));
				data.setRight(curs.getInt(curs.getColumnIndex(IS_CORRECT)) == 1 ? true : false);
				data.setType(curs.getInt(curs.getColumnIndex(TYPE)));
				data.setScore(curs.getFloat(curs.getColumnIndex(SCORE)));
				Log.e("得分专用Log", "获取得分1" + data.getScore());

				if (data.getType() == Constant.SUBJECT_TYPE_SINGLE_SELECT || data.getType() == Constant.SUBJECT_TYPE_JUDGE) {
					if (data.isRight()) {
						if (isWord) {// 套题模式
							totalScore = (int) data.getScore();
						} else {// 随机模式
							totalScore = 1;
						}
					}
				} else {
					if (data.isRight()) {
						if (isWord) {// 套题模式
							totalScore = (int) data.getScore();
						} else {// 随机模式
							totalScore = 2;
						}
					}
				}
				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return totalScore;
	}



	

	public SubjectBasicData getData(int subjectId, SQLiteDatabase db) {
		SubjectBasicData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + subjectId;
		try {
			curs = db.rawQuery(sql, null);
			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return data;
	}

	@Override
	public SubjectBasicData parseCursor(Cursor curs) {
		SubjectBasicData subjectData = new SubjectBasicData();
		subjectData.setId(curs.getInt(0));
		subjectData.setChapterId(curs.getInt(1));
		subjectData.setFlag(curs.getInt(2));
		subjectData.setSubjectType(curs.getInt(3));
		subjectData.setQuestion(curs.getString(4));
		subjectData.setOption(curs.getString(5));
		subjectData.setAnswer(curs.getString(6));
		subjectData.setUserAnswer(curs.getString(7));
		subjectData.setAnalysis(curs.getString(8));
	//	subjectData.setErrorCount(curs.getInt(9));
		subjectData.setScore(curs.getInt(10));
		subjectData.setFavorite(curs.getInt(9));
		subjectData.setuScore(curs.getInt(11));

		subjectData.setRemark(curs.getString(12));
		//Log.e("得分专用Log", "获取得分3---" + subjectData.getScore() + "id 是" + subjectData.getQuestion() );


		return subjectData;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
