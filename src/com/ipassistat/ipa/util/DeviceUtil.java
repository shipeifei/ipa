package com.ipassistat.ipa.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 获取设备信息的工具类，包括屏幕宽度高度，设备唯一标识等等
 * 
 * @author LiuYuHang
 *
 */
public class DeviceUtil {

	public static int getDeviceWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public static int getDeviceHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/***
	 * 判断手机是否存在SIM
	 * create at 2015-5-19
	 * author 时培飞
	 */
	public static Boolean isExistsSIM(Context context)
	{
		TelephonyManager mTelephonyManager = (TelephonyManager)context. getSystemService(Context.TELEPHONY_SERVICE);  
		int simState = mTelephonyManager.getSimState();  
		String hintMessage = "";  
		switch (simState) {  
		case TelephonyManager.SIM_STATE_UNKNOWN:  
		    hintMessage = "无法获取SIM来源";  
		    break;  
		case TelephonyManager.SIM_STATE_ABSENT:  
		    hintMessage = "no SIM card is available in the device";  
		    break;  
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:  
		    hintMessage = "Locked: requires the user's SIM PIN to unlock";  
		    break;  
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:  
		    hintMessage = "Locked: requires the user's SIM PUK to unlock ";  
		    break;  
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:  
		    hintMessage = "Locked: requries a network PIN to unlock";  
		    break;  
		case TelephonyManager.SIM_STATE_READY:  
		    hintMessage = "Ready";  
		    break;  
		default:  
		    break;  
		}  
		  
		Toast.makeText(context, hintMessage, Toast.LENGTH_LONG).show();  
		return false;
	}
	
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取版本号
	 * @return
	 */
	public static String getVersonName(Context context){
		PackageManager manager=context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String versionName = info.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
