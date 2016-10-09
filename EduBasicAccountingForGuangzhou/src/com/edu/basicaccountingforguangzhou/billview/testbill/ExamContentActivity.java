package com.edu.basicaccountingforguangzhou.billview.testbill;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.billview.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.billview.subject.TestMode;
import com.edu.basicaccountingforguangzhou.billview.subject.dao.SignDataDao;
import com.edu.basicaccountingforguangzhou.billview.subject.data.BaseSubjectData;
import com.edu.basicaccountingforguangzhou.billview.subject.data.BaseTestData;
import com.edu.basicaccountingforguangzhou.billview.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.billview.testbill.adapter.SubjectViewPagerAdapter;
import com.edu.basicaccountingforguangzhou.billview.testbill.data.SubjectTestDataDao;
import com.edu.basicaccountingforguangzhou.billview.testbill.dialog.PictureBrowseDialog;
import com.edu.basicaccountingforguangzhou.billview.testbill.dialog.SignChooseDialog;
import com.edu.basicaccountingforguangzhou.billview.testbill.util.SoundPoolUtil;
import com.edu.basicaccountingforguangzhou.data.BaseData;
import com.edu.library.util.DBCopyUtil;

/**
 * 
 * 测试示例
 * 
 * @author lucher
 * 
 */
public class ExamContentActivity extends FragmentActivity implements OnItemClickListener {

	// 显示题目的viewpager控件
	private ViewPager viewPager;
	private SubjectViewPagerAdapter mSubjectAdapter;

	private int mCurrentIndex;
	private TextView tvQuestion;

	// 印章选择对话框
	private SignChooseDialog signDialog;
	// 图片浏览对话框
	private PictureBrowseDialog picBrowseDialog;

	// 页面相关状态的监听
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		// 页面切换后调用
		@Override
		public void onPageSelected(int item) {
			mCurrentIndex = item;
			BaseSubjectData subject = mSubjectAdapter.getData(mCurrentIndex).getSubjectData();
			tvQuestion.setText(mSubjectAdapter.getData(mCurrentIndex).getSubjectIndex() + "." + subject.getQuestion());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int item) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 窗口全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_content);
		init();
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	private void init() {
		// 检测数据库是否已拷贝
		DBCopyUtil fileCopyUtil = new DBCopyUtil(this);
		fileCopyUtil.checkDBVersion(Constant.DATABASE_NAME);
		SoundPoolUtil.getInstance().init(this);

		List<SignData> signs = (List<SignData>) SignDataDao.getInstance(this, Constant.DATABASE_NAME).getAllDatas();
		
		signDialog = new SignChooseDialog(this, signs, this);

		picBrowseDialog = new PictureBrowseDialog(this);

		viewPager = (ViewPager) findViewById(R.id.vp_content);
		viewPager.setOnPageChangeListener(mPageChangeListener);
		tvQuestion = (TextView) findViewById(R.id.tvQuestion);

		List<BaseTestData> datas = SubjectTestDataDao.getInstance(this).getSubjects(TestMode.MODE_EXAM);
		mSubjectAdapter = new SubjectViewPagerAdapter(getSupportFragmentManager(), datas, this);
		viewPager.setAdapter(mSubjectAdapter);
	}

	public void onClick(View view) throws IOException {
		switch (view.getId()) {
		case R.id.btnSign:
			sign();
			break;

		case R.id.btnFlash:
			mSubjectAdapter.showFlash(mCurrentIndex);
			break;

		case R.id.btnPic:
			browsePics();
			break;

		case R.id.btnDone:
			mSubjectAdapter.submit();
			break;

		case R.id.btnLeft:
			if (mCurrentIndex != 0) {
				mCurrentIndex--;
				viewPager.setCurrentItem(mCurrentIndex, true);
			}
			break;

		case R.id.btnRight:
			if (mCurrentIndex != mSubjectAdapter.getCount() - 1) {
				mCurrentIndex++;
				viewPager.setCurrentItem(mCurrentIndex, true);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 显示印章选择对话框
	 */
	private void sign() {
		if (!signDialog.isShowing()) {
			signDialog.show();
		}
	}

	/**
	 * 显示浏览图片对话框
	 */
	private void browsePics() {
		if (!picBrowseDialog.isShowing()) {
			String pic = mSubjectAdapter.getData(mCurrentIndex).getSubjectData().getPic();
			if (pic == null)
				return;
			String[] pics = pic.split(SubjectConstant.SEPARATOR_ITEM);
			picBrowseDialog.setPics(pics);
			picBrowseDialog.show();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (mSubjectAdapter.getCount() > 0) {
			tvQuestion.setText(mSubjectAdapter.getData(mCurrentIndex).getSubjectData().getQuestion());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		signDialog.dismiss();
		mSubjectAdapter.sign(mCurrentIndex, (SignData) view.getTag());
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// ToastUtil.showToast(this, "onKeyDown,keycode:" + keyCode);
	// return super.onKeyDown(keyCode, event);
	// }
}
