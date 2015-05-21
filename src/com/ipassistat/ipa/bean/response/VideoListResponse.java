package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.VideoList;

public class VideoListResponse extends BaseResponse{

	/**
	 * 频道名称
	 */
	private String channel_name;
	
	/**
	 * 翻页结果
	 */
	private PageResults paged;
	
	/**
	 * 视频列表
	 */
	private List<VideoList> videoList;

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public List<VideoList> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<VideoList> videoList) {
		this.videoList = videoList;
	}
	
	
}
