package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

public class RecordDataDao extends BaseDataDao {

	private static final String TAG = "RecordDataDao";

	public static final String ID = "ID";
	// 类别字段名
	public static final String TEST_ID = "TEST_ID";
	// 类别字段名
	public static final String TYPE = "TYPE";
	// 基础题
	public static final String BASIC_IDS = "BASIC_IDS";
	// 填制、分录题
	public static final String OTHER_IDS = "OTHER_IDS";
	// 考试名
	public static final String TEST_NAME = "TEST_NAME";
	// 测试时间
	public static final String TEST_TIME = "TEST_TIME";
	// 总时间
	public static final String TOTAL_TIME = "TOTAL_TIME";
	// 已用时间
	public static final String USED_TIME = "USED_TIME";
	// 数据库文档有说明
	public static final String TEST_CONFIG = "TEST_CONFIG";
	// 分数
	public static final String SCORE = "SCORE";
	// 答题状态字段名
	public static final String STATE = "STATE";
	// 具体解释查看数据库设计文档
	public static final String SEND_STATE = "SEND_STATE";
	// 用户分数
	public static final String USCORE = "USCORE";
	// 在线考试是否可查看成绩
	public static final String LOOK_MODE = "LOOK_MODE"; 
	/**
	 * 自身引用
	 */
	private static RecordDataDao instance = null;

	private RecordDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_RECORD";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static RecordDataDao getInstance(Context context) {
		if (instance == null)
			instance = new RecordDataDao(context);
		return instance;
	}

	@Override
	public RecordData parseCursor(Cursor curs) {
		int idIndex = curs.getColumnIndex(ID);
		int testId = curs.getColumnIndex(TEST_ID);
		int typeIndex = curs.getColumnIndex(TYPE);
		int basicIndex = curs.getColumnIndex(BASIC_IDS);
		int otherIndex = curs.getColumnIndex(OTHER_IDS);
		int testName = curs.getColumnIndex(TEST_NAME);
		int stateIndex = curs.getColumnIndex(STATE);
		int testTimeIndex = curs.getColumnIndex(TEST_TIME);
		int totalTime = curs.getColumnIndex(TOTAL_TIME);
		int usedTime = curs.getColumnIndex(USED_TIME);
		int testConfig = curs.getColumnIndex(TEST_CONFIG);
		int score = curs.getColumnIndex(SCORE);
		int sendState = curs.getColumnIndex(SEND_STATE);
		int uScore = curs.getColumnIndex(USCORE);
		int lookMode = curs.getColumnIndex(LOOK_MODE);
		int remarkIndex = curs.getColumnIndex(REMARK);
		RecordData data = new RecordData();

		data.setId(curs.getInt(idIndex));
		data.setTestId(curs.getInt(testId));
		data.setType(curs.getInt(typeIndex));
		data.setBasic_id(curs.getString(basicIndex));
		data.setOther_id(curs.getString(otherIndex));
		data.setTestName(curs.getString(testName));
		data.setState(curs.getInt(stateIndex));
		data.setRemark(curs.getString(remarkIndex));
		data.setSendState(curs.getInt(sendState));
		data.setTestTime(curs.getString(testTimeIndex));
		data.setTotalTime(curs.getInt(totalTime));
		data.setUsedTime(curs.getInt(usedTime));
		data.setTestContfig(curs.getString(testConfig));
		data.setScore(curs.getInt(score));
		data.setuScore(curs.getInt(uScore));
		data.setLookMode(curs.getInt(lookMode)== 1 ? true : false); 
		return data;
	}

	/**
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<RecordData> getRandWordDatas(int chapterId, int type) {
		Cursor curs = null;
		List<RecordData> listDatas = new ArrayList<RecordData>();
		String sql = " SELECT * FROM ( SELECT * FROM  " + TABLE_NAME + " ORDER BY ID DESC )  WHERE   TYPE = " + type + "  AND  TEST_ID  =  " + chapterId + " GROUP BY REMARK ";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
	}

	/**
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public RecordData getRandWordData(int chapterId, String randWord) {
		Cursor curs = null;
		RecordData listDatas = null;
//		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE =" + type + "  AND  TEST_ID  = " + chapterId + " AND REMARK = ? ";
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TEST_ID  = " + chapterId + " AND REMARK = ? ";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			String[] strings = { randWord };
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, strings);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				listDatas = parseCursor(curs);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
	}

	/**
	 * 获取最后一条数据
	 * 
	 * @param chapterId
	 * @param type
	 * @param server_id
	 * @return
	 */
	public RecordData getDataLast() {

		RecordData data = null;
		Cursor curs = null;
		String sql = " SELECT * FROM " + TABLE_NAME;
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
	 * 获得固定套题id号
	 * 
	 * @param testId
	 *            章节id
	 * @param word
	 *            套题名称
	 * @return
	 */
	public int getWordId(int testId, String word) {

		RecordData data = null;
		Cursor curs = null;
		String sql = " SELECT * FROM " + TABLE_NAME + " WHERE TEST_ID = " + testId + " AND REMARK = ?";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String[] strings = { word };
			curs = mDb.rawQuery(sql, strings);

			if (curs != null) {
				if (curs.getCount() == 0)
					return 0;
				curs.moveToLast();
				data = new RecordData();
				int idIndex = curs.getColumnIndex(ID);
				int remarkIndex = curs.getColumnIndex(REMARK);
				data.setId(curs.getInt(idIndex));
				data.setRemark(curs.getString(remarkIndex));
				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data.getId();
	}

	/**
	 * 根据章节id和类别、完成状态获取数据，测试用
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<RecordData> getDatasForState(int testType, int chapterId) {
		Cursor curs = null;
		List<RecordData> listDatas = new ArrayList<RecordData>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE    ( TYPE = " + testType + "  OR TYPE = -2 )  AND  TEST_ID  = " + chapterId + " AND  ( STATE = 1  OR  STATE = 2 )  ORDER BY ID DESC";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
	}

	/**
	 * 根据章节id和类别、完成状态获取数据,练习用
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<RecordData> getDatasForTestType(int testType, int chapterId) {
		Cursor curs = null;
		List<RecordData> listDatas = new ArrayList<RecordData>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE    TYPE = " + testType + "  AND  TEST_ID  = " + chapterId + " AND  ( STATE = 1  OR  STATE = 2 )  ORDER BY ID DESC";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return listDatas;
	}

	/**
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public RecordData getDataForChapterId(int chapterId, int type) {

		RecordData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE = " + type + " OR TYPE = -2" + " AND  TEST_ID  = " + chapterId;
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
	 * 复制数据
	 */
	public void copyDataForWord(String word, int id) {
		RecordData data = null;
		Cursor curs = null;

		String sql = " INSERT INTO  " + TABLE_NAME + "  ( TEST_ID,TYPE,BASIC_IDS,OTHER_IDS,TEST_NAME,TEST_TIME,TOTAL_TIME,USED_TIME,TEST_CONFIG,"
				+ "STATE,SEND_STATE,SCORE,USCORE,REMARK) SELECT  TEST_ID,TYPE,BASIC_IDS,OTHER_IDS, 0,0,0,0,0,0,0,0,0, ? FROM " + TABLE_NAME + " WHERE ID = " + id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String[] strings = { word };
			curs = mDb.rawQuery(sql, strings);

			if (curs != null) {
				if (curs.getCount() == 0)
					return;
				curs.moveToFirst();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return;
	}

	/**
	 * 根据paperId获取数据
	 * 
	 * @return
	 */
	public RecordData getDatabyTestId(int testId) {

		RecordData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TEST_ID = " + testId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
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
	 * 根据类别、完成状态获取数据
	 * 
	 * @param type
	 *            类别
	 * @return
	 */
	public List<RecordData> getDatasForType(int testType) {
		Cursor curs = null;
		List<RecordData> listDatas = new ArrayList<RecordData>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE    TYPE = " + testType + " AND ( STATE =1  or STATE= 2 ) ORDER BY ID DESC";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
	}

	/**
	 * 在线考试最后一条，用于继续考试
	 * 
	 * @param testType
	 * @return
	 */
	public RecordData getDataForTypeLast(int testType) {

		RecordData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE    TYPE = " + testType;
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
	 * 根据dataId取考是否可查看答案
	 */
	public Boolean getSendState(int dataId) {
		Boolean returnValue = false;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + dataId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				int testSendStateIndex = curs.getColumnIndex(LOOK_MODE);
				returnValue = curs.getInt(testSendStateIndex) == 1 ? true : false;

				// Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return returnValue;
	}
	
	/**
	 * 更新数据
	 * 
	 * @param id
	 * @param values
	 */
	public void updateDataByDataId(int DataId, ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateData:" + DataId);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.update(TABLE_NAME, values, ID + "=?", new String[] { String.valueOf(DataId) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}
}
