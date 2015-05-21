package com.ipassistat.ipa.constant;

import android.text.InputType;

/**
 * @Discription：
 * @package： com.ichsy.mboss.constant.InputTypeInfo
 * @author： ls
 * @date: 2015-02-09 上午9:58
 */
public class InputTypeInfo {

	public static final boolean DEBUG = true;// 是否是debug模式
	/** 密码可见 */
	public static final int VISIBLE_PASSWORD = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
	/** 密码不可见 */
	public static final int VARIATION_PASSWORD = InputType.TYPE_CLASS_TEXT
			| InputType.TYPE_TEXT_VARIATION_PASSWORD;

}
