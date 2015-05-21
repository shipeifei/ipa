package com.ipassistat.ipa.bean.request;

/**
 * 设置默认地址
 * @author ren
 *
 */
public class AddressDefaultRequest extends BaseRequest{

	private String address;  //地址ID

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}
