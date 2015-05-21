/*
 * SendSmsManager.java [V 1.0.0]
 * classes : com.ipassistat.ipa.util.SendSmsManager
 * 时培飞 Create at 2015-5-5 上午9:39:40
 */
package com.ipassistat.ipa.util;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * 发送短信
 * com.ipassistat.ipa.util.SendSmsManager
 * 
 * @author 时培飞 Create at 2015-5-5 上午9:39:40
 */
public class SendSmsManager {

	
	private static SendSmsManager sendSmsManager;

	public static synchronized SendSmsManager getSharedInstance() {
		if (sendSmsManager == null) {
			sendSmsManager = new SendSmsManager();
		}
		return sendSmsManager;
	}
	
	/**
	 * 发送指定短信内容给指定的一个人
	 * 
	 * @param context
	 * @param mobile
	 * @param content
	 */
	public static void sendMessage(Context context, String mobile, String content) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		if (content.length() >= 70) {
			List<String> ms = smsManager.divideMessage(content);
			for (String str : ms) {
				smsManager.sendTextMessage(mobile, null, str, sentIntent, null);
			}
		} else {
			smsManager.sendTextMessage(mobile, null, content, sentIntent, null);
		}
	}

	/**
	 * 发送短信给联系人
	 * 
	 * @param context
	 * @param list
	 * @param content
	 */
	public static void sendMessageToList(Context context, String list, String content) {
		String[] split = list.split(",");

		String mobile = null;
		for (int i = 0; i < split.length; i++) {
			mobile = split[i];
			if (TextUtils.isEmpty(mobile)) {
				continue;
			}
			if (mobile.contains(":")) {
				mobile = mobile.substring(split[i].indexOf(":") + 1);
			}
			sendMessage(context, mobile, content);
		}
	}
}
