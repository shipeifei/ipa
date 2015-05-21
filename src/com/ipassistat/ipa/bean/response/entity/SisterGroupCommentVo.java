package com.ipassistat.ipa.bean.response.entity;

/**
 * 姐妹圈-评论 实体类
 * 
 * @author LiuYuHang
 *
 */
public class SisterGroupCommentVo {

	/**
	 * 评论人信息 User
	 */
	public UserInfo postPublisherList;
	/**
	 * 被评论人信息
	 */
	public UserInfo publishedList;
	// 評論的編號
	public String comment_code;
	// 楼层数 string
	public int comment_floor;
	// 正文 string
	public String post_content;
	// 评论时间 string
	public String publish_time;
	// 点赞数
	public String post_praise;
	// 是否点赞
	public String ispraise;
	
	//新版本的图片实体类
	public ImageInfo[] picInfos;
	public SaleProduct productinfo;

	/**
	 * 0:针对帖子的评论；1：针对帖子评论的评论
	 */
	public String type;

}
