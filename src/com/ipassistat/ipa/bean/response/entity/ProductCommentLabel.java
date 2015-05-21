package com.ipassistat.ipa.bean.response.entity;
/**
 * 印象标签实体类
 * @author wr
 *
 */
public class ProductCommentLabel {
	private int label_amount;//印象标签数量
	private String label;//印象标签名称
	public int getLabel_amount() {
		return label_amount;
	}
	public void setLabel_amount(int label_amount) {
		this.label_amount = label_amount;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label; 
	}

}
