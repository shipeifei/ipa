package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品订单信息
 * 
 * @author renheng
 * 
 */
public class ApiSellerOrderListResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ApiSellerListResult> apiSellerList; // 每个订单的商品信息
	private String alipaySign; // 支付宝Sign 签名过的
	private int orderStatusNumber; // 订单里的商品数量
	private String create_time; // 订单生成时间
	private String order_code; // 订单号
	private String due_money; // 实付总价
	private String order_status; // 订单状态
	private String ifFlashSales;// 是否为闪购订单 0:闪购 1:非闪购
	private String alipayUrl; // 支付宝移动支付链接
	private String payType;
	private MicroMessagePayment micoPayment; //微信支付实体
	
	
	public List<ApiSellerListResult> getApiSellerList() {
		return apiSellerList;
	}
	public void setApiSellerList(List<ApiSellerListResult> apiSellerList) {
		this.apiSellerList = apiSellerList;
	}
	public String getAlipaySign() {
		return alipaySign;
	}
	public void setAlipaySign(String alipaySign) {
		this.alipaySign = alipaySign;
	}
	public int getOrderStatusNumber() {
		return orderStatusNumber;
	}
	public void setOrderStatusNumber(int orderStatusNumber) {
		this.orderStatusNumber = orderStatusNumber;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getDue_money() {
		return due_money;
	}
	public void setDue_money(String due_money) {
		this.due_money = due_money;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getIfFlashSales() {
		return ifFlashSales;
	}
	public void setIfFlashSales(String ifFlashSales) {
		this.ifFlashSales = ifFlashSales;
	}
	public String getAlipayUrl() {
		return alipayUrl;
	}
	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}
	public MicroMessagePayment getMicoPayment() {
		return micoPayment;
	}
	public void setMicoPayment(MicroMessagePayment micoPayment) {
		this.micoPayment = micoPayment;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	
	

}
