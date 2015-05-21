package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.util.DeviceUtil;

/**
 * 弹出式对话框
 * 
 * @author LiuYuHang
 *
 */
public class PopAction {
	private Context mContext;
	private static PopupWindow mWindow;

	@SuppressWarnings("deprecation") public PopAction(Context context, View contentView) {
		mContext = context;
		if (mWindow == null) {
			mWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mWindow.setOutsideTouchable(true);
			mWindow.setFocusable(true);
			mWindow.setBackgroundDrawable(new BitmapDrawable());
		} else {
			mWindow.setContentView(contentView);
		}
	}

	public void setOnDismissListener(OnDismissListener l) {
		mWindow.setOnDismissListener(l);
	}

	public void setAnimation(Animation animation) {

	}

	@Deprecated
	public void pop(View parent, int x, int y) {
		if (mWindow == null) return;
		mWindow.dismiss();
		mWindow.showAtLocation(parent, Gravity.CENTER, x, y);

	}

	public View getContentView() {
		return mWindow.getContentView();
	}

	/**
	 * 从底部弹出
	 * 
	 * @param v
	 */
	public void popFromBottom(View parent) {
		if (mWindow == null) return;
		mWindow.dismiss();
		// mWindow.showAsDropDown(v);
		int deviceHeight = DeviceUtil.getDeviceHeight(mContext);
		mWindow.setAnimationStyle(R.style.popmenu_anim);
		mWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 0, deviceHeight - mWindow.getContentView().getMeasuredHeight());
	}

	public void dismiss() {
		if (mWindow == null) return;
		mWindow.dismiss();
	}

}
