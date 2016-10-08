package com.edu.basicaccountingforguangzhou.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.info.SignInfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SignsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<SignInfo> mList = new ArrayList<SignInfo>();
	private int mPosition = -1;

	public SignsAdapter(Context context, List<SignInfo> list) {
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_sign, null);
		} else {
			view = convertView;
		}

		ImageView sign = (ImageView) view.findViewById(R.id.iv_sign);
		TextView signContent = (TextView) view.findViewById(R.id.tv_sign_content);

		signContent.setText(mList.get(position).getContent());
		try {
			String str = mList.get(position).getSignName();
			int i = str.indexOf(".");
			str = str.substring(0, i);
			str = "sign_" + str + "_small.png";
			System.out.println(str);
			sign.setBackgroundDrawable(Drawable.createFromStream(mContext.getAssets().open("img/" + str), ""));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return view;
	}

	/**
	 * 
	 * 
	 * @param list
	 */
	public void setChoosePosi(int p) {
		mPosition = p;
	}

	public void refreshList(List<SignInfo> list) {
		mList = list;
		notifyDataSetChanged();
	}
}
