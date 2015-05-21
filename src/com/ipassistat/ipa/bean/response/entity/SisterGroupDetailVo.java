package com.ipassistat.ipa.bean.response.entity;

/**
 * 姐妹圈 详情页的列表ITEM
 * 
 * @author LiuYuHang
 * @param <T>
 *
 */
public class SisterGroupDetailVo {

	/**
	 * 用户信息栏，包括帖子名字，阅读数和回复数
	 */
	public static final int TYPE_USERINFO = 1;
	/**
	 * 商品介绍栏
	 */
	public static final int TYPE_INTRO = 2;

	/**
	 * section栏
	 */
	public static final int TYPE_COMMENT_HEAD = 3;
	/**
	 * 评论栏
	 */
	public static final int TYPE_COMMENT = 4;

	/**
	 * 商品介绍栏的头部标题
	 */
//	public static final int TYPE_COMMENT_HEAD = 5;

	/**
	 * public static final int TYPE_USERINFO = 1;<br>
	 * public static final int TYPE_INTRO = 2;<br>
	 * public static final int TYPE_SECTION = 3;<br>
	 * public static final int TYPE_COMMENT = 4;<br>
	 */
	public int sectionType;

	public SisterGroupPostVo section;
}
