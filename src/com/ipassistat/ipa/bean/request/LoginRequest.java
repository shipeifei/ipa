package com.ipassistat.ipa.bean.request;
/**
 * 登录请求参数对象
 * @author lxc
 *
 */
public class LoginRequest extends BaseRequest {
	
	private String login_name;
	
	private String password;
	/**
	 * 注册类型。可选值:app(手机APP注册)，site(网站注册)。手机APP注册时会发送手机验证码。
	 */
	private String client_source;
	

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClient_source() {
		return client_source;
	}

	public void setClient_source(String client_source) {
		this.client_source = client_source;
	}


	
}
