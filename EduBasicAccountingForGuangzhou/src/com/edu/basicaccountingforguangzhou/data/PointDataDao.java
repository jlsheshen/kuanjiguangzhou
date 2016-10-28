package com.edu.basicaccountingforguangzhou.data;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.edu.basicaccountingforguangzhou.Constant;

public class PointDataDao extends BaseDataDao{
	
	private static final String TAG = "PointDataDao";
	
	public static final String CHAPTER_ID="CHAPTER_ID";
	/**
	 * 內容
	 */
	public static final String CONTENT="POINT_CONTENT";
	/**
	 * 自身引用
	 */
	private static PointDataDao instance = null;
	
	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static PointDataDao getInstance(Context context) {
		if (instance == null)
			instance = new PointDataDao(context);
		return instance;
	}
	private PointDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_DIFFICULT_POINT";
	}


	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public PointData getAllDatas(int chapterId) {
		PointData datas = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME +" WHERE CHAPTER_ID = " + chapterId ;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				 datas=new PointData();
				int idIndex=curs.getColumnIndex(ID);
				int chapterIndex=curs.getColumnIndex(CHAPTER_ID);
				int contentIndex=curs.getColumnIndex(CONTENT);
				int remarkIndex = curs.getColumnIndex(REMARK);
				datas.setId(curs.getInt(idIndex));
				datas.setChapter_id(curs.getInt(chapterIndex));
				datas.setContent(curs.getString(contentIndex));
				datas.setRemark(curs.getString(remarkIndex));
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
	public PointData parseCursor(Cursor curs) {
//		PointData data=new PointData();
//		int idIndex=curs.getColumnIndex(ID);
//		int chapterIndex=curs.getColumnIndex(CHAPTER_ID);
//		int contentIndex=curs.getColumnIndex(CONTENT);
//		int remarkIndex = curs.getColumnIndex(REMARK);
//		data.setId(curs.getInt(idIndex));
//		data.setChapter_id(curs.getInt(chapterIndex));
//		data.setContent(curs.getString(contentIndex));
//		data.setRemark(curs.getString(remarkIndex));
		return null;
	}
	

}
