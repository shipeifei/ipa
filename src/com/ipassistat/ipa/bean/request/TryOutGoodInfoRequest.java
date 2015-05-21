package com.ipassistat.ipa.bean.request;

/**
 * @Discription： 试用商品详情数据请求
 * @package： com.ichsy.mboss.bean.request.PayTryOutGoodInfoRequest
 * @author： MaoYaNan
 * @date：2014-10-11 下午3:52:21
 */
public class TryOutGoodInfoRequest extends BaseRequest {
	private String sku_code; // 商品ID
	private String width; // 屏幕宽度
	private String end_time; // 活动结束时间

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

}
