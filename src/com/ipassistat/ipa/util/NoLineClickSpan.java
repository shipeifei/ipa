package com.ipassistat.ipa.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.ui.personal.activity.LoginActivity;


/**
 * 不带下划线的链接，可以跳转到 Intent ，也可以 打开 浏览器
 * 
 * @author maoyn
 * 
 */
public class NoLineClickSpan extends ClickableSpan {

	Context context;
	private String title;
	private String url;

	public NoLineClickSpan(Context context, String title, String url) {
		super();
		this.context = context;
		this.title = title;
		this.url = url;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(context.getResources().getColor(
				R.color.protocol_text_color_link));
	}

	@Override
	public void onClick(View widget) {
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
}
