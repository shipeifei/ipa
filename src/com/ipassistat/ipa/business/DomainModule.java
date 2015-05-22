package com.ipassistat.ipa.business;

import android.content.Context;

import com.ipassistat.ipa.bean.request.UpdateCheckRequest;
import com.ipassistat.ipa.bean.response.UpdateCheckResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;

/***
 * 语音请求
 * @author shipeifei
 *
 */
public class DomainModule extends BaseModule {

	public DomainModule(HttpRequestLisenter dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}
	
//	public void checkUpdate(Context context,String versionApp,String versionCode){
//		UpdateCheckRequest updateCheckRequest = new UpdateCheckRequest();
//		updateCheckRequest.setVersionApp(versionApp);
//		updateCheckRequest.setVersionCode(versionCode);
//		updateCheckRequest.setIosAndroid(2);
//		getDataByPost(context, ConfigInfo.API_UPDATE_CHECK, updateCheckRequest, UpdateCheckResponse.class);
//	}
	
	

}
