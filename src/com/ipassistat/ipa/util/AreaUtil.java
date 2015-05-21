package com.ipassistat.ipa.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.ipassistat.ipa.bean.local.CityLocalEntity;
import com.ipassistat.ipa.bean.local.CityModel;

public class AreaUtil {
	
	
	/**
	 * 根据城市编码返回城市名称
	 * @param context
	 * @param code 城市编码
	 * @return
	 */
	public static String getCityNameByCode(Context context, String code){
		CityModel model = getCityData(context);
		List<CityLocalEntity> list= model.getList();
		for(int i=0;i<list.size();i++){
			CityLocalEntity entity= list.get(i);
			if(entity.getCityID().equals(code)) return entity.getCityName();
		}
		return null;
	}
	
	public static CityModel getCityData(Context context) {
		try {
			String str= FileUtis.convertStreamToString(context.getAssets().open(
					"city.json"));
			Gson gson=new Gson();
			CityModel model=gson.fromJson(str, CityModel.class);
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
