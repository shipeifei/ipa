package com.ipassistat.ipa.domain.bean;

/***
 * 打电话意图
 * @author shipeifei
 *
 */
public class CallIntent {

	/***
	 * 联系人名称
	 */
	private String name="";
	/***
	 * 电话号码
	 */
	private String number="";
	/***
	 * 电话类型
	 */
	private String method="";
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
