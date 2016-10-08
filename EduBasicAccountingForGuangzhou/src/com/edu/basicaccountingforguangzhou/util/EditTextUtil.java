package com.edu.basicaccountingforguangzhou.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.widget.EditText;

/**
 * EditText工具类
 * 
 * @author lucher
 * 
 */
public class EditTextUtil {
	/**
	 * 禁用系统输入法 且使用反射达到显示光标 让其默认不弹出输入法，可在manifest里加入
	 * android:windowSoftInputMode="adjustUnspecified|stateHidden" >
	 * 
	 * @param et
	 */
	public static void setInputtypeNull(EditText et) {
		Class<EditText> cls = EditText.class;
		try {
			Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
			setShowSoftInputOnFocus.setAccessible(false);
			setShowSoftInputOnFocus.invoke(et, false);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
