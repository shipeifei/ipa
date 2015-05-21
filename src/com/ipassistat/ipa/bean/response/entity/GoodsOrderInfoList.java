package com.ipassistat.ipa.bean.response.entity;

/**
 * 订单商品信息列表
 * @author renheng
 *
 */
public class GoodsOrderInfoList {

	private int sku_num; //	 int	商品数量	商品数量
	private String is_commented; //	 String	是否评价	是否评价:0:未评价；1：已评价
	private String sku_code; //	 String	sku编号	sku编号
	public int getSku_num() {
		return sku_num;
	}
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	public String getIs_commented() {
		return is_commented;
	}
	public void setIs_commented(String is_commented) {
		this.is_commented = is_commented;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	
	
}
