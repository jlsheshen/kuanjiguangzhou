package com.edu.basicaccountingforguangzhou.billview.subject.bill.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.edu.basicaccountingforguangzhou.billview.subject.bill.element.ElementLayoutParams;
import com.edu.basicaccountingforguangzhou.billview.subject.bill.element.info.SignInfo;
import com.edu.basicaccountingforguangzhou.billview.subject.bill.scale.IScaleable;
import com.edu.basicaccountingforguangzhou.billview.subject.bill.scale.ScaleUtil;

/**
 * 印章视图,支持缩放
 * 
 * @author lucher
 * 
 */
public class SignView extends ImageView implements IScaleable {

	private static final String TAG = "SignView";
	// 对应印章的信息
	private SignInfo mData;

	private DragListener mListener;

	public SignView(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		setScaleType(ScaleType.FIT_XY);
	}

	/**
	 * 设置印章对应数据，并把数据应用到该印章上
	 * 
	 * @param data
	 *            数据
	 * @param scale
	 *            缩放比例
	 * @return 印章是否合法
	 */
	public boolean apply(SignInfo data, float scale) {
		mData = data;
		if (data.getBitmap() == null) {
			return false;
		}
		setImageBitmap(data.getBitmap());
		postScale(scale, 0);

		return true;
	}

	/**
	 * 获取对应空数据
	 * 
	 * @return
	 */
	public SignInfo getData() {
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
	}

	/**
	 * 印章拖动
	 * 
	 * @param dx
	 *            x方向的距离
	 * @param dy
	 *            y方向的距离
	 * @param scale
	 *            缩放比例
	 */
	public void move(float dx, float dy, float scale) {
		ElementLayoutParams params = (ElementLayoutParams) getLayoutParams();
		params.setX(Math.round(params.getX() - dx));
		params.setY(Math.round(params.getY() - dy));

		mData.setX(mData.getX() - dx / scale);
		mData.setY(mData.getY() - dy / scale);
		Log.e(TAG, "x:" + mData.getX() + ",y:" + mData.getY());
		Log.d(TAG, "x:" + params.getX() + ",y:" + params.getY());

		requestLayout();
	}

	/**
	 * 设置为等待拖拽状态
	 */
	public void setWaitingState() {
		// 动画方式显示
		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, 5f, 1f)).with(ObjectAnimator.ofFloat(this, View.SCALE_Y, 5f, 1f)).with(ObjectAnimator.ofFloat(this, View.ALPHA, 0, 1f));
		set.setInterpolator(new BounceInterpolator());
		set.setDuration(300).start();
	}

	/**
	 * 开始拖拽，主要处理拖拽时印章的变化
	 */
	public void startDrag() {
		// 动画方式拖拽
		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, 0.9f, 1.1f, 1.0f)).with(ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.9f, 1.1f, 1.0f))
				.with(ObjectAnimator.ofFloat(this, View.ALPHA, 1, 0.5f));
		set.setInterpolator(new AnticipateInterpolator());
		set.setDuration(300).start();
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mListener != null) {
					mListener.onDragStart();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
	}

	/**
	 * 结束拖拽，主要是让印章恢复初始的状态
	 */
	public void endDrag() {
		setAlpha(0xff);
	}

	/**
	 * 动画的方式显示，模拟盖章动画
	 * 
	 * @param billView
	 *            单据视图，盖章的时候配合发生改变
	 */
	public void animateShow(View billView) {
		// 动画方式盖章
		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, 1.1f, 0.9f, 1.0f).setDuration(300)).with(ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.1f, 0.9f, 1.0f).setDuration(300))
				.before(ObjectAnimator.ofFloat(billView, View.ALPHA, 0.8f, 0.9f, 1f).setDuration(200));
		set.setInterpolator(new AnticipateInterpolator());
		set.start();
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mListener != null) {
					mListener.onDragEnd();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
	}

	/**
	 * 提交
	 */
	public void submit() {
		saveAnswer();
		judgeAnswer();
		showAnswer(true);
	}

	/**
	 * 保存用户答案
	 */
	public void saveAnswer() {
	}

	/**
	 * 判断用户答案
	 */
	public void judgeAnswer() {
	}

	/**
	 * 显示答案
	 * 
	 * @param user
	 *            true-显示用户答案，false-显示正确答案
	 */
	public void showAnswer(boolean user) {
		if (user && mData.isUser()) {
			setVisibility(View.VISIBLE);
		} else if (!user && !mData.isUser()) {
			setVisibility(View.VISIBLE);
		} else {
			setVisibility(View.GONE);
		}
	}

	/**
	 * 设置拖拽监听
	 * 
	 * @param listener
	 */
	public void setOnDragListener(DragListener listener) {
		mListener = listener;
	}

	/**
	 * 拖拽接口
	 * 
	 * @author lucher
	 * 
	 */
	public interface DragListener {
		/**
		 * 拖拽开始，拖拽启动动画结束后调用
		 */
		void onDragStart();

		/**
		 * 拖拽结束，盖章动画结束后调用
		 */
		void onDragEnd();
	}
}
