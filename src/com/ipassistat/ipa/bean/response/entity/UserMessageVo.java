package com.ipassistat.ipa.bean.response.entity;

/**
 * 消息页面，与我相关的消息
 * 
 * @author LiuYuHang
 *
 */
public class UserMessageVo extends SystemMessageVo {
	// 用户 UserInfo
	public UserInfo messageUser;

	public String old_comment;

	/**
	 * 相关帖子的ID
	 */
	public String post_code;
	
}
