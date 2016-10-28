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
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.SubjectState;
import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.TestMode;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplate;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplateFactory;
import com.edu.basicaccountingforguangzhou.subject.data.TestGroupBillData;
import com.edu.basicaccountingforguangzhou.testbill.data.SubjectTestDataDao;
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
		TestData testData = null;

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
					int subjectType = curs.getInt(curs.getColumnIndex(SUBJECT_TYPE));
					TestData data = parseCursor(curs);
					Log.e("wwwww", "多页传票 dataid" + data.getSubjectId() + "---" + subjectType);

					if (!isChild) {
						datas.add(data);
					}
					if (subjectType == Constant.SUBJECT_TYPE_SINGLE_SELECT || subjectType == Constant.SUBJECT_TYPE_MULTI_SELECT || subjectType == Constant.SUBJECT_TYPE_JUDGE) {
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
						basicData.setRight(data.getState() == 1 ? true : false);
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
						// 初始化测试数据
						testData = new TestBillData();
						int id = curs.getInt(curs.getColumnIndex(SUBJECT_ID));
						testData = SubjectTestDataDao.getInstance(mContext).getBillTestData(testData, id);
						testData.setTestMode(2);
						// testData = SubjectBillDataDao.getInstance(mContext,
						// Constant.DATABASE_NAME)
						// parseCursor( testData);
						// 初始化题目数据
						subjectData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getDataById(Integer.valueOf(data.getSubjectId()));
						// subjectData.getErrorCount();
						((TestBillData) testData).setSubjectData(subjectData);
						// 初始化模板数据
						BillTemplate template = BillTemplateFactory.createTemplate(mDb, ((SubjectBillData) subjectData).getTemplateId(), mContext);
						((TestBillData) testData).setTemplate(template);
						String result = ((TestBillData) testData).loadTemplate(mContext);
						// Log.e("TestBillData", "获取billData----TestBill中" +
						// testData + data.getSubjectId());
						if (0 != 2) {
							data.setTitle(subjectData.getQuestion());
							subjectData.setSubjectIndex(index++);
							subjectData.setIndexName(indexChild + "");
							((TestBillData) testData).setSubjectData((SubjectBillData) subjectData);
							// Log.e("wwwwwwww", "错题次数testData" +
							// subjectData.getErrorCount()+ "----" +
							// data.getId());
							// testData.setSubjectData(subjectData);
							// Log.e("wwwwwwww", "错题次数testData" +
							// ((SubjectBillData)testData.getSubjectData()).getERROR_COUNT()
							// + "----" + data.getId());
							isChild = false;
							data.setBillData((TestBillData) testData);
							// Log.e("wwwwwwww", "错题次数" +
							// data.getBillData().getSubjectData().getERROR_COUNT()
							// + "----" + data.getId());
						}
						// Log.e("TestBillData", "初始化模板数据1" + TAG);

						// String result = ((TestBillData)
						// testData.getBillData()).loadTemplate(mContext);
						// Log.e("TestBillData", "初始化模板数据2" + TAG);
						// if (result.equals("success")) {
						// Log.d(TAG, "load data success:" + testData);
						// } else {
						// Log.e(TAG, "load data error:" + result);
						// ToastUtil.showToast(mContext, result);
						// }
					} else if (subjectType == SubjectType.SUBJECT_GROUP_BILL) {
						isChild = false;
						indexChild++;
						int id = curs.getInt(curs.getColumnIndex(SUBJECT_ID));
						// 初始化测试数据
						testData = new TestGroupBillData();
						testData.setTestMode(2);
						testData = SubjectTestDataDao.getInstance(mContext).getGroupBillSubjectData(testData, -1, id);
					//	testData = parseCursor(curs, testData, -1);
						// 初始化题目数据,分组单据存在多张单据题目
						List<SubjectBillData> bills = (List<SubjectBillData>) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getDatas(Integer.valueOf(data.getSubjectId()), mDb);
						List<TestBillData> testBills = new ArrayList<TestBillData>(bills.size());
						((TestGroupBillData) testData).setTestDatas(testBills);
						for (int i = 0; i < bills.size(); i++) {
							SubjectBillData bill = bills.get(i);
							TestBillData testBill = new TestBillData();
							testBill.setTestMode(2);
							testBills.add(testBill);
							testBill = (TestBillData) SubjectTestDataDao.getInstance(mContext).getGroupBillSubjectData(testData, i, id);
							//testBill = (TestBillData) parseCursor(curs, testBill, i);
							testBill.setSubjectData(bill);
							// 初始化模板数据
							BillTemplate template = BillTemplateFactory.createTemplate(mDb, ((SubjectBillData) bill).getTemplateId(), mContext);
							testBill.setTemplate(template);
							String result = testBill.loadTemplate(mContext);
							((TestGroupBillData) testData).setIndexNum(index++);
							((TestGroupBillData) testData).setIndexName(indexChild + "");

							isChild = false;

							if (result.equals("success")) {
								Log.d(TAG, "load data success:" + testData);
							} else {
								Log.e(TAG, "load data error:" + result);
								ToastUtil.showToast(mContext, result);
							}
						}
						Log.e("wwwww", "多页传票" + data.getSubjectId());
						data.setTestGroupBillData((TestGroupBillData) testData);
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		// catch (Exception e) {
		// Log.e("TestBillData", "这是什么错误" + e.toString());
		//
		// e.printStackTrace();
		// }
		finally {
			closeDb(mDb, curs);
		}
		return datas;
	}

	/**
	 * cursor解析,用于多组单据
	 * 
	 * @param curs
	 * @param data
	 * @param index
	 */


	// public void parseCursor(Cursor curs, TestData data) {
	//
	//
	// data.setId(curs.getInt(0));
	// data.setFlag(curs.getInt(1));
	// data.setSubjectType(curs.getInt(2));
	// data.setSubjectId("" + curs.getInt(3));
	// data.setuAnswer(curs.getString(4));
	// data.setuScore(curs.getInt(6));
	// if (testMode == TestMode.MODE_EXAM) {// 测试模式不加载用户数据
	// data.setState(curs.getInt(SubjectState.STATE_INIT));
	// }
	//
	// data.setRemark(curs.getString(8));
	// if (data.getSubjectType() == SubjectType.SUBJECT_BILL) {
	// ((TestBillData) data).setuSigns(curs.getString(5));
	// }}

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
