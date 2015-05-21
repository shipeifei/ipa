package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 更新日期
	 */
	private String recreation_updatetime = "";

	/**
	 * 图片
	 */
	private List<PicAllInfo> picInfos = new ArrayList<PicAllInfo>();

	/**
	 * 视频名称
	 */
	private String recreation_name = "";

	/**
	 * 更新集数
	 */
	private int recreation_updatesum;

	/**
	 * 链接地址
	 */
	private String recreation_url = "";
	

	/**
	 * 播放时长
	 */
	private String playing_time = "";
	public String getRecreation_updatetime() {
		return recreation_updatetime;
	}

	public void setRecreation_updatetime(String recreation_updatetime) {
		this.recreation_updatetime = recreation_updatetime;
	}

	public List<PicAllInfo> getPicInfos() {
		return picInfos;
	}

	public void setPicInfos(List<PicAllInfo> picInfos) {
		this.picInfos = picInfos;
	}

	public String getRecreation_name() {
		return recreation_name;
	}

	public void setRecreation_name(String recreation_name) {
		this.recreation_name = recreation_name;
	}

	public int getRecreation_updatesum() {
		return recreation_updatesum;
	}

	public void setRecreation_updatesum(int recreation_updatesum) {
		this.recreation_updatesum = recreation_updatesum;
	}

	public String getRecreation_url() {
		return recreation_url;
	}

	public void setRecreation_url(String recreation_url) {
		this.recreation_url = recreation_url;
	}

	public String getPlaying_time() {
		return playing_time;
	}

	public void setPlaying_time(String playing_time) {
		this.playing_time = playing_time;
	}


}
