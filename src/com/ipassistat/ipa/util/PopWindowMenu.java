package com.ipassistat.ipa.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ipassistat.ipa.R;

/**
 * 弹出PopWindow的菜单
 * 
 * @author LiuYuHang
 *
 */
public class PopWindowMenu {
	private Context mContext;
	private PopupWindow mWindow;
	private View mParentView;

	@SuppressWarnings("deprecation") public PopWindowMenu(Context context, View contentView, View parentView) {
		mContext = context;
		mParentView = contentView;

		mWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mWindow.setOutsideTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void show() {
		if (mWindow.isShowing()) {
			// mWindow.dismiss();
			return;
		}
		int windowWidth = mWindow.getContentView().getMeasuredWidth();
		int viewWidth = mParentView.getWidth();
		int offsetX = windowWidth / 2 - viewWidth / 2;
		if (offsetX > 0)
			offsetX = -offsetX;
		final int offsetX_l = offsetX;

		mWindow.showAsDropDown(mParentView, offsetX_l, (int) mContext.getResources().getDimension(R.dimen.size_1));
	}

	public void dismiss() {
		mWindow.dismiss();
	}

	public void setOnDismissListener(OnDismissListener l) {
		mWindow.setOnDismissListener(l);
	}

	public boolean isShowing() {
		return mWindow.isShowing();
	}

}
