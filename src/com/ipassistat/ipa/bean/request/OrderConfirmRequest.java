package com.ipassistat.ipa.bean.request;

import java.util.List;

import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;

/***
 * 确认订单
 * @author renheng
 */
public class OrderConfirmRequest extends BaseRequest {

	private List<GoodsInfoForAdd> goods; //	 GoodsInfoForAdd[]	是		商品	
	private String buyer_code;	  //String	否	123456	买家编号	可为空，默认取当前登录人的编号
	private String order_type; //	 String	是	449715200003	订单类型	449715200003试用订单、449715200004闪购订单、449715200005普通订单
	public List<GoodsInfoForAdd> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsInfoForAdd> goods) {
		this.goods = goods;
	}
	public String getBuyer_code() {
		return buyer_code;
	}
	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	
}
