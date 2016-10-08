package com.edu.basicaccountingforguangzhou;

import java.io.Serializable;
import java.util.List;

import com.edu.basicaccountingforguangzhou.adapter.SubjectViewPagerAdapter;
import com.edu.basicaccountingforguangzhou.data.EduSqliteDbOprater;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.data.TextInfoDataDao;
import com.edu.basicaccountingforguangzhou.dialog.ConfirmReDoDialog;
import com.edu.basicaccountingforguangzhou.dialog.ConfirmReDoDialog.OnRedoClickListener;
import com.edu.basicaccountingforguangzhou.model.BillDataModel;
import com.edu.basicaccountingforguangzhou.model.SubjectBillDataModel;
import com.edu.basicaccountingforguangzhou.model.SubjectModel;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.model.ViewModel;
import com.edu.basicaccountingforguangzhou.view.AutoJumpNextListener;
import com.edu.basicaccountingforguangzhou.view.CardViewForPractice;
import com.edu.basicaccountingforguangzhou.view.CardViewForPractice.onClickListeners;
import com.edu.basicaccountingforguangzhou.view.CardViewForPractice.onClickListenersForError;
import com.edu.basicaccountingforguangzhou.view.CardViewForPractice.onClickListenersForHide;
import com.edu.basicaccountingforguangzhou.view.IOnClickListener;
import com.edu.basicaccountingforguangzhou.view.SubjectViewListener;
import com.edu.library.util.ToastUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 题目练习界面
 * 
 * @author lucher
 * 
 */
public class SubjectTestActivity extends BaseFragmentActivity
		implements SubjectViewListener, OnClickListener, IOnClickListener, onClickListeners, onClickListenersForError, onClickListenersForHide, AutoJumpNextListener {
	private static final String TAG = "SubjectTestActivity";

	/**
	 * 题目viewpager界面
	 */
	private ViewPager vpSubject;
	/**
	 * 题目信息对应的adapter
	 */
	private SubjectViewPagerAdapter adapter;

	// 当前页面
	private int currentIndex;
	// 测试类别
	private int type = 0;
	// title布局
	private RelativeLayout titleLayout;

	// 测试类别
	private TextView tvTestType;
	private List<TestData> mDatas;
	// 答题卡布局
	private RelativeLayout rlCard;
	private LinearLayout llyoutCard;
	// 答題卡动画
	private AnimatorSet animationCard;
	private View view;
	private Button btnCard;
	private ProgressDialog loadingDialog;
	private int typeClear;
	private int show = 0;// 判断题卡当前什么状态
	private int textId;
	private Button btnLast;// 上一题
	private Button btnNext;// 下一题
	private Button btnStamp;// 印章
	private Button btnInfo;// 显示图片
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			initView();
			initData();
		};
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			loadingDialog.dismiss();
			type = 1;
			refreshData();
			btnCard.setBackgroundResource(R.drawable.btn_card_n);
			show = 0;
			findViewById(R.id.btn_submit).setVisibility(View.VISIBLE);
			btnStamp.setVisibility(View.GONE);
			btnInfo.setVisibility(View.GONE);
			super.handleMessage(msg);
		};
	};
	// 页面相关状态的监听
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		// 页面切换后调用
		@Override
		public void onPageSelected(int item) {
			adapter.saveUserAnswer(currentIndex);
			currentIndex = item;
			// tvTestType.setText(mDatas.get(item).getTitle());
			if (mDatas.get(item).getSubjectType() == 5 && type == 1) {
				btnStamp.setVisibility(View.VISIBLE);
			} else {
				btnStamp.setVisibility(View.GONE);
			}

			if (mDatas.get(item).getSubjectType() == 5) {
				btnInfo.setVisibility(View.VISIBLE);
			} else {
				btnInfo.setVisibility(View.GONE);
			}
			if (mDatas.get(item).getSubjectType() == 4) {
				String[] questions = mDatas.get(item).getTitle().split(">>>");
				tvTestType.setText(Html.fromHtml(questions[0], null, null));
			} else {
				tvTestType.setText(mDatas.get(item).getTitle());
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int item) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject_test);

		mHandler.sendEmptyMessageDelayed(0, 200);

	}

	// 加载数据
	public void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getInt("testType");
			mDatas = (List<TestData>) bundle.getSerializable("datas");
			currentIndex = bundle.getInt("index");
			textId = bundle.getInt("textId");
		}
		if (type == 1) {
			findViewById(R.id.btn_submit).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.btn_submit).setVisibility(View.GONE);
		}
		adapter = new SubjectViewPagerAdapter(getSupportFragmentManager(), mDatas, (AutoJumpNextListener) mContext, this, type);
		vpSubject.setAdapter(adapter);
		vpSubject.setOnPageChangeListener(mPageChangeListener);
		vpSubject.setCurrentItem(currentIndex);
		Constant.TITLE_HEIGHT = titleLayout.getHeight();
		// tvTestType.setText(mDatas.get(currentIndex).getTitle());
		if (mDatas.get(currentIndex).getSubjectType() == 4) {
			String[] questions = mDatas.get(currentIndex).getTitle().split(">>>");
			tvTestType.setText(Html.fromHtml(questions[0], null, null));
		} else {
			tvTestType.setText(mDatas.get(currentIndex).getTitle());
		}

	}

	/**
	 * 刷新数据
	 */
	private void refreshData() {
		// id数据直接查询id
		// if (textId == 1) {
		mDatas = TestDataModel.getInstance(mContext).getDatas(textId, 2);
		// } else if (textId == 2) {
		// mDatas = TestDataModel.getInstance(mContext).getDatas(23, 2);
		// } else if (textId == 3) {
		// mDatas = TestDataModel.getInstance(mContext).getDatas(16, 2);
		// } else if (textId == 4) {
		// mDatas = TestDataModel.getInstance(mContext).getDatas(18, 2);
		// } else if (textId == 5) {
		// mDatas = TestDataModel.getInstance(mContext).getDatas(19, 2);
		// }
		adapter = new SubjectViewPagerAdapter(getSupportFragmentManager(), mDatas, (AutoJumpNextListener) mContext, this, type);
		vpSubject.setAdapter(adapter);
		vpSubject.setOnPageChangeListener(mPageChangeListener);
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	public void initView() {
		titleLayout = (RelativeLayout) this.findViewById(R.id.rl_title);
		vpSubject = (ViewPager) findViewById(R.id.vp_content);
		tvTestType = (TextView) findViewById(R.id.tv_test_type);
		btnCard = (Button) findViewById(R.id.btn_card);
		rlCard = (RelativeLayout) this.findViewById(R.id.rl_card);
		btnLast = (Button) findViewById(R.id.btn_last);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnStamp = (Button) findViewById(R.id.btn_stamp);
		btnInfo = (Button) findViewById(R.id.btn_info);
		btnLast.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		rlCard.setOnClickListener(this);
		btnCard.setOnClickListener(this);
		btnStamp.setOnClickListener(this);
		btnInfo.setOnClickListener(this);

	}

	/**
	 * 设置当前页
	 * 
	 * @param index
	 */
	public void setCurrentPage(int index) {
		currentIndex = index;
		vpSubject.setCurrentItem(index);
	}

	/**
	 * 重置
	 */
	public void reset() {
		setCurrentPage(0);
		adapter.reset();
	}

	@Override
	public void showQueCard() {

	}

	/**
	 * 提交
	 */
	private void startResultActivity() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("datas", (Serializable) mDatas);
		bundle.putInt("textId", textId);
		intent.putExtras(bundle);
		intent.setClass(this, SubjectTestResult.class);
		startActivity(intent);
	}

	/**
	 * 返回
	 */
	private void startBackActivity() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("textId", textId);
		bundle.putInt("index", currentIndex);
		intent.putExtras(bundle);
		intent.setClass(SubjectTestActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_submit:
			showConfirmDialog("确定提交吗？");
			break;

		case R.id.btn_back:
			if (TextInfoDataDao.getInstance(mContext).querState(textId) != 1) {
				TextInfoDataDao.getInstance(mContext).upDoneFlag(textId, 2);
			}
			// startActivity(MainActivity.class);
			startBackActivity();
			this.finish();
			break;

		case R.id.btn_card:
			showQuestionCard();
			break;

		case R.id.rl_card:
			hideCard();
			break;

		case R.id.btn_last:
			if (currentIndex != 0) {
				currentIndex--;
				vpSubject.setCurrentItem(currentIndex, true);
			} else {
				ToastUtil.showToast(mContext, "已经是第一题！");
			}
			break;

		case R.id.btn_next:
			if (currentIndex != mDatas.size() - 1) {
				currentIndex++;
				vpSubject.setCurrentItem(currentIndex, true);
			}
			break;

		case R.id.btn_stamp:
			adapter.setVisbilte(currentIndex);
			break;

		case R.id.btn_info:
			adapter.setPictureVisbilte(currentIndex);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (adapter != null) {
			adapter.saveUserAnswer(currentIndex);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 隐藏弹出卡
	 */
	private void hideCard() {
		// showCard = false;
		animationCard = new AnimatorSet();
		animationCard.play(ObjectAnimator.ofFloat(rlCard, View.X, rlCard.getScrollX(), rlCard.getWidth()));
		animationCard.setDuration(200);
		animationCard.setInterpolator(new DecelerateInterpolator());
		animationCard.start();
		animationCard.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				rlCard.setVisibility(View.GONE);
				btnCard.setBackgroundResource(R.drawable.btn_card_n);
				show = 0;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				rlCard.setVisibility(View.GONE);
				btnCard.setBackgroundResource(R.drawable.btn_card_n);
				show = 0;
			}
		});
	}

	/*
	 * * 显示弹出卡
	 * 
	 * @param view 弹出框里的内容
	 */
	private void showCard(View view) {
		rlCard.removeAllViews();
		rlCard.addView(view);
		rlCard.setVisibility(View.VISIBLE);
		animationCard = new AnimatorSet();
		ViewTreeObserver observer = rlCard.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				rlCard.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				animationCard.play(ObjectAnimator.ofFloat(rlCard, View.X, rlCard.getWidth(), android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				animationCard.setDuration(200);
				animationCard.setInterpolator(new DecelerateInterpolator());
				animationCard.start();
			}
		});
	}

	/**
	 * 显示答题卡
	 */
	private void showQuestionCard() {
		CardViewForPractice viewForPractice = new CardViewForPractice(mContext, mDatas, this, type, vpSubject);
		viewForPractice.onClickListen(this);
		viewForPractice.onClickListenForError(this);
		viewForPractice.onClickListenForHide(this);
		view = viewForPractice.init();
		view.setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		if (show == 0) {
			showCard(view);
			btnCard.setBackgroundResource(R.drawable.btn_card_p);
			show = 1;
		} else if (show == 1) {
			hideCard();
			btnCard.setBackgroundResource(R.drawable.btn_card_n);
			show = 0;

		}
	}

	@Override
	public void ionClick(int iCurrentQuestion) {
		vpSubject.setCurrentItem(iCurrentQuestion);
		hideCard();
		btnCard.setBackgroundResource(R.drawable.btn_card_n);
		show = 0;
	}

	/**
	 * 全部重做
	 */
	private void redoAllQuestions() {
		for (int i = 0; i < mDatas.size(); i++) {
			TestDataModel.getInstance(mContext).updateAllState(mDatas.get(i).getId(), 0);
			if (mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT || mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_JUDGE
					|| mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT) {
				SubjectModel.getInstance(mContext).cleanUserAnswerAndUscore(Integer.valueOf(mDatas.get(i).getSubjectId()), "", 0, false);
			} else if (mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_ENTRY) {
				String[] s = mDatas.get(i).getSubjectId().split(">>>");
				for (int j = 0; j < s.length; j++) {
					BillDataModel.getInstance(mContext).updateContentS(s[j], "", "", 0, (float) 0);
				}
			} else {
				SubjectBillDataModel.getInstance(mContext).updateContent(Integer.valueOf(mDatas.get(i).getSubjectId()), 0, 0);
				ViewModel.getInstance(mContext).updateContent(Integer.valueOf(mDatas.get(i).getSubjectId()), "", 0);
				EduSqliteDbOprater eso = new EduSqliteDbOprater(mContext);
				eso.deleteUserSign(Integer.parseInt(mDatas.get(i).getSubjectId()));
			}
		}

	}

	// /**
	// * 重做错题
	// */
	// private void redoError() {
	// for (int i = 0; i < mDatas.size(); i++) {
	// if (mDatas.get(i).getState() == 2) {
	// TestDataModel.getInstance(mContext).updateStateAndSendState(mDatas.get(i).getId(),
	// 0, 0);
	// if (mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT
	// || mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_JUDGE
	// || mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT)
	// {
	// SubjectModel.getInstance(mContext).cleanUserAnswerAndUscore(mDatas.get(i).getSubjectId(),
	// "", 0, false);
	// } else if (mDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_ENTRY)
	// {
	// BillDataModel.getInstance(mContext).updateContent(mDatas.get(i).getSubjectId(),
	// "", "", 0, (float) 0);
	// } else {
	// SubjectBillDataModel.getInstance(mContext).updateContent(mDatas.get(i).getSubjectId(),
	// 0, 0);
	// ViewModel.getInstance(mContext).updateContent(mDatas.get(i).getSubjectId(),
	// "", 0);
	// }
	// }
	// }
	// }

	/**
	 * 处理延迟
	 */
	private void delay() {
		loadingDialog = ProgressDialog.show(mContext, "", "正在清除中,请稍等...", false, false);
		// 设置风格为圆形进度条
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.show();
		// if (loadingDialog != null && loadingDialog.isShowing()) {
		// loadingDialog.dismiss();
		// }

		new Thread(new Runnable() {

			@Override
			public void run() {
				spandTimeMethod();

				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	private void spandTimeMethod() {
		try {
			// if (typeClear == 1) {
			redoAllQuestions();
			// } else {
			// redoError();
			// }
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClicksError(View view) {
		// typeClear = 2;
		// delay();
	}

	@Override
	public void onClicks(View view) {
		// typeClear = 1;
		delay();
		hideCard();
		TextInfoDataDao.getInstance(mContext).upDoneFlag(textId, 0);
	}

	@Override
	public void autoJumpNext() {// 自动跳转到下一题
		if (currentIndex != mDatas.size() - 1) {
			currentIndex++;
			vpSubject.setCurrentItem(currentIndex, true);
		}
	}

	@Override
	public void onClicksHide(View view) {
		hideCard();
		show = 0;
	}

	/**
	 * 是否完成答题
	 */
	private void showConfirmDialog(String title) {

		final ConfirmReDoDialog dialog = new ConfirmReDoDialog(mContext, title);
		dialog.setOnRedoClickListener(new OnRedoClickListener() {

			@Override
			public void onOkClicked(View view) {
				startResultActivity();
				finish();
			}

			@Override
			public void onCancelClicked(View view) {
				dialog.dismiss();

			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

}
