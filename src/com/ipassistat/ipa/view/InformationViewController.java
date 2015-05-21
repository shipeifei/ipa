package com.ipassistat.ipa.view;

import android.content.Context;
import android.view.View;

public class InformationViewController {
	private View mInfomationView;
	private View mErrorView;

	/**
	 * 
	 * @param context
	 * @param infomationView 
	 * @param errorView
	 */
	public InformationViewController(Context context, View infomationView, View errorView) {
		this.mInfomationView = infomationView;
		this.mErrorView = errorView;
	}

	public void showMainView(boolean show) {

	}

	public void showLoadingView(boolean show) {

	}

	public void showErrorView(boolean show) {

	}
}
