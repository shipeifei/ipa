package com.ipassistat.ipa.bean.local;

import android.util.Log;

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
