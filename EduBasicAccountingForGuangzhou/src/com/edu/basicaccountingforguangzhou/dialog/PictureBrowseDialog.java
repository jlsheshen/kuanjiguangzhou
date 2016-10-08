package com.edu.basicaccountingforguangzhou.dialog;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.util.BitmapParseUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 单据题目图片资料查看对话框
 * 
 * @author lucher
 * 
 */
public class PictureBrowseDialog extends Dialog implements android.view.View.OnClickListener {

	private ScrollView scroll;
	private LinearLayout picContent;
	private Context mContext;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public PictureBrowseDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.dialog_picture_browse);
		mContext = context;

		picContent = (LinearLayout) findViewById(R.id.picContent);
		scroll = (ScrollView) findViewById(R.id.scrollView);
		picContent.setOnClickListener(this);
		this.setCanceledOnTouchOutside(true);
	}

	/**
	 * 设置图片
	 * 
	 * @param pics
	 *            图片uri数组
	 */
	public void setPics(String[] pics) {
		picContent.removeAllViews();
		scroll.scrollTo(0, 0);
		for (String pic : pics) {
			View view = View.inflate(mContext, R.layout.item_picture, null);
			ImageView iv = (ImageView) view.findViewById(R.id.img_pic);
			picContent.addView(view);
			iv.setImageBitmap(BitmapParseUtil.parse(pic, mContext));
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}
}
