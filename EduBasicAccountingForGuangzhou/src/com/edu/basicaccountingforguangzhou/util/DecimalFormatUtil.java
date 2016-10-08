package com.edu.basicaccountingforguangzhou.util;

import java.text.DecimalFormat;

/**
 * @author Lucher 本类用来按指定格式来格式化一个小数
 */

public class DecimalFormatUtil {

	/**
	 * 把number按照pattern格式化
	 * 
	 * @param number
	 *            待格式化的小数
	 * @param pattern
	 *            格式化的正则："0"-无小数，"0.00"-两位小数，"0.0000"-四位小数。。。
	 *            格式化的正则："#"-无小数，"#.00"-两位小数，"#.0000"-四位小数。。。
	 * @return 格式化后的小数
	 */
	public static String formatFloat(float number, String pattern) {
		DecimalFormat decimalFormat = new DecimalFormat(pattern);// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		String result = decimalFormat.format(number);// format 返回的是字符串

		return result;
	}
}
