package com.ipassistat.ipa.domain.action;

import com.ipassistat.ipa.util.SendSmsManager;

import android.content.Context;

/***
 * 发短信领域
 * 
 * @author shipeifei
 * 
 */
public class SendMessageDomainAction implements IDomainAction {

	@Override
	public void action(Object object, Context context) {

		SendSmsManager.getSharedInstance().sendMessage(context, "18301298552", "测试发送短信内容");

	}

	@Override
	public void success() {
		// TODO Auto-generated method stub

	}

	@Override
	public void error() {
		// TODO Auto-generated method stub

	}

}
