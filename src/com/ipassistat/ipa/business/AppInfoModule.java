package com.ipassistat.ipa.business;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ipassistat.ipa.bean.request.UpdateCheckRequest;
import com.ipassistat.ipa.bean.response.UpdateCheckResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.LogUtil;

/**
 * 应用程序信息模块
 * @author lxc
 *
 */
public class AppInfoModule extends BaseModule{

	
	public AppInfoModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}
	
	
	public void getWelcomeInfoList(){
		
	}

	/**
	 * 检查是否更新
	 * @param context
	 * @param versionApp
	 * @param versionCode
	 */
	public void checkUpdate(Context context,String versionApp,String versionCode){
		UpdateCheckRequest updateCheckRequest = new UpdateCheckRequest();
		updateCheckRequest.setVersionApp(versionApp);
		updateCheckRequest.setVersionCode(versionCode);
		updateCheckRequest.setIosAndroid(2);
		getDataByPost(context, ConfigInfo.API_UPDATE_CHECK, updateCheckRequest, UpdateCheckResponse.class);
	}
	
	public String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
//	        versioncode = pi.versionCode;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	    	LogUtil.outLogDetail(e.getMessage());
	    }  
	    return "V"+versionName;  
	}  
	
	
}
