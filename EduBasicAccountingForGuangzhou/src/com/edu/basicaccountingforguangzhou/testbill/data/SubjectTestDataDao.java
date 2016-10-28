package com.edu.basicaccountingforguangzhou.testbill.data;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.data.BaseData;
import com.edu.basicaccountingforguangzhou.data.BaseDataDao;
import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicDataDao;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.SubjectBillDataDao;
import com.edu.basicaccountingforguangzhou.data.TestBillData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.SubjectState;
import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.TestMode;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplate;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplateFactory;
import com.edu.basicaccountingforguangzhou.subject.data.TestBasicData;
import com.edu.basicaccountingforguangzhou.subject.data.TestGroupBillData;
import com.edu.library.data.DBHelper;
import com.edu.library.util.ToastUtil;


/**
 * 测试数据数据库操作dao层
 * 
 * @author lucher
 * 
 */
public class SubjectTestDataDao extends BaseDataDao {

	private static final String TAG = "SubjectTestDataDao";

	// 用户答案
	public static final String UANSWER = "UANSWER";
	// 用户印章-单据题
	public static final String USIGNS = "USIGNS";
	// 用户得分
	public static final String USCORE = "USCORE";
	// 题目状态
	public static final String STATE = "STATE";

	/**
	 * 自身引用
	 */
	private static SubjectTestDataDao instance = null;

	private SubjectTestDataDao(Context context) {
		super(context, Constant.DATABASE_NAME);
		TABLE_NAME = "TB_SUBJECT_TEST";

	}


	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SubjectTestDataDao getInstance(Context context) {
		if (instance == null)
			instance = new SubjectTestDataDao(context);
		return instance;
	}

	/**
	 * 获取所有题目
	 * 
	 * @param testMode
	 *            测试模式，见{@link TestMode}
	 * 
	 * @return
	 */
	public List<TestData> getSubjects(int testMode) {
		Cursor curs = null;
		List<TestData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, dbName, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				datas = new ArrayList<TestData>(curs.getCount());
				int index = 1;

				while (curs.moveToNext()) {
					// 初始化测试数据
					TestData testData = initTestData(curs);
					testData.setSubjectIndex(String.valueOf(index++));
					testData.setTestMode(testMode);

					datas.add(testData);
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
	public TestData getGroupBillSubjectData(TestData data, int index,int id){
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE SUBJECT_ID = " + id;
		DBHelper helper = new DBHelper(mContext, dbName, null);
		mDb = helper.getWritableDatabase();
		curs = mDb.rawQuery(sql, null);
		if (curs != null) {
			if (curs.getCount() == 0)
				{return null;}
			curs.moveToLast();
			data = parseCursor(curs,data,index);
		}
		
		return data;
	}
	
	public TestData getBillTestData( TestData data, int id){
		Cursor curs = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE SUBJECT_ID = " + id;
		try {
			DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
			mDb = helper.getWritableDatabase();
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				if (curs.getCount() == 0)
					{return null;}
//				curs.moveToFirst();
				curs.moveToLast();
				parseCursor(curs,data);
//				Log.e("查看uAnswer专用log", "data:" + data.getuAnswer());
//
//
//				Log.d(TAG, "data:" + data);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeDb(mDb, curs);
		}

		return data;
	}

	/**
	 * 初始化测试数据
	 * 
	 * @param curs
	 * @return
	 */
	private TestData initTestData(Cursor curs) {
		TestData testData = null;
		BaseSubjectData subjectData = null;
		int subjectType = curs.getInt(2);

		switch (subjectType) {
		case SubjectType.SUBJECT_BILL:
			// 初始化测试数据
			testData = new TestBillData();
			parseCursor(curs, testData);
			// 初始化题目数据
			subjectData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getData( Integer.valueOf(testData.getSubjectId()), mDb);

			testData.setSubjectData(subjectData);
			// 初始化模板数据
			BillTemplate template = BillTemplateFactory.createTemplate(mDb, ((SubjectBillData) subjectData).getTemplateId(), mContext);
			((TestBillData) testData).setTemplate(template);
			Log.e("TestBillData", "初始化模板数据" + TAG);

			String result = ((TestBillData) testData).loadTemplate(mContext);
			if (result.equals("success")) {
				Log.d(TAG, "load data success:" + testData);
			} else {
				Log.e(TAG, "load data error:" + result);
				ToastUtil.showToast(mContext, result);
			}

			break;

		case SubjectType.SUBJECT_SINGLE:
		case SubjectType.SUBJECT_MULTI:
		case SubjectType.SUBJECT_JUDGE:
			// 初始化测试数据
			testData = new TestBasicData();
			parseCursor(curs, testData);
			// 初始化题目数据
			subjectData = (SubjectBasicData) SubjectBasicDataDao.getInstance(mContext).getData(Integer.valueOf(testData.getSubjectId()), mDb);

			testData.setSubjectData(subjectData);

			break;

		default:
			break;
		}

		return testData;
	}
	/**
	 * 更新指定的测试数据
	 * 
	 * @param data
	 */
	public synchronized void updateTestData(TestData data) {
		try {
			Log.e(TAG, TABLE_NAME + "-updateData" + data.getSubjectType()  );
			DBHelper helper = new DBHelper(mContext, dbName, null);
			mDb = helper.getWritableDatabase();
			long id = Integer.valueOf(data.getSubjectId());
			ContentValues values = new ContentValues();
			values.put(UANSWER, data.getBillData().getuAnswer());
			Log.e(TAG, TABLE_NAME + "UANSWER" + data.getBillData().getuAnswer()  );

			values.put(USCORE, data.getBillData().getuScore());
			Log.e(TAG, TABLE_NAME + "USCORE" + data.getBillData().getuScore()  + "----" + id );

			values.put(STATE, data.getState());
	//		Log.e(TAG, TABLE_NAME + "STATE" + data.getState()  );
	
			if (data.getSubjectType() == 5) {
				values.put(USIGNS, ((TestBillData) data.getBillData()).getuSigns());
				Log.e(TAG, TABLE_NAME + "USIGNS" + data.getBillData().getuSigns());

			}
			mDb.update(TABLE_NAME, values, SUBJECT_ID + "=?", new String[] { String.valueOf(id) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDb(mDb);
		}
	}
	

	public TestData parseCursor(Cursor curs, TestData data, int index) {
		data.setId(curs.getInt(0));
		data.setFlag(curs.getInt(1));
		data.setSubjectType(curs.getInt(2));
		data.setSubjectId("" + curs.getInt(3));
		data.setRemark(curs.getString(8));
		if (data.getTestMode() == TestMode.MODE_EXAM) {// 测试模式不加载用户数据
			data.setuAnswer(null);
			data.setuScore(0);
			data.setState(SubjectState.STATE_INIT);
			if (data instanceof TestBillData) {
				((TestBillData) data).setuSigns(null);
			} else if (data instanceof TestGroupBillData) {
				((TestGroupBillData) data).setuSigns(null);
			}
		} else {
			data.setuScore(curs.getInt(6));
			data.setState(curs.getInt(7));
			if (index == -1) {// group
				((TestGroupBillData) data).setuSigns(curs.getString(5));
				data.setuAnswer(curs.getString(4));
			} else {
				// 解析对应单据的用户答案
				String answer = curs.getString(4);
				String sign = curs.getString(5);
				if (answer != null && !answer.equals("")) {
					String[] answers = answer.split(SubjectConstant.SEPARATOR_GROUP);
					data.setuAnswer(answers[index]);
				} else {
					data.setuAnswer(answer);
				}
				if (sign != null && !sign.equals("")) {
					String[] signs = sign.split(SubjectConstant.SEPARATOR_GROUP);
					((TestBillData) data).setuSigns(signs[index]);
				} else {
					((TestBillData) data).setuSigns(sign);
				}
			}
		}
		return data;
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
	 * 批量更新测试数据
	 * 
	 * @param values
	 */
//	public synchronized void updateTestDatas(List<TestData> datas) {
//		
//			Log.d(TAG, TABLE_NAME + "-updateDatas");
//			DBHelper helper = new DBHelper(mContext, dbName, null);
//			mDb = helper.getWritableDatabase();
//			mDb.beginTransaction();
//			try {
//			for (TestData data : datas) {
//				long id = data.getId();
//		//		Log.d(TAG, "这里看看idgetBillData" + data.getBillData().getId() );
//		//		Log.d(TAG, "这里看看 id data" + data.getId() );
//	
//				ContentValues values = new ContentValues();
//				values.put(UANSWER, data.getBillData().getuAnswer());
//				values.put(USCORE, data.getBillData().getuScore());
//				values.put(STATE, data.getBillData().getState());
//				if (data.getBillData() instanceof TestBillData) {
//					values.put(USIGNS, ((TestBillData) data).getuSigns());
//				}
//				mDb.update(TABLE_NAME, values, ID + "=?", new String[] { String.valueOf(id) });
//			}
//			mDb.setTransactionSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			mDb.endTransaction();
//			closeDb(mDb);
//		}
//				
//	}

	@Override
	public TestData parseCursor(Cursor curs) {
		return null;
	}
}
