package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

/**
 * 订单信息列表信息
 * @author renheng
 *
 */
public class OrderList {

	private String send_type; //	 String	配送方式	配送方式
	private String pay_type; //	 String	支付方式	支付方式
	private List<GoodsOrderInfoList> goodsOrderInfoLists;	 //	订单商品信息列表	订单商品信息列表
	private String create_time; //	 String	订单生成时间	订单生成时间
	private String order_money; //	 String	订单金额	价格合计
	private OrderAddress orderAddresses; //	 OrderAddress	订单地址信息列表	订单地址信息列表
	private String order_code; //	 String	订单号	订单号
	private String order_status; //	 String	订单状态	订单状态
	private String order_type; //	 String	订单类型	订单类型
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public List<GoodsOrderInfoList> getGoodsOrderInfoLists() {
		return goodsOrderInfoLists;
	}
	public void setGoodsOrderInfoLists(List<GoodsOrderInfoList> goodsOrderInfoLists) {
		this.goodsOrderInfoLists = goodsOrderInfoLists;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOrder_money() {
		return order_money;
	}
	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}
	public OrderAddress getOrderAddresses() {
		return orderAddresses;
	}
	public void setOrderAddresses(OrderAddress orderAddresses) {
		this.orderAddresses = orderAddresses;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	
}
