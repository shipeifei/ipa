package com.ipassistat.ipa.service;

import java.util.Map;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.push.baidu.PushUtils;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

/***
 * 发送通讯录联系人服务
 * 
 * @author shipeifei
 * 
 */
public class SendContacterService extends IntentService {

	public SendContacterService() {
		super("SendContacterService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			//DeviceUtil.isExistsSIM(this);
			String userId = PushUtils.getUserId(this);
			String channelId = PushUtils.getChannelId(this);
			LogUtil.outLogDetail("USERID", userId);
			LogUtil.outLogDetail("CHANNELID", channelId);

			// 获取保存在手机的联系人信息
			Map<String, ContactPerson> phoneMap = ContactsUtil.getSharedInstance().getPhoneContacts(this);
			if (phoneMap != null && phoneMap.size() > 0) {

				LogUtil.outLogDetail("", "读取手机联系人");
				// 遍历手机联系人
				for (Map.Entry<String, ContactPerson> entry : phoneMap.entrySet()) {
					System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				}
			}

			// 获取sim卡里面的联系人
			Map<String, ContactPerson> simMap = ContactsUtil.getSharedInstance().getSIMContacts(this);
			if (simMap != null && simMap.size() > 0) {

				LogUtil.outLogDetail("", "读取sim卡联系人");
				// 遍历联系人
				for (Map.Entry<String, ContactPerson> entry : simMap.entrySet()) {
					System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				}
			}

		} catch (Exception e) {
			LogUtil.outLogDetail("", "读取通讯录失败！！");
			return;
			// TODO: handle exception
		}

	}

}
