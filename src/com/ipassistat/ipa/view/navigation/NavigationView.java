package com.ipassistat.ipa.view.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义导航条工具类
 * 
 * @author LiuYuHang
 * @E-Mail liuyh152@163.com
 * @date 2014年9月20日
 */
@Deprecated
public class NavigationView extends LinearLayout implements NavigationController {
	private Activity mActivity;

	private NavigationController mController;

	private final int ID_VIEW_OF_LEFT = 211;
	private final int ID_VIEW_OF_RIGHT = ID_VIEW_OF_LEFT + 1;
	private final int ID_VIEW_OF_CENTER = ID_VIEW_OF_RIGHT + 1;

	public NavigationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NavigationView(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (getContext() instanceof Activity) {
			mActivity = (Activity) getContext();
			setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			setOrientation(LinearLayout.HORIZONTAL);

			setController(this);

			View rootView = createBasicView();
			onViewCreate(rootView);
		}
	}

	/**
	 * 基础View创建之后
	 * 
	 * @param rootView
	 */
	private void onViewCreate(View rootView) {
		// ---
		Button leftBtn = new Button(getContext());
		leftBtn.setText("返回");
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getController().back();
			}
		});

		Button rightBtn = new Button(getContext());
		rightBtn.setText("发表");
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});

		TextView centerText = new TextView(getContext());
		centerText.setText("发表文章");

		setLeftView(leftBtn);
		setCenterView(centerText);
		setRightView(rightBtn);
	}

	/**
	 * 创建导航条的基础布局，分为左中右三个区域
	 * 
	 * @return
	 */
	private View createBasicView() {
		LinearLayout leftView = new LinearLayout(getContext());
		leftView.setId(ID_VIEW_OF_LEFT);
		LinearLayout rightView = new LinearLayout(getContext());
		rightView.setId(ID_VIEW_OF_RIGHT);
		LinearLayout centerView = new LinearLayout(getContext());
		centerView.setId(ID_VIEW_OF_CENTER);

		LayoutParams centerlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		LayoutParams otherlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0);

		centerView.setLayoutParams(centerlp);
		leftView.setLayoutParams(otherlp);
		rightView.setLayoutParams(otherlp);

		centerView.setGravity(Gravity.CENTER);
		leftView.setGravity(Gravity.CENTER);
		rightView.setGravity(Gravity.CENTER);

		addView(leftView);
		addView(centerView);
		addView(rightView);
		return this;
	}

	/**
	 * 设置中间的布局
	 * 
	 * @param v
	 */
	public void setCenterView(View v) {
		addViewById(ID_VIEW_OF_CENTER, v);
	}

	/**
	 * 设置右边的布局
	 * 
	 * @param v
	 */
	public void setRightView(View v) {
		addViewById(ID_VIEW_OF_RIGHT, v);
	}

	/**
	 * 设置左边的布局
	 * 
	 * @param v
	 */
	public void setLeftView(View v) {
		addViewById(ID_VIEW_OF_LEFT, v);
	}

	/**
	 * 根据id添加子View
	 */
	private void addViewById(int viewId, View child) {
		ViewGroup view = (ViewGroup) findViewById(viewId);
		if (view == null) return;

		view.removeAllViews();
		view.addView(child);
	}

	public NavigationController getController() {
		return mController;
	}

	public void setController(NavigationController mController) {
		this.mController = mController;
	}

	@Override
	public void back() {
		if (mActivity == null) return;

		mActivity.finish();
	}

	@Override
	public void pop(Intent intent) {

		if (mActivity == null) return;

		mActivity.startActivity(intent);
	}

}
