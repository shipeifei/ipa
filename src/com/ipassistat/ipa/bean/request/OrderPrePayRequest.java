package com.ipassistat.ipa.bean.request;

import java.util.List;

import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;

/**
 * 订单预结算
 * 
 * @author renheng
 * 
 */
public class OrderPrePayRequest extends BaseRequest {

	private List<PurchaseGoods> purchaseGoods; // 商品信息
	private String order_money; // 订单总价
	private String type; // 商品类型

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PurchaseGoods> getPurchaseGoods() {
		return purchaseGoods;
	}

	public void setPurchaseGoods(List<PurchaseGoods> purchaseGoods) {
		this.purchaseGoods = purchaseGoods;
	}

	public String getOrder_money() {
		return order_money;
	}

	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}

}
