package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.CityEntity;

/**
 * 城市响应bean
 * @author renheng
 *
 */
public class CityResponse extends BaseResponse{

	private List<CityEntity> city;

	public List<CityEntity> getCity() {
		return city;
	}

	public void setCity(List<CityEntity> city) {
		this.city = city;
	}

	
	
}
