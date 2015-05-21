package com.ipassistat.ipa.bean.local;

import java.io.Serializable;

/**
 * 
 * 
 * Package: com.ichsy.mboss.bean.local  
 *  
 * File: CityLocalEntity.java   
 *  
 * @author: 任恒   Date: 2015-2-3  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration Name  
 *
 */
public class CityLocalEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cityID;
	private String cityName;
	
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
