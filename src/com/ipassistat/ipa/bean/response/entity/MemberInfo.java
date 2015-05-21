package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 用户类(此类和userinfo类相似，avatar类型不同，坑爹啊）
 * @author lxc
 *
 */
public class MemberInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户头像*/
	private String photo="";
	/**
	 * 等级名称	
	 */
	private String level_name="";
	/**
	 * 积分单位	
	 */
	private String score_unit="";
	/**
	 * 等级
	 */
	private int level;
	
	private String nickname="";
	/**
	 * 积分	
	 */
	private int score;
	/**
	 * 加入时间
	 */
	private String create_time="";	 	
	/**
	 * 性别	
	 */
	private long gender;
	
	private AppPhoto avatar;// 	用户头像	
	
	private long group;//	用户组
	/**
	 * 用户编号
	 */
	private String  member_code="";
	
	private String mobile;//用户电话	;
	
	/**
	 * 用户头像地址
	 */
	private String member_avatar="";
	
	private String birthday="";
	private String city="";
	private String century="";

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getScore_unit() {
		return score_unit;
	}

	public void setScore_unit(String score_unit) {
		this.score_unit = score_unit;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public AppPhoto getAvatar() {
		return avatar;
	}

	public void setAvatar(AppPhoto avatar) {
		this.avatar = avatar;
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
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

	
	public String getMember_avatar() {
		return member_avatar;
	}

	public void setMember_avatar(String member_avatar) {
		this.member_avatar = member_avatar;
	}




	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCentury() {
		return century;
	}

	public void setCentury(String century) {
		this.century = century;
	}
	
	
	
}
