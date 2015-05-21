package com.ipassistat.ipa.bean.local;

import java.util.List;

/**
 * 市
 * 
 * @author LiuYuHang
 * @date 2014年10月21日
 */
public class NativeCityModel {

	// "cityID": "110100",
	public String cityID;
	// "cityName": "市辖区",
	public String cityName;
	public List<NativeAreaModel> districtList;
}
