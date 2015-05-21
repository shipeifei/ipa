package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.adapter.SimperViewPagerAdapter;
import com.ipassistat.ipa.ui.fragment.SisterGroupMessageFragment;
import com.ipassistat.ipa.util.IntentUtil;
import com.umeng.analytics.MobclickAgent;

public class SisterGroupMessageActivity extends BaseActivity implements OnClickListener {
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sistergroup_message);

		setTitleText("消息中心");
		findListeners(this, id.left_imgv, id.button_left, id.button_right);

		disableSlideClose();
		initViewView();
		MobclickAgent.onEvent(getApplicationContext(), "1041");
	}

	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onPageStart("1041"); //统计页面
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		 MobclickAgent.onPageEnd("1041"); //统计页面
	}
	
	private void initViewView() {
		int select = getIntent().getIntExtra(IntentUtil.INTENT_SELECT, 0);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

		//SisterGroupMessageFragment leftFragment = new SisterGroupMessageFragment(SisterGroupMessageFragment.TYPE_MINE);
		//SisterGroupMessageFragment rightFragment = new SisterGroupMessageFragment(SisterGroupMessageFragment.TYPE_SYSTEM);

		List<Fragment> fragments = new ArrayList<Fragment>();
		//fragments.add(leftFragment);
		//fragments.add(rightFragment);

		SimperViewPagerAdapter adapter = new SimperViewPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(select);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					radioGroup.check(id.button_left);
					MobclickAgent.onEvent(getApplicationContext(), "1120");
				} else if (position == 1) {
					radioGroup.check(id.button_right);
					MobclickAgent.onEvent(getApplicationContext(), "1121");
				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			finish();
			break;
		case id.button_left:
			mViewPager.setCurrentItem(0);
			break;
		case id.button_right:
			mViewPager.setCurrentItem(1);
			break;
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
