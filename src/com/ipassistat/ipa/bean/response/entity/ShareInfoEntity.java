/**
 * 
 */
package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 分享内容类
 * 
 * @Description
 * @author lis
 * @date 2015-2-27
 * 
 */
public class ShareInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 活动详情链接 */
	private String targetUrl = " ";
	/** 网络平台分享的内容 */
	private String content = " ";
	/** 短信平台分享的内容 */
	private String SMSContent = " ";
	/** 活动标题 */
	private String title = " ";
	/** 分享的图片链接 */
	private String picUrl = " ";
	/** 返回的埋点Code */
	private String backCode = " ";
	/** 分享的埋点Code */
	private String shareCode = " ";

	/**
	 * @return the backCode
	 */
	public String getBackCode() {
		return backCode;
	}

	/**
	 * @param backCode
	 *            the backCode to set
	 */
	public void setBackCode(String backCode) {
		this.backCode = backCode;
	}

	/**
	 * @return the shareCode
	 */
	public String getShareCode() {
		return shareCode;
	}

	/**
	 * @param shareCode
	 *            the shareCode to set
	 */
	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}

	/**
	 * @return the picUrl
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * @param picUrl
	 *            the picUrl to set
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	/**
	 * @return the targetUrl
	 */
	public String getTargetUrl() {
		return targetUrl;
	}

	/**
	 * @param targetUrl
	 *            the targetUrl to set
	 */
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the sMSContent
	 */
	public String getSMSContent() {
		return SMSContent;
	}

	/**
	 * @param sMSContent
	 *            the sMSContent to set
	 */
	public void setSMSContent(String sMSContent) {
		SMSContent = sMSContent;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
