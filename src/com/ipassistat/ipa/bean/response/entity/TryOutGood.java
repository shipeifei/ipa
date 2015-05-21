package com.ipassistat.ipa.bean.response.entity;

import java.math.BigDecimal;

/**
 * @Discription： 试用商品列表
 * @package： com.ichsy.mboss.bean.response.TtyOutGood
 * @author： MaoYaNan
 * @date：2014-9-28 上午9:42:15
 */
public class TryOutGood {
	private String count; // 商品数量
	private String photo; // 商品图片
	private BigDecimal postage;// 邮费
	private String activity_code; // 活动ID
	private String id; // 商品sku编码
	private String time; // 试用商品结束时间
	private String system_time; // 试用商品当前系统时间
	private String fieldServerEnd; // 记录服务器时间
	private String price; // 商品价值
	private String status; // 申请状态
	private String surplus_count; // 商品剩余件数
	private String name; // 商品名称
	private String tryout_count; // 商品申请人数

	/**
	 * 449746930003:免费 449746930002:付邮
	 */
	private String is_freeShipping; // 试用商品类型

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getFieldServerEnd() {
		return fieldServerEnd;
	}

	public void setFieldServerEnd(String fieldServerEnd) {
		this.fieldServerEnd = fieldServerEnd;
	}

	public String getSurplus_count() {
		return surplus_count;
	}

	public void setSurplus_count(String surplus_count) {
		this.surplus_count = surplus_count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSystem_time() {
		return system_time;
	}

	public void setSystem_time(String system_time) {
		this.system_time = system_time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIs_freeShipping() {
		return is_freeShipping;
	}

	public void setIs_freeShipping(String is_freeShipping) {
		this.is_freeShipping = is_freeShipping;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getTryout_count() {
		return tryout_count;
	}

	public void setTryout_count(String tryout_count) {
		this.tryout_count = tryout_count;
	}

}
