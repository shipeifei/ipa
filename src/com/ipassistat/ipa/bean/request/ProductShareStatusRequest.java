package com.ipassistat.ipa.bean.request;

/**
 * @Discription： shared status request
 * @package： com.ichsy.mboss.bean.request.ProductShareStatusRequest
 * @author： MaoYaNan
 * @date：2014-9-28 下午5:15:21
 */
public class ProductShareStatusRequest extends BaseRequest {
	private String sku_code;
	private String end_time; // 活动结束时间

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
