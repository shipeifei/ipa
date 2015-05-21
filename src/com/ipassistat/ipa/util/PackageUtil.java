/*
 * PackageUtil.java [V 1.0.0]
 * classes : com.ipassistat.ipa.util.PackageUtil
 * 时培飞 Create at 2015-5-4 上午9:16:37
 */
package com.ipassistat.ipa.util;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import com.ipassistat.ipa.bean.local.AppInfo;

/**
 * 获取所有安装的应用程序 com.ipassistat.ipa.util.PackageUtil
 * 
 * @author 时培飞 Create at 2015-5-4 上午9:16:37
 */
public class PackageUtil {
	public static String SHAREDFRENCE_NAME = "PackageUtil";

	private static PackageUtil packageUtil;

	public static synchronized PackageUtil getSharedInstance() {
		if (packageUtil == null) {
			packageUtil = new PackageUtil();
		}
		return packageUtil;
	}

	/***
	 * 获取所有安装的应用程序
	 * 
	 * @author 时培飞 Create at 2015-5-4 上午9:23:14
	 */
	public static List<AppInfo> getAllPackages(Context context) {
		List<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		AppInfo tmpInfo;
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;
			/*
			 * tmpInfo.appIcon = packageInfo.applicationInfo
			 * .loadIcon(getPackageManager());
			 */
			tmpInfo.toString();
			appList.add(tmpInfo);

		}
		return appList;
	}

	/***
	 * 判断是否已经安装了指定的应用程序
	 * 
	 * @author 时培飞 Create at 2015-5-4 上午9:31:07
	 */
	public static String isExistsApp( Context context,String name) {
		AppInfo tmpInfo;
		String flag = "";
		List<AppInfo> lists=getAllPackages(context);
		if (lists != null && lists.size() > 0) {
			for (int i = 0; i < lists.size(); i++) {
				tmpInfo = (AppInfo) lists.get(i);
				if (tmpInfo.appName.indexOf(name) > -1) {
					flag = tmpInfo.packageName;
					break;
				}
			}
		}
		return flag;
	}

}
