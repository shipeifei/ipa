package com.ipassistat.ipa.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.adapter.PaginationAdapter;
import com.ipassistat.ipa.adapter.SisterGroupMessageMineAdapter;
import com.ipassistat.ipa.adapter.SisterGroupMessageSystemAdapter;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.SystemMessageResponse;
import com.ipassistat.ipa.bean.response.UserMessageResponse;
import com.ipassistat.ipa.bean.response.entity.SystemMessageVo;
import com.ipassistat.ipa.bean.response.entity.UserMessageVo;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;

/**
 * 姐妹圈 - 消息中心
 * 
 * @author LiuYuHang
 * @date 2014年10月10日
 * 
 */
public class SisterGroupMessageFragment extends BaseFragment {
	public static final int TYPE_SYSTEM = 111;
	public static final int TYPE_MINE = TYPE_SYSTEM + 1;

	private int mMessageType;
	private PaginationListView mListView;

	
//	@SuppressLint("ValidFragment")
//	public SisterGroupMessageFragment(int messageType) {
//
//		this.mMessageType = messageType;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_message_listview, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mListView = (PaginationListView) getView().findViewById(R.id.listview_main);
		mListView.setEmptyRes(drawable.icon_information, drawable.error_empty_sistergroup_mine);
		mListView.setDividerHeight(10);
		mListView.setOnPaginationListener(new PaginationListener() {

			@Override
			public void onRequestNextPage(int page) {
				//new SisterGroupModule(SisterGroupMessageFragment.this).messageOperationListPull(getActivity(), mMessageType, page + 1);
			}

			@Override
			public void onRefresh() {
				//new SisterGroupModule(SisterGroupMessageFragment.this).messageOperationListPull(getActivity(), mMessageType, 0);
			}
		});

		// 向服务端请求数据
		//new SisterGroupModule(this).messageOperationListPull(getActivity(), mMessageType, 0);
	}

	/**
	 * 根据数据刷新UI
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 * 
	 * @param url
	 * @param response
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initListViewAdapter(String url, BaseResponse response) {
		PaginationAdapter adapter = mListView.getAdapter();

		int page = (Integer) response.getTag(); // page为0是第一次加载，不为零则为翻页

		switch (mMessageType) {
		case TYPE_SYSTEM:
			SystemMessageResponse sysResponse = (SystemMessageResponse) response;
			if (page == 0) {
				adapter = new SisterGroupMessageSystemAdapter(getActivity(), sysResponse.messages);
				mListView.setAdapter(adapter);
			} else {
				adapter.loadNextPage(sysResponse.messages);
			}
			mListView.delegatePageCheck(sysResponse.paged);
			asyncPostReadState(sysResponse.messages);
			break;
		case TYPE_MINE:
			UserMessageResponse userResponse = (UserMessageResponse) response;
			if (page == 0) {
				adapter = new SisterGroupMessageMineAdapter(getActivity(), userResponse.messages);
				mListView.setAdapter(adapter);

				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						PaginationAdapter adapter = mListView.getAdapter();
						UserMessageVo userMessageVo = (UserMessageVo) adapter.getItem(position - 1);
						String postId = userMessageVo.post_code;

						IntentUtil.startSisterGroupDetail(getActivity(), postId);
					}

				});
			} else {
				adapter.loadNextPage(userResponse.messages);
			}
			mListView.delegatePageCheck(userResponse.paged);

			asyncPostReadState(userResponse.messages);
			break;
		}

	}

	/**
	 * 向服务器发送已经查看过的标识，会判断列表中是否有未读的数据，整理之后发送
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月13日
	 */
	private void asyncPostReadState(List<? extends SystemMessageVo> list) {
		if (list == null || list.isEmpty()) return;

		List<SystemMessageVo> copyList = new ArrayList<SystemMessageVo>(list);
		Iterator<SystemMessageVo> iterator = copyList.iterator();
		String ids = "";
		while (iterator.hasNext()) {
			SystemMessageVo item = iterator.next();
			if (item.is_read == SystemMessageVo.TYPE_NO_READ) {
				ids += item.message_code + ",";
			}
		}
		if (!TextUtils.isEmpty(ids)) {
			//new SisterGroupModule(this).messageOperationStateChange(getActivity(), StringUtil.cutLastString(ids));
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object response) {
		super.onMessageSucessCalledBack(url, response);
		if (getActivity() == null) return;
		if (url.equals(ConfigInfo.API_MESSAGE_SYSTEM) || url.equals(ConfigInfo.API_MESSAGE_MINE)) {
			initListViewAdapter(url, (BaseResponse) response);
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mListView.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mListView.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mListView.onRefreshComplete();
	}
}
