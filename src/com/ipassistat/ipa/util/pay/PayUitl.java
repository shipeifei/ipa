package com.ipassistat.ipa.util.pay;

import com.alipay.sdk.app.PayTask;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.TextureView;

/**
 * 支付工具类
 * @author Administrator
 *
 */
public class PayUitl {

	private static PayInterface mPayInterface;
	
	private static Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			Result result = new Result((String) msg.obj);
			if (null != result.resultStatus && result.resultStatus.equals("9000")) {
				mPayInterface.paySucess();
			}else {
				mPayInterface.payFail();
			}
			
		}
	};
	/**
	 * 支付宝支付
	 * @param context
	 * @param orderInfo
	 */
//	public static void payByAli(final Context context,final String orderInfo,final Handler handler){
//		new Thread() {
//			public void run() {
//				PayTask alipay = new PayTask((Activity) context);
//				String result = alipay.pay(orderInfo);
//				Message msg = new Message();
//				msg.obj = result;
//				mHandler.sendMessage(msg);
//			}
//		}.start();
//	}
	
	public static void payByAli(final Context context,final String orderInfo,PayInterface payInterface){
		if (TextUtils.isEmpty(orderInfo)) {
			return;
		}
		mPayInterface = payInterface; 
		new Thread() {
			public void run() {
				PayTask alipay = new PayTask((Activity) context);
				String result = alipay.pay(orderInfo);
				Message msg = new Message();
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	public static void payByWeiXin(Context context, String orderCode, String prepayId, String nonceStr, String timeStamp, String packageValue, String sign,String pageFlag){
		WeiXinPay pay=new WeiXinPay(context);
		pay.payByWeiXin(orderCode, prepayId, nonceStr, timeStamp, packageValue, sign,pageFlag);
	}
	
	public interface PayInterface{
		public void paySucess();
		public void payFail();
	}
}
