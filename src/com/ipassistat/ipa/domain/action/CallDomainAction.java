package com.ipassistat.ipa.domain.action;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ipassistat.ipa.domain.bean.CallDomainResponse;
import com.ipassistat.ipa.domain.bean.DomainBaseResponse;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;

public class CallDomainAction implements IDomainAction {

	@Override
	public void action(Object object,final Context context) {

		final CallDomainResponse domainResponse = (CallDomainResponse) object;
		if (domainResponse != null) {

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					@SuppressWarnings("static-access")
					String result = ContactsUtil.getSharedInstance().findPhoneNumber(domainContext.context, domainResponse.getSemantic().getIntent().getName());
					if (!TextUtils.isEmpty(result)) {
						IntentUtil.telePhone(context, result);
					} else {
						// IntentUtil.startSystemCallPhoneActivity(domainContext.context,
						// "");
						// ToastUtil.showToast(domainContext.context,
						// "没有找到此联系人");
					}
				}
			});
			thread.start();
		}
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
