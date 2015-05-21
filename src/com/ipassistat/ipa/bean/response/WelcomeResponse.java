package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.WelcomeEntity;

public class WelcomeResponse extends BaseResponse{
	
	private List<WelcomeEntity> stratPage;

	public List<WelcomeEntity> getStratPage() {
		return stratPage;
	}

	public void setStratPage(List<WelcomeEntity> stratPage) {
		this.stratPage = stratPage;
	}


	
	

}
