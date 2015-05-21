package com.ipassistat.ipa.bean.local;

import java.io.Serializable;

/**
 * 同步购物车商品列表实体类
 * @author wr
 *
 */
public class GoodsEntity implements Serializable{
	private static final long serialVersionUID = 1L;	
	private String sku_code;//sku编号
	private int sku_num;//商品数量
	private int sku_changenum;//sku_num为变化数量，增加的时候为正，减少的时候为负
	private int sku_stock;//库存剩余数量
	private String product_picurl;//商品图片
	private String product_price;//商品价格
	private String product_name;//商品名称	
	private String product_type;//0:普通商品 1:限时抢购商品2:试用商品
	private String flag_stock;//库存是否足够  1：足够  0：不足
	private String flag_product;//商品是否有效 1:有效商品，0:无效商品
	public String getFlag_product() {
		return flag_product;
	}
	public void setFlag_product(String flag_product) {
		this.flag_product = flag_product;
	}
	public int getSku_stock() {
		return sku_stock;
	}
	public void setSku_stock(int sku_stock) {
		this.sku_stock = sku_stock;
	}
	public String getFlag_stock() {
		return flag_stock;
	}
	public void setFlag_stock(String flag_stock) {
		this.flag_stock = flag_stock;
	}
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
	public String getProduct_picurl() {
		return product_picurl;
	}
	public void setProduct_picurl(String product_picurl) {
		this.product_picurl = product_picurl;
	}
	public String getProduct_price() {
		return product_price;
	}
	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public int getSku_changenum() {
		return sku_changenum;
	}
	public void setSku_changenum(int sku_changenum) {
		this.sku_changenum = sku_changenum;
	}

}
