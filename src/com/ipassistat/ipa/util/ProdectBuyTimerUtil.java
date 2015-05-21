package com.ipassistat.ipa.util;

import android.content.Context;
import android.os.Handler;

import com.ipassistat.ipa.view.CustomDigitalClock;

public abstract class ProdectBuyTimerUtil {
	private Handler handler;
	private long mEndTime;;

	public abstract void onDisplay(String timeText);

	public ProdectBuyTimerUtil(Context context) {
		handler = new Handler();
		handler.post(timeTask);
	}

	/**
	 * 
	 *
	 * @param endTime
	 * @author LiuYuHang
	 * @date 2014年11月19日
	 */
	public void resume(long endTime) {
		this.mEndTime = endTime;
		onDisplay(formatTimeText(mEndTime));
	}

	public void stop() {
		handler.removeCallbacks(timeTask);
	}

	private Runnable timeTask = new Runnable() {

		@Override
		public void run() {
			onDisplay(formatTimeText(mEndTime));
			handler.postDelayed(timeTask, 1000);
		}
	};

	private String formatTimeText(long endTIme) {
		long currentTime = System.currentTimeMillis();
		long distanceTime = endTIme - currentTime;
		distanceTime /= 1000;

		if (endTIme == 0) {
			return "";
		} else if (endTIme == -1) {
			return "已结束";
		} else if (distanceTime <= 0) {
			return "已结束";
		} else {
			return dealTime(distanceTime);
		}
	}

	/**
	 * deal time string
	 * 
	 * @param time
	 * @return
	 */
	private String dealTime(long time) {
		StringBuffer returnString = new StringBuffer();
		long day = time / (24 * 60 * 60);
		long hours = (time % (24 * 60 * 60)) / (60 * 60);
		long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
		long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
		String dayStr = String.valueOf(day);
		String hoursStr = CustomDigitalClock.timeStrFormat(String.valueOf(hours));
		String minutesStr = CustomDigitalClock.timeStrFormat(String.valueOf(minutes));
		String secondStr = CustomDigitalClock.timeStrFormat(String.valueOf(second));
		// returnString.append(mText);
		if (!"0".equals(dayStr)) {
			// returnString.append(dayStr).append(mDayText);
		}
		returnString.append(hoursStr).append(":").append(minutesStr).append(":").append(secondStr);
		// returnString.append("距离结束  ").append(hoursStr).append(":").append(minutesStr).append(":").append(secondStr);
		return returnString.toString();
	}
}
