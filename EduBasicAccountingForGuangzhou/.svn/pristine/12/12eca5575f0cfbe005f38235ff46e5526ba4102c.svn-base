package com.edu.basicaccountingforguangzhou.dialog;

import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.adapter.FillInPicAdapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ListView;

/**
 * 测试范围选择对话框对话框
 * 
 * @author lucher
 * 
 */
public class FillInPictureDialog extends Dialog {

	private ListView listView;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public FillInPictureDialog(Context context, List<String> datas) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.layout_fill_in_popwindows);
		listView = (ListView) findViewById(R.id.lv_pic_popwindows);
		FillInPicAdapter fillInPicAdapter = new FillInPicAdapter(context, datas);
		listView.setAdapter(fillInPicAdapter);
	}

}
