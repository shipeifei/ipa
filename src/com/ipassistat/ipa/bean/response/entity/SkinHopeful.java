/*
 * SkinHopeful.java [V 1.0.0]
 * classes : com.ichsy.mboss.bean.response.entity.SkinHopeful
 * 时培飞 Create at 2014-12-15 上午9:47:27
 */
package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * com.ichsy.mboss.bean.response.entity.SkinHopeful
 * @author 时培飞 
 * @discretion 
 * Create at 2014-12-15 上午9:47:27
 */
public class SkinHopeful  implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -3766368476386791245L;
private String hopeful_code;
private String hopeful_name;
/**
 * @return the hopeful_code
 */
public String getHopeful_code() {
	return hopeful_code;
}
/**
 * @param hopeful_code the hopeful_code to set
 */
public void setHopeful_code(String hopeful_code) {
	this.hopeful_code = hopeful_code;
}
/**
 * @return the hopeful_name
 */
public String getHopeful_name() {
	return hopeful_name;
}
/**
 * @param hopeful_name the hopeful_name to set
 */
public void setHopeful_name(String hopeful_name) {
	this.hopeful_name = hopeful_name;
}
}
