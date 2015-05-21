package com.ipassistat.ipa.business;

import com.ipassistat.ipa.dao.BusinessInterface;


import android.content.Context;
import android.text.TextUtils;

public class HmlLoginController extends LoginController{

	private Context context;
	private UserModule module;
	
	public HmlLoginController(Context context, Operation oper, BusinessInterface bus) {
		super(context, oper);
		// TODO Auto-generated constructor stub
		this.context=context;
		module=new UserModule(bus);
	}

	/**
	 * 调用注销接口，调用成功之后，调用logoutLocal()方法
	 */
	public void logout(){
		module.logout(context);
	}
	
	public void logoutLocal(){
		module.logoutLocal(context);
	}
	
	//判断是否是登录状态
	public boolean isLogin(){
		boolean isLoginFlag;
		String str = UserModule.getUserToken(context);
		isLoginFlag = TextUtils.isEmpty(str)?false:true;
		return isLoginFlag;
	}

	
}
