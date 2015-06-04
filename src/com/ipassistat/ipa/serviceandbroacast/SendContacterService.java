package com.ipassistat.ipa.serviceandbroacast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.ipassistat.ipa.bean.request.ContactRequest;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.business.ContactModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.SharedPreferenceUtil;
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
public class SendContacterService extends IntentService implements HttpRequestLisenter {

	ContactModule contactModule=new ContactModule(this);
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
			// 获取保存在手机的联系人信息
			Map<String, ContactPerson> phoneMap = ContactsUtil.getSharedInstance().getPhoneContacts(this, false);
			phoneMap.putAll(ContactsUtil.getSharedInstance().getSIMContacts(this, false));

			if (phoneMap != null && phoneMap.size() > 0) {

				LogUtil.outLogDetail("", "读取手机联系人");

				// 遍历手机联系人

				// [{"mobile":"12345434545","name":"asdfsadf","letter":"a","py":"asdf"},]
				StringBuffer srBuffer = new StringBuffer();

				for (Map.Entry<String, ContactPerson> entry : phoneMap.entrySet()) {

					srBuffer.append(",{\"mobile\":\"" + entry.getValue().getPhoneNum() + "\",\"name\":\"" + entry.getValue().getName() + "\",\"letter\":\"" + entry.getValue().getLetter()
							+ "\",\"py\":\"" + entry.getValue().getPinYinName() + "\"}");

				}
				String result = srBuffer.toString().substring(1);
				StringBuffer resuBuffer = new StringBuffer();
				resuBuffer.append("[");
				resuBuffer.append(result);
				resuBuffer.append("]");
				
				//上传通讯录
				ContactRequest contactRequest=new ContactRequest();
				contactRequest.setContacts(resuBuffer.toString());
				contactModule.postContacterInfo(this, contactRequest);
				LogUtil.outLogDetail("", resuBuffer.toString());

			}

		} catch (Exception e) {
			LogUtil.outLogDetail("", "读取通讯录失败！！");
			return;
			// TODO: handle exception
		}

	}

	/**
	 * 把Map转化成list
	 * 
	 * @param map
	 *            需要转换的Map
	 * @param list
	 *            需要接受的list
	 */
	private List<ContactPerson> iteratorMap(Map<String, ContactPerson> map) {

		List<ContactPerson> list = new ArrayList<ContactPerson>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			list.add(map.get(key));
		}

		return list;
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		SharedPreferenceUtil.putStringInfo(this, ConfigInfo.IS_SEND_CONTACTER, "1");
		
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String ur, String result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoNet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoTimeOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}
