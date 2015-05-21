package com.ipassistat.ipa.bean.request;
/**
 * 忘记密码
 * @author Administrator
 *
 */
public class ForgetPwdRequest extends BaseRequest{
	private String login_name;
	private String password;
	private String verify_code;
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
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}
	public String getClient_source() {
		return client_source;
	}
	public void setClient_source(String client_source) {
		this.client_source = client_source;
	}
	
}
