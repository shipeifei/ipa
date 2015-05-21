package com.ipassistat.ipa.bean.response;

import java.util.ArrayList;
import java.util.List;

import com.ipassistat.ipa.bean.response.entity.RecreationListVo;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;

/**
 * 娱乐接口的返回
 * 
 * Package: com.ichsy.mboss.bean.response
 * 
 * File: RecreationResponse.java
 * 
 * @author: LiuYuHang Date: 2015年2月3日
 *
 *          Modifier： Modified Date： Modify：
 * 
 *          Copyright @ 2015 Corpration CHSY
 * 
 */
public class RecreationResponse extends BaseResponse {

	/**
	 * 栏目列表
	 */
//	private List<RecreationListVo> recreationList = new ArrayList<RecreationListVo>();
	
	private List<VideoChannel> channel = new ArrayList<VideoChannel>();

	public List<VideoChannel> getChannel() {
		return channel;
	}

	public void setChannel(List<VideoChannel> channel) {
		this.channel = channel;
	}
	
	

}
