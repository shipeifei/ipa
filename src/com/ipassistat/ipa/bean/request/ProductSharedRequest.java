package com.ipassistat.ipa.bean.request;

/**
 * @Discription： 向服务器传递分享数据
 * @package： com.ichsy.mboss.bean.request.ProductSharedRequest
 * @author： MaoYaNan
 * @date：2014-9-28 下午5:26:34
 */
public class ProductSharedRequest extends BaseRequest {
	private String share_type; // 分享平台
	private String sku_code; // sku编码
	private String end_time; // 活动结束时间

	public String getShare_type() {
		return share_type;
	}

	public void setShare_type(String share_type) {
		this.share_type = share_type;
	}

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
