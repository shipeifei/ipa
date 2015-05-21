package com.ipassistat.ipa.bean.response.entity;
/**
 * 评论人信息实体类
 * @author wr
 *
 */
public class Commentator {
	private String nickname;//评论人昵称
	private String member_avatar;//头像URI
	private String skin_type;//肤质
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMember_avatar() {
		return member_avatar;
	}
	public void setMember_avatar(String member_avatar) {
		this.member_avatar = member_avatar;
	}
	public String getSkin_type() {
		return skin_type;
	}
	public void setSkin_type(String skin_type) {
		this.skin_type = skin_type;
	}
	
}
