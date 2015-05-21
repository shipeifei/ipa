package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.BeautyAddress;

/**
 * 新增收货地址
 * @author renheng
 *
 */
public class AddressAddResponse extends BaseResponse{

	private List<BeautyAddress> adress;  //收货地址列表

	public List<BeautyAddress> getAdress() {
		return adress;
	}

	public void setAdress(List<BeautyAddress> adress) {
		this.adress = adress;
	}
	
	
}
