package com.ipassistat.ipa.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;
import com.ipassistat.ipa.ui.gesture.SlideCloseGesture;
import com.ipassistat.ipa.ui.gesture.SlideCloseGesture.CustomGestureListener;
import com.ipassistat.ipa.util.InputMethodUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;


/***
 * 所有activity的超类
 * com.ipassistat.ipa.ui.activity.BaseActivity
 * @author 时培飞 
 * Create at 2015-4-24 下午4:55:19
 */
public abstract class BaseActivity extends FragmentActivity implements HttpRequestLisenter, CustomGestureListener {

	/**
	 * 滑动关闭的手势默认为打开
	 */
	private boolean SlideOpen = true;
	private GestureDetector mGestureDetector;
	protected ProgressHub mHub;
	
	private boolean isRegiterEventBus = false;  //是否使用eventbus来接受消息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this, new SlideCloseGesture(this));
		mHub = ProgressHub.getInstance(getApplication());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//initGlobalLisener();
	}
	
	/**
	 * 绑定控件id
	 */
	protected abstract void findViewById();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();
	
	/***
	 * 初始化数据
	 */
	protected abstract void initData();
	/***
	 * 绑定事件
	 */
	protected abstract void  bindEvents();

	/**
	 * 禁用滑动关闭手势
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月28日
	 */
	protected void disableSlideClose() {
		SlideOpen = false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (SlideOpen) {
			// 手势滑动关闭
			mGestureDetector.onTouchEvent(ev);
			return super.dispatchTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	//注册eventbus
	protected void registerEventBus(){
		isRegiterEventBus = true;
		//EventBus.getDefault().register(this);
	}
	
	
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {}

	@Override
	public void onError(String ur, String result) {
		ToastUtil.showToast(this, "网络异常");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgressHub();
		InputMethodUtil.showForced(getApplicationContext(), getCurrentFocus(), false, null);
		if(isRegiterEventBus){
			//EventBus.getDefault().unregister(this);
		}
	}

	@Override
	public void onNoNet() { }

	@Override
	public void onNoTimeOut() {}

	/**
	 * 添加监听器简单封装
	 * 
	 * @param ids
	 *            需要添加监听器的ids
	 * @param listener
	 */
	public void findListeners(int[] ids, View.OnClickListener listener) {
		if (ids == null) return;
		for (int i = 0; i < ids.length; i++) {
			View v = findViewById(ids[i]);
			if (v != null) v.setOnClickListener(listener);
		}
	}
	
	/**
	 * 隐藏进度条
	 */
	protected void dismissProgressHub(){
		if(mHub!=null){
			mHub.dismiss();
		}
	}

	/**
	 * 添加监听器简单封装
	 * 
	 * @param ids
	 *            需要添加监听器的ids
	 * @param listener
	 */
	public void findListeners(View.OnClickListener listener, int... ids) {
		findListeners(ids, listener);
	}

	/**
	 * 设置顶部View的Title（如果有）
	 * 
	 * @param title
	 */
	public void setTitleText(CharSequence title) {
		TextView titleView = (TextView) findViewById(R.id.title);
		if (titleView != null) titleView.setText(title);
	}

	public void hidLeftButton(boolean hide) {
		ImageView leftImageView = (ImageView) findViewById(R.id.left_imgv);
		leftImageView.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
	}

	/**
	 * 设置右上角图片按钮资源
	 * 
	 * @param resId
	 */
	public void setTitleRightImage(int resId) {
		ImageView rightImageView = (ImageView) findViewById(R.id.right_imgv);
		if (rightImageView != null) {
			rightImageView.setImageResource(resId);
			rightImageView.setVisibility(View.VISIBLE);
		}
	}

	public void setTitleRightText(CharSequence text, OnClickListener listener) {
		TextView rightTextView = (TextView) findViewById(id.right_textview);
		if (rightTextView != null) {
			rightTextView.setText(text);
			rightTextView.setOnClickListener(listener);
			ViewUtil.setViewVisiblityWithAnimation(View.VISIBLE, true, rightTextView);
		}
	}

	public void setRightPopText(String text) {
		TextView rightPopTextView = (TextView) findViewById(R.id.textview_right_tip);

		if (TextUtils.isEmpty(text)) {
			rightPopTextView.setVisibility(View.GONE);
		} else {
			rightPopTextView.setVisibility(View.VISIBLE);
			rightPopTextView.setText(text);
		}
	}

	public void showRightSpace(boolean show) {
		// View rightSpace = findViewById(R.id.view_rightspace);
		// rightSpace.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onFinish() {
		dismissProgressHub();
	}

	@Override
	public void finish() {
		InputMethodUtil.showForced(getApplicationContext(), getCurrentFocus(), false, null);
		super.finish();
	}

	@Override
	public void onSlideClose() {
		finish();
	}

	

}
