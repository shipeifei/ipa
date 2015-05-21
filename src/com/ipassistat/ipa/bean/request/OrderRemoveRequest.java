package com.ipassistat.ipa.bean.request;

/**
 * 删除订单
 * @author renheng
 *
 */
public class OrderRemoveRequest extends BaseRequest {

	private String user_code; //	 String	否		用户code	非必填 可根据当前登录用户获取
	private String order_code; //	 String	是		订单code	我的订单中获取
	
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	
}
