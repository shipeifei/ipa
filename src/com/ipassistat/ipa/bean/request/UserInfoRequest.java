package com.ipassistat.ipa.bean.request;

import java.io.Serializable;

import com.ipassistat.ipa.bean.response.entity.BeautyAddress;

/**
 * UserInfo request
 * 
 * com.ichsy.mboss.bean.request.UserInfoRequest
 * 
 * @author maoyn<br/>
 *         create at 2014-9-23 下午5:53:30
 */
public class UserInfoRequest extends BaseRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sex = "";
	private BeautyAddress adress;
	private String birthday = "";
	private String nickname = "";
	private String avatar = "";
	private String area_code = "";


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BeautyAddress getAdress() {
		return adress;
	}

	public void setAdress(BeautyAddress adress) {
		this.adress = adress;
	}


	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}


}
