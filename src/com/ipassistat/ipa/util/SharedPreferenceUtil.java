package com.ipassistat.ipa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.codec.binary.Base64;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * sharedPreference 工具类
 * @author lxc
 */
public class SharedPreferenceUtil {
	public static String SHAREDFRENCE_NAME = "hml";
	
	private static SharedPreferences sharedPreferences;
	
	public static synchronized SharedPreferences getSharedPreferences (Context context){
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SHAREDFRENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		}
		return sharedPreferences;
	}
	
	public static SharedPreferences getSharedPreferences(Context context,String spName){
		return context.getSharedPreferences(spName, Context.MODE_WORLD_WRITEABLE);
	}
	
	public static void putStringInfo(Context context,String key,String info){
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, info);
		editor.commit();
	}
	
	
    
    public static String getStringInfo(Context context,String key){
    	String info = getSharedPreferences(context).getString(key, "");
        return info;
    }
    
    public static void putBooleanInfo(Context context,String key,boolean flag){
    	Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, flag);
		editor.commit();
    }
    
    public static boolean getBooleanInfo(Context context,String key){
    	boolean flag = getSharedPreferences(context).getBoolean(key, false);
    	return flag;
    }
    
    public static boolean clearStringInfo(Context context,String key){
    	Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, "");
		return editor.commit();
    }
    
    /**
     * 保存对象
     * @param context
     * @param key
     * @param object
     * @return
     * @throws Exception
     */
    public static boolean saveObject (Context context,String key,Object object) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		String productBase64 = new String(Base64.encodeBase64(baos
				.toByteArray()));
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, productBase64);
		boolean isSucess  = editor.commit();
		oos.close();
		return isSucess;
    }
    
    /**
     * 得到对象
     * @param context
     * @param key
     * @return
     */
    public static Object getObject(Context context,String key) throws Exception{
    	String productBase64 = getSharedPreferences(context).getString(key, "");
		byte[] base64Bytes = Base64.decodeBase64(productBase64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);	
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object returnObject = ois.readObject();			
		ois.close();
		return returnObject;
    }
    /**
     * 保存对象
     * @param context
     * @param key
     * @param spName :sharedpreference name
     * @param object
     * @return
     * @throws Exception
     */
    public static boolean saveObject (Context context,String key,String spName,Object object) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		String productBase64 = new String(Base64.encodeBase64(baos
				.toByteArray()));
		SharedPreferences.Editor editor = getSharedPreferences(context,spName).edit();
		editor.putString(key, productBase64);
		boolean isSucess  = editor.commit();
		oos.close();
		return isSucess;
    }
 
	/**
	 * 得到对象
	 * @param context
	 * @param key
	 * @param spName
	 * @return
	 * @throws Exception
	 */
    public static Object getObject(Context context,String key,String spName) throws Exception{
    	String productBase64 = getSharedPreferences(context,spName).getString(key, "");
    	if(!TextUtils.isEmpty(productBase64))
    	{
		byte[] base64Bytes = Base64.decodeBase64(productBase64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);	
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object returnObject = ois.readObject();			
		ois.close();
		bais.close();
		return returnObject;
    	}
    	else 
    	{
    		return null;
    	}
    }
    /**
     * 清除
     * @param context
     * @param spName
     * @return
     */
    public static boolean clearObject(Context context,String spName){
    	Editor editor = getSharedPreferences(context,spName).edit();
    	editor.clear();
    	return editor.commit();
    }
    
    /**
     * 清除所有的保存数据
     * @param context
     * @param spName
     * @return
     */
   /* public static boolean clearAllObject(Context context){
    	Editor editor = context.getSharedPreferences(SHAREDFRENCE_NAME, Context.MODE_WORLD_WRITEABLE).edit();
    	editor.clear();
    	return editor.commit();
    }*/
    
    
    /**
     * 保存bitmap
     * @param context
     * @param key
     * @param imageView
     * @return
     * @throws Exception
     */
    public static boolean saveImageView(Context context,String key,ImageView imageView) throws Exception{
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		((BitmapDrawable) imageView.getDrawable()).getBitmap()
				.compress(CompressFormat.JPEG, 50, baos);
		
		String imageBase64 = new String(Base64.encodeBase64(baos
				.toByteArray()));
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, imageBase64);
		boolean isScuess = editor.commit();
		baos.close();
		return isScuess;
    }
    /**
     * 得到imageView
     * @param context
     * @param key
     * @return
     */
    public static ImageView getImageView(Context context,String key){
    	ImageView imageView = new ImageView(context);
    	String imageBase64 = getSharedPreferences(context).getString(key,"");
		byte[] base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		imageView.setImageDrawable(Drawable.createFromStream(bais,"bitmap_name"));
    	return imageView;
    }
    
    
    
}
