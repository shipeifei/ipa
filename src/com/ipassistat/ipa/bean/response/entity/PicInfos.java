package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 商品评论列表图片实体类
 * @author wr
 *
 */
public class PicInfos  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String picUrl = "";//图片url
	
	private Integer height;//图片高度
	
	private Integer width;//图片宽度
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}

}
