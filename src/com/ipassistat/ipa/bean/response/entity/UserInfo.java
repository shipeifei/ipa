package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

import com.ipassistat.ipa.bean.response.BaseResponse;

/**
 * 修改用户信息后返回的数据
 * 
 * com.ichsy.mboss.bean.response.entity.UserInfo
 * 
 * @author maoyn<br/>
 *         create at 2014-9-23 下午6:17:40
 */
public class UserInfo extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String phone = "";
	private String nickname = ""; // 昵称
	private String userid = "";
	private String skin_type = ""; // 皮肤类型
	private String avatar = "";
	private String create_time = "";
	private String century; //	 String	年代
	private String sex;

	private long gender;
	private long group;
	private int level;
	private String level_name = "";
	private String member_code = "";
	private String mobile = "";
	private int score;
	private String score_unit = "";
	/**
	 * 用户头像地址
	 */
	private String member_avatar = "";

	@Override
	public String toString() {
		return "UserInfo [phone=" + phone + ", nickname=" + nickname + ", userid=" + userid + ", avatar=" + avatar + ", skin_type=" + skin_type + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSkin_type() {
		return skin_type;
	}

	public void setSkin_type(String skin_type) {
		this.skin_type = skin_type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public long getGender() {
		return gender;
	}

	public void setGender(long gender) {
		this.gender = gender;
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getScore_unit() {
		return score_unit;
	}

	public void setScore_unit(String score_unit) {
		this.score_unit = score_unit;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getMember_avatar() {
		return member_avatar;
	}

	public void setMember_avatar(String member_avatar) {
		this.member_avatar = member_avatar;
	}

	public String getCentury() {
		return century;
	}

	public void setCentury(String century) {
		this.century = century;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
