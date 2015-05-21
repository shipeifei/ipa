/*
 * CacheSubject.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.CacheSubject
 * 时培飞 Create at 2014-12-10 上午9:55:37
 */
package com.ipassistat.ipa.dao.cache;

/**
 * com.ichsy.mboss.cache.CacheSubject
 * 
 * @discretion :声明真实缓存对象和代理缓存对象的接口
 * @author 时培飞 Create at 2014-12-10 上午9:55:37
 */
public interface CacheSubject {
	/**
	 * @discretion 保存缓存数据包括：插入和更新
	 * @author 时培飞 Create at 2014-12-10 上午9:58:21
	 * @param key
	 *            :保存键
	 * @param content
	 *            :保存数据路径
	 * 
	 */
	void saveData(String key, String content);

	/**
	 * @discretion 获取缓存数据
	 * @author 时培飞 Create at 2014-12-10 上午10:07:09
	 * @param key
	 *            :保存键
	 */
	String getData(String key);

	/**
	 * @discretion 设置数据保存方式
	 * @author 时培飞 Create at 2014-12-10 上午10:06:21
	 * @param type
	 *            :保存方式可以实现；1代表:txt、2代表:sqlite、3代表:shareprefences格式的保存
	 */
	void setSaveType(int type);
}
