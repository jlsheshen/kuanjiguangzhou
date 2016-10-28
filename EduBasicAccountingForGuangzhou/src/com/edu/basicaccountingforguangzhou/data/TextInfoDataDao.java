package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

public class TextInfoDataDao extends BaseDataDao {

	private static final String TAG = "TextInfoDataDao";

	public static final String ID = "ID";
	public static final String NAME = "NAME";// 试题名称
	public static final String LOCK = "LOCK";// 是否解锁
	public static final String DONE = "DONE";// 是否完成
	public static final String SCORE = "SCORE";// 分数

	/**
	 * 自身引用
	 */
	private static TextInfoDataDao instance = null;

	private TextInfoDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_TEST_INFO";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static TextInfoDataDao getInstance(Context context) {
		if (instance == null)
			instance = new TextInfoDataDao(context);
		return instance;
	}

	/**
	 * 获得书的名称数据
	 * 
	 * @param type
	 * @return
	 */

	public List<TextInfoData> getInfoDatas() {
		Cursor curs = null;
		List<TextInfoData> data = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				data = new ArrayList<TextInfoData>(curs.getCount());
				while (curs.moveToNext()) {
					data.add(parseCursor(curs));
					Log.d(TAG, "data:" + data);
				}
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
	 * 查询当前是否锁定
	 * 
	 * @param id
	 * @param values
	 */
	public int querLock(int id) {
		Cursor curs = null;
		int lock = 0;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT LOCK FROM " + TABLE_NAME + " WHERE ID =  '" + id + "'";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.moveToNext()) {
				lock = curs.getInt(curs.getColumnIndex(LOCK));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return lock;

	}

	/**
	 * 查询当前试题状态
	 *
	 * @param id
	 * @param values
	 */
	public int querState(int id) {
		Cursor curs = null;
		int done = 0;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT DONE FROM " + TABLE_NAME + " WHERE ID = '" + id + "'";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.moveToNext()) {
				done = curs.getInt(curs.getColumnIndex(DONE));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return done;

	}

	/**
	 * 更新是否锁定标识
	 * 
	 * @param
	 */
	public void upLockFlag(int id) {
		// 是否上传的flag 0是为没传；1是上传
		String sql = "UPDATE " + TABLE_NAME + " SET " + " LOCK = " + 1 + " WHERE ID IN (" + id + ")";
		Log.d(TAG, "sql:" + sql);
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.execSQL(sql);
	}

	/**
	 * 更新是否完成
	 * 
	 * @param
	 */
	public void upDoneFlag(int id, int done) {
		// 是否上传的flag 0是为没传；1是上传
		String sql = "UPDATE " + TABLE_NAME + " SET " + " DONE = " + done + " WHERE ID IN (" + id + ")";
		Log.d(TAG, "sql:" + sql);
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.execSQL(sql);
	}

	/**
	 * 插入分数信息
	 * 
	 * @param
	 */
	public void insertScore(int id, int score) {
		// 是否上传的flag 0是为没传；1是上传
		String sql = "UPDATE " + TABLE_NAME + " SET " + " SCORE = " + score + " WHERE ID IN (" + id + ")";
		Log.d(TAG, "sql:" + sql);
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.execSQL(sql);
	}

	@Override
	public TextInfoData parseCursor(Cursor curs) {
		TextInfoData data = new TextInfoData();
		data.setId(curs.getInt(curs.getColumnIndex(ID)));
		data.setName(curs.getString(curs.getColumnIndex(NAME)));
		data.setLock(curs.getInt(curs.getColumnIndex(LOCK)));
		data.setDone(curs.getInt(curs.getColumnIndex(DONE)));
		data.setScore(curs.getInt(curs.getColumnIndex(SCORE)));
		return data;
	}

}
