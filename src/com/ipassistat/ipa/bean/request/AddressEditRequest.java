package com.ipassistat.ipa.bean.request;

/**
 * 修改地址 
 * @author renheng
 *
 */
public class AddressEditRequest extends BaseRequest{
		
	private String id; //地址ID
	private String mobile;  //手机号码
	private String street; //详细地址
	private String name; //收货人
	private String provinces; //省市区
	private String postcode; //邮政编码
	private String areaCode; //区编码
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
	
}
