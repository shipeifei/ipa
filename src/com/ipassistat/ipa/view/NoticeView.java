package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.util.ViewUtil;

public class NoticeView extends LinearLayout {

	private View mContentView; // 整个 通知信息控件
	private View mMessageView; // LinearLayout
	private TextView mMessageTextView; // TextView
	private ImageView mLoadingImageView; // ImageView
	private int[] customErrorImage; // ImageView数组

	public NoticeView(Context context) {
		super(context);
		init();
	}

	public NoticeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NoticeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * @discretion: 初始化数据
	 * @author: MaoYaNan
	 * @date: 2014-10-22 上午10:43:24
	 */
	private void init() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.activity_error, null);
		mContentView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mMessageView = mContentView.findViewById(id.view_error);
		mMessageTextView = (TextView) mContentView
				.findViewById(id.textview_error_content);
		mLoadingImageView = (ImageView) mContentView
				.findViewById(id.imageview_loading);
		addView(mContentView);
		onFirstLoading();
	}

	/**
	 * 首次加载，显示loading进度
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月26日
	 * 
	 */
	public void onFirstLoading() {
		View defaultView = mContentView.findViewById(id.view_group);
		View customView = mContentView.findViewById(id.view_custom);
		defaultView.setVisibility(View.VISIBLE);
		customView.setVisibility(View.GONE);

		dismissMessageView(false);
		// mProgressBar.setVisibility(View.VISIBLE);
		// mMessageTextView.setText(getContext().getString(string.global_message_loading));
		mMessageTextView.setVisibility(View.GONE);
		showLoadingView(true);
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
			AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImageView
					.getDrawable();
			animationDrawable.start();
		}
	}

	/**
	 * 如果没有加载导数据显示的布局
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public void setEmptyRes(int... resIds) {
		customErrorImage = new int[resIds.length];
		for (int i = 0; i < resIds.length; i++) {
			if (i >= customErrorImage.length)
				return;
			customErrorImage[i] = resIds[i];
		}
	}

	/**
	 * 显示自定义的错误View
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月15日
	 */
	public void showCustomView(boolean show, OnClickListener listener,
			int... resIds) {
		View defaultView = mContentView.findViewById(id.view_group);
		View customView = mContentView.findViewById(id.view_custom);

		if (show) {
			ViewUtil.setViewVisiblityWithAnimation(View.GONE, false,
					mMessageTextView, mLoadingImageView);
			ViewUtil.swapViewWithAnimation(defaultView, customView, true);
			// defaultView.setVisibility(View.GONE);
			// customView.setVisibility(View.VISIBLE);
			ImageView[] imageviews = new ImageView[2];
			imageviews = new ImageView[2];
			imageviews[0] = (ImageView) mContentView
					.findViewById(id.imageview_customTop);
			imageviews[1] = (ImageView) mContentView
					.findViewById(id.imageview_customBottom);

			for (int i = 0; i < imageviews.length; i++) {
				imageviews[i].setImageBitmap(null);
				if (i >= resIds.length)
					break;
				if (resIds[i] == 0) {
					imageviews[i].setImageBitmap(null);
				} else {
					imageviews[i].setImageResource(resIds[i]);
					imageviews[i].setOnClickListener(listener);
				}
			}
		} else {
			ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true,
					mMessageTextView, mLoadingImageView);
			ViewUtil.swapViewWithAnimation(customView, defaultView, true);
			// defaultView.setVisibility(View.VISIBLE);
			// customView.setVisibility(View.GONE);
		}
	}

	/**
	 * 隐藏信息页面
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
	 * 设置当前为无网UI,和点击的监听器
	 * 
	 * @param listener
	 *            UI的点击事件（一般为重试事件）
	 * @author LiuYuHang
	 * @date 2014年10月21日
	 */
	public void noNet(OnClickListener listener) {
		showCustomView(true, listener, drawable.error_no_net_top,
				drawable.error_no_net);
	}

	public void close() {
		showCustomView(false, null);
		showLoadingView(false);
	}

}
