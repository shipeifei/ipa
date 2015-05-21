/*
 * SqliteCache.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.SqliteCache
 * 时培飞 Create at 2014-12-10 上午10:16:37
 */
package com.ipassistat.ipa.dao.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * com.ichsy.mboss.cache.SqliteCache
 * @author 时培飞 
 * @discretion sqlite数据缓存实现类
 * Create at 2014-12-10 上午10:16:37
 */
public class SqliteCache implements CacheSubject {
	private MySqliteOpenHelper mSqliteOpenHelper;
	private static SqliteCache mCacheDao;
	private SQLiteDatabase mSqLiteDatabase;
	public SqliteCache(Context context) {
		mSqliteOpenHelper = new MySqliteOpenHelper(context);
		mSqLiteDatabase = mSqliteOpenHelper.getWritableDatabase();
	}
	/**
	 * @discretion 使用单例模式
	 * @author 时培飞
	 * Create at 2014-12-10 上午10:18:27
	 */
	public static SqliteCache getInstance(Context context){
		if (mCacheDao == null) {
			mCacheDao = new SqliteCache(context);
		}
		return mCacheDao;
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
	public void saveData(String key, String content) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySqliteOpenHelper.URL, key);
		contentValues.put(MySqliteOpenHelper.API_CONTENT, content);
		Cursor cursor = getCursor(key);
		if (cursor.moveToFirst()) {
			mSqLiteDatabase.update(MySqliteOpenHelper.CACHE_TABLE, contentValues, MySqliteOpenHelper.URL + "= ?", new String[]{key});
		}else {
			mSqLiteDatabase.insertOrThrow(MySqliteOpenHelper.CACHE_TABLE, null, contentValues);
		}
		cursor.close();
	}

	/* (non-Javadoc)
	 * @see com.ichsy.mboss.cache.CacheSubject#getData(java.lang.String)
	 */
	@Override
	public String getData(String key) {
		Cursor cursor = getCursor(key);
		String apiContent = null;
		if (cursor.moveToFirst()) {
			
			apiContent = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.API_CONTENT));
		}
		cursor.close();
		return apiContent;
	}

	/* (non-Javadoc)
	 * @see com.ichsy.mboss.cache.CacheSubject#setSaveType(int)
	 */
	@Override
	public void setSaveType(int type) {
		

	}
	/**
	 * @discretion 根据保存键获取缓存内容
	 * @author 时培飞
	 * Create at 2014-12-10 上午10:20:55
	 */
	private Cursor getCursor(String key){
		Cursor cursor = mSqLiteDatabase.query(MySqliteOpenHelper.CACHE_TABLE, null, MySqliteOpenHelper.URL + "= ?",new String[]{key}, null, null, null);
		return cursor;
	}

}
