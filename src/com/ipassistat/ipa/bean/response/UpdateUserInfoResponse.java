package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.response.entity.UserInfo;

/**
 * 修改用户信息后的返回值
 * 
 * com.ichsy.mboss.bean.response.UpdateUserInfoResponse
 * 
 * @author maoyn<br/>
 *         create at 2014-9-23 下午6:19:40
 */
public class UpdateUserInfoResponse extends BaseResponse {
	UserInfo userInfo;

	@Override
	public String toString() {
		return "UpdateUserInfoResponse [userInfo=" + userInfo + "]";
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
