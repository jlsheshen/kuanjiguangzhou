package com.edu.basicaccountingforguangzhou.adapter;

import java.util.List;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 基础封装adapter 封装Viewholder
 * 
 * @author wzg
 * @param <T>
 * 
 */
public class BaseAdapters<T> extends BaseAdapter {
	//数据
	private List<T> ltData;

	public BaseAdapters(List<T> ltData) {
		this.ltData = ltData;
	}

	@Override
	public int getCount() {
		return ltData.size();
	}

	@Override
	public Object getItem(int position) {
		return ltData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * 公共的viewholder处理类
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("hiding")
	protected <T extends View>T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
