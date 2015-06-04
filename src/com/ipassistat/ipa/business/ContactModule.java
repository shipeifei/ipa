package com.ipassistat.ipa.business;

import android.content.Context;
import com.ipassistat.ipa.bean.request.ContactRequest;
import com.ipassistat.ipa.bean.response.ContactsResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;

public class ContactModule extends BaseModule {

	public ContactModule(HttpRequestLisenter dataCallBack) {
		super(dataCallBack);

	}

	/***
	 * 提交联系人信息到服务器 create at 2015-5-27 author 时培飞
	 */
	public void postContacterInfo(Context context, ContactRequest request) {
		getDataByPost(context, ConfigInfo.POST_CONTACTER_INFO, request, ContactsResponse.class);

	}

}
