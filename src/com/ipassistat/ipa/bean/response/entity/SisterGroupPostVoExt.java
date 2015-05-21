package com.ipassistat.ipa.bean.response.entity;

/**
 * 姐妹圈 - 列表详情 - 条目 扩展 的 实体类 
 * @author LiuYuHang
 * @date 2014年9月24日
 *
 */
public class SisterGroupPostVoExt extends SisterGroupPostVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 姐妹圈的特殊性，需要做特殊处理
	 * 
	 * 这个是处理姐妹圈 详情页 评论的扩展Vo
	 */
	public SisterGroupCommentVo native_comment;
}
