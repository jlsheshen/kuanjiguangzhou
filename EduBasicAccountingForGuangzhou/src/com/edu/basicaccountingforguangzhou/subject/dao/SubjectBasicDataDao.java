package com.edu.basicaccountingforguangzhou.subject.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.data.BaseDataDao;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;

/**
 * 基础题型数据库操作类：单多判
 * 
 * @author lucher
 * 
 */
public class SubjectBasicDataDao extends BaseDataDao {

	private static final String TAG = "SubjectBasicDataDao";

	/**
	 * 自身引用
	 */
	private static SubjectBasicDataDao instance = null;

	private SubjectBasicDataDao(Context context, String db) {
		super(context, db);
		TABLE_NAME = "TB_SUBJECT_BASIC";

	}

	

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SubjectBasicDataDao getInstance(Context context, String db) {
		if (instance == null)
			instance = new SubjectBasicDataDao(context, db);
		return instance;
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
		subjectData.setAnalysis(curs.getString(7));
		subjectData.setErrorCount(curs.getInt(8));
		subjectData.setScore(curs.getInt(9));
		subjectData.setFavorite(curs.getInt(10));
		subjectData.setRemark(curs.getString(11));
		Log.e("得分专用Log", "获取得分4" + subjectData.getScore());


		return subjectData;
	}

}
