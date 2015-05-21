/*
 * MessageBroadCast.java [V 1.0.0]
 * classes : com.ichsy.mboss.broadcast.MessageBroadCast
 * 时培飞 Create at 2015-4-14 下午5:25:59
 */
package com.ipassistat.ipa.broadcast;

import com.ipassistat.ipa.bean.response.SystemMessageResponse;
import com.ipassistat.ipa.business.SisterGroupModule;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.eventbus.MainMessageEvent;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * com.ichsy.mboss.broadcast.MessageBroadCast
 * @author 时培飞 
 * Create at 2015-4-14 下午5:25:59
 */
public class MessageBroadCast extends BroadcastReceiver implements BusinessInterface {

	/** 消息数据处理 增加人：时培飞 增加于:2014-11-26 */
	private SisterGroupModule mSisterGroupModule;
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		

		mSisterGroupModule = new SisterGroupModule(this);
		if (UserModule.isLogin(context)) {
			mSisterGroupModule.messageOperationStateGet(context);

		}
		
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onMessageSucessCalledBack(java.lang.String, java.lang.Object)
	 */
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		// TODO Auto-generated method stub
		if (url.equals(ConfigInfo.API_MESSAGE_READ_STATE)) {
			SystemMessageResponse response = (SystemMessageResponse) object;

			if (response.count > 0) {// 有相关消息，设置消息个数

				//EventBus.getDefault().post(new MainMessageEvent("MessageService", response.count));

				return;
			}

			// 没有消失提示就隐藏消息提示图标
			//EventBus.getDefault().post(new MainMessageEvent("MessageService", 0));

		}
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onMessageFailedCalledBack(java.lang.String, java.lang.Object)
	 */
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onError(java.lang.String, java.lang.String)
	 */
	@Override
	public void onError(String ur, String result) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onNoNet()
	 */
	@Override
	public void onNoNet() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onNoTimeOut()
	 */
	@Override
	public void onNoTimeOut() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ichsy.mboss.dao.BusinessInterface#onFinish()
	 */
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}
