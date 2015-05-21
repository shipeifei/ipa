package com.ipassistat.ipa.bean.response;

public class Advertise {
	/**
	 * 版位名称
	 */
	private String place_code;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 广告名称
	 */
	private String ad_name;//
	/**
	 * 排序
	 */
	private int ad_sort;
	/**
	 * 链接地址
	 */
	private String adImg_url;
	/**
	 * 图片
	 */
	private String adImg;
	/**
	 * 广告id
	 */
	private String ad_code;
	/**
	 * 商品类型 0：普通商品 1：限购商品 2：试用商品
	 */
	private String productType;
	
	private String share_cotent; //	String	分享内容	
	private String share_pic; //	String	分享图片
	private String share_title; //分享标题
	
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPlace_code() {
		return place_code;
	}
	public void setPlace_code(String place_code) {
		this.place_code = place_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAd_name() {
		return ad_name;
	}
	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}
	public int getAd_sort() {
		return ad_sort;
	}
	public void setAd_sort(int ad_sort) {
		this.ad_sort = ad_sort;
	}
	public String getAdImg_url() {
		return adImg_url;
	}
	public void setAdImg_url(String adImg_url) {
		this.adImg_url = adImg_url;
	}
	public String getAdImg() {
		return adImg;
	}
	public void setAdImg(String adImg) {
		this.adImg = adImg;
	}
	public String getAd_code() {
		return ad_code;
	}
	public void setAd_code(String ad_code) {
		this.ad_code = ad_code;
	}
	public String getShare_cotent() {
		return share_cotent;
	}
	public void setShare_cotent(String share_cotent) {
		this.share_cotent = share_cotent;
	}
	public String getShare_pic() {
		return share_pic;
	}
	public void setShare_pic(String share_pic) {
		this.share_pic = share_pic;
	}
	public String getShare_title() {
		return share_title;
	}
	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}
	
	
}
