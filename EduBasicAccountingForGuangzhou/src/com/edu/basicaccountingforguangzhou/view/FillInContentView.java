package com.edu.basicaccountingforguangzhou.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.common.MyOnFocusChangeListener;
import com.edu.basicaccountingforguangzhou.data.EduSqliteDbOprater;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.dialog.CustomerDialog;
import com.edu.basicaccountingforguangzhou.dialog.FillInPictureDialog;
import com.edu.basicaccountingforguangzhou.dialog.PictureBrowseDialog;
import com.edu.basicaccountingforguangzhou.dialog.SignChooseDialog;
import com.edu.basicaccountingforguangzhou.dialog.SignChooseDialog.OnButtonClickListener;
import com.edu.basicaccountingforguangzhou.info.QuestionInfo;
import com.edu.basicaccountingforguangzhou.info.SignInfo;
import com.edu.basicaccountingforguangzhou.info.UserSignInfo;
import com.edu.basicaccountingforguangzhou.model.SubjectBillDataModel;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.testbill.data.SubjectTestDataDao;
import com.edu.basicaccountingforguangzhou.util.PreferenceHelper;
import com.edu.basicaccountingforguangzhou.util.Utils;
import com.edu.ime.CustomerInputView;
import com.edu.ime.ViewInfo;
import com.edu.library.util.RandomUtil;
import com.edu.library.util.ToastUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 单据答题界面
 * 
 * @author lucher
 * 
 */
public class FillInContentView extends RelativeLayout implements OnClickListener, OnKeyboardFocusChangeListener {

	public static final String IS_FIRST_SIGN = "signFirst";

	// 标题
	private TextView tvTitle;
	private Button btnSign; // 按钮：印章
	private Button btnDone; // 按钮：完成
	private RelativeLayout layout;// 标题

	// 移植view
	private EduSqliteDbOprater eso; // 数据库操作类
	private AbsoluteLayout inputViewHolder;
	private CustomerInputView inputView;

	private List<QuestionInfo> listQuestionInfo = new ArrayList<QuestionInfo>();// 问题列表（问题，答案，控件位置，印章信息）
	private List<SignInfo> listSigns = new ArrayList<SignInfo>(); // 所有印章信息
	private SignChooseDialog dialog;
	private Vector<LinearLayout> listAnswerSign = new Vector<LinearLayout>(); // 印章答案（显示在界面上的）
	private Vector<LinearLayout> listUserSign = new Vector<LinearLayout>(); // 印章用户答案（显示在界面上的）
	private List<EditText> listET = new ArrayList<EditText>(); // 所有动态生成的View都存在这里面

	// 复制
	private FrameLayout flFuck;
	private int nowLoc; // 现在是第几题
	private int[] postion = new int[2]; // 题目内容坐标
	private CustomerDialog tempDialog;

	private AbsoluteLayout flat; // 所有动态生成控件都画在这上面
	private LinearLayout background; // 底图
	private LinearLayout quesBar; // 业务事项描述框

	private ScrollView scrollHolder;

	// private int typeId;
	// private int billId;
	// private int initFlag;// 值为2或3为查看答案和用户答案对比模式
	// private boolean showDetails;// 是否为查看详细
	private boolean showResult;// 是否显示结果界面
	private int state;// 0-未做，1-正确，2-错误,3-未完成

	private RelativeLayout rlTitle;
	private boolean threadFlag;
	private long recordMill;
// 用户答案,正确答案
	private Button btnShowUser, btnShowRight;
	private LinearLayout rlSwitchHolder;

	private LinearLayout lyHolder;

	private View lastUnfocusView;

	private boolean initFocusFlag = true;

	// 指示图片
	private RelativeLayout rlIndicator;
	// 第一次使用的提示动画
	private AnimatorSet indicatorAnim = new AnimatorSet();

	private Context mContext;

	// 对应记录的id
	private int testId;
	private String titleIndex;

	private TestData mTestData;
	// 对应数据
	private SubjectBillData mData;
	//试卷状态

	// 所得分数
	private float mUscore;

	// // 结果相关控件
	// private View resultLayout;
	// private Button btnShowDetails, btnSendScore;
	// private TextView tvScore;

	private LayoutInflater inflater;
	private boolean inited;
	private ImageButton btnOriginal;
	private FillInPictureDialog picDialog;
	private int mTestMode;// 试卷状态

	private static final String[] bigChineseNum = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾" };

	/**
	 * 获取要显示的
	 */
	private int getOnTouchIndex;

	// 图片浏览对话框
	private PictureBrowseDialog picBrowseDialog;

	public FillInContentView(Context context, TestData data, int testMode) {
		super(context);
		mContext = context;

		inflater = LayoutInflater.from(mContext);
		mTestData = data;
		mData = (SubjectBillData) data.getData();
		this.testId = data.getId();
		// this.typeId = mData.getBillTypeId();
		// this.billId = mData.getBillId();
		this.titleIndex = mData.getIndexName();
		this.showResult = false;
		this.state = data.getState();
		this.mTestMode = testMode;
		inflater.inflate(R.layout.loading_layout, this);

		initData();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		if (!inited) {
			removeAllViews();

			inflater.inflate(R.layout.layout_fillin_view, this);
			// initResult();
			// if (!showResult) {
			init();
			initInputView();
			refreshState();
		}
		// }
		inited = true;

	}

	/**
	 * 初始化结果相关控件
	 */
	private void initResult() {
		// resultLayout = findViewById(R.id.layout_result);
		// btnShowDetails = (Button) findViewById(R.id.btn_show_details);
		// btnSendScore = (Button) findViewById(R.id.btn_send_score);
		// tvScore = (TextView) findViewById(R.id.tv_socre);
		// btnShowDetails.setOnClickListener(this);
		// btnSendScore.setOnClickListener(this);
		// btnSendScore.setVisibility(View.GONE);
		// if (showResult) {
		// resultLayout.setVisibility(View.VISIBLE);
		// mUscore = mData.getuScore();
		// if (mUscore == 10) {
		// tvScore.setText("10");
		// } else {
		// tvScore.setText(DecimalFormatUtil.formatFloat(mUscore, "0.0"));
		// }
		// if (state == 1 || state == 2) {// 已完成
		// if (mTestData.getSendState() == 0) {// 已完成，但未发送
		// setSendBtnVisBilit();
		// } else {// 完成也已发送
		// if (Constant.TEST_TYPE_EXERCIZE_OR_SWEETOWN == 2) {
		// btnSendScore.setVisibility(View.GONE);
		// } else {
		// // btnSendScore.setVisibility(View.VISIBLE);
		// }
		// }
		// }
		// } else {
		// resultLayout.setVisibility(View.GONE);
		// }

	}

	/**
	 * 初始化inputview
	 */
	private void initInputView() {
		inputView = new CustomerInputView(inputViewHolder, mContext, listET);
		inputView.setDragable(true);
		// if (state == 1 || state == 2) {
		// listET.get(getOnTouchIndex).requestFocus();
		// inputView.setInputtypeNull(listET.get(getOnTouchIndex));
		// }
		// if (initFlag == 2 || initFlag == 3) {
		// inputView.hideCustomKeyBorad();
		// }
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		// 清除数据
		listQuestionInfo.clear();
		listSigns.clear();
		listAnswerSign.clear();
		listAnswerSign.clear();
		listUserSign.clear();
		listET.clear();

		eso = new EduSqliteDbOprater(mContext);
		listSigns = eso.getAllSigns();

		// 取数据
		listQuestionInfo.add(eso.getQuestionInfoByTypeIdForTest(1, mData.getId()));
		nowLoc = 0;
	}

	/**
	 * 初始化
	 */
	private void init() {
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		tvTitle.setText("错误" + mTestData.getErrorCount() + "次");
		quesBar = (LinearLayout) findViewById(R.id.ll_quesbar);
		background = new LinearLayout(mContext);
		background.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		inputViewHolder = (AbsoluteLayout) findViewById(R.id.input_view);
		lyHolder = (LinearLayout) findViewById(R.id.ly_cs_holder);
		flat = new AbsoluteLayout(mContext);
		flat.setLayoutParams(new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0));
		flFuck = (FrameLayout) findViewById(R.id.fg_fuck);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
		btnSign = (Button) findViewById(R.id.btn_seal);
		btnDone = (Button) findViewById(R.id.btn_accomplish);
		layout = (RelativeLayout) findViewById(R.id.rl_title);
		rlSwitchHolder = (LinearLayout) findViewById(R.id.ly_switch);
		scrollHolder = (ScrollView) findViewById(R.id.sc_holder);
		scrollHolder.setOnTouchListener(new TouchListenerImpl());
		btnShowUser = (Button) findViewById(R.id.btn_show_user);
		btnShowUser.setOnClickListener(this);
		btnShowUser.setHovered(true);
		btnShowRight = (Button) findViewById(R.id.btn_show_right);
		btnShowRight.setOnClickListener(this);
		// btnShowCard.setOnClickListener(this);
		// btnHideCard.setOnClickListener(this);
		threadFlag = true;
		tempDialog = new CustomerDialog(mContext, R.layout.dialog_signs);

		rlIndicator = (RelativeLayout) this.findViewById(R.id.rl_audit_indicator);
		rlIndicator.setOnClickListener(this);
		btnOriginal = (ImageButton) this.findViewById(R.id.btn_original);
		btnOriginal.setOnClickListener(this);

		// 印章监听，按下时显示印章列表
		btnSign.setOnClickListener(this);
		// btnBack.setOnClickListener(this);
		// 完成\重做\提交按钮
		btnDone.setOnClickListener(this);

		// 初始化动态添加控件（EditText）
		generatView(nowLoc);
		emptyPage(nowLoc);
		String bsName = listQuestionInfo.get(nowLoc).getBsName(); // 底图名称
		// Log.e("bsName", "bsName---->" + bsName);
		changeBackground(bsName); // 更换底图
		// showPicture(listQuestionInfo.get(nowLoc).getPicName());
		picBrowseDialog = new PictureBrowseDialog(mContext);
		if (listQuestionInfo.get(nowLoc).getContent() != null) {
			// 标题
			// tvTitle.setText(titleIndex + "." +
			// ToDBC(listQuestionInfo.get(nowLoc).getContent()));
		}
	}

	// 去除TextView设置文本自带排版样式的方法
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 显示图片
	 * 
	 * @param picName
	 */
	private void showPicture(String picName) {
		List<String> picList = new ArrayList<String>();
		if (picName != null && !picName.equals("")) {
			String[] picNames = picName.split("&");
			for (int i = 0; i < picNames.length; i++) {
				picList.add(picNames[i]);
			}
			if (picList != null && picList.size() > 0) {
				picDialog = new FillInPictureDialog(mContext, picList);
				picDialog.show();
			}
			// } else {
			// btnOriginal.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 显示浏览图片对话框
	 */
	private void browsePics() {
		if (!picBrowseDialog.isShowing()) {
			// String pic =
			// mSubjectAdapter.getData(mCurrentIndex).getSubjectData().getPic();
			String pic = listQuestionInfo.get(nowLoc).getPicName();
			String[] pics = pic.split("<<<");
			picBrowseDialog.setPics(pics);
			picBrowseDialog.show();
		}
	}

	/**
	 * 刷新状态
	 */
	private void refreshState() {
		// if (state == 1 || state == 2) {
		// rlSwitchHolder.setVisibility(View.VISIBLE);
		// btnSign.setVisibility(View.INVISIBLE);
		// btnDone.setVisibility(View.INVISIBLE);
		// showAnswer(0, 1);
		// } else if (state == 3) {
		// showHistoryAnswer(nowLoc);
		// }
		if (mTestMode != BaseScrollView.TEST_MODE_NORMAL) {
			rlSwitchHolder.setVisibility(View.VISIBLE);
			btnSign.setVisibility(View.INVISIBLE);
			btnDone.setVisibility(View.INVISIBLE);
			tvTitle.setVisibility(View.VISIBLE);

			showAnswer(0, 1);
		} else {
			showHistoryAnswer(nowLoc);
			tvTitle.setVisibility(View.GONE);

		}
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				rlTitle.setVisibility(View.GONE);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private class TouchListenerImpl implements OnTouchListener {
		private float oldP;
		private float newP;

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				oldP = motionEvent.getRawY();
				inputView.dismissKeyBoard();
				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				newP = motionEvent.getRawY();
				int scrollViewMeasuredHeight = scrollHolder.getChildAt(0).getMeasuredHeight();
				if (scrollY == 0 && (newP - oldP) > 10) {
					// System.out.println("滑动到了顶部 scrollY=" + scrollY);
					rlTitle.setVisibility(View.VISIBLE);
					recordMill = System.currentTimeMillis();
					// new AutoHide().start();
				} else if ((scrollY + height) == scrollViewMeasuredHeight) {
					// System.out.println("滑动到了底部 scrollY=" + scrollY);
					// System.out.println("滑动到了底部 height=" + height);
					// System.out.println("滑动到了底部 scrollViewMeasuredHeight="
					// + scrollViewMeasuredHeight);
				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	/**
	 * 动态生成EditText和CheckView
	 * 
	 * @param list
	 *            list，用于存放EditText
	 * @param count
	 *            需要使用到的EditText的数量
	 * @return
	 */
	private void generatView(final int location) {

		int etCount = listQuestionInfo.get(location).getLsET().size();
		int listETSize = listET.size();

		// Log.e("etSize", location + " --- " + etCount + " " + cbCount
		// + " " + listETSize + " " + listCBSize);

		// 生成EditText
		if (listETSize < etCount) {

			int loop1 = etCount - listETSize;
			int getFocus = 0;
			for (int i = 0; i < loop1; i++) { // 如果已有控件不足，增加
				final int ii = i;
				final EditText et = new EditText(mContext);
				ColorStateList color = getResources().getColorStateList(R.drawable.et_selector_color_bg);
				et.setTextColor(color);
				et.setPadding(0, 0, 0, 0);
				if (isForMutipleLineEt(listQuestionInfo.get(location).getLsET().get(i).getInputTyep())) {
					et.setSingleLine();
					et.setTextSize(14);
				}
				et.setTag(listQuestionInfo.get(location).getLsET().get(i));
				// 添加数字自动下移的金额控件 flag= 6
				if (isForSingleMoneyEditText(listQuestionInfo.get(location).getLsET().get(i).getInputTyep())) {
					// InputFilter[] filters = { new InputFilter.LengthFilter(1)
					// };
					// et.setFilters(filters);
					et.setGravity(Gravity.CENTER);
					et.setTextSize(18);
					et.setInputType(InputType.TYPE_CLASS_NUMBER);
					TextWatcher txtW = new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
							if (inputView != null && !("".equals(String.valueOf(s))) && s.length() == 1) {
								if (listQuestionInfo.get(location).getLsET().size() > ii + 1 && !isForSingleMoneyEditText(listQuestionInfo.get(location).getLsET().get(ii + 1).getInputTyep())) {
									inputView.setNextEditTextFocused(true);
								} else {
									inputView.setNextEditTextFocused(false);
								}
								// Utils.showToast("111", FillInContent.this);
							} else if (s.length() > 1) {
								et.setText("");
								Editable editable = et.getText();
								editable.append(s.subSequence(s.length() - 1, s.length()));

							}
						}
					};
					listQuestionInfo.get(location).getLsET().get(i).setTw(txtW);
					et.addTextChangedListener(txtW);
				}
				// flag= 6結束
				if (isForGroupEditText(listQuestionInfo.get(location).getLsET().get(i).getInputTyep())) {
					et.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					if (isForBigLetterDateEditText(listQuestionInfo.get(location).getLsET().get(i).getInputTyep())) {
						// if (getFocus == 0) {
						// et.setTextSize(14);
						// getOnTouchIndex = i;
						// // et.requestFocus();
						// // et.requestFocusFromTouch();
						// getFocus++;
						// } else {
						et.setTextSize(14);
						// }
					} else {
						et.setTextSize(14);
					}
					et.setTypeface(Typeface.SERIF);
				}

				et.setBackgroundColor(Color.argb(0, 0, 0, 0));
				et.setOnFocusChangeListener(new MyOnFocusChangeListener());
				if (i == (loop1 - 1)) {
					et.setImeOptions(EditorInfo.IME_ACTION_DONE);
				} else if (i < (loop1 - 1)) {
					et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				}
				if (0 == listQuestionInfo.get(location).getLsET().get(i).getEditable()) {
					et.setGravity(Gravity.CENTER_VERTICAL);
					et.setEnabled(false);
					et.setTextColor(0xff232323);
				}
				if (listQuestionInfo.get(location).getLsET().get(i).getInputTyep() == 1) {
					et.setTextSize(18);
				}
				if (listQuestionInfo.get(location).getLsET().get(i).getInputTyep() == 7) {
					et.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
					et.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					et.setTextSize(22);
					et.setTextScaleX((float) 1.45);
				}
				if (listQuestionInfo.get(location).getLsET().get(i).getInputTyep() == 8) {
					et.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
					et.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					et.setTextSize(22);
					et.setTextScaleX((float) 2.5);
				}
				if (listQuestionInfo.get(location).getLsET().get(i).getInputTyep() == 3) {
					et.setTextSize(18);
				}

				flat.addView(et);
				listET.add(et);

				/********* 优化重组部分代码 **********/
				// 清除答案
				if (0 == listQuestionInfo.get(location).getIsCompleted()) {
					// EditText置空
					listET.get(i).setVisibility(View.VISIBLE);
					if (listQuestionInfo.get(location).getLsET().get(i).getEditable() == 0) {
						listET.get(i).setText(listQuestionInfo.get(location).getLsET().get(i).getInitialValue());
						listET.get(i).setEnabled(false);
					} else {
						listET.get(i).setText("");
					}
					// listET.get(i).setFocusable(false);
					// listET.get(i).setFocusableInTouchMode(false);
					listET.get(i).setBackgroundColor(Color.argb(0, 0, 0, 0));
				} else if (1 == listQuestionInfo.get(location).getIsCompleted() || 2 == listQuestionInfo.get(location).getIsCompleted()) {

				}

				// 设置控件坐标
				List<ViewInfo> lsET = listQuestionInfo.get(location).getLsET();
				if (lsET.size() > 0) {
					int left = lsET.get(i).getxAxis();
					int top = lsET.get(i).getyAxis();
					int width = lsET.get(i).getWidth();
					int Height = lsET.get(i).getHeight();
					android.widget.AbsoluteLayout.LayoutParams params = new android.widget.AbsoluteLayout.LayoutParams(width, Height, left, top);
					listET.get(i).setLayoutParams(params);
					// listET.get(i).setText(lsET.get(i).getAnswer());
					listET.get(i).requestLayout();
				}

				// 表单中不需要挖成空填写默认值的操作
				if (listQuestionInfo.get(nowLoc).getLsET().get(i).getEditable() == 0) {
					listET.get(i).setText(listQuestionInfo.get(nowLoc).getLsET().get(i).getInitialValue());
					listET.get(i).setEnabled(false);
				}
			}

		} else if (listETSize > etCount) { // 如果已有控件过多，隐藏
			for (int i = 0; i < etCount; i++) { // 将需要数量的控件设置为显示
				if (i == (etCount - 1)) {
					listET.get(i).setImeOptions(EditorInfo.IME_ACTION_DONE);
				} else if (i < (etCount - 1)) {
					listET.get(i).setImeOptions(EditorInfo.IME_ACTION_NEXT);
				}
			}

			int loop2 = listETSize - etCount;

			for (int i = 0; i < loop2; i++) {
				listET.get(etCount + i).setVisibility(View.GONE);
			}

		}
		background.addView(flat);
		lyHolder.addView(background);
	}

	/**
	 * 一大波填空类型判断方法开始
	 * 
	 * @author lushichao
	 * 
	 */

	private boolean isForMutipleLineEt(int type) {
		if (type != 4)
			return true;
		else
			return false;
	}

	private boolean isForSingleMoneyEditText(int type) {
		if (type == 6)
			return true;
		else
			return false;
	}

	private boolean isForGroupEditText(int type) {
		if (type > 100)
			return true;
		else
			return false;
	}

	private boolean isForBigLetterDateEditText(int type) {
		if (type > 100 && type < 200)
			return true;
		else
			return false;
	}

	private boolean isForBigLetterDateEditText2(int type) {
		if (type / 100 == 1)
			return true;
		else
			return false;
	}

	private boolean isForSmallLetterDateEditText2(int type) {
		if (type / 100 == 2)
			return true;
		else
			return false;
	}

	/**
	 * 一大波填空类型判断方法结束
	 * 
	 * @author lushichao
	 * 
	 */

	class AutoHide extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((System.currentTimeMillis() - recordMill) > 4000) {
				if (threadFlag) {
					myHandler.sendEmptyMessage(0);
				}
			}
		}

	}

	/**
	 * 显示答案历史记录
	 * 
	 * @param location
	 */
	private void showHistoryAnswer(int location) {
		List<UserSignInfo> list3 = listQuestionInfo.get(location).getLsUserSign();
		for (int i1 = 0; i1 < listET.size(); i1++) {
			if (((ViewInfo) listET.get(i1).getTag()).getEditable() == 1) {
				listET.get(i1).setEnabled(true);
				listET.get(i1).setTextColor(0xff333333);
				if (isForSingleMoneyEditText(((ViewInfo) listET.get(i1).getTag()).getInputTyep())) {
					listET.get(i1).removeTextChangedListener(((ViewInfo) listET.get(i1).getTag()).getTw());
					listET.get(i1).setText(((ViewInfo) listET.get(i1).getTag()).getUserAnswer());
					listET.get(i1).setFocusable(true);
					listET.get(i1).addTextChangedListener(((ViewInfo) listET.get(i1).getTag()).getTw());
				} else {
					listET.get(i1).setText(((ViewInfo) listET.get(i1).getTag()).getUserAnswer());
					listET.get(i1).setFocusable(true);
				}
				// 如果不随机抽题要添加外框在这里
				// listET.get(i1).setBackgroundColor(0x55000000);
			} else {
				if (isForSingleMoneyEditText(((ViewInfo) listET.get(i1).getTag()).getInputTyep())) {
					listET.get(i1).removeTextChangedListener(((ViewInfo) listET.get(i1).getTag()).getTw());
					listET.get(i1).setText(((ViewInfo) listET.get(i1).getTag()).getInitialValue());
					listET.get(i1).setFocusable(false);
					listET.get(i1).addTextChangedListener(((ViewInfo) listET.get(i1).getTag()).getTw());
				} else {
					listET.get(i1).setText(((ViewInfo) listET.get(i1).getTag()).getInitialValue());
					listET.get(i1).setFocusable(false);
				}
			}
		}

		// 印章答案
		if (list3 != null) {
			for (int i = 0; i < list3.size(); i++) {
				String str = list3.get(i).getUserSignName();
				int position = str.indexOf(".");
				str = str.substring(0, position);
				str = "sign_" + str + "_big.png";
				int left = list3.get(i).getUserXAxis();
				int top = list3.get(i).getUserYAxis();
				list3.get(i).setSign(generateImageButton(str, left, top, false));

			}
		}

		// setRandomStyle(location);
	}

	private LinearLayout generateImageButton(String imgName, int left, int top, boolean isRightAnswer) {
		int leftOffset = 0, topOffset = 0;
		LinearLayout ll = new MyLinearLayout(mContext);
		ImageButton ib = new ImageButton(mContext);
		ll.addView(ib);
		flFuck.addView(ll);
		int width = 0;
		int height = 0;
		try {
			ll.getChildAt(0).setBackgroundDrawable(Drawable.createFromStream(mContext.getAssets().open("img/" + imgName), imgName));

			Bitmap rawBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("img/" + imgName));
			width = rawBitmap.getWidth();
			height = rawBitmap.getHeight();
			leftOffset = width / 2;
			topOffset = height / 2;

		} catch (IOException e) {
			e.printStackTrace();
		}

		android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(width, height);
		if (isRightAnswer) {
			params.setMargins(left, top, 0, 0);
		} else {
			if (left != 0) {
				left = left - width;
			}
			params.setMargins(left + leftOffset, top - topOffset, 0, 0);
		}
		ll.setLayoutParams(params);

		return ll;
	}

	/**
	 * 清空需要数量的EditText的内容，remove已经显示的印章图片，清空checkBox
	 * 
	 * @param list
	 *            list，用于存放EditText
	 * @return
	 */
	@SuppressLint("NewApi")
	private void emptyPage(int location) {
		quesBar.setX(0);
		quesBar.setY(0);
		// 用户印章信息重置
		// signImg.clear();
		List<UserSignInfo> listUSI = listQuestionInfo.get(location).getLsUserSign();
		for (int i = 0; i < listUSI.size(); i++) {
			if (listUSI.get(i).getSign() != null) {
				flFuck.removeView(listUSI.get(i).getSign());
			}
		}

		// 答案印章信息重置
		for (int i = 0; i < listAnswerSign.size(); i++) {
			if (listAnswerSign.get(i) != null) {
				flFuck.removeView(listAnswerSign.get(i));
			}
		}
		listAnswerSign.clear();
	}

	class MyLinearLayout extends LinearLayout {

		public MyLinearLayout(Context context) {
			super(context);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			return true;
		}

	}

	/**
	 * 更换底图
	 * 
	 * @param bsName
	 *            底图图片名称
	 */
	private void changeBackground(String bsName) {
		try {
			background.setBackgroundDrawable(Drawable.createFromStream(mContext.getAssets().open("img/" + bsName), bsName));// 替换底图
			Bitmap rawBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("img/" + bsName));
			int width = rawBitmap.getWidth();
			int marginLeft = (1024 - width) / 2;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.setMargins(marginLeft, 0, 0, 0);
			// flat.setLayoutParams(lp);
			background.setLayoutParams(lp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void emptySigns(int questionID, int location) {
		listQuestionInfo.get(location).getLsUserSign().clear();
		eso.deleteUserSigns(questionID);
	}

	class QuesBarOnTouchListener implements OnTouchListener {

		private int screenWidth;
		private int screenHeight;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			// Log.e("llquesbar", "");
			DisplayMetrics dm = getResources().getDisplayMetrics();
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;

			int x = (int) event.getRawX();
			int y = (int) event.getRawY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				postion[0] = x - quesBar.getLeft();
				postion[1] = y - quesBar.getTop();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (x > screenWidth - 30 || x < 30 || y < 70 || y > screenHeight - 30) {
					return true;
				}

				android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(quesBar.getWidth(), quesBar.getHeight());

				params.setMargins(x - postion[0], y - postion[1], 0, 0);
				// tikaTemp.setVisibility(View.GONE);

				quesBar.setLayoutParams(params);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			}
			return true;
		}

	}

	private void sign() {

		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final int inPos = position;
				// if
				// (PreferenceHelper.getInstance(FillInContent.this).isFirstTime(IS_FIRST_SIGN))
				// {// 如果第一次使用，显示引导界面
				// showIndicator();
				// GifView gifSign = (GifView) findViewById(R.id.gif_sign);
				// gifSign.init(R.drawable.gif_fillin_indicator, true);
				// flFuck.setOnTouchListener(new OnTouchListener() {
				//
				// @Override
				// public boolean onTouch(View v, MotionEvent event) {
				// float x = event.getX();
				// float y = event.getY();
				//
				// String str = listSigns.get(inPos).getSignName();
				// if (isSignUsed(str)) {
				// Utils.showToast("同类印章只能加盖一次", getApplicationContext());
				// flFuck.setOnTouchListener(null);
				// } else {
				//
				// UserSignInfo usi = new UserSignInfo();
				// usi.setUserSignName(str);
				// usi.setRight(isSignRight(str));
				// int location = str.indexOf(".");
				// str = str.substring(0, location);
				// str = "sign_" + str + "_big.png";
				// DisplayMetrics dm = getResources().getDisplayMetrics();
				// usi.setUserXAxis((int) x);
				// usi.setUserYAxis((int) y);
				// LinearLayout ll = generateImageButton(str, (int) x, (int) y,
				// false);
				// usi.setSign(ll);
				// usi.setQuestionID(listQuestionInfo.get(nowLoc).getId());
				// listQuestionInfo.get(nowLoc).getLsUserSign().add(usi);
				// flFuck.setOnTouchListener(null);
				// SoundPoolUtil.getInstance().play(FillInContent.this,
				// SoundPoolUtil.SOUND_SEAL_SUCCESS_ID);
				// }
				// return false;
				// }
				// });
				// } else {// 否则直接进入欢迎页面
				flFuck.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						float x = event.getX();
						float y = event.getY();

						String str = listSigns.get(inPos).getSignName();
						if (isSignUsed(str)) {
							Utils.showToast("同类印章只能加盖一次", mContext.getApplicationContext());
							flFuck.setOnTouchListener(null);
						} else {

							UserSignInfo usi = new UserSignInfo();
							usi.setUserSignName(str);
							usi.setRight(isSignRight(str));
							int location = str.indexOf(".");
							str = str.substring(0, location);
							str = "sign_" + str + "_big.png";
							DisplayMetrics dm = getResources().getDisplayMetrics();
							usi.setUserXAxis((int) x);
							usi.setUserYAxis((int) y);
							LinearLayout ll = generateImageButton(str, (int) x, (int) y, false);
							usi.setSign(ll);
							usi.setQuestionID(listQuestionInfo.get(nowLoc).getId());
							listQuestionInfo.get(nowLoc).getLsUserSign().add(usi);
							flFuck.setOnTouchListener(null);
							ToastUtil.showToast(mContext, "盖章", 1);
						}
						return false;
					}
				});

				// }
				dialog.dismiss();

			}
		};
//		dialog = new SignChooseDialog(mContext, listSigns, listener);
//		dialog.setOnButtonClickListener(new OnButtonClickListener() {
//
//			public void onCancelClicked() {
//				dialog.dismiss();
//
//			}
//		});
//		dialog.show();
//		dialog.setCanceledOnTouchOutside(true);

		// if (tempDialog.isShowing()) {
		// tempDialog.dismiss();
		// }
		// tempDialog.show();
		//
		// SignsAdapter signsAdapter = new SignsAdapter(FillInContent.this,
		// listSigns);
		//
		// ListView signs = (ListView) tempDialog.findViewById(R.id.lv_signs);
		// signs.setAdapter(signsAdapter);

		// signs.setOnItemClickListener(new MyOnItemClickListener());
	}

	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			String str = listSigns.get(arg2).getSignName();
			if (isSignUsed(str)) {
				Utils.showToast("同类印章只能加盖一次", mContext);
			} else {

				UserSignInfo usi = new UserSignInfo();
				usi.setUserSignName(str);
				usi.setRight(isSignRight(str));
				int location = str.indexOf(".");
				str = str.substring(0, location);
				str = "sign_" + str + "_big.png";
				DisplayMetrics dm = getResources().getDisplayMetrics();
				LinearLayout ll = generateImageButton(str, dm.widthPixels, 0, false);
				ll.setClickable(true);
				ll.bringToFront();
				ll.setOnTouchListener(new MyOnTouchListener());
				usi.setSign(ll);
				usi.setQuestionID(listQuestionInfo.get(nowLoc).getId());
				listQuestionInfo.get(nowLoc).getLsUserSign().add(usi);
				ToastUtil.showToast(mContext, "盖章", 2);
			}

			tempDialog.dismiss();

		}

	}

	private boolean isSignUsed(String signName) {
		List<UserSignInfo> lsUSI = listQuestionInfo.get(nowLoc).getLsUserSign();
		if (lsUSI == null || lsUSI.size() == 0) {
			return false;
		} else {
			for (int i = 0; i < lsUSI.size(); i++) {
				if (lsUSI.get(i).getUserSignName().equals(signName)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSignRight(String signName) {
		List<SignInfo> lsSign = listQuestionInfo.get(nowLoc).getLsSign();
		for (int i = 0; i < lsSign.size(); i++) {
			if (lsSign.get(i).getSignName().equals(signName)) {
				return true;
			}
		}
		return false;
	}

	class MyOnTouchListener implements OnTouchListener {
		int lastX = 0, lastY = 0; // 记录移动的最后的位置

		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			// 获取Action
			int ea = event.getAction();

			// 获取屏幕宽高信息
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int screenWidth = dm.widthPixels;
			int screenHeight = dm.heightPixels;

			Log.i("TAG", "Touch:" + ea);
			switch (ea) {
			case MotionEvent.ACTION_DOWN: // 按下
				Log.d("ACTION_DOWN", "===============ACTION_DOWN==============");
				flFuck.requestDisallowInterceptTouchEvent(true);
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			/**
			 * layout(l,t,r,b) l Left position, relative to parent t Top
			 * position, relative to parent r Right position, relative to parent
			 * b Bottom position, relative to parent
			 */
			case MotionEvent.ACTION_MOVE: // 移动
				Log.d("ACTION_MOVE", "===============ACTION_MOVE==============");
				// 移动中动态设置位置
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;
				int left = v.getLeft() + dx;
				int top = v.getTop() + dy;
				int right = v.getRight() + dx;
				int bottom = v.getBottom() + dy;
				if (left < 0) {
					left = 0;
					right = left + v.getWidth();
				}
				if (right > screenWidth) {
					right = screenWidth;
					left = right - v.getWidth();
				}
				if (top < 0) {
					top = 0;
					bottom = top + v.getHeight();
				}
				if (bottom > screenHeight) {
					bottom = screenHeight;
					top = bottom - v.getHeight();
				}
				v.layout(left, top, right, bottom);
				Log.i("", "position：" + left + ", " + top + ", " + right + ", " + bottom);
				// 将当前的位置再次设置
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP: // 脱离
				Log.d("ACTION_UP", "===============ACTION_UP==============");
				flFuck.requestDisallowInterceptTouchEvent(false);
				AlertDialog.Builder malertdialog = new AlertDialog.Builder(mContext).setMessage("确认印章盖在这里吗？").setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 固定印章位置
						v.setClickable(false);
						android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(v.getWidth(), v.getHeight());

						params.setMargins(v.getLeft(), v.getTop(), 0, 0);
						v.setLayoutParams(params);
						// 储存用户选择的印章坐标
						int index = 0;
						List<UserSignInfo> listUS = listQuestionInfo.get(nowLoc).getLsUserSign();
						for (int i = 0; i < listUS.size(); i++) {
							if (listUS.get(i).getSign() == v) {
								index = i;
							}
						}
						listQuestionInfo.get(nowLoc).getLsUserSign().get(index).setUserXAxis(v.getLeft());
						listQuestionInfo.get(nowLoc).getLsUserSign().get(index).setUserYAxis(v.getTop());
					}
				});
				malertdialog.show();
				break;
			}
			return false;
		}
	}

	// 显示印章
	public void showSign() {
		sign();
	}

	// 显示图片
	public void showPictures() {
		// showPics();
		// showPicture(listQuestionInfo.get(nowLoc).getPicName());
		browsePics();
	}

	/**
	 * 保存用户答案
	 */
	public void saveUserAnswer() {
		// if (state == 1 || state == 2) {
		// return;
		// }
		// 填写信息
		for (int i = 0; i < listQuestionInfo.get(nowLoc).getLsET().size(); i++) {
			if (listET != null && listET.size() >= listQuestionInfo.get(nowLoc).getLsET().size()) {
				String str = listET.get(i).getText().toString();
				listQuestionInfo.get(nowLoc).getLsET().get(i).setUserAnswer(str);
			}
		}
		eso.updateUserAnswer(listQuestionInfo, nowLoc, mContext);
		mUscore = getScore();
		state = mUscore == mData.getScore() ? 1 : 2;
		mTestData.setState(state);
		mData.setuScore(mUscore);
		SubjectBillDataModel.getInstance(mContext).updateUscore(mData.getId(), mUscore);
		TestDataModel.getInstance(mContext).updateState(testId, state);
	}

	/**
	 * 完成
	 */
	public void done() {
		listQuestionInfo.get(nowLoc).setIsCompleted((byte) 2);
		//mData.setCompleted(true);
		saveUserAnswer();

		mUscore = getScore();
		// tvScore.setText(DecimalFormatUtil.formatFloat(mUscore, "0.0"));
		// 保存对应的状态以及分数
		state = mUscore == mData.getScore() ? 1 : 2;
		// mTestData.setState(state);
		// TestDataModel.getInstance(mContext).updateState(testId, state);
		SubjectBillDataModel.getInstance(mContext).updateUscore(mData.getId(), mUscore);
		// mData.setuScore(mUscore);
		showResult = true;
		// resultLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 计算得分，获取答案状态，全对是1，有错是2
	 * 
	 * @return 分数
	 */
	private float getScore() {
		List<ViewInfo> lsET = listQuestionInfo.get(nowLoc).getLsET();
		float totalCount = 0;
		float rightCount = 0;
		HashMap<Integer, Integer> bigDateMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> smallNumDateMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> numMoneyMap = new HashMap<Integer, Integer>();
		getScoreLoop: for (int i = 0; i < lsET.size(); i++) {
			String string = listET.get(i).getText().toString();
			if (lsET.get(i).getEditable() == 1) {
				if (isForBigLetterDateEditText2(lsET.get(i).getInputTyep())) {
					if (bigDateMap.containsKey(getGroupId(lsET.get(i).getInputTyep()))) {
						continue getScoreLoop;
					} else {
						int groupId = getGroupId(lsET.get(i).getInputTyep());
						bigDateMap.put(groupId, 1);
						boolean isRight = true;
						innerLoop1: for (int ib = 0; ib < lsET.size(); ib++) {
							if (isForBigLetterDateEditText2(lsET.get(ib).getInputTyep()) && getGroupId(lsET.get(ib).getInputTyep()) == groupId) {
								if (!judeCapsDate(lsET.get(ib).getAnswer(), lsET.get(ib).getUserAnswer(), lsET.get(ib).getInputTyep())) {
									isRight = false;
									break innerLoop1;
								}
							}
						}
						totalCount++;
						if (isRight) {
							rightCount++;
							if (lsET.get(i).getIsRighted() == 0) {
								eso.setViewInfoRighted(lsET.get(i).getId());

							}
						}
					}

				} else if (isForSmallLetterDateEditText2(lsET.get(i).getInputTyep())) {
					if (smallNumDateMap.containsKey(getGroupId(lsET.get(i).getInputTyep()))) {
						continue getScoreLoop;
					} else {
						int groupId = getGroupId(lsET.get(i).getInputTyep());
						smallNumDateMap.put(groupId, 1);
						boolean isRight = true;
						innerLoop1: for (int ib = 0; ib < lsET.size(); ib++) {
							if (isForSmallLetterDateEditText2(lsET.get(ib).getInputTyep()) && getGroupId(lsET.get(ib).getInputTyep()) == groupId) {
								if (!lsET.get(ib).getUserAnswer().equals(lsET.get(ib).getAnswer())) {
									isRight = false;
									break innerLoop1;
								}
							}
						}
						totalCount++;
						if (isRight) {
							rightCount++;
							if (lsET.get(i).getIsRighted() == 0) {
								eso.setViewInfoRighted(lsET.get(i).getId());

							}
						}
					}
				} else if (isForSingleMoneyEditText(lsET.get(i).getInputTyep())) {
					if (numMoneyMap.containsKey(lsET.get(i).getInputTyep())) {
						continue getScoreLoop;
					} else {
						numMoneyMap.put(lsET.get(i).getInputTyep(), 1);
						boolean isRight = true;
						innerLoop1: for (int ib = 0; ib < lsET.size(); ib++) {
							if (isForSingleMoneyEditText(lsET.get(ib).getInputTyep())) {
								if (!lsET.get(ib).getUserAnswer().equals(lsET.get(ib).getAnswer())) {
									isRight = false;
									break innerLoop1;
								}
							}
						}
						totalCount++;
						if (isRight) {
							rightCount++;
							if (lsET.get(i).getIsRighted() == 0) {
								eso.setViewInfoRighted(lsET.get(i).getId());

							}
						}
					}
				} else {
					totalCount++;
					if (lsET.get(i).getIsMutiple() == 0) {
						if (string.equals(lsET.get(i).getAnswer())) {
							rightCount++;
							if (lsET.get(i).getIsRighted() == 0) {
								eso.setViewInfoRighted(lsET.get(i).getId());

							}
						}
					}
				}
			}
		}

		float score = (rightCount / totalCount) * mData.getScore();
		// TestDataModel.getInstance(mContext).updateStateAndScore(testId,
		// subId, rightCount == totalCount ? 1 : 2, score, 0);

		return score;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_seal:
			// subjectData.setDone(true);
			sign();

			break;
		case R.id.btn_accomplish:
			done();
			// setSendBtnVisBilit();
			break;
		// case R.id.btn_back:
		// this.finish();
		// exit();
		// break;
		case R.id.btn_que_card:

			break;
		case R.id.btn_select_back:

			break;
		case R.id.btn_show_user:
			showAnswer(0, 1);

			break;
		case R.id.btn_show_right:
			showAnswer(0, 2);

			break;

		case R.id.rl_audit_indicator:
			hideIndicator();

			break;

		case R.id.btn_show_details:
			// if (mTestData.getType() == Constant.TEST_TYPE_EXERCIZE) {
			// if (mTestData.getRemark() == null ||
			// !mTestData.getRemark().equals("1")) {
			// ToastUtil.showToast(mContext, "现在还不可以查看答案", 1000);
			// return;
			// }
			// }
			showResult = inited = false;
			initData();
			initView();

			break;

		case R.id.btn_original:
			showPics();
			break;

		default:
			break;
		}

	}

	private void setSendBtnVisBilit() {
		// if (Constant.TEST_TYPE_EXERCIZE_OR_SWEETOWN == 2) {
		// btnSendScore.setVisibility(View.GONE);
		// sendScore();
		// } else {
		// // btnSendScore.setVisibility(View.VISIBLE);
		// }
	}

	/**
	 * 显示答案，或者显示答题情况（对错）
	 * 
	 * @param list
	 *            list，用于存放EditText
	 * @param operate
	 *            1,答题结果;2,正确答案
	 * @return
	 */
	private synchronized void showAnswer(int location, int operate) {
		// Log.e("showAnswer", "showAnswer---> " + operate);
		Vector<SignInfo> lsSign = new Vector<SignInfo>(listQuestionInfo.get(location).getLsSign());
		List<UserSignInfo> listUI = listQuestionInfo.get(location).getLsUserSign();
		if (1 == operate) { // 答题结果
			btnShowUser.setHovered(true);
			btnShowRight.setHovered(false);
			// 答案印章信息重置
			for (int i = 0; i < listAnswerSign.size(); i++) {
				if (listAnswerSign.get(i) != null) {
					flFuck.removeView(listAnswerSign.get(i));
				}
			}
			listAnswerSign.clear();
			List<UserSignInfo> list3 = listQuestionInfo.get(location).getLsUserSign();
			for (int i = 0; i < listUserSign.size(); i++) {
				if (listUserSign.get(i) != null) {
					flFuck.removeView(listUserSign.get(i));
				}
			}
			if (listUserSign != null)
				listUserSign.clear();
			if (list3 != null) {
				for (int i = 0; i < list3.size(); i++) {
					String str = list3.get(i).getUserSignName();
					int position = str.indexOf(".");
					str = str.substring(0, position);
					str = "sign_" + str + "_big.png";
					int left = list3.get(i).getUserXAxis();
					int top = list3.get(i).getUserYAxis();
					LinearLayout tmp = generateImageButton(str, left, top, false);
					list3.get(i).setSign(tmp);
					listUserSign.add(tmp);
				}
			}
			// 填空内容比对
			for (int i = 0; i < listET.size(); i++) {
				String str = ((ViewInfo) listET.get(i).getTag()).getUserAnswer();
				if (((ViewInfo) listET.get(i).getTag()).getEditable() == 1) {
					if (isForSingleMoneyEditText(((ViewInfo) listET.get(i).getTag()).getInputTyep())) {
						listET.get(i).removeTextChangedListener(((ViewInfo) listET.get(i).getTag()).getTw());
						listET.get(i).setText(str);
						listET.get(i).addTextChangedListener(((ViewInfo) listET.get(i).getTag()).getTw());
					} else {
						listET.get(i).setText(((ViewInfo) listET.get(i).getTag()).getUserAnswer());
					}
				}

				listET.get(i).setFocusable(false);
				String string = listET.get(i).getText().toString();
				if (string.equals(((ViewInfo) listET.get(i).getTag()).getAnswer()) && ((ViewInfo) listET.get(i).getTag()).getEditable() == 1) {
					// 正确答案字体颜色为蓝色
					listET.get(i).setTextColor(Color.BLUE);
				} else if (!string.equals(((ViewInfo) listET.get(i).getTag()).getAnswer()) && ((ViewInfo) listET.get(i).getTag()).getEditable() == 1) {
					// 输入答案错误，EditText变色
					if (isForBigLetterDateEditText2(((ViewInfo) listET.get(i).getTag()).getInputTyep())) {
						// modi by lucher
						if (!judeCapsDate(((ViewInfo) listET.get(i).getTag()).getAnswer(), listET.get(i).getText().toString(), ((ViewInfo) listET.get(i).getTag()).getInputTyep())) {
							listET.get(i).setBackgroundResource(R.drawable.et_bg_random_wrong);
						}
					} else {
						listET.get(i).setBackgroundResource(R.drawable.et_bg_random_wrong);
					}
				}
			}
			// 印章内容比较
			List<UserSignInfo> lsUSI = listQuestionInfo.get(nowLoc).getLsUserSign();
			for (int i = 0; i < lsUSI.size(); i++) {
				if (!lsUSI.get(i).isRight()) {
					lsUSI.get(i).getSign().setBackgroundColor(Color.YELLOW);
				}
			}
		} else if (0 == operate) {

		} else if (2 == operate) { // 正确答案
			btnShowRight.setHovered(true);
			btnShowUser.setHovered(false);
			if (listUI != null) {
				for (int i = 0; i < listUI.size(); i++) {
					listUI.get(i).getSign().setVisibility(View.GONE);
					// Log.e("111", "11111");
				}
			}
			// 填空答案
			for (int i = 0; i < listET.size(); i++) {
				listET.get(i).setTextColor(Color.BLACK);
				listET.get(i).setBackgroundColor(0x00000000);
				String str = ((ViewInfo) listET.get(i).getTag()).getAnswer();
				if (((ViewInfo) listET.get(i).getTag()).getEditable() == 1) {
					if (isForSingleMoneyEditText(((ViewInfo) listET.get(i).getTag()).getInputTyep())) {
						listET.get(i).removeTextChangedListener(((ViewInfo) listET.get(i).getTag()).getTw());
						listET.get(i).setText(str);
						listET.get(i).addTextChangedListener(((ViewInfo) listET.get(i).getTag()).getTw());
					} else {
						listET.get(i).setText(str);
					}
				}
				//
				// Log.e("222", "22222");
			}
			// 印章答案
			for (int i = 0; i < lsSign.size(); i++) {
				String str = lsSign.get(i).getSignName();
				int position = str.indexOf(".");
				str = str.substring(0, position);
				str = "sign_" + str + "_big.png";
				int left = lsSign.get(i).getxAxis();
				int top = lsSign.get(i).getyAxis();
				listAnswerSign.add(generateImageButton(str, left, top, true));
				// Log.e("444", "444444");
			}
		}

	}

	private boolean isToDoJudge(View lastView, View currentView) {
		if (lastView == null || currentView == null)
			return true;
		if (((ViewInfo) lastView.getTag()).getInputTyep() == ((ViewInfo) currentView.getTag()).getInputTyep() && isForSingleMoneyEditText(((ViewInfo) currentView.getTag()).getInputTyep()))
			return false;
		else if (isForGroupEditText(((ViewInfo) lastView.getTag()).getInputTyep()) && ((ViewInfo) lastView.getTag()).getInputTyep() / 100 == ((ViewInfo) currentView.getTag()).getInputTyep() / 100
				&& ((ViewInfo) lastView.getTag()).getInputTyep() % 100 / 10 == ((ViewInfo) currentView.getTag()).getInputTyep() % 100 / 10)
			return false;
		else
			return true;
	}

	/**
	 * 目标view
	 * 
	 * @param view
	 *            指定类型
	 * @param etType
	 *            0为灰色 1为透明
	 * @param states
	 */
	private void setMutipleEditTextState(View view, int etType, int states) {
		if (etType < 10) {
			for (EditText etT2 : listET) {
				if (((ViewInfo) etT2.getTag()).getInputTyep() == etType) {
					Log.e("tag", "tag--->  " + ((ViewInfo) etT2.getTag()).getId() + " pop-->  " + ((ViewInfo) view.getTag()).getId());
					if (states == 0)
						if (((ViewInfo) etT2.getTag()).getId() == ((ViewInfo) view.getTag()).getId()) {
							etT2.setBackgroundColor(0x990066FF);
						} else {
							etT2.setBackgroundColor(0x55000000);
						}
					else
						etT2.setBackgroundColor(0x00000000);
				}
			}
		} else {
			int dateType = etType / 100;
			int dateGroup = etType % 100 / 10;
			for (EditText etT2 : listET) {
				int tempDateType = ((ViewInfo) etT2.getTag()).getInputTyep() / 100;
				int tempDateGroup = ((ViewInfo) etT2.getTag()).getInputTyep() % 100 / 10;
				if (dateType == tempDateType && dateGroup == tempDateGroup) {
					if (states == 0)
						if (((ViewInfo) etT2.getTag()).getId() == ((ViewInfo) view.getTag()).getId()) {
							etT2.setBackgroundColor(0x990066FF);
						} else {
							etT2.setBackgroundColor(0x55000000);
						}
					else
						etT2.setBackgroundColor(0x00000000);
				}
			}
		}
	}

	private int getGroupId(int inputType) {
		return inputType % 100 / 10;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// subjectData.setDone(true);
		// done();
		if (!initFocusFlag)
			return;
		if (lastUnfocusView == null) {
			lastUnfocusView = view;
		} else {
			if (!isToDoJudge(lastUnfocusView, view)) {
				lastUnfocusView.setBackgroundColor(0x55000000);
				lastUnfocusView = view;
				return;
			} else {

				if (isForSingleMoneyEditText(((ViewInfo) view.getTag()).getInputTyep())) {
					setMutipleEditTextState(view, ((ViewInfo) view.getTag()).getInputTyep(), 0);
				} else if (isForBigLetterDateEditText2(((ViewInfo) view.getTag()).getInputTyep())) {
					setMutipleEditTextState(view, ((ViewInfo) view.getTag()).getInputTyep(), 0);
				} else if (isForSmallLetterDateEditText2(((ViewInfo) view.getTag()).getInputTyep())) {
					setMutipleEditTextState(view, ((ViewInfo) view.getTag()).getInputTyep(), 0);
				}

			}

			/**
			 * 联动的结果判断逻辑
			 */
			for (int i = 0; i < listET.size(); i++) {
				if (lastUnfocusView.equals(listET.get(i))) {
					String rightAnswer = listQuestionInfo.get(nowLoc).getLsET().get(i).getAnswer();
					String userString = listET.get(i).getText().toString();

					if (rightAnswer.equals(userString) || isForBigLetterDateEditText2(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
						boolean isMutipleAllRight = true;
						List<EditText> tempList = new ArrayList<EditText>();
						// inputType为1的时候联动多空判定,大写日期
						if (isForBigLetterDateEditText2(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
							int group = getGroupId(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep());
							for (EditText etT : listET) {
								if (isForBigLetterDateEditText2(((ViewInfo) etT.getTag()).getInputTyep()) && getGroupId(((ViewInfo) etT.getTag()).getInputTyep()) == group) {
									tempList.add(etT);
									// if
									// (!etT.getText().toString().equals(((ViewInfo)
									// etT.getTag()).getAnswer())) {
									// isMutipleAllRight = false;
									// }
									// modi by lucher
									if (!judeCapsDate(((ViewInfo) etT.getTag()).getAnswer(), etT.getText().toString(), ((ViewInfo) etT.getTag()).getInputTyep())) {
										isMutipleAllRight = false;
									}
								}
							}
							for (EditText et : tempList) {
								if (isMutipleAllRight) {
									et.setBackgroundColor(0x0000ff00);
								} else {
									et.setBackgroundColor(0x88ff0000);
								}
							}
						} else if (isForSmallLetterDateEditText2(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
							// 小写日期判断
							int group = getGroupId(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep());
							for (EditText etT : listET) {
								if (isForSmallLetterDateEditText2(((ViewInfo) etT.getTag()).getInputTyep()) && getGroupId(((ViewInfo) etT.getTag()).getInputTyep()) == group) {
									tempList.add(etT);
									if (!etT.getText().toString().equals(((ViewInfo) etT.getTag()).getAnswer())) {
										isMutipleAllRight = false;
									}
								}
							}
							for (EditText et : tempList) {
								if (isMutipleAllRight) {
									et.setBackgroundColor(0x0000ff00);
								} else {
									et.setBackgroundColor(0x88ff0000);
								}
							}
						} else if (isForSingleMoneyEditText(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
							for (EditText etT : listET) {
								if (isForSingleMoneyEditText(((ViewInfo) etT.getTag()).getInputTyep())) {
									tempList.add(etT);
									if (!etT.getText().toString().equals(((ViewInfo) etT.getTag()).getAnswer())) {
										isMutipleAllRight = false;
									}
								}
							}
							for (EditText et : tempList) {
								if (isMutipleAllRight) {
									et.setBackgroundColor(0x0000ff00);
								} else {
									et.setBackgroundColor(0x88ff0000);
								}
							}
						} else {
							listET.get(i).setBackgroundColor(0x0000ff00);
						}
						//
						// Log.e("cu", "-->"
						// + listQuestionInfo.get(nowLoc).getLsET().get(i)
						// .getInputTyep()
						// + " --- "
						// + listQuestionInfo.get(nowLoc).getLsET().get(i)
						// .getId());
					} else {
						if (isForSingleMoneyEditText(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
							for (EditText etT : listET) {
								for (ViewInfo viT : listQuestionInfo.get(nowLoc).getLsET()) {
									if (((ViewInfo) etT.getTag()).getId() == viT.getId() && isForSingleMoneyEditText(viT.getInputTyep())) {
										etT.setBackgroundColor(0x88ff0000);
									}
								}
							}
						} else if (isForSmallLetterDateEditText2(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())
								|| isForBigLetterDateEditText2(listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep())) {
							int dateType = listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep() / 100;
							int dateGroup = listQuestionInfo.get(nowLoc).getLsET().get(i).getInputTyep() % 100 / 10;
							for (EditText etT : listET) {
								if (((ViewInfo) etT.getTag()).getInputTyep() / 100 == dateType && ((ViewInfo) etT.getTag()).getInputTyep() % 100 / 10 == dateGroup) {
									etT.setBackgroundColor(0x88ff0000);
								}
							}

						}

						listET.get(i).setBackgroundColor(0x88ff0000);
					}
				}
			}
			lastUnfocusView = view;

		}
		// done();
	}

	/**
	 * 对比大写日期的正确性
	 * 
	 * @param answer
	 *            正确答案
	 * @param userAnswer
	 *            用户答案
	 * @param type
	 *            日期类别，年月日
	 * @return
	 */
	private boolean judeCapsDate(String answer, String userAnswer, int type) {
		boolean result = false;
		String tmp = String.valueOf(type);
		if (tmp.length() == 3) {
			if (tmp.endsWith("4") || tmp.endsWith("6")) {// 年或日
				result = answer.equals(userAnswer);
			} else if (tmp.endsWith("5")) {// 月，对于3-8，前面可加可不加0
				result = fuzzyEqualsMonth(answer, userAnswer);
			}
		}

		return result;
	}

	/**
	 * 模糊对比月份
	 * 
	 * @param answer
	 * @param userAnswer
	 * @return
	 */
	private boolean fuzzyEqualsMonth(String answer, String userAnswer) {
		boolean equal = false;
		if (answer.equals("零壹") || answer.equals("零贰") || answer.equals("零壹拾") || answer.equals("壹拾壹") || answer.equals("壹拾贰")) {
			equal = answer.equals(userAnswer);
		} else {
			if (answer.equals(userAnswer) || userAnswer.length() == 1 && answer.endsWith(userAnswer)) {
				equal = true;
			}
		}

		return equal;
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
		Log.i("aaa", String.format("y:%d,m:%d,d:%d", year, month, date));

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
	 * 隐藏图片提示
	 */
	private void hideIndicator() {
		if (indicatorAnim.isRunning())
			return;
		PreferenceHelper.getInstance(mContext).setFirstTimeFalse(IS_FIRST_SIGN);
		indicatorAnim.play(ObjectAnimator.ofFloat(rlIndicator, View.ALPHA, 1, 0));
		indicatorAnim.setDuration(800);
		indicatorAnim.setInterpolator(new DecelerateInterpolator());
		indicatorAnim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				rlIndicator.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				rlIndicator.setVisibility(View.GONE);
			}
		});
		indicatorAnim.start();
	}

	/**
	 * 显示审核操作的图片提示
	 */
	private void showIndicator() {
		rlIndicator.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示图片
	 */
	private void showPics() {
		if (picDialog != null) {
			picDialog.show();
		}
	}

	/**
	 * 重置题目
	 */
	public void reset() {
		state = 0;
		mTestData.setState(state);
		TestDataModel.getInstance(mContext).updateState(testId, state);
		SubjectBillDataModel.getInstance(mContext).updateUscore(mData.getId(), 0);

		showResult = inited = false;

		initData();
		initView();
		eso.clearUserAnswer(mData.getId());
		listQuestionInfo.get(nowLoc).setIsCompleted((byte) 0);
		//mData.setCompleted(false);
		eso.updateUserAnswer(listQuestionInfo, nowLoc, mContext);
		eso.deleteUserSigns(mData.getId());
	}

}
