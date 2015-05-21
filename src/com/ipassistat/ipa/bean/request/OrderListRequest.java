package com.ipassistat.ipa.bean.request;

/**
 * 订单列表
 * 
 * @author renheng
 * 
 */
public class OrderListRequest extends BaseRequest{

	private String nextPage; //	 String	是		下一页	
	private String buyer_code; //	 String	是		买家编号	
	private String order_status; //		订单状态	下单成功-未付款:4497153900010001,下单成功-未发货:4497153900010002,已发货:4497153900010003,已收货:4497153900010004,交易成功:4497153900010005,交易失败:4497153900010006
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
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getBuyer_code() {
		return buyer_code;
	}
	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	
	
}
