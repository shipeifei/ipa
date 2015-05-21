package com.ipassistat.ipa.bean.request;

/**
 * 商品详情应用输入参数
 * 
 * @author wr
 * 
 */
public class TryOutInfoRequest extends BaseRequest {
	private String sku_code;// sku编码
	private String width;// 屏幕宽度
	private String end_time; // 活动结束时间
	private Integer picWidth;

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}
