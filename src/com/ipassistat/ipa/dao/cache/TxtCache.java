/*
 * TxtCache.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.TxtCache
 * 时培飞 Create at 2014-12-10 上午10:24:07
 */
package com.ipassistat.ipa.dao.cache;

import android.content.Context;

/**
 * com.ichsy.mboss.cache.TxtCache
 * 
 * @author 时培飞
 * @discretion txt数据缓存实现类 Create at 2014-12-10 上午10:24:07
 */
public class TxtCache implements CacheSubject {

	private static TxtCache mTxtDao;
    private Context mContext;
	public TxtCache(Context context) {
		this.mContext=context;
	}

	/**
	 * @discretion 使用单例模式
	 * @author 时培飞 Create at 2014-12-10 上午10:18:27
	 */
	public static TxtCache getInstance(Context context) {
		if (mTxtDao == null) {
			mTxtDao = new TxtCache(context);
		}
		return mTxtDao;
	}

	/**
	 * @discretion 保存缓存数据包括：插入和更新
	 * @author 时培飞 Create at 2014-12-10 上午9:58:21
	 * @param key
	 *            :保存键
	 * @param content
	 *            :保存数据路径
	 * 
	 */
	@Override
	public synchronized void saveData(String key, String content) {
		
		CacheTools.writeFileByNIO(mContext, key, content);
         
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ichsy.mboss.cache.CacheSubject#getData(java.lang.String)
	 */
	@Override
	public String getData(String key) {
		
		Object obj=CacheTools.readByNIO(mContext, key);
		if(obj!=null)
		{
			return String.valueOf(obj);
		}
		else {
			return null;
		}
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
