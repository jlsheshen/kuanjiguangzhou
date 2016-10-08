package com.edu.basicaccountingforguangzhou.view;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.TextInfoData;
import com.edu.basicaccountingforguangzhou.util.RotateTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextItemFirstView extends RelativeLayout {
	private Context mContext;
	private RotateTextView tvTitle;// 标题
	private ImageView imgUnfinish;// 没有完成
	private RelativeLayout rlFinish;// 完成
	private TextView tvScore;// 分数
	private TextInfoData data;
	private Typeface tf;// 字体

	public TextItemFirstView(Context context, TextInfoData textInfoData) {
		super(context);
		mContext = context;
		data = textInfoData;
		initWidget();

	}

	private void initWidget() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_book_one, this);
		tvTitle = (RotateTextView) this.findViewById(R.id.tv_title_one);
		imgUnfinish = (ImageView) this.findViewById(R.id.img_unfinished_one);
		rlFinish = (RelativeLayout) this.findViewById(R.id.rl_finish_one);
		tvScore = (TextView) this.findViewById(R.id.tv_score_one);
		tf = Typeface.createFromAsset(mContext.getAssets(), "STXINGKA.TTF");

		this.setFocusable(true);
		this.setClickable(true);
	}

	/**
	 * 设置试题名称
	 */
	private void setTextName(String title) {
		tvTitle.setText(title + "");
	}

	/**
	 * 设置未完成、完成,显示分数
	 */
	private void setfinish(int score, int done) {
		if (done == 1) {
			rlFinish.setVisibility(View.VISIBLE);
			tvScore.setText(score + "");
			tvScore.setTypeface(tf);
		} else if (done == 2) {
			imgUnfinish.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 刷新按钮的UI
	 *
	 * @param levelInfoEntity
	 */
	public void refreshButton(TextInfoData mtextInfoData) {
		data = mtextInfoData;
		setTextName(data.getName());
		setfinish(data.getScore(), data.getDone());
	}

}
