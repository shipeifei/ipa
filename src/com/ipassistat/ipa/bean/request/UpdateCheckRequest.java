package com.ipassistat.ipa.bean.request;
/**
 * 更新检查
 * @author Administrator
 *
 */
public class UpdateCheckRequest extends BaseRequest{

	/**
	 * 版本号	对应App版本添加时的版本号，区分大小写。如：V1.0、V1.1、V1.3等
	 */
	private String versionApp;
	/**
	 * 平台代码	对应每个系统的App的App_code值，如：刘嘉玲App的Code值是SI2001
	 */
	private String versionCode;
	/**
	 * ios :1 ,android:2
	 */
	private int iosAndroid;
	
	public String getVersionApp() {
		return versionApp;
	}
	public void setVersionApp(String versionApp) {
		this.versionApp = versionApp;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public int getIosAndroid() {
		return iosAndroid;
	}
	public void setIosAndroid(int iosAndroid) {
		this.iosAndroid = iosAndroid;
	}
	
	
}
