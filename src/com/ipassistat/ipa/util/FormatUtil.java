package com.ipassistat.ipa.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 修改格式 工具类
 * @author renheng
 *
 */
public class FormatUtil {

	/**
	 * 将double类型的数字改为保留两位小数
	 * @param number要格式的数字
	 * @return
	 */
//	public static double getTwoNumber(double number){
//		BigDecimal b=new BigDecimal(number);
//		double d=b.setScale(2, 4).doubleValue();
//		return d;
//	}
	
	public static String getTwoNumber(double number){
		DecimalFormat df=new DecimalFormat("###0.00");
		String s=df.format(number);
		return s;
	}
	
	public static String getTwoNumber(String str){
		double d=Double.valueOf(str);
		return getTwoNumber(d);
	}
	
}
