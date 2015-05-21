package com.ipassistat.ipa.bean.response.entity;


/**
 * 欢迎页接口实体
 * @author lxc
 *
 */
public class WelcomeEntity {
	/**
	 * 结束时间
	 */
	private String end_time;
	/**
	 * 开始时间
	 */
	private String start_time;
	/**
	 * 图片地址
	 */
	private String url;
	
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "WelcomeEntity [end_time=" + end_time + ", start_time="
				+ start_time + ", url=" + url + "]";
	}
	
	
	

}
