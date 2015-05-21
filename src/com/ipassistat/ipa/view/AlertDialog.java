package com.ipassistat.ipa.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * 为了方便以后统一修改样式，统一定义在一个地方
 * 
 * @author LiuYuHang
 * @date 2014年11月3日
 */
public class AlertDialog {
	private Builder mBuilder;

	public AlertDialog(Context context) {
		mBuilder = new Builder(context);
	}

	public AlertDialog setItems(CharSequence[] items, OnClickListener listener) {
		mBuilder.setItems(items, listener);
		return this;
	}
	
	public AlertDialog setTitle(CharSequence message) {
		mBuilder.setTitle(message);
		return this;
	}

	public AlertDialog setMessage(CharSequence message) {
		mBuilder.setMessage(message);
		return this;
	}

	public AlertDialog setPositiveButton(CharSequence text, OnClickListener listener) {
		mBuilder.setPositiveButton(text, listener);
		return this;
	}

	public AlertDialog setNegativeButton(CharSequence text, OnClickListener listener) {
		mBuilder.setNegativeButton(text, listener);
		return this;
	}

	public void show() {
		mBuilder.show();
	}
}
