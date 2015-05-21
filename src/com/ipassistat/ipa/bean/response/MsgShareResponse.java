package com.ipassistat.ipa.bean.response;

import java.util.List;

public class MsgShareResponse extends BaseResponse {

	/**
	 * 发送失败的列表
	 */
	private List<String> error_tel;

	public List<String> getError_tel() {
		return error_tel;
	}

	public void setError_tel(List<String> error_tel) {
		this.error_tel = error_tel;
	}
	
}
