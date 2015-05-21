package com.ipassistat.ipa.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络检查工具类
 * @author lxc
 *
 */
public class NetUtil {
	
	
	private static ConnectivityManager mConnectivityManager = null;
	
	private static ConnectivityManager getConnectivityManager(Context context){
		if (mConnectivityManager == null) {
			mConnectivityManager = (ConnectivityManager) context 
					.getSystemService(Context.CONNECTIVITY_SERVICE); 
		}
		return mConnectivityManager;
	}

	
	public static boolean isNetworkConnected(Context context) { 
		if (context != null) { 
			NetworkInfo mNetworkInfo = getConnectivityManager(context).getActiveNetworkInfo(); 
			if (mNetworkInfo != null) { 
				return mNetworkInfo.isAvailable(); 
			} 
		} 
			return false; 
	} 
	
	
	public static boolean isWifiConnected(Context context) { 
		if (context != null) { 
			NetworkInfo mWiFiNetworkInfo = getConnectivityManager(context) 
			.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
			if (mWiFiNetworkInfo != null) { 
				return mWiFiNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	} 
	
	
	public static boolean isMobileConnected(Context context) { 
		if (context != null) { 
			NetworkInfo mMobileNetworkInfo = getConnectivityManager(context) 
			.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
			if (mMobileNetworkInfo != null) { 
				return mMobileNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	} 
	
	
	/**
	 * 获得网络连接类型
	 * @param context
	 * @return one of TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH, or other types defined by ConnectivityManager
	 */
	public static int getConnectedType(Context context) { 
		if (context != null) { 
			NetworkInfo mNetworkInfo = getConnectivityManager(context).getActiveNetworkInfo(); 
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { 
				return mNetworkInfo.getType(); 
			} 
		} 
		return -1; 
	} 
	
	/**
	 * 获取手机ip地址
	 * 
	 * @return
	 */
	public static String getPhoneIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
}
