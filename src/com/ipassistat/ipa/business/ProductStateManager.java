package com.ipassistat.ipa.business;

import java.util.HashMap;

/**
 * 商品详情页刷新的控制类，会保存本次开启应用后的商品操作
 * 
 * @author LiuYuHang
 * @date 2014年11月1日
 */
public class ProductStateManager {
	private final static String KEY_ID = "order_id";
	private static HashMap<String, String> mProductStateMap;

	private synchronized static HashMap<String, String> getProductMap() {
		if (mProductStateMap == null) mProductStateMap = new HashMap<String, String>();
		return mProductStateMap;
	}

	/**
	 * 当前商品是否在需要更新的列表中
	 *
	 * @param sku_code
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public static boolean isProductChanged(String sku_code) {
		return getProductMap().get(sku_code) != null;
	}

	/**
	 * 通知商品状态已经发生改变，需要UI刷新
	 *
	 * @param sku_code
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public static void notifyProductStateHasChanged(String sku_code) {
		getProductMap().put(sku_code, KEY_ID);
	}

	/**
	 * 如果UI已经最当前商品做过处理，从map中移除
	 *
	 * @param sku_code
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public static void notifyUiHasFlushed(String sku_code) {
		getProductMap().remove(sku_code);
	}

	/**
	 * 清理所有商品
	 *
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public static void clear() {
		if (mProductStateMap != null) getProductMap().clear();
	}
}
