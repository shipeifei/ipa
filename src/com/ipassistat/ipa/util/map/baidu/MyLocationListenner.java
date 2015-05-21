/*
 * MyLocationListenner.java [V 1.0.0]
 * classes : com.ipassistat.ipa.util.map.baidu.MyLocationListenner
 * 时培飞 Create at 2015-4-21 上午11:25:17
 */
package com.ipassistat.ipa.util.map.baidu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ipassistat.ipa.util.LogUtil;

/**
 * 百度地图回调函数 com.ipassistat.ipa.util.map.baidu.MyLocationListenner
 * 
 * @author 时培飞 Create at 2015-4-21 上午11:25:17
 */
public class MyLocationListenner implements BDLocationListener {

	public Handler messHandler;

	@Override
	public void onReceiveLocation(BDLocation location) {

		// 无法获取定位
		if (location == null) {
			Message message = Message.obtain();
			message.what = 101;

			messHandler.sendMessage(message);
			return;
		}
		LocationMessage locationMessage = new LocationMessage();
		int type = location.getLocType();

		String coorType = location.getCoorType();

		// 判断是否有定位精度半径
		if (location.hasRadius()) {
			// 获取定位精度半径，单位是米
			float accuracy = location.getRadius();

		}

		// 获取当前位置
		if (location.hasAddr()) {
			// 获取反地理编码。 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。
			String address = location.getAddrStr();
			locationMessage.setCurrentAddress(address);
			LogUtil.outLogDetail(address);

		}

		// 获取当前的城市
		String province = location.getProvince(); // 获取省份信息
		locationMessage.setProvince(province);
		String city = location.getCity(); // 获取城市信息
		locationMessage.setCity(city);
		String district = location.getDistrict(); // 获取区县信息
		locationMessage.setDistrict(district);

		// 获取经纬度
		double latitude = location.getLatitude();// 纬度
		locationMessage.setLatitude(latitude);
		double longitude = location.getLongitude();// 经度
		locationMessage.setLongitude(longitude);

		// 发送获取的结果到主界面
		if (messHandler != null) {
			Message message = Message.obtain();
			message.what = 100;
			Bundle bundle = new Bundle();
			bundle.putSerializable("location", locationMessage);
			message.setData(bundle);
			messHandler.sendMessage(message);
		}

	}

	@Override
	public void onReceivePoi(BDLocation poiLocation) {

		if (poiLocation == null) {

			return;
		}

		if (poiLocation.hasPoi()) {
			String poiStr = poiLocation.getPoi();

		}

		if (poiLocation.hasAddr()) {
			// 获取反地理编码。 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。
			String address = poiLocation.getAddrStr();

		}
	}

}
