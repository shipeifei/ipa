package com.ipassistat.ipa.view.navigation;

import android.content.Intent;

public interface NavigationController {
	
	/**
	 * 退回上一层activity
	 */
	public void back();

	/**
	 * 开启新的activity
	 * 
	 * @param intent
	 */
	public void pop(Intent intent);
}
