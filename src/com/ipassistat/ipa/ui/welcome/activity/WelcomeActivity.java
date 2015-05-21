package com.ipassistat.ipa.ui.welcome.activity;

import java.util.List;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.request.WelcomeRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.UpdateCheckResponse;
import com.ipassistat.ipa.bean.response.WelcomeResponse;
import com.ipassistat.ipa.bean.response.entity.WelcomeEntity;
import com.ipassistat.ipa.business.AppInfoModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.StatusCode;
import com.ipassistat.ipa.ui.activity.BaseActivity;
import com.ipassistat.ipa.util.ApiUrl;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.DateUtil;
import com.ipassistat.ipa.util.DialogUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.map.baidu.MyLocationListenner;
import com.ipassistat.ipa.version.update.UpdatePupWindow;
import com.umeng.analytics.MobclickAgent;


/***
 * 欢迎页面
 * com.ipassistat.ipa.ui.activity.WelcomeActivity
 * @author 时培飞 
 * Create at 2015-4-20 下午6:42:51
 */
public class WelcomeActivity extends BaseActivity {

	// 成员变量声明
	private ImageView mWelImageView;
	private AppInfoModule mAppInfoModule;
	private AlertDialog mShowUpdateDialog;
	private String mDownloadUrl;
	public static WelcomeActivity welcomeActivity;

	
	
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			IntentUtil.intentToMainActivityOnTime();
         
		}
		
	};

	
	private View.OnClickListener updateByForceListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			mShowUpdateDialog.dismiss();
			 new UpdatePupWindow(WelcomeActivity.this, mDownloadUrl, true);

		}
	};

	private View.OnClickListener updateByDefault = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mShowUpdateDialog.dismiss();
			UpdatePupWindow pop = new UpdatePupWindow(WelcomeActivity.this, mDownloadUrl, false);
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
	};

	private View.OnClickListener cancelUpdateListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			mShowUpdateDialog.dismiss();
			mHandler.sendEmptyMessageDelayed(0, 2000);
			
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);
		//toastEnvironment();

		

		// 禁止默认的页面统计方式，这样将不会再自动统计Activity 在fragmentActivity+Fragment中使用
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);

		welcomeActivity = WelcomeActivity.this;
		mAppInfoModule = new AppInfoModule(this);

		initWidgets();
		//getWelcomeData();
		disableSlideClose();

		
		
		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	
	
	
	/**
	 * 初始化控件
	 * 
	 * @author 时培飞 Create at 2014-12-2 上午11:24:05
	 */
	private void initWidgets() {
		mWelImageView = (ImageView) findViewById(R.id.welcome_imgv);
		Drawable drawable = getResources().getDrawable(R.drawable.bg_welcome);

		mWelImageView.setBackgroundDrawable(drawable);
	}

	/**
	 * 检测版本升级
	 * 
	 * @author 时培飞
	 * 
	 *         Create at 2014-12-2 上午11:26:46
	 */
	private void checkUpdate() {
		String versionName = mAppInfoModule.getAppVersionName(WelcomeActivity.this);
		mAppInfoModule.checkUpdate(WelcomeActivity.this, versionName, ConfigInfo.VERSION_CODE);
	}

	/**
	 * 获取欢迎页面的数据
	 * 
	 * @author 时培飞 Create at 2014-12-2 上午11:28:13
	 */
	private void getWelcomeData() {

		WelcomeRequest welcomeRequest = new WelcomeRequest();
		welcomeRequest.setApp_code(ConfigInfo.VERSION_CODE);
		mAppInfoModule.getDataByPost(WelcomeActivity.this, ConfigInfo.API_GET_WELCOME_LIST, welcomeRequest, WelcomeResponse.class);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		BaseResponse baseResp = (BaseResponse) object;
		if (baseResp.getResultCode() == ConfigInfo.RESULT_OK) {
			if (url.equals(ConfigInfo.API_GET_WELCOME_LIST)) {
				WelcomeResponse welcomeResponse = (WelcomeResponse) object;
				List<WelcomeEntity> list = welcomeResponse.getStratPage();
				/**
				 * 增加时间比较
				 */
				boolean isInPeriod = false;
				for (WelcomeEntity welcomeEntity : list) {
					if (DateUtil.isInShowPeriod(welcomeEntity.getStart_time(), welcomeEntity.getEnd_time())) {
						BitmapOptionsFactory.getInstance(this).display(mWelImageView, welcomeEntity.getUrl());
						isInPeriod = true;
						checkUpdate();
						return;
					}
				}
				// 服务器欢迎页面的图片不在有效期之内，则显示本地默认的欢迎页面
				if (!isInPeriod) {
					Drawable drawable = getResources().getDrawable(R.drawable.bg_welcome);
					mWelImageView.setBackgroundDrawable(drawable);
				}
				checkUpdate();

			}

			if (url.equals(ConfigInfo.API_UPDATE_CHECK)) {
				UpdateCheckResponse response = (UpdateCheckResponse) object;
				if (response.getResultCode() == StatusCode.RESULT_OK) {
					mDownloadUrl = response.getAppUrl();
					/**
					 * 1、代表强制升级，2、代表不强制升级，3、代表不用升级
					 */
					if (response.getUpgradeSelect().equals("1")) {
						mShowUpdateDialog = DialogUtil.showUpdateDialog(WelcomeActivity.this, null, updateByForceListner, "发现新版本", response.getUpgradeContent(), "", "立即升级");
					} else if (response.getUpgradeSelect().equals("2")) {
						mShowUpdateDialog = DialogUtil.showUpdateDialog(WelcomeActivity.this, cancelUpdateListner, updateByDefault, "发现新版本", response.getUpgradeContent(), "暂不更新", "立即升级");
					} else {
						mHandler.sendEmptyMessageDelayed(0, 2000);

					}

				}
			}
		} else {
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {

		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	public void onNoNet() {

		super.onNoNet();
		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	public void onFinish() {

		super.onFinish();
		
		
	}

	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 退出时销毁定位
		//mLocationClient.stop();
	
		mHandler = null;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1001"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1001"); //
		MobclickAgent.onPause(this);
	}

	private void toastEnvironment() {
		if (ApiUrl.getInStance().getAPI_URL_CURRENT().equals(ApiUrl.HOST_URL_TEST)) {
			ToastUtil.showToast(this, "测试环境");
		} else if (ApiUrl.getInStance().getAPI_URL_CURRENT().equals(ApiUrl.HOST_URL_RC)) {
			ToastUtil.showToast(this, "RC环境");
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		
		
	}

	@Override
	protected void initData() {
		
		
	}

	@Override
	protected void bindEvents() {
	
		
	}
	
	

		
}
