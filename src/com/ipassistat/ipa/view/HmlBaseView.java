package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.util.ViewUtil;

/**
 * 
 * 基础布局，可以用来显示空布局，或者其他布局
 * 
 * @Package com.ichsy.mboss.view
 * 
 * @File HmlBaseView.java
 * 
 * @author LiuYuHang Date: 2015年2月5日
 *
 *         Modifier： Modified Date： Modify：
 * 
 *         Copyright @ 2015 Corpration CHSY
 * 
 */
public class HmlBaseView extends LinearLayout {
	public static final int STATE_LOADING = 1;// 数据还在加载中
	public static final int STATE_NONET = STATE_LOADING + 1;// 数据加载错误（网络异常）
	public static final int STATE_EMPTY = STATE_NONET + 1;// 数据为空
	public static final int STATE_COMPLATE = STATE_EMPTY + 1;// 正常显示数据

	private ViewGroup mContentRootView;

	private View mMessageView;
	private TextView mMessageTextView;
	private ImageView mLoadingImageView;

	private int[] mCustomErrorImage;

	private OnClickListener mNoDataListener;// 没有数据点击的监听器

	public HmlBaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HmlBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HmlBaseView(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化方法
	 * 
	 * Modifier： Modified Date： Modify：
	 */
	private void init() {
		View rootView = inflate(getContext(), com.ipassistat.ipa.R.layout.activity_base_layout, null);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		addView(rootView);

		mMessageView = rootView.findViewById(id.view_error);
		mMessageTextView = (TextView) rootView.findViewById(id.textview_error_content);
		mLoadingImageView = (ImageView) rootView.findViewById(id.imageview_loading);
	}

	/**
	 * 设置内容主布局
	 * 
	 * 
	 * Modifier： Modified Date： Modify：
	 */
	public void setContentView(View contentView) {
		mContentRootView = (ViewGroup) findViewById(id.view_content);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mContentRootView.addView(contentView,params);
		showLoadingView(true);
	}

	/**
	 * 如果没有加载导数据显示的布局
	 *
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public void setEmptyRes(int... resIds) {
		mCustomErrorImage = new int[resIds.length];
		for (int i = 0; i < resIds.length; i++) {
			if (i >= mCustomErrorImage.length) return;
			mCustomErrorImage[i] = resIds[i];
		}
	}

	public void setEmptyViewLisenter(final OnClickListener listener) {
		if (listener != null) {
			mNoDataListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					showLoadingView(true);
					listener.onClick(v);
				}
			};
		}
	}

	public View findTouchView() {
		return findViewById(id.view_touch);
	}

	/**
	 * 设置错误信息
	 *
	 * @param message
	 *
	 * @author LiuYuHang
	 * @date 2014年10月10日
	 */
	public void setMessageText(CharSequence message) {
		if (!TextUtils.isEmpty(message)) mMessageTextView.setText(message);
		dismissMessageView(false);
	}

	/**
	 * 展示或者隐藏主View
	 * 
	 * @param show
	 * 
	 *            Modifier： Modified Date： Modify：
	 */
	public void showContentView(boolean show) {
		ViewUtil.setViewVisiblityWithAnimation(show ? View.VISIBLE : View.GONE, true, mContentRootView);
	}

	/**
	 * 隐藏信息页面（自定义页面，无网页面，错误页面）
	 *
	 * @param dismiss
	 *
	 * @author LiuYuHang
	 * @date 2014年10月10日
	 */
	public void dismissMessageView(boolean dismiss) {
		mMessageView.setVisibility(dismiss ? View.GONE : View.VISIBLE);
	}

	/**
	 * 首次加载，显示loading进度
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月26日
	 *
	 */
	public void onFirstLoading() {
		View defaultView = findViewById(id.view_group);
		View customView = findViewById(id.view_custom);
		defaultView.setVisibility(View.VISIBLE);
		customView.setVisibility(View.GONE);

		dismissMessageView(false);
		// mProgressBar.setVisibility(View.VISIBLE);
		// mMessageTextView.setText(getContext().getString(string.global_message_loading));
		mMessageTextView.setVisibility(View.GONE);
		showLoadingView(true);

		ViewUtil.setViewVisiblityWithAnimation(View.GONE, true, mContentRootView);
	}

	public void showEmptyView() {
		ViewUtil.setViewVisiblityWithAnimation(View.GONE, true, mLoadingImageView, mContentRootView);
		if (mCustomErrorImage != null && mCustomErrorImage.length > 0) {
			// 有自定义页面
			showCustomView(true, mNoDataListener, mCustomErrorImage);
		} else {
			setMessageText(getContext().getString(string.global_message_no_data));
			showCustomView(false, mNoDataListener);
		}
		dismissMessageView(false);
	}

	/**
	 * 显示loadingUI
	 *
	 * @param show
	 * @author LiuYuHang
	 * @date 2014年10月22日
	 */
	public void showLoadingView(boolean show) {
		dismissMessageView(!show);
		showCustomView(false, null);
		if (show) {
			ViewUtil.swapViewWithAnimation(mContentRootView, mLoadingImageView, true);
			AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImageView.getDrawable();
			animationDrawable.start();
		} else {
			ViewUtil.swapViewWithAnimation(mLoadingImageView, mContentRootView, true);
		}
	}

	/**
	 * 显示自定义的错误View
	 *
	 * @author LiuYuHang
	 * @date 2014年10月15日
	 */
	public void showCustomView(boolean show, OnClickListener listener, int... resIds) {
		View defaultView = findViewById(id.view_group);
		View customView = findViewById(id.view_custom);

		if (show) {
			ViewUtil.setViewVisiblityWithAnimation(View.GONE, false, mMessageTextView, mLoadingImageView);
			ViewUtil.swapViewWithAnimation(defaultView, customView, true);
			ImageView[] imageviews = new ImageView[2];
			imageviews = new ImageView[2];
			imageviews[0] = (ImageView) findViewById(id.imageview_customTop);
			imageviews[1] = (ImageView) findViewById(id.imageview_customBottom);

			for (int i = 0; i < imageviews.length; i++) {
				imageviews[i].setImageBitmap(null);
				if (i >= resIds.length) break;
				if (resIds[i] == 0) {
					imageviews[i].setImageBitmap(null);
				} else {
					imageviews[i].setImageResource(resIds[i]);
					imageviews[i].setOnClickListener(listener);
				}
			}
		} else {
			ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, mMessageTextView, mLoadingImageView);
			ViewUtil.setViewVisiblityWithAnimation(View.GONE, true, mLoadingImageView);
			ViewUtil.swapViewWithAnimation(customView, defaultView, false);
		}
	}

	/**
	 * 设置当前为无网UI
	 *
	 * @param listener
	 *            UI的点击事件（一般为重试事件）
	 * @author LiuYuHang
	 * @date 2014年10月21日
	 */
	public void noNet(final OnClickListener listener) {
		showCustomView(true, listener == null ? null : new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoadingView(true);
				listener.onClick(v);
			}

		}, drawable.error_no_net_top, drawable.error_no_net);
	}

}
