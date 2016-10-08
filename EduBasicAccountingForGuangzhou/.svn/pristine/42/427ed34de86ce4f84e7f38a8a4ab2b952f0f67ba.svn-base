package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * 单据题型数据操作dao
 * 
 * @author lucher
 * 
 */
public class SubjectBillDataDao extends BaseDataDao {

	private static final String TAG = "SubjectBillDataDao";

	/**
	 * 详细信息见doc/EduBasicAccounting_database.xls
	 */
	public static final String ID = "id";
	public static final String SERVER_ID = "SERVER_ID";
	public static final String CHAPTER_ID = "CHAPTER_ID";
	public static final String BASEMAP_ID = "basemap_id";
	public static final String CONTENT = "content";
	public static final String IS_COMPLETED = "is_completed";
	public static final String SHOW_FLAG = "show_flag";
	public static final String PIC_NAME = "pic_name";
	public static final String TYPE = "TYPE";
	public static final String CHILDREN = "CHILDREN";
	public static final String SCORE = "score";
	public static final String USCORE = "uscore";

	/**
	 * 自身实例
	 */
	private static SubjectBillDataDao instance = null;

	public SubjectBillDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_SUBJECT_BILL";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SubjectBillDataDao getInstance(Context context) {
		if (instance == null)
			instance = new SubjectBillDataDao(context);
		return instance;
	}

	/**
	 * 获得所有数据
	 * 
	 * @param chapterId
	 * @param type
	 * @return
	 */
	public List<SubjectBillData> getAllDatasByType(int chapterId) {
		Cursor curs = null;
		List<SubjectBillData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " SELECT TB_SUBJECT_BILL.id,TB_SUBJECT_BILL.SERVER_ID,TB_SUBJECT_BILL.CHAPTER_ID,TB_SUBJECT_BILL.TYPE,TB_SUBJECT_BILL.CHILDREN,TB_SUBJECT_BILL.basemap_id,TB_SUBJECT_BILL.content,TB_SUBJECT_BILL.is_completed,TB_SUBJECT_BILL.show_flag,TB_SUBJECT_BILL.pic_name ,TB_SUBJECT_BILL.score,TB_SUBJECT_BILL.uscore  FROM "
					+ TABLE_NAME + " LEFT JOIN TB_CHAPTER ON TB_SUBJECT_BILL.CHAPTER_ID = TB_CHAPTER.ID WHERE PARENT_ID  =  " + chapterId + " AND TYPE  = 2 " + " OR  TYPE  =  0 ";
			// + " AND TYPE = 2 AND SERVER_ID = "
			// + server_id
			// + " OR TYPE = 0 AND SERVER_ID = " + server_id;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<SubjectBillData>();
				while (curs.moveToNext()) {
					SubjectBillData data = parseCursor(curs);
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
	 * 获得数据
	 * 
	 * @param type
	 * @return
	 */
	public SubjectBillData getBillDatas(int id) {
		Cursor curs = null;
		SubjectBillData data = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID =" + id;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				data = parseCursor(curs);

				Log.i(TAG, "data:" + data);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return data;
	}

	/**
	 * 获得最后一条数据
	 * 
	 * @param chapterId
	 * @param type
	 * @param server_id
	 * @return
	 */
	public SubjectBillData getBillDataLast(int chapterId, int type, int server_id) {

		SubjectBillData data = null;
		Cursor curs = null;
		String sql = " SELECT * FROM " + TABLE_NAME + "  WHERE CHAPTER_ID  = " + chapterId + " AND TYPE =  " + type + " AND SERVER_ID = " + server_id;
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
	 * 拷贝
	 * 
	 * @param ids
	 * @param cleanUserAnswer
	 * @return
	 */
	public void getCopyBilData(int id, int createServerId) {
		Cursor curs = null;
		SubjectBillData data = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " INSERT INTO " + TABLE_NAME + " ( SERVER_ID,CHAPTER_ID,TYPE,CHILDREN,BASEMAP_ID,CONTENT,IS_COMPLETED, SHOW_FLAG,PIC_NAME ,SCORE,USCORE ) SELECT " + createServerId
					+ ", CHAPTER_ID,TYPE,CHILDREN,BASEMAP_ID,CONTENT,0, SHOW_FLAG,PIC_NAME ,SCORE,0  FROM " + TABLE_NAME + " WHERE ID = " + id;

			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				data = parseCursor(curs);

				Log.i(TAG, "data:" + data);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		// return data;
	}

	/**
	 * 获取最后一条数据
	 * 
	 * @return
	 */
	public SubjectBillData getDataLast() {

		SubjectBillData data = null;
		Cursor curs = null;
		String sql = "SELECT ID,TYPE,CHILDREN FROM " + TABLE_NAME;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				// data = parseCursor(curs);
				data = new SubjectBillData();
				int idIndex = curs.getColumnIndex(ID);
				data.setId(curs.getInt(idIndex));
				data.setType(curs.getInt(curs.getColumnIndex(TYPE)));
				data.setChildren(curs.getString(curs.getColumnIndex(CHILDREN)));
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
	 * 获取数据
	 * 
	 * @return
	 */
	public SubjectBillData getDataForId(int id) {

		SubjectBillData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				data = new SubjectBillData();
				int idIndex = curs.getColumnIndex(ID);
				data.setId(curs.getInt(idIndex));
				data.setChildren(curs.getString(curs.getColumnIndex(CHILDREN)));
				data.setServerId(curs.getInt(curs.getColumnIndex(SERVER_ID)));
				data.setScore(curs.getFloat(curs.getColumnIndex(SCORE)));
				data.setCompleted(curs.getInt(curs.getColumnIndex(IS_COMPLETED)) == 0 ? false : true);
				data.setuScore(curs.getFloat(curs.getColumnIndex(USCORE)));
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
	 * 获取所有id号的list
	 * 
	 * @return
	 */
	@Override
	public SubjectBillData parseCursor(Cursor curs) {
		SubjectBillData data = new SubjectBillData();
		data.setId(curs.getInt(curs.getColumnIndex(ID)));
		data.setServerId(curs.getInt(curs.getColumnIndex(SERVER_ID)));
		data.setChapterId(curs.getInt(curs.getColumnIndex(CHAPTER_ID)));
		data.setBasemapId(curs.getInt(curs.getColumnIndex(BASEMAP_ID)));
		// data.setBillId(curs.getInt(curs.getColumnIndex(BILL_ID)));
		// data.setBillTypeId(curs.getInt(curs.getColumnIndex(BILL_TYPE_ID)));
		data.setCompleted(curs.getInt(curs.getColumnIndex(IS_COMPLETED)) == 0 ? false : true);
		data.setContent(curs.getString(curs.getColumnIndex(CONTENT)));
		data.setPicName(curs.getString(curs.getColumnIndex(PIC_NAME)));
		data.setType(curs.getInt(curs.getColumnIndex(TYPE)));
		data.setChildren(curs.getString(curs.getColumnIndex(CHILDREN)));
		data.setuScore(curs.getFloat(curs.getColumnIndex(USCORE)));
		data.setScore(curs.getFloat(curs.getColumnIndex(SCORE)));

		return data;
	}

	/**
	 * 删除单据题及关联数据
	 * 
	 * @param id
	 */
	public void deleteDatas(int id) {
		try {
			Log.e(TAG, TABLE_NAME + "-deleteData:" + id);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.delete(TABLE_NAME, ID + "=?", new String[] { String.valueOf(id) });
			ViewDataDao.getInstance(mContext).deleteDataByQuesionId(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}
}
