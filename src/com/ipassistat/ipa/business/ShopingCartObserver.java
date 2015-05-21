package com.ipassistat.ipa.business;

import java.util.List;

/**
 * 购物车观察者
 * 
 * @author LiuYuHang
 * @date 2014年11月1日
 */
public interface ShopingCartObserver<T> {
	/**
	 * 购物车数量发生变化的时候调用
	 *
	 * @param productCount
	 *            购物车商品种类总数
	 * @param totalCount
	 *            所有商品总数
	 * @param changeCount
	 *            变更的数量
	 * @author LiuYuHang
	 * @param <T>
	 * @date 2014年11月1日
	 */
	void onShopCartCountChanged(List<T> productArray, int totalCount, int changeCount);

	void onWarning(String message);

	void onError(int code, String message);
}
