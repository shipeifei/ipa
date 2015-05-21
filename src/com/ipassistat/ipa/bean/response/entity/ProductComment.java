package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

/**
 * 商品评论列表实体类
 * @author wr
 *
 */
public class ProductComment {
	private Commentator commentator;//评论人信息
	private String sku_name;// sku名称 
	private String comment_content;//评论内容
	private List<PicAllInfo> picInfos;//图片
	private String label;// 印象标签 
	private String sku_code;// sku编号 
	private String app_code;// app编号 
	private String comment_time;// 评论时间 
	public Commentator getCommentator() {
		return commentator;
	}
	public void setCommentator(Commentator commentator) {
		this.commentator = commentator;
	}
	public String getSku_name() {
		return sku_name;
	}
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public List<PicAllInfo> getPicInfos() {
		return picInfos;
	}
	public void setPicInfos(List<PicAllInfo> picInfos) {
		this.picInfos = picInfos;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

}
