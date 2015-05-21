package com.ipassistat.ipa.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.adapter.viewholder.RecreationAdapter;
import com.ipassistat.ipa.bean.response.RecreationResponse;
import com.ipassistat.ipa.business.RecreationModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.view.NoticeView;
import com.ipassistat.ipa.view.PullPinnedHeaderListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PullPinnedHeaderListView.OnPinedRefreshListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @Description 娱乐首页
 * @author gbr
 * @author lis
 * @date 2015-4-20
 * 
 */
public class RecreationFragment extends BaseFragment implements OnItemClickListener,
		OnPinedRefreshListener, OnScrollListener {

	/** 整体页面的Title */
	private TitleBar mTitleBar;
	/** 娱乐首页ListView */
	private PullPinnedHeaderListView mBodyListView;
	/** 预加载View */
	private NoticeView mNoticeView;
	/** 娱乐首页ListView适配器 */
	private RecreationAdapter mRecreationAdapter;
	/** 娱乐请求数据Module */
	private RecreationModule mRecreationModel;
	private View mView;
	LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		mView = inflater.inflate(R.layout.fragment_recreation, container, false);
		return mView;
	}

	@Override
	protected void onLazyCreate() {
		super.onLazyCreate();
		mTitleBar = (TitleBar) mView.findViewById(id.recreation_title_bar);
		mTitleBar.setTitleText(getString(R.string.recreation));
		mTitleBar.setVisibility(TitleBarButton.leftImgv, View.GONE);
		mBodyListView = (PullPinnedHeaderListView) mView.findViewById(id.recreation_layout);
		mNoticeView = (NoticeView) mView.findViewById(R.id.noticeView);
		mNoticeView.showLoadingView(true);
		mRecreationAdapter = new RecreationAdapter(getActivity());
		View headerView = mInflater.inflate(R.layout.adapter_recreation_header, mBodyListView,
				false);

		mBodyListView.setPinnedHeaderView(headerView); // 设置头试图

		// 没有加载数据显示的布局
		mBodyListView.setDividerHeight(10);
		mRecreationModel = new RecreationModule(this);
		mBodyListView.setPullAvailable(true, false);
		mRecreationModel.getChannel(getActivity());
		registListener();
	}

	/**
	 * 注册监听
	 */
	private void registListener() {
		mBodyListView.setOnItemClickListener(this);
		mBodyListView.setOnScrollListener(this);
		mBodyListView.setonRefreshListener(this);
		mBodyListView.setSelector(new ColorDrawable(Color.TRANSPARENT));

	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (getActivity() == null)
			return;
		mNoticeView.close();
		RecreationResponse response = (RecreationResponse) object;
		if (url.equals(ConfigInfo.API_VIDEO_CHANNEL)) {
			mRecreationAdapter.clear();
			if (response.getChannel() == null || response.getChannel().size() == 0) {
				mNoticeView.showCustomView(true, null, R.drawable.error_image,
						drawable.error_tryout_nodate);
			} else {
				for (int i = 0; i < response.getChannel().size(); i++) {
					if (response.getChannel().get(i).getVideoList() != null
							&& response.getChannel().get(i).getVideoList().size() > 0) {
						mRecreationAdapter.addSeparatorItem(response.getChannel().get(i));
						mRecreationAdapter.addItem(response.getChannel().get(i));
					}
				}
				mBodyListView.setAdapter(mRecreationAdapter);
				mBodyListView.onRefreshComplete();
			}

		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		if (url.equals(ConfigInfo.API_VIDEO_CHANNEL)) {
			mBodyListView.onRefreshComplete();
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mNoticeView.showCustomView(true, new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNoticeView.showLoadingView(true);
				mRecreationModel.getChannel(getActivity());
			}
		}, drawable.error_no_net_top, drawable.error_no_net);
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mNoticeView.showCustomView(true, new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNoticeView.showLoadingView(true);
				mRecreationModel.getChannel(getActivity());
				mBodyListView.onRefreshComplete();
			}
		}, drawable.error_no_net_top, drawable.error_no_net);
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mBodyListView.onRefreshComplete();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		mRecreationAdapter.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		mBodyListView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	@Override
	public void onRefresh(boolean isTop) {
		if (isTop) {
			mRecreationModel.getChannel(getActivity());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1080"); // 统计页面
		MobclickAgent.onResume(getActivity()); // 统计时长
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1080");
		MobclickAgent.onPause(getActivity());
	}
}
