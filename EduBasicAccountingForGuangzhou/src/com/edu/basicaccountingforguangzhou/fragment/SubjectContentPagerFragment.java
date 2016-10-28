package com.edu.basicaccountingforguangzhou.fragment;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.subject.ISubject;
import com.edu.basicaccountingforguangzhou.subject.SubjectListener;
import com.edu.basicaccountingforguangzhou.subject.SubjectType;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.SignInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.listener.BillZoomListener;
import com.edu.basicaccountingforguangzhou.subject.bill.listener.SignViewListener;
import com.edu.basicaccountingforguangzhou.subject.bill.view.SignView;
import com.edu.basicaccountingforguangzhou.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.subject.view.GroupBillView;
import com.edu.basicaccountingforguangzhou.subject.view.ZoomableBillView;
import com.edu.basicaccountingforguangzhou.testbill.util.SoundPoolUtil;
import com.edu.basicaccountingforguangzhou.view.AutoJumpNextListener;
import com.edu.basicaccountingforguangzhou.view.BaseScrollView;
//import com.edu.basicaccountingforguangzhou.view.BroadcastReciverViewPager.broadCastReceiveByXml;
import com.edu.basicaccountingforguangzhou.view.FenLuContentView;
import com.edu.basicaccountingforguangzhou.view.FillInContentView;
import com.edu.basicaccountingforguangzhou.view.SubjectJudgeView;
import com.edu.basicaccountingforguangzhou.view.SubjectMultiSelectView;
import com.edu.basicaccountingforguangzhou.view.SubjectSingleSelectView;
import com.edu.basicaccountingforguangzhou.view.SubjectViewListener;
import com.edu.library.util.ToastUtil;






import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * viewpager里存放的界面
 * 
 * @author lucher
 * 
 */
public class SubjectContentPagerFragment extends Fragment implements BillZoomListener, SignViewListener, OnClickListener {

	/**
	 * 题目内容数据
	 */
private TestData mData;
//	private BaseData mData;

	/**
	 * 对应的视图
	 */
	private View mView;

	/**
	 * 答题监听
	 */
	private SubjectViewListener mListener;

	private int testMode;// 测试模式

	// 是否初始化
	private boolean prepared;
	/**
	 * 下一题跳转监听
	 */
	private AutoJumpNextListener mAutoListener;
	
	//避免多次发出广播的type
	int tempType = 1;

	// 用户答案,正确答案
		private Button btnShowUser, btnShowRight;
	
	// 缩放按钮
	private Button btnZoomIn, btnZoomOut;
	
	// 题目视图
	private ISubject subjectView;
	SubjectListener subjectListener;
	
	Context context;
	TextView tvSubjectType;


	/**
	 * 新建实例
	 * 
	 * @param data
	 *            对应数据
	 * @param listener
	 *            题目监听
	 * @param more
	 *            是否有更多题，用于普通模式
	 * @return
	 */
	public static SubjectContentPagerFragment newInstance(TestData data, SubjectListener subjectListener,AutoJumpNextListener autoListener, SubjectViewListener listener, int testMode) {
		SubjectContentPagerFragment fragment = new SubjectContentPagerFragment();
		fragment.mData = data;
		fragment.mListener = listener;
		fragment.mAutoListener = autoListener;
		fragment.testMode = testMode;
		fragment.subjectListener = subjectListener;
		return fragment;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			onVisible();	
		} else {
			onInvisible();
		}
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int delay = 0;
		if (((TestData)mData).getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
			delay = 300;
		}
		mView.postDelayed(new Runnable() {

			@Override
			public void run() {
				onVisible();
			}
		}, delay);

	}

	/**
	 * fragment可见时回调
	 */
	private void onVisible() {
		if (getUserVisibleHint() && prepared && mData.getSubjectType() == SubjectType.SUBJECT_BILL) {
			((ZoomableBillView) subjectView).requestDefaultFocus();
		} else if (getUserVisibleHint() && prepared && mData.getSubjectType() == SubjectType.SUBJECT_GROUP_BILL) {
			((GroupBillView) subjectView).requestDefaultFocus();
		}
	}

	/**
	 * fragment不可见时回调
	 */
	private void onInvisible() {
		Log.e("lucher", "onInvisible " + mData.getId());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("lucher", "onCreateView:" + mData.getId());
		context  = container.getContext();


		
		int type = mData.getSubjectType();// 题型
		switch (type) {

		case Constant.SUBJECT_TYPE_SINGLE_SELECT:
			mView = new SubjectSingleSelectView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((SubjectSingleSelectView) mView).setAutoJumpNextListener(mAutoListener);
			}

			break;
		case Constant.SUBJECT_TYPE_MULTI_SELECT:
			mView = new SubjectMultiSelectView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((BaseScrollView) mView).setAutoJumpNextListener(mAutoListener);
			}

			break;
		case Constant.SUBJECT_TYPE_JUDGE:
			mView = new SubjectJudgeView(context, mData, testMode);
			((BaseScrollView) mView).setSubjectViewListener(mListener);
			if (mAutoListener != null) {
				((BaseScrollView) mView).setAutoJumpNextListener(mAutoListener);
			}
			break;
		case Constant.SUBJECT_TYPE_ENTRY:
			mView = new FenLuContentView(context, mData, testMode);
			// ((BaseScrollView) mView).setSubjectViewListener(mListener);
			break;
		case Constant.SUBJECT_TYPE_BILL:
			
			mView = View.inflate(context, R.layout.fragment_bill, null);
			initBill();

			//subjectView = (ISubject) mView;
//			mView = new FillInContentView(context, mData, testMode);
			subjectView.applyData(mData.getBillData(),testMode);
			
			break;
			case Constant.SUBJECT_GROUP_BILL:
				mView = new GroupBillView(context,  mData.getTestGroupBillData(),testMode);
				// 根据不同的题型创建对应的view
				subjectView = (ISubject) mView;
				subjectView.setSubjectListener(subjectListener);
				break;

		default:
			break;
		}
		prepared = true;

		return mView;
	}

	/**
	 * 重置
	 */
	public void reset(boolean Bill) {
		if( Bill){
			if (subjectView == null) {
				return;
			}
			 subjectView.reset();
		}else{
		if (mView instanceof BaseScrollView) {
			((BaseScrollView) mView).reset();
		} else if (mView instanceof FenLuContentView) {
			((FenLuContentView) mView).reset();
		} else if (mView instanceof ZoomableBillView) {
			
		}}
	}
	
	/**
	 * 提交
	 * 
	 * @return 得分
	 */
	public float submit() {
		return subjectView.submit();

	}


	/**
	 * 保存用户答案
	 */
	public void saveUserAnswer() {
		if (mView instanceof FillInContentView) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					((FillInContentView) mView).saveUserAnswer();
				}
			}).start();
		}
	}

	public void setSignVisbilte() {
		if (mView instanceof FillInContentView) {
			((FillInContentView) mView).showSign();
		}
	}

	public void setPicturesVisbilte() {
		if (mView instanceof FillInContentView) {
			((FillInContentView) mView).showPictures();
		}
	}
	/**
	 * 初始化单据相关控件
	 * 
	 * @throws IOException
	 */
	private void initBill() {
		btnZoomIn = (Button) mView.findViewById(R.id.btnZoomIn);
		btnZoomOut = (Button) mView.findViewById(R.id.btnZoomOut);
		btnZoomIn.setOnClickListener(this);
		btnZoomOut.setOnClickListener(this);

		subjectView = (ZoomableBillView) mView.findViewById(R.id.billView);

		((ZoomableBillView) subjectView).setBillZoomListener(this);
		((ZoomableBillView) subjectView).setSignListener(this);
		
		

		

		if (testMode != 1 ) {
			mView.findViewById(R.id.ly_switch).setVisibility(View.VISIBLE);
			btnShowUser = (Button)mView. findViewById(R.id.btn_show_user);
			btnShowUser.setOnClickListener(this);
			btnShowUser.setHovered(true);
			btnShowRight = (Button)mView. findViewById(R.id.btn_show_right);
		
			tvSubjectType = (TextView) mView.findViewById(R.id.tv_subject_type);
			tvSubjectType.setVisibility(View.VISIBLE);
			
			int errorCount =((SubjectBillData)mData.getBillData().getSubjectData()).getErrorCount();
			Log.e("fragment", "错题次数" + errorCount + "----" + mData.getId());

			tvSubjectType.setText("错误" + errorCount + "次");


			btnShowRight.setOnClickListener(this);
			btnShowRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					subjectView.showDetails();
					btnShowRight.setHovered(true);
					btnShowUser.setHovered(false);
					
				}
			});
			btnShowUser.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mData.getSubjectType() == SubjectType.SUBJECT_BILL) {
						((ZoomableBillView) subjectView).showUserAnswer();
						btnShowUser.setHovered(true);
						btnShowRight.setHovered(false);
					} else {
						ToastUtil.showToast(context, "该题型不支持该操作");
					}					
				}
			});
		}
	}
	
	
	/**
	 * 盖章
	 * 
	 * @param sign
	 *            印章数据
	 */
	public void sign(SignData signData) {
		Log.e("查看sgin专用","fragment--sign"  +  signData.getId());

		if (mData.getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
			SignInfo signInfo = new SignInfo();
			signInfo.setId(signData.getId());
			signInfo.setBitmap(signData.getPic());
			boolean result = ((ZoomableBillView) subjectView).addSignView(signInfo);
			if (!result) {
				// 添加失败处理
			}
		} else {
			ToastUtil.showToast(context, "该题型不支持盖章操作");
		}
	}
	/**
	 * 显示详情
	 */
	public void showDetails() {
		subjectView.showDetails();
	}
	
	
	/**
	 * 显示用户答案
	 */
	public void showUserAnswer() {
		if (mData.getSubjectType() == SubjectType.SUBJECT_BILL) {
			Log.e("TestBillData", "点击获取用户数据" );

			((ZoomableBillView) subjectView).showUserAnswer();
		}  else if (mData.getSubjectType() == SubjectType.SUBJECT_GROUP_BILL) {
			((GroupBillView) subjectView).showUserAnswer();
		} else {
			ToastUtil.showToast(context, "该题型不支持该操作");
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnZoomIn) {
			((ZoomableBillView) subjectView).zoomIn();
		} else if (v.getId() == R.id.btnZoomOut) {
			((ZoomableBillView) subjectView).zoomOut();
		}
	}

	@Override
	public void onDragStart(SignView view) {
	//	subjectView.showDetails();
		ToastUtil.showToast(context, "开始盖章");


		
	}

	@Override
	public void onDragEnd(SignView view) {
		ToastUtil.showToast(context, "盖章结束了");
		SoundPoolUtil.getInstance().init(context).play(getActivity(), SoundPoolUtil.SOUND_SEAL_SUCCESS_ID);
	}

	@Override
	public void onDragHint(SignView view, String msg) {
		ToastUtil.showToast(context, msg);
	}

	@Override
	public void onZoomInit(boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);		
	}

	@Override
	public void onZoomInStart(int currentScaleTimes) {
		//目前为空
	}

	@Override
	public void onZoomInEnd(int currentScaleTimes, boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);		
	}

	@Override
	public void onZoomOutStart(int currentScaleTimes) {
		//目前为空
		
	}

	@Override
	public void onZoomOutEnd(int currentScaleTimes, boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);		
	}

}
