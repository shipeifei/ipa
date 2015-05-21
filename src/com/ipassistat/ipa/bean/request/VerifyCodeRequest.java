package com.ipassistat.ipa.bean.request;
/**
 * 获得验证码请求参数对象
 * @author lxc
 *
 */
public class VerifyCodeRequest extends BaseRequest {
	/**
	 * 发送类型
	 * 可选值：reginster(注册),login(登录),resetpassword(重置密码),forgetpassword(忘记密码),
	 * changephone(修改手机号)。
	 */
	private String send_type;
	
	private String mobile;

	public String getSend_type() {
		return send_type;
	}

	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
