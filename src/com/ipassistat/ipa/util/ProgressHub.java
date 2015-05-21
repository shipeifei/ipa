package com.ipassistat.ipa.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.view.WindowManger;

/**
 * progessView
 * 
 * @author LiuYuHang
 * @date 2014年9月28日
 *
 */
public class ProgressHub {
	static ProgressHub mProgressHub;
	private WindowManger mWm;

	public static ProgressHub getInstance(Context context) {
		if (mProgressHub == null) mProgressHub = new ProgressHub(context);
		return mProgressHub;
	}

	public ProgressHub(Context context) {
		mWm = new WindowManger(context);
	}

	/**
	 * 给当前windowManager添加布局
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public WindowManger buildLayout(WindowManger mWm, CharSequence message) {
		View contentView = View.inflate(mWm.getContext(), layout.loading_view, null);
		mWm.setContentView(contentView);

		TextView messageView = (TextView) mWm.getContentView().findViewById(id.textview_error_content);
		if (message != null) messageView.setText(message);
		mWm.wmParams.flags = WindowManger.FLAG_NORMAL;

		return mWm;
	}

	/**
	 * 显示一个不会阻挡用户操作的PopDialog
	 *
	 * @param message
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public void show() {
		show(null);
	}

	/**
	 * 显示一个不会阻挡用户操作的PopDialog
	 *
	 * @param message
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public synchronized void show(CharSequence message) {
		dismiss();
		buildLayout(mWm, message).pop();
	}

	/**
	 * 显示一个阻挡用户操作的diaolog
	 *
	 * @param message
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public synchronized void showWithNoTouch(CharSequence message) {
		dismiss();

		mWm = buildLayout(mWm, message);
		mWm.wmParams.flags = WindowManger.FLAG_BLOCK;
		mWm.pop();
	}

	public synchronized void dismiss() {
		mWm.dismiss();
	}
}
