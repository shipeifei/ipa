package com.ipassistat.ipa.bean.request;

/**
 * 验证码 验证是否正确
 * @author Administrator
 *
 */
public class VerifyCodeCheckRequest extends BaseRequest{
	/**
	 * 
	 */
	private String phone;
	
	/**
	 * 验证码
	 */
	private String verify;
	/**
	 * 发送类型	可选值：reginster(注册),forgetpassword(忘记密码)。
	 */
	private String type;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
