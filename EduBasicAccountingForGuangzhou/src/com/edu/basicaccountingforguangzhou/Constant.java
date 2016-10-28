package com.edu.basicaccountingforguangzhou;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 常量定义
 * 
 * @author lucher
 * 
 */
public class Constant {

	/**
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "EduBasicAccounting.db";
	// 视频路径
	public static final String PATH_VEDIO = "/mnt/sdcard/EduResources/EduMicroVideo/";

	/********** 题型 **********/
	/**
	 * 题型：单项
	 */
	public static final int SUBJECT_TYPE_SINGLE_SELECT = 1;
	/**
	 * 题型：多选
	 */
	public static final int SUBJECT_TYPE_MULTI_SELECT = 2;
	/**
	 * 题型：判断
	 */
	public static final int SUBJECT_TYPE_JUDGE = 3;
	/**
	 * 题型：分录
	 */
	public static final int SUBJECT_TYPE_ENTRY = 4;
	/**
	 * 题型：填制
	 */
	public static final int SUBJECT_TYPE_BILL = 5;
	/**
	 * 分组单据题
	 */
	public static final int SUBJECT_GROUP_BILL = 9;

	/**
	 * 当前选中的章节id
	 */
	public static int CURRENT_CHAPTER_ID = 0;
	/**
	 * 当前选中的章节名称
	 */
	public static String CHAPTER_NAME = "第一章 概述";
	/**
	 * 当前选中的父章节id
	 */
	public static int CURRENT_PARENT_CHAPTER_ID = 1;
	public static final String SCORE = "";
	/**
	 * 节名称
	 */
	public static String CHAPTER_CHILD_NAME = "第一节 会计的概述";
	/**
	 * 用于答题界面显示数字键盘时候需要减去的区域高度
	 */
	public static int TITLE_HEIGHT = 0;

	/**
	 * 关卡状态：开启
	 */
	public static final int STATE_ENABLE = 1;
	/**
	 * 关卡状态：未开启
	 */
	public static final int STATE_DISABLE = 0;

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTime(String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		String date = sdf.format(new Date());
		return date;
	}

}
