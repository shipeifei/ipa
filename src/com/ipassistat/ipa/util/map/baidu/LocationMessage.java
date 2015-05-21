package com.ipassistat.ipa.util.map.baidu;

import java.io.Serializable;

/***
 * 
 * @author shipeifei
 * 
 */
public class LocationMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4470637620621899569L;
	/* 当前位置* */
	private String currentAddress;
	/***
	 * 当前省份
	 */
	private String province;
	/***
	 * 当前城市
	 */
	private String city;

	/***
	 * 当前地区
	 */
	private String district;

	private double latitude;// 纬度
	private double longitude;// 经度
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
}
