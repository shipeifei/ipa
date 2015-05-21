package com.ipassistat.ipa.util;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法管理 工具类
 * 
 * @author LiuYuHang
 * @date 2014年9月28日
 * 
 */
public class InputMethodUtil {

	public interface InputMethodListner {
		/**
		 * 监听输入法键盘打开关闭的状态
		 * 
		 * @param open
		 * 
		 * @author LiuYuHang
		 * @date 2014年10月9日
		 */
		void onInputMethodOpend(boolean open);
	}

	/**
	 * 打开或者关闭输入法
	 * 
	 * @param context
	 * @param focusView
	 *            焦点View
	 * @param show
	 *            是否显示
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月28日
	 */
	public static void showForced(Context context, View focusView, boolean show, InputMethodListner listner) {
		if (focusView == null) return;
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (show) {
			focusView.requestFocus();
			imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
		} else {
			imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0); // 强制隐藏键盘
		}

		if (listner != null) listner.onInputMethodOpend(show);
	}

	/**
	 * 监听输入法的打开关闭状态
	 * 
	 * @return
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月9日
	 */
	public static void setOnKeyboardStateListener(final View rootView, final InputMethodListner listner) {

		// 监听输入法
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
				if (heightDiff > 100) {
					LogUtil.outLogDetail("inputMethod open");
				} else {
					LogUtil.outLogDetail("inputMethod close");
				}
				listner.onInputMethodOpend(heightDiff > 100);
			}
		});
	}

	public static boolean isKeyboardOpen(View rootView) {
		int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
		return heightDiff > 100;
	}
}
