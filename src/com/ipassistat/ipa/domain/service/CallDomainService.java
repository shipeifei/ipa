package com.ipassistat.ipa.domain.service;

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
	public boolean parseJsonInfo(String result) {

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
	public void selectAction() {
       CallDomainAction action=new CallDomainAction();
       action.action(domainBaseResponse);
	}

	@Override
	public String startService(String result) {
		if(parseJsonInfo(result))
		{
			selectAction();
			
		}
		else {
			
		}
		return null;
	}

}
