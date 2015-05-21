package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.local.BaseProduct;

/**
 * 商品详情返回结果
 * 
 * @author wr
 * 
 */
public class GoodsDetailResponse extends BaseProduct {
	private String status;// 商品状态4497153900060001=待上架 4497153900060002=已上架
	// 4497153900060003=商家下架 4497153900060004=平台强制下架

	private String stock_num;// 月销量
	private String store_num;// 库存数

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStock_num() {
		return stock_num;
	}

	public void setStock_num(String stock_num) {
		this.stock_num = stock_num;
	}

	public String getStore_num() {
		return store_num;
	}

	public void setStore_num(String store_num) {
		this.store_num = store_num;
	}

}
