package com.edu.basicaccountingforguangzhou.subject.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.data.SubjectBillData1;
import com.edu.library.data.BaseDataDao;

/**
 * 数据库操作dao层
 * 
 * @author lucher
 * 
 */
public class SubjectBillDataDao extends BaseDataDao {

	private static final String TAG = "SubjectBillDataDao";

	/**
	 * 自身引用
	 */
	private static SubjectBillDataDao instance = null;

	private SubjectBillDataDao(Context context, String db) {
		super(context, db);
	}

	@Override
	public void setTableName() {
		TABLE_NAME = "TB_BILL_SUBJECT";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SubjectBillDataDao getInstance(Context context, String db) {
		if (instance == null)
			instance = new SubjectBillDataDao(context, db);
		return instance;
	}

	public SubjectBillData1 getData(int id, SQLiteDatabase db) {
		SubjectBillData1 data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id;
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
	public SubjectBillData1 parseCursor(Cursor curs) {
		SubjectBillData1 subjectData = new SubjectBillData1();
		subjectData.setId(curs.getInt(0));
		subjectData.setChapterId(curs.getInt(1));
		subjectData.setFlag(curs.getInt(2));
		subjectData.setTemplateId(curs.getInt(3));
		subjectData.setQuestion(curs.getString(4));
		subjectData.setPic(curs.getString(5));
		subjectData.setLabels(curs.getString(6));
		subjectData.setAnswer(curs.getString(7));
		subjectData.setScore(curs.getInt(8));
		subjectData.setErrorCount(curs.getInt(9));
		subjectData.setFavorite(curs.getInt(10));
		subjectData.setRemark(curs.getString(11));
		subjectData.setSubjectType(SubjectType.SUBJECT_BILL);

		return subjectData;
	}

}
