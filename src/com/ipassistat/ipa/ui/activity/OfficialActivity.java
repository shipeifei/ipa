package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.OfficialAdapter;
import com.ipassistat.ipa.bean.response.OfficialResponse;
import com.ipassistat.ipa.bean.response.entity.Activity;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.business.OfficialCenterModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 官方活动
 * 
 * @author renheng
 * @author lis 优化
 * @date 2015-03-30
 * 
 */
public class OfficialActivity extends BaseActivity implements OnClickListener, PaginationListener {
	/** 标题栏 */
	private TitleBar mTitlebar;
	/** 自定义的可翻页ListView */
	private PaginationListView mListView;
	/** 每页条数 */
	private int limit = 10;
	/** 页数 */
	private int page = 0;
	/** 官方活动模块实例 */
	private OfficialCenterModule module = new OfficialCenterModule(this);
	/** 官方活动适配器 */
	private OfficialAdapter mOfficialAdapter;
	private Context mContext = this;
	/** 无数据View */
	private View mNoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_official);
		init();
		initListeners();
		refreshOfficialList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1027"); // 统计页面
		MobclickAgent.onResume(this);      // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1027"); // 保证 onPageEnd 在onPause 之前调用,因为onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * ListView监听
	 */
	private void initListeners() {
		mListView.setOnPaginationListener(this);
	}

	/**
	 * 布局初始化
	 */
	private void init() {
		initTitlebar();
		mNoView = findViewById(R.id.no);
		mListView = (PaginationListView) findViewById(R.id.offcial_activities);
		mListView.setDividerHeight(34);
	}

	/**
	 * Title初始化
	 */
	private void initTitlebar() {
		mTitlebar = (TitleBar) findViewById(R.id.titlebar);
		mTitlebar.setTitleText(getString(R.string.official_activities));
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, this);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (url.equals(ConfigInfo.API_OFFICIAL_LIST)) {
			OfficialResponse resp = (OfficialResponse) object;
			final List<Activity> mDatas = resp.getActivities();
			if (mDatas.size() == 0) {
				mNoView.setVisibility(View.VISIBLE);
			} else {
				mNoView.setVisibility(View.GONE);
				if (mOfficialAdapter == null) {
					mOfficialAdapter = new OfficialAdapter(this, mDatas);
					mListView.setAdapter(mOfficialAdapter);
				} else {
					mOfficialAdapter.loadNextPage(mDatas);
				}
				/*
				 * 获取是否更多结果判断码
				 */
				mListView.delegatePageCheck(resp.getPaged());
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Activity vo = mDatas.get(position - 1);
						ShareInfoEntity shareInfoEntity = new ShareInfoEntity();
						shareInfoEntity.setContent(vo.getInfo_content());
						shareInfoEntity.setPicUrl(vo.getShare_pic());
						shareInfoEntity.setTargetUrl(vo.getUrl());
						shareInfoEntity.setSMSContent(vo.getInfo_content());
						shareInfoEntity.setTitle(vo.getName());
						MobclickAgent.onEvent(OfficialActivity.this, "1076", vo.getName());
						IntentUtil.startWebView(getApplicationContext(), vo.getName(), vo.getUrl(),
								"1028", "1079", shareInfoEntity);
					}
				});
			}
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		mListView.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {
				page = 0;
				module.getOfficial(OfficialActivity.this, limit, page);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			MobclickAgent.onEvent(mContext, "1077");
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新列表
	 */
	private void refreshOfficialList() {
		mOfficialAdapter = null;
		page = 0;
		module.getOfficial(this, limit, page);
	}

	@Override
	public void onRefresh() {
		refreshOfficialList();
	}

	@Override
	public void onRequestNextPage(int page) {
		module.getOfficial(this, limit, page + 1);
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mListView.onRefreshComplete(); // 刷新完成
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
