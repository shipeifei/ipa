package com.ipassistat.ipa.domain.service;

import android.content.Context;

import com.ipassistat.ipa.domain.bean.DomainBaseResponse;

/***
 * 语音领域基类
 * @author shipeifei
 *
 */
public abstract class IDoaminBaseService {
	
	public DomainBaseResponse domainBaseResponse=null;
	/**
	 * 解析json
	 * create at 2015-5-21
	 * author 时培飞
	 */
   public abstract  boolean parseJsonInfo(String result,Context context);
    
    /***
     * 处理不同行为
     * create at 2015-5-21
     * author 时培飞
     */
   public abstract void selectAction(Context context);
   
   
   /***
    * 开启服务
    * create at 2015-5-21
    * author 时培飞
    */
   public abstract String startService(String result,Context context);
}
