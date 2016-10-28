package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.subject.ISubject;
import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplate;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplateFactory;
import com.edu.library.data.DBHelper;
import com.edu.library.util.ToastUtil;

/**
 * 课后练习，随堂练习数据库操作类
 * 
 * @author lucher
 * 
 */
public class TestDataDao extends BaseDataDao {

	private static final String TAG = "TestDataDao";

	// 类别字段名
	public static final String SERVER_ID = "SERVER_ID";
	// 类别字段名
	public static final String TYPE = "TYPE";
	// 章节id
	public static final String CHAPTER_ID = "CHAPTER_ID";
	// 具体解释查看数据库设计文档
	public static final String SUBJECT_TYPE = "SUBJECT_TYPE";
	// 题目id字段名
	public static final String SUBJECT_ID = "SUBJECT_ID";
	// 答题状态字段名
	public static final String STATE = "STATE";
	// 具体解释查看数据库设计文档
	public static final String SEND_STATE = "SEND_STATE";
	public static final String ERROR_COUNT = "ERROR_COUNT";
	
	// 题目视图
	private ISubject subjectView;
	
	
	// 用户答案
		public static final String UANSWER = "UANSWER";
		// 用户印章-单据题
		public static final String USIGNS = "USIGNS";
		// 用户得分
		public static final String USCORE = "USCORE";
		
		TestData testData = null;
		BaseSubjectData subjectData = null;
		 
	


	/**
	 * 自身引用
	 */
	private static TestDataDao instance = null;
	private int index = 1;

	private TestDataDao(Context context) {
		super(context);
		TABLE_NAME = "TB_TEST";
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static TestDataDao getInstance(Context context) {
		if (instance == null)
			instance = new TestDataDao(context);
		return instance;
	}

	@Override
	public TestData parseCursor(Cursor curs) {
		int idIndex = curs.getColumnIndex(ID);
		int serverId = curs.getColumnIndex(SERVER_ID);
		int subTypeIndex = curs.getColumnIndex(SUBJECT_TYPE);
		int chapterId = curs.getColumnIndex(CHAPTER_ID);
		int typeIndex = curs.getColumnIndex(TYPE);
		int subjectId = curs.getColumnIndex(SUBJECT_ID);
		int stateIndex = curs.getColumnIndex(STATE);
		int remarkIndex = curs.getColumnIndex(REMARK);
		int errorCount = curs.getColumnIndex(ERROR_COUNT);
		TestData data = new TestData();

		data.setId(curs.getInt(idIndex));
		data.setServerId(curs.getInt(serverId));
		data.setChapterId(curs.getInt(chapterId));
		data.setType(curs.getInt(typeIndex));
		data.setSubjectType(curs.getInt(subTypeIndex));
		data.setSubjectId(curs.getString(subjectId));
		data.setState(curs.getInt(stateIndex));
		data.setRemark(curs.getString(remarkIndex));
		data.setSendState(curs.getInt(curs.getColumnIndex(SEND_STATE)));
		data.setErrorCount(curs.getInt(errorCount));
		return data;
	}

	/**
	 * 根据dataId取考试用时和ExamId
	 */
	public TestData getUseTime(int dataId) {
		TestData returnValue = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + dataId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				// curs.moveToFirst();
				curs.moveToLast();
				returnValue = new TestData();


				// Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return returnValue;
	}

	/**
	 * 更新数据
	 * 
	 * @param id
	 * @param values
	 */
	public void updateDataByDataId(int DataId, ContentValues values) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateData:" + DataId);
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			mDb.update(TABLE_NAME, values, ID + "=?", new String[] { String.valueOf(DataId) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
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
		Cursor curs = null;
		List<TestData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type + " AND CHAPTER_ID = " + chapterId;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			
			if (curs != null) {
				datas = new ArrayList<TestData>(curs.getCount());
				index = 1;
				int indexSingel = 1;
				int indexMulti = 1;
				int indexJuge = 1;
				int indexChild = 0;
				boolean isChild = false;
			
				while (curs.moveToNext()) {
					int  subjectType = curs.getInt(curs.getColumnIndex(SUBJECT_TYPE));
				TestData data = parseCursor(curs);
					if (!isChild) {
						datas.add(data);
					}
					if (subjectType == Constant.SUBJECT_TYPE_SINGLE_SELECT || subjectType == Constant.SUBJECT_TYPE_MULTI_SELECT
							|| subjectType == Constant.SUBJECT_TYPE_JUDGE) {
						isChild = false;
						SubjectBasicData basicData = (SubjectBasicData) SubjectBasicDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId()));
						data.setTitle(basicData.getQuestion());
						basicData.setSubjectIndex(index++);
						if (subjectType == Constant.SUBJECT_TYPE_SINGLE_SELECT) {
							basicData.setIndexName((indexSingel++) + "");
						} else if (subjectType == Constant.SUBJECT_TYPE_MULTI_SELECT) {
							basicData.setIndexName((indexMulti++) + "");
						} else {
							basicData.setIndexName((indexJuge++) + "");
						}
						data.setData(basicData);
					} else if (subjectType == Constant.SUBJECT_TYPE_ENTRY) {
						isChild = false;
						indexChild++;
						SubjectEntryData entryData = (SubjectEntryData) SubjectEntryDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId().split(">>>")[0]));
						if (entryData.getType() != 2) {
							data.setTitle(entryData.getQuestion());
							Log.e(TAG, "分录的问题" + entryData.getQuestion());
							entryData.setSubjectIndex(index++);
							entryData.setIndexName((indexChild) + "");
							data.setData(entryData);
						} else if (entryData.getType() == 2) {// child
							data.setTitle(entryData.getQuestion());
							entryData.setIndexName((indexChild) + "-1");
							entryData.setSubjectIndex(index++);
							data.setData(entryData);
							datas.addAll(setData(entryData.getChildren(), indexChild));
							isChild = false;
						}
					} else if (subjectType == Constant.SUBJECT_TYPE_BILL) {

						isChild = false;
						indexChild++;
//						SubjectBillData billData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext).getDataById(Integer.valueOf(data.getSubjectId()));	
						// 初始化测试数据
						testData = new TestBillData();

						// 初始化题目数据
//						subjectData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getDataById(testData.getSubjectId());
						subjectData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getDataById(Integer.valueOf(data.getSubjectId()));
						Log.e(TAG, "当前数据库名字" + TABLE_NAME);

						Log.e(TAG, "执行顺序流1");

					//	parseCursor(curs, testData);


						testData.setSubjectData(subjectData);		
						Log.e(TAG, "执行顺序流2");
						
						Log.e(TAG, "执行顺序流2getTemplateId" + ((SubjectBillData) subjectData).getTemplateId());

						
						// 初始化模板数据
						BillTemplate template = BillTemplateFactory.createTemplate(mDb, ((SubjectBillData) subjectData).getTemplateId(), mContext);
//						subjectView.applyData(mData);
//						prepared = true;
						Log.e(TAG, "执行顺序流2-2");

					((TestBillData) testData).setTemplate(template);
					Log.e(TAG, "执行顺序流2-3");

						String result = ((TestBillData) testData).loadTemplate();
						Log.e(TAG, "执行顺序流3");

						if (0!= 2) {
							Log.e(TAG, "执行顺序流4");

							data.setTitle(subjectData.getQuestion());
							subjectData.setSubjectIndex(index++);
							subjectData.setIndexName(indexChild + "");
							Log.e(TAG, "执行顺序流5");

							Log.e(TAG, "setIndexName" + indexChild + "" + "id是" + subjectData.getId());
							isChild = false;

							Log.e(TAG, "执行顺序流6");

							data.setBillData((TestBillData) testData);

						} else {
							data.setTitle(subjectData.getQuestion());
							subjectData.setSubjectIndex(index++);
							subjectData.setIndexName((indexChild) + "_1");
							data.setBillData((TestBillData) testData);
//							datas.addAll(setData(subjectData.getChildren(), indexChild));
							isChild = false;
						}
				
						if (result.equals("success")) {
							Log.d(TAG, "load data success:" + testData);
						} else {
							Log.e(TAG, "load data error:" + result);
							ToastUtil.showToast(mContext, result);
						}
					}
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
	public void parseCursor(Cursor curs, TestData data) {
		data.setId(curs.getInt(0));
		data.setFlag(curs.getInt(1));
		data.setSubjectType(curs.getInt(2));
		data.setSubjectId("" + curs.getInt(3));
		data.setuAnswer(curs.getString(4));
		data.setuScore(curs.getInt(6));
		data.setState(curs.getInt(7));
		data.setRemark(curs.getString(8));
		if (data.getSubjectType() == SubjectType.SUBJECT_BILL) {
			((TestBillData) data).setuSigns(curs.getString(5));
		}
	}

	/**
	 * 解析具有子类的题
	 * 
	 * @param childing
	 *            children内容
	 * @param index
	 *            索引
	 */
	private List<TestData> setData(String childing, int indexName) {
		String[] children = childing.split("<");
		List<TestData> data = new ArrayList<TestData>();
		for (int i = 0; i < children.length; i++) {
			String[] child = children[i].split("_");
			if (child[0].equals("1")) {
				SubjectEntryData entryData = (SubjectEntryData) SubjectEntryDataDao.getInstance(mContext).getDataById(Integer.valueOf(child[1]));
				data.add(setChildrenEntryData(entryData, (indexName + "-" + (i + 2))));
			} else {
				SubjectBillData billData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getDataById(Integer.valueOf(child[1]));

				data.add(setChildrenBillData(billData, (indexName + "-" + (i + 2))));
			
			}
		}
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param billData
	 * @param index
	 */
	private TestData setChildrenBillData(SubjectBillData billData, String indexName) {
		TestData data = new TestData();
		data.setSubjectType(Constant.SUBJECT_TYPE_BILL);
		billData.setSubjectIndex(index++);
		billData.setIndexName(indexName);
		data.setData(billData);
		data.setTitle(billData.getQuestion());
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param entryData
	 * @param index
	 */
	private TestData setChildrenEntryData(SubjectEntryData entryData, String indexName) {
		TestData data = new TestData();

		data.setSubjectType(Constant.SUBJECT_TYPE_ENTRY);
		entryData.setSubjectIndex(index++);
		entryData.setIndexName(indexName);
		data.setData(entryData);
		data.setTitle(entryData.getQuestion());
		return data;
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
	public TestData getDataForParentChapterId(int chapterId, int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE =" + type + "  AND  CHAPTER_ID  = " + chapterId;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();// ToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public TestData getDataByChapterId(int chapterId) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public List<TestData> getAllDataForParentChapterId2(int parentChapterId, int type) {

		// TestData data = null;
		Cursor curs = null;
		List<TestData> listDatas = new ArrayList<TestData>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   TYPE =" + type + "  AND  PARENT_CHAPTER_ID  = " + parentChapterId + " ORDER BY ID DESC";
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}

				// Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
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
	public TestData getDataParentChapterIdTest(int parentChapterId, int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE   PARENT_CHAPTER_ID = " + parentChapterId + " AND  TYPE =" + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				// curs.moveToFirst();
				curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public TestData getDataByIdForTest(int Id) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + Id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				// curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public TestData getDataByTypeForTests(int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToFirst();
				// curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public TestData getDataLastRecord(int type) {

		TestData data = null;
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				curs.moveToLast();
				data = parseCursor(curs);

				Log.d(TAG, "data:" + data);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
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
	public List<TestData> getDataByType(int type) {
		List<TestData> listDatas = new ArrayList<TestData>();
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);

			if (curs != null) {
				if (curs.getCount() == 0)
					return null;
				while (curs.moveToNext()) {
					listDatas.add(parseCursor(curs));
				}

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return listDatas;
	}

	/**
	 * 删除数据
	 * 
	 * @param table
	 *            表名
	 * @param whereClause
	 *            表示SQL语句中条件部分的语句
	 * @param whereArgs
	 *            表示占位符的值
	 * @return
	 */
	public int deleteDataBy(String whereClause, String[] whereArgs) {
		int result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		result = mDb.delete(TABLE_NAME, whereClause, whereArgs);
		mDb.close();
		return result;
	}

	/**
	 * 根据章节id和类别删除数据
	 * 
	 * @param chapterId
	 *            章节id
	 * @param type
	 *            类别
	 * @return
	 */
	public void deleteDatasByChapterId(int serverId, int mChapterId, int type) {
		Cursor curs = null;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE = " + type + " AND SERVER_ID = " + serverId + " AND CHAPTER_ID = " + mChapterId;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				while (curs.moveToNext()) {
					int id = curs.getInt(curs.getColumnIndex(ID));
					int subjectType = curs.getInt(curs.getColumnIndex(SUBJECT_TYPE));
					int subjectId = curs.getInt(curs.getColumnIndex(SUBJECT_ID));
					if (subjectType == Constant.SUBJECT_TYPE_SINGLE_SELECT || subjectType == Constant.SUBJECT_TYPE_MULTI_SELECT || subjectType == Constant.SUBJECT_TYPE_JUDGE) {
						SubjectBasicDataDao.getInstance(mContext).deleteData(subjectId);
						this.deleteData(id);
					} else if (subjectType == Constant.SUBJECT_TYPE_ENTRY) {
						SubjectEntryDataDao.getInstance(mContext).deleteData(subjectId);
						this.deleteData(id);
					} else if (subjectType == Constant.SUBJECT_TYPE_BILL) {
						// SubjectBillDataDao.getInstance(mContext).deleteDatas(subjectId);
					}

				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}
	}



}
