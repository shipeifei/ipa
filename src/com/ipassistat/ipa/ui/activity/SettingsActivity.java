package com.ipassistat.ipa.ui.activity;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.UpdateCheckResponse;
import com.ipassistat.ipa.business.AppInfoModule;
import com.ipassistat.ipa.business.AppSettingController;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.constant.StatusCode;
import com.ipassistat.ipa.ui.welcome.activity.GuideActivity;
import com.ipassistat.ipa.util.DeviceUtil;
import com.ipassistat.ipa.util.DialogUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.ActionSheet;
import com.ipassistat.ipa.view.DialogView;
import com.ipassistat.ipa.view.MyPupWindow;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.ActionSheet.ActionSheetListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置界面
 * 
 * @author maoyn
 * 
 */
public class SettingsActivity extends BaseActivity implements OnClickListener,
		ActionSheetListener, OnCheckedChangeListener {
	
	private static final String TAG = "SettingsActivity";
	private TitleBar mTitlebar;
	private CheckBox mPush_switch;
	private TextView mChangePasswd;
	private TextView mClearCache;
	private TextView mTel;
	private TextView mAboutUs;
	private Button mExit;
	private String mStringTitle;
	private ActionSheet mShow;
	private Context mContext;
	private UserModule mModule;

	// 增加手动检测升级功能 增加人:时培飞 增加日期:2014-12-01
	/** 版本号 */
	private TextView mVersionName;
	/** 手动升级按钮 */
	private TextView mHandUpdate;
	private AppInfoModule mAppInfoModule;
	/** 下载apk路径 */
	private String mDownloadUrl;
	/** 升级对话框 */
	private AlertDialog showUpdateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mContext = this;
		mModule = new UserModule(this);
		// 获得调用参数
		Intent intent = getIntent();
		mStringTitle = intent.getStringExtra("title");
		if (mStringTitle == null) {
			mStringTitle = "";
		}

		mAppInfoModule = new AppInfoModule(this);
		initViews();
		initListener();
		initDatas();
	}

	/**
	 * 
	 * @author 时培飞
	 * @discretion:检测升级 Create at 2014-12-1 上午10:35:22
	 */
	private void checkUpdate() {
		String versionName = mAppInfoModule
				.getAppVersionName(SettingsActivity.this);
		mAppInfoModule
				.checkUpdate(SettingsActivity.this, versionName, ConfigInfo.VERSION_CODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1013"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1013"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		judgeLogoutButtonVisible();
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (ConfigInfo.API_LOGOUT.equals(url)) {
			new UserModule(SettingsActivity.this)
					.logoutLocal(SettingsActivity.this);
			IntentUtil.startPersonalCenterActivity(mContext); // 跳转到
																// 个人中心的，有登录按钮的界面
		}

		// 增加手动检测升级功能 增加人:时培飞 增加日期:2014-12-01
		if (url.equals(ConfigInfo.API_UPDATE_CHECK)) {
			UpdateCheckResponse response = (UpdateCheckResponse) object;
			if (response.getResultCode() == StatusCode.RESULT_OK) {
				mDownloadUrl = response.getAppUrl();
				/**
				 * 1、代表强制升级，2、代表不强制升级，3、代表不用升级
				 */
				if (response.getUpgradeSelect().equals("1")) {
					showUpdateDialog = DialogUtil.showUpdateDialog(
							SettingsActivity.this, null, updateByForceListner,
							"发现新版本", response.getUpgradeContent(), "", "立即升级");
				} else if (response.getUpgradeSelect().equals("2")) {
					showUpdateDialog = DialogUtil.showUpdateDialog(
							SettingsActivity.this, cancelUpdateListner,
							updateByDefault, "发现新版本",
							response.getUpgradeContent(), "暂不更新", "立即升级");
				} else {
					ToastUtil.showToast(mContext,
							getString(R.string.update_message));
				}

			}

		}

	}

	/**
	 * 版本升级需要的回调函数
	 */
	private View.OnClickListener updateByDefault = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			

			showUpdateDialog.dismiss();

			showUpdateDialog.dismiss();

			MyPupWindow pop = new MyPupWindow(SettingsActivity.this,
					R.layout.pup_layout, mDownloadUrl, false);

			// handler.sendEmptyMessageDelayed(0, 2000);
		}
	};

	/**
	 * 版本升级需要的回调函数
	 */
	private View.OnClickListener updateByForceListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			showUpdateDialog.dismiss();
			MyPupWindow pop = new MyPupWindow(SettingsActivity.this,
					R.layout.pup_layout, mDownloadUrl, true);
		}
	};
	private View.OnClickListener cancelUpdateListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			showUpdateDialog.dismiss();
			// handler.sendEmptyMessageDelayed(0, 2000);
		}
	};

	private Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			intentToMainActivityOnTime();
		}
	};

	private void intentToMainActivityOnTime() {
		Intent intent = new Intent(this, GuideActivity.class);
		startActivity(intent);
		SettingsActivity.this.finish();
		overridePendingTransition(anim.fade_in, anim.fade_out);
	}

	private void initDatas() {
		mTitlebar.setTitleText(mStringTitle);

		// 设置版本号
		String versionName = mAppInfoModule
				.getAppVersionName(SettingsActivity.this);
		mVersionName.setText("当前版本" + versionName);

	}

	private void initViews() {
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		mPush_switch = (CheckBox) findViewById(R.id.push_switch);
		mChangePasswd = (TextView) findViewById(R.id.changePasswd);
		mClearCache = (TextView) findViewById(R.id.clearCache);
		mTel = (TextView) findViewById(R.id.tel);
		mAboutUs = (TextView) findViewById(R.id.aboutUs);
		mExit = (Button) findViewById(R.id.exit);
		// 添加手动检测升级功能: 添加人:时培飞 添加时间:2014-12-01
		mHandUpdate = (TextView) findViewById(R.id.hand_update);
		mVersionName = (TextView) findViewById(R.id.version_name);

		judgeLogoutButtonVisible();
		judgePushButtonVisible();
	}

	/**
	 * @discretion: 判断退出按钮是否生效
	 * @author: MaoYaNan
	 * @date: 2014-10-11 下午3:32:01
	 */
	private void judgeLogoutButtonVisible() {
		if (UserModule.isLogin(mContext)) {
			mExit.setVisibility(View.VISIBLE);
		} else {
			mExit.setVisibility(View.GONE);
		}
	}

	/**
	 * 判断推送按钮开启或关闭状态
	 */
	private void judgePushButtonVisible() {

		mPush_switch.setChecked(AppSettingController.getInstance().isPushOpen(
				getApplicationContext()));
	}

	private void initListener() {
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(mContext, "1038");
						finish();
					}
				});
		mTitlebar.setButtonClickListener(TitleBarButton.rightImgv,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mTitlebar.setVisibility(TitleBarButton.rightImgv, View.GONE);

		mChangePasswd.setOnClickListener(this);
		mClearCache.setOnClickListener(this);
		mTel.setOnClickListener(this);
		mAboutUs.setOnClickListener(this);
		mExit.setOnClickListener(this);
		mPush_switch.setOnCheckedChangeListener(this);

		mHandUpdate.setOnClickListener(this);
		mVersionName.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changePasswd:
			// 修改密码
			MobclickAgent.onEvent(mContext, "1033");
			if (UserModule.isLogin(mContext)) {
				IntentUtil.startPwdResetActivity(mContext);
			} else {
				IntentUtil.startLoginActivity(SettingsActivity.this,RequestCodeConstant.RESET_PWD);
			}

			break;
		// 添加手动检测升级功能: 添加人:时培飞 添加时间:2014-12-01
		case R.id.hand_update:
			MobclickAgent.onEvent(mContext, "1034");
			checkUpdate();
			break;
		case R.id.version_name:
			MobclickAgent.onEvent(mContext, "1034");
			checkUpdate();
			break;
		case R.id.clearCache:
			// 清除缓存对话框
			MobclickAgent.onEvent(mContext, "1034");
			DialogView.getAlertDialogWithoutTitle(mContext,
					getString(R.string.dialog_clear_cache),
					getString(R.string.dialog_clear_cache_cancle),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}, getString(R.string.dialog_clear_cache_sure),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.showToast(mContext,
									getString(R.string.clear_cache_success));
						}
					});
			break;
		case R.id.tel:
			// 拨打客服电话对话框
			MobclickAgent.onEvent(mContext, "1035");
			DialogView.getAlertDialogWithoutTitle(
					mContext,
					getString(R.string.call_string) + getString(R.string.call),
					getString(R.string.call_string),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + getString(R.string.call)));
							startActivity(intent);
						}
					}, getString(R.string.call_cancle),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});

			break;
		case R.id.aboutUs:
			// 关于我们
			MobclickAgent.onEvent(mContext, "1036");
			String versionName = DeviceUtil.getVersonName(this);
			String platform = "Android";
			String url = getString(R.string.about_us_url) + "?platform="
					+ platform + "&version=v" + versionName;
			IntentUtil.startWebView(mContext, getString(R.string.about_us),
					url, "1036", null);
			break;
		case R.id.exit:
			// 退出登陆
			MobclickAgent.onEvent(mContext, "1037");
			showPopUpWindow();
			break;

		default:
			break;
		}

	}

	/**
	 * @discretion: 显示退出窗口
	 * @author: MaoYaNan
	 * @date: 2014-10-11 下午3:35:05
	 */
	private void showPopUpWindow() {
		mShow = ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelableOnTouchOutside(true)
				.setPanelLayout(R.layout.item_popupwindow_logout)
				.setListener(this).show();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// ToastUtil.showToast(mContext, "消失");
	}

	@Override
	public void onSetPanelListener(LinearLayout panel) {
		Button button1 = (Button) panel.findViewById(R.id.button1);
		Button button2 = (Button) panel.findViewById(R.id.button2);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mModule.logout(mContext);
				mShow.dismiss();
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mShow.dismiss();
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		MobclickAgent.onEvent(mContext, "1032");

		// AppSettingController.setPushMsg(mContext,isChecked);//调用是否接收推送的方法
		AppSettingController.getInstance().onPushSettingChanged(mContext,
				isChecked);
	};

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == Activity.RESULT_OK) {
			if (arg0 == RequestCodeConstant.RESET_PWD) {
				mExit.setVisibility(View.VISIBLE); // 显示退出按钮
				IntentUtil.startPwdResetActivity(mContext);
			}
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

}
