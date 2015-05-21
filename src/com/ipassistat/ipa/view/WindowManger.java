package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

/**
 * 类似于Dialog的popwinow
 * 
 * @author LiuYuHang
 *
 */
public class WindowManger {
	public static final int FLAG_NORMAL = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
	public static final int FLAG_BLOCK = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
	public WindowManager.LayoutParams wmParams ;
	private WindowManager wManager;
	private Context context;

	private View mContentView;
	private Animation mAnimation;

	public WindowManger(Context context) {
		this.context = context;

		wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		wmParams = new WindowManager.LayoutParams();

//		wmParams.token = ((Activity)context).getWindow().getDecorView().getWindowToken();
//		wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL          ; // 设置window
		// type
		 wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; //
		// 设置window
		// type
		wmParams.format = PixelFormat.TRANSLUCENT; // 设置图片格式，效果为背景透明

		// WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		// WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

		wmParams.gravity = Gravity.CENTER; // 调整悬浮窗口至右侧中间
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	}

	/**
	 * 设置布局
	 * 
	 * @param v
	 */
	public void setContentView(View contentView) {
		this.mContentView = contentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * 设置弹出动画
	 */
	public void setAnimation(Animation animation) {
		this.mAnimation = animation;
	}

	/**
	 * 弹出View
	 */
	public synchronized void pop() {
		if (mContentView == null) { throw new NullPointerException("还没有设置contentView"); }
		// 然后，就可以将需要加到悬浮窗口中的View加入到窗口中了：
		// if (view.getParent == null)// 如果view没有被加入到某个父组件中，则加入WindowManager中

		dismiss();
		wManager.addView(mContentView, wmParams);
	}

	public synchronized void dismiss() {
		if (wmParams == null || mContentView == null) return;

		try {
			wManager.removeView(mContentView);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
