package com.edu.basicaccountingforguangzhou.subject.data;

import java.util.ArrayList;
import java.util.List;

import com.edu.basicaccountingforguangzhou.data.SubjectBillData;
import com.edu.basicaccountingforguangzhou.subject.SubjectConstant;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BaseElementInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.element.info.BlankInfo;
import com.edu.basicaccountingforguangzhou.subject.bill.template.BillTemplate;

/**
 * 单据测试数据
 * 
 * @author lucher
 * 
 */
public class TestBillData extends BaseTestData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7866847549496754459L;

	// 单据模板
	private BillTemplate template;
	// 用户印章
	private String uSigns;
	// 题目数据
	private SubjectBillData subjectData;

	/**
	 * 加载模板，把题目数据加载到模板里
	 * 
	 * @return 加载结果信息，成功为success
	 */
	public String loadTemplate() {
		if (subjectData == null) {
			return id + ",subjectId:" + subjectId + ",题目数据为空";
		}
		if (template == null) {
			return id + ",subjectId:" + subjectId + ",模板数据为空";
		}
		// 获取题目里的空，模板里的空，答案记录里的空 然后进行初始化操作
		String[] blanks = subjectData.getAnswer().split(SubjectConstant.SEPARATOR_ITEM);
		List<BlankInfo> blankDatas = new ArrayList<BlankInfo>();
		for (BaseElementInfo element : template.getElementDatas()) {
			if (element.getType() > SubjectConstant.ELEMENT_TYPE_BLANK_START && element.getType() < SubjectConstant.ELEMENT_TYPE_BLANK_END) {// 填空题
				blankDatas.add((BlankInfo) element);
			}
		}
		String[] uBlankses = null;
		if (uAnswer != null) {
			uBlankses = uAnswer.split(SubjectConstant.SEPARATOR_ITEM);
		}
		if (blanks.length != blankDatas.size()) {
			return id + ",subjectId:" + subjectId + ",题目数据与模板不匹配";
		}

		// 初始化正确答案和用户答案
		for (int i = 0; i < blanks.length; i++) {
			if (blanks[i].startsWith(SubjectConstant.FLAG_PREFIX_DISABLED)) {// 不需要用户填写，直接显示答案
				String answer = blanks[i].substring(SubjectConstant.FLAG_PREFIX_DISABLED.length(), blanks[i].length());
				blankDatas.get(i).setEditable(false);
				blankDatas.get(i).setAnswer(answer);
			} else {
				blankDatas.get(i).setEditable(true);
				//正确答案初始化
				if (blanks[i].equals(SubjectConstant.FLAG_NULL_STRING)) {// 空内容为null代表空字符串
					blankDatas.get(i).setAnswer("");
				} else {
					blankDatas.get(i).setAnswer(blanks[i]);
				}
				//用户答案初始化
				if (uBlankses != null && uBlankses.length == blankDatas.size()) {
					if (uBlankses[i].equals(SubjectConstant.FLAG_NULL_STRING)) {// 空内容为null代表空字符串
						blankDatas.get(i).setuAnswer("");
					} else {
						blankDatas.get(i).setuAnswer(uBlankses[i]);
					}
				}
			}
		}

		return "success";
	}

	public BillTemplate getTemplate() {
		return template;
	}

	public void setTemplate(BillTemplate template) {
		this.template = template;
	}

	public String getuSigns() {
		return uSigns;
	}

	public void setuSigns(String uSigns) {
		this.uSigns = uSigns;
	}

	@Override
	public SubjectBillData getSubjectData() {
		return subjectData;

	}

	@Override
	public void setSubjectData(BaseSubjectData subjectData) {
		this.subjectData = (SubjectBillData) subjectData;
	}

	@Override
	public String toString() {
		return String.format("subjectData:%s,template:%s,uBlanks:%s,score:%s", subjectData, template, uAnswer, uScore);
	}

}
