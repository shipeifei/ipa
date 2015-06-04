package com.ipassistat.ipa.domain.action;

import com.ipassistat.ipa.domain.bean.DomainBaseResponse;

import android.content.Context;

/***
 * 所有领域的接口
 * @author shipeifei
 *
 */
public interface IDomainAction {
	
	DomainContext domainContext=DomainContext.getInstance();
	
	/***
	 * 动作行为
	 * create at 2015-5-20
	 * author 时培飞
	 */
    void action(Object object,Context context);
   
   /***
    * 执行成功
    * create at 2015-5-20
    * author 时培飞
    */
    void success();
   
   /***
    * 执行失败
    * create at 2015-5-20
    * author 时培飞
    */
    void error();
}
