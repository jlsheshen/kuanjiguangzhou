package com.edu.basicaccountingforguangzhou.adapter;

import java.util.List;

import com.edu.basicaccountingforguangzhou.Constant;
import com.edu.basicaccountingforguangzhou.R;
import com.edu.basicaccountingforguangzhou.data.BaseSubjectData;
import com.edu.basicaccountingforguangzhou.data.SubjectBasicData;
import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.data.SubjectEntryData;
import com.edu.basicaccountingforguangzhou.data.TestData;
import com.edu.basicaccountingforguangzhou.subject.data.TestGroupBillData;
import com.edu.basicaccountingforguangzhou.view.BaseScrollView;
import com.edu.basicaccountingforguangzhou.view.IOnClickListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 答题卡显示
 * 
 * @author WZG
 * 
 */
public class CardAdapterForPractice extends BaseAdapters {
	private static final String TAG = "CardAdapterForPractice";
	// * context
	private Context context;
	// * 数据
	private List<TestData> data;
	// * 自定义监听类
	private IOnClickListener iOnClickListener;
	private int testModes;// 考试模式
	private List<TestData> allDatas;
	private int frontIndex = 1;
	private int behindIndex = 1;

	public CardAdapterForPractice(Context contextPters, List<TestData> ltBaseEntitiesPters, IOnClickListener iOnClickListenerPters, int testMode, List<TestData> allDatas) {
		super(ltBaseEntitiesPters);
		this.context = contextPters;
		this.data = ltBaseEntitiesPters;
		iOnClickListener = iOnClickListenerPters;
		this.testModes = testMode;
		this.allDatas = allDatas;
	}

	@Override
	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.btn_card_practice, parent, false);
		Button btnCard = (Button) convertView;
		//Log.e(TAG, "datas一个的长度是" + data.size());
//		for (int i = 0; i < data.size(); i++) {
////			Log.e(TAG, "第" +i + "个data的type是" +data.get(i).getData().getSubjectType());
//		//	/Log.e(TAG, "第" +i + "个data的getSubjectId是" +data.get(i).getSubjectId());
//		
//		}
		
		
		btnCard.setBackgroundResource(getSrc(data.get(position)));
		TestData testData = data.get(position);
		BaseSubjectData baseSubjectData = null ;
//		Log.e("www", "testData" + testData.getSubjectId() + "---" + testData.getSubjectType() );
		if (testData.getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
//			Log.e(TAG, "获取billData" + testData.toString());
	//		Log.e("TestBillData", "获取billData" + testData.getBillData() + "-------" +testData.getSubjectData()+"++" +  testData.getSubjectId());

			
			baseSubjectData	=testData.getBillData() == null?testData.getSubjectData():testData.getBillData().getSubjectData();
//			Log.e(TAG, "第" + "个data的getSubjectId是" +testData.getSubjectId());
//			Log.e(TAG, "第"  + "个data的getIndexName是" +baseSubjectData.getIndexName());

		}else if (testData.getSubjectType() == Constant.SUBJECT_GROUP_BILL) {
			baseSubjectData = new BaseSubjectData();
			baseSubjectData.setIndexName(testData.getTestGroupBillData().getIndexName());
			 baseSubjectData.setSubjectIndex(testData.getTestGroupBillData().getIndexNum());
		}else{
			
			 baseSubjectData =testData.getData();
//			 Log.e(TAG, "第" + "个data的getSubjectId是" +testData.getSubjectId());
//				Log.e(TAG, "第"  + "个data的getIndexName是" +baseSubjectData.getIndexName());

		}
		String indexName = baseSubjectData.getIndexName();
//		for (int i = 0; i < data.size(); i++) {
//		
//
//	
//		}
		
		btnCard.setText(indexName);

//		btnCard.setText((data.get(position).getData()).getIndexName());
		btnCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (data.get(position).getSubjectType() == Constant.SUBJECT_TYPE_SINGLE_SELECT || data.get(position).getSubjectType() == Constant.SUBJECT_TYPE_MULTI_SELECT
						|| data.get(position).getSubjectType() == Constant.SUBJECT_TYPE_JUDGE) {
					iOnClickListener.ionClick(((SubjectBasicData) data.get(position).getData()).getSubjectIndex() - 1);
				} else if (data.get(position).getSubjectType() == Constant.SUBJECT_TYPE_ENTRY) {
					iOnClickListener.ionClick(((SubjectEntryData) data.get(position).getData()).getSubjectIndex() - 1);
				} else if (data.get(position).getSubjectType() == Constant.SUBJECT_TYPE_BILL) {
					iOnClickListener.ionClick(((SubjectBillData) data.get(position).getBillData().getSubjectData()).getSubjectIndex() - 1);
				}else {
					iOnClickListener.ionClick(data.get(position).getTestGroupBillData().getIndexNum() - 1);
				}
			}
		});

		return convertView;
	}

	/**
	 * 当前题状态背景图
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getSrc(TestData entity) {
		// int src = -1;
		// if (testModes != Constant.TEST_TYPE_EXERCIZE) {// 随堂练习
		// if (entity.getState() == 1) {
		// src = R.drawable.btn_card_right;
		// } else if (entity.getState() == 0) {
		// src = R.drawable.btn_card_nodo;
		// } else {
		// src = R.drawable.btn_card_error;
		// }
		// }
		// else {// 课后练习
		// if (!Constant.IS_SUCCUESS) {// 没收答案之前
		// if (entity.getState() == 1 || entity.getState() == 2 ||
		// entity.getState() == 3) {// 做过
		// src = R.drawable.btn_card_do;
		// } else {// 没做过
		// src = R.drawable.btn_card_nodo;
		// }
		// } else {
		// if (entity.getState() == 1) {
		// src = R.drawable.btn_card_right;
		// } else {
		// src = R.drawable.btn_card_error;
		// }
		// }

		// }
		int src = -1;
		if (testModes == BaseScrollView.TEST_MODE_NORMAL) {// 答题模式
			if (entity.getState() == 1 || entity.getState() == 2) {
				src = R.drawable.btn_card_nodo;
			} else if (entity.getState() == 0) {
				src = R.drawable.btn_card_undo;
			} else {
				src = R.drawable.btn_card_unfinish;
			}
		} else {// 课后练习
			if (entity.getState() == 1) {// 做过
				src = R.drawable.btn_card_right;
			} else {
				src = R.drawable.btn_card_error;

			}

		}

		return src;
	}
}