package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;

/**
 * 典型类题数据库操作类
 * 
 * @author edu_lx
 * 
 */
public class ExampleDataDao extends BaseDataDao {

	private static final String TAG = "ExampleDataDao";
	/**
	 * item的标号 例1，例2...
	 */
	public static final String ITEM_NUM = "ITEM_NUM";

	/**
	 * item的名称
	 */
	public static final String ITEM_NAME = "ITEM_NAME";

	/**
	 * 例题的种类 文字，图片，视频
	 */
	public static final String EXAM_TYPE = "EXAM_TYPE";

	/**
	 * 例题的内容(图片名或者视频名)
	 */
	public static final String EXAM_CONTENT = "EXAM_CONTENT";
	/**
	 * 图片名称
	 */
	public static final String IMAGE_NAME = "IMAGE_NAME";
	/**
	 * 图片描述
	 */
	public static final String IMAGE_CONTENT = "IMAGE_CONTENT";
	/**
	 * 自身引用
	 */
	private static ExampleDataDao instance = null;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static ExampleDataDao getInstance(Context context) {
		if (instance == null)
			instance = new ExampleDataDao(context);
		return instance;
	}

	private ExampleDataDao(Context context) {
		super(context);
		TABLE_NAME = "EXAMPLE";
	}

	/**
	 * 获取指定ID下的所有数据
	 * 
	 * @return
	 */
	public List<ExampleData> getDatasbyid(int chaptetid) {
		List<ExampleData> datas = null;
		Cursor curs = null;

		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE CHAPTER_ID = " + chaptetid+" order by ID ASC";
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);

			if (curs != null && curs.getCount() != 0) {
				datas = new ArrayList<ExampleData>();
				while (curs.moveToNext()) {
					ExampleData data = parseCursor(curs);
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
	 * 解析cursor
	 * 
	 * @param curs
	 * @return
	 */
	@Override
	public ExampleData parseCursor(Cursor curs) {
		ExampleData data = new ExampleData();
		int idIndex = curs.getColumnIndex(ID);
		int itemNumIndex = curs.getColumnIndex(ITEM_NUM);
		int itemNameIndex = curs.getColumnIndex(ITEM_NAME);
		int examTypeIndex = curs.getColumnIndex(EXAM_TYPE);
		int examContentIndex = curs.getColumnIndex(EXAM_CONTENT);
		int remarkIndex = curs.getColumnIndex(REMARK);
		int imageName = curs.getColumnIndex(IMAGE_NAME);
		int imageContent = curs.getColumnIndex(IMAGE_CONTENT);
		data.setId(curs.getInt(idIndex));
		data.setItem_name(curs.getString(itemNameIndex));
		data.setItem_num(curs.getString(itemNumIndex));
		data.setExam_type(curs.getString(examTypeIndex));
		data.setExam_content(curs.getString(examContentIndex));
		data.setRemark(curs.getString(remarkIndex));
		data.setImage_name(curs.getString(imageName));
		data.setImage_content(curs.getString(imageContent));
		return data;
	}

//	@Override
//	public void setTableName() {
//		TABLE_NAME = "EXAMPLE";
//		
//	}
}
