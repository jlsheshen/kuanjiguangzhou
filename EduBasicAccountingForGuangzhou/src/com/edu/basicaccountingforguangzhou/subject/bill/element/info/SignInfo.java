package com.edu.basicaccountingforguangzhou.subject.bill.element.info;

import com.edu.basicaccountingforguangzhou.subject.bill.element.ElementType;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 对应印章的数据
 * 
 * @author lucher
 * 
 */
public class SignInfo extends BaseElementInfo {

	// 是否为用户印章
	private boolean user;
	// 印章图片
	private Bitmap bitmap;

	public SignInfo() {

	}

	/**
	 * 构造
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	public SignInfo(int x, int y) {
		super(x, y, 0, 0, ElementType.TYPE_SIGN);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		Log.e("wwwwwwww", "执行setBitmap流1");
	
		Log.e("wwwwwwww", "执行setBitmap流1" + bitmap.getWidth());


		setWidth(bitmap.getWidth());	
		Log.e("wwwwwwww", "执行setBitmap流2");

		setHeight(bitmap.getHeight());
		Log.e("wwwwwwww", "执行setBitmap流3");

	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

}
