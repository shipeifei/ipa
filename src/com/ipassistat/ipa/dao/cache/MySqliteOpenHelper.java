package com.ipassistat.ipa.dao.cache; 

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @descrption
 * @author lxc   
 * @version 创建时间：2014年11月26日 上午11:46:37 
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {

	public static final String HML_DB = "hml.db";
	public static final String CACHE_TABLE = "hml_cache_table";
	public static final String URL = "url";
	public static final String API_CONTENT = "api_content";
	//数据库版本  
    private static final int VERSION = 1;  
    //新建一个表  
    String sql = "create table if not exists "+CACHE_TABLE+" "+  
    "(id int primary key,"+URL+" text,"+API_CONTENT+" text)";  
	
	public MySqliteOpenHelper(Context context) {
		super(context, HML_DB, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}

