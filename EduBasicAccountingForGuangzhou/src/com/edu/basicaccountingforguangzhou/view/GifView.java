package com.edu.basicaccountingforguangzhou.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

/**
 * 显示gif类图片的view 使用这个类，需要关闭硬件加速功能 <application
 * android:hardwareAccelerated="false"......
 * 
 * @author lucher
 * 
 */
public class GifView extends View {

	// 解析gif的工具类
	private Movie mMovie;
	// 记录开始时间
	private long mMovieStart;
	// 是否循环
	private boolean mCirculation;
	// gif相关监听
	private GifViewListener mListener;

	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 初始化
	 * 
	 * @param gifId
	 *            gif图片res id
	 * @param circulation
	 *            是否循环播放
	 */
	public void init(int gifId, boolean circulation) {
		// 以文件流的形式转换成Movie
		mMovie = Movie.decodeStream(getResources().openRawResource(gifId));
		this.mCirculation = circulation;
	}

	/**
	 * 设置gif监听
	 * 
	 * @param listener
	 */
	public void setGifViewListener(GifViewListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onDraw(Canvas canvas) {
		long now = android.os.SystemClock.uptimeMillis();

		if (mMovieStart == 0) { // 第一次初始化mMovieStart
			mMovieStart = now;
		}

		if (mMovie != null) {
			int dur = mMovie.duration();// gif播放一次的时间
			if (dur == 0) {
				dur = 1000;
			}

			if (mCirculation) {// 循环模式
				int relTime = (int) ((now - mMovieStart) % dur);
				mMovie.setTime(relTime);
				mMovie.draw(canvas, 0, 0);
				invalidate();
			} else {// 非循环模式
				int relTime = (int) (now - mMovieStart);
				if (relTime >= dur) {// 播放完毕
					if (mListener != null)
						mListener.onGifOver(this.getId());
					return;
				} else {
					mMovie.setTime(relTime);
					mMovie.draw(canvas, 0, 0);
					invalidate();
				}
			}
		}

	}

	/**
	 * gif视图监听
	 * 
	 * @author lucher
	 * 
	 */
	public interface GifViewListener {
		/**
		 * gif播放完毕（只限于不循环模式下）
		 * 
		 * @param 注册者的id
		 */
		public void onGifOver(int id);
	}
}