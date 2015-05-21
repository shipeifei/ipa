package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.response.entity.BeautyAddress;

/**
 * @Discription： 个人中心，和个人资料的返回数据
 * @package： com.ichsy.mboss.bean.response.UserInfoResponse
 * @author： MaoYaNan
 * @date：2014-10-8 下午3:09:01
 */
public class UserInfoResponse extends BaseResponse {
	/**
	 * 基本个人信息： 昵称，性别，头像，皮肤类型，期待改善方向，收货地址
	 */
	private String sex; // 性别
	private BeautyAddress adress; // 默认收货地址
	private String avatar; // 头像
	private String nickname; // 昵称
	private String birthday; // 皮肤类型
	private String century; //年代
	private String area_code;//地区

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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getCentury() {
		return century;
	}

	public void setCentury(String century) {
		this.century = century;
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
