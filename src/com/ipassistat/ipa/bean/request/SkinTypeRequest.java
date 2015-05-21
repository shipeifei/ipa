/*
 * SkinTypeRequest.java [V 1.0.0]
 * classes : com.ichsy.mboss.bean.request.SkinTypeRequest
 * 时培飞 Create at 2014-12-12 上午9:17:50
 */
package com.ipassistat.ipa.bean.request;

import android.R.integer;

/**
 * com.ichsy.mboss.bean.request.SkinTypeRequest
 * @author 时培飞 
 * @discretion 护肤类型实体
 * Create at 2014-12-12 上午9:17:50
 */
public class SkinTypeRequest extends BaseRequest {
private int version;

/**
 * @return the version
 */
public int getVersion() {
	return version;
}

/**
 * @param version the version to set
 */
public void setVersion(int version) {
	this.version = version;
}
}
