package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 收货地址列表
 * @author renheng
 *
 */
public class BeautyAddress implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id; // 地址id
	private String isdefault; // 是否默认 1是默认 
	private String name; // 姓名
	private String street; // 详细地址
	private String postcode; // 邮政编码
	private String provinces; // 省市区
	private String mobile; // 手机号码
	private String areaCode; //	 区编码
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
