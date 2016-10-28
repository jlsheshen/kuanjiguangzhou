package com.edu.subject.common;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.edu.library.util.ToastUtil;
import com.edu.subject.ISubject;
import com.edu.subject.SubjectListener;
import com.edu.subject.SubjectType;
import com.edu.subject.bill.element.info.SignInfo;
import com.edu.subject.bill.listener.BillZoomListener;
import com.edu.subject.bill.listener.SignViewListener;
import com.edu.subject.bill.view.SignView;
import com.edu.subject.data.BaseTestData;
import com.edu.subject.data.SignData;
import com.edu.subject.view.SingleSelectView;
import com.edu.subject.view.ZoomableBillView;
import com.edu.testbill.R;
import com.edu.testbill.util.SoundPoolUtil;

/**
 * viewpager里存放的fragment
 * 
 * @author lucher
 * 
 */
public class SubjectViewPagerFragment extends Fragment implements BillZoomListener, SignViewListener, OnClickListener {

	/**
	 * 题目内容数据
	 */
	private BaseTestData mData;
	// 题目视图
	private ISubject subjectView;

	/**
	 * 对应的视图
	 */
	private View mView;

	// 是否初始化
	private boolean prepared;

	// 缩放按钮
	private Button btnZoomIn, btnZoomOut;

	private Context mContext;
	private SubjectListener mListener;

	/**
	 * 新建实例
	 * 
	 * @param data
	 *            对应数据
	 * @param listener
	 * @return
	 */
	public static SubjectViewPagerFragment newInstance(BaseTestData data, SubjectListener listener) {
		SubjectViewPagerFragment fragment = new SubjectViewPagerFragment();
		fragment.mData = data;
		fragment.mListener = listener;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("lucher", "onCreateView");
		mContext = container.getContext();

		switch (mData.getSubjectType()) {
		case SubjectType.SUBJECT_BILL:
			mView = View.inflate(mContext, R.layout.fragment_bill, null);
			initBill();

			break;

		case SubjectType.SUBJECT_SINGLE:
			mView = new SingleSelectView(mContext, mData);
			subjectView = (ISubject) mView;

			break;

		default:
			break;
		}
		subjectView.applyData(mData);
		subjectView.setSubjectListener(mListener);
		prepared = true;

		return mView;
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
		}
	}

	/**
	 * fragment不可见时回调
	 */
	private void onInvisible() {
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
	}

	@Override
	public void onZoomInit(boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);
	}

	@Override
	public void onZoomInStart(int currentScaleTimes) {
	}

	@Override
	public void onZoomInEnd(int currentScaleTimes, boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);
	}

	@Override
	public void onZoomOutStart(int currentScaleTimes) {
	}

	@Override
	public void onZoomOutEnd(int currentScaleTimes, boolean zoomInEnable, boolean zoomOutEnable) {
		btnZoomIn.setEnabled(zoomInEnable);
		btnZoomOut.setEnabled(zoomOutEnable);
	}

	@Override
	public void onDragStart(SignView view) {
		ToastUtil.showToast(mContext, "开始盖章了");
	}

	@Override
	public void onDragEnd(SignView view) {
		ToastUtil.showToast(mContext, "盖章结束了");
		SoundPoolUtil.getInstance().play(getActivity(), SoundPoolUtil.SOUND_SEAL_SUCCESS_ID);
	}

	@Override
	public void onDragHint(SignView view, String msg) {
		ToastUtil.showToast(mContext, msg);
	}

	/**
	 * 重置
	 */
	public void reset() {
		subjectView.reset();
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
	 * 盖章
	 * 
	 * @param sign
	 *            印章数据
	 */
	public void sign(SignData signData) {
		if (mData.getSubjectType() == SubjectType.SUBJECT_BILL) {
			SignInfo signInfo = new SignInfo();
			signInfo.setId(signData.getId());
			signInfo.setBitmap(signData.getPic());
			boolean result = ((ZoomableBillView) subjectView).addSignView(signInfo);
			if (!result) {
				// 添加失败处理
			}
		} else {
			ToastUtil.showToast(mContext, "该题型不支持盖章操作");
		}
	}

	/**
	 * 显示闪电符
	 * 
	 */
	public void showFlash() {
		if (mData.getSubjectType() == SubjectType.SUBJECT_BILL) {
			((ZoomableBillView) subjectView).showFlashes();
		} else {
			ToastUtil.showToast(mContext, "该题型不支持闪电符操作");
		}

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnZoomIn) {
			((ZoomableBillView) subjectView).zoomIn();
		} else if (view.getId() == R.id.btnZoomOut) {
			((ZoomableBillView) subjectView).zoomOut();
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
			((ZoomableBillView) subjectView).showUserAnswer();
		} else {
			ToastUtil.showToast(mContext, "该题型不支持该操作");
		}
	}
}
