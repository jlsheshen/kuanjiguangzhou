package com.edu.basicaccountingforguangzhou.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

public class Utils {
	private static Toast toast = null;

	public static void showToast(String showString, Context context) {
		if (toast == null) {
			toast = Toast.makeText(context, showString, Toast.LENGTH_SHORT);
		} else {
			toast.setText(showString);
		}
		toast.show();
	}

	public static void showToast(int resId, Context context) {
		String showString = context.getResources().getString(resId);
		if (toast == null) {
			toast = Toast.makeText(context, showString, Toast.LENGTH_SHORT);
		} else {
			toast.setText(showString);
		}
		toast.show();
	}

	public static String stringFormat(String str) {
		if (str != null) {
			return str.trim();
		} else {
			return "";
		}
	}

	public static int getCombinations(int size, int count) {
		if (size / 5 > 2 && count < size) {
			return 10;
		} else if (size / 5 > 2 && count == size) {
			return 1;
		}
		List<List<Object>> result = new ArrayList<List<Object>>();
		long n = (long) Math.pow(2, size);
		List<Object> combine;
		for (long l = 0L; l < n; l++) {
			combine = new ArrayList<Object>();
			for (int i = 0; i < size; i++) {
				if ((l >>> i & 1) == 1) {
					combine.add(new Object());
				}
			}
			if (combine.size() == count)
				result.add(combine);
		}
		return result.size();
	}
}
