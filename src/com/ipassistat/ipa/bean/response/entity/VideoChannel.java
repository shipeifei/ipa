package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频频道类
 * 
 * @Description
 * @author lis
 * @date 2015-4-20
 * 
 */
public class VideoChannel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 所处位置
	 */
	private String channel_page = "";

	/**
	 * 频道名称
	 */
	private String channel_name = "";

	/**
	 * 频道编号
	 */
	private String channel_code = "";

	/**
	 * 视频列表
	 */
	List<VideoList> videoList = new ArrayList<VideoList>();

	/**
	 * 帖子列表
	 */
	private List<PostsList> posts;

	public String getChannel_page() {
		return channel_page;
	}

	public void setChannel_page(String channel_page) {
		this.channel_page = channel_page;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public List<VideoList> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<VideoList> videoList) {
		this.videoList = videoList;
	}

	public List<PostsList> getPosts() {
		return posts;
	}

	public void setPosts(List<PostsList> posts) {
		this.posts = posts;
	}

}
