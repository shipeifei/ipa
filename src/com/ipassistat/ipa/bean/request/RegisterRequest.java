package com.ipassistat.ipa.bean.request;

public class RegisterRequest extends BaseRequest{
	private String nickname;	//	昵称	昵称,最长30位

	private String login_name;
	/**
	 * app	客户端来源	注册类型。可选值:app(手机APP注册)，site(网站注册)。手机APP注册时会发送手机验证码。
	 */
	private String client_source;

	private String password;	

	private String verify_code;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getClient_source() {
		return client_source;
	}

	public void setClient_source(String client_source) {
		this.client_source = client_source;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}
	
	
	
}
