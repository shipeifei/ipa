/*
 * SkinType.java [V 1.0.0]
 * classes : com.ichsy.mboss.bean.response.SkinType
 * 时培飞 Create at 2014-12-12 上午9:52:02
 */
package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

import android.R.string;

/**
 * com.ichsy.mboss.bean.response.SkinType
 * @author 时培飞 
 * Create at 2014-12-12 上午9:52:02
 */
public class SkinType implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -6092498346732544527L;
private String skin_code;
private String skin_name;
/**
 * @return the skin_code
 */
public String getSkin_code() {
	return skin_code;
}
/**
 * @param skin_code the skin_code to set
 */
public void setSkin_code(String skin_code) {
	this.skin_code = skin_code;
}
/**
 * @return the skin_name
 */
public String getSkin_name() {
	return skin_name;
}
/**
 * @param skin_name the skin_name to set
 */
public void setSkin_name(String skin_name) {
	this.skin_name = skin_name;
}
}
