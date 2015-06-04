package com.ipassistat.ipa.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.adapter.SimperViewPagerAdapter;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.domain.action.DomainContext;
import com.ipassistat.ipa.plugin.model.FileSpec;
import com.ipassistat.ipa.plugin.model.SiteSpec;
import com.ipassistat.ipa.serviceandbroacast.ContacterSyncService;
import com.ipassistat.ipa.serviceandbroacast.SendContacterService;
import com.ipassistat.ipa.ui.fragment.HomeFragment;
import com.ipassistat.ipa.ui.fragment.MeFragment;
import com.ipassistat.ipa.ui.personal.activity.LoginActivity;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.SharedPreferenceUtil;
import com.ipassistat.ipa.util.map.baidu.LocationMessage;
import com.ipassistat.ipa.util.map.baidu.MyLocationListenner;
import com.ipassistat.ipa.view.TableViewPager;
import com.umeng.analytics.MobclickAgent;

/***
 * 首页 com.ipassistat.ipa.ui.activity.MainActivity
 * 
 * @author 时培飞 Create at 2015-4-24 下午5:44:58
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private SiteSpec site;
	private FileSpec file;
	private String fragmentName;
	private boolean loaded;
	
	private Theme theme;
	private FrameLayout rootView;

	private final String TAG = "MainActivity";
	public static final String INTENT_INTENT_TYPE = "intent_type";
	public static final String INTENT_INTENT_POSITON = "intent_position";
	protected ProgressDialog pd;

	// 首页的Table
	private TableViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private int[] mButtonMenuId;
	private int mOldPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_viewpager);

		disableSlideClose();

		initWidget();

		checkIntent(getIntent());

		// 注册EventBus消息传送机制
		// EventBus.getDefault().register(this);

		/*
		 * Uri uri = getIntent().getData(); if (uri != null) {
		 * LogUtil.outLogDetail(TAG, "schme:" + uri.toString());
		 * 
		 * // 解析URI String[] str = uri.toString().split("[=]"); String type =
		 * str[str.length - 1]; String skuCode = str[2].split("[&]")[0];
		 * 
		 * // 判断需要跳转的界面类型 if (type.equals(Constant.GOODS_TYPE_NORMAL) ||
		 * type.equals(Constant.GOODS_TYPE_LIMIT)) { //
		 * IntentUtil.startGoodsDetail(this, skuCode, type); } else if
		 * (type.equals("5")) { IntentUtil.startSisterGroupDetail(this,
		 * str[2].split("&")[0]); } else { String s = str[3].split("[&]")[0];
		 * String endTimeOld = Uri.decode(s); String endTime =
		 * endTimeOld.replace("+", " "); if
		 * (type.equals(Constant.GOODS_TYPE_FREE)) {
		 * IntentUtil.jumpToTrialInfoActivity(this, Constant.TRIAL_FREE,
		 * skuCode, endTime); } else { IntentUtil.jumpToTrialInfoActivity(this,
		 * Constant.TRIAL_POSTAGE, skuCode, endTime); } } }
		 */

		DomainContext.getInstance().context = this;
		// 判断是否已经发送了联系人信息
		if (TextUtils.isEmpty(SharedPreferenceUtil.getStringInfo(this, ConfigInfo.IS_SEND_CONTACTER))) {
			// 开启发送联系人信息服务
			Intent sendContact = new Intent();
			sendContact.setClass(this, SendContacterService.class);
			startService(sendContact);
		}

		// 开启监控联系人信息服务
		Intent sendContact = new Intent();
		sendContact.setClass(this, ContacterSyncService.class);
		startService(sendContact);
		
	
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		checkIntent(intent);
	}

	/***
	 * 检查是否需要跳转(例如推送点击打开的)
	 * 
	 * @author 时培飞 Create at 2015-4-24 下午5:49:48
	 */
	private void checkIntent(Intent intent) {
		if (getIntent() == null)
			return;

	}

	/***
	 * 初始化界面控件以及数据
	 * 
	 * @author 时培飞 Create at 2015-4-16 下午7:00:37
	 */
	private void initWidget() {

		mViewPager = (TableViewPager) findViewById(R.id.viewpager);
		mRadioGroup = (RadioGroup) findViewById(R.id.bottom_banner);
		mButtonMenuId = new int[] { id.home_tv, id.time_limited_buy, id.sister_group, id.tiny_community };

		// final TinyCommunityFragment tinyCommunityFragment = new
		// TinyCommunityFragment();
		List<Fragment> _fragments = new ArrayList<Fragment>();
		_fragments.add(new HomeFragment());
		// _fragments.add(new RecreationFragment());
		// _fragments.add(new SisterGroupFragment());
		 _fragments.add(new MeFragment());

		SimperViewPagerAdapter adapter = new SimperViewPagerAdapter(getSupportFragmentManager(), _fragments);

		mViewPager.setAdapter(adapter);
		mRadioGroup.check(mButtonMenuId[0]);

		for (int i = 0; i < mButtonMenuId.length; i++) {
			findViewById(mButtonMenuId[i]).setOnClickListener(this);
		}
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				mRadioGroup.check(mButtonMenuId[position]);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// radioGroup.check(buttonMenuId[0]);
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

		if (arg0 == 200) {
			if (arg1 != LoginActivity.RESULT_OK) {
				mRadioGroup.check(mButtonMenuId[mOldPosition]);
				mViewPager.setCurrentItem(mOldPosition);
			} else {
				mViewPager.setCurrentItem(3);
			}
		}

	}

	// /**
	// * 在主线程中处理接收EventBus发送的用户消息
	// *
	// * @return void
	// * @param event
	// * @author 时培飞 Create at 2015-4-8 上午11:26:00
	// */
	// public void onEventMainThread(MainMessageEvent event) {
	//
	// setMessageState(Integer.parseInt(event.getMsg().toString()));
	//
	// }

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {

	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {

	}

	@Override
	public void onError(String ur, String result) {
	}

	@Override
	public void onNoNet() {

	}

	@Override
	public void onNoTimeOut() {

	}

	@Override
	public void onFinish() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1004"); // 统计页面

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mViewPager.getCurrentItem() != 0) {
				mViewPager.setCurrentItem(0, false);
			} else {
				MainActivity.this.finish();
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		mOldPosition = mViewPager.getCurrentItem();
		switch (v.getId()) {
		case id.home_tv:
			mViewPager.setCurrentItem(0, false);
			break;
		case id.time_limited_buy:
			mViewPager.setCurrentItem(1, false);
			break;
		case id.sister_group:
			mViewPager.setCurrentItem(2, false);
			break;
		case id.tiny_community:
			MobclickAgent.onEvent(this, "1202");
			mViewPager.setCurrentItem(3, false);

			break;

		}
	}

	public void onPause() {
		super.onPause();
	}

	public void onDestory() {
		super.onDestroy();

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
