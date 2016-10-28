package com.edu.subject.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.edu.library.util.ToastUtil;
import com.edu.subject.ISubject;
import com.edu.subject.SubjectListener;
import com.edu.subject.bill.element.info.SignInfo;
import com.edu.subject.bill.listener.BillZoomListener;
import com.edu.subject.bill.listener.SignViewListener;
import com.edu.subject.bill.view.SignView;
import com.edu.subject.bill.view.ZoomableBillView;
import com.edu.subject.data.BaseTestData;
import com.edu.subject.data.SignData;
import com.edu.subject.data.TestBillData;
import com.edu.testbill.R;
import com.edu.testbill.util.SoundPoolUtil;

/**
 * 单据题型视图
 * 
 * @author lucher
 * 
 */
public class BillView extends RelativeLayout implements ISubject, BillZoomListener, SignViewListener, OnClickListener {

	// 缩放按钮
	private Button btnZoomIn, btnZoomOut;
	private Context mContext;
	private ZoomableBillView billView;
	// 单据相关数据
	private TestBillData mData;

	public BillView(Context context, TestBillData data) {
		super(context);
		View.inflate(context, R.layout.layout_bill_view, this);
		mContext = context;
		mData = data;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		btnZoomIn = (Button) findViewById(R.id.btnZoomIn);
		btnZoomOut = (Button) findViewById(R.id.btnZoomOut);
		btnZoomIn.setOnClickListener(this);
		btnZoomOut.setOnClickListener(this);
		billView = (ZoomableBillView) findViewById(R.id.billView);
		billView.setBillZoomListener(this);
		billView.setSignListener(this);
		applyData(mData);
	}

	/**
	 * 获取测试数据
	 * @return
	 */
	public TestBillData getTestData() {
		return mData;
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnZoomIn) {
			billView.zoomIn();
		} else if (view.getId() == R.id.btnZoomOut) {
			billView.zoomOut();
		}
	}

	/**
	 * 盖章操作
	 * 
	 * @param signData
	 */
	public void sign(SignData signData) {
		SignInfo signInfo = new SignInfo();
		signInfo.setId(signData.getId());
		signInfo.setBitmap(signData.getPic());
		boolean result = billView.addSignView(signInfo);
		if (!result) {
			// 添加失败处理
		}
	}

	/**
	 * 显示闪电符
	 */
	public void showFlashes() {
		billView.showFlashes();
	}

	/**
	 * 显示用户答案
	 */
	public void showUserAnswer() {
		billView.showUserAnswer();
	}

	@Override
	public void applyData(BaseTestData data) {
		billView.applyData(mData);
	}

	@Override
	public void saveAnswer() {
		billView.saveAnswer();
	}

	@Override
	public float submit() {
		return billView.submit();
	}

	@Override
	public void showDetails() {
		billView.showDetails();
	}

	@Override
	public void reset() {
		billView.reset();
	}

	@Override
	public void setSubjectListener(SubjectListener listener) {
		billView.setSubjectListener(listener);
	}

	/**
	 * 获取默认焦点
	 */
	public void requestDefaultFocus() {
		billView.requestDefaultFocus();
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
		SoundPoolUtil.getInstance().play((Activity) mContext, SoundPoolUtil.SOUND_SEAL_SUCCESS_ID);
	}

	@Override
	public void onDragHint(SignView view, String msg) {
		ToastUtil.showToast(mContext, msg);
	}
}
