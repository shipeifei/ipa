package com.ipassistat.ipa.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenInfoUtil {
	public static DisplayMetrics getScreenInfo(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	    return dm;
	}
	
	
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	    return dm.widthPixels;
	}
}
