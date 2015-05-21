package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 官方活动请求bean
 * @author renheng
 *
 */
public class OfficialRequest extends BaseRequest{

	private PageOption paging;
	private String picWidth;
	
	public String getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
	
	
}
