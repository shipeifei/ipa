package com.ipassistat.ipa.domain.action;

import com.ipassistat.ipa.util.SendSmsManager;

import android.content.Context;

/***
 *  发短信领域
 * @author shipeifei
 *
 */
public class SendMessageDomainAction implements IDomainAction {

	@Override
	public void action(Context context, String actionContent) {
		final Context _conContext=context;
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				SendSmsManager.getSharedInstance().sendMessage(_conContext, "18301298552", "测试发送短信内容");

			}
		});
		thread.start();
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
