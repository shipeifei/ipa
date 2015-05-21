package com.ipassistat.ipa.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.PaginationAdapter;
import com.ipassistat.ipa.adapter.RecreationTvListAdapter;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.VideoListResponse;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;
import com.ipassistat.ipa.bean.response.entity.VideoList;
import com.ipassistat.ipa.business.RecreationModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 视频列表
 * 
 * @author wrj
 * @author lis
 * 
 */
public class RecreationTvListActivity extends BaseActivity implements PaginationListener {
	/** 标题 */
	private TitleBar mtitleBar;
	/** 精彩花絮列表 */
	private PaginationListView mpaginationlistview;
	/** 娱乐请求数据Module */
	private RecreationModule mRecreationModule;
	/** 标题名 */
	private String mtitleName;
	int curentPage = 0;
	private VideoChannel mVideoChannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recreation_tvlist);
		initview();

		mRecreationModule = new RecreationModule(this);
		mVideoChannel = (VideoChannel) getIntent().getSerializableExtra(
				IntentKey.RECREATION_TVLIST_TITLE);
		mtitleName = mVideoChannel.getChannel_name();
		mtitleBar.setTitleText(mtitleName);
		registerListener();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initListViewAdapter(String url, BaseResponse response) {
		PaginationAdapter mPaginationAdapter = mpaginationlistview.getAdapter();
		int page = (Integer) response.getTag(); // page为0是第一次加载，不为零则为翻页
		LogUtil.outLogDetail("page", page);
		VideoListResponse videoListResponse = (VideoListResponse) response;
		if (mPaginationAdapter == null || page == 0) {
			mPaginationAdapter = new RecreationTvListAdapter(RecreationTvListActivity.this,
					videoListResponse.getVideoList());
			mpaginationlistview.setAdapter(mPaginationAdapter);
		} else {
			mPaginationAdapter.loadNextPage(videoListResponse.getVideoList());
		}
		mpaginationlistview.delegatePageCheck(videoListResponse.getPaged());
	}

	/**
	 * 初始化布局
	 * 
	 * @param bean
	 */
	private void initview() {
		mtitleBar = (TitleBar) findViewById(R.id.recreation_tvlist_title_bar);
		mpaginationlistview = (PaginationListView) findViewById(R.id.listView_snippet);
		mpaginationlistview
				.setEmptyRes(drawable.error_image, drawable.error_empty_sistergroup_mine);
		mpaginationlistview.setOnPaginationListener(this);
	}

	private void registerListener() {
		mpaginationlistview.setEmptyViewLisenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mpaginationlistview.showLoadingView(true);
				mRecreationModule.getVideoList(RecreationTvListActivity.this,
						mVideoChannel.getChannel_code(), curentPage);
				mpaginationlistview.onRefreshComplete();

			}
		});
		/**
		 * 视频列表点击事件
		 */
		mpaginationlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("rawtypes")
				PaginationAdapter mPaginationAdapter = mpaginationlistview.getAdapter();
				VideoList mVideoList = (VideoList) mPaginationAdapter.getItem(position - 1);
				String videoId = mVideoList.getRecreation_name()
						+ mVideoList.getRecreation_updatesum();
				MobclickAgent.onEvent(RecreationTvListActivity.this, "1217", videoId);
				String title = mVideoList.getRecreation_name();
				String recreation_url = mVideoList.getRecreation_url();
				ShareInfoEntity mShareInfoEntity = new ShareInfoEntity();
				mShareInfoEntity.setTitle(getString(string.recreation_video_share_title));
				mShareInfoEntity.setTargetUrl(recreation_url);
				mShareInfoEntity.setContent(getString(string.recreation_video_share_content));
				mShareInfoEntity
						.setSMSContent(getString(string.recreation_video_share_content_msg));
				mShareInfoEntity.setPicUrl(mVideoList.getPicInfos().get(0).getSmallPicInfo()
						.getPicUrl());
				mShareInfoEntity.setBackCode("1219");
				mShareInfoEntity.setShareCode("1218");
				IntentUtil.startWebView(RecreationTvListActivity.this, title, recreation_url,
						"1073", mShareInfoEntity);
			}
		});
		mRecreationModule.getVideoList(RecreationTvListActivity.this,
				mVideoChannel.getChannel_code(), curentPage);

		mtitleBar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(RecreationTvListActivity.this, "1219");
				finish();
			}
		});
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mpaginationlistview.noNet(new OnClickListener() {
			@Override
			public void onClick(View v) {
				curentPage = 0;
				mRecreationModule.getVideoList(RecreationTvListActivity.this,
						mVideoChannel.getChannel_code(), curentPage);
			}
		});
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mpaginationlistview.noNet(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				curentPage = 0;
				mRecreationModule.getVideoList(RecreationTvListActivity.this,
						mVideoChannel.getChannel_code(), curentPage);
			}
		});
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (url.equals(ConfigInfo.API_VIDEO_LIST)) {
			initListViewAdapter(url, (BaseResponse) object);
			mpaginationlistview.onRefreshComplete();
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		if (url.equals(ConfigInfo.API_VIDEO_LIST)) {
			mpaginationlistview.onRefreshComplete();
		}
	}

	@Override
	public void onRefresh() {
		curentPage = 0;
		mRecreationModule.getVideoList(RecreationTvListActivity.this,
				mVideoChannel.getChannel_code(), curentPage);
	}

	public void onRequestNextPage(int page) {
		curentPage++;
		mRecreationModule.getVideoList(RecreationTvListActivity.this,
				mVideoChannel.getChannel_code(), curentPage);
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mpaginationlistview.onRefreshComplete();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1072"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1072"); //
		MobclickAgent.onPause(this);
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
