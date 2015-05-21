package com.ipassistat.ipa.util;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Discription： 数据处理工具类
 * @package： com.ichsy.mboss.util.DataUtils
 * @author： MaoYaNan
 * @date：2014-10-16 下午8:44:58
 */
public class DateUtil {
	/**
	 * @discretion: 处理倒计时，服务器与本地时间差的问题
	 * @author: MaoYaNan
	 * @date: 2014-10-16 下午8:43:30
	 * @param type
	 *            列表的数据类型
	 * @param fieldNameEnd
	 *            结束时间的变量名
	 * @param fieldNameSys
	 *            系统时间的变量名
	 * @param list
	 *            数据列表
	 * @return
	 */
	public static <T> List<T> setEndTime(Class<T> type, String fieldNameEnd,
			String fieldNameSys, List<T> list) {
		if(list == null) return list;
		long temp_end, temp_current, temp_sys;
		String endTime, sysTime;
		Field fieldEnd, fieldSys, fieldServerEnd;
		try {
			fieldEnd = Class.forName(type.getName()).getDeclaredField(
					fieldNameEnd);

			fieldSys = Class.forName(type.getName()).getDeclaredField(
					fieldNameSys);
			fieldServerEnd = Class.forName(type.getName()).getDeclaredField(
					"fieldServerEnd");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (T t : list) {
				fieldEnd.setAccessible(true);
				fieldSys.setAccessible(true);
				fieldServerEnd.setAccessible(true);
				endTime = (String) fieldEnd.get(t);
				sysTime = (String) fieldSys.get(t);
				if (fieldServerEnd != null) {
					fieldServerEnd.set(t, endTime); // 记录服务器结束时间
					// LogUtil.outLogDetail("-------server end-----" +
					// fieldServerEnd.get(t));
				}
				temp_end = sf.parse(endTime).getTime();
				temp_sys = sf.parse(sysTime).getTime();
				temp_current = System.currentTimeMillis();

				// Date current = new Date(temp_current);
				// String currentDate = sf.format(current); //测试数据正确性

				temp_end += temp_current - temp_sys;
				Date dt = new Date(temp_end);
				endTime = sf.format(dt);
				fieldEnd.set(t, endTime);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @discretion:处理倒计时，服务器与本地时间差的问题
	 * @author: MaoYaNan
	 * @date: 2014-10-17 上午10:22:22
	 * @param type
	 *            列表的数据类型
	 * @param fieldNameEnd
	 *            结束时间的变量名
	 * @param fieldNameSys
	 *            系统时间的变量名
	 * @param t
	 *            单个处理对象
	 * @return
	 */
	public static <T> T setEndTime(Class<T> type, String fieldNameEnd,
			String fieldNameSys, T t) {
		long temp_end, temp_current, temp_sys;
		String endTime, sysTime;
		Field fieldEnd, fieldSys, fieldServerEnd;
		try {
			fieldEnd = Class.forName(type.getName()).getDeclaredField(
					fieldNameEnd);

			fieldSys = Class.forName(type.getName()).getDeclaredField(
					fieldNameSys);

			fieldServerEnd = Class.forName(type.getName()).getDeclaredField(
					"fieldServerEnd");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			fieldEnd.setAccessible(true);
			fieldSys.setAccessible(true);
			fieldServerEnd.setAccessible(true);
			endTime = (String) fieldEnd.get(t);
			if (fieldServerEnd != null) {
				fieldServerEnd.set(t, endTime); // 记录服务器结束时间
				// LogUtil.outLogDetail("-------server end-----"
				// + fieldServerEnd.get(t));
			}
			sysTime = (String) fieldSys.get(t);
			temp_end = sf.parse(endTime).getTime();
			temp_sys = sf.parse(sysTime).getTime();
			temp_current = System.currentTimeMillis();

			// Date current = new Date(temp_current);
			// String currentDate = sf.format(current); //测试用的

			temp_end += temp_current - temp_sys;
			Date dt = new Date(temp_end);
			endTime = sf.format(dt);
			fieldEnd.set(t, endTime);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static String getUpdateTime() {
		Date nowDate = new Date();
		return nowDate.getHours() + ":" + nowDate.getMinutes() + ":"
				+ nowDate.getSeconds();
	}

	/**
	 * 当天显示，今天 HH:MM 过去显示 MM-DD HH:MM
	 * 
	 * @param lastTime
	 * @return
	 * @author sargent
	 * @date 2014年11月4日
	 */
	public static String getUpdateTime(long lastTime) {
		String result = null;

		if (isIdenticalDay(lastTime)) {// 今天
			SimpleDateFormat tempSdf = new SimpleDateFormat("今天 HH:mm",
					Locale.CHINA);
			result = tempSdf.format(new Date(lastTime));
		} else if (isIdenticalYear(lastTime)) {// 今年
			SimpleDateFormat tempSdf = new SimpleDateFormat("MM-dd HH:mm",
					Locale.CHINA);
			result = tempSdf.format(new Date(lastTime));
		} else {// 去年
			SimpleDateFormat tempSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.CHINA);
			result = tempSdf.format(new Date(lastTime));
		}
		return result;
	}

	public static boolean isIdenticalDay(long lastTime) {
		boolean result = false;
		SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		String tempLast = daySdf.format(new Date(lastTime));
		String tempCurrent = daySdf.format(new Date());
		if (tempLast != null && tempLast.equals(tempCurrent))
			result = true;
		return result;
	}

	public static boolean isIdenticalYear(long lastTime) {
		boolean result = false;
		SimpleDateFormat daySdf = new SimpleDateFormat("yyyy", Locale.CHINA);
		String tempLast = daySdf.format(new Date(lastTime));
		String tempCurrent = daySdf.format(new Date());
		if (tempLast != null && tempLast.equals(tempCurrent))
			result = true;
		return result;
	}
	/**
	 * 判断系统当前项目是否在显示期间内
	 * 增加人：时培飞 增加时间:2014-12-02 
	 * @param startTime
	 * @param endTime
	 */
	public  static  boolean isInShowPeriod(String startTime, String endTime) {
		boolean isInShowPeriod = false;
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate = simpleDateFormat.parse(startTime);
			Date endDate = simpleDateFormat.parse(endTime);
			Date currDate = new Date();
			if (currDate.before(endDate) && currDate.after(startDate)) {
				isInShowPeriod = true;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isInShowPeriod;
	}
}
