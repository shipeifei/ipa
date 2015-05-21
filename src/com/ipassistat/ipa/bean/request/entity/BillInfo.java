package com.ipassistat.ipa.bean.request.entity;

/**
 * 发票信息
 * @author renheng
 *
 */
public class BillInfo {

	private String bill_Type; // String 发票类型 发票类型
								// 449746310001:普通发票,449746310002:增值税发票
	private String bill_detail; // String 发票内容 发票内容
	private String bill_title; // String 发票抬头 发票抬头
	public String getBill_Type() {
		return bill_Type;
	}
	public void setBill_Type(String bill_Type) {
		this.bill_Type = bill_Type;
	}
	public String getBill_detail() {
		return bill_detail;
	}
	public void setBill_detail(String bill_detail) {
		this.bill_detail = bill_detail;
	}
	public String getBill_title() {
		return bill_title;
	}
	public void setBill_title(String bill_title) {
		this.bill_title = bill_title;
	}
	
	
	
}
