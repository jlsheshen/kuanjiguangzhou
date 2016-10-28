package com.edu.basicaccountingforguangzhou.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.edu.basicaccountingforguangzhou.info.QuestionInfo;
import com.edu.basicaccountingforguangzhou.info.RandomBlankEntity;
import com.edu.basicaccountingforguangzhou.info.SignInfo;
import com.edu.basicaccountingforguangzhou.info.TypeInfo;
import com.edu.basicaccountingforguangzhou.info.UserSignInfo;
import com.edu.basicaccountingforguangzhou.util.Utils;
import com.edu.ime.ViewInfo;
import com.edu.library.util.RandomUtil;

public class EduSqliteDbOprater {

	private static final int QUES_RANDOM_COUNT = 5;
	private static final String TAG = "EduSqliteDbOprater";
	private EduSqliteDbManager<?> dbMangager;
	private static final String[] bigChineseNum = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾" };

	public EduSqliteDbOprater(Context context) {
		dbMangager = EduSqliteDbManager.getInstance(context);
	}

	/**
	 * @param id
	 * @param flag
	 *            1为正常获取 其他为获取随机
	 * @return
	 */
	public QuestionInfo getQuestionInfoByTypeIdForTest(int flag, int id) {

		QuestionInfo qi = new QuestionInfo();
		// is_completed 0为填制 1为审核
		String sql = "SELECT que.id id " + ",que.content content " + ",que.pic_name pic_name" + ",bs.bs_name bs_name " + ",que.is_completed is_completed " + ",bs.id bs_id "
				+ "FROM tb_subject_bill que " + ", tb_basemap bs " + "WHERE que.basemap_id = bs.id and que.id = " + id;
		Log.e("sql############", "sql--->" + sql);
		try {
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标

			qi.setId(cursor.getInt(cursor.getColumnIndex("id"))); // question主键
			qi.setBsId(cursor.getInt(cursor.getColumnIndex("bs_id"))); // question主键
			qi.setBsName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("bs_name")))); // 地图名称
			// Log.e("bs_name",
			// "bs_name--->"
			// + Utils.stringFormat(cursor.getString(cursor
			// .getColumnIndex("bs_name"))));
			qi.setContent(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("content")))); // 题目内容
			qi.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
			qi.setLsSign(getSignInfo(qi.getId())); // 印章信息
			// // 控件EditTetx信息

			// Log.e("flag", "flag-->" + flag);
			if (flag == 1) {
				qi.setLsET(getViewInfoET(qi.getId(), 1));
			} else {
				qi.setLsET(getViewInfoRandomET(qi.getId()));
			}
			qi.setLsUserSign(getUserSignInfo(qi.getId()));
			qi.setPicName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("pic_name"))));
		} catch (Exception e) {
			Log.e("出事了！！%% getQuestionInfoByTypeId", e.toString());
			e.printStackTrace();

		}
		return qi;
	}

	/**
	 * 按照子级分类取得问题
	 * 
	 * @param types
	 *            子级分类
	 * @return 某一类型题目信息
	 */
	public List<QuestionInfo> getQuestionInfoByTypeId(String types) {

		List<QuestionInfo> listQuestion = new ArrayList<QuestionInfo>();

		String sql = "SELECT que.id id " + ",que.content content " + ",bs.bs_name bs_name " + ",que.is_completed is_completed " + ",bs.id bs_id " + "FROM tb_subject_bill que " + ", tb_basemap bs "
				+ "WHERE que.basemap_id = bs.id and que.type_id in (" + types + ")";

		try {
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标
			// Log.e("sql", "sql--->" + cursor.getCount());
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				QuestionInfo qi = new QuestionInfo();

				qi.setId(cursor.getInt(cursor.getColumnIndex("id"))); // question主键
				qi.setBsId(cursor.getInt(cursor.getColumnIndex("bs_id"))); // question主键
				qi.setBsName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("bs_name")))); // 地图名称
				qi.setContent(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("content")))); // 题目内容
				qi.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
				qi.setLsSign(getSignInfo(qi.getId())); // 印章信息
				qi.setLsET(getViewInfoET(qi.getId(), 1)); // 控件EditTetx信息
				qi.setLsUserSign(getUserSignInfo(qi.getId()));

				listQuestion.add(qi);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		return listQuestion;
	}

	/**
	 * 按照父级分类取得问题
	 * 
	 * @param types
	 *            父级分类
	 * @return 某一类型题目信息
	 */
	public List<QuestionInfo> getQuestionInfoByParentId(String types) {

		List<QuestionInfo> listQuestion = new ArrayList<QuestionInfo>();

		String sql = "SELECT que.id id " + ",que.content content " + ",que.is_completed is_completed " + ",bs.bs_name bs_name " + ",bs.id bs_id " + "FROM tb_subject_bill que " + ", tb_basemap bs "
				+ ", tb_type t " + "WHERE que.basemap_id = bs.id " + "and que.type_id = t.id " + "and t.parent_id in (" + types + ")";
		try {
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				QuestionInfo qi = new QuestionInfo();

				qi.setId(cursor.getInt(cursor.getColumnIndex("id"))); // question主键
				qi.setBsId(cursor.getInt((cursor.getColumnIndex("bs_id")))); // question主键
				qi.setBsName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("bs_name")))); // 地图名称
				qi.setContent(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("content")))); // 题目内容
				qi.setIsCompleted(Byte.parseByte(cursor.getString(cursor.getColumnIndex("is_completed"))));
				qi.setLsSign(getSignInfo(qi.getId())); // 印章信息
				qi.setLsET(getViewInfoETNoUserAnswer(qi.getId())); // 控件EditTetx信息

				qi.setLsUserSign(new ArrayList<UserSignInfo>());

				listQuestion.add(qi);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listQuestion;
	}

	/**
	 * 按照question_id取印章信息
	 * 
	 * @param questionId
	 *            tb_subject_bill表外键
	 * @return 某道题目的印章信息
	 */
	public List<SignInfo> getSignInfo(int questionId) {

		List<SignInfo> listSign = new ArrayList<SignInfo>();

		String sql = "SELECT s.id " // 印章ID
				+ ",s.content " // 印章
				+ ",s.sign_name " // 印章对应的图片名称（eg:"x.png"）
				+ ",s.x_axis " + ",s.y_axis " + "FROM  tb_subject_bill que " + ", tb_sign s " + "WHERE s.question_id = que.id " + "and que.id = " + Integer.toString(questionId);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				SignInfo si = new SignInfo();

				si.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 印章ID
				si.setSignName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("sign_name")))); // 印章对应的图片名称（eg:"x.png"）
				si.setContent(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("content")))); // 印章名称（eg:"财务专用章"）
				si.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis"))); // 印章横坐标
				si.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis"))); // 印章纵坐标

				listSign.add(si);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listSign;
	}

	public List<UserSignInfo> getUserSignInfo(int questionId) {

		List<UserSignInfo> listUserSign = new ArrayList<UserSignInfo>();

		String sql = "SELECT question_id " // 印章ID
				+ ",user_x_axis " // 印章
				+ ",user_y_axis " // 印章对应的图片名称（eg:"x.png"）
				+ ",user_sign_name " + ",is_right " + "FROM tb_user_sign " + "WHERE question_id =  " + Integer.toString(questionId);
		System.out.println(sql);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				UserSignInfo si = new UserSignInfo();

				si.setQuestionID(cursor.getInt(cursor.getColumnIndex("question_id"))); // 印章ID
				si.setUserSignName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("user_sign_name")))); // 印章对应的图片名称（eg:"x.png"）
				si.setUserXAxis(cursor.getInt(cursor.getColumnIndex("user_x_axis"))); // 印章横坐标
				si.setUserYAxis(cursor.getInt(cursor.getColumnIndex("user_y_axis"))); // 印章纵坐标
				si.setRight(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_right")))); // 所盖印章是否正确
				si.setInserted(true); // 已经插入过了
				listUserSign.add(si);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listUserSign;
	}

	/**
	 * 
	 * 以指定格式获取当前日期
	 * 
	 * @param yymmdd
	 *            1为年 2月 3为日
	 * @param flag
	 *            1为大写中文 2为阿拉伯数字
	 * @param mode
	 *            获取的模式，1-当前日期，2-与当前不同的随机日期
	 * @return
	 */
	public String getCurrentDateString(int yymmdd, int flag, int mode) {
		String returnResult = new String();
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		if (mode == 2) {// 随机出与今天不同的日期来充当审核模块的错误答案
			year = RandomUtil.getRandomData(1000, 2999, year);
			month = RandomUtil.getRandomData(1, 12, month);
			date = RandomUtil.getRandomData(1, 31, date);
		}
		Log.i(TAG, String.format("y:%d,m:%d,d:%d", year, month, date));

		if (flag == 1) {
			StringBuffer sb = new StringBuffer();
			if (yymmdd == 1) {
				returnResult = sb.append(bigChineseNum[year / 1000]).append(bigChineseNum[year % 1000 / 100]).append(bigChineseNum[year % 100 / 10]).append(bigChineseNum[year % 10]).toString();
			} else if (yymmdd == 2) {
				if (month < 10) {
					returnResult = sb.append(bigChineseNum[0]).append(bigChineseNum[month]).toString();
				} else if (month == 10) {
					returnResult = sb.append(bigChineseNum[0]).append(bigChineseNum[1]).append(bigChineseNum[10]).toString();
				} else {
					returnResult = sb.append(bigChineseNum[1]).append(bigChineseNum[10]).append(bigChineseNum[month % 10]).toString();
				}
			} else if (yymmdd == 3) {
				if (date < 10) {
					returnResult = sb.append(bigChineseNum[0]).append(bigChineseNum[date]).toString();
				} else if (date >= 10 && date % 10 == 0) {
					if (date == 10) {
						returnResult = sb.append(bigChineseNum[0]).append(bigChineseNum[date / 10]).append(bigChineseNum[10]).toString();
					} else {
						returnResult = sb.append(bigChineseNum[date / 10]).append(bigChineseNum[10]).toString();
					}
				} else {
					returnResult = sb.append(bigChineseNum[date / 10]).append(bigChineseNum[10]).append(bigChineseNum[date % 10]).toString();
				}
			}
		} else if (flag == 2) {
			if (yymmdd == 1) {
				returnResult = String.valueOf(year);
			} else if (yymmdd == 2) {
				if (month < 10) {
					returnResult = "0" + String.valueOf(month);
				} else {
					returnResult = String.valueOf(month);
				}
			} else if (yymmdd == 3) {
				if (date < 10) {
					returnResult = "0" + String.valueOf(date);
				} else {
					returnResult = String.valueOf(date);
				}
			}
		}
		return returnResult;
	}

	/**
	 * 
	 * @param questionId
	 *            题目ID
	 * @param flag
	 *            1取出用户答案 2为不取用户答案（测试用）
	 * @return
	 */
	public List<ViewInfo> getViewInfoET(int questionId, int flag) {

		List<ViewInfo> listView = new ArrayList<ViewInfo>();

		String sql = "SELECT v.question_id " + ",v.id " + ",v.height " + ",v.width " + ",v.initial_value " + ",v.editable " + ",v.input_type " + ",v.x_axis " + ",v.y_axis " + ",v.answer "
				+ ",v.user_answer user_answer " + ",v.radio_group " + ",v.randomFlah " + ",v.isRighted " + "FROM tb_subject_bill que " + ", tb_view v "
				+ "WHERE que.id = v.question_id and v.radio_group = 0 and que.id = " + Integer.toString(questionId) + " order by v.id";
		// Log.e("sql", "sql - getViewInfoET--> " + sql);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ViewInfo vi = new ViewInfo();
				vi.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
				vi.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
				vi.setInitialValue(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("initial_value"))));
				vi.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis")));
				vi.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis")));
				vi.setEditable(cursor.getInt(cursor.getColumnIndex("editable")));
				int inputType = cursor.getInt(cursor.getColumnIndex("input_type"));
				vi.setInputTyep(inputType);

				if (inputType > 100 && inputType / 100 == 1) {
					String strData = cursor.getString(cursor.getColumnIndex("answer")) == null ? null : cursor.getString(cursor.getColumnIndex("answer")).replace("￥", "¥");
					if (strData == null || "".equals(strData)) {
						if (inputType % 100 % 10 == 4) {
							vi.setAnswer(getCurrentDateString(1, 1, 1));
						} else if (inputType % 100 % 10 == 5) {
							vi.setAnswer(getCurrentDateString(2, 1, 1));
						} else if (inputType % 100 % 10 == 6) {
							vi.setAnswer(getCurrentDateString(3, 1, 1));
						}
					} else {
						vi.setAnswer(strData);
					}
				} else if (inputType > 100 && inputType / 100 == 2) {
					String strData = cursor.getString(cursor.getColumnIndex("answer")) == null ? null : cursor.getString(cursor.getColumnIndex("answer")).replace("￥", "¥");
					if (strData == null || "".equals(strData)) {
						if (inputType % 100 % 10 == 1) {
							vi.setAnswer(getCurrentDateString(1, 2, 1));
						} else if (inputType % 100 % 10 == 2) {
							vi.setAnswer(getCurrentDateString(2, 2, 1));
						} else if (inputType % 100 % 10 == 3) {
							vi.setAnswer(getCurrentDateString(3, 2, 1));
						}
					} else {
						vi.setAnswer(strData);
					}
				} else {
					vi.setAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))) == null ? null
							: Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))).replace("￥", "¥"));
				}
				if (flag == 2) {
					vi.setUserAnswer("");
				} else {
					vi.setUserAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("user_answer"))));
				}

				vi.setRadioGroup(cursor.getInt(cursor.getColumnIndex("radio_group")));
				vi.setRandomFlag(cursor.getInt(cursor.getColumnIndex("randomFlah")));
				vi.setIsRighted(cursor.getInt(cursor.getColumnIndex("isRighted")));
				listView.add(vi);
			}
			// Log.e("listView", "listView - size --> " + listView.size());
		} catch (Exception e) {
			Log.e("出事了 里面", e.toString());
			e.printStackTrace();

		}

		return listView;
	}

	public void setViewInfoRighted(int id) {
		String sql = "update tb_view set isRighted = 1 where id = " + id;
		dbMangager.executeSql(sql);
	}

	// public boolean isThisViewRighted(int id){
	// boolean isRighted;
	// String sql = "select * from tb_view where id = "+id;
	// try {
	// Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});
	// if
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 按照question_id取控件信息
	 * 
	 * @param questionId
	 *            tb_subject_bill表外键
	 * @return 某道题目的EditText信息
	 */
	public List<ViewInfo> getViewInfoRandomET(int questionId) {

		List<ViewInfo> listView = new ArrayList<ViewInfo>();

		// 多组View的List存储
		List<ViewInfo> numAmountList = new ArrayList<ViewInfo>();

		HashMap<Integer, List<ViewInfo>> dateNumMap = new HashMap<Integer, List<ViewInfo>>();
		HashMap<Integer, List<ViewInfo>> dateBigMap = new HashMap<Integer, List<ViewInfo>>();

		String sql = "SELECT v.question_id " + ",v.id " + ",v.height " + ",v.width " + ",v.initial_value " + ",v.editable " + ",v.input_type " + ",v.x_axis " + ",v.y_axis " + ",v.answer "
				+ ",v.user_answer user_answer " + ",v.radio_group " + ",v.type_index " + ",v.randomFlah " + ",v.isRighted " + "FROM tb_subject_bill que " + ", tb_view v "
				+ "WHERE que.id = v.question_id and v.radio_group = 0 and que.id = " + Integer.toString(questionId) + " order by v.id";
		// Log.e("sql", "sql - getViewInfoET-->22222 " + sql);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});
			int groupId = 1;
			int localId = 1;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ViewInfo vi = new ViewInfo();

				vi.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
				vi.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
				vi.setInitialValue(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("initial_value"))));
				vi.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis")));
				vi.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis")));
				vi.setEditable(cursor.getInt(cursor.getColumnIndex("editable")));
				int inputType = cursor.getInt(cursor.getColumnIndex("input_type"));
				if (inputType > 100 && inputType / 100 == 1) {
					if (inputType % 100 % 10 == 4) {
						vi.setAnswer(getCurrentDateString(1, 1, 1));
					} else if (inputType % 100 % 10 == 5) {
						vi.setAnswer(getCurrentDateString(2, 1, 1));
					} else if (inputType % 100 % 10 == 6) {
						vi.setAnswer(getCurrentDateString(3, 1, 1));
					}
				} else if (inputType > 100 && inputType / 100 == 2) {
					if (inputType % 100 % 10 == 1) {
						vi.setAnswer(getCurrentDateString(1, 2, 1));
					} else if (inputType % 100 % 10 == 2) {
						vi.setAnswer(getCurrentDateString(2, 2, 1));
					} else if (inputType % 100 % 10 == 3) {
						vi.setAnswer(getCurrentDateString(3, 2, 1));
					}
				} else {
					vi.setAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))) == null ? null
							: Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))).replace("￥", "¥"));
				}
				vi.setUserAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("user_answer"))));
				vi.setRadioGroup(cursor.getInt(cursor.getColumnIndex("radio_group")));
				vi.setRandomEditable(0);
				vi.setRandomFlag(cursor.getInt(cursor.getColumnIndex("randomFlah")));
				vi.setIsRighted(cursor.getInt(cursor.getColumnIndex("isRighted")));
				if (inputType == 6) {
					numAmountList.add(vi);
					vi.setInputTyep(6);
					if (cursor.getInt(cursor.getColumnIndex("type_index")) == 0) {
						vi.setIsMutiple(1);
						vi.setMutipleViewList(numAmountList);
						listView.add(vi);
					}
				} else if (inputType > 100 && inputType < 200) {
					vi.setInputTyep(inputType);
					if (!dateBigMap.containsKey(inputType % 100 / 10)) {
						dateBigMap.put(inputType % 100 / 10, new ArrayList<ViewInfo>());
						dateBigMap.get(inputType % 100 / 10).add(vi);
					} else {
						dateBigMap.get(inputType % 100 / 10).add(vi);
					}
					// dateBigList.add(vi);
					if (cursor.getInt(cursor.getColumnIndex("type_index")) == 0) {
						vi.setIsMutiple(1);
						vi.setMutipleViewList(dateBigMap.get(inputType % 100 / 10));
						listView.add(vi);
					}
				} else if (inputType > 200 && inputType < 300) {
					// dateNumList.add(vi);
					vi.setInputTyep(inputType);
					if (!dateNumMap.containsKey(inputType % 100 / 10)) {
						dateNumMap.put(inputType % 100 / 10, new ArrayList<ViewInfo>());
						dateNumMap.get(inputType % 100 / 10).add(vi);
					} else {
						dateNumMap.get(inputType % 100 / 10).add(vi);
					}
					if (cursor.getInt(cursor.getColumnIndex("type_index")) == 0) {
						vi.setIsMutiple(1);
						vi.setMutipleViewList(dateNumMap.get(inputType % 100 / 10));
						listView.add(vi);
					}
				} else {
					vi.setIsMutiple(0);
					vi.setInputTyep(inputType);
					vi.setMutipleViewList(new ArrayList<ViewInfo>());
					listView.add(vi);
				}

			}
			// Log.e("listView", "listView - size --> " + listView.size());
			// for (ViewInfo viT : listView) {
			//
			// }

		} catch (Exception e) {
			Log.e("出事了！！！ getViewInfoET", e.toString());
			e.printStackTrace();

		}
		setRandomEditableId(questionId, listView);
		return listView;
	}

	public void clearUserAnswer(int questionId) {
		String sqlClear = "update tb_view set randomFlah = 0,user_answer='' where question_id = " + questionId;
		String sqlClearSign = "delete from TB_USER_SIGN where question_id = " + questionId;
		// Log.e("clearDbRandomFlag sql", "sql-->" + sqlClear);
		dbMangager.executeSql(sqlClear);
		dbMangager.executeSql(sqlClearSign);
	}

	public void deleteUserSign(int questionId) {
		String sqlClearSign = "delete from TB_USER_SIGN where question_id = " + questionId;
		dbMangager.executeSql(sqlClearSign);
	}
	// /**
	// * 根据bill_type_id和bill_id获取queid
	// *
	// * @return
	// */
	// public int getQueId(int bill_type_id, int bill_id) {
	// int id = 0;
	// Cursor curs = null;
	// String sql = "SELECT * FROM tb_subject_bill WHERE bill_type_id = " +
	// bill_type_id + " AND bill_id=" + bill_id;
	// try {
	// curs = dbMangager.queryData2Cursor(sql, new String[] {});
	// if (curs != null) {
	// if (curs.getCount() == 0)
	// return id;
	// curs.moveToFirst();
	//
	// id = curs.getInt(curs.getColumnIndex("id"));
	// }
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return id;
	// }

	public void setRandomEditableId(int quesId, List<ViewInfo> listView) {
		List<ViewInfo> withoutAlreadyList = new ArrayList<ViewInfo>(listView);

		String containSql = "select * from random_blank where ques_id = " + quesId;
		// Log.e("quesId", "quesID--> " + quesId);
		Cursor cursorContain = null;
		try {
			cursorContain = dbMangager.queryData2Cursor(containSql, new String[] {});
		} catch (Exception e) {
			Log.e("bbbbbbbbbbbbbbb", "Exception-->" + e.toString());
			e.printStackTrace();
		}
		// 该表还没有点开过. 为random_blank初始化数据
		if (cursorContain == null || cursorContain.getCount() == 0) {
			for (int i = 0; i < listView.size(); i++) {
				// 初始化不需要填的空被排除在外
				if (listView.get(i).getEditable() == 1) {
					String sql4 = "INSERT INTO  random_blank(view_link_id,edit_count,ques_id) " + "values(" + listView.get(i).getId() + "," + 0 + "," + quesId + ")";
					dbMangager.executeSql(sql4);
				} else {
					withoutAlreadyList.remove(listView.get(i));
				}
			}
			if (withoutAlreadyList.size() == 0)
				return;
			// 进行第一次随机取数
			int[] result = new int[QUES_RANDOM_COUNT];
			result = getRandomArrayIndex(withoutAlreadyList.size(), QUES_RANDOM_COUNT, withoutAlreadyList);

			// 将随机好的index赋值给复制list，并更新数据库中的计数器
			for (int ranId : result) {
				withoutAlreadyList.get(ranId).setRandomEditable(1);
				String updateSql = "update random_blank set edit_count = edit_count+1 where view_link_id = " + withoutAlreadyList.get(ranId).getId();
				dbMangager.executeSql(updateSql);
			}

			// 将复制list中随机好的对象值传给原有list
			for (ViewInfo vF : listView) {
				for (ViewInfo vfTemp : withoutAlreadyList) {
					if (vF.getId() == vfTemp.getId() && vfTemp.getRandomEditable() == 1) {
						vF.setRandomEditable(1);
					}
				}
			}

			// 已有数据，进行对随机到次数进行平均分布式伪随机的逻辑
		} else {
			String orderBySql = "select * from random_blank where ques_id =" + quesId + " order by edit_count";
			// Log.e("sql", "sql--> " + orderBySql);
			// 记录当前被随机过的最大次数
			Cursor orderByCursor = null;
			// 记录每个随机次数的实体类list
			List<RandomBlankEntity> countIntegerList = new ArrayList<RandomBlankEntity>();
			try {

				orderByCursor = dbMangager.queryData2Cursor(orderBySql, new String[] {});
				do {
					countIntegerList.add(new RandomBlankEntity(orderByCursor.getInt(orderByCursor.getColumnIndex("id")), orderByCursor.getInt(orderByCursor.getColumnIndex("view_link_id")),
							orderByCursor.getInt(orderByCursor.getColumnIndex("edit_count")), orderByCursor.getInt(orderByCursor.getColumnIndex("ques_id"))));
					// Log.e("id@@@@@",
					// "id---> "
					// + orderByCursor.getInt(orderByCursor
					// .getColumnIndex("id")));

				} while (orderByCursor.moveToNext());
				int[] resultId = new int[QUES_RANDOM_COUNT];
				// 将随机好的index赋值给复制list，并更新数据库中的计数器
				for (int ranId = 0; ranId < resultId.length; ranId++) {
					resultId[ranId] = countIntegerList.get(ranId).getViewLinkId();

				}
				for (ViewInfo vfTemp : withoutAlreadyList) {
					for (int ranId = 0; ranId < resultId.length; ranId++) {
						if (vfTemp.getId() == resultId[ranId]) {
							vfTemp.setRandomEditable(1);
							String updateSql = "update random_blank set edit_count = edit_count+1 where view_link_id = " + resultId[ranId];
							dbMangager.executeSql(updateSql);
						}
					}
				}

				// 将复制list中随机好的对象值传给原有list
				for (ViewInfo vF : listView) {
					for (ViewInfo vfTemp : withoutAlreadyList) {
						if (vF.getId() == vfTemp.getId() && vfTemp.getRandomEditable() == 1) {
							vF.setRandomEditable(1);
						}
					}
				}
			} catch (Exception e) {
				Log.e("出事了 setRandomEditableId", "出事了啊啊啊啊啊" + e.toString());
				e.printStackTrace();
			}
		}

	}

	public int[] getRandomArrayIndex(int max, int length, List<ViewInfo> listView) {
		int[] result = new int[length];
		boolean[] bool = new boolean[max];
		if (max < length) {
			for (int p = 0; p < length; p++) {
				result[p] = p;
			}
			return result;
		}

		for (int p = 0; p < result.length; p++) {
			result[p] = -1;
		}
		for (int i = 0; i < result.length; i++) {
			do {
				result[i] = new Random().nextInt(max);
			} while (bool[result[i]]);
			bool[result[i]] = true;
		}
		return result;
	}

	/**
	 * 按照question_id取控件信息,UserAnswer置空
	 * 
	 * @param questionId
	 *            tb_subject_bill表外键
	 * @return 某道题目的EditText信息
	 */
	public List<ViewInfo> getViewInfoETNoUserAnswer(int questionId) {

		List<ViewInfo> listView = new ArrayList<ViewInfo>();

		String sql = "SELECT v.question_id " + ",v.id " + ",v.height " + ",v.width " + ",v.initial_value " + ",v.x_axis " + ",v.y_axis " + ",v.answer "
		// + ",v.user_answer user_answer "
				+ ",v.radio_group " + "FROM tb_subject_bill que " + ", tb_view v " + "WHERE que.id = v.question_id and v.radio_group = 0 and que.id = " + Integer.toString(questionId);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ViewInfo vi = new ViewInfo();

				vi.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
				vi.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
				vi.setInitialValue(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("initial_value"))));
				vi.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis")));
				vi.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis")));
				vi.setAnswer(
						Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))) == null ? null : Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))).replace("￥", "¥"));
				vi.setUserAnswer("");
				vi.setRadioGroup(cursor.getInt(cursor.getColumnIndex("radio_group")));

				listView.add(vi);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listView;
	}

	/**
	 * 按照question_id取控件信息
	 * 
	 * @param questionId
	 *            tb_subject_bill表外键
	 * @return 某道题目的CheckBox信息
	 */
	public List<ViewInfo> getViewInfoCB(int questionId) {

		List<ViewInfo> listView = new ArrayList<ViewInfo>();

		String sql = "SELECT v.question_id " + ",v.id " + ",v.height " + ",v.width " + ",v.initial_value " + ",v.x_axis " + ",v.y_axis " + ",v.answer " + ",v.user_answer " + ",v.radio_group "
				+ "FROM tb_subject_bill que " + ", tb_view v " + "WHERE que.id = v.question_id and v.radio_group <> 0 and que.id = " + Integer.toString(questionId);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ViewInfo vi = new ViewInfo();

				vi.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
				vi.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
				vi.setInitialValue(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("initial_value"))));
				vi.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis")));
				vi.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis")));
				vi.setAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))));
				vi.setUserAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("user_answer"))));
				vi.setRadioGroup(cursor.getInt(cursor.getColumnIndex("radio_group")));
				// Log.d("answer",
				// cursor.getString(cursor.getColumnIndex("answer")));
				listView.add(vi);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listView;
	}

	/**
	 * 按照question_id取控件信息
	 * 
	 * @param questionId
	 *            tb_subject_bill表外键
	 * @return 某道题目的CheckBox信息
	 */
	public List<ViewInfo> getViewInfoCBNoUserAnswer(int questionId) {

		List<ViewInfo> listView = new ArrayList<ViewInfo>();

		String sql = "SELECT v.question_id " + ",v.id " + ",v.height " + ",v.width " + ",v.initial_value " + ",v.x_axis " + ",v.y_axis " + ",v.answer "
		// + ",v.user_answer "
				+ ",v.radio_group " + "FROM tb_subject_bill que " + ", tb_view v " + "WHERE que.id = v.question_id and v.radio_group <> 0 and que.id = " + Integer.toString(questionId);
		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ViewInfo vi = new ViewInfo();

				vi.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
				vi.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
				vi.setInitialValue(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("initial_value"))));
				vi.setxAxis(cursor.getInt(cursor.getColumnIndex("x_axis")));
				vi.setyAxis(cursor.getInt(cursor.getColumnIndex("y_axis")));
				vi.setAnswer(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("answer"))));
				vi.setUserAnswer("");
				vi.setRadioGroup(cursor.getInt(cursor.getColumnIndex("radio_group")));
				// Log.d("answer",
				// cursor.getString(cursor.getColumnIndex("answer")));
				listView.add(vi);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listView;
	}

	/**
	 * 取得所有父级菜单信息,暂时没有用
	 *
	 * @return 父级菜单
	 */
	public List<TypeInfo> getFatherType() {
		List<TypeInfo> listTypeInfo = new ArrayList<TypeInfo>();

		String sql = "select id " + ",type_name " + "from tb_type " + "where parent_id is null";

		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				TypeInfo ti = new TypeInfo();

				ti.setId(cursor.getInt(cursor.getColumnIndex("id")));
				ti.setTypeName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("type_name"))));

				listTypeInfo.add(ti);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listTypeInfo;
	}

	/**
	 * 按照父级菜单ID获得子级菜单信息
	 * 
	 * @param parentId
	 *            父级菜单ID
	 * @return 父级菜单ID对应的子级菜单
	 */
	public List<TypeInfo> getChildType(int parentId) {
		List<TypeInfo> listTypeInfo = new ArrayList<TypeInfo>();

		String sql = "select id " + ",type_name " + "from tb_type " + "where parent_id = " + Integer.toString(parentId);

		try {

			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				TypeInfo ti = new TypeInfo();

				ti.setId(cursor.getInt(cursor.getColumnIndex("id")));
				ti.setTypeName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("type_name"))));

				listTypeInfo.add(ti);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return listTypeInfo;
	}

	/**
	 * 存储用户填写的答案
	 * 
	 * @param ListVI
	 *            tb_view表的实体类列表
	 */
	public void updateUserAnswer(List<QuestionInfo> list, int location, Context c) {
		List<ViewInfo> LsET = list.get(location).getLsET();
		// List<ViewInfo> LsCB = list.get(location).getLsCB();
		List<UserSignInfo> LsUserSign = list.get(location).getLsUserSign();
		// 改变题目完成状态
		String sql1 = "UPDATE tb_subject_bill " + "SET is_completed = " + list.get(location).getIsCompleted() + " WHERE id = " + list.get(location).getId();
		// Log.e("sql1", "sql1 " + sql1 + "");
		try {
			ContentValues values1 = new ContentValues();
			values1.put("is_completed", list.get(location).getIsCompleted());
			// dbMangager.updaeTest("tb_subject_bill", "id", values1,
			// list.get(location).getId());
			// Log.e("sql1", "sql1----> " + sql1);
			dbMangager.executeSql(sql1);
		} catch (Exception e) {
			Log.e("能不能出事了", e.toString());
			e.printStackTrace();
		}
		// 用户填制信息
		for (int i = 0; i < LsET.size(); i++) {
			String sql2 = "";
			sql2 = "UPDATE tb_view " + "SET user_answer = \"" + LsET.get(i).getUserAnswer() + "\" WHERE id = " + LsET.get(i).getId();
			// Log.e("sql2", "sql2 " + sql2 + "");
			try {

				ContentValues values2 = new ContentValues();
				values2.put("user_answer", LsET.get(i).getUserAnswer());
				// dbMangager.updaeTest("tb_view", "id", values2, LsET.get(i)
				// .getId());
				// Log.e("sql2", "sql2----> " + sql2);
				dbMangager.executeSql(sql2);
			} catch (Exception e) {
				Log.e("Exception", "Exception---->  " + e.toString());
				e.printStackTrace();
			}
		}
		// 用户选择的印章信息
		for (int i = 0; i < LsUserSign.size(); i++) {
			if (!LsUserSign.get(i).isInserted()) {
				String sql4 = "INSERT INTO  tb_user_sign(question_id,user_x_axis,user_y_axis,is_right,user_sign_name) " + "values(" + LsUserSign.get(i).getQuestionID() + ","
						+ LsUserSign.get(i).getUserXAxis() + "," + LsUserSign.get(i).getUserYAxis() + ",\"" + LsUserSign.get(i).isRight() + "\",\"" + LsUserSign.get(i).getUserSignName() + "\")";

				// Log.e("sql4", "sql4----> " + sql4);
				try {

					dbMangager.executeSql(sql4);
					LsUserSign.get(i).setInserted(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 存储用户填写的答案
	 * 
	 * @param ListVI
	 *            tb_view表的实体类列表
	 */
	public void updateUserAnswer(List<QuestionInfo> list, int location, Context c, List<QuestionInfo> list2) {
		List<ViewInfo> LsET = list.get(location).getLsET();
		List<ViewInfo> LsETRan = list2.get(location).getLsET();
		List<UserSignInfo> LsUserSign = list.get(location).getLsUserSign();
		// 改变题目完成状态
		String sql1 = "UPDATE tb_subject_bill " + "SET is_completed = " + list.get(location).getIsCompleted() + " WHERE id = " + list.get(location).getId();
		// Log.e("sql1", "sql1 " + sql1 + "");
		try {
			ContentValues values1 = new ContentValues();
			values1.put("is_completed", list.get(location).getIsCompleted());
			// dbMangager.updaeTest("tb_subject_bill", "id", values1,
			// list.get(location).getId());
			// Log.e("sql1", "sql1----> " + sql1);
			dbMangager.executeSql(sql1);
		} catch (Exception e) {
			Log.e("能不能出事了", e.toString());
			e.printStackTrace();
		}
		for (int p = 0; p < LsETRan.size(); p++) {
			for (int i = 0; i < LsET.size(); i++) {
				if (LsETRan.get(p).getRandomEditable() == 1) {
					if (LsETRan.get(p).getId() == LsET.get(i).getId() && LsETRan.get(p).getIsMutiple() == 0) {
						String sql2 = "UPDATE tb_view " + "SET user_answer = \"" + LsET.get(i).getUserAnswer() + "\",randomFlah = 1 WHERE id = " + LsETRan.get(p).getId();
						// Log.e("sql2", "sql2 " + sql2 + "");
						try {
							dbMangager.executeSql(sql2);
						} catch (Exception e) {
							Log.e("Exception", "Exception---->  " + e.toString());
							e.printStackTrace();
						}
					} else if (LsETRan.get(p).getId() == LsET.get(i).getId() && LsETRan.get(p).getIsMutiple() == 1) {
						for (int po = 0; po < LsETRan.get(p).getMutipleViewList().size(); po++) {
							for (int iu = 0; iu < LsET.size(); iu++) {
								if (LsETRan.get(p).getMutipleViewList().get(po).getId() == LsET.get(iu).getId()) {
									String sql2 = "UPDATE tb_view " + "SET user_answer = \"" + LsET.get(iu).getUserAnswer() + "\",randomFlah = 1 WHERE id = " + LsET.get(iu).getId();
									// Log.e("sql3", "sql3 " + sql2
									// + " rightAnswer--> "
									// + LsET.get(iu).getAnswer());
									try {
										dbMangager.executeSql(sql2);
									} catch (Exception e) {
										Log.e("Exception", "Exception---->  " + e.toString());
										e.printStackTrace();
									}
								}
							}

						}
					}
				}
			}
		}
		// 用户选择的印章信息
		for (int i = 0; i < LsUserSign.size(); i++) {
			if (!LsUserSign.get(i).isInserted()) {
				String sql4 = "INSERT INTO  tb_user_sign(question_id,user_x_axis,user_y_axis,is_right,user_sign_name) " + "values(" + LsUserSign.get(i).getQuestionID() + ","
						+ LsUserSign.get(i).getUserXAxis() + "," + LsUserSign.get(i).getUserYAxis() + ",\"" + LsUserSign.get(i).isRight() + "\",\"" + LsUserSign.get(i).getUserSignName() + "\")";

				try {

					dbMangager.executeSql(sql4);
					LsUserSign.get(i).setInserted(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 取得用的到的印章信息
	 * 
	 * @return 数据库已有的印章信息
	 */
	public List<SignInfo> getAllSigns() {
		List<SignInfo> listSigns = new ArrayList<SignInfo>();
		String sql = "SELECT DISTINCT sign_name,content " + "FROM tb_sign";
		try {
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {});

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				SignInfo signInfo = new SignInfo();
				signInfo.setContent(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("content"))));
				signInfo.setSignName(Utils.stringFormat(cursor.getString(cursor.getColumnIndex("sign_name"))));
				listSigns.add(signInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listSigns;
	}

	/**
	 * 按下“重做”按钮后清空用户答案
	 * 
	 * @param questionID
	 *            题目ID
	 */
	public void emptyUserAnswer(int questionID) {
		String sql1 = "UPDATE tb_subject_bill " + "SET is_completed = 0 " + "WHERE id = " + String.valueOf(questionID);

		String sql2 = "UPDATE tb_view " + "SET user_answer = \"\" " + "WHERE question_id = " + String.valueOf(questionID);

		String sql3 = "DELETE FROM tb_user_sign " + "WHERE question_id = " + String.valueOf(questionID);
		try {
			dbMangager.updateDataBySql(sql1, new String[] {});
			dbMangager.updateDataBySql(sql2, new String[] {});
			dbMangager.updateDataBySql(sql3, new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteUserSigns(int questionID) {
		String sql = "DELETE FROM tb_user_sign WHERE question_id = ?";
		String[] bindArgs = { questionID + "" };
		try {
			dbMangager.deleteDataBySql(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改审核部分用户的答案
	 * 
	 * @param id
	 * @param mode
	 *            修改为对应的模式
	 * @param c
	 */
	public void updateAuditSubject(int id, int mode, Context c) {
		try {
			String sql1 = "UPDATE tb_audit_view " + "SET mode = " + mode + " WHERE  id= " + id;
			Log.d(TAG, "sql:" + sql1);
			dbMangager.executeSql(sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改用户答案
	 * 
	 * @param id
	 * @param answer
	 */
	public void updateUserAnswer(int id, int answer) {
		try {
			String sql1 = "UPDATE tb_audit_view " + "SET user_answer = " + answer + ", flag = 1 WHERE  id= " + id;
			Log.d(TAG, "UserAnswer sql:" + sql1);
			dbMangager.executeSql(sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除数据库里的答题和抽题状态
	 * 
	 * @param quesId
	 *            对应问题的id
	 */
	public void cleanAnswerState(int quesId) {
		try {
			String sql1 = "UPDATE tb_audit_view " + "SET mode = 0 WHERE mode!=2 and question_id=" + quesId;
			Log.d(TAG, "clean sql1:" + sql1);
			dbMangager.executeSql(sql1);
			String sql2 = "UPDATE tb_audit_view " + "SET user_answer = 0 WHERE question_id=" + quesId;
			Log.d(TAG, "clean sql2:" + sql2);
			dbMangager.executeSql(sql2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取抽取的总题数
	 * 
	 * @param quesId
	 *            对应问题的id
	 * @return
	 */
	public int getTotalCount(int quesId) {
		int count = 0;
		try {
			String sql = "select count(mode) count from tb_audit_view where mode=1 and question_id=" + quesId;
			Log.d(TAG, "count sql:" + sql);
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标
			count = cursor.getInt(cursor.getColumnIndex("count")); // question主键
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取答对的总题数
	 * 
	 * @param quesId
	 *            对应问题的id
	 * @return
	 */
	public int getCorrectCount(int quesId) {
		int count = 0;
		try {
			String sql = "select count(user_answer) count from tb_audit_view where user_answer=1 and question_id=" + quesId;
			Log.d(TAG, "count sql:" + sql);
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标
			count = cursor.getInt(cursor.getColumnIndex("count")); // question主键
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取需要加积分的个数
	 * 
	 * @param quesId
	 *            对应问题的id
	 * @return
	 */
	public int getFlagCount(int quesId) {
		int count = 0;
		try {
			String sql = "select count(flag) count from tb_audit_view where user_answer=1 and mode=1 and flag=1 and question_id=" + quesId;
			Log.d(TAG, "count sql:" + sql);
			Cursor cursor = dbMangager.queryData2Cursor(sql, new String[] {}); // 打开游标
			count = cursor.getInt(cursor.getColumnIndex("count")); // question主键
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 修改flag的值
	 * 
	 * @param flag
	 * @param quesId
	 *            对应问题的id
	 */
	public void updateFlag(int flag, int quesId) {
		try {
			String sql1 = "UPDATE tb_audit_view " + "SET flag = " + flag + " WHERE user_answer=1 and mode=1 and flag=1 and question_id=" + quesId;
			Log.d(TAG, "UserAnswer sql:" + sql1);
			dbMangager.executeSql(sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
