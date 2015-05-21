package com.ipassistat.ipa.bean.response;

/**
 * 返回请求的基类
 * 
 * @author LiuYuHang
 *
 */
public class BaseResponse {

	/**
	 * 返回值 如果返回标记1则为API调用成功 否则则是错误编号
	 */
	private int resultCode;

	private Object tag;

	/**
	 * 前台提示
	 */
	private String resultMessage;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

}
