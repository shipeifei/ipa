package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.Activity;
import com.ipassistat.ipa.bean.response.entity.PageResults;

public class OfficialResponse extends BaseResponse{

	private PageResults paged;  //翻页结果
	
	private List<Activity> activities ; //活动列表

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	
	
}
