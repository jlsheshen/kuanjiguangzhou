package com.edu.basicaccountingforguangzhou.adapter;

import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.library.util.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 填制题的图片adapter
 * 
 * @author WZG
 *
 */
public class FillInPicAdapter extends BaseAdapter {
	private List<String> mPicList;
	private Context mContext;

	public FillInPicAdapter(Context context, List<String> picList) {
		this.mContext = context;
		this.mPicList = picList;
	}

	@Override
	public int getCount() {
		return mPicList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fillin_picture, null);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.img_pic);
			if (ImageUtil.getImageResId(mContext, mPicList.get(position)) != 0) {
				imageView.setBackgroundDrawable(mContext.getResources().getDrawable(ImageUtil.getImageResId(mContext, mPicList.get(position))));
			}
		}
		return convertView;
	}
}