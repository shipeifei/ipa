package com.ipassistat.ipa.domain.action;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;

public class TelephoneDomainAction implements IDomainAction {

	@Override
	public void action(final Context context,String actionContent) {
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				@SuppressWarnings("static-access")
				String result = ContactsUtil.getSharedInstance().findPhoneNumber(context, "哥哥");
				if (!TextUtils.isEmpty(result)) {
					IntentUtil.telePhone(context, result);
				} else {
					ToastUtil.showToast(context, "没有找到此联系人");
				}
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
