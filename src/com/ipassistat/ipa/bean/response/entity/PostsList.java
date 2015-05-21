package com.ipassistat.ipa.bean.response.entity;

import java.util.List;

public class PostsList {
	private String post_label=""; //	 String	选择标签	
	private String linkUrl=""; //	 String	url	
	private String post_code="";	 //String	帖子ID	
	private String post_browse=""; // String	浏览量	
	private String post_praise=""; //	 String	点赞量	
	private String post_content=""	; //String	正文	
	private PostPublisherList postPublisherLists;	 //PostPublisherList	发布人信息列表		
	private String isofficial="";//	 String	官方帖	是：449746760001；否：449746760002
	private String publish_time=""; //	 String	发布时间	1天前
	private int post_count;//	 int	回复量	
	private String issessence=""; //	 String	精华帖	是：449746770001；否：449746770002
	private List<PicAllInfo> picInfos; //	 PicAllInfo[]	图片	
	private String post_img=""; //	 String	原图片	1.0版本返回图片字段名称
	private String post_title=""; //	 String	标题	
	private String ishot="";//	 String	是否火	是：449746880001；否：449746880002
	public String getPost_label() {
		return post_label;
	}
	public void setPost_label(String post_label) {
		this.post_label = post_label;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public String getPost_browse() {
		return post_browse;
	}
	public void setPost_browse(String post_browse) {
		this.post_browse = post_browse;
	}
	public String getPost_praise() {
		return post_praise;
	}
	public void setPost_praise(String post_praise) {
		this.post_praise = post_praise;
	}
	public String getPost_content() {
		return post_content;
	}
	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}
	public String getIsofficial() {
		return isofficial;
	}
	public void setIsofficial(String isofficial) {
		this.isofficial = isofficial;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public int getPost_count() {
		return post_count;
	}
	public void setPost_count(int post_count) {
		this.post_count = post_count;
	}
	public String getIssessence() {
		return issessence;
	}
	public void setIssessence(String issessence) {
		this.issessence = issessence;
	}
	public List<PicAllInfo> getPicInfos() {
		return picInfos;
	}
	public void setPicInfos(List<PicAllInfo> picInfos) {
		this.picInfos = picInfos;
	}
	public String getPost_img() {
		return post_img;
	}
	public void setPost_img(String post_img) {
		this.post_img = post_img;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getIshot() {
		return ishot;
	}
	public void setIshot(String ishot) {
		this.ishot = ishot;
	}
	public PostPublisherList getPostPublisherLists() {
		return postPublisherLists;
	}
	public void setPostPublisherLists(PostPublisherList postPublisherLists) {
		this.postPublisherLists = postPublisherLists;
	}

	
}
