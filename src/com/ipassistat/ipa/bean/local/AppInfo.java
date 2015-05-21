/*
 * AppInfo.java [V 1.0.0]
 * classes : com.ipassistat.ipa.bean.local.AppInfo
 * 时培飞 Create at 2015-5-4 上午9:21:14
 */
package com.ipassistat.ipa.bean.local;

import android.util.Log;

/**
 * com.ipassistat.ipa.bean.local.AppInfo
 * @author 时培飞 
 * Create at 2015-5-4 上午9:21:14
 */
public class AppInfo {
	public String appName = "";
	public String packageName = "";
	public String versionName = "";
	public int versionCode = 0;
	

	public void print() {
		Log.v("app", "Name:" + appName + " Package:" + packageName);
		Log.v("app", "Name:" + appName + " versionName:" + versionName);
		Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
	}
}
