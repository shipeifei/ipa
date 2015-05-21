package com.ipassistat.ipa.business;

import android.content.Context;

import com.baidu.android.pushservice.PushManager;


/**
 * 应用程序的全局设置工具类
 * 
 * @Package com.ichsy.mboss.ui.business
 * 
 * @File AppSettingController.java
 * 
 * @author LiuYuHang Date: 2015年3月2日
 *
 *         Modifier： Modified Date： Modify：
 * 
 *         Copyright @ 2015 Corpration CHSY
 *
 */
public class AppSettingController implements HmlSettingListener {
	private static AppSettingController appSettingControllerInstance;

	/**
	 * 是否允许推送消息(默认应该是可以推送的）
	 */
	// public boolean isPushMessage = true;
	private int mSisterGroupMessageCount = 0;

	public static AppSettingController getInstance() {
		if (appSettingControllerInstance == null) {
			appSettingControllerInstance = new AppSettingController();
		}
		return appSettingControllerInstance;
	}

	/*
	 * 
	 * 设置是否可以推送消息 (non-Javadoc)
	 * 
	 * @see
	 * com.ichsy.mboss.ui.business.HmlSettingListener#onPushSettingChanged
	 * (android.content.Context, boolean)
	 */
	@Override
	public void onPushSettingChanged(Context context, boolean isPush) {
		UserModule.saveReceiveStatus(context, isPush + "");// 将是否接收推送的状态保存到本地
		if (isPush) {
			PushManager.resumeWork(context);
		} else {
			PushManager.stopWork(context);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ichsy.mboss.ui.business.HmlSettingListener#isPushOpen(android
	 * .content.Context)
	 */
	@Override
	public boolean isPushOpen(Context context) {
		String isReceive = UserModule.getReceiveStatus(context);// 得到本地保存的是否接收推送的状态
		if (!isReceive.isEmpty()) {
			// 如果本地保存的数据不为空则根据状态设置
			if (isReceive.equals("true")) {
				
				return true;
			} else {
				
				return false;
			}
		}

		return true;
	}

	/**
	 * 圈子页面的推送数发生变化的时候
	 * 
	 * @param messageCount
	 * 
	 *            Modifier： Modified Date： Modify：
	 */
	public void onSisterGroupMessageCountChanged(int messageCount) {
		mSisterGroupMessageCount = messageCount;
	}

	/**
	 * 获取到当前圈子的消息数量
	
	 * @return
	
	 * Modifier： Modified Date： Modify：
	 */
	public int getSisterGroupMessageCount() {
		return mSisterGroupMessageCount;
	}
}
