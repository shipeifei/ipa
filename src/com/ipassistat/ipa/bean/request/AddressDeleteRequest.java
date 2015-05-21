package com.ipassistat.ipa.bean.request;

/**
 * 删除地址
 * @author renheng
 *
 */
public class AddressDeleteRequest extends BaseRequest{

	private String address;  //地址ID

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}
