package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

/**
 * 订单商品列表(家有接口)
 * @author renheng
 *
 */
public class ApiOrderSellerDetailsResult {

	private String region; //	 String	地区	
	private double price; //	 Double	价格	
	private String promotionType; //	 String	促销种类	
	private List<ApiSellerStandardAndStyleResult>StandardAndStyleList; //	ApiSellerStandardAndStyleResult[]	规格/款式	
	private String productCode; //	 String	sku编号	
	private String mainpicUrl; //	 String	商品图片链接	
	private List<ApiOrderDonationDetailsResult> detailsList; //	 ApiOrderDonationDetailsResult[]	赠品列表	
	private String number; //	 String	数量	
	private String productName; //	 String	商品名称	
	private String promotionDescribe; //	 String	促销描述
	private String productShortName; //	 String	商品名称简介
	private String productType; //	 String	商品类型	明确商品类型的列表不返回 0：普通商品 1：限购商品 2：试用商品
	private String ifEvaluate; //	 String	是否评价	是：true 否：false
	private String end_time; //	 String	结束时间	试用商品才会返回结束时间
	
	
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getProductShortName() {
		return productShortName;
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
	public String getIfEvaluate() {
		return ifEvaluate;
	}
	public void setIfEvaluate(String ifEvaluate) {
		this.ifEvaluate = ifEvaluate;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return StandardAndStyleList;
	}
	public void setStandardAndStyleList(
			List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		StandardAndStyleList = standardAndStyleList;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getMainpicUrl() {
		return mainpicUrl;
	}
	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}
	public List<ApiOrderDonationDetailsResult> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(List<ApiOrderDonationDetailsResult> detailsList) {
		this.detailsList = detailsList;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPromotionDescribe() {
		return promotionDescribe;
	}
	public void setPromotionDescribe(String promotionDescribe) {
		this.promotionDescribe = promotionDescribe;
	}
	
	
}
