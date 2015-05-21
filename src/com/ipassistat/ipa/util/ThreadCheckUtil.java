/*
 * ThreadCheckUtil.java [V 1.0.0]
 * classes : com.ichsy.mboss.util.ThreadCheckUtil
 * 时培飞 Create at 2015-4-9 下午2:36:00
 */
package com.ipassistat.ipa.util;

import android.os.Bundle;
import android.util.Log;

/**
 * com.ichsy.mboss.util.ThreadCheckUtil
 * 
 * @author 时培飞 Create at 2015-4-9 下午2:36:00
 */
public class ThreadCheckUtil {
	public static long getThreadId() {
		Thread t = Thread.currentThread();
		return t.getId();
	}

	/**
	 * 获取单独线程信息
	 * 
	 * @return
	 */
	public static String getThreadSignature() {
		Thread t = Thread.currentThread();
		long l = t.getId();
		String name = t.getName();
		long p = t.getPriority();
		String gname = t.getThreadGroup().getName();
		return ("(Thread):" + name + ":(id)" + l + "(:priority)" + p + ":(group)" + gname);
	}

	/**
	 * 获取当前线程 信息
	 */
	public static void logThreadSignature() {
		Log.d("ThreadUtils", getThreadSignature());
	}

	public static void logThreadSignature(String name) {
		Log.d("ThreadUtils", name + ":" + getThreadSignature());
	}

	public static void sleepForInSecs(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 讲String放进Bundle 中
	 * 
	 * @param message
	 * @return
	 */
	public static Bundle getStringAsBundle(String message) {
		Bundle b = new Bundle();
		b.putString("message", message);
		return b;
	}

	/**
	 * 
	 * 获取Bundle的String
	 * 
	 * @param b
	 * @return
	 */
	public static String getStringFromABundle(Bundle b) {
		return b.getString("message");
	}
}
