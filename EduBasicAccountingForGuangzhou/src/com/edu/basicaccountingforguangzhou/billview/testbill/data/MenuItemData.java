package com.edu.basicaccountingforguangzhou.billview.testbill.data;

import com.edu.library.data.BaseListItemData;

/**
 * 菜单项数据封装
 * 
 * @author lucher
 * 
 */
public class MenuItemData extends BaseListItemData {

	public MenuItemData(String text) {
		setText(text);
	}

	@Override
	public String getText() {
		return text;
	}

}
