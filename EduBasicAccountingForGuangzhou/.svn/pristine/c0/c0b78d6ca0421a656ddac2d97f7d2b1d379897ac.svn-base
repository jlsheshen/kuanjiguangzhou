package com.edu.basicaccountingforguangzhou.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * bitmap解析工具类，目前支持assets和file
 * 
 * @author lucher
 * 
 */
public class BitmapParseUtil {

	private static final String TAG = "BitmapParseUtil";
	// assets图片资源前缀
	private static String ASSETS_PREFIX = "assets://";
	// file图片资源前缀
	private static String FILE_PREFIX = "file://";

	/**
	 * 解析图片
	 * 
	 * @param uri
	 * @param context
	 *            assets资源需要
	 * @return
	 */
	public static Bitmap parse(String uri, Context context) {
		Log.d(TAG, "parsing...uri:" + uri);
		Bitmap bitmap = null;
		try {
			if (uri.startsWith(ASSETS_PREFIX)) {
				uri = uri.substring(ASSETS_PREFIX.length(), uri.length());
				bitmap = BitmapFactory.decodeStream(context.getAssets().open(uri));
			} else if (uri.startsWith(FILE_PREFIX)) {
				uri = uri.substring(FILE_PREFIX.length(), uri.length());
				bitmap = BitmapFactory.decodeFile(uri);
			} else {
				Log.e(TAG, "parse error, invalid uri:" + uri);
			}
		} catch (Exception e) {
			Log.e(TAG, "parse error, uri:" + uri);
			e.printStackTrace();
		}

		return bitmap;
	}
}
