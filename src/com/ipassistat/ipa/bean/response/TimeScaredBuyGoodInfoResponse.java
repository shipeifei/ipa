package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.local.BaseProduct;

/**
 * @Discription： 限时抢购商品详情
 * @package： com.ichsy.mboss.bean.response.TimeScaredBuyResponse
 * @author： MaoYaNan
 * @date：2014-9-30 上午10:29:12
 */
public class TimeScaredBuyGoodInfoResponse extends BaseProduct {
	private String comment_count; // 评论数量
	private String oldPrice; // 商品原价
	private String newPrice; // 商品现价
	private String count; // 商品数量
	private String labels; // 商品标签
	private String endTime; // 结束时间
	private String remaind_count; // 商品剩余件数
	private String systemTime; // 当前服务器时间
	private String fieldServerEnd; // 服务器结束时间

	private String rebate; // 商品折扣

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRemaind_count() {
		return remaind_count;
	}

	public void setRemaind_count(String remaind_count) {
		this.remaind_count = remaind_count;
	}

	public String getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	public String getFieldServerEnd() {
		return fieldServerEnd;
	}

	public void setFieldServerEnd(String fieldServerEnd) {
		this.fieldServerEnd = fieldServerEnd;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

}
