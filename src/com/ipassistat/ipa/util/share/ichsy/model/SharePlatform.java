package com.ipassistat.ipa.util.share.ichsy.model;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 分享实例的抽象接口
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 *
 */
public abstract class SharePlatform {
	public static int SHARE_TYPE_SINA = 1;

	public int witch;
	public int icon;
	public String title;
	public Context context;

	/**
	 * 分享组件的type
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 * @return
	 */
	protected abstract int type();

	/**
	 * 分享组件的icon
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 * @return
	 */
	protected abstract int iconId();

	/**
	 * 分享组件的提示
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 * @return
	 */
	protected abstract String title();

	public SharePlatform(Context context) {
		this.context = context;
		witch = type();
		icon = iconId();
		title = title();
	}

	/**
	 * 授权方法
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 */
	public abstract void authorize();

	/**
	 * 注销
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 */
	public abstract void loginOut();

	/**
	 * 分享文字
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 */
	public abstract void shareText(String content);

	/**
	 * 分享图片
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 */
	public abstract void shareImage(Bitmap bitmap);

}
