package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ProvinceEntity;

/**
 * 省份响应bean
 * @author renheng
 *
 */
public class ProvinceResponse extends BaseResponse{
	
	private List<ProvinceEntity> provinces;

	public List<ProvinceEntity> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<ProvinceEntity> provinces) {
		this.provinces = provinces;
	}
	
	
		
}
