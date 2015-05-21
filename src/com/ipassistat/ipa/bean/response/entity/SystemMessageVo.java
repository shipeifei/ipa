package com.ipassistat.ipa.bean.response.entity;

/**
 * 消息的父类
 * 
 * @author LiuYuHang
 *
 */
public class SystemMessageVo {
	public static final int TYPE_READ = 1;
	public static final int TYPE_NO_READ = 0;

	public static final String MESSAGE_TYPE_ALERT = "449746910002";// 系统通知
	public static final String MESSAGE_TYPE_EDITER = "449746910001";// 小编提醒

	// 消息ID
	public String message_code;// 系统消息编码 1
	/**
	 * 消息类型例 通知:449746640004 ;小编提醒:449746640005
	 */
	public String message_type;// 122333 122333
	// 消息内容
	public String message_info;// 消息内容 回复消息
	// 时间
	public String create_time, send_time;// 创建时间 2009/07/07 21:51:22
	// 状态
	public int is_read;// 已读-1，未读-0 1

	// message_info String
	// create_time String
	// is_read int 已读-1，未读-0 1
	// message_type String 122333 122333
	// message_code String 系统消息编码 1
}
