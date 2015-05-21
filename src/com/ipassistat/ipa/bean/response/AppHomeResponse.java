package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;

/**
 * 首页
 * 
 * Package: com.ichsy.mboss.bean.response  
 *  
 * File: AppHomeResponse.java   
 *  
 * @author: 任恒   Date: 2015-2-11  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration Name  
 *
 */
public class AppHomeResponse extends BaseResponse{

	public PageResults paged; //翻页实体
	
	public List<VideoChannel> channel; //频道列表
}
