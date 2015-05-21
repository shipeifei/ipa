package com.ipassistat.ipa.bean.response.entity;

public class PicInfo {
	private int height;
	private int width;
	private String picNewUrl;
	private String picOldUrl;
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getPicNewUrl() {
		return picNewUrl;
	}
	public void setPicNewUrl(String picNewUrl) {
		this.picNewUrl = picNewUrl;
	}
	public String getPicOldUrl() {
		return picOldUrl;
	}
	public void setPicOldUrl(String picOldUrl) {
		this.picOldUrl = picOldUrl;
	}

}
