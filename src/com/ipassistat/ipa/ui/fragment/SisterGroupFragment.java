package com.ipassistat.ipa.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.SisterGroupListViewAdapter;
import com.ipassistat.ipa.bean.response.SisterGroupPostListResponse;
import com.ipassistat.ipa.bean.response.SystemMessageResponse;
import com.ipassistat.ipa.business.SisterGroupModule;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.ui.activity.SisterGroupMessageActivity;
import com.ipassistat.ipa.ui.activity.SisterGroupPostEditActivity;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.http.HttpContext;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 姐妹圈主页
 * 
 * @author LiuYuHang
 * @date 2014年9月9日
 */
public class SisterGroupFragment extends BaseFragment implements OnClickListener {
	public static final int RESULT_CODE_SEND_SUCCESS = 21312312;

	private PaginationListView mListView;
	private SisterGroupListViewAdapter mAdapter;
	private Map<String, String> mCategoryMap;// 存储分类信息,和分类信息title

	private TextView mTitleTextView;
	private ImageView mLeftImageButton, mRightImageButton;

	private LinearLayout mFilterView;
	private PopupWindow mWindow;

	private String mCurrentUserToken;//用户key

	private SisterGroupModule mSisterGroupModule;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sister_group, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MobclickAgent.onEvent(getActivity(), "1039");

		mCurrentUserToken = UserModule.getUserToken(getActivity());

		mSisterGroupModule = new SisterGroupModule(this);
		mSisterGroupModule.init(getActivity());

		// 存放分类的map组，格式(key:全部 value:"0")
		mCategoryMap = new HashMap<String, String>();
		mCategoryMap.put(getString(R.string.post_filter_item_quanbu), SisterGroupModule.POST_TYPE_ALL);
		mCategoryMap.put(getString(R.string.post_filter_item_fabu), SisterGroupModule.POST_TYPE_SENDED);
		mCategoryMap.put(getString(R.string.post_filter_item_canyu), SisterGroupModule.POST_TYPE_PART);
		mCategoryMap.put(getString(R.string.post_filter_item_shoucang), SisterGroupModule.POST_TYPE_COLLECT);

		// mListView = (PullListView) view.findViewById(R.id.post_list);
		mListView = (PaginationListView) view.findViewById(R.id.post_list);

		mTitleTextView = (TextView) view.findViewById(R.id.title);
		mLeftImageButton = (ImageView) view.findViewById(R.id.left_imgv);
		mRightImageButton = (ImageView) view.findViewById(R.id.right_imgv);

		mTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.icon_action_selector, 0);
		mTitleTextView.setCompoundDrawablePadding(ViewUtil.dip2px(getActivity(), 6));
		mTitleTextView.setText(string.post_filter_item_quanbu);

		mLeftImageButton.setImageResource(drawable.icon_message);

		RelativeLayout.LayoutParams rightLp = (android.widget.RelativeLayout.LayoutParams) mRightImageButton.getLayoutParams();
		if (rightLp == null) {
			rightLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		rightLp.rightMargin = 0;
		mRightImageButton.setLayoutParams(rightLp);
		mRightImageButton.setImageResource(drawable.icon_edit_post);
		ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, mRightImageButton);

		ViewUtil.findViewListener(view, this, id.left_imgv, id.right_imgv, id.title);

		View headerView = getActivity().getLayoutInflater().inflate(layout.adapter_trial_header, mListView, false);
		mListView.setPinnedHeaderView(getActivity().getLayoutInflater().inflate(R.layout.adapter_section, mListView, false));

		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				IntentUtil.startSisterGroupDetail(getActivity(), mAdapter.getItem(position - 1));
				MobclickAgent.onEvent(getActivity(), "1115");
			}

		});

		mListView.setOnPaginationListener(new PaginationListener() {

			@Override
			public void onRequestNextPage(int page) {
				mSisterGroupModule.postOperationPull(getActivity(), getCategoryType(), page + 1, SisterGroupFragment.this);
			}

			@Override
			public void onRefresh() {
				mSisterGroupModule.postOperationPull(getActivity(), getCategoryType(), 0, SisterGroupFragment.this);
			}
		});

		mSisterGroupModule.postOperationPull(getActivity(), SisterGroupModule.POST_TYPE_ALL, 0, SisterGroupFragment.this);

		mListView.setEmptyRes(drawable.error_image);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		
		if (UserModule.isLogin(getActivity())) {
			mSisterGroupModule.messageOperationStateGet(getActivity());
		}

		// 判断当前用户，如果不是之前的用户，那么重新数据
		if (!mCurrentUserToken.equals(UserModule.getUserToken(getActivity())) && mFilterView != null) {
			mFilterView.getChildAt(0).performClick();
			// filterPostList(getView().findViewById(id.post_filter_item_quanbu));
		}
		mCurrentUserToken = UserModule.getUserToken(getActivity());
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (getActivity() == null)
			return;

		//圈子信息列表
		if (url.equals(ConfigInfo.API_POST_OPERATION_LIST_ALL) || url.equals(ConfigInfo.API_POST_OPERATION_LIST_OTHER)) {

			SisterGroupPostListResponse response = (SisterGroupPostListResponse) object;
			int page = (Integer) response.getTag();

			if (mAdapter == null || page == 0) {
				mAdapter = new SisterGroupListViewAdapter(getActivity(), response.posts);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.loadNextPage(response.posts);
			}

			mListView.delegatePageCheck(response.paged);

			mListView.onRefreshComplete();
		} else if (url.equals(ConfigInfo.API_MESSAGE_READ_STATE)) {//个人中心消息
			SystemMessageResponse response = (SystemMessageResponse) object;

			if (response != null && response.is_read != null && response.is_read.equals("1")) {
				mLeftImageButton.setImageResource(drawable.icon_message_tip);
			} else {
				mLeftImageButton.setImageResource(drawable.icon_message);
			}
		}
	}

	@Override
	public void onHttpRequestSuccess(String url, HttpContext httpContext) {
		super.onHttpRequestSuccess(url, httpContext);
		if (getActivity() == null)
			return;

		if (url.equals(ConfigInfo.API_POST_OPERATION_LIST_ALL) || url.equals(ConfigInfo.API_POST_OPERATION_LIST_OTHER)) {

			SisterGroupPostListResponse response = (SisterGroupPostListResponse) httpContext.responseVo;
			int page = (Integer) response.getTag();

			if (mAdapter == null || page == 0) {
				mAdapter = new SisterGroupListViewAdapter(getActivity(), response.posts);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.loadNextPage(response.posts);
			}

			mListView.delegatePageCheck(response.paged);

		} else if (url.equals(ConfigInfo.API_MESSAGE_READ_STATE)) {
			SystemMessageResponse response = (SystemMessageResponse) httpContext.responseVo;

			if (response != null && response.is_read != null && response.is_read.equals("1")) {
				mLeftImageButton.setImageResource(drawable.icon_message_tip);
			} else {
				mLeftImageButton.setImageResource(drawable.icon_message);
			}
		}
	}

	@Override
	public void onHttpRequestComplete(boolean success, String url, HttpContext httpContext) {
		super.onHttpRequestComplete(success, url, httpContext);
		if (url.equals(ConfigInfo.API_POST_OPERATION_LIST_ALL) || url.equals(ConfigInfo.API_POST_OPERATION_LIST_OTHER)) {
			mListView.onRefreshComplete();
		}
	}

	/***
	 * 无网络图片点击事件
	 */
	@Override
	public void onHttpRequestFailed(String url, HttpContext httpContext) {
		super.onHttpRequestFailed(url, httpContext);

		mListView.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSisterGroupModule.postOperationPull(getActivity(), getCategoryType(), mAdapter == null ? 0 : mAdapter.page + 1, SisterGroupFragment.this);
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_SEND_SUCCESS) {
			MobclickAgent.onEvent(getActivity(), "1209");
			mSisterGroupModule.postOperationPull(getActivity(), getCategoryType(), 0, SisterGroupFragment.this);
		}
	}

	/**
	 * 显示筛选View
	 * 
	 * @param v
	 * @author sargent
	 * @date 2014年9月11日
	 */
	@SuppressWarnings("deprecation")
	private void showCategoryMenu(View v) {
		/*
		 * PopWindowMenu menu = new
		 * PopWindowMenu(getActivity().getApplicationContext(), getFilterView(),
		 * v); menu.show();
		 */

		if (mWindow == null) {
			if (mFilterView == null && getActivity() != null) {
				// 生成筛选视图
				mFilterView = (LinearLayout) View.inflate(getActivity().getApplicationContext(), R.layout.adapter_post_filter, null);
				for (int i = 0; i < mFilterView.getChildCount(); i++) {
					mFilterView.getChildAt(i).setOnClickListener(SisterGroupFragment.this);
					if (i == 0)
						mFilterView.getChildAt(0).setSelected(true);
				}
			}
			mWindow = new PopupWindow(mFilterView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mWindow.setOutsideTouchable(true);
			mWindow.setFocusable(true);
			mWindow.setBackgroundDrawable(new BitmapDrawable());
			mWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					mTitleTextView.setSelected(false);
				}
			});
		}
		if (mWindow.isShowing()) {
			mWindow.dismiss();
		} else {
			v.setSelected(true);
			int windowWidth = mWindow.getContentView().getMeasuredWidth();
			int viewWidth = v.getMeasuredWidth();
			int offsetX = windowWidth / 2 - viewWidth / 2;
			if (offsetX > 0)
				offsetX = -offsetX;
			mWindow.showAsDropDown(v, offsetX, (int) getResources().getDimension(R.dimen.size_1));
		}
	}

	/**
	 * 获取当前分类Type
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 * 
	 * @return
	 */
	private String getCategoryType() {
		String type = SisterGroupModule.POST_TYPE_ALL;
		if (mFilterView == null)
			return type;
		for (int i = 0; i < mFilterView.getChildCount(); i++) {
			View child = mFilterView.getChildAt(i);
			if (child.isSelected())
				return mCategoryMap.get(((TextView) child).getText().toString());
		}
		return type;
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		if (url.equals(ConfigInfo.API_POST_OPERATION_LIST_ALL) || url.equals(ConfigInfo.API_POST_OPERATION_LIST_OTHER)) {
			mListView.onRefreshComplete();
		}

	}

	/**
	 * 响应帖子筛选的点击事件
	 * 
	 * @param v
	 * @author sargent
	 * @date 2014年9月10日
	 */
	private void filterPostList(View v) {
		mWindow.dismiss();

		// 点击的是当前选择的，不作处理
		String title = ((TextView) v).getText().toString();
		if (mTitleTextView.getText().equals(title))
			return;
		
		//点击"我发布的"选择判断用户是否登录
		if (!UserModule.isLogin(getActivity()) && !title.equals(mCategoryMap.get(getString(R.string.post_filter_item_quanbu)))) {
			IntentUtil.startLoginActivity(getActivity());
			return;
		}

		mListView.showLoadingView(true);
		
		// 设置标题
		mTitleTextView.setText(title);
		
		// 清除筛选视图中的所有子View的选中状态
		for (int i = 0; i < mFilterView.getChildCount(); i++) {
			mFilterView.getChildAt(i).setSelected(false);
		}
		v.setSelected(true);

		String categoryType = mCategoryMap.get(title);
		mSisterGroupModule.postOperationPull(getActivity(), categoryType, 0, SisterGroupFragment.this);

		// 根据下拉列表选项得到不同的空图片信息
		int messageRes = selectEmptyMessageRes();
		mListView.setEmptyRes(drawable.error_image, messageRes);
	}

	/***
	 * 根据下拉列表选项得到不同的空图片信息
	 * 
	 * @author 时培飞 Create at 2015-4-14 下午2:08:15
	 */
	public int selectEmptyMessageRes() {
		int messageRes = 0;
		if (getCategoryType().equals(SisterGroupModule.POST_TYPE_ALL)) {
			messageRes = 0;
		} else if (getCategoryType().equals(SisterGroupModule.POST_TYPE_COLLECT)) {
			messageRes = drawable.error_empty_sistergroup_collect;
		} else if (getCategoryType().equals(SisterGroupModule.POST_TYPE_PART)) {
			messageRes = drawable.error_empty_sistergroup_part;
		} else if (getCategoryType().equals(SisterGroupModule.POST_TYPE_SENDED)) {
			messageRes = drawable.error_empty_sistergroup_send;
		}
		return messageRes;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv://返回事件
			MobclickAgent.onEvent(getActivity(), "1117");
			if (UserModule.isLogin(getActivity())) {
				startActivity(new Intent(getActivity(), SisterGroupMessageActivity.class));
			} else {
				IntentUtil.startLoginActivity(getActivity());
			}
			break;
		case R.id.title://点击标题筛选
			showCategoryMenu(v);
			break;
		case R.id.right_imgv://发帖事件
			MobclickAgent.onEvent(getActivity(), "1116");
			if (UserModule.isLogin(getActivity())) {
				Intent intent = new Intent(getActivity(), SisterGroupPostEditActivity.class);
				startActivityForResult(intent, 0);
			} else {
				IntentUtil.startLoginActivity(getActivity());
			}
			break;
		case R.id.post_filter_item_quanbu://全部帖子事件
			filterPostList(v);
			break;
		case R.id.post_filter_item_fabu://我的发布的点击事件
			MobclickAgent.onEvent(getActivity(), "1206");
			filterPostList(v);
			break;
		case R.id.post_filter_item_canyu://我参与的点击事件
			MobclickAgent.onEvent(getActivity(), "1207");
			filterPostList(v);
			break;
		case R.id.post_filter_item_shoucang://我收藏的点击事件
			MobclickAgent.onEvent(getActivity(), "1208");
			filterPostList(v);
			break;
		default:
			break;
		}
	}
}
