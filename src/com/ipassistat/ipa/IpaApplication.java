package com.ipassistat.ipa;

import java.io.File;

import android.os.Environment;
import android.os.StrictMode;

import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ipassistat.ipa.business.AppSettingController;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.domain.action.DomainContext;
import com.ipassistat.ipa.httprequest.ApiUrl;
import com.ipassistat.ipa.ui.activity.MainActivity;
import com.ipassistat.ipa.util.GlobalUtil;
import com.umeng.analytics.AnalyticsConfig;

/***
 * 因百度推送需要，将父类改为FrontiaApplication com.ipassistat.ipa.HmlApplication
 * 
 * @author 时培飞 Create at 2015-4-24 下午5:03:21
 */
public class IpaApplication extends FrontiaApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化请求服务器路径
		ApiUrl.getInStance().setAPI_URL_CURRENT(ApiUrl.HOST_URL_TEST);
		ApiUrl.getInStance().setTinyCommuUrl(ApiUrl.TINY_COMMU_TEST);
		ApiUrl.getInStance().setUploadFileUrl(ApiUrl.FILE_UPLOAD_URL_TEST);

		// 设置友盟统计的APPKEY
		AnalyticsConfig.setAppkey(getResources().getString(R.string.um_appkey));

		// 开启百度推送进程
		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, com.ipassistat.ipa.util.push.baidu.PushUtils.getMetaValue(getApplicationContext(), "api_key"));
		if (!AppSettingController.getInstance().isPushOpen(getApplicationContext())) {
			PushManager.stopWork(getApplicationContext());
		}

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		DomainContext.getInstance().context=getApplicationContext();
		GlobalUtil.initByEnvironment();

		if (ConfigInfo.DEBUG) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()// 磁盘读
					.detectDiskWrites()// 磁盘写
					.detectNetwork()// 网络访问
					.penaltyLog()//
					.build());

			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}

		// 设置图片保存路径
		File cacheDir = new File(Environment.getExternalStorageDirectory() + "/" + ConfigInfo.DEFAULT_DISK_DIR_CACHE);
		AQUtility.setCacheDir(cacheDir);

		GlobalUtil.init(getApplicationContext());

		/***
		 * 科大讯飞语音配置
		 */
		SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=55375024");

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		BitmapAjaxCallback.clearCache();
	}

}
