package com.ipassistat.ipa.bean.request;

/**
 * 确认收货
 * @author renheng
 *
 */
public class ReceiveConfirmRequest extends BaseRequest{

	private String orderCode; //订单编号

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	
	
}
