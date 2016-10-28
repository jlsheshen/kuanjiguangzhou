package com.edu.basicaccountingforguangzhou.data;

/**
 * 服务器返回来的Topic数据库操作类
 * 
 * @author edu_lx
 * 
 */

import android.content.Context;
import android.database.Cursor;

public class TopicDataDao extends BaseDataDao {

	private static final String TAG = "TopocDataDao";

	public static final String DATAID = "DATAID";

	public static final String TOPIC = "TOPIC";

	/**
	 * 自身实例
	 */
	private static TopicDataDao instance = null;

	public TopicDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_TOPIC";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static TopicDataDao getInstance(Context context) {
		if (instance == null)
			instance = new TopicDataDao(context);
		return instance;
	}


//	public List<Topic> getDataByDataId(int dataid) {
//		TopicData data= null;
//		Cursor curs = null;
//		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE DATAID = " + dataid;
//		try {
//			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
//			mDb = helper.getWritableDatabase();
//			curs = mDb.rawQuery(sql, null);
//			if (curs != null) {
//				if (curs.getCount() == 0)
//					return null;
//				curs.moveToFirst();
//
//				int idIndex = curs.getColumnIndex(ID);
//				int dataidIndex = curs.getColumnIndex(DATAID);
//				int topicIndex = curs.getColumnIndex(TOPIC);
//				data = new TopicData();
//				data.setId(curs.getInt(idIndex));
//				data.setDataid(curs.getInt(dataidIndex));
//				data.setTopic(curs.getString(topicIndex));
//				Log.d(TAG, "data:" + data);
//			}
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		} finally {
//			closeDb(mDb, curs);
//		}
//		List <Topic> mTopics = JSONKit.deserializeList(data.getTopic(), Topic.class);
//		return mTopics;
//	}

	@Override
	public BaseData parseCursor(Cursor curs) {
		// TODO Auto-generated method stub
		return null;
	}


}
