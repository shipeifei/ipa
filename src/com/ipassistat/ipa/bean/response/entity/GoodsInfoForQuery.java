package com.ipassistat.ipa.bean.response.entity;

import java.util.List;
/**
 * 购物车商品列表实体类
 * @author wr
 *
 */
public class GoodsInfoForQuery {
	private List<PcPropertyinfoForFamily> sku_property;//商品属性  商品规格,商品款式
	private Double sku_price;//商品价格
	private String area_code;//地区编号
	private String sales_info;//促销描述
	private int sku_stock;//库存
	private String sales_type;//促销种类 
	private String pic_url;//商品图片链接
	private int limit_order_num;//每单限购数量
	private String flag_stock;//库存是否足够  1:足够，0:不足
	private int sku_num;//商品数量
	private String flag_product;//是否有效商品   1:有效商品，0:无效商品 
	private String product_code;//商品编号
	private String sku_name;//商品名称
	private String sku_code;//sku编号
	public List<PcPropertyinfoForFamily> getSku_property() {
		return sku_property;
	}
	public void setSku_property(List<PcPropertyinfoForFamily> sku_property) {
		this.sku_property = sku_property;
	}
	public Double getSku_price() {
		return sku_price;
	}
	public void setSku_price(Double sku_price) {
		this.sku_price = sku_price;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getSales_info() {
		return sales_info;
	}
	public void setSales_info(String sales_info) {
		this.sales_info = sales_info;
	}
	public String getSales_type() {
		return sales_type;
	}
	public void setSales_type(String sales_type) {
		this.sales_type = sales_type;
	}
	public int getSku_stock() {
		return sku_stock;
	}
	public void setSku_stock(int sku_stock) {
		this.sku_stock = sku_stock;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public int getLimit_order_num() {
		return limit_order_num;
	}
	public void setLimit_order_num(int limit_order_num) {
		this.limit_order_num = limit_order_num;
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
	public String getFlag_product() {
		return flag_product;
	}
	public void setFlag_product(String flag_product) {
		this.flag_product = flag_product;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSku_name() {
		return sku_name;
	}
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

}
