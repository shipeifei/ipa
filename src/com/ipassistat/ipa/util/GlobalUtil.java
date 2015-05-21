package com.ipassistat.ipa.util;

import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.ApiUrl;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class GlobalUtil {
	public static DisplayMetrics displayMetrics;

	public static void init(Context context) {
		displayMetrics = context.getResources().getDisplayMetrics();

		// File ext = Environment.getExternalStorageDirectory();
		// File cacheDir = new File(ext, ConfigInfo.DEFAULT_DISK_DIR_CACHE);
		// cacheDir.mkdirs();
		// AQUtility.setCacheDir(cacheDir);
	}

	/***
	 * @discretion 判断是否存在sd卡
	 * @author 时培飞 Create at 2015-1-5 下午3:07:19
	 */
	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	//在不同环境下做的操作
	public static void initByEnvironment(){
		if(!TextUtils.isEmpty(ApiUrl.getInStance().getAPI_URL_CURRENT())){
			if (ApiUrl.getInStance().getAPI_URL_CURRENT()
					.equals(ApiUrl.HOST_URL_PRODUCE)) {
				ConfigInfo.DEBUG = false;
			} else {
				ConfigInfo.DEBUG = true;
			}
		}
	}
}
