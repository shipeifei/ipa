package com.ipassistat.ipa.util;

import android.util.Log;

import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;

public class LogUtil {

	public static final String TAG = Constant.DEFAULT_DEBUG_TAG;

	public static boolean DEBUG = ConfigInfo.DEBUG;

	

	public static void outLogDetail(Object result) {
		outLogDetail(TAG, result);
	}

	public static void outLogDetail(String tag, Object result) {
		if (DEBUG) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			Log.i(TAG, ste.getFileName() + ": Line:" + ste.getLineNumber() + result);
		}
	}

	public static void outLogDetail(int result) {
		if (DEBUG) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			Log.i(TAG, ste.getFileName() + ": Line:" + ste.getLineNumber() + result);
		}
	}

	public static void outLogError(String result) {
		outLogError(TAG, result);
	}

	public static void outLogError(String tag, String result) {
		if (DEBUG) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			Log.e(TAG, ste.getFileName() + ": Line:" + ste.getLineNumber() + result);
		}
	}
}
