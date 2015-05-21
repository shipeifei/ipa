package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 商品评论列表图片实体类
 * @author wr
 *
 */
public class PicAllInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private PicInfos smallPicInfo;//小图
	private PicInfos bigPicInfo;//大图
	private PicInfos picInfo;//原图
	public PicInfos getSmallPicInfo() {
		return smallPicInfo;
	}
	public void setSmallPicInfo(PicInfos smallPicInfo) {
		this.smallPicInfo = smallPicInfo;
	}
	public PicInfos getBigPicInfo() {
		return bigPicInfo;
	}
	public void setBigPicInfo(PicInfos bigPicInfo) {
		this.bigPicInfo = bigPicInfo;
	}
	public PicInfos getPicInfo() {
		return picInfo;
	}
	public void setPicInfo(PicInfos picInfo) {
		this.picInfo = picInfo;
	}

}
