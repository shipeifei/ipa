package com.ipassistat.ipa.bean.response;

import java.util.List;
/**
 * 广告条
 * @author lxc
 *
 */
public class BannerResponse extends BaseResponse{

	private List<Advertise> advertise;

	public List<Advertise> getAdvertise() {
		return advertise;
	}

	public void setAdvertise(List<Advertise> advertise) {
		this.advertise = advertise;
	}
	
	
	
}
