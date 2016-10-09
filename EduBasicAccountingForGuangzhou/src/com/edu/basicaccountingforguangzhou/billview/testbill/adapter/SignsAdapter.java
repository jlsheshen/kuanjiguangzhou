package com.edu.basicaccountingforguangzhou.billview.testbill.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.billview.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.billview.subject.util.BitmapParseUtil;

/**
 * 印章选择表格的adapter
 * 
 * @author lucher
 * 
 */
public class SignsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<SignData> mList = new ArrayList<SignData>();

	public SignsAdapter(Context context, List<SignData> list) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mList = list;
	}

	@Override
	public int getCount() {

		return mList.size();

	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);

	}

	@Override
	public long getItemId(int position) {

		return position;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_sign, null);
		} else {
			view = convertView;
		}

		ImageView sign = (ImageView) view.findViewById(R.id.iv_sign);
		sign.setImageBitmap(BitmapParseUtil.parse(mList.get(position).getPic(), mContext));
		TextView signContent = (TextView) view.findViewById(R.id.tv_sign_content);
		signContent.setText(mList.get(position).getName());
		view.setTag(mList.get(position));

		return view;
	}
}
