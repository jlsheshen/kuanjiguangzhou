package com.edu.basicaccountingforguangzhou.subject.bill.view;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.SubjectState;
import com.edu.basicaccountingforguangzhou.subject.TestMode;
import com.edu.basicaccountingforguangzhou.subject.bill.element.ElementLayoutParams;
import com.edu.basicaccountingforguangzhou.subject.bill.element.ElementType;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.scale.IScaleable;
import com.edu.basicaccountingforguangzhou.subject.bill.scale.ScaleUtil;

/**
 * 单据里对应空的封装,支持缩放
 * 
 * @author lucher
 * 
 */
public class BlankEditText extends EditText implements IScaleable {

	// 默认字体大小
	private static int DEFAULT_TEXT_SIZE = 16;

	// 对应空的信息
	private BlankInfo mData;
	// 缩放比重
	private float mScaleWeight;

	// 是否初始化
	private boolean init;
	// 当前测试模式
	private int mTestMode;
	// 答题状态
	private int mState;

	public BlankEditText(Context context, int testMode, int state) {
		super(context);
		mTestMode = testMode;
		mState = state;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		setPadding(1, 0, 1, 0);
		setGravity(Gravity.CENTER_VERTICAL);
		setSingleLine();
		setBackgroundColor(0x55000000);
		setTextColor(R.drawable.blank_edit_text_color_black);
		setImeOptions(EditorInfo.IME_ACTION_NEXT);
	}

	/**
	 * 根据空的不同类型初始化空的属性
	 */
	private void initBlank() {
		if (init)
			return;
		setInputType();
		// 判断是否需要用户填写
		if (mData.isEditable()) {
			setEnabled(true);
		} else {
			setEnabled(false);
			setText(mData.getAnswer());
		}
		// 状态初始化
		if (mState == SubjectState.STATE_FINISHED) {
			if (mTestMode == TestMode.MODE_PRACTICE || mTestMode == TestMode.MODE_SHOW_DETAILS) {
				judgeAnswer();
				showAnswer(true);
				refreshState();
			} else if (mTestMode == TestMode.MODE_EXAM) {
				showAnswer(true);
			}
		}

		init = !init;
	}

	/**
	 * 设置输入方式
	 */
	private void setInputType() {
		InputFilter[] filters;
		switch (mData.getType()) {
		case ElementType.TYPE_DATE_LOWER:
			setInputType(InputType.TYPE_CLASS_NUMBER);

			break;
		case ElementType.TYPE_AMOUNT_LOWER:
			setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

			break;
		case ElementType.TYPE_AMOUNT_LOWER_SEP:
			filters = new InputFilter[] { new LengthFilter(1) };
			setFilters(filters); // 最大输入1个字符。

			break;

		case ElementType.TYPE_VERTICAL:
			setSingleLine(false);

			break;

		default:
			break;
		}
	}

	@Override
	public void setTextSize(float size) {
		super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);// 为了方便字体大小的自适应，采用px方式设置字体大小
		setText(getText());// 如果不调用，设置字体大小后，内容可能不会居中
		int selection = getText().toString().length();
		setSelection(selection);// 设置把光标定位到最后
	}

	/**
	 * 设置对应空数据，并把数据应用到该空上
	 * 
	 * @param data
	 *            数据
	 * @param textSize
	 *            字体大小
	 * @param scale
	 *            缩放比例
	 * @param scaleWeight
	 *            缩放比重
	 */
	public void apply(BlankInfo data, float scale, float scaleWeight) {
		mData = data;
		initBlank();

		if (data.getTextSize() <= 0) {
			data.setTextSize(DEFAULT_TEXT_SIZE);
		}

		mScaleWeight = scaleWeight;
		postScale(scale, 0);
	}

	/**
	 * 获取对应空数据
	 * 
	 * @return
	 */
	public BlankInfo getData() {
		return mData;
	}

	@Override
	public void postScale(float scale, int scaleTimes) {
		// 布局参数缩放
		int scaledX = ScaleUtil.getScaledValue(mData.getX(), scale);
		int scaledY = ScaleUtil.getScaledValue(mData.getY(), scale);
		int scaledWidth = ScaleUtil.getScaledValue(mData.getWidth(), scale);
		int scaledHeight = ScaleUtil.getScaledValue(mData.getHeight(), scale);
		setLayoutParams(new ElementLayoutParams(mData.getType(), scaledX, scaledY, scaledWidth, scaledHeight));
		// 字体缩放
		float textSize = mData.getTextSize() * (float) Math.pow(mScaleWeight, (scaleTimes + 1));
		setTextSize(textSize);

		// 宽高缩放-如果不缩放，可能引起内容显示不全
		setWidth(scaledWidth);
		setHeight(scaledHeight);
		Log.d("lucher", "scaledHeight:" + scaledHeight);
	}

	/**
	 * 提交
	 */
	public void submit() {
		saveAnswer();
		judgeAnswer();
		showAnswer(true);
		refreshState();
	}

	/**
	 * 保存用户答案
	 */
	public void saveAnswer() {
		mData.setuAnswer(getText().toString().trim());
	}

	/**
	 * 判断答案正确与否
	 */
	public void judgeAnswer() {
		if (mData.getuAnswer() != null) {
			switch (mData.getType()) {
			case ElementType.TYPE_KEY_WORD:// 关键字匹配
				if (mData.getuAnswer().trim().contains(mData.getAnswer().trim())) {
					mData.setRight(true);
				}
				break;

			case ElementType.TYPE_MULTI_ANSWER:// 多个答案匹配
				String[] answers = mData.getAnswer().trim().split(SubjectConstant.SEPARATOR_MULTI_ANSWER);
				if (Arrays.asList(answers).contains(mData.getuAnswer().trim())) {
					mData.setRight(true);
				}

				break;

			default:// 答案对比匹配
				if (mData.getAnswer().trim().equals(mData.getuAnswer().trim())) {
					mData.setRight(true);
				}
				break;
			}
		}
	}

	/**
	 * 显示答案
	 * 
	 * @param user
	 *            true-显示用户答案，false-显示正确答案
	 */
	public void showAnswer(boolean user) {
		if (user) {
			setText(mData.getuAnswer());
			refreshState();
		} else {
			setText(mData.getAnswer());
			setBackgroundColor(0x55000000);
		}
	}

	/**
	 * 刷新该空的作答状态
	 */
	public void refreshState() {
		if (mTestMode == TestMode.MODE_PRACTICE) {// 练习模式
			setEnabled(false);
			if (mData.isRight()) {
				setBackgroundColor(0x990066FF);
			} else {
				setBackgroundColor(0x88ff0000);
			}
		} else if (mTestMode == TestMode.MODE_SHOW_DETAILS) {// 显示详情模式
			setEnabled(false);
			if (mData.isRight()) {
				setBackgroundColor(0x990066FF);
			} else {
				setBackgroundColor(0x88ff0000);
			}
		} else if (mTestMode == TestMode.MODE_EXAM) {// 考试模式
		}
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			clearFocus();// 解决readme中所说的输入法软件盘可能会遮挡的问题
		}
		return super.onKeyPreIme(keyCode, event);
	}

	@Override
	public String toString() {
		return "BlankEditText：" + mData;
	}

	/**
	 * 金额格式filter
	 * 
	 * @author lucher
	 * 
	 */
	public class CashierInputFilter implements InputFilter {
		Pattern mPattern;

		// 输入的最大金额
		private static final int MAX_VALUE = Integer.MAX_VALUE;
		// 小数点后的位数
		private static final int POINTER_LENGTH = 2;

		private static final String POINTER = ".";

		private static final String ZERO = "0";

		public CashierInputFilter() {
			mPattern = Pattern.compile("([0-9]|\\.)*");
		}

		/**
		 * @param source
		 *            新输入的字符串
		 * @param start
		 *            新输入的字符串起始下标，一般为0
		 * @param end
		 *            新输入的字符串终点下标，一般为source长度-1
		 * @param dest
		 *            输入之前文本框内容
		 * @param dstart
		 *            原内容起始坐标，一般为0
		 * @param dend
		 *            原内容终点坐标，一般为dest长度-1
		 * @return 输入内容
		 */
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			String sourceText = source.toString();
			String destText = dest.toString();

			// 验证删除等按键
			if (TextUtils.isEmpty(sourceText)) {
				return "";
			}

			Matcher matcher = mPattern.matcher(source);
			// 已经输入小数点的情况下，只能输入数字
			if (destText.contains(POINTER)) {
				if (!matcher.matches()) {
					return "";
				} else {
					if (POINTER.equals(source)) { // 只能输入一个小数点
						return "";
					}
				}

				// 验证小数点精度，保证小数点后只能输入两位
				int index = destText.indexOf(POINTER);
				int length = dend - index;

				if (length > POINTER_LENGTH) {
					return dest.subSequence(dstart, dend);
				}
			} else {
				// 没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
				if (!matcher.matches()) {
					return "";
				} else {
					if ((POINTER.equals(source) || ZERO.equals(source)) && TextUtils.isEmpty(destText)) {
						return "";
					}
				}
			}

			// 验证输入金额的大小
			double sumText = Double.parseDouble(destText + sourceText);
			if (sumText > MAX_VALUE) {
				return dest.subSequence(dstart, dend);
			}

			return dest.subSequence(dstart, dend) + sourceText;
		}
	}
}
