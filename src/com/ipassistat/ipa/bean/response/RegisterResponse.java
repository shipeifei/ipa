package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.response.entity.MemberConfig;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.ScoredChange;
import com.ipassistat.ipa.bean.response.entity.UserInfo;
/**
 * 用户注册返回值对象
 * @author lxc
 *
 */
public class RegisterResponse extends BaseResponse{
	private String user_token;
	/**
	 * 用户配置	
	 */
	private MemberConfig config;
	/**
	 * 积分变动
	 */
	private ScoredChange scored;
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
	public ScoredChange getScored() {
		return scored;
	}
	public void setScored(ScoredChange scored) {
		this.scored = scored;
	}
	public MemberInfo getUser() {
		return user;
	}
	public void setUser(MemberInfo user) {
		this.user = user;
	}
	
}
