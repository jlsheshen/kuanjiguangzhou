package com.edu.basicaccountingforguangzhou.subject.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.subject.bill.element.ElementType;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BaseElementInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.FlashInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.SignInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplate;
import com.edu.basicaccountingforguangzhou.subject.util.BitmapParseUtil;
import com.edu.library.util.ToastUtil;

/**
 * 单据模板数据库操作类
 * 
 * @author lucher
 * 
 */
public class TemplateDataDao {

	private static final String TAG = "BillTestDataDao";

	/**
	 * 自身引用
	 */
	private static TemplateDataDao instance = null;
	private Context mContext;

	private TemplateDataDao(Context context) {
		mContext = context;
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static TemplateDataDao getInstance(Context context) {
		if (instance == null)
			instance = new TemplateDataDao(context);
		return instance;
	}

	/**
	 * 根据id获取模板数据
	 * 
	 * @param id
	 * @return
	 */
	public BillTemplate getBillTemplate(int id, SQLiteDatabase db) {
		BillTemplate template = null;
		Cursor curs = null;

		String sql = "SELECT * FROM TB_BILL_TEMPLATE LEFT JOIN TB_TEMPLATE_ELEMENTS ON TB_BILL_TEMPLATE.ID = TB_TEMPLATE_ELEMENTS.TEMPLATE_ID WHERE TB_BILL_TEMPLATE.ID = " + id;
		// Log.d(TAG, "sql:" + sql);

		curs = db.rawQuery(sql, null);

		if (curs != null) {
			List<BaseElementInfo> elements = new ArrayList<BaseElementInfo>(curs.getCount());
			int i = 0;

			while (curs.moveToNext()) {
				if (curs.isFirst()) {
					template = new BillTemplate();
					template.setId(curs.getInt(0));
					template.setName(curs.getString(1));
					template.setBitmap(BitmapParseUtil.parse(curs.getString(2), mContext));
					template.setFlag(curs.getInt(3));
				}
				BaseElementInfo element;
				if (i==16) {

				}

				int type = curs.getInt(7);

				switch (type) {
				case ElementType.TYPE_SIGN:
					element = new SignInfo();
					initElement(element, curs);
					((SignInfo) element).setUser(false);
					
					Bitmap bitmap = getSignBitmap(curs.getInt(12), db);

					((SignInfo) element).setBitmap(bitmap);

					break;
				case ElementType.TYPE_FLASH:
					element = new FlashInfo();
					initElement(element, curs);

					break;

				default:
					element = new BlankInfo();
					initElement(element, curs);
					try {
						((BlankInfo) element).setTextSize(curs.getInt(12));
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtil.showToast(mContext, "字体大小必须为整数：" + element);
						((BlankInfo) element).setTextSize(0);
					}

					break;
				}

				elements.add(element);

			}

			template.setElementDatas(elements);

		}

		return template;
	}

	/**
	 * 初始化element
	 * 
	 * @param element
	 * @param curs
	 */
	private void initElement(BaseElementInfo element, Cursor curs) {
		element.setId(curs.getInt(5));
		element.setType(curs.getInt(7));
		element.setX(curs.getInt(8));
		element.setY(curs.getInt(9));
		element.setWidth(curs.getInt(10));
		element.setHeight(curs.getInt(11));
		element.setScore(curs.getFloat(13));
	}

	/**
	 * 获取印章图片
	 * 
	 * @param id
	 * @return
	 */
	public Bitmap getSignBitmap(int id, SQLiteDatabase db) {
		Bitmap bitmap = null;
		String sql = "SELECT * FROM TB_SIGN WHERE ID = " + id;

		Cursor curs = db.rawQuery(sql, null);

		if (curs != null) {

			curs.moveToFirst();
			String uri = curs.getString(3);

			bitmap = BitmapParseUtil.parse(uri, mContext);
		}
		return bitmap;
	}

}