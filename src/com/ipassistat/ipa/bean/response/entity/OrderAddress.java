package com.ipassistat.ipa.bean.response.entity;

/**
 * 订单地址信息列表
 * @author renheng
 *
 */
public class OrderAddress {

	private String buyer_mobile; //	 String	收货人手机号	手机号

	private String area_code; //	 String	收货人省市区	收货人地址所在地区选择的第三级编号
	private String buyer_address; //	 String	收货人地址	收货人地址
	private String postCode; //	 String	邮政编码	邮政编码
	private String buyer_name; //	 String	收货人姓名	收货人姓名
	public String getBuyer_mobile() {
		return buyer_mobile;
	}
	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getBuyer_address() {
		return buyer_address;
	}
	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	
	
}
