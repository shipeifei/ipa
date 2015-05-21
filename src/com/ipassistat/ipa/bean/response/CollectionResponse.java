package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;

/**
 * 我的收藏返回数据
 * 
 * com.ichsy.mboss.bean.response.CollectionResponse
 * 
 * @author maoyn<br/>
 *         create at 2014-9-24 下午3:07:32
 */
public class CollectionResponse extends BaseResponse {
	private PageResults paged;
	private List<SaleProduct> products;

	@Override
	public String toString() {
		return "CollectionResponse [paged=" + paged + ", products=" + products
				+ "]";
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public List<SaleProduct> getProducts() {
		return products;
	}

	public void setProducts(List<SaleProduct> products) {
		this.products = products;
	}

}
