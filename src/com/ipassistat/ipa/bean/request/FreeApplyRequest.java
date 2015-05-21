package com.ipassistat.ipa.bean.request;

/**
 * @Discription： 申请试用的
 * @package： com.ichsy.mboss.bean.request.FreeApplyRequest
 * @author： MaoYaNan
 * @date：2014-10-17 下午4:15:20
 */
public class FreeApplyRequest extends BaseRequest {
	private String activityCode; // 活动编号
	private String address_code; // 收货地址Id
	private String buyer_mobile; // 收货人手机号
	private String orderSource; // 订单来源
	private String area_code; // 收货人省市区
	private String sku_name; // 商品名称
	private String buyer_address; // 收货人地址
	private String postCode; // 邮政编码
	private String buyer_name; // 收货人姓名
	private String sku_code; // 商品ID
	private String end_time; // 活动结束时间

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getAddress_code() {
		return address_code;
	}

	public void setAddress_code(String address_code) {
		this.address_code = address_code;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
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

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
