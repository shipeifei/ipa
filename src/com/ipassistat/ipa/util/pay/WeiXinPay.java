package com.ipassistat.ipa.util.pay;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Context;
import android.widget.Toast;

/**
 * 微信支付工具类
 * @author RenHeng
 *
 */
public class WeiXinPay{

	public static final  String AppID = "wxb4964132ba624ffd";  
	//public static final  String AppSecret = "1629a47c071c01946439cfcb532f86c7";
	public static final  String PartnerID ="1224643701";
	//public static final  String PartnerKey ="801a0a0f994249130b81cc0c2502ff1d";
	//public static final  String AppKey="hAOfmrH13RYzjJfgVq4ypTdyYqJxlEyEO6Axwd2mTDAbgpxwbdNR194YtqE52u5rI279qy5kcBG7gVZserVMFu2lPURay5I2oYgNULiCTYpQJgaU4QEoyBRtFh8shJRk";
	
	private IWXAPI api;
	
	public WeiXinPay(Context context){
		api = WXAPIFactory.createWXAPI(context, AppID);
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if(isPaySupported){
			api.registerApp(AppID);
		}else{
			Toast.makeText(context, "微信支付失败，请先安装微信哦", Toast.LENGTH_LONG).show();
		}
	}
	
	public  void payByWeiXin(String orderCode ,String prepayId, String nonceStr, String timeStamp, String packageValue, String sign, String pageFlag){
		PayReq req = new PayReq();
		String extdata= orderCode+","+pageFlag;
		req.extData=extdata;
		req.appId = AppID;
		req.partnerId = PartnerID;
		req.prepayId=prepayId;
		req.nonceStr=nonceStr;
		req.timeStamp=timeStamp;
		req.packageValue =  packageValue;
		req.sign = sign;
		api.sendReq(req);
	}
}
class ExtData{
	public String orderCode;
	public String pageFlag;
}
