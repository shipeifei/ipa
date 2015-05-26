package com.ipassistat.ipa.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.CityLocalEntity;
import com.ipassistat.ipa.bean.local.CityModel;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.util.ContactsUtil;
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

/***
 * 
 * @author shipeifei
 * 
 */
public class CityChoiceActivity extends BaseActivity implements ISort<ContactPerson> {

	private SortListView<ContactPerson> mSortListView;

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
		mSortListView = (SortListView<ContactPerson>) findViewById(R.id.sortlistview);
		mSortListView.setSortData(this);
		mSortListView.setOnSortListViewClick(new OnSortListViewClickListener<ContactPerson>() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(int position, ContactPerson t) {

				// EventBus.getDefault().post(new
				// MessageEvent("CityChoiceActivity",t));
				mActvity.finish();
			}
		});
	}

	private void initTitleBar() {
		mBar = (TitleBar) findViewById(R.id.bar);
		mBar.setTitleText("联系人");
	}

	/***
	 * 
	 * create at 2015-5-22 author 时培飞
	 */
	private Map<String, ContactPerson> getContactData() {
		Map<String, ContactPerson> persons = ContactsUtil.getSharedInstance().getPhoneContacts(getApplicationContext(),true);// 读取本地通讯录
		persons.putAll(ContactsUtil.getSharedInstance().getSIMContacts(getApplicationContext(),true));// 读取SIM卡
		return persons;
	}

	/**
	 * 把Map转化成list
	 * 
	 * @param map
	 *            需要转换的Map
	 * @param list
	 *            需要接受的list
	 */
	private List<ContactPerson> iteratorMap(Map<String, ContactPerson> map) {

		List<ContactPerson> list = new ArrayList<ContactPerson>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			list.add(map.get(key));
		}

		return list;
	}

	@Override
	public List<ContactPerson> getSortStrings() {
		List<ContactPerson> canInstroduce = iteratorMap(getContactData());

		return canInstroduce;
	}

	@Override
	public List<ContactPerson> getSortModel() {
		List<ContactPerson> canInstroduce = iteratorMap(getContactData());

		return canInstroduce;
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
