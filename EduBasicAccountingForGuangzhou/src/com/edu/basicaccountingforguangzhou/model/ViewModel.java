package com.edu.basicaccountingforguangzhou.model;

import java.util.List;

import com.edu.basicaccountingforguangzhou.data.ViewData;
import com.edu.basicaccountingforguangzhou.data.ViewDataDao;

import android.content.ContentValues;
import android.content.Context;

public class ViewModel {

	private static final String TAG = "ViewModel";

	// 自身实例
	private static ViewModel instance;

	private Context mContext;

	public static synchronized ViewModel getInstance(Context context) {
		if (instance == null) {
			instance = new ViewModel(context);
		}
		return instance;
	}

	private ViewModel(Context context) {
		this.mContext = context;
	}

	/**
	 * Tbview插入数据
	 * 
	 * @param question_id
	 * @param x_axis
	 * @param y_axis
	 * @param width
	 * @param height
	 * @param answer
	 * @param initial_value
	 * @param user_answer
	 * @param radio_group
	 * @param input_type
	 * @param editable
	 * @param type_index
	 * @param randomFlah
	 * @param isRighted
	 */
	public void insertBillView(int question_id, double x_axis, double y_axis, double width, double height, String answer, String initial_value, String user_answer, int radio_group, int input_type,
			int editable, int type_index, int randomFlah, int isRighted) {
//		ContentValues values = new ContentValues();
//		values.put(ViewDataDao.QUESTION_ID, question_id);
//		values.put(ViewDataDao.X_AXIS, x_axis);
//		values.put(ViewDataDao.Y_AXIS, y_axis);
//		values.put(ViewDataDao.WIDTH, width);
//		values.put(ViewDataDao.HEIGHT, height);
//		values.put(ViewDataDao.ANSWER, answer);
//		values.put(ViewDataDao.INITIAL_VALUE, initial_value);
//		values.put(ViewDataDao.USER_ANSWER, user_answer);
//		values.put(ViewDataDao.RADIO_GROUP, radio_group);
//		values.put(ViewDataDao.INPUT_TYPE, input_type);
//		values.put(ViewDataDao.EDITABLE, editable);
//		values.put(ViewDataDao.TYPE_INDEX, type_index);
//		values.put(ViewDataDao.RANDOMFLAH, randomFlah);
//		values.put(ViewDataDao.ISRIGHTED, isRighted);
//
//		ViewDataDao.getInstance(mContext).insertData(values);

	}

	/**
	 * 根据id获取
	 * 
	 * @param id
	 * @return
	 */
//	public List<ViewData> getViewIDatasById(int id) {
//		return ViewDataDao.getInstance(mContext).getBillTemplate(id);
//
//	}
//
//	public void getCopyViewData(int oldId, int newId) {
//		ViewDataDao.getInstance(mContext).getCopyViewData(oldId, newId);
//	}

	/**
	 * 清除数据
	 * 
	 * @param questionId
	 * @param uAnswer
	 * @param Right
	 */
//	public void updateContent(int questionId, String uAnswer, int Right) {
//		ContentValues values = new ContentValues();
//		values.put(ViewDataDao.USER_ANSWER, uAnswer);
//		values.put(ViewDataDao.ISRIGHTED, Right);
//		ViewDataDao.getInstance(mContext).upDateContent(questionId, uAnswer, Right);
//	}

}
