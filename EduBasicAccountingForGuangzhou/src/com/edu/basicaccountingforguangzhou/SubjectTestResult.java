package com.edu.basicaccountingforguangzhou;

import java.io.Serializable;
import java.util.List;

import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.SubjectEntryData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.data.TextInfoDataDao;
import com.edu.basicaccountingforguangzhou.model.TestDataModel;
import com.edu.basicaccountingforguangzhou.view.BaseScrollView;
import com.edu.library.EduBaseActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectTestResult extends EduBaseActivity implements OnClickListener {
	private List<TestData> subjectDatas;// 考试数据
	private TextView textView;// 显示分数
	private Button btnBack;// 返回主界面
	private Button btnLook;// 查看答案
	private int textId;
	private Typeface tf;// 字体
	private ImageView img;// 显示是否通过图片
	private TextView tvTime;// 答题时间
	private TextView tvName;
	private int totalSores = 0;// 本套试卷总分

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 窗口全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_test_result);
		initView();
		initData();
	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			subjectDatas = (List<TestData>) bundle.getSerializable("datas");
			textId = bundle.getInt("textId");
		}
		// if (subjectDatas != null) {
		// for (TestData testData : subjectDatas) {
		// if (testData.getState() == 1) {
		// score = score + 2;
		// }
		// }
		// }
		computeGrade();
		tf = Typeface.createFromAsset(getAssets(), "STXINGKA.TTF");
		textView.setTypeface(tf);
		textView.setText(totalSores + "");
		if (totalSores < 60) {
			textView.setTextColor(this.getResources().getColor(R.color.red));
			img.setBackgroundResource(R.drawable.img_text_fail);
		} else {
			textView.setTextColor(this.getResources().getColor(R.color.green));
			img.setBackgroundResource(R.drawable.img_text_over);
		}
		TextInfoDataDao.getInstance(mContext).insertScore(textId, totalSores);
		tvTime.setText(Constant.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		if (textId == 1) {
			tvName.setText("基础会计试卷一");
		} else if (textId == 2) {
			tvName.setText("基础会计试卷二");
		} else if (textId == 3) {
			tvName.setText("基础会计试卷三");
		} else if (textId == 4) {
			tvName.setText("基础会计试卷四");
		} else if (textId == 5) {
			tvName.setText("基础会计试卷五");
		}
		unLock();
	}

	@Override
	protected void initView() {
		textView = (TextView) findViewById(R.id.tv_score);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnLook = (Button) findViewById(R.id.btn_look);
		img = (ImageView) findViewById(R.id.img_toast);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvName = (TextView) findViewById(R.id.tv_name);
		btnBack.setOnClickListener(this);
		btnLook.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(MainActivity.class);
			finish();
			break;

		case R.id.btn_look:
			// startActivity(SubjectTestActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("testType", BaseScrollView.TEST_MODE_TEST);
			bundle.putInt("index", 0);
			bundle.putInt("textId", textId);
			bundle.putSerializable("datas", (Serializable) subjectDatas);
			startActivity(SubjectTestActivity.class, bundle);
			finish();
			break;

		default:
			break;
		}

	}

	// 解锁下一张考试（得分大于60才解锁下一关）
	private void unLock() {
		if (textId >= 1 && textId < 5 && totalSores >= 60 && TextInfoDataDao.getInstance(mContext).querState(textId + 1) == 0) {
			// TextInfoDataDao.getInstance(mContext).upLockFlag(textId + 1);
		}
		TextInfoDataDao.getInstance(mContext).upDoneFlag(textId, 1);
	}

	/**
	 * 计算本章节成绩
	 */
	private void computeGrade() {
		if (subjectDatas != null) {
			for (int i = 0; i < subjectDatas.size(); i++) {
				// 单选、判断
				if (subjectDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT || subjectDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_JUDGE) {
					if (((SubjectBasicData) subjectDatas.get(i).getData()).isRight()) {
						totalSores = (int) (totalSores + ((SubjectBasicData) subjectDatas.get(i).getData()).getScore());
					}
				} else if (subjectDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT) {// 多选
					if (((SubjectBasicData) subjectDatas.get(i).getData()).isRight()) {
						totalSores = (int) (totalSores + ((SubjectBasicData) subjectDatas.get(i).getData()).getScore());
					}
				} else if (subjectDatas.get(i).getSubjectType() == Constant.SUBJECT_TYPE_ENTRY) {// 分录
					if (((SubjectEntryData) subjectDatas.get(i).getData()).isCorrect()) {
						totalSores = (int) (totalSores + ((SubjectEntryData) subjectDatas.get(i).getData()).getScore());
					} else {
						if (subjectDatas.get(i).getState() != 0) {
							subjectDatas.get(i).setErrorCount(subjectDatas.get(i).getErrorCount() + 1);
							TestDataModel.getInstance(mContext).updateStateAndErrorCount(subjectDatas.get(i).getId(), 2, subjectDatas.get(i).getErrorCount());
						}
					}

				} else {// 填制
//					if (( (SubjectBillData) subjectDatas.get(i).getBillData().getSubjectData()).getSCORE() == ((SubjectBillData) subjectDatas.get(i).getBillData().getSubjectData()).getSCORE()) {
//						totalSores = (int) (totalSores + ((SubjectBillData) subjectDatas.get(i).getBillData().getSubjectData()).getSCORE());
//					} else {
//						if (subjectDatas.get(i).getState() != 0) {
//							totalSores = (int) (totalSores + ((SubjectBillData) subjectDatas.get(i).getBillData().getSubjectData()).getSCORE());
//							subjectDatas.get(i).setErrorCount(subjectDatas.get(i).getErrorCount() + 1);
//							TestDataModel.getInstance(mContext).updateStateAndErrorCount(subjectDatas.get(i).getId(), 2, subjectDatas.get(i).getErrorCount());
//						}
//					}
					totalSores = (int) (totalSores + (subjectDatas.get(i).getBillData().getuScore()));

				}
			}
		} else {
			return;
		}
	}

}
