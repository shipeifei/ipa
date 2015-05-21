/** 
 *mboss
 *com.ichsy.mboss.dao.cache
 *ASimpleCache.java
 * @version 1.0 
 * @author shipf
 * @date 2015-4-2
 */
package com.ipassistat.ipa.dao.cache;

import com.alipay.sdk.app.PayTask;

import android.app.Activity;
import android.content.Context;
import android.os.Message;

/***
 * 使用acache缓存数据，主要把数据缓存到手机文件里面
 * 
 * @author shipf
 * @date 2015-4-2
 * 
 * 
 */
public class ASimpleCache implements CacheSubject {

	private static ASimpleCache mSimpleCacheDao;
	private Context mContext;
	private static ACache mCache;

	public ASimpleCache(Context context) {
		this.mContext = context;
		mCache = ACache.get(mContext);
	}

	/**
	 * @discretion 使用单例模式
	 * @author 时培飞 Create at 2014-12-10 上午10:18:27
	 */
	public static ASimpleCache shareASimpleCache(Context context) {
		if (mSimpleCacheDao == null) {
			mSimpleCacheDao = new ASimpleCache(context);
		}
		return mSimpleCacheDao;
	}

	/**
	 * @version 1.0
	 * @author shipf
	 * @createDate 2015-4-2
	 * @updateDate
	 */
	@Override
	public void saveData(final String key, final String content) {
		// TODO Auto-generated method stub
		synchronized (this) {
			// 增加线程进行数据缓存提高相应时间
			mCache.put(key, content);
		}

	}

	/**
	 * @version 1.0
	 * @author shipf
	 * @createDate 2015-4-2
	 * @updateDate
	 */
	@Override
	public String getData(String key) {
		// TODO Auto-generated method stub

		return mCache.getAsString(key);

	}

	/**
	 * @version 1.0
	 * @author shipf
	 * @createDate 2015-4-2
	 * @updateDate
	 */
	@Override
	public void setSaveType(int type) {
		// TODO Auto-generated method stub

	}

}
