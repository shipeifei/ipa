package com.ipassistat.ipa.bean.request;

/**
 * 订单详情
 * @author renheng
 *
 */
public class OrderDetailRequest extends BaseRequest {

	private String buyer_code; //	 String	是		买家编号	
	private String order_code; //	 String	是		订单编号
	private String picWidth; 
	private String browserUrl; //ip地址
	
	
	public String getBrowserUrl() {
		return browserUrl;
	}
	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}
	public String getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}
	public String getBuyer_code() {
		return buyer_code;
	}
	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	
}
