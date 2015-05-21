/*
 * CacheFactory.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.CacheFactory
 * 时培飞 Create at 2014-12-11 上午9:25:19
 */
package com.ipassistat.ipa.dao.cache;

import android.R.integer;
import android.content.Context;

/**
 * com.ichsy.mboss.cache.CacheFactory
 * 
 * @author 时培飞
 * @discretion 缓存工厂创建类 Create at 2014-12-11 上午9:25:19
 */
public class CacheFactory {
	/**
	 * 
	 * @author 时培飞
	 * @discretion 创建具体的缓存数据产品
	 * @param type
	 *            :1代表:txt、2代表:sqlite、3代表:shareprefences格式的保存 4:aSimpleCache缓存 Create at
	 *            2014-12-11 上午9:26:34
	 */
	public static CacheSubject createCacheSubject(int type, Context context) {
		CacheSubject product;

		switch (type) {

		case 1:// txt缓存数据
			
			product = TxtCache.getInstance(context);
			break;
		case 2:// sqlite缓存数据
			
			product = SqliteCache.getInstance(context);
			break;
		case 3:// shareprefences 缓存数据
			
			product = SharedPreferenceCache.getInstance(context);
			break;
		case 4:
			
			product=ASimpleCache.shareASimpleCache(context);
			
			break;
		default:// 默认缓存数据为:sqlite
			product = SqliteCache.getInstance(context);
			break;
		}
		return product;
	}
}
