package com.ipassistat.ipa.bean.request;

/**
 * 新增地址 
 * @author renheng
 *
 */
public class AddressAddRequest extends BaseRequest{
		
	private String phone;  //手机号码
	private String address; //详细地址
	private String name; //收货人
	private String province; //省市区
	private String postcode; //邮政编码
	private String areaCode; //区ID
	private String isDefault; //是否默认收货地址
	
	
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
}
