package com.ipassistat.ipa.domain.service;

import android.content.Context;

import com.google.gson.Gson;
import com.ipassistat.ipa.domain.action.CallDomainAction;
import com.ipassistat.ipa.domain.bean.CallDomainResponse;
import com.ipassistat.ipa.domain.bean.DomainBaseResponse;

/***
 * 打电话通用服务
 * 
 * @author shipeifei
 * 
 */
public class CallDomainService extends IDoaminBaseService {

	@Override
	public boolean parseJsonInfo(String result,Context context) {

		boolean flag=true;
		try {
			domainBaseResponse = new CallDomainResponse();
			Gson gson = new Gson();
			domainBaseResponse = gson.fromJson(result, CallDomainResponse.class);
		} catch (Exception e) {
			domainBaseResponse=null;
			flag=false;
			// TODO: handle exception
		}
		return flag;
	
		
	}

	@Override
	public void selectAction(Context context) {
       CallDomainAction action=new CallDomainAction();
       action.action(domainBaseResponse,context);
	}

	@Override
	public String startService(String result,Context context) {
		if(parseJsonInfo(result,context))
		{
			selectAction(context);
			
		}
		else {
			
		}
		return null;
	}

}
