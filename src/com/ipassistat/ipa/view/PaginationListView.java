package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.adapter.PaginationAdapter;
import com.ipassistat.ipa.adapter.PaginationAdapter.DataDelegate;
import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.view.PullListView.OnRefreshListener;
import com.ipassistat.ipa.view.paginationListView.SectionBaseAdapter;
import com.ipassistat.ipa.view.paginationListView.SectionPinnedInterface;

/**
 * 自定义的可翻页ListView<br>
 * 
 * 
 * @author shipeifei
 * @date 2014年10月22日
 */
public class PaginationListView extends LinearLayout implements OnRefreshListener, DataDelegate {
	private final static long DEFAUL_LOADING_DELAY_TIME = 200;// 用户上拉或者下拉之后，会有一小段延迟之后才开始真正请求数据

	private Handler mHandler;
	private PaginationAdapter<?> mAdapter;

	private View mNomorePageView;

	private boolean settingPullEnable = true;// 记录之前定义的是否可以翻页
	private boolean settingBottomEnable = true;

	private View contentView;
	private HmlBaseView mBaseView;
	private PullListView mPullListView;

	private View mHeaderView;
	private int mHeaderViewHeight;
	private boolean mHeaderViewVisible;
	private int mHeaderViewWidth;

	public interface PaginationListener {
		/**
		 * 上拉刷新
		 * 
		 * @author LiuYuHang
		 * @date 2014年9月25日
		 *
		 */
		void onRefresh();

		/**
		 * 下拉加载更多
		 * 
		 * @param page
		 *            当前页数
		 * @author ShiPeiFei
		 * @date 2014年9月25日
		 *
		 */
		void onRequestNextPage(int page);
	}

	private PaginationListener paginationListener;

	public PaginationListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PaginationListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PaginationListView(Context context) {
		super(context);
		init();
	}

	public void setOnScrollListener(OnScrollListener l) {
		mPullListView.setOnScrollListener(l);
	}

	/**
	 * 初始化PaginationListView，添加实际的pullListView和errorView
	 * 
	 * @author ShiPeiFei
	 * @date 2014年9月26日
	 *
	 */
	@SuppressWarnings("deprecation")
	private void init() {
		mHandler = new Handler();
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setOrientation(LinearLayout.VERTICAL);

		contentView = View.inflate(getContext(), R.layout.listview_custom, null);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mPullListView = (PullListView) View.inflate(getContext(), layout.activity_listview_layout, null);
		mPullListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mPullListView.setHeaderDividersEnabled(true);
		mPullListView.setDividerHeight(1);
		mPullListView.setSelector(new BitmapDrawable());

		mBaseView = (HmlBaseView) contentView.findViewById(id.view_baseview);
		mBaseView.setContentView(mPullListView);
		// mPullListView.setOnScrollListener(this);
		mBaseView.onFirstLoading();

		addView(contentView);
	}

	public void addHeadView(View view) {
		mPullListView.addHeaderView(view);
	}

	public void removeHeaderView(View v) {
		mPullListView.removeHeaderView(v);
	}

	public View findTouchView() {
		return mBaseView.findTouchView();
	}

	/**
	 * 如果没有加载导数据显示的布局
	 *
	 * @author ShiPeiFei
	 * @date 2014年10月14日
	 */
	public void setEmptyRes(int... resIds) {
		mBaseView.setEmptyRes(resIds);
	}

	public void setEmptyViewLisenter(final OnClickListener listener) {
		mBaseView.setEmptyViewLisenter(listener);
	}

	@Override
	public void onRefresh(final boolean isTop) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isTop) {
					getPaginationListener().onRefresh();
				} else {
					getPaginationListener().onRequestNextPage(getAdapter().page);
				}
			}
		}, DEFAUL_LOADING_DELAY_TIME);

	}

	public void setOnPaginationListener(PaginationListener paginationListener) {
		this.paginationListener = paginationListener;
		this.mPullListView.setonRefreshListener(this);
	}

	public PaginationListener getPaginationListener() {
		return paginationListener;
	}

	public void onRefreshComplete() {
		mPullListView.onRefreshComplete();
	}

	public void setDividerHeight(int dividerHeight) {
		mPullListView.setDividerHeight(dividerHeight);
	}

	public void setAdapter(PaginationAdapter<?> adapter) {
		mAdapter = adapter;
		mPullListView.setAdapter(adapter);
		adapter.dataDelegate = this;

		mBaseView.dismissMessageView(true);

		if (adapter.dataDelegate != null && adapter != null) {
			adapter.dataDelegate.onDataChanaged(adapter.getCount());
		}

		adapter.dataDelegate.onDataReset();
	}

	public PaginationAdapter<?> getAdapter() {
		return mAdapter;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mPullListView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mPullListView.setOnItemLongClickListener(listener);
	}

	/**
	 * 设置是否可以上拉，下拉刷新
	 *
	 * @param top
	 * @param bottom
	 * @author ShiPeiFei
	 * @date 2014年10月22日
	 */
	public void setPullAvailable(boolean top, boolean bottom) {
		mPullListView.setPullAvailable(top, bottom);
		settingPullEnable = mPullListView.mPullTopEnable;
		settingBottomEnable = mPullListView.mPullBottomEnable;
	}

	/**
	 * 显示loadingUI
	 *
	 * @param show
	 * @author ShiPeiFei
	 * @date 2014年10月22日
	 */
	public void showLoadingView(boolean show) {
		mBaseView.showLoadingView(show);
	}

	@Override
	public void onDataChanaged(int count) {
		if (count > 0) {
			mBaseView.showContentView(true);
			mBaseView.dismissMessageView(true);
			mBaseView.showCustomView(false, null);
		} else {
			mBaseView.showEmptyView();
		}
	}

	/**
	 * 设置当前为无网UI
	 *
	 * @param listener
	 *            UI的点击事件（一般为重试事件）
	 * @author ShiPeiFei
	 * @date 2014年10月21日
	 */
	public void noNet(final OnClickListener listener) {
		if (mAdapter == null || mAdapter.page == 0) {
			mBaseView.noNet(listener);
		}
	}

	/**
	 * 设置当前listView没有更多数据
	 *
	 * @param content
	 *            提示文字
	 * @author ShiPeiFei
	 * @date 2014年10月27日
	 */
	public void noMorePage(String content) {
		if (mNomorePageView == null) {
			mNomorePageView = View.inflate(getContext(), layout.listview_bottom_nomore, null);
			updateNoMorePageText(content);
			mPullListView.addFooterView(mNomorePageView);
		}
		mPullListView.setPullAvailable(settingPullEnable, false);
		mPullListView.noMorePage();
	}

	/**
	 * 设置当前listView没有更多数据
	 *
	 * @param view
	 *            自定义的view
	 * @author ShiPeiFei
	 * @date 2014年10月24日
	 */
	public void noMorePage(View view) {
		if (mNomorePageView == null) {
			mNomorePageView = view;
			mPullListView.addFooterView(mNomorePageView);
		}
		mPullListView.setPullAvailable(settingPullEnable, false);
		mPullListView.noMorePage();
	}

	/**
	 * 设置当前listView没有更多数据
	 *
	 * @author ShiPeiFei
	 * @date 2014年10月24日
	 */
	public void noMorePage() {
		noMorePage("");
	}

	/**
	 * 更新底部已加载全部 的文字内容
	 *
	 * @param content
	 * @author LiuYuHang
	 * @date 2014年10月27日
	 */
	public void updateNoMorePageText(String content) {
		if (TextUtils.isEmpty(content) || mNomorePageView == null) return;

		TextView contentView = (TextView) mNomorePageView.findViewById(id.textview_content);
		contentView.setText(content);
	}

	/**
	 * 去掉底部 已加载全部的View，并且列表现在可以上拉加载更多
	 *
	 * @author ShiPeiFei
	 * @date 2014年10月27日
	 */
	public void mayHaveNextPage() {
		if (mNomorePageView != null) {
			mPullListView.removeFooterView(mNomorePageView);
			mNomorePageView = null;
		}
		mPullListView.setPullAvailable(settingPullEnable, settingBottomEnable);
		mPullListView.mayHaveNextPage();
	}

	/**
	 * 让listview判断是否有下一页
	 *
	 * @param pageResults
	 * @author ShiPeiFei
	 * @date 2014年10月24日
	 */
	public void delegatePageCheck(PageResults pageResults) {
		if (pageResults == null) return;

		if (pageResults.getMore() == 0) {
			noMorePage();
		} else {
			mayHaveNextPage();
		}
	}

	@Override
	public void onNetError(CharSequence message) {
		
	}

	@Override
	public void onDataReset() {
		if (settingBottomEnable) mayHaveNextPage();
	}

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;

		// Disable vertical fading when the pinned header is present
		// TODO change ListView to allow separate measures for top and bottom
		// fading edge;
		// in this particular case we would like to disable the top, but not the
		// bottom edge.
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(mPullListView.getFirstVisiblePosition());
		}
	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null || !(mAdapter instanceof SectionPinnedInterface)) { return; }

		int state = ((SectionPinnedInterface) mAdapter).getPinnedHeaderState(position);
		switch (state) {
		case SectionBaseAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case SectionBaseAdapter.PINNED_HEADER_VISIBLE: {
			((SectionPinnedInterface) mAdapter).configurePinnedHeader(mHeaderView, position, 255);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mHeaderViewVisible = true;
			break;
		}

		case SectionBaseAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			if (firstView != null) {
				int bottom = firstView.getBottom();
				int headerHeight = mHeaderView.getHeight();
				int y;
				int alpha;
				if (bottom < headerHeight) {
					y = (bottom - headerHeight);
					alpha = 255 * (headerHeight + y) / headerHeight;
				} else {
					y = 0;
					alpha = 255;
				}
				((SectionPinnedInterface) mAdapter).configurePinnedHeader(mHeaderView, position, alpha);
				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
				}
				mHeaderViewVisible = true;
			}
			break;
		}
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	

}
