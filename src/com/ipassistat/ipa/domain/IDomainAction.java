package com.ipassistat.ipa.domain;

import android.content.Context;

/***
 * 所有领域的接口
 * @author shipeifei
 *
 */
public interface IDomainAction {
	
	/***
	 * 动作行为
	 * create at 2015-5-20
	 * author 时培飞
	 */
    void action(Context context,String actionContent);
   
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
