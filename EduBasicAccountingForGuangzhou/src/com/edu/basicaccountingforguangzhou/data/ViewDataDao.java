package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.library.data.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class ViewDataDao extends BaseDataDao {
	/**
	 * 自身实例
	 */
	private static ViewDataDao instance = null;
	public final static String QUESTION_ID = "question_id";
	// 详细见数据库文档
	public final static String X_AXIS = "x_axis";
	public final static String Y_AXIS = "y_axis";
	public final static String WIDTH = "width";
	public final static String HEIGHT = "height";
	public final static String ANSWER = "answer";
	public final static String INITIAL_VALUE = "initial_value";
	public final static String USER_ANSWER = "user_answer";
	public final static String RADIO_GROUP = "radio_group";
	public final static String INPUT_TYPE = "input_type";

	public final static String EDITABLE = "editable";
	public final static String TYPE_INDEX = "type_index";
	public final static String RANDOMFLAH = "randomFlah";
	public final static String ISRIGHTED = "isRighted";
	public final static String ID = "id";

	private ViewDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_VIEW";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static ViewDataDao getInstance(Context context) {
		if (instance == null)
			instance = new ViewDataDao(context);
		return instance;
	}

	/*
	 * * 根据id获得数据
	 * 
	 * @return
	 */
	public List<ViewData> getViewIDatasById(int id) {
		Cursor curs = null;
		List<ViewData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " SELECT * FROM " + TABLE_NAME + " WHERE question_id = " + id;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				datas = new ArrayList<ViewData>();
				while (curs.moveToNext()) {
					ViewData data = parseCursor(curs);
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

	/*
	 * * 拷贝
	 * 
	 * @return
	 */
	public void getCopyViewData(int id, int newId) {
		Cursor curs = null;
		List<ViewData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "INSERT INTO  " + TABLE_NAME
					+ " ( QUESTION_ID, X_AXIS,Y_AXIS,WIDTH,HEIGHT,ANSWER,INITIAL_VALUE, USER_ANSWER,RADIO_GROUP,INPUT_TYPE,EDITABLE ,TYPE_INDEX,RANDOMFLAH,ISRIGHTED) SELECT " + newId
					+ ", X_AXIS,Y_AXIS,WIDTH,HEIGHT,ANSWER,INITIAL_VALUE ,null,RADIO_GROUP,INPUT_TYPE,EDITABLE ,TYPE_INDEX,RANDOMFLAH,0 FROM  " + TABLE_NAME + "  WHERE QUESTION_ID = " + id;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				datas = new ArrayList<ViewData>();
				while (curs.moveToNext()) {
					ViewData data = parseCursor(curs);
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

	public void upDateContent(int questionId, String uAnswer, int Right) {
		Cursor curs = null;
		List<ViewData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "UPDATE " + TABLE_NAME + " SET USER_ANSWER = null, ISRIGHTED =" + Right + " WHERE QUESTION_ID =" + questionId;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				curs.moveToFirst();
				datas = new ArrayList<ViewData>();
				while (curs.moveToNext()) {
					ViewData data = parseCursor(curs);
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

	@Override
	public ViewData parseCursor(Cursor curs) {
		ViewData data = new ViewData();
		int idIndex = curs.getColumnIndex(ID);
		int quesIndex = curs.getColumnIndex(QUESTION_ID);
		int xIndex = curs.getColumnIndex(X_AXIS);
		int yIndex = curs.getColumnIndex(Y_AXIS);
		int withIndex = curs.getColumnIndex(WIDTH);
		int heightIndex = curs.getColumnIndex(HEIGHT);
		int answerIndex = curs.getColumnIndex(ANSWER);
		int initial_valueIndex = curs.getColumnIndex(INITIAL_VALUE);
		int user_answerIndex = curs.getColumnIndex(USER_ANSWER);
		int radio_groupIndex = curs.getColumnIndex(RADIO_GROUP);
		int input_typeIndex = curs.getColumnIndex(INPUT_TYPE);
		int editableIndex = curs.getColumnIndex(EDITABLE);
		int type_indexIndex = curs.getColumnIndex(TYPE_INDEX);
		int randomflahIndex = curs.getColumnIndex(RANDOMFLAH);
		int isrightedIndex = curs.getColumnIndex(ISRIGHTED);
		data.setId(curs.getInt(idIndex));
		data.setQuestion_id(curs.getInt(quesIndex));
		data.setX_axis(curs.getDouble(xIndex));
		data.setY_axis(curs.getDouble(yIndex));
		data.setWidth(curs.getDouble(withIndex));
		data.setHeight(curs.getDouble(heightIndex));
		data.setAnswer(curs.getString(answerIndex));
		data.setInitial_value(curs.getString(initial_valueIndex));
		data.setUser_answer(curs.getString(user_answerIndex));
		data.setRadio_group(curs.getInt(radio_groupIndex));
		data.setInput_type(curs.getInt(input_typeIndex));
		data.setEditable(curs.getInt(editableIndex));
		data.setType_index(curs.getInt(type_indexIndex));
		data.setRandomFlah(curs.getInt(randomflahIndex));
		data.setIsRighted(curs.getInt(isrightedIndex));

		return data;
	}

	public void deleteDataByQuesionId(int questionId) {
		try {
			Log.e(TAG, TABLE_NAME + "-deleteData:" + questionId);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.delete(TABLE_NAME, QUESTION_ID + "=?", new String[] { String.valueOf(questionId) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}
}
