package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

/**
 * 用户配置
 * @author lxc
 *
 */
public class MemberConfig {
	/**
	 * 关联账号
	 */
	private List<MemberOther> accounts;
	/**
	 * 是否接受消息通知
	 */
	private int push;
	public List<MemberOther> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<MemberOther> accounts) {
		this.accounts = accounts;
	}
	public int getPush() {
		return push;
	}
	public void setPush(int push) {
		this.push = push;
	}
	
}
