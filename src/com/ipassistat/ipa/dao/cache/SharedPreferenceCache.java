/*
 * SharedPreferenceCache.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.SharedPreferenceCache
 * 时培飞 Create at 2014-12-10 上午10:50:51
 */
package com.ipassistat.ipa.dao.cache;

import com.ipassistat.ipa.util.SharedPreferenceUtil;

import android.content.Context;

/**
 * com.ichsy.mboss.cache.SharedPreferenceCache
 * 
 * @author 时培飞 Create at 2014-12-10 上午10:50:51
 */
public class SharedPreferenceCache implements CacheSubject {

	private static SharedPreferenceCache mSDao;
	private Context mContext;

	public SharedPreferenceCache(Context context) {
		this.mContext = context;
	}

	/**
	 * @discretion 使用单例模式
	 * @author 时培飞 Create at 2014-12-10 上午10:18:27
	 */
	public static SharedPreferenceCache getInstance(Context context) {
		if (mSDao == null) {
			mSDao = new SharedPreferenceCache(context);
		}
		return mSDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ichsy.mboss.cache.CacheSubject#saveData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveData(String key, String content) {
		SharedPreferenceUtil.putStringInfo(mContext, key, content);
	

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ichsy.mboss.cache.CacheSubject#getData(java.lang.String)
	 */
	@Override
	public String getData(String key) {
		
		return SharedPreferenceUtil.getStringInfo(mContext, key) == null ? null
				: SharedPreferenceUtil.getStringInfo(mContext, key);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ichsy.mboss.cache.CacheSubject#setSaveType(int)
	 */
	@Override
	public void setSaveType(int type) {
		

	}

}
