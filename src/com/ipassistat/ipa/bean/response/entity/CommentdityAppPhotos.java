package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

public class CommentdityAppPhotos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 原图高
	 */

	private String height;
	/**
	 * 原图宽
	 */
	private String width;

	/**
	 * 原图宽
	 */
	private String thumb;

	/**
	 * 原图
	 */
	private String large;

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

}
