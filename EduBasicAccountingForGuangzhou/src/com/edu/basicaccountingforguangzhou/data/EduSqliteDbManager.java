package com.edu.basicaccountingforguangzhou.data;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.Constant;

/**
 * 数据库管理类，具备增删改查操作。 增删改 --> 操作一个sql语句，并且有返回值。 查询 --> 1. 返回一个游标类型 2.
 * 返回一个List<Object> 3. 返回一个List<Map<String, Object>>
 * 
 * @author
 * @param <T>
 */
public class EduSqliteDbManager<T> extends BaseDataDao {

	public static EduSqliteDbManager<?> instance = null;
	private static final String DATABASE_FILEPATH = "/data/data/com.edu.originalvoucher/databases";
	private static final String DATABASE_FILENAME = "EduOriginalVoucher.db";
	public static final String DATABASE_PATH = DATABASE_FILEPATH + File.separator + DATABASE_FILENAME;

	// private SQLiteDatabase mDb;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 */
	@SuppressWarnings("static-access")
	private EduSqliteDbManager(Context context) {
		super(context);
		// dbHelper = new EduSqliteDbHelper(context);
		// mDb = SQLiteDatabase.openOrCreateDatabase(
		// dbHelper.DATABASE_PATH2, null);
		// sqliteDatabase = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static EduSqliteDbManager<?> getInstance(Context context) {
		if (instance == null)
			instance = new EduSqliteDbManager(context);
		return instance;
	}

	/***
	 * 获取本类对象实例
	 * 
	 * @param context
	 *            上下文对象
	 * @return
	 */
	// @SuppressWarnings("rawtypes")
	// public static final EduSqliteDbManager<?> getInstance(Context context) {
	// if (instance == null)
	// instance = new EduSqliteDbManager(context);
	// return instance;
	// }

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (mDb.isOpen())
			mDb.close();
		if (instance != null)
			instance = null;
	}

	/**
	 * 插入数据
	 * 
	 * @param sql
	 *            执行更新操作的sql语句
	 * @param bindArgs
	 *            sql语句中的参数,参数的顺序对应占位符顺序
	 * @return result 返回新添记录的行号，与主键id无关
	 */
	public Long insertDataBySql(String sql, String[] bindArgs) throws Exception {
		long result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		SQLiteStatement statement = mDb.compileStatement(sql);
		if (bindArgs != null) {
			int size = bindArgs.length;
			for (int i = 0; i < size; i++) {
				// 将参数和占位符绑定，对应
				statement.bindString(i + 1, bindArgs[i]);
			}
			result = statement.executeInsert();
			statement.close();
		}
		mDb.close();
		return result;
	}

	public void executeSql(String sql) {
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.execSQL(sql);
		mDb.close();

	}

	// public void getValue(Context context) {
	// DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME,
	// null);
	// mDb = helper.getWritableDatabase();
	// Cursor c = mDb.rawQuery("select * from tb_view where  id = 17",
	// new String[] {});
	// c.moveToFirst();
	// mDb.close();
	// Log.e("userAnswer",
	// "user-->" + c.getString(c.getColumnIndex("user_answer")));
	// }

	/**
	 * TODO 执行多条sql
	 * 
	 * @param sqls
	 *            日 期:2013-11-15
	 */
	public void executMultiDataBySql(ArrayList<String> sqls) {
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		mDb.beginTransaction();
		for (int i = 0; i < sqls.size(); i++) {
			System.out.println(sqls.get(i));
			mDb.execSQL(sqls.get(i));
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            要插入的数据
	 * @return result 返回新添记录的行号，与主键id无关
	 */
	public Long insertData(String table, ContentValues values) {
		long result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		result = mDb.insert(table, null, values);
		mDb.close();
		return result;
	}

	/**
	 * 更新数据
	 * 
	 * @param sql
	 *            执行更新操作的sql语句
	 * @param bindArgs
	 *            sql语句中的参数,参数的顺序对应占位符顺序
	 */
	public void updateDataBySql(String sql, String[] bindArgs) throws Exception {
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		SQLiteStatement statement = mDb.compileStatement(sql);
		if (bindArgs != null) {
			int size = bindArgs.length;
			for (int i = 0; i < size; i++) {
				statement.bindString(i + 1, bindArgs[i]);
			}
			statement.execute();
			statement.close();
		}
		mDb.close();
	}

	/**
	 * 更新数据
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            表示更新的数据
	 * @param whereClause
	 *            表示SQL语句中条件部分的语句
	 * @param whereArgs
	 *            表示占位符的值
	 * @return
	 */
	public int updataData(String table, ContentValues values, String whereClause, String[] whereArgs) {
		int result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		result = mDb.update(table, values, whereClause, whereArgs);
		mDb.close();
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param sql
	 *            执行更新操作的sql语句
	 * @param bindArgs
	 *            sql语句中的参数,参数的顺序对应占位符顺序
	 */
	public void deleteDataBySql(String sql, String[] bindArgs) throws Exception {
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		SQLiteStatement statement = mDb.compileStatement(sql);
		if (bindArgs != null) {
			int size = bindArgs.length;
			for (int i = 0; i < size; i++) {
				statement.bindString(i + 1, bindArgs[i]);
			}
			Method[] mm = statement.getClass().getDeclaredMethods();
			for (Method method : mm) {
				Log.i("info", method.getName());
				/**
				 * 反射查看是否能获取executeUpdateDelete方法 查看源码可知
				 * executeUpdateDelete是public的方法，但是好像被隐藏了所以不能被调用，
				 * 利用反射貌似只能在root以后的机器上才能调用，小米是可以，其他机器却不行，所以还是不能用。
				 */
			}
			statement.execute();
			statement.close();
		}
		mDb.close();
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
	public int deleteData(String table, String whereClause, String[] whereArgs) {
		int result = 0;
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		result = mDb.delete(table, whereClause, whereArgs);
		mDb.close();
		return result;
	}

	/**
	 * 查询数据
	 * 
	 * @param searchSQL
	 *            执行查询操作的sql语句
	 * @param selectionArgs
	 *            查询条件
	 * @return 返回查询的游标，可对数据自行操作，需要自己关闭游标
	 */
	public Cursor queryData2Cursor(String sql, String[] selectionArgs) throws Exception {
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		Cursor cursor = mDb.rawQuery(sql, selectionArgs);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		mDb.close();
		return cursor;
	}

	/**
	 * 查询数据
	 * 
	 * @param sql
	 *            执行查询操作的sql语句
	 * @param selectionArgs
	 *            查询条件
	 * @param object
	 *            Object的对象
	 * @return List<Object> 返回查询结果
	 */
	public List<T> queryData2Object(String sql, String[] selectionArgs, Class<T> clazz) throws Exception {
		List<T> mList = new ArrayList<T>();
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		Cursor cursor = mDb.rawQuery(sql, selectionArgs);
		Field[] fields;
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				T object = clazz.newInstance();
				fields = object.getClass().getDeclaredFields();
				Object obj;
				for (int i = 0; i < fields.length; i++) {
					System.out.println("+++++++++++++++++++++++++++" + fields[i].getName());
					// 为JavaBean 设值
					if (fields[i].getType().equals(Integer.class)) {
						obj = cursor.getInt(cursor.getColumnIndex(fields[i].getName()));
					} else {
						obj = cursor.getString(cursor.getColumnIndex(fields[i].getName()));
					}
					invokeSet(object, fields[i].getName(), obj);
				}
				mList.add(object);
			}
		}
		if (cursor != null)
			cursor.close();
		mDb.close();
		return mList;
	}

	/**
	 * 查询数据
	 * 
	 * @param sql
	 *            执行查询操作的sql语句
	 * @param selectionArgs
	 *            查询条件
	 * @param object
	 *            Object的对象
	 * @return List<Map<String, Object>> 返回查询结果
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryData2Map(String sql, String[] selectionArgs, Object object) throws Exception {
		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		DBHelper helper = new DBHelper(mContext, Constant.DATABASE_NAME, null);
		mDb = helper.getWritableDatabase();
		Cursor cursor = mDb.rawQuery(sql, selectionArgs);
		Field[] f;
		Map<String, Object> map;
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				map = new HashMap<String, Object>();
				f = object.getClass().getDeclaredFields();
				for (int i = 0; i < f.length; i++) {
					map.put(f[i].getName(), cursor.getString(cursor.getColumnIndex(f[i].getName())));
				}
				mList.add(map);
			}
		}
		if (cursor != null)
			cursor.close();
		mDb.close();
		return mList;
	}

	/**
	 * java反射bean的set方法
	 * 
	 * @param objectClass
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行set方法
	 * 
	 * @param object
	 *            执行对象
	 * @param fieldName
	 *            属性
	 * @param value
	 *            值
	 */
	public void invokeSet(Object object, String fieldName, Object value) {
		Method method = getSetMethod(object.getClass(), fieldName);
		try {
			method.invoke(object, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public BaseData parseCursor(Cursor curs) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void setTableName() {
//		// TODO Auto-generated method stub
//		
//	}
}
