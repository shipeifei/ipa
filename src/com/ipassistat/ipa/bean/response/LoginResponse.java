package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.response.entity.MemberConfig;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.ScoredChange;
/**
 * 登录返回值。
 * @author lxc
 *
 */
public class LoginResponse extends BaseResponse{

	private String user_token;
	/**
	 * 用户配置	
	 */
	private MemberConfig config;
	/**
	 * 积分变动
	 */
	private ScoredChange sChange;
	/**
	 * 用户资料	
	 */
	private MemberInfo user;
	
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}
	public MemberConfig getConfig() {
		return config;
	}
	public void setConfig(MemberConfig config) {
		this.config = config;
	}
	public ScoredChange getsChange() {
		return sChange;
	}
	public void setsChange(ScoredChange sChange) {
		this.sChange = sChange;
	}
	public MemberInfo getUser() {
		return user;
	}
	public void setUser(MemberInfo user) {
		this.user = user;
	}
	
}
