package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 个人中心中我的收藏网络请求参数实体
 * 
 * com.ichsy.mboss.bean.request.CollectionRequest
 * 
 * @author maoyn<br/>
 *         create at 2014-9-24 下午3:03:50
 */
public class CollectionRequest extends BaseRequest {
	private PageOption paging;
	private Integer picWidth;

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	@Override
	public String toString() {
		return "CollectionRequest [paging=" + paging + "]";
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

}
