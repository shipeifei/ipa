package com.ipassistat.ipa.bean.local;

import java.util.List;

import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.entity.PicInfo;

/**
 * 商品的超类
 * 
 * @author LiuYuHang
 * @date 2014年11月18日
 */
public class BaseProduct extends BaseResponse {

	private String product_code;// 商品编码
	private List<PicInfo> photos;// 商品图片
	private String linkUrl;// 分享链接地址
	private String favstatus;// 收藏状态 0为未收藏 1是已收藏
	private String name;// 商品名称
	private String sku_code;// sku编码
	private List<PicInfo> infophotos;// 产品详情

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public List<PicInfo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PicInfo> photos) {
		this.photos = photos;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getFavstatus() {
		return favstatus;
	}

	public void setFavstatus(String favstatus) {
		this.favstatus = favstatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public List<PicInfo> getInfophotos() {
		return infophotos;
	}

	public void setInfophotos(List<PicInfo> infophotos) {
		this.infophotos = infophotos;
	}

}
