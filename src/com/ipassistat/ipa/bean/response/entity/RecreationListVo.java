package com.ipassistat.ipa.bean.response.entity;  

import java.io.Serializable;
import java.util.List;

/**  
 * 
 * Package: com.ichsy.mboss.bean.response.entity  
 *  
 * File: RecreationListVo.java   
 *  
 * @author: LiuYuHang   Date: 2015年2月3日  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration CHSY  
 * 
 */
public class RecreationListVo implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -3997994316378419648L;

	/**
	 * 栏目ID
	 */
	private String recreationId;
	
	/**
	 * 栏目名称
	 */
	private String recreationTitle;
	
	private List<RecreationVo> recreations;

	public String getRecreationId() {
		return recreationId;
	}
	public void setRecreationId(String recreationId) {
		this.recreationId = recreationId;
	}

	public String getRecreationTitle() {
		return recreationTitle;
	}

	public void setRecreationTitle(String recreationTitle) {
		this.recreationTitle = recreationTitle;
	}

	public List<RecreationVo> getRecreations() {
		return recreations;
	}

	public void setRecreations(List<RecreationVo> recreations) {
		this.recreations = recreations;
	}
	
}
  