package com.ipassistat.ipa.bean.request.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品信息
 * 
 * @author renheng
 * 
 */
public class PurchaseGoods implements Parcelable {

	private String sku_code; // 商品ID
	private String order_count; // 商品数量

	private String goods_name;// 商品名称
	private String price;// 商品价格
	private String pic_url;// 商品图片
	private String product_type;// 商品类型
	private String product_code;//商品编码
	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public static final Parcelable.Creator<PurchaseGoods> CREATOR = new Creator<PurchaseGoods>() {

		@Override
		public PurchaseGoods[] newArray(int size) {

			return new PurchaseGoods[size];
		}

		@Override
		public PurchaseGoods createFromParcel(Parcel source) {
			PurchaseGoods good = new PurchaseGoods();
			good.sku_code = source.readString();
			good.order_count = source.readString();
			good.goods_name = source.readString();
			good.price = source.readString();
			good.pic_url = source.readString();
			good.product_type = source.readString();
			good.product_code=source.readString();
			return good;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sku_code);
		dest.writeString(order_count);
		dest.writeString(goods_name);
		dest.writeString(price);
		dest.writeString(pic_url);
		dest.writeString(product_type);
		dest.writeString(product_code);
	}

	@Override
	public String toString() {
		return "PurchaseGoods [sku_code=" + sku_code + ", order_count="
				+ order_count + ", goods_name=" + goods_name + ", price="
				+ price + ", pic_url=" + pic_url + ", product_type="
				+ product_type + "]";
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getPrice() {
		return price;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getOrder_count() {
		return order_count;
	}

	public void setOrder_count(String order_count) {
		this.order_count = order_count;
	}

}
