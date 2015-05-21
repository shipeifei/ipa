package com.ipassistat.ipa.business;

import android.content.Context;

import com.ipassistat.ipa.bean.request.OfficialRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.OfficialResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;
import com.ipassistat.ipa.util.GlobalUtil;

/**
 * 官方活动模块
 * 
 * @author lxc
 *
 */
public class OfficialCenterModule extends BaseModule {

	public OfficialCenterModule(HttpRequestLisenter dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取官方活动
	 * 
	 * @param context
	 * @param limit
	 *            每页条数
	 * @param offset
	 *            起码页号
	 */
	public void getOfficial(Context context, int limit, int offset) {
		OfficialRequest req = new OfficialRequest();
		PageOption opt = new PageOption();
		opt.setLimit(limit);
		opt.setOffset(offset);
		req.setPicWidth(GlobalUtil.displayMetrics.widthPixels + "");
		req.setPaging(opt);
		req.tag = offset;
		getDataByPost(context, ConfigInfo.API_OFFICIAL_LIST, req, OfficialResponse.class);
	}

}
