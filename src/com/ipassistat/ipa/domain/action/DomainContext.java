package com.ipassistat.ipa.domain.action;

import android.content.Context;


/***
 * 执行领域上下文
 * @author shipeifei
 *
 */
public class DomainContext {

	private static DomainContext _instance = null;
	public Context context;

	private DomainContext() {
	}

	public static DomainContext getInstance() {
		if (_instance == null) {
			_instance = new DomainContext();

		}
		return _instance;
	}
}
