package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * @Discription： 试用中心列表数据请求
 * @package： com.ichsy.mboss.bean.request.TryOutCenterRequest
 * @author： MaoYaNan
 * @date：2014-9-28 下午5:47:54
 */
public class TryOutCenterRequest extends BaseRequest {
	private PageOption paging;
	private Integer picWidth;

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
}
