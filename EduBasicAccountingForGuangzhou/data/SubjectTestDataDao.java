package com.edu.testbill.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.edu.library.data.BaseDataDao;
import com.edu.library.data.DBHelper;
import com.edu.library.util.ToastUtil;
import com.edu.subject.SubjectType;
import com.edu.subject.TestMode;
import com.edu.subject.bill.template.BillTemplate;
import com.edu.subject.bill.template.BillTemplateFactory;
import com.edu.subject.dao.SubjectBasicDataDao;
import com.edu.subject.dao.SubjectBillDataDao;
import com.edu.subject.data.BaseSubjectData;
import com.edu.subject.data.BaseTestData;
import com.edu.subject.data.SubjectBasicData;
import com.edu.subject.data.SubjectBillData;
import com.edu.subject.data.TestBasicData;
import com.edu.subject.data.TestBillData;
import com.edu.testbill.Constant;

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
	}

	@Override
	public void setTableName() {
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
	public List<BaseTestData> getSubjects(int testMode) {
		Cursor curs = null;
		List<BaseTestData> datas = null;
		try {
			DBHelper helper = new DBHelper(mContext, dbName, null);
			mDb = helper.getWritableDatabase();
			String sql = "SELECT * FROM " + TABLE_NAME;
			Log.d(TAG, "sql:" + sql);
			curs = mDb.rawQuery(sql, null);
			if (curs != null) {
				datas = new ArrayList<BaseTestData>(curs.getCount());
				int index = 1;

				while (curs.moveToNext()) {
					// 初始化测试数据
					BaseTestData testData = initTestData(curs);
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

	/**
	 * 初始化测试数据
	 * 
	 * @param curs
	 * @return
	 */
	private BaseTestData initTestData(Cursor curs) {
		BaseTestData testData = null;
		BaseSubjectData subjectData = null;
		int subjectType = curs.getInt(2);

		switch (subjectType) {
		case SubjectType.SUBJECT_BILL:
			// 初始化测试数据
			testData = new TestBillData();
			parseCursor(curs, testData);
			// 初始化题目数据
			subjectData = (SubjectBillData) SubjectBillDataDao.getInstance(mContext, Constant.DATABASE_NAME).getData(testData.getSubjectId(), mDb);
			testData.setSubjectData(subjectData);
			// 初始化模板数据
			BillTemplate template = BillTemplateFactory.createTemplate(mDb, ((SubjectBillData) subjectData).getTemplateId(), mContext);
			((TestBillData) testData).setTemplate(template);
			String result = ((TestBillData) testData).loadTemplate();
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
			subjectData = (SubjectBasicData) SubjectBasicDataDao.getInstance(mContext, Constant.DATABASE_NAME).getData(testData.getSubjectId(), mDb);
			testData.setSubjectData(subjectData);

			break;

		default:
			break;
		}

		return testData;
	}

	public void parseCursor(Cursor curs, BaseTestData data) {
		data.setId(curs.getInt(0));
		data.setFlag(curs.getInt(1));
		data.setSubjectType(curs.getInt(2));
		data.setSubjectId(curs.getInt(3));
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
	public synchronized void updateTestDatas(List<BaseTestData> datas) {
		try {
			Log.d(TAG, TABLE_NAME + "-updateDatas");
			DBHelper helper = new DBHelper(mContext, dbName, null);
			mDb = helper.getWritableDatabase();
			mDb.beginTransaction();
			for (BaseTestData data : datas) {
				long id = data.getId();
				ContentValues values = new ContentValues();
				values.put(UANSWER, data.getuAnswer());
				values.put(USCORE, data.getuScore());
				values.put(STATE, data.getState());
				if (data instanceof TestBillData) {
					values.put(USIGNS, ((TestBillData) data).getuSigns());
				}
				mDb.update(TABLE_NAME, values, ID + "=?", new String[] { String.valueOf(id) });
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mDb.endTransaction();
			closeDb(mDb);
		}
	}

	@Override
	public BaseTestData parseCursor(Cursor curs) {
		return null;
	}
}
