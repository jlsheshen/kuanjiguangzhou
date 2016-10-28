package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;

/**
 * 产品类别数据库操作类
 * 
 * @author lucher
 * 
 */
public class ChapterDataDao extends BaseDataDao {

	private static final String TAG = "ChapterDataDao";

	/**
	 * 父id
	 */
	public static final String PARENT_ID = "PARENT_ID";

	/**
	 * 名字
	 */
	public static final String NAME = "NAME";
	
	/**
	 * 随堂练习
	 */
	public static final String PAPER_ID_PRACTICE = "PAPER_ID_PRACTICE";
	
	/**
	 * 课后练习
	 */
	public static final String PAPER_ID_EXERCISE = "PAPER_ID_EXERCISE";
	
	/**
	 * 自身引用
	 */
	private static ChapterDataDao instance = null;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static ChapterDataDao getInstance(Context context) {
		if (instance == null)
			instance = new ChapterDataDao(context);
		return instance;
	}

	private ChapterDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_CHAPTER";
	}

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public List<ChapterData> getAllDatas() {
		List<ChapterData> datas = null;
		Cursor curs = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<ChapterData>();
				while (curs.moveToNext()) {
					ChapterData data = parseCursor(curs);
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
	 * 获取parentId的内容
	 * 
	 * @param patentId
	 * @return
	 */
	public List<ChapterData> selectChapter(int patentId) {
		ArrayList<ChapterData> chapterInfoList = new ArrayList<ChapterData>();
		Cursor curs = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = " select  *  from " + TABLE_NAME + " where PARENT_ID  =  " + patentId;
			curs = mDb.rawQuery(sql, null);
			if (curs != null && curs.getCount() != 0) {
				chapterInfoList = new ArrayList<ChapterData>();
				while (curs.moveToNext()) {
					ChapterData data = parseCursor(curs);
					chapterInfoList.add(data);
					Log.e(TAG, "data:" + data);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
		return chapterInfoList;
	}

	/**
	 * 解析cursor
	 * 
	 * @param curs
	 * @return
	 */
	@Override
	public ChapterData parseCursor(Cursor curs) {

		ChapterData data = new ChapterData();
		int idIndex = curs.getColumnIndex(ID);
		int parentIdIndex = curs.getColumnIndex(PARENT_ID);
		int nameIndex = curs.getColumnIndex(NAME);
		int remarkIndex = curs.getColumnIndex(REMARK);
		int practiceIndex = curs.getColumnIndex(PAPER_ID_PRACTICE);
		int exerciseIndex = curs.getColumnIndex(PAPER_ID_EXERCISE);
		data.setId(curs.getInt(idIndex));
		data.setParentId(curs.getInt(parentIdIndex));
		data.setName(curs.getString(nameIndex));
		data.setRemark(curs.getString(remarkIndex));
		data.setPaperIdPractice(curs.getLong(practiceIndex));
		data.setPaperIdExercise(curs.getLong(exerciseIndex));

		return data;
	}


}
