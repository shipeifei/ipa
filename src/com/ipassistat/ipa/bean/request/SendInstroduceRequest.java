package com.ipassistat.ipa.bean.request;
/**
 * 发送推荐联系人接口：短信推荐APP
 * @author 赵然
 * 创建时间：2014年11月26日12:14:22
 */
public class SendInstroduceRequest extends BaseRequest {
/**
 * APPID
 */
	private String app; 

	/**
	 * 被推荐人手机号	多个手机号用‘，’号分割
	 */
	private String tels;
	/**
	 * 	推荐人手机号	会员手机号
	 */
	private String mobile;
	public String getTels() {
		return tels;
	}
	public void setTels(String tels) {
		this.tels = tels;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	
	
}
