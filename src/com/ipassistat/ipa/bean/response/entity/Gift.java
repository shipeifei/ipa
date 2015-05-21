package com.ipassistat.ipa.bean.response.entity;

/**
 * 赠品列表
 * @author renheng
 *
 */
public class Gift {
	private String sku_num; //	 String	商品数量	商品数量
	private String sku_name; //	 String	商品名称	商品名称
	private String sku_code; //	 String	商品编号	商品编号
	private String pic_url; //	 String	商品图片链接	商品图片链接
	public String getSku_num() {
		return sku_num;
	}
	public void setSku_num(String sku_num) {
		this.sku_num = sku_num;
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
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	
}
