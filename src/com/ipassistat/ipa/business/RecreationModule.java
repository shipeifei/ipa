package com.ipassistat.ipa.business;

import android.content.Context;

import com.ipassistat.ipa.bean.request.RecreationRequset;
import com.ipassistat.ipa.bean.request.VideoListRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.RecreationResponse;
import com.ipassistat.ipa.bean.response.VideoListResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.GlobalUtil;

/**
 * 娱乐请求数据Module
 * 
 * @Description
 * @author lis
 * @date 2015-4-20
 * 
 */
public class RecreationModule extends BaseModule {

	public RecreationModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
	}

	/**
	 * 获取频道列表
	 * 
	 * @param context
	 */
	public void getChannel(Context context) {
		RecreationRequset request = new RecreationRequset();
		request.setPicWidth(GlobalUtil.displayMetrics.widthPixels);
		getDataByPost(context, ConfigInfo.API_VIDEO_CHANNEL, request, RecreationResponse.class);
	}

	/**
	 * 获取视频列表
	 * 
	 * @param context
	 * @param recreation_type
	 * @param page
	 *            频道ID，翻页
	 */

	public void getVideoList(Context context, String recreation_type, int page) {
		VideoListRequest request = new VideoListRequest();
		request.recreation_type = recreation_type;
		request.picWidth = GlobalUtil.displayMetrics.widthPixels;
		request.paging = new PageOption();
		request.paging.setLimit(10);
		request.paging.setOffset(page);
		request.tag = page;
		getDataByPost(context, ConfigInfo.API_VIDEO_LIST, request, VideoListResponse.class);
	}
}
