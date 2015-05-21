package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 普通商品-实体类
 * 
 * @author wr
 *
 */
public class SaleProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;// 商品sku编码
	private String title, name;// 商品名称
	private String sell_price;// 商品当前价格
	private String market_price;// 商品原价
	private String buy_count, stock_num;// 商品购买数
	private List<String> labels;// 商品标签
	private String photo, photos;// 商品图片
	private String productType;//商品类型

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(String buy_count) {
		this.buy_count = buy_count;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStock_num() {
		return stock_num;
	}

	public void setStock_num(String stock_num) {
		this.stock_num = stock_num;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
}
