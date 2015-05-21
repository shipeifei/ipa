package com.ipassistat.ipa.util.share.ichsy;

import android.content.Context;
import android.view.View;

import com.ipassistat.ipa.view.PopAction;

/**
 * 分享组件
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 *
 */
public class ShareHandler {

	private static ShareHandler mShareHandler;
	private ShareController mController;
	public Context mContext;
	public PopAction mShareWindow;

	public static ShareHandler getInstance(Context context, ShareController controller) {
		if (mShareHandler == null)
			mShareHandler = new ShareHandler(context, controller);
		return mShareHandler;
	}

	/**
	 * 构造函数
	 */
	public ShareHandler(Context context, ShareController controller) {
		if (controller == null) {
			throw new NullPointerException("controller 不能为 NULL");
		}
		mController = controller;
		mContext = context;
	}

	public void openShare(View rootView) {
		View shareView = ShareViewFactory.getDefaultView(this, mController);
		mShareWindow = new PopAction(mContext, shareView);

		mShareWindow.popFromBottom(rootView);
	}

}
