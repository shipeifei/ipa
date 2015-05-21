package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

public class PopPostListRequest extends BaseRequest{
	/**
	 *  PageOption	是	5,10	翻页选项	输入起始页码和每页10条
	 */
	private PageOption paging;
	
	private int picWidth;

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

	public int getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}

	
	
	
	
}
