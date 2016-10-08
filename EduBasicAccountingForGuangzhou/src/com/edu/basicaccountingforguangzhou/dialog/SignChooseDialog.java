package com.edu.basicaccountingforguangzhou.dialog;

import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.adapter.SignsAdapter;
import com.edu.basicaccountingforguangzhou.info.SignInfo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

/**
 * 测试范围选择对话框对话框
 * 
 * @author lucher
 * 
 */
public class SignChooseDialog extends Dialog implements android.view.View.OnClickListener {

	// 全选，确定，取消按钮
	private Button btnCancel;

	// 按钮点击监听
	private OnButtonClickListener mListener;

	private Context mContext;

	private GridView gvSing;

	private List<SignInfo> datas;

	private OnItemClickListener itemClickListener;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param type
	 * @param level
	 *            关卡
	 */
	public SignChooseDialog(Context context, List<SignInfo> datas, OnItemClickListener itemClickListener) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog_sign_select);
		mContext = context;
		this.datas = datas;
		this.itemClickListener = itemClickListener;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		gvSing = (GridView) this.findViewById(R.id.gv_sign);
		btnCancel.setOnClickListener(this);
		SignsAdapter signsAdapter = new SignsAdapter(mContext, datas);
		gvSing.setAdapter(signsAdapter);
		gvSing.setOnItemClickListener(itemClickListener);
	}

	/**
	 * 设置按钮点击监听
	 * 
	 * @param listener
	 */
	public void setOnButtonClickListener(OnButtonClickListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		case R.id.btn_cancel:
			this.dismiss();

			break;

		default:
			break;
		}
	}

	/**
	 * 按钮点击监听
	 * 
	 * @author lucher
	 * 
	 */
	public interface OnButtonClickListener {

		/**
		 * 点击取消
		 */
		public void onCancelClicked();

	}
}
