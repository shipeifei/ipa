package com.ipassistat.ipa.bean.response;

import java.io.Serializable;

/**
 * 订单预结算
 * @author renheng
 *
 */
public class OrderPrePayResponse extends BaseResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private String order_money; //	 String	订单金额	订单金额
	private String prompt; //	 String	订单提示	订单提示
	private String postage; //	 String	运费	运费
	public String getOrder_money() {
		return order_money;
	}
	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}


}
