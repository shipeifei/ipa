package com.ipassistat.ipa.view.sortlistview;

import java.util.List;

/**
 * sortListView要实现的接口
 * 
 * Package: com.ichsy.mboss.view.sortlistview  
 *  
 * File: ISort.java   
 *  
 * @author: 任恒   Date: 2015-3-20  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration Name  
 *
 */
public interface ISort<T> {

	/**
	 * 获取listview需要显示的字符串(即T中的字符串字段)
	 * @return
	 */
	public List<String> getSortStrings();
	
	/**
	 * 获取listview的显示item所映射的对象 
	 * @return
	 */
	public List<T> getSortModel();
	
}
