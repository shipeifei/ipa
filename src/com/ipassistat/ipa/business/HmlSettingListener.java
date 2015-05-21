package com.ipassistat.ipa.business;

import android.content.Context;

/**
 * 设置页面的抽象接口
 * 
 * Package: com.ichsy.mboss.ui.business
 * 
 * File: HmlSettingListener.java
 * 
 * @author: LiuYuHang Date: 2015年2月2日
 *
 *          Modifier： Modified Date： Modify：
 * 
 *          Copyright @ 2015 Corpration CHSY
 * 
 */
public interface HmlSettingListener {

	/**
	 * push发生变化的处理
	 * 
	 * @param context
	 * @param isPush
	 * 
	 *            Modifier： Modified Date： Modify：
	 */
	void onPushSettingChanged(Context context, boolean isPush);

	/**
	 * 判断当前是否可以push
	 * 
	 * @param context
	 * @return
	 * 
	 *         Modifier： Modified Date： Modify：
	 */
	boolean isPushOpen(Context context);

}
