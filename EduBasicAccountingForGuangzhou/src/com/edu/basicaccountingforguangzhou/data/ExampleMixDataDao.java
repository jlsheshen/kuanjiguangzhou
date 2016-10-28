package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;

public class ExampleMixDataDao extends BaseDataDao {
	// 内容
	public static final String CONTENT = "CONTENT";
	// 内容类型
	public static final String TYPE = "TYPE";
	/**
	 * 自身引用
	 */
	private static ExampleMixDataDao instance = null;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static ExampleMixDataDao getInstance(Context context) {
		if (instance == null)
			instance = new ExampleMixDataDao(context);
		return instance;
	}

	private ExampleMixDataDao(Context context) {
		super(context);
		TABLE_NAME = "Example_MIX_CONTENT";
	}

	public List<ExampleMixData> getDatasByExampleMixIds(String ids) {
		Cursor curs = null;
		List<ExampleMixData> datas = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID IN (" + ids + ")";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<ExampleMixData>();

				while (curs.moveToNext()) {
					ExampleMixData data = parseCursor(curs);
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

	@Override
	public ExampleMixData parseCursor(Cursor curs) {
		// TODO Auto-generated method stub
		ExampleMixData data = new ExampleMixData();
		int idIndex = curs.getColumnIndex(ID);
		int contentIndex = curs.getColumnIndex(CONTENT);
		int typeIndex = curs.getColumnIndex(TYPE);
		data.setId(curs.getInt(idIndex));
		data.setContent(curs.getString(contentIndex));
		data.setType(curs.getInt(typeIndex));
		return data;
	}

//	@Override
//	public void setTableName() {
//		TABLE_NAME = "Example_MIX_CONTENT";
//		
//	}

}
