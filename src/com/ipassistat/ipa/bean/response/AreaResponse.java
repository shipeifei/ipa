package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.AreaEntity;

/**
 * 城区响应bean
 * @author renheng
 *
 */
public class AreaResponse extends BaseResponse {

	private List<AreaEntity> area;

	public List<AreaEntity> getArea() {
		return area;
	}

	public void setArea(List<AreaEntity> area) {
		this.area = area;
	}
	
	
}
