package com.ipassistat.ipa.ui.activity;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.CityLocalEntity;
import com.ipassistat.ipa.bean.local.CityModel;
import com.ipassistat.ipa.util.FileUtis;
import com.ipassistat.ipa.util.eventbus.MessageEvent;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.sortlistview.ISort;
import com.ipassistat.ipa.view.sortlistview.SortListView;
import com.ipassistat.ipa.view.sortlistview.SortListView.OnSortListViewClickListener;
import com.umeng.analytics.MobclickAgent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 地区选择
 * 
 * Package: com.ichsy.mboss.ui.activity
 * 
 * File: CityChoiceActivity.java
 * 
 * @author: 任恒 Date: 2015-2-2
 * 
 *          Modifier： Modified Date： Modify：
 * 
 *          Copyright @ 2015 Corpration Name
 * 
 */
public class CityChoiceActivity extends BaseActivity implements ISort<CityLocalEntity> {

	private SortListView<CityLocalEntity> mSortListView;

	private TitleBar mBar;

	public static final String SELECT_CITY_NAME = "SELECT_CITY_NAME";

	private Activity mActvity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_choice);
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1070"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1070");
		MobclickAgent.onResume(this);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		initTitleBar();
		mActvity = CityChoiceActivity.this;
		mSortListView = (SortListView<CityLocalEntity>) findViewById(R.id.sortlistview);
		mSortListView.setSortData(this);
		mSortListView.setOnSortListViewClick(new OnSortListViewClickListener<CityLocalEntity>() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(int position, CityLocalEntity t) {
				
				//EventBus.getDefault().post(new MessageEvent("CityChoiceActivity",t));
				mActvity.finish();
			}
		});
	}

	private void initTitleBar() {
		mBar = (TitleBar) findViewById(R.id.bar);
		mBar.setTitleText("地区");
	}

	private CityModel getCityData() {
		try {
			String str = FileUtis.convertStreamToString(getAssets().open("city.json"));
			Gson gson = new Gson();
			CityModel model = gson.fromJson(str, CityModel.class);
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getSortStrings() {
		Iterator<CityLocalEntity> it = getCityData().getList().iterator();
		List<String> citys = new LinkedList<String>();
		while (it.hasNext()) {
			citys.add(it.next().getCityName());
		}

		return citys;
	}

	@Override
	public List<CityLocalEntity> getSortModel() {
		return getCityData().getList();
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
