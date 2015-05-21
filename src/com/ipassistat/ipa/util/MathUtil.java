package com.ipassistat.ipa.util;

/**
 * 
 * 
 * Package: com.ichsy.mboss.util  
 *  
 * File: MathUtil.java   
 *  
 * @author: 任恒   Date: 2015-2-28  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration Name  
 *
 */
public class MathUtil {
	
	/**
	 * 获取频道的索引值，每三个为1组
	 * @param position  频道在list中的position
	 * @return 索引从0开始，0为第一个位置,对应icon_title_0 ；1为第二个位置,对应icon_title_1；2为第三个位置,对应icon_title_2
	 */
	public static int getIndexInMovie(int position){
		int index = -1;
		switch (position%3) {
		case 0:
			index = 0;
			break;
		case 1:
			index = 1;
			break;
		case 2:
			index = 2;
			break;
		default:
			break;
		}
		return index;
	}
}
