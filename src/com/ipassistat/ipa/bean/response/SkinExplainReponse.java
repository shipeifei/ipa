/*
 * SkinExplainReponse.java [V 1.0.0]
 * classes : com.ichsy.mboss.bean.response.SkinExplainReponse
 * 时培飞 Create at 2014-12-15 上午9:46:43
 */
package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.SkinHopeful;

/**
 * com.ichsy.mboss.bean.response.SkinExplainReponse
 * @author 时培飞 
 * Create at 2014-12-15 上午9:46:43
 */
public class SkinExplainReponse extends BaseResponse {
private List<SkinHopeful> hopefulList;

/**
 * @return the hopefulList
 */
public List<SkinHopeful> getHopefulList() {
	return hopefulList;
}

/**
 * @param hopefulList the hopefulList to set
 */
public void setHopefulList(List<SkinHopeful> hopefulList) {
	this.hopefulList = hopefulList;
}
}
