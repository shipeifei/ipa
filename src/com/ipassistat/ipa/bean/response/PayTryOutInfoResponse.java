package com.ipassistat.ipa.bean.response;

import java.io.Serializable;

/**
 * @Discription： 付邮试用详情数据
 * @package： com.ichsy.mboss.bean.response.PayTryOutInfoResponse
 * @author： MaoYaNan
 * @date：2014-9-26 下午2:32:37
 */
public class PayTryOutInfoResponse extends BaseResponse implements Serializable {
	private String activityCode; // 活动编号
	private String linkUrl;
	private String count; // 商品件数
	private String status; // 申请试用状态
	private String tryout_price; // 商品试用价
	private String postage; // 邮费
	private String photo; // 商品图片
	private String old_price; // 商品原价
	private String surplus_count; // 商品剩余件数
	private String product_code; // 商品编码
	private String name; // 商品名称
	private String describe; // 试用须知描述
	private String sku_code; // sku编码
	private String fieldServerEnd; // 服务器结束时间
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSystemtime() {
		return systemtime;
	}

	public void setSystemtime(String systemtime) {
		this.systemtime = systemtime;
	}

	private String systemtime; // 试用商品当前系统时间

	public String getFieldServerEnd() {
		return fieldServerEnd;
	}

	public void setFieldServerEnd(String fieldServerEnd) {
		this.fieldServerEnd = fieldServerEnd;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getOld_price() {
		return old_price;
	}

	public void setOld_price(String old_price) {
		this.old_price = old_price;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTryout_price() {
		return tryout_price;
	}

	public void setTryout_price(String tryout_price) {
		this.tryout_price = tryout_price;
	}

	public String getSurplus_count() {
		return surplus_count;
	}

	public void setSurplus_count(String surplus_count) {
		this.surplus_count = surplus_count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

}
