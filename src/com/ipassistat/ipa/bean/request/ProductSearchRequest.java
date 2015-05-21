package com.ipassistat.ipa.bean.request;

/**
 * 商品 - 搜索
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class ProductSearchRequest extends BaseRequest {
	public String keyword;
	public String picWidth;

	public ProductSearchRequest(String keyword) {
		this.keyword = keyword;
	}

}
