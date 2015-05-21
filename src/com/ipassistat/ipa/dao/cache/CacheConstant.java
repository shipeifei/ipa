package com.ipassistat.ipa.dao.cache; 

import android.R.integer;

/**
 * @descrption
 * @author lxc   
 * @version 创建时间：2014年11月27日 上午10:17:04 
 */
public class CacheConstant {
	
	/**
	 * 缓存超时请求时间 
	 */
	public static final int CACHE_TIME_OUT = 10*1000;
	
	/**
	 * 正常网络请求时间（即不用）
	 */
	public static final int DEFAULT_TIME_OUT = 20*1000;
	/**
	 * 默认数据缓存方式为------------1代表:txt、2代表:sqlite、3代表:shareprefences格式的保存 4:代表aSimpleCache第三方缓存 增加人：时培飞 增加时间:2014-12-11
	 */
	public static final int CACHE_SAVE_TYPE=4;
}

