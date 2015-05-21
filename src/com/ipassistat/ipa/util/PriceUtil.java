package com.ipassistat.ipa.util;

import android.content.Context;

/**
 * 处理价格的工具类
 * 
 * @author LiuYuHang
 * @date 2014年12月8日
 */
public class PriceUtil {

	/**
	 * 获取前面带有￥的价格显示
	 *
	 * @param price
	 * @return
	 * @author LiuYuHang
	 * @date 2014年12月8日
	 */
	public static String formatPrice(String price) {

		if (price.indexOf(".") == -1) {
			//整数，不带小数点
			price = price + ".00";
		}else{
			//小数，带小数点
			String str = price.substring(price.indexOf(".")+1,price.length());//截取小数点以后的字符串
			if(str.length() == 1){
				//例如123.0这种小数点后只有一位数的要多添加个0
				price = price + "0";
			}
		}
		if (!price.startsWith("￥")) {
			price = "￥" + price;
		}
		
		return price;
	}
}
