package com.ipassistat.ipa.util;

import java.lang.reflect.Field;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreferences的工具类
 *
 */
public class SharePreferencesUtils {
	/**
	 * 存储单个属性
	 * @param field
	 * @param sp
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static void saveField(Field field ,SharedPreferences sp,Object object) throws IllegalArgumentException, IllegalAccessException{
		field.setAccessible(true);
		Class<?> fildType = field.getType();
		if(String.class == fildType || Character.class == fildType){
			sp.edit().putString(field.getName(), String.valueOf(field.get(object))).commit();
		}else if(Integer.TYPE == fildType || Integer.class == fildType){
			sp.edit().putInt(field.getName(), field.getInt(object)).commit();
		}else if(boolean.class == fildType){
			sp.edit().putBoolean(field.getName(), field.getBoolean(object)).commit();
		}else if(Long.class == fildType){
			sp.edit().putLong(field.getName(), field.getLong(object)).commit();
		}else if(Float.class == fildType){
			sp.edit().putFloat(field.getName(), field.getFloat(object)).commit();
		}
		
	}
	/**
	 * 拿到单个属性
	 * @param field
	 * @param sp
	 * @return
	 */
	private static String getFieldFromSp(Field field ,SharedPreferences sp){
		field.setAccessible(true);
		Class<?> fildType = field.getType();
		if(String.class == fildType || Character.class == fildType){
			return sp.getString(field.getName(), "");
		}else if(Integer.TYPE == fildType || Integer.class == fildType){
			return String.valueOf(sp.getInt(field.getName(), 0));
		}else if(Boolean.class == fildType){
			return String.valueOf(sp.getBoolean(field.getName(), false));
		}else if(Long.class == fildType){
			return String.valueOf(sp.getLong(field.getName(), 0L));
		}else if(Float.class == fildType){
			return String.valueOf(sp.getFloat(field.getName(), 0F));
		}
		return "";
		
	}
	/**
	 * 数据存储
	 * @param sp
	 * @param classzz
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  static void save(Context mContext, Object object) throws IllegalArgumentException, IllegalAccessException{
		SharedPreferences sp = mContext.getSharedPreferences(object.getClass().getSimpleName().toLowerCase(), 2);//1:read 2:write
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			saveField(field, sp, object);
		}
	}
	
	/**
	 * 获得存储对象
	 * @param classzz
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Object getObjectFromSp(Context mContext, Class<?> classzz) throws InstantiationException, IllegalAccessException{
		Object object = classzz.newInstance();
		SharedPreferences sp = mContext.getSharedPreferences(classzz.getSimpleName(), 1);//1:read 2:write
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			try {
				FieldUtils.setValueToFiled(field, object, getFieldFromSp(field, sp));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
	
	
    public static String PREFERENCE_NAME = "HuiJiaYouCommon";

    /**
     * put string preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * put long preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * get long preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * put float preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

}
