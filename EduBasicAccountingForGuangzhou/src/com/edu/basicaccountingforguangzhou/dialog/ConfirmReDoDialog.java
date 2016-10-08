package com.edu.basicaccountingforguangzhou.dialog;

import com.edu.basicaccountingforguangzhou.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmReDoDialog extends BaseDialog implements android.view.View.OnClickListener {
	/**
	 * 继续，重来
	 */
	private Button btnOk, btnCancel;
	// 关卡标签
	private TextView tvLevel;

	// 按钮点击监听
	private OnRedoClickListener mListener;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param level
	 *            关卡
	 */
	public ConfirmReDoDialog(Context context, String level) {
		super(context);
		setContentView(R.layout.dialog_redo);
		init();
		tvLevel.setText(level);
	}

	/**
	 * 初始化
	 */
	private void init() {
		btnOk = (Button) this.findViewById(R.id.btn_ok);
		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		tvLevel = (TextView) this.findViewById(R.id.tv_msg);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	/**
	 * 设置按钮点击监听
	 * 
	 * @param listener
	 */
	public void setOnRedoClickListener(OnRedoClickListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mListener != null) {
			switch (v.getId()) {
			case R.id.btn_ok:
				mListener.onOkClicked(v);
				break;
			case R.id.btn_cancel:
				mListener.onCancelClicked(v);
				break;
			}
		}
	}

	/**
	 * 按钮点击监听
	 * 
	 * @author lucher
	 * 
	 */
	public interface OnRedoClickListener {
		/**
		 * 继续按钮点击
		 * 
		 * @param id
		 */
		public void onOkClicked(View view);

		/**
		 * 重来按钮点击
		 * 
		 * @param id
		 */
		public void onCancelClicked(View view);

	}
}
