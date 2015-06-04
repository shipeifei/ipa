package com.ipassistat.ipa.domain.action;

import com.ipassistat.ipa.util.IntentUtil;

import android.content.Context;

/***
 * 打开浏览器领域
 * @author shipeifei
 *
 */
public class BroswerDomainAction implements IDomainAction {

	@Override
	public void action(Object object,Context context) {
		// TODO Auto-generated method stub
		IntentUtil.openBrowser(domainContext.context, "");
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
