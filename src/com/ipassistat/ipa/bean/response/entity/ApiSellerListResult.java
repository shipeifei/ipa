package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每个订单的商品信息
 * 
 * @author renheng
 * 
 */
public class ApiSellerListResult  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sell_price; // String 商品单价
	private String mainpic_url; // String 商品图片链接
	private List<ApiSellerStandardAndStyleResult> standardAndStyleList; // 规格/款式
	private String product_code; // 商品编号
	private String product_name;// 商品名称
	private String labels; // 活动标签
	private String warehouseCity; // 仓储城市
	private String product_number; // 商品数量
	private String productShortName; //	 商品名称简介
	private String productType; //	 商品类型	明确商品类型的列表不返回 0：普通商品 1：限购商品 2：试用商品
	private String ifEvaluate; //	是否评价	是：true 否：false
	
	public String getProductShortName() {
		return productShortName;
	}
	public String getIfEvaluate() {
		return ifEvaluate;
	}
	public void setIfEvaluate(String ifEvaluate) {
		this.ifEvaluate = ifEvaluate;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}
	public String getMainpic_url() {
		return mainpic_url;
	}
	public void setMainpic_url(String mainpic_url) {
		this.mainpic_url = mainpic_url;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public String getWarehouseCity() {
		return warehouseCity;
	}
	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}
	public String getProduct_number() {
		return product_number;
	}
	public void setProduct_number(String product_number) {
		this.product_number = product_number;
	}
	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return standardAndStyleList;
	}
	public void setStandardAndStyleList(
			List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		this.standardAndStyleList = standardAndStyleList;
	}
	
	
	
	
	
}
