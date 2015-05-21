package com.ipassistat.ipa.bean.request.entity;

/**
 * 提交订单
 * @author renheng
 *
 */
public class OrderDetail {

	private int sku_num; //商品数量
	private String sku_code; //sku编号
	
	public int getSku_num() {
		return sku_num;
	}
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	
	
	
}
