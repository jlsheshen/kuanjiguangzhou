package com.edu.basicaccountingforguangzhou.model;
/**
 * 二级科目Second_Subject表 数据处理模型封装
 * 
 * @author edu_lx
 * 
 */

import java.util.List;

import com.edu.basicaccountingforguangzhou.data.BaseData;
import com.edu.basicaccountingforguangzhou.data.BaseDataDao;
import com.edu.basicaccountingforguangzhou.data.SecondSubjectData;
import com.edu.basicaccountingforguangzhou.data.SecondSubjectDataDao;

import android.content.ContentValues;
import android.content.Context;


public class SecondSubjectDataModel {

	// 自身实例
	private static SecondSubjectDataModel instance;

	private Context mContext;

	public static synchronized SecondSubjectDataModel getInstance(Context context) {
		if (instance == null) {
			instance = new SecondSubjectDataModel(context);
		}
		return instance;
	}

	private SecondSubjectDataModel(Context context) {
		this.mContext = context;
	}
//	/**
//	 * 插入数据
//	 * 
//	 * @param id
//	 * @return
//	 */
//	public void insertData(Topic topic) {
//		ContentValues values = new ContentValues();
//		values.put(SecondSubjectDataDao.NAME, "二级科目");
//		values.put(SecondSubjectDataDao.DEPENDENTID, "DEPENDENTID");
//		values.put(BaseDataDao.REMARK, "REMARK");
//		
//		SecondSubjectDataDao.getInstance(mContext).insertData(values);
//	}
	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public List<BaseData> getAllDatas() {
		List<BaseData> datas = SecondSubjectDataDao.getInstance(mContext).getAllDatas();

		return datas;
	}
	
	/**
	 * 根据DependentId获取所有数据
	 * 
	 * @return
	 */
	public List<SecondSubjectData> getDatasByDependentId(int dependentId) {
		List<SecondSubjectData> datas = SecondSubjectDataDao.getInstance(mContext).getDatasByDependid(dependentId);

		return datas;
	}

	/**
	 * 根据id获取对应的数据
	 * 
	 * @param id
	 * @return
	 */
	public SecondSubjectData getDataById(int id) {
		return (SecondSubjectData) SecondSubjectDataDao.getInstance(mContext).getDataById(id);
	}
	
}