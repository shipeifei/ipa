package com.ipassistat.ipa.business;

import android.content.Context;

import com.ipassistat.ipa.bean.request.SendInstroduceRequest;
import com.ipassistat.ipa.bean.response.MsgShareResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;

public class InstroduceModul extends BaseModule {

	public InstroduceModul(HttpRequestLisenter dataCallBack) {
		super(dataCallBack);
	}

	public void postMsgShareList(Context context, SendInstroduceRequest request){
		getDataByPost(context, ConfigInfo.MSG_SHARE_SENDSHARELIST, request, MsgShareResponse.class);
	}
}
