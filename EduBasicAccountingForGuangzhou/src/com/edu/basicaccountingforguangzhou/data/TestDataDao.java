package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * 课后练习，随堂练习数据库操作类
 * 
 * @author lucher
 * 
 */
public class TestDataDao extends BaseDataDao {

	private static final String TAG = "TestDataDao";

	// 类别字段名
	public static final String SERVER_ID = "SERVER_ID";
	// 类别字段名
	public static final String TYPE = "TYPE";
	// 章节id
	public static final String CHAPTER_ID = "CHAPTER_ID";
	// 具体解释查看数据库设计文档
	public static final String SUBJECT_TYPE = "SUBJECT_TYPE";
	// 题目id字段名
	public static final String SUBJECT_ID = "SUBJECT_ID";
	// 答题状态字段名
	public static final String STATE = "STATE";
	// 具体解释查看数据库设计文档
	public static final String SEND_STATE = "SEND_STATE";
	public static final String ERROR_COUNT = "ERROR_COUNT";

	/**
	 * 自身引用
	 */
	private static TestDataDao instance = null;
	private int index = 1;

	private TestDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_TEST";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static TestDataDao getInstance(Context context) {
		if (instance == null)
			instance = new TestDataDao(context);
		return instance;
	}

	@Override
	public TestData parseCursor(Cursor curs) {
		int idIndex = curs.getColumnIndex(ID);
		int serverId = curs.getColumnIndex(SERVER_ID);
		int subTypeIndex = curs.getColumnIndex(SUBJECT_TYPE);
		int chapterId = curs.getColumnIndex(CHAPTER_ID);
		int typeIndex = curs.getColumnIndex(TYPE);
		int subjectId = curs.getColumnIndex(SUBJECT_ID);
		int stateIndex = curs.getColumnIndex(STATE);
		int remarkIndex = curs.getColumnIndex(REMARK);
		int errorCount = curs.getColumnIndex(ERROR_COUNT);
		TestData data = new TestData();

		data.setId(curs.getInt(idIndex));
		data.setServerId(curs.getInt(serverId));
		data.setChapterId(curs.getInt(chapterId));
		data.setType(curs.getInt(typeIndex));
		data.setSubjectType(curs.getInt(subTypeIndex));
		data.setSubjectId(curs.getString(subjectId));
		data.setState(curs.getInt(stateIndex));
		data.setRemark(curs.getString(remarkIndex));
		data.setSendState(curs.getInt(curs.getColumnIndex(SEND_STATE)));
		data.setErrorCount(curs.getInt(errorCount));
		return data;
	}

	/**
	 * 根据dataId取考试用时和ExamId
	 */
	public TestData getUseTime(int dataId) {
		TestData returnValue = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + dataId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				// curs.moveToFirst();
				curs.moveToLast();
				returnValue = new TestData();

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

	/**
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<TestData> getDatas(int chapterId, int type) {
		Cursor curs = null;
		List<TestData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type + " AND CHAPTER_ID = " + chapterId;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				datas = new ArrayList<TestData>(curs.getCount());
				index = 1;
				int indexSingel = 1;
				int indexMulti = 1;
				int indexJuge = 1;
				int indexChild = 0;
				boolean isChild = false;
				while (curs.moveToNext()) {
					TestData data = parseCursor(curs);
					if (!isChild) {
						datas.add(data);
					}
					if (data.getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT || data.getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT
							|| data.getSubjectType() == Constant.SUBJECT_TYPE_JUDGE) {
						isChild = false;
						SubjectBasicData basicData = (SubjectBasicData) SubjectBasicDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId()));
						data.setTitle(basicData.getQuestion());
						basicData.setSubjectIndex(index++);
						if (data.getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT) {
							basicData.setIndexName((indexSingel++) + "");
						} else if (data.getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT) {
							basicData.setIndexName((indexMulti++) + "");
						} else {
							basicData.setIndexName((indexJuge++) + "");
						}
						data.setData(basicData);
					} else if (data.getSubjectType() == Constant.SUBJECT_TYPE_ENTRY) {
						isChild = false;
						indexChild++;
						SubjectEntryData entryData = (SubjectEntryData) SubjectEntryDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId().split(">>>")[0]));
						if (entryData.getType() != 2) {
							data.setTitle(entryData.getQuestion());
							entryData.setSubjectIndex(index++);
							entryData.setIndexName((indexChild) + "");
							data.setData(entryData);
						} else if (entryData.getType() == 2) {// child
							data.setTitle(entryData.getQuestion());
							entryData.setIndexName((indexChild) + "-1");
							entryData.setSubjectIndex(index++);
							data.setData(entryData);
							datas.addAll(setData(entryData.getChildren(), indexChild));
							isChild = false;
						}
					} else if (data.getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
						isChild = false;
						indexChild++;
						SubjectBillData billData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId()));
						if (billData.getType() != 2) {
							data.setTitle(billData.getContent());
							billData.setSubjectIndex(index++);
							billData.setIndexName((indexChild) + "");
							data.setData(billData);
						} else {
							data.setTitle(billData.getContent());
							billData.setSubjectIndex(index++);
							billData.setIndexName((indexChild) + "_1");
							data.setData(billData);
							datas.addAll(setData(billData.getChildren(), indexChild));
							isChild = false;
						}
					}
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
	 * 解析具有子类的题
	 * 
	 * @param childing
	 *            children内容
	 * @param index
	 *            索引
	 */
	private List<TestData> setData(String childing, int indexName) {
		String[] children = childing.split("<");
		List<TestData> data = new ArrayList<TestData>();
		for (int i = 0; i < children.length; i++) {
			String[] child = children[i].split("_");
			if (child[0].equals("1")) {
				SubjectEntryData entryData = (SubjectEntryData) SubjectEntryDataDao.getInstance(mContext).getDataById(Integer.valueOf(child[1]));
				data.add(setChildrenEntryData(entryData, (indexName + "-" + (i + 2))));
			} else {
				SubjectBillData billData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext).getDataById(Integer.valueOf(child[1]));
				data.add(setChildrenBillData(billData, (indexName + "-" + (i + 2))));
			}
		}
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param billData
	 * @param index
	 */
	private TestData setChildrenBillData(SubjectBillData billData, String indexName) {
		TestData data = new TestData();
		data.setSubjectType(Constant.SUBJECT_TYPE_BILL);
		billData.setSubjectIndex(index++);
		billData.setIndexName(indexName);
		data.setData(billData);
		data.setTitle(billData.getContent());
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param entryData
	 * @param index
	 */
	private TestData setChildrenEntryData(SubjectEntryData entryData, String indexName) {
		TestData data = new TestData();
		data.setSubjectType(Constant.SUBJECT_TYPE_ENTRY);
		entryData.setSubjectIndex(index++);
		entryData.setIndexName(indexName);
		data.setData(entryData);
		data.setTitle(entryData.getQuestion());
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
	public TestData getDataForParentChapterId(int chapterId, int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE =" + type + "  AND  CHAPTER_ID  = " + chapterId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();// ToLast();
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public TestData getDataByChapterId(int chapterId) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME;
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<TestData> getAllDataForParentChapterId2(int parentChapterId, int type) {

		// TestData data = null;
		Cursor curs = null;
		List<TestData> listDatas = new ArrayList<TestData>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE =" + type + "  AND  PARENT_CHAPTER_ID  = " + parentChapterId + " ORDER BY ID DESC";
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

				// Log.d(TAG, "data:" + data);
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
	public TestData getDataParentChapterIdTest(int parentChapterId, int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   PARENT_CHAPTER_ID = " + parentChapterId + " AND  TYPE =" + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				// curs.moveToFirst();
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public TestData getDataByIdForTest(int Id) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + Id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				// curs.moveToLast();
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public TestData getDataByTypeForTests(int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				// curs.moveToLast();
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public TestData getDataLastRecord(int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
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
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<TestData> getDataByType(int type) {
		List<TestData> listDatas = new ArrayList<TestData>();
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
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
	 * 删除数据
	 * 
	 * @param table
	 *            表名
	 * @param whereClause
	 *            表示SQL语句中条件部分的语句
	 * @param whereArgs
	 *            表示占位符的值
	 * @return
	 */
	public int deleteDataBy(String whereClause, String[] whereArgs) {
		int result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		result = mDb.delete(TABLE_NAME, whereClause, whereArgs);
		mDb.close();
		return result;
	}

	/**
	 * 根据章节id和类别删除数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public void deleteDatasByChapterId(int serverId, int mChapterId, int type) {
		Cursor curs = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type + " AND SERVER_ID = " + serverId + " AND CHAPTER_ID = " + mChapterId;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				while (curs.moveToNext()) {
					int id = curs.getInt(curs.getColumnIndex(ID));
					int subjectType = curs.getInt(curs.getColumnIndex(SUBJECT_TYPE));
					int subjectId = curs.getInt(curs.getColumnIndex(SUBJECT_ID));
					if (subjectType == Constant.SUBJECT_TYPE_SINGLE_SELECT || subjectType == Constant.SUBJECT_TYPE_MULTI_SELECT || subjectType == Constant.SUBJECT_TYPE_JUDGE) {
						SubjectBasicDataDao.getInstance(mContext).deleteData(subjectId);
						this.deleteData(id);
					} else if (subjectType == Constant.SUBJECT_TYPE_ENTRY) {
						SubjectEntryDataDao.getInstance(mContext).deleteData(subjectId);
						this.deleteData(id);
					} else if (subjectType == Constant.SUBJECT_TYPE_BILL) {
						// SubjectBillDataDao.getInstance(mContext).deleteDatas(subjectId);
					}

				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
	}

}
