package com.ipassistat.ipa.bean.local;

/**
 * 本地操作返回的对象。
 * @author Administrator
 *
 */
public class LocalResponeResult {

	/**
	 * 1:sucess other:fail
	 */
	private int status;
	
	private String error;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
