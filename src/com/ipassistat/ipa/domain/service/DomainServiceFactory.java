package com.ipassistat.ipa.domain.service;

import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.OffHostApduService;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ipassistat.ipa.constant.DomainServiceConstant;
import com.ipassistat.ipa.domain.action.DomainContext;
import com.ipassistat.ipa.domain.bean.DomainBaseResponse;
import com.ipassistat.ipa.util.LogUtil;

/**
 * 通用服务工厂模式
 * 
 * @author shipeifei
 * 
 */
public class DomainServiceFactory {

	/***
	 * 创建不同产品的服务 create at 2015-5-22 author 时培飞
	 */
	public static void createDomainService(String result, Context context) {
		IDoaminBaseService bService = null;
		DomainBaseResponse baseResponse = parseDomainJson(result);

		if (baseResponse != null) {
			if (baseResponse.getRc() == 0)// 语义解析成功
			{
				if (baseResponse.getService() == DomainServiceConstant.CALL_SERVICE || baseResponse.getService().equals(DomainServiceConstant.CALL_SERVICE)) {
					bService = new CallDomainService();
					bService.startService(result, context);
				}

			}// 语音解析错误信息
			else {
				Intent intent = new Intent();
				intent.setAction("com.ipa.parseerror");
				intent.putExtra("rc", baseResponse.getRc());
				context.sendBroadcast(intent);
			}
		}// 解析失败进行错误提示
		else {

		}
	}

	/***
	 * 解析json create at 2015-5-22 author 时培飞
	 */
	public static DomainBaseResponse parseDomainJson(String result) {
		if (TextUtils.isEmpty(result)) {
			return null;
		}

		LogUtil.outLogDetail("请求返回的结果为：" + result);
		DomainBaseResponse baseResponse = null;
		try {
			Gson gson = new Gson();
			baseResponse = gson.fromJson(result, DomainBaseResponse.class);
		} catch (Exception e) {
			LogUtil.outLogDetail("解析接口返回的json格式出错！");
		}

		return baseResponse;

	}
}
