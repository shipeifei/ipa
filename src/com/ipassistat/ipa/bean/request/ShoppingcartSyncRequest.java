package com.ipassistat.ipa.bean.request;

import java.util.List;

import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;


/**
 * 购物车同步应用输入参数
 * @author wr
 *
 */
public class ShoppingcartSyncRequest extends BaseRequest{
	private List<GoodsInfoForAdd> goodsList;//商品列表
	private String buyer_code;//买家编号
	public List<GoodsInfoForAdd> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsInfoForAdd> goodsList) {
		this.goodsList = goodsList;
	}
	public String getBuyer_code() {
		return buyer_code;
	}
	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

}
