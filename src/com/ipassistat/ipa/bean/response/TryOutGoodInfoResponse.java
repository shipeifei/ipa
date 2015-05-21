package com.ipassistat.ipa.bean.response;

import java.util.ArrayList;

import com.ipassistat.ipa.bean.response.entity.PicInfo;

/**
 * @Discription： 付邮试用返回的数据类
 * @package： com.ichsy.mboss.bean.response.TryOutGoodInfpResponse
 * @author： MaoYaNan
 * @date：2014-9-29 下午4:42:36
 */
public class TryOutGoodInfoResponse extends BaseResponse {
	private String stock_num; // 商品月销量
	private String comment_count; // 商品评论数
	private String newPrice; // 商品现价
	// private ArrayList<String> labels; // 商品标签
	private ArrayList<PicInfo> infophotos;// 产品详情图片list
	private ArrayList<PicInfo> photos;// 商品图片
	private String oldPrice; // 商品原价
	private String name; // 商品名称
	private String rebate;// 商品折扣
	private String sku_code; // 商品编号
	private String linkUrl;//分享链接地址

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public ArrayList<PicInfo> getInfophotos() {
		return infophotos;
	}

	public void setInfophotos(ArrayList<PicInfo> infophotos) {
		this.infophotos = infophotos;
	}

	public ArrayList<PicInfo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<PicInfo> photos) {
		this.photos = photos;
	}

	public String getName() {
		return name;
	}

	public String getStock_num() {
		return stock_num;
	}

	public void setStock_num(String stock_num) {
		this.stock_num = stock_num;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	

}
