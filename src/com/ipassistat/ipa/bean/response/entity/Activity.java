package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 活动
 * 
 * @author renheng
 * 
 */
public class Activity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_time = "";  // 活动结束日期
	private String name = ""; // 活动名称
	private String start_time = ""; // 活动开始日期
	private String photo = ""; // 活动图片
	private String url = ""; // 活动详情链接
	private String share_pic = "";// 分享图片
	private String info_content = ""; // 分享内容

	/**
	 * @return the share_pic
	 */
	public String getShare_pic() {
		return share_pic;
	}

	/**
	 * @param share_pic
	 *            the share_pic to set
	 */
	public void setShare_pic(String share_pic) {
		this.share_pic = share_pic;
	}

	/**
	 * @return the info_content
	 */
	public String getInfo_content() {
		return info_content;
	}

	/**
	 * @param info_content
	 *            the info_content to set
	 */
	public void setInfo_content(String info_content) {
		this.info_content = info_content;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
