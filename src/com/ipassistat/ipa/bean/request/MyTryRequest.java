package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * @Discription： 我的试用的数据请求
 * @package： com.ichsy.mboss.bean.request.MyTryRequest
 * @author： MaoYaNan
 * @date：2014-10-14 下午5:12:28
 */
public class MyTryRequest extends BaseRequest {
	private String type;
	private PageOption paging;
	private Integer picWidth;

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
}
