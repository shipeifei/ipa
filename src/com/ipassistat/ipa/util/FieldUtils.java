package com.ipassistat.ipa.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.annotation.SuppressLint;

/**
 * 实体的工具类
 * 
 */
public class FieldUtils {
	/**
	 * 设置属性值
	 * 
	 * @param field
	 * @param object
	 * @param fieldValue
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressLint("SimpleDateFormat")
	public static void setValueToFiled(Field field, Object object, String fieldValue) throws IllegalArgumentException, IllegalAccessException {

		field.setAccessible(true);// 允许插入
		Class<?> fieldType = field.getType();// 属性的类型

		if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {// int
			Integer value = Integer.parseInt(fieldValue);
			field.set(object, value);
		} else if (String.class == fieldType) {// string
			field.set(object, fieldValue);
		} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {// long
			Long value = Long.parseLong(fieldValue);
			field.set(object, value);
		} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {// float
			Float value = Float.parseFloat(fieldValue);
			field.set(object, value);
		} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {// short
			Short value = Short.parseShort(fieldValue);
			field.set(object, value);
		} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {// double
			Double value = Double.parseDouble(fieldValue);
			field.set(object, value);
		} else if (Character.TYPE == fieldType) {// char
			Character value = Character.valueOf(fieldValue.charAt(0));
			field.set(object, value);
		} else if (Date.class == fieldType) {// date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(fieldValue);
				field.set(object, date);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (Boolean.class == fieldType) {
			field.setBoolean(object, (Boolean.parseBoolean(String.valueOf(fieldValue))));
		}
	}

	/**
	 * 拿到指定的属性值
	 * 
	 * @param field
	 * @param object
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void getValueFromField(Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		Class<?> fieldType = field.getType();

		if (Integer.TYPE == fieldType || Integer.class == fieldType) {// int
			field.getInt(object);
		} else if (String.class == fieldType) {// string
			String.valueOf(field.get(object));
		} else if (Short.class == fieldType) {// short
			field.getShort(object);
		} else if (Long.class == fieldType) {// long
			field.getLong(object);
		} else if (Float.class == fieldType) {// float
			field.getFloat(object);
		} else if (Double.class == fieldType) {// double
			field.getDouble(object);
		} else if (Character.class == fieldType) {// char
			field.getChar(object);
		} else if (Date.class == fieldType) {// date
			String.valueOf(field.get(object));
		} else if (Boolean.class == fieldType) {// boolean
			field.getBoolean(object);
		}
	}

	/**
	 * 反射拿到对象///有问题
	 *
	 * @param clazz
	 * @param memberName
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	@Deprecated
	public static Object findMember(Class<?> clazz, String memberName) {

		try {
			Field field = clazz.getDeclaredField(memberName);
			return field.get(clazz);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过反射获取属性的泛型类型
	 * @param clazz
	 * @param filedName
	 * @return
	 * @author: 任恒   Date: 2015-3-25
	 */
	public static Type getFieldGenericsType(Class<?> clazz, String filedName){
		try {
			ParameterizedType pt = (ParameterizedType) clazz.getField(filedName).getGenericType();
			return pt.getActualTypeArguments()[0];
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 反射获取属性类型
	 * @param clazz
	 * @param filedName 属性名字
	 * @return
	 * @author: 任恒   Date: 2015-3-25
	 */
	public static Type getFieldType(Class<?> clazz, String filedName){ 
		try {
			return clazz.getDeclaredField(filedName).getType();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
