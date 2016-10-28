package com.edu.subject.common;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.edu.subject.SubjectType;
import com.edu.subject.common.SubjectCardAdapter.OnCardItemClickListener;
import com.edu.subject.data.BaseTestData;
import com.edu.testbill.R;

/**
 * 答题卡中具体题型对应的表格
 * 
 * @author lucher
 * 
 */
public class CardGridItem {

	// 对应的视图
	private View mView;
	// 题型标签
	private TextView tvTitle;
	// 对应的表格
	private CardGridView cardGrid;

	public CardGridItem(Context context) {
		mView = View.inflate(context, R.layout.card_grid_item, null);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
		cardGrid = (CardGridView) mView.findViewById(R.id.cardGrid);
	}

	/**
	 * 将数据应用到该item上
	 * 
	 * @param type
	 * @param datas
	 * @param listener
	 * @return
	 */
	public View apply(int type, List<BaseTestData> datas, OnCardItemClickListener listener) {
		switch (type) {
		case SubjectType.SUBJECT_BILL:
			tvTitle.setText("单据题");

			break;
		case SubjectType.SUBJECT_BLANK:
			tvTitle.setText("填空题");

			break;
		case SubjectType.SUBJECT_COMPLEX_ENTRY:
			tvTitle.setText("混合式分录题");

			break;
		case SubjectType.SUBJECT_ENTRY:
			tvTitle.setText("分录题");

			break;
		case SubjectType.SUBJECT_JUDGE:
			tvTitle.setText("判断题");

			break;
		case SubjectType.SUBJECT_MULTI:
			tvTitle.setText("多选题");

			break;
		case SubjectType.SUBJECT_SHORT_ANSWER:
			tvTitle.setText("简答题");

			break;
		case SubjectType.SUBJECT_SINGLE:
			tvTitle.setText("单选题");

			break;

		default:
			tvTitle.setText("其他题型");
			break;
		}
		SubjectCardAdapter adapter = new SubjectCardAdapter(mView.getContext(), datas);
		cardGrid.setAdapter(adapter);
		adapter.setCardItemClickListener(listener);

		return mView;
	}

}
