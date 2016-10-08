package com.edu.basicaccountingforguangzhou.info;

import java.io.Serializable;

/**
 * 实体类:分类
 * 
 * @author 孙兴达
 *
 */
public class TypeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8524580403040419986L;
	private int id;	//分类ID
	private String typeName;	//分类名称
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
