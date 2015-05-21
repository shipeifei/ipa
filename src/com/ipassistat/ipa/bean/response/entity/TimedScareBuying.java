package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 限时抢购的商品实体类
 * 
 * com.ichsy.mboss.bean.response.entity.TimedScareBuying
 * 
 * @author maoyn<br/>
 *         create at 2014-9-23 上午10:02:49
 */
public class TimedScareBuying implements Serializable {
	private String oldPrice; // 商品原价
	private String systemTime; // 当前服务器时间
	private String newPrice; // 商品现价
	private String name; // 产品名称
	private String sku_code; // 商品sku编码
	private String rebate; // 商品折扣
	private String photoUrl; // 商品图片url
	private String endTime; // 结束时间
	private String fieldServerEnd; // 服务器结束时间
	private String remaind_count; // 商品剩余数量

	public String getRemaind_count() {
		return remaind_count;
	}

	public void setRemaind_count(String remaind_count) {
		this.remaind_count = remaind_count;
	}

	public String getFieldServerEnd() {
		return fieldServerEnd;
	}

	public void setFieldServerEnd(String fieldServerEnd) {
		this.fieldServerEnd = fieldServerEnd;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
