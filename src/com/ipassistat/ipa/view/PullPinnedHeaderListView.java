package com.ipassistat.ipa.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.util.DateUtil;

/**
 * 粘性的，实现了下拉刷新，上拉加载的listView
 * 
 * @author maoyn
 */
public class PullPinnedHeaderListView extends PinnedHeaderListView implements
		OnScrollListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;

	private final static int PULL_To_REFRESH = 1;

	private final static int REFRESHING = 2;

	private final static int DONE = 3;

	private final static int LOADING = 4;

	private boolean isTop = true;

	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private LinearLayout footView;

	private TextView tipsTextview;

	private TextView lastUpdatedTextView;

	private ImageView arrowImageView;

	private ProgressBar progressBar;

	private TextView foottipsTextview;

//	private TextView footlastUpdatedTextView;

	private ImageView footarrowImageView;

	private ProgressBar footprogressBar;

	private RotateAnimation animation;

	private RotateAnimation reverseAnimation;

	private RotateAnimation footanimation;

	private RotateAnimation footreverseAnimation;

	private boolean isRecored;

	private boolean isBottomRecored;

	private int headContentWidth;

	private int headContentHeight;

	private int startY;

	private int startBottomY;

	private int firstItemIndex;

	private int state = DONE;

	private int bottomstate = DONE;

	private boolean isBack;

	private boolean isBottomBack;

	private OnPinedRefreshListener refreshListener;

	/**
     * 
     */
	private boolean isRefreshable;

	private boolean isBackRefreshable;

	private int lastItem;

	private int totalItemCount;

	private boolean isFootVisible = true;

	private boolean isHeaderVisible = true;

	private int upImageResources = R.drawable.z_arrow_down;
	private int bottomImageResources = R.drawable.z_arrow_down;
	private int textSize = 16;
	private int textColor = android.R.color.black;

	private View mNomorePageView;

	private boolean hasNextPage; // 是不是还有下一页

	public boolean mPullTopEnable = true, mPullBottomEnable = true;// 上拉下拉刷新是否可用

	public PullPinnedHeaderListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param isFootVisible
	 * @param isHeaderVisible
	 */

	public PullPinnedHeaderListView(Context context, boolean isFootVisible,
			boolean isHeaderVisible) {
		super(context);
		this.isFootVisible = isFootVisible;
		this.isHeaderVisible = isHeaderVisible;
		init(context);
	}

	public PullPinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.ToolListFootVisible);
		// isFootVisible =
		// a.getBoolean(R.styleable.ToolListFootVisible_foot_is_visible, true);
		// isHeaderVisible =
		// a.getBoolean(R.styleable.ToolListFootVisible_head_is_visible, true);
		// upImageResources=
		// a.getResourceId(R.styleable.ToolListFootVisible_up_image_resource,R.drawable.arrow_01);
		// bottomImageResources=
		// a.getResourceId(R.styleable.ToolListFootVisible_buttom_image_resource,
		// R.drawable.arrow_02);
		// textSize= a.getResourceId(R.styleable.ToolListFootVisible_textSize,
		// 16);
		// textColor=
		// a.getResourceId(R.styleable.ToolListFootVisible_textColor,android.R.color.black);
		// a.recycle();

		init(context);
	}

	private void init(Context context) {
		this.setDividerHeight(1);
		this.setVerticalScrollBarEnabled(false);
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.listview_header,
				null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		if (this.isHeaderVisible) {
			addHeaderView(headView, null, false);
		}
		footView = (LinearLayout) inflater.inflate(R.layout.listview_footer,
				null);
		footarrowImageView = (ImageView) footView
				.findViewById(R.id.foot_arrowImageView);
		footarrowImageView.setMinimumWidth(70);
		footarrowImageView.setMinimumHeight(50);
		footprogressBar = (ProgressBar) footView
				.findViewById(R.id.foot_progressBar);
		foottipsTextview = (TextView) footView
				.findViewById(R.id.foot_tipsTextView);
//		footlastUpdatedTextView = (TextView) footView
//				.findViewById(R.id.foot_lastUpdatedTextView);

		footView.setPadding(0, 0, 0, -1 * headContentHeight);

		footView.invalidate();
		if (this.isFootVisible) {
			addFooterView(footView, null, false);
		}
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		footanimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		footanimation.setInterpolator(new LinearInterpolator());
		footanimation.setDuration(250);
		footanimation.setFillAfter(true);

		footreverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		footreverseAnimation.setInterpolator(new LinearInterpolator());
		footreverseAnimation.setDuration(200);
		footreverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		bottomstate = DONE;
		isBackRefreshable = false;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) { // �����lastitem��ֵ
		lastItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
		firstItemIndex = firstVisibleItem;
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					// //Log.v(TAG, "在down时候记录当前位置‘");
				}

				if (lastItem == totalItemCount && !isBottomRecored) {
					isBottomRecored = true;
					startBottomY = (int) event.getY();
					// //Log.v(TAG, "在bottomdown时候记录当前位置‘");
				}

				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();

						// //Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();

						isTop = true;

						onRefresh(isTop);

						// //Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}

				isRecored = false;
				isBack = false;

				// //Log.v(TAG, "bottomstate== PULL_To_REFRESH" + bottomstate +
				// "=="
				// + PULL_To_REFRESH);
				if (bottomstate != REFRESHING && bottomstate != LOADING) {
					if (bottomstate == DONE) {

					}
					if (bottomstate == PULL_To_REFRESH) {
						bottomstate = DONE;
						changeFootViewByState();

						// //Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (bottomstate == RELEASE_To_REFRESH) {
						bottomstate = REFRESHING;
						changeFootViewByState();

						isTop = false;
						onRefresh(isTop);

						// //Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}

				isBottomRecored = false;
				isBottomBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					// //Log.v(TAG, "在move时候记录下位置");
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING
						&& mPullTopEnable) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

							// //Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

							// //Log.v(TAG, "由松开刷新状态转变到done状态");
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

							// //Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

							// //Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}

					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				// Log.v(TAG, "isBottomRecored=" + bottomstate + "" + lastItem
				// + "==" + totalItemCount);

				if (!isBottomRecored && lastItem == totalItemCount) {
					// Log.v(TAG, "在bottommove时候记录下位置");
					isBottomRecored = true;
					startBottomY = tempY;
				}

				if (bottomstate != REFRESHING && isBottomRecored
						&& bottomstate != LOADING && mPullBottomEnable) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (bottomstate == RELEASE_To_REFRESH) {

						setSelection(totalItemCount - 1);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((startBottomY - tempY) / RATIO < headContentHeight)
								&& (startBottomY - tempY) > 0) {
							bottomstate = PULL_To_REFRESH;
							changeFootViewByState();

							// Log.v(TAG, "由松开刷新状态转变到上拉刷新状态");
						}
						// 一下子推到顶了
						else if (startBottomY - tempY <= 0) {
							bottomstate = DONE;
							changeFootViewByState();

							// Log.v(TAG, "由松开刷新状态转变到done状态");
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (bottomstate == PULL_To_REFRESH) {

						setSelection(totalItemCount - 1);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((startBottomY - tempY) / RATIO >= headContentHeight) {
							bottomstate = RELEASE_To_REFRESH;
							isBottomBack = true;
							changeFootViewByState();

							// Log.v(TAG, "由done或者上拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (startBottomY - tempY < 0) {
							bottomstate = DONE;
							changeFootViewByState();

							// Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}

					// done状态下
					if (bottomstate == DONE) {
						if (startBottomY - tempY > 0) {
							bottomstate = PULL_To_REFRESH;
							changeFootViewByState();
						}
					}

					// 更新headView的size
					if (bottomstate == PULL_To_REFRESH) {
						if ((startBottomY - tempY) <= 0) {
							footView.setPadding(0, 0, 0, headContentHeight + 1
									* (startBottomY - tempY) / RATIO);
						}
					}

					// 更新headView的paddingTop
					if (bottomstate == RELEASE_To_REFRESH) {
						footView.setPadding(0, 0, 0, (startBottomY - tempY)
								/ RATIO - headContentHeight);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			// Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			// Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(upImageResources);
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态，done");
			break;
		}
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeFootViewByState() {
		switch (bottomstate) {
		case RELEASE_To_REFRESH:
			footarrowImageView.setVisibility(View.VISIBLE);
			footprogressBar.setVisibility(View.GONE);
			foottipsTextview.setVisibility(View.VISIBLE);
//			footlastUpdatedTextView.setVisibility(View.VISIBLE);

			footarrowImageView.clearAnimation();
			footarrowImageView.startAnimation(footanimation);

			foottipsTextview.setText("松开加载");

			// Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			footprogressBar.setVisibility(View.GONE);
			foottipsTextview.setVisibility(View.VISIBLE);
			footarrowImageView.setVisibility(View.INVISIBLE);
//			footlastUpdatedTextView.setVisibility(View.VISIBLE);
			footarrowImageView.clearAnimation();
			footarrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBottomBack) {
				isBottomBack = false;
				footarrowImageView.clearAnimation();
				footarrowImageView.startAnimation(footreverseAnimation);

				foottipsTextview.setText("上拉加载更多");
			} else {
				foottipsTextview.setText("上拉加载更多");
			}
			// Log.v(TAG, "当前状态，上拉刷新");
			break;

		case REFRESHING:

			footView.setPadding(0, 0, 0, 0);

			footprogressBar.setVisibility(View.VISIBLE);
			footarrowImageView.clearAnimation();
			footarrowImageView.setVisibility(View.GONE);
			foottipsTextview.setText("正在加载...");
//			footlastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			// footView.setPadding(0, 0, 0, -1 * headContentHeight);
			if (mPullBottomEnable && isRefreshable) {
				footView.setPadding(0, 0, 0, 0);
			} else {
				footView.setPadding(0, 0, 0, -1 * headContentHeight);
			}
			footarrowImageView.setVisibility(View.INVISIBLE);
			foottipsTextview.setVisibility(View.VISIBLE);
			footprogressBar.setVisibility(View.GONE);
			footarrowImageView.clearAnimation();
			footarrowImageView.setImageResource(bottomImageResources);
			foottipsTextview.setText("上拉加载更多");
//			footlastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态，done");
			break;
		}
	}

	public void setonRefreshListener(OnPinedRefreshListener onRefreshListener) {
		this.refreshListener = onRefreshListener;
		isRefreshable = true;
	}

	public interface OnPinedRefreshListener {
		public void onRefresh(boolean isTop);
	}

	public void onRefreshComplete() { // ----------------------------------------------//����ݼ������ʱ�����ø÷���������������ʾ��ˢ��������ʧ��
		// LogUtil.out//LogDetail("----------->>>>>>>>>>>>>>>> onRefreshComplete");
		;
		state = DONE;
		bottomstate = DONE;
		lastUpdatedTextView.setText("最近更新" + DateUtil.getUpdateTime());
//		footlastUpdatedTextView.setText("最近更新" + DateUtil.getUpdateTime());
		changeHeaderViewByState();
		changeFootViewByState();
	}

	private void onRefresh(boolean isTop) {
		state = LOADING;
		bottomstate = LOADING;
		if (refreshListener != null) {
			refreshListener.onRefresh(isTop);
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText("最近更新" + DateUtil.getUpdateTime());
		super.setAdapter(adapter);
	}

	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}

	/**
	 * 让listview判断是否有下一页
	 * 
	 * @param pageResults
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public void delegatePageCheck(PageResults pageResults) {
		if (pageResults == null)
			return;

		if (pageResults.getMore() == 0) {
			noMorePage();
		} else {
			mayHaveNextPage();
		}
	}

	/**
	 * 设置当前listView没有更多数据
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public void noMorePage() {
		hasNextPage = false;

		if (mNomorePageView == null) {
			mNomorePageView = View.inflate(getContext(),
					layout.listview_bottom_nomore, null);
			addFooterView(mNomorePageView); // 添加已全部加载的试图控件
			setPullAvailable(mPullTopEnable, false);
		}
	}

	public void mayHaveNextPage() {
		hasNextPage = true;

		if (mNomorePageView != null) {
			removeFooterView(mNomorePageView);
			mNomorePageView = null;
		}
		setPullAvailable(mPullTopEnable, mPullBottomEnable);
	}

	public void setPullAvailable(boolean topEnable, boolean bottomEnable) {
		mPullTopEnable = topEnable;
		mPullBottomEnable = bottomEnable;
	}
}
