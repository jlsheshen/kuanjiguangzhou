package com.edu.basicaccountingforguangzhou.adapter;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.subject.data.SignData;
import com.edu.basicaccountingforguangzhou.subject.util.BitmapParseUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


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
		sign.setImageBitmap(BitmapParseUtil.parse(mList.get(position).getPic(), mContext, true));
		TextView signContent = (TextView) view.findViewById(R.id.tv_sign_content);
		signContent.setText(mList.get(position).getName());
		Log.e("查看sgin专用", " SignsAdapter." + mList.get(position).getId());

		view.setTag(mList.get(position));

		return view;
	}
}
