package com.edu.basicaccountingforguangzhou.data;

import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;

/**
 * 数据库操作dao层基类
 * 
 * @author lucher
 * 
 */
public abstract class BaseDataDao {

	public static final String TAG = "BaseDataDao";



	/**
	 * 数据库名称
	 */
	protected String dbName;

	/**
	 * id
	 */
	
	
	/**
	 * 数据库
	 */
	public SQLiteDatabase mDb;

	public Context mContext;

	/**
	 * id
	 */
	public static String ID = "ID";
	
	public static String SUBJECT_ID = "SUBJECT_ID";
	/**
	 * 备注，预留字段
	 */
	public final static String REMARK = "REMARK";
	/**
	 * 数据库表名
	 */
	public String TABLE_NAME;

	public BaseDataDao(Context context) {
		mContext = context;
	}
	protected BaseDataDao(Context context, String dbName) {
		mContext = context;
		this.dbName = dbName;
	//	setTableName();
	}

	/**
	 * 设置table名称
	 */
//	public abstract void setTableName();


	/**
	 * 解析cursor
	 * 
	 * @param curs
	 * @return
	 */
	public abstract BaseData parseCursor(Cursor curs);

	/**
	 * 根据id获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public synchronized BaseData getDataById(int id) {
		BaseData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
//				curs.moveToFirst();
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
	 * 通过Test_ID将sendExamID存入Remark字段
	 * 
	 * @param testID
	 * @param values
	 */
	public synchronized void updateDataByTestID(int testID, ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateDataByTestID:" + testID);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.update(TABLE_NAME, values, "TEST_ID" + "=?", new String[] { String.valueOf(testID) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}

	/**
	 * 更新数据
	 * 
	 * @param id
	 * @param values
	 */
	public synchronized void updateData(int id, ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateData:" + id);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.update(TABLE_NAME, values, ID + "=?", new String[] { String.valueOf(id) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param values
	 */
	public synchronized void insertData(ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-insertData");
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.insert(TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param values
	 */
	public synchronized void bulkInsertData(List<ContentValues> valuesList) {
		try {
			Log.d(TAG, TABLE_NAME + "-bulkInsertData");
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.beginTransaction();
			for (ContentValues values : valuesList) {
				mDb.insert(TABLE_NAME, null, values);
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mDb.endTransaction();
			closeDb(mDb);
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 */
	public synchronized void deleteData(int id) {
		try {
			Log.e(TAG, TABLE_NAME + "-deleteData:" + id);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.delete(TABLE_NAME, ID + "=?", new String[] { String.valueOf(id) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}

	/**
	 * 
	 * 清除所有数据
	 */
	public boolean deleteAllDatas() {
		boolean result = true;
		try {
			Log.e(TAG, TABLE_NAME + "-deleteAllDatas");
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.delete(TABLE_NAME, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			closeDb(mDb);
		}
		return result;
	}

	/**
	 * 关闭数据库以及cursor
	 * 
	 * @param db
	 * @param curs
	 */
	public void closeDb(SQLiteDatabase db, Cursor curs) {
		closeDb(db);
		if (curs != null) {
			curs.close();
			curs = null;
		}
	}

	/**
	 * 关闭数据库
	 * 
	 * @param db
	 */
	public void closeDb(SQLiteDatabase db) {
		if (mDb != null) {
			mDb.close();
		}
	}

	/**
	 * 获取最大id
	 * 
	 * @return
	 */
	public long getMaxItemId() {
		final int maxIdIndex = 0;
		long id = -1;
		Cursor curs = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery("SELECT MAX(" + ID + ") FROM " + TABLE_NAME, null);

			if (curs != null && curs.moveToNext()) {
				id = curs.getLong(maxIdIndex);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return id;
	}
	/**
	 * 通过Ques_ID更新数据
	 * 
	 * @param testID
	 * @param values
	 */
	public synchronized void updateDataByQuesID(int quesID, ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateDataByQuesID:" + quesID);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.update(TABLE_NAME, values, "question_id" + "=?", new String[] { String.valueOf(quesID) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}
}
