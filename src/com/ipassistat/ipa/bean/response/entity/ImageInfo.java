package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 图片对象的实体类
 * 
 * @author LiuYuHang
 * @date 2014年12月5日
 */
public class ImageInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageInfoVo smallPicInfo = new ImageInfoVo(); // PicInfos 小图
	public ImageInfoVo bigPicInfo = new ImageInfoVo();// PicInfos 大图
	public ImageInfoVo picInfo = new ImageInfoVo(); // PicInfos 原图

}
