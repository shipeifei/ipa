package com.ipassistat.ipa.domain;

import com.ipassistat.ipa.util.IntentUtil;

import android.content.Context;

/***
 * 打开浏览器领域
 * @author shipeifei
 *
 */
public class BroswerDomain implements IDomainAction {

	@Override
	public void action(Context context, String actionContent) {
		// TODO Auto-generated method stub
		IntentUtil.openBrowser(context, actionContent);
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
