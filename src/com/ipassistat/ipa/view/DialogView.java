package com.ipassistat.ipa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 显示一个AlerDialog com.ichsy.mboss.view.DialogView
 * 
 * @author maoyn<br/>
 *         create at 2014-9-19 下午6:31:00
 */
public class DialogView {

	/**
	 * @discretion: 带标题的警告框
	 * @author: MaoYaNan
	 * @date: 2014-10-11 下午2:54:33
	 * @param context
	 *            显示的界面
	 * @param title
	 *            标题
	 * @param message
	 *            显示的详细信息
	 * @param button_neg
	 *            左边的按钮显示文字
	 * @param listener_neg
	 *            左边按钮的响应事件
	 * @param button_pos
	 *            右边按钮显示的文字
	 * @param listener_pos
	 *            右边按钮的响应事件
	 * @return
	 */
	public static AlertDialog getAlertDialogWithTitle(Context context,
			String title, String message, String button_neg,
			DialogInterface.OnClickListener listener_neg, String button_pos,
			DialogInterface.OnClickListener listener_pos) {
		if (context == null && context instanceof Activity)
			return null;

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
				.setNegativeButton(button_pos, listener_pos)
				.setPositiveButton(button_neg, listener_neg)
				.setMessage(message).show();
		return dialog;
	}

	/**
	 * @discretion: 不带标题的警告框
	 * @author: MaoYaNan
	 * @date: 2014-10-11 下午2:57:24
	 * @param context
	 *            显示的界面
	 * @param message
	 *            显示的详细信息
	 * @param button_neg
	 *            左边的按钮显示文字
	 * @param listener_neg
	 *            左边按钮的响应事件
	 * @param button_pos
	 *            右边按钮显示的文字
	 * @param listener_pos
	 *            右边按钮的响应事件
	 * @return
	 */
	public static AlertDialog getAlertDialogWithoutTitle(Context context,
			String message, String button_neg,
			DialogInterface.OnClickListener listener_neg, String button_pos,
			DialogInterface.OnClickListener listener_pos) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setNegativeButton(button_pos, listener_pos)
				.setPositiveButton(button_neg, listener_neg)
				.setMessage(message).show();
		return dialog;
	}

	/**
	 * @discretion: 带一个按钮的警告框
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午10:22:09
	 * @param context
	 *            显示的界面
	 * @param message
	 *            显示的详细信息
	 * @param button_neg
	 *            按钮的文字
	 * @param listener_neg
	 *            按钮的点击事件
	 * @return
	 */
	public static AlertDialog getAlertDialogWithOneButton(Context context,
			String message, String button_neg,
			DialogInterface.OnClickListener listener_neg) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setPositiveButton(button_neg, listener_neg)
				.setMessage(message).show();
		return dialog;
	}

}
