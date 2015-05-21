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

	public void getMsgShareList(Context context,ContactRequest request){
		getDataByPost(context, ConfigInfo.GET_SHARED_CONTACTS, request, ContactsResponse.class);
		
	}
}
