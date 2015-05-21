/*
 * SkinType.java [V 1.0.0]
 * classes : com.ichsy.mboss.bean.response.SkinType
 * 时培飞 Create at 2014-12-12 上午9:20:49
 */
package com.ipassistat.ipa.bean.response;

import java.io.Serializable;
import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ProductCategory;
import com.ipassistat.ipa.bean.response.entity.SkinType;

/**
 * com.ichsy.mboss.bean.response.SkinType
 * @author 时培飞 
 * Create at 2014-12-12 上午9:20:49
 */
public class SkinTypeResponse extends BaseResponse  {

	private List<SkinType> skinList;//分类列表

	/**
	 * @return the skinList
	 */
	public List<SkinType> getSkinList() {
		return skinList;
	}

	/**
	 * @param skinList the skinList to set
	 */
	public void setSkinList(List<SkinType> skinList) {
		this.skinList = skinList;
	}

	
}
