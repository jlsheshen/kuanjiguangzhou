package com.edu.basicaccountingforguangzhou;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.data.TextInfoData;
import com.edu.basicaccountingforguangzhou.data.TextInfoDataDao;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.view.BaseScrollView;
import com.edu.basicaccountingforguangzhou.view.TextItemFirstView;
import com.edu.basicaccountingforguangzhou.view.TextItemView;
import com.edu.library.EduBaseActivity;
import com.edu.library.util.DBCopyUtil;
import com.edu.library.util.DoubleClickExitUtil;
import com.edu.library.util.ToastUtil;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends EduBaseActivity implements OnClickListener {
	private List<TestData> mDatas;// 存放所有的数据
	private int show = 0;// 题型及分数设置
	private ImageView imgInfo;
	private Button btnTextInfo;
	private List<TextInfoData> infoDatas;// 套题信息
	private LinearLayout ly;
	private int lastTextId;// 上一次进入的那套试题id
	private int currentIndex;// 上一次退出的位置
	private int pos;// 当前题

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 窗口全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		// 数据库检测
		DBCopyUtil copyUtil = new DBCopyUtil(mContext);
		copyUtil.checkDBVersion(Constant.DATABASE_NAME);
		initView();
		initData();
		addButton();
	}

	@Override
	protected void initData() {
		getAllData(MainActivity.this);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			lastTextId = bundle.getInt("textId");
	//	Log.e("MainActivity", "textId" + lastTextId);
			currentIndex = bundle.getInt("index");
		}

	}

	@Override
	protected void initView() {
		btnTextInfo = (Button) findViewById(R.id.btn_text_info);
		this.findViewById(R.id.btn_set).setOnClickListener(this);
		btnTextInfo.setOnClickListener(this);
		imgInfo = (ImageView) findViewById(R.id.img_info);
		ly = (LinearLayout) findViewById(R.id.ly_item);
	}

	// 添加button
	public void addButton() {
		ly.removeAllViews();
		for (int i = 0; i < infoDatas.size(); i++) {
			if (i == 0) {
				TextItemFirstView button = new TextItemFirstView(MainActivity.this, infoDatas.get(i));
				ly.addView(button);
				button.refreshButton(infoDatas.get(i));
				button.setTag(infoDatas.get(i));
				button.setOnClickListener(btnLevelClickListener);
			} else {
				TextItemView button = new TextItemView(MainActivity.this, infoDatas.get(i));
				ly.addView(button);
				button.refreshButton(infoDatas.get(i));
				button.setTag(infoDatas.get(i));
				button.setOnClickListener(btnLevelClickListener);
			}

		}

	}

	/**
	 * 获得levelInfo表的所有数据
	 */
	public void getAllData(Context context) {
		infoDatas = new ArrayList<TextInfoData>();
		infoDatas = TextInfoDataDao.getInstance(mContext).getInfoDatas();
	}

	private void setTagInfo() {
		getAllData(MainActivity.this);
		for (int i = 0; i < infoDatas.size(); i++) {

			if (i == 0) {
				TextItemFirstView button = (TextItemFirstView) ly.getChildAt(i);
				button.setTag(infoDatas.get(i));
			} else {
				TextItemView button = (TextItemView) ly.getChildAt(i);
				button.setTag(infoDatas.get(i));
			}

		}
	}

	OnClickListener btnLevelClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTagInfo();
			TextInfoData levelInfoData = (TextInfoData) v.getTag();
			if (levelInfoData.getLock() == Constant.STATE_ENABLE) {
				// if (levelInfoData.getId() == 1) {
				testFirst(levelInfoData.getId());
				// } else if (levelInfoData.getId() == 2) {
				// testSecond(levelInfoData.getId());
				// } else if (levelInfoData.getId() == 3) {
				// testThree(levelInfoData.getId());
				// } else if (levelInfoData.getId() == 4) {
				// testFour(levelInfoData.getId());
				// } else if (levelInfoData.getId() == 5) {
				// testFive(levelInfoData.getId());
				// }
			} else {
				ToastUtil.showToast(mContext, "“您需要先通过试卷" + (levelInfoData.getId() - 1) + "才能解锁哦~”");
			}

		}
	};

	/**
	 * 启动activity
	 * 
	 * @param data
	 * @param pos
	 */
	private void startActivity(TestData data, int pos, int textId) {
		Bundle bundle = new Bundle();
		if (TextInfoDataDao.getInstance(mContext).querState(textId) == 1) {
			bundle.putInt("testType", BaseScrollView.TEST_MODE_TEST);
		} else {
			bundle.putInt("testType", BaseScrollView.TEST_MODE_NORMAL);
		}
		bundle.putInt("index", pos);
		bundle.putInt("textId", textId);
		//bundle.putSerializable("datas", (Serializable) mDatas);
		startActivity(SubjectTestActivity.class, bundle);
		this.finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.btn_text_info:
			showTextInfoCard();
			break;

		case R.id.btn_set:
			ToastUtil.showToast(mContext, "敬请期待哦！");
			break;

		default:
			break;
		}
	}

	/**
	 * 显示答题卡
	 */
	private void showTextInfoCard() {
		if (show == 0) {
			imgInfo.setVisibility(View.VISIBLE);
			btnTextInfo.setBackgroundResource(R.drawable.btn_type_p);
			show = 1;
		} else if (show == 1) {
			imgInfo.setVisibility(View.GONE);
			btnTextInfo.setBackgroundResource(R.drawable.btn_type_n);
			show = 0;
		}
	}

	@Override
	public void onBackPressed() {
		DoubleClickExitUtil.doubleClickExit(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		addButton();
	}

	// 进入第几套试题
	private void testFirst(int textId) {
		mDatas = TestDataModel.getInstance(mContext).getDatas(textId, 2);
		if (textId == lastTextId) {
			pos = currentIndex;
		} else {
			pos = 0;
		}
		if (mDatas != null && mDatas.size() > 0) {
			startActivity(mDatas.get(pos), pos, textId);
		}
		imgInfo.setVisibility(View.GONE);
		btnTextInfo.setBackgroundResource(R.drawable.btn_type_n);
		show = 0;
	}

}
