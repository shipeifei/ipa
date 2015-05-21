package com.ipassistat.ipa.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast;

	public static void showToast(Context context, String info) {
		try {
			if (mToast == null) {
				mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
			} else {
				mToast.setText(info);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		} catch (Exception e) {
		}
	}

	public static void showToast(Context context, int stringId) {
		showToast(context, context.getResources().getString(stringId));
	}

}
