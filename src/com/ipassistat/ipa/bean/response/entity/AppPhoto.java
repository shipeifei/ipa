package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 用户头像类
 * @author lxc
 *
 */
public class AppPhoto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String thumb;//	 String	缩略图	
	private String large;//	 String	原图
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
