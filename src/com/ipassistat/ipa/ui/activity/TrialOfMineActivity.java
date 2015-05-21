package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.ui.fragment.TrialContentFragment;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * @Discription： 我的试用界面
 * @package： com.ichsy.mboss.activity.TrialOfMineActivity
 * @author： MaoYaNan
 * @date：2014-10-13 下午5:42:39
 */
public class TrialOfMineActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private static final String TAG = "MyTrialActivity";
	private Context mContext;
	private ViewPager viewPager;
	private TitleBar titlebar;
	private String stringTitle;
	private FragmentManager manager;
	private List<Fragment> frags = new ArrayList<Fragment>();
	private List<RadioButton> titles = new ArrayList<RadioButton>();
	private TrialPagerAdapter adapter;
	private RadioButton trial_free;
	private RadioButton trial_postage;
	private RadioGroup trial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trail_of_mine);
		mContext = this;
		// 获得调用参数
		Intent intent = getIntent();
		stringTitle = intent.getStringExtra("title");
		if (stringTitle == null) {
			stringTitle = "";
		}
		manager = getSupportFragmentManager();
		initView();
		initDatas();
		initListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("1016"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("1016"); // 保证 onPageEnd 在onPause 之前调用,因为
	}

	private void initDatas() {
		titlebar.setTitleText(stringTitle);
		// 添加标题
		titles.add(trial_free);
		titles.add(trial_postage);
		//TrialContentFragment trial_free = new TrialContentFragment(
				//Constant.TRIAL_FREE);
		//TrialContentFragment trial_postage = new TrialContentFragment(
				//Constant.TRIAL_POSTAGE);

		// 添加Fragment
		//frags.add(trial_free);
		//frags.add(trial_postage);
		// 设置适配器
		adapter = new TrialPagerAdapter(manager);
		viewPager.setAdapter(adapter);

	}

	private void initListeners() {
		titlebar.setButtonClickListener(TitleBarButton.leftImgv,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(mContext, "1046");
						finish();
					}
				});
		titlebar.setButtonClickListener(TitleBarButton.rightImgv,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		titlebar.setVisibility(TitleBarButton.rightImgv, View.GONE);
		trial_free.setOnClickListener(this);
		trial_postage.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);

	}

	private void initView() {
		// TitleBar
		titlebar = (TitleBar) findViewById(R.id.titleBar);
		trial = (RadioGroup) findViewById(R.id.trial_radiobutton);
		trial_free = (RadioButton) findViewById(R.id.trial_free);
		trial_postage = (RadioButton) findViewById(R.id.trial_postage);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	class TrialPagerAdapter extends FragmentPagerAdapter {

		public TrialPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return frags.get(arg0);
		}

		@Override
		public int getCount() {
			return frags.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		trial.check(arg0);
		if (arg0 == 0) {
			MobclickAgent.onEvent(mContext, "1044");
			trial_free.setChecked(true);
		} else {
			MobclickAgent.onEvent(mContext, "1043");
			trial_postage.setChecked(true);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.trial_free:
			MobclickAgent.onEvent(mContext, "1044");
			viewPager.setCurrentItem(0, true);
			break;
		case R.id.trial_postage:
			MobclickAgent.onEvent(mContext, "1043");
			viewPager.setCurrentItem(1, true);
			break;

		default:
			break;
		}
	}
}
