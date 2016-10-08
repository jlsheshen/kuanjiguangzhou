package com.edu.basicaccountingforguangzhou.model;

import com.edu.basicaccountingforguangzhou.data.SubjectBillDataDao;

import android.content.ContentValues;
import android.content.Context;

/**
 * 单据题数据处理模型封装
 * 
 * @author lucher
 * 
 */
public class SubjectBillDataModel {
	// 自身实例
	private static SubjectBillDataModel instance;

	private Context mContext;

	public static synchronized SubjectBillDataModel getInstance(Context context) {
		if (instance == null) {
			instance = new SubjectBillDataModel(context);
		}
		return instance;
	}

	private SubjectBillDataModel(Context context) {
		this.mContext = context;
	}

	/**
	 * 更新对应题的分数
	 * 
	 * @param id
	 * @param uscore
	 */
	public void updateUscore(int id, float uscore) {
		// 更新数据库
		ContentValues values = new ContentValues();
		values.put(SubjectBillDataDao.USCORE, uscore);
		SubjectBillDataDao.getInstance(mContext).updateData(id, values);
	}

	/**
	 * 清除数据
	 * 
	 * @param id
	 * @param uscore
	 * @param completed
	 */
	public void updateContent(int id, float uscore, int completed) {
		ContentValues values = new ContentValues();
		values.put(SubjectBillDataDao.USCORE, uscore);
		values.put(SubjectBillDataDao.IS_COMPLETED, completed);
		SubjectBillDataDao.getInstance(mContext).updateData(id, values);
	}

}
