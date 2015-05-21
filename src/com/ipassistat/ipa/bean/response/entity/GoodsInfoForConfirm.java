package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

/**
 * 商品列表
 * @author renheng
 *
 */
public class GoodsInfoForConfirm {
	private List<PcPropertyinfoForFamily> sku_property; //	 PcPropertyinfoForFamily[]	商品属性	商品规格,商品款式
	private double sku_price; //	 Double	商品价格	商品价格
	private String area_code; //	 String	仓储地区	仓储地区
	private String sales_info; //	 String	促销描述	促销描述
	private String sales_type; //	 String	促销种类	促销种类
	private List<Gift> giftList; //	 Gift[]	赠品列表	赠品列表
	private String pic_url; //	 String	商品图片链接	商品图片链接
	private int sku_num	; // int	商品数量	商品数量
	private String product_code; //	 String	商品编号	商品编号
	private String sales_code; //	 String	促销活动编号	促销活动编号
	private String sku_name; //	 String	商品名称	商品名称
	private int now_stock; //	 int	当前库存量	当前库存量
	private String sku_code; //	 String	sku编号	sku编号
	public List<PcPropertyinfoForFamily> getSku_property() {
		return sku_property;
	}
	public void setSku_property(List<PcPropertyinfoForFamily> sku_property) {
		this.sku_property = sku_property;
	}
	public double getSku_price() {
		return sku_price;
	}
	public void setSku_price(double sku_price) {
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
	public List<Gift> getGiftList() {
		return giftList;
	}
	public void setGiftList(List<Gift> giftList) {
		this.giftList = giftList;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public int getSku_num() {
		return sku_num;
	}
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSales_code() {
		return sales_code;
	}
	public void setSales_code(String sales_code) {
		this.sales_code = sales_code;
	}
	public String getSku_name() {
		return sku_name;
	}
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}
	public int getNow_stock() {
		return now_stock;
	}
	public void setNow_stock(int now_stock) {
		this.now_stock = now_stock;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	
	
}
