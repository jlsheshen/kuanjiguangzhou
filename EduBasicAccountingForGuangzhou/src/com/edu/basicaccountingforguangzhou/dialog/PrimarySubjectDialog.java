package com.edu.basicaccountingforguangzhou.dialog;

import java.util.HashMap;
import java.util.List;

import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.adapter.PrimarySubjectAdapter;
import com.edu.basicaccountingforguangzhou.data.FirstSubjectData;
import com.edu.basicaccountingforguangzhou.model.FirstSubjectDataModel;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 一级科目选择对话框
 * 
 * @author lucher
 * 
 */
public class PrimarySubjectDialog extends BaseDialog implements OnCheckedChangeListener, OnItemClickListener {

	/**
	 * 一级科目类型-父科目
	 */
	private static final int SUBJECT_TYPE_PARENT = 1;

	/**
	 * 科目按钮组,存放父科目
	 */
	private RadioGroup rgPSubjects;

	/**
	 * 科目列表，存放子科目
	 */
	private ListView lvSubjects;
	/**
	 * lvSubjects使用的adapter
	 */
	private PrimarySubjectAdapter adapter;

	/**
	 * 一级科目父类数据保存
	 */
	private List<FirstSubjectData> datas;
	/**
	 * 一级科目子类数据保存
	 */
	private List<FirstSubjectData> tmp;

	/**
	 * 保存entryItem点击监听
	 */
	private HashMap<Integer, OnPItemClickListener> listeners;

	/**
	 * 当前对应的entryid
	 */
	private int currentEntryId;

	public PrimarySubjectDialog(Context context) {
		super(context);

		setContentView(R.layout.dialog_primary_subject);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		rgPSubjects = (RadioGroup) this.findViewById(R.id.rg_subjects);
		lvSubjects = (ListView) this.findViewById(R.id.lv_subjects);
		rgPSubjects.setOnCheckedChangeListener(this);

		datas = FirstSubjectDataModel.getInstance(mContext).getDatasByType(SUBJECT_TYPE_PARENT);
		listeners = new HashMap<Integer, OnPItemClickListener>();

		initChildSubjects();
		initParentSubjects();
	}

	/**
	 * 初始化父类科目类别
	 */
	private void initParentSubjects() {
		for (int i = 0; i < datas.size(); i++) {
			FirstSubjectData data = datas.get(i);
			RadioButton rb = (RadioButton) View.inflate(mContext, R.layout.item_p_button, null);
			rb.setText(data.getName());
			rb.setId(data.getId());
			rgPSubjects.addView(rb);
			if (i == 0) {// 默认设置第一个为选中
				rb.setChecked(true);
			}
		}
	}

	/**
	 * 初始化子类科目类别
	 */
	private void initChildSubjects() {
		adapter = new PrimarySubjectAdapter(mContext, null);
		lvSubjects.setAdapter(adapter);
		lvSubjects.setOnItemClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		refreshChildren(checkedId);
	}

	/**
	 * 刷新子类科目
	 * 
	 * @param 待刷新子类的父类
	 */
	private void refreshChildren(int parentId) {
		tmp = FirstSubjectDataModel.getInstance(mContext).getDatasByParentid(parentId);
		adapter.setDatas(tmp);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 设置item点击监听
	 * 
	 * @param item的id
	 * @param listener
	 */
	public void setOnPItemClickListener(int id, OnPItemClickListener listener) {
		listeners.put(id, listener);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		dismiss();
		OnPItemClickListener listener = listeners.get(currentEntryId);
		if (listener != null) {
			listener.onPItemClick(view.getId(), ((TextView) view).getText().toString());
		}
	}

	/**
	 * 显示
	 * 
	 * @param id
	 *            对应的entryid
	 */
	public void show(int id) {
		super.show();
		currentEntryId = id;
	}

	/**
	 * 科目选中监听
	 * 
	 * @author lucher
	 * 
	 */
	public interface OnPItemClickListener {
		/**
		 * 科目点击回调
		 * 
		 * @param id
		 *            对应科目数据库id
		 * @param text
		 *            对应文本
		 */
		void onPItemClick(int id, String text);
	}
}
