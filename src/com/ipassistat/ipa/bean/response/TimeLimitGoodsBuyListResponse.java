package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.TimedScareBuying;

/**
 * 限时抢购商品列表，网络访问返回数据
 * 
 * com.ichsy.mboss.bean.response.TimeLimitGoodsBuyListResponse
 * 
 * @author maoyn<br/>
 *         create at 2014-9-23 上午10:03:28
 */
public class TimeLimitGoodsBuyListResponse extends BaseResponse {
	private PageResults paged;
	private List<TimedScareBuying> products;

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public List<TimedScareBuying> getProducts() {
		return products;
	}

	public void setProducts(List<TimedScareBuying> products) {
		this.products = products;
	}
}
