package com.ipassistat.ipa.bean.response;

import java.util.List;


public class ContactsResponse extends BaseResponse {
	/**
	 * 	是否有推荐	1:推荐过,0:没有推荐过
	 */
	
	private String bound_status;
	/**
	 * 已推荐联系人列表
	 */
	private List<String> mobile;
	public String getBound_status() {
		return bound_status;
	}
	public void setBound_status(String bound_status) {
		this.bound_status = bound_status;
	}
	public List<String> getMobile() {
		return mobile;
	}
	public void setMobile(List<String> mobile) {
		this.mobile = mobile;
	} 

	
}
