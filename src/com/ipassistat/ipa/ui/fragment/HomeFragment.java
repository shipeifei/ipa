package com.ipassistat.ipa.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.HomeAdatper;
import com.ipassistat.ipa.bean.response.Advertise;
import com.ipassistat.ipa.bean.response.AppHomeResponse;
import com.ipassistat.ipa.bean.response.BannerResponse;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;
import com.ipassistat.ipa.business.GoodsModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.domain.SendMessageDomain;
import com.ipassistat.ipa.domain.TelephoneDomain;
import com.ipassistat.ipa.domain.bean.DomainBaseResponse;
import com.ipassistat.ipa.ui.activity.GoodsListActivity;
import com.ipassistat.ipa.ui.activity.OfficialActivity;
import com.ipassistat.ipa.ui.activity.PersonalActivity;
import com.ipassistat.ipa.util.ContactsUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.JsonParser;
import com.ipassistat.ipa.util.PackageUtil;
import com.ipassistat.ipa.util.SendSmsManager;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.map.baidu.LocationMessage;
import com.ipassistat.ipa.util.map.baidu.MyLocationListenner;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.ipassistat.ipa.view.banner.AutoBanner;
import com.ipassistat.ipa.view.pulldown.PullToRefreshView;
import com.ipassistat.ipa.view.pulldown.PullToRefreshView.OnHeaderRefreshListener;
import com.umeng.analytics.MobclickAgent;
import java.text.DateFormat;

/***
 * 首页 com.ipassistat.ipa.ui.fragment.HomeFragment
 * 
 * @author 时培飞 Create at 2015-4-30 下午2:41:30
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnPageChangeListener, OnHeaderRefreshListener {

	private TextView mGoodsTextView, mTrialCenterTextView, mOfficalActiTextView, mMyCenterTextView;

	private Button recognize;
	private EditText mResultText;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog iatDialog;
	public static final String PREFER_NAME = "com.iflytek.setting";
	int ret = 0;// 函数调用返回值

	private TitleBar mTitleBar;
	private View mView;
	private Activity mActivity;

	private boolean isRefresh;
	private int mPageSize = 10;
	private int mCurrentPage = 0;
	//private SisterGroupModule mSisterGroupModule;
	private SharedPreferences mSharedPreferences;
	/**
	 * 是否刷新当前页面 1：只要进入了贴子详情页面，下次就必须刷新 浏览数发生了变化 。
	 */
	public static boolean isRefreshCurrentPage;

	// 百度地图相关成员变量
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private Handler locationHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 100:

				Bundle bundle = msg.getData();
				LocationMessage locationMessage = (LocationMessage) bundle.get("location");
				if (locationMessage != null) {
					if (!TextUtils.isEmpty(locationMessage.getCurrentAddress())) {
						mTitleBar.setTitleText("当前位置：" + locationMessage.getCurrentAddress());
					} else {
						mTitleBar.setTitleText("定位失败");
					}
				} else {
					mTitleBar.setTitleText("定位失败");
				}
				break;
			case 101:// 定位失败
				mTitleBar.setTitleText("定位失败");

				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_home, null);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//mSisterGroupModule = new SisterGroupModule(this);
		mActivity = getActivity();

		// 开启百度地图
		mLocationClient = new LocationClient(mActivity);
		myListener.messHandler = locationHandler;
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
		mLocationClient.start();

		initWidgets();
		initIat();
		String result = "{\"rc\": 0, \"text\": \"打开浏览器\",\"service\": \"cn.yunzhisheng.appmgr\", \"code\": \"APP_LAUNCH\",error:{\"code\":\"aaa\",\"message\":\"asdfsaf\"}}";
		DomainBaseResponse domainBaseResponse = new DomainBaseResponse();

		Gson gson = new Gson();
		// gson.fromJson(result, domainBaseResponse);
		domainBaseResponse = gson.fromJson(result, DomainBaseResponse.class);
		if (domainBaseResponse != null) {
		}
	}

	/***
	 * 设置百度地图配置信息
	 * 
	 * @author 时培飞 Create at 2015-4-21 下午12:24:09
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setServiceName("com.baidu.location.f");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPriority(LocationClientOption.GpsFirst); // gps
		option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	private void initWidgets() {

		mTitleBar = (TitleBar) mView.findViewById(R.id.title_bar);
		mTitleBar.setVisibility(TitleBarButton.leftImgv, View.GONE);
		mTitleBar.setTitleText("正在定位...");
		mTitleBar.setButtonClickListener(TitleBarButton.rightImgv, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(mActivity, "leftImgv has been clicked");
			}
		});
		initListView();
	}

	private void initListView() {

		mGoodsTextView = (TextView) mActivity.findViewById(R.id.goods);
		mTrialCenterTextView = (TextView) mActivity.findViewById(R.id.trial_center);
		mOfficalActiTextView = (TextView) mActivity.findViewById(R.id.offical_activity);
		mMyCenterTextView = (TextView) mActivity.findViewById(R.id.my_center);

		recognize = (Button) mActivity.findViewById(R.id.iat_recognize);
		mResultText = (EditText) mActivity.findViewById(R.id.iat_text);

		mGoodsTextView.setOnClickListener(this);
		mTrialCenterTextView.setOnClickListener(this);
		mOfficalActiTextView.setOnClickListener(this);
		mMyCenterTextView.setOnClickListener(this);

		recognize.setOnClickListener(this);

	}

	private void initIat() {
		mSharedPreferences = mActivity.getSharedPreferences(PREFER_NAME, Activity.MODE_PRIVATE);
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(mActivity.getApplicationContext(), mInitListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(mActivity.getApplicationContext(), mInitListener);
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			// Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				ToastUtil.showToast(mActivity.getApplicationContext(), "初始化失败,错误码：" + code);
			}
		}
	};

	private void getBannerList(String column_code, boolean isRefresh) {
		if (!isRefresh) {
		}
		new GoodsModule(this).postBarnnerList(mActivity, column_code);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.goods:
			MobclickAgent.onEvent(mActivity, "1017");
			IntentUtil.startEmailSelect(mActivity);
			break;
		case R.id.my_center:
			MobclickAgent.onEvent(mActivity, "1014");
			Intent intent = new Intent();
			intent.setClass(mActivity, PersonalActivity.class);
			intent.putExtra("title", getResources().getString(R.string.personal));
			startActivity(intent);
			break;
		case R.id.offical_activity:
			MobclickAgent.onEvent(mActivity, "1015");
			Intent intent1 = new Intent();
			intent1.setClass(mActivity, OfficialActivity.class);
			intent1.putExtra("title", getResources().getString(R.string.official_activities));
			startActivity(intent1);
			break;
		case R.id.trial_center:
			/*
			 * MobclickAgent.onEvent(mActivity, "1016"); Intent intent2 = new
			 * Intent(); intent2.setClass(mActivity, TrialCenterActivity.class);
			 * intent2.putExtra("title",
			 * getResources().getString(R.string.trial_center));
			 * startActivity(intent2);
			 */
			break;

		case R.id.iat_text:
			break;
		case R.id.iat_recognize:

			mResultText.setText(null);// 清空显示内容
			// 设置参数
			setParam();
			// boolean isShowDialog =
			// mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show),
			// true);
			/*
			 * if (isShowDialog) { // 显示听写对话框
			 * iatDialog.setListener(recognizerDialogListener);
			 * iatDialog.show(); showTip(getString(R.string.text_begin)); } else
			 */

			// 不显示听写对话框
			ret = mIat.startListening(recognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				ToastUtil.showToast(mActivity.getApplicationContext(), "听写失败,错误码：" + ret);
			} else {
				ToastUtil.showToast(mActivity.getApplicationContext(), "开始听写");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			ToastUtil.showToast(mActivity.getApplicationContext(), "开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			ToastUtil.showToast(mActivity.getApplicationContext(), error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			ToastUtil.showToast(mActivity.getApplicationContext(), "结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			Log.i("aa", text);

			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			if (isLast) {
				ToastUtil.showToast(mActivity.getApplicationContext(), mResultText.getText().toString());
				if (mResultText.getText().toString().indexOf("电子邮件") > -1) {
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String packageName = PackageUtil.isExistsApp(mActivity, "电子邮件");
							if (!TextUtils.isEmpty(packageName)) {
								IntentUtil.openInstallApp(mActivity, packageName);
							}
						}
					});
					thread.start();

				} else if (mResultText.getText().toString().indexOf("浏览器") > -1) {
					IntentUtil.openBrowser(mActivity, "http://www.baidu.com");

				} else if (mResultText.getText().toString().indexOf("打电话") > -1) {
					TelephoneDomain telephoneDomain = new TelephoneDomain();
					telephoneDomain.action(mActivity, "哥哥");
				} else if (mResultText.getText().toString().indexOf("发短信") > -1) {
					SendMessageDomain sendMessageDomain = new SendMessageDomain();
					sendMessageDomain.action(mActivity, "测试发送短信");
				} else {
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							IntentUtil.sendSms(mActivity, "100086");
						}
					});
					thread.start();
				}
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			ToastUtil.showToast(mActivity.getApplicationContext(), "当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");

		// 加载识别本地资源，mIat为听写对象，resPath为本地识别资源路径
		// mIat.setParameter(ResourceUtil.ASR_RES_PATH, resPath);
		// 设置引擎类型为本地
		// mIat.setParameter(SpeechConstant.ENGINE_TYPE,
		// SpeechConstant.TYPE_LOCAL);

		// 设置引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);

	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {

		super.onMessageFailedCalledBack(url, object);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onNoNet() {
		super.onNoNet();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1007"); // 统计页面
	}

	public void onPause() {

		super.onPause();
		MobclickAgent.onPageEnd("1007");
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		//mSisterGroupModule.getPopPostList(mActivity, mPageSize, mCurrentPage);
		getBannerList(ConfigInfo.BANNER_CODE, isRefresh);
	}

}
