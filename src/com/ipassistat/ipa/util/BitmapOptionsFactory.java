package com.ipassistat.ipa.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.ipassistat.ipa.R.drawable;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * 获取图片下载器配置信息的工具类
 * 
 * @author LiuYuHang
 * @date 2014年11月7日
 */
public class BitmapOptionsFactory {

	private static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 *
	 * @param context
	 *            application context
	 * @return
	 */
	public static BitmapUtils getInstance(Context context) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(context);
			bitmapUtils.configDefaultLoadFailedImage(drawable.default_goodsdetail_img);
			bitmapUtils.configDefaultLoadingImage(drawable.default_goodsdetail_img);
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		}
		return bitmapUtils;
	}

	public static BitmapDisplayConfig getOptionConfig(Context context) {
		return getOptionConfig(context, drawable.default_goodsdetail_img);
	}

	public static BitmapDisplayConfig getOptionConfig(Context context, int resId) {
		BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
		// bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		bitmapDisplayConfig.setLoadingDrawable(context.getResources().getDrawable(resId));
		bitmapDisplayConfig.setLoadFailedDrawable(context.getResources().getDrawable(resId));
		bitmapDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
		return bitmapDisplayConfig;
	}

	/**
	 * 实例化一个bitmap的下载工具类
	 *
	 * @param context
	 * @param loadingDrawable
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月7日
	 */
	public static BitmapUtils newInstanceBitmapUtils(Context context, int loadingDrawable) {
		// BitmapUtils bitmapUtils = new BitmapUtils(context);
		// bitmapUtils.configDefaultLoadFailedImage(loadingDrawable);
		// bitmapUtils.configDefaultLoadingImage(loadingDrawable);
		// return bitmapUtils;
		return getInstance(context);
	}

	/**
	 * 实例化一个bitmap的下载工具类
	 *
	 * @param context
	 * @param loadingDrawable
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月7日
	 */
	public static BitmapUtils newInstanceBitmapUtils(Context context) {
		return newInstanceBitmapUtils(context, drawable.default_goodsdetail_img);
	}

	// public static DisplayImageOptions getImageOption() {
	// return getImageOption(drawable.goodsdetail_default_img);
	// }
	//
	// public static DisplayImageOptions getImageOption(int drawableId) {
	// DisplayImageOptions options = new
	// DisplayImageOptions.Builder().showImageOnLoading(drawableId)
	// // 设置图片下载期间显示的图片
	// .showImageForEmptyUri(drawableId)
	// // 设置图片Uri为空或是错误的时候显示的图片
	// .showImageOnFail(drawableId)
	// // 设置图片加载或解码过程中发生错误显示的图片
	// .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
	// return options;
	// }

}
