package com.edu.basicaccountingforguangzhou.model;

import java.util.List;

import com.edu.basicaccountingforguangzhou.data.BaseDataDao;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.data.TestDataDao;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * 练习题数据处理模型封装
 * 
 * @author lucher
 * 
 */
public class TestDataModel {
	private static final String TAG = "TestDataModel";

	// 自身实例
	private static TestDataModel instance;

	private Context mContext;

	public static synchronized TestDataModel getInstance(Context context) {
		if (instance == null) {
			instance = new TestDataModel(context);
		}
		return instance;
	}

	private TestDataModel(Context context) {
		this.mContext = context;
	}

	/**
	 * 根据dataId获取答题时间和ExamID
	 */
	public TestData getUseTimeByDataId(int dataId) {
		return TestDataDao.getInstance(mContext).getUseTime(dataId);
	}

	/**
	 * 根据章节id和类别获取数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public List<TestData> getDatas(int chapterId, int type) {
		return TestDataDao.getInstance(mContext).getDatas(chapterId, type);
	}
	

	/**
	 * 更新某个题的状态和发送状态
	 * 
	 * @param id
	 *            数据库id
	 * @param state
	 *            状态：0-未做，1-正确，2-错误
	 * @param sendState
	 *            发送状态：0-未发送，1-已发送
	 */
	public void updateStateAndErrorCount(int id, int state, int errorCount) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(TestDataDao.STATE, state);
		values.put(TestDataDao.ERROR_COUNT, errorCount);
		TestDataDao.getInstance(mContext).updateData(id, values);
	}
	
	
	/**
	 * 更新某个题的状态和发送状态
	 * 
	 * @param id
	 *            数据库id
	 * @param state
	 *            状态：3-未完成
	 */
	public void updateUnFinishState(int id, int state) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(TestDataDao.STATE, state);
		TestDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新某个题的状态和发送状态
	 * 
	 * @param id
	 *            数据库id
	 * @param state
	 *            状态：0-未做，1-正确，2-错误
	 * @param sendState
	 *            发送状态：0-未发送，1-已发送
	 */
	public void updateAllState(int id, int state) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(TestDataDao.STATE, state);
		TestDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新某个题的发送状态
	 * 
	 * @param id
	 *            数据库id
	 * @param sendState
	 *            发送状态：0-未发送，1-已发送
	 */
	public void updateSendState(int id, int sendState) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(TestDataDao.SEND_STATE, sendState);
		TestDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 更新某个题的是否可查看答案状态
	 * 
	 * @param id
	 *            数据库id
	 * @param sendState
	 *            是否可以查看：1可以，其他不可以
	 */
	public void updateLookState(int id, String State) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(BaseDataDao.REMARK, State);
		TestDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 根据id获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public TestData getDataById(int id) {
		return (TestData) TestDataDao.getInstance(mContext).getDataById(id);
	}

	/**
	 * 根据父章节id获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public TestData getDataForParentChapterId(int chapterId, int type) {
		return TestDataDao.getInstance(mContext).getDataForParentChapterId(chapterId, type);
	}

	/**
	 * 根据父章节id获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public TestData getDataChapterId(int parentChapterId) {
		return TestDataDao.getInstance(mContext).getDataByChapterId(parentChapterId);
	}

	/**
	 * 根据id获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public TestData getDataforId(int id) {
		return TestDataDao.getInstance(mContext).getDataByIdForTest(id);
	}

	/**
	 * 根据type获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public List<TestData> getDataforType(int type) {
		return TestDataDao.getInstance(mContext).getDataByType(type);
	}

	/**
	 * 根据type获取对应数据
	 * 
	 * @param id
	 * @return
	 */
	public TestData getDataType(int type) {
		return TestDataDao.getInstance(mContext).getDataByTypeForTests(type);
	}

	/**
	 * 根据父章节id获取对应数据
	 * 
	 * @param parentChapterId
	 * @param type
	 * @return
	 */
	public TestData getDataParentChapterIdTest(int parentChapterId, int type) {
		return TestDataDao.getInstance(mContext).getDataParentChapterIdTest(parentChapterId, type);
	}

	/**
	 * 清除用户答案
	 * 
	 * @param id
	 */
	public void clearUserAnswer(int id) {
	}

	/**
	 * 更新题目对应的state
	 * 
	 * @param testId
	 * @param state
	 *            0-未做，1-正确，2-错误,3-未完成
	 */
	public void updateState(int testId, int state) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(TestDataDao.STATE, state);
		TestDataDao.getInstance(mContext).updateData(testId, values);
	}

	/**
	 * 插入数据
	 * 
	 * @param serverId
	 *            服务器端id
	 * @param type
	 *            练习类型
	 * @param subjectId
	 *            题目id
	 * @param chapterId
	 *            章节id
	 * @param subjectType
	 *            题目类型
	 */
	public void insertData(int serverId, int type, int subjectId, int chapterId, int subjectType) {
		ContentValues values = new ContentValues();
		values.put(TestDataDao.CHAPTER_ID, chapterId);
		values.put(TestDataDao.SERVER_ID, serverId);
		values.put(TestDataDao.SUBJECT_ID, subjectId);
		if (subjectType == 5) {
			values.put(TestDataDao.SUBJECT_TYPE, 4);
		} else if (subjectType == 7) {
			values.put(TestDataDao.SUBJECT_TYPE, 5);
		} else {
			values.put(TestDataDao.SUBJECT_TYPE, subjectType);
		}
		values.put(TestDataDao.TYPE, type);
		values.put(TestDataDao.STATE, 0);
		values.put(TestDataDao.SEND_STATE, 0);

		Log.i(TAG, "insertData-subjectId:" + subjectId);
		TestDataDao.getInstance(mContext).insertData(values);
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 */
	public void deleteData(int serverId, int mChapterId, int type) {
		// String whereClause = "SERVER_ID=?";
		// String[] whereArgs = { String.valueOf(serverId) };
		// TestDataDao.getInstance(mContext).deleteDataBy(whereClause,
		// whereArgs);
		TestDataDao.getInstance(mContext).deleteDatasByChapterId(serverId, mChapterId, type);
	}

	private void updateStateAndSend(int id, int state, int sendSate) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(TestDataDao.STATE, state);
		contentValues.put(TestDataDao.SEND_STATE, sendSate);
		TestDataDao.getInstance(mContext).updateData(id, contentValues);
	}
}
