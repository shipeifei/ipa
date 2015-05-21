package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.BeautyAddress;

/**
 * 收货地址
 * @author ren
 *
 */
public class AddresssReceiveResponse extends BaseResponse{

	private List<BeautyAddress> adress; //收货地址列表

	public List<BeautyAddress> getAdress() {
		return adress;
	}

	public void setAdress(List<BeautyAddress> adress) {
		this.adress = adress;
	}
	
	
}
