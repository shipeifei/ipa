package com.ipassistat.ipa.bean.request.entity;

import java.io.Serializable;

/**
 * 同步购物车商品列表实体类
 * @author wr
 *
 */
public class GoodsInfoForAdd implements Serializable{

	private static final long serialVersionUID = 1L;
	private int sku_num;//商品数量
	private String area_code;//地区编号  可不填写，添加购物车不再需要区域编号
	private String product_code;//商品编号
	private String sku_code;//sku编号
	public int getSku_num() {
		return sku_num;
	}
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
}
