package com.edu.basicaccountingforguangzhou.view;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.adapter.CardAdapterForPractice;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.dialog.ConfirmReDoDialog;
import com.edu.basicaccountingforguangzhou.dialog.ConfirmReDoDialog.OnRedoClickListener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 答题卡视图
 * 
 * 
 */
public class CardViewForPractice {
	private List<TestData> datas;
	private int testModes;// 考试模式
	// 点击监听回调
	private IOnClickListener oListener;
	// * 答题题干数据
	private String[] strStem;
	// 科目列表布局顶层容器
	private View mView;
	private Context mContext;
	private ViewPager vpSubjec;
	private onClickListeners listeners;
	private onClickListenersForError listenerError;
	private onClickListenersForHide forHide;

	public CardViewForPractice(Context context, List<TestData> data, IOnClickListener oListener, int testMode, ViewPager vpSubject) {
		this.datas = data;
		this.oListener = oListener;
		this.testModes = testMode;
		mContext = context;
		vpSubjec = vpSubject;
	}

	public View init() {
		mView = View.inflate(mContext, R.layout.view_card_practice, null);
		strStem = mContext.getResources().getStringArray(R.array.question_practise);
		LinearLayout llyout = (LinearLayout) mView.findViewById(R.id.llyout_card);
		Button btnAllRedo = (Button) mView.findViewById(R.id.btn_redo);
		// Button btnErrorRedo = (Button)
		// mView.findViewById(R.id.btn_redo_error);
		// Button btnHide = (Button) mView.findViewById(R.id.btn_hide);
		// if(testModes== Constant.TEST_TYPE_EXERCIZE){
		// if(Constant.IS_SUCCUESS){//test by xu
		// btnErrorRedo.setVisibility(View.VISIBLE);
		// }else{
		// btnErrorRedo.setVisibility(View.GONE);
		// }
		// }else{
		// btnErrorRedo.setVisibility(View.VISIBLE);
		// }
		btnAllRedo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (listeners != null) {
				// listeners.onClicks(v);
				// }
				showRedoDialog("全部重做将会清除之前的答题记录哦，确定全部重做吗？");
			}
		});
		// btnErrorRedo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(listenerError!=null){
		// listenerError.onClicksError(v);
		// }
		// }
		// });
		// btnHide.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (forHide != null) {
		// forHide.onClicksHide(v);
		// }
		// }
		// });
		for (int i = 1; i < strStem.length; i++) {// 1到5为题目类型
			View view = View.inflate(mContext, R.layout.llyout_card_item_practise, null);
			llyout.addView(view);
			TextView tvTilte = (TextView) view.findViewById(R.id.tv_itcard_child_title);
			List<TestData> dataClass = new ArrayList<TestData>();
			if (i == 1) {
				for (int j = 0; j < datas.size(); j++) {
					if (datas.get(j).getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT) {
						dataClass.add(datas.get(j));
						tvTilte.setText("【" + strStem[i] + "】");
					}
				}
			} else if (i == 2) {
				for (int j = 0; j < datas.size(); j++) {
					if (datas.get(j).getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT) {
						dataClass.add(datas.get(j));
						tvTilte.setText("【" + strStem[i] + "】");
					}
				}
			} else if (i == 3) {
				for (int j = 0; j < datas.size(); j++) {
					if (datas.get(j).getSubjectType() == Constant.SUBJECT_TYPE_JUDGE) {
						dataClass.add(datas.get(j));
						tvTilte.setText("【" + strStem[i] + "】");
					}
				}
			} else if (i == 4) {
				for (int j = 0; j < datas.size(); j++) {
					if (datas.get(j).getSubjectType() == Constant.SUBJECT_TYPE_ENTRY || datas.get(j).getSubjectType() == Constant.SUBJECT_TYPE_BILL||datas.get(j).getSubjectType() == Constant.SUBJECT_GROUP_BILL) {
						dataClass.add(datas.get(j));
						tvTilte.setText("【" + strStem[i] + "】");
					}
				}
			}
			if (dataClass.size() == 0) {
				// view = null;
				llyout.removeView(view);// 移除对应的view，否则布局混乱
				continue;
			}
			CardGridView gridView = (CardGridView) view.findViewById(R.id.gv_itcard_child_choice);

			gridView.setAdapter(new CardAdapterForPractice(mContext, dataClass, oListener, testModes, datas));
		}
		llyout.getChildAt(0).requestFocus();
		return mView;

	}

	public interface onClickListeners {
		/**
		 * 全部重做
		 * 
		 * @param view
		 */
		void onClicks(View view);
	}

	public void onClickListen(onClickListeners clickListeners) {
		listeners = clickListeners;
	}

	public interface onClickListenersForError {
		/**
		 * 全部重做
		 * 
		 * @param view
		 */
		void onClicksError(View view);
	}

	public void onClickListenForError(onClickListenersForError clickListenerss) {
		listenerError = clickListenerss;
	}

	public interface onClickListenersForHide {
		/**
		 * 全部重做
		 * 
		 * @param view
		 */
		void onClicksHide(View view);
	}

	public void onClickListenForHide(onClickListenersForHide clickListenerss) {
		forHide = clickListenerss;
	}

	/**
	 * 显示是否重做全部答题卡记录
	 */
	private void showRedoDialog(String title) {

		final ConfirmReDoDialog dialog = new ConfirmReDoDialog(mContext, title);
		dialog.setOnRedoClickListener(new OnRedoClickListener() {

			@Override
			public void onOkClicked(View view) {
				if (listeners != null) {
					listeners.onClicks(view);
				}

			}

			@Override
			public void onCancelClicked(View view) {
				dialog.dismiss();

			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

}
