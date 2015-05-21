package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.GoodsInfoForQuery;
/**
 * 同步购物车返回结果
 * @author wr
 *
 */
public class ShoppingcartSyncResponse extends BaseResponse{
	private String salesAdv;//优惠头条
	private int disable_account_num;//失效商品数量
	private int acount_num;//商品总数量
	private List<GoodsInfoForQuery> shoppingCartList;//购物车商品列表
	private int disableSku;//失效商品条数
	public String getSalesAdv() {
		return salesAdv;
	}
	public void setSalesAdv(String salesAdv) {
		this.salesAdv = salesAdv;
	}
	public int getDisable_account_num() {
		return disable_account_num;
	}
	public void setDisable_account_num(int disable_account_num) {
		this.disable_account_num = disable_account_num;
	}
	public int getAcount_num() {
		return acount_num;
	}
	public void setAcount_num(int acount_num) {
		this.acount_num = acount_num;
	}
	public List<GoodsInfoForQuery> getShoppingCartList() {
		return shoppingCartList;
	}
	public void setShoppingCartList(List<GoodsInfoForQuery> shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}
	public int getDisableSku() {
		return disableSku;
	}
	public void setDisableSku(int disableSku) {
		this.disableSku = disableSku;
	}
}
