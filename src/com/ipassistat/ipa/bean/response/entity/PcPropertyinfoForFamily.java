package com.ipassistat.ipa.bean.response.entity;
/**
 * 商品属性实体类
 * @author wr
 *
 */
public class PcPropertyinfoForFamily {
	private String propertyValue;//属性名称
	private String propertyKey;//属性编号
	private String sku_code;//sku编号
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyKey() {
		return propertyKey;
	}
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

}
