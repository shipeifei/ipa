package com.ipassistat.ipa.bean.response.entity;

/**
 * 微信支付实体类
 * @author Ren
 *
 */
public class MicroMessagePayment {
	private String sign; //	 String	商家根据微信开放平台文档对数据做的签名	
	private String partnerid; //	 String	商户id	
	private String timeStamp; //	 String	当前的时间	
	private String prepayid; //	 String	预支付订单	
	private String packageValue; //	 String	商家根据文档填写的数据和签名	
	private String appid; //	 String	商家在微信开放平台申请的应用id	
	private String nonceStr; //	 String	随机串，防重发
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPackageValue() {
		return packageValue;
	}
	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
	
}
