package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 规格/款式
 * @author renheng
 *
 */
public class ApiSellerStandardAndStyleResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String standardAndStyleKey; //	规格/款式key	
	private String standardAndStyleValue; //	规格/款式value
	
	public String getStandardAndStyleKey() {
		return standardAndStyleKey;
	}
	public void setStandardAndStyleKey(String standardAndStyleKey) {
		this.standardAndStyleKey = standardAndStyleKey;
	}
	public String getStandardAndStyleValue() {
		return standardAndStyleValue;
	}
	public void setStandardAndStyleValue(String standardAndStyleValue) {
		this.standardAndStyleValue = standardAndStyleValue;
	}
	
	
}
