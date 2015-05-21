package com.ipassistat.ipa.view.pulldown;

import java.util.logging.Logger;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author wangxiao1@cjsc.com.cn
 * @date 2013-7-9
 * 
 */
public class PullDownScrollView extends LinearLayout {

	private static final String TAG = "PullDownScrollView";

	private int refreshTargetTop = -200;
	private int headContentHeight;

	private RefreshListener refreshListener;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private final static int RATIO = 3;
	private int preY = 0;
	private boolean isElastic = false;
	private int startY;
	private int state;

	private String note_release_to_refresh = "松开加载";
	private String note_pull_to_refresh = "下拉刷新";
	private String note_refreshing = "正在刷新...";

	private IPullDownElastic mElastic;

	public PullDownScrollView(Context context) {
		super(context);
		init();

	}

	public PullDownScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
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
	}

	/**
	 * ˢ�¼���
	 * 
	 * @param listener
	 */
	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	/**
	 * 添加下拉刷新试图
	 * 
	 * @param elastic
	 */
	public void setPullDownElastic(IPullDownElastic elastic) {
		mElastic = elastic;

		headContentHeight = mElastic.getElasticHeight();
		refreshTargetTop = -headContentHeight;
		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, headContentHeight);
		lp.topMargin = refreshTargetTop;
		
		addView(mElastic.getElasticLayout(), 0, lp);
	}

	/**
	 * ���ø�����ʾ��
	 * 
	 * @param pullToRefresh
	 *            ����ˢ����ʾ��
	 * @param releaseToRefresh
	 *            �ɿ�ˢ����ʾ��
	 * @param refreshing
	 *            ����ˢ����ʾ��
	 */
	public void setRefreshTips(String pullToRefresh, String releaseToRefresh,
			String refreshing) {
		note_pull_to_refresh = pullToRefresh;
		note_release_to_refresh = releaseToRefresh;
		note_refreshing = refreshing;
	}

	/*
	 * 
	 * @see
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			preY = (int) ev.getY();
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {

			if (!isElastic
					&& canScroll()
					&& (int) ev.getY() - preY >= headContentHeight
							/ (3 * RATIO) && refreshListener != null
					&& mElastic != null) {

				isElastic = true;
				startY = (int) ev.getY();
				return true;
			}

		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		handleHeadElastic(event);
		return super.onTouchEvent(event);
	}

	private void handleHeadElastic(MotionEvent event) {
		if (refreshListener != null && mElastic != null) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Logger.i(TAG, "down");
				break;
			case MotionEvent.ACTION_UP:

				// Logger.i(TAG, "up");
				if (state != IPullDownElastic.REFRESHING && isElastic) {

					if (state == IPullDownElastic.DONE) {
						// ʲô������
						setMargin(refreshTargetTop);
					}
					if (state == IPullDownElastic.PULL_To_REFRESH) {
						state = IPullDownElastic.DONE;
						setMargin(refreshTargetTop);
						changeHeaderViewByState(state, false);
						// Logger.i(TAG, "������ˢ��״̬����done״̬");
					}
					if (state == IPullDownElastic.RELEASE_To_REFRESH) {
						state = IPullDownElastic.REFRESHING;
						setMargin(0);
						changeHeaderViewByState(state, false);
						onRefresh();
						// Logger.i(TAG, "���ɿ�ˢ��״̬����done״̬");
					}

				}
				isElastic = false;
				break;
			case MotionEvent.ACTION_MOVE:
			
				int tempY = (int) event.getY();

				if (state != IPullDownElastic.REFRESHING && isElastic) {
					
					if (state == IPullDownElastic.RELEASE_To_REFRESH) {
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = IPullDownElastic.PULL_To_REFRESH;
							changeHeaderViewByState(state, true);
							// Logger.i(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
						} else if (tempY - startY <= 0) {
							state = IPullDownElastic.DONE;
							changeHeaderViewByState(state, false);
							// Logger.i(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
						}
					}
					if (state == IPullDownElastic.DONE) {
						if (tempY - startY > 0) {
							state = IPullDownElastic.PULL_To_REFRESH;
							changeHeaderViewByState(state, false);
						}
					}
					if (state == IPullDownElastic.PULL_To_REFRESH) {
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = IPullDownElastic.RELEASE_To_REFRESH;
							changeHeaderViewByState(state, false);
						} else if (tempY - startY <= 0) {
							state = IPullDownElastic.DONE;
							changeHeaderViewByState(state, false);
						}
					}
					if (tempY - startY > 0) {
						setMargin((tempY - startY) / 2 + refreshTargetTop);
					}
				}
				break;
			}
		}
	}

	/**
     * 
     */
	private void setMargin(int top) {
		LinearLayout.LayoutParams lp = (LayoutParams) mElastic
				.getElasticLayout().getLayoutParams();
		lp.topMargin = top;
		mElastic.getElasticLayout().setLayoutParams(lp);
		mElastic.getElasticLayout().invalidate();
	}

	private void changeHeaderViewByState(int state, boolean isBack) {

		mElastic.changeElasticState(state, isBack);
		switch (state) {
		case IPullDownElastic.RELEASE_To_REFRESH:
			mElastic.showArrow(View.VISIBLE);
			mElastic.showProgressBar(View.GONE);
			mElastic.showLastUpdate(View.VISIBLE);
			mElastic.setTips(note_release_to_refresh);

			mElastic.clearAnimation();
			mElastic.startAnimation(animation);
			break;
		case IPullDownElastic.PULL_To_REFRESH:
			mElastic.showArrow(View.VISIBLE);
			mElastic.showProgressBar(View.GONE);
			mElastic.showLastUpdate(View.VISIBLE);
			mElastic.setTips(note_pull_to_refresh);
			mElastic.clearAnimation();

			if (isBack) {
				mElastic.startAnimation(reverseAnimation);
			}
			break;
		case IPullDownElastic.REFRESHING:
			mElastic.showArrow(View.GONE);
			mElastic.showProgressBar(View.VISIBLE);
			mElastic.showLastUpdate(View.GONE);
			mElastic.setTips(note_refreshing);

			mElastic.clearAnimation();
			break;
		case IPullDownElastic.DONE:
			mElastic.showProgressBar(View.GONE);
			mElastic.clearAnimation();
			break;
		}
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
		}
	}

	/**
     * 
     */
	@Override
	public void computeScroll() {
		// if (scroller.computeScrollOffset()) {
		// int i = this.scroller.getCurrY();
		// LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
		// this.refreshView
		// .getLayoutParams();
		// int k = Math.max(i, refreshTargetTop);
		// lp.topMargin = k;
		// this.refreshView.setLayoutParams(lp);
		// this.refreshView.invalidate();
		// invalidate();
		// }
	}

	/**
	 * 
	 * @param text
	 */
	public void finishRefresh(String text) {
		if (mElastic == null) {
			
			return;
		}
		if (state == IPullDownElastic.DONE) {
			
		}
		state = IPullDownElastic.DONE;
		if (text != null) {
			mElastic.setLastUpdateText(text);
		}
		changeHeaderViewByState(state, false);
		

		mElastic.showArrow(View.VISIBLE);
		mElastic.showLastUpdate(View.VISIBLE);
		setMargin(refreshTargetTop);
		
	}

	/**
	 * 
	 * @param text
	 */
	public void finishRefresh() {
		if (mElastic == null) {
			
			return;
		}
		if (state == IPullDownElastic.DONE) {
			
		}
		state = IPullDownElastic.DONE;
		changeHeaderViewByState(state, false);
	

		mElastic.showArrow(View.VISIBLE);
		mElastic.showLastUpdate(View.VISIBLE);
		setMargin(refreshTargetTop);
	
	}

	private boolean canScroll() {
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof AbsListView) {
				int top = ((AbsListView) childView).getChildAt(0).getTop();
				int pad = ((AbsListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((AbsListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return canScroll(this);
	}

	/**
	 * ������д�˷������Լ���������ӿؼ���Ŀǰֻ����AbsListView��ScrollView
	 * 
	 * @param view
	 * @return
	 */
	public boolean canScroll(PullDownScrollView view) {
		return false;
	}

	

	/**
	 * ˢ�¼���ӿ�
	 */
	public interface RefreshListener {
		public void onRefresh(PullDownScrollView view);
	}

}
