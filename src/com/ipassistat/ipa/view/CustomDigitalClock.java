package com.ipassistat.ipa.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import com.ipassistat.ipa.util.ProdectBuyTimerUtil;

/**
 * Custom digital clock —————————————————— from 惠家有
 */

@SuppressWarnings("deprecation")
public class CustomDigitalClock extends DigitalClock {

	Calendar mCalendar;
	private FormatChangeObserver mFormatChangeObserver;

	private Runnable mTicker;
	private Handler mHandler;
	private long mEndTime;
	private ClockListener mClockListener;
	private boolean hasEnd = false;
	private boolean mTickerStopped = false;

	private Context mContext;
	private String mText = "";
	private Drawable[] mCompoundDrawables;
	private String mDayText = " 天";
	private boolean hasEnded = false;
	private boolean hasSetEndTime = false;

	ProdectBuyTimerUtil timerUtil;

	// private ImageView mImageView;
	// private int mDrawableId;

	public CustomDigitalClock(Context context) {
		super(context);
		this.mContext = context;
		initClock(context.getApplicationContext());
	}

	public CustomDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initClock(context.getApplicationContext());
	}

	private void initClock(Context context) {

		mCompoundDrawables = getCompoundDrawables();
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}

		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);

		setFormat();

		timerUtil = new ProdectBuyTimerUtil(context) {

			@Override
			public void onDisplay(String timeText) {
				// TODO 这里写时间改变之后的UI逻辑

			}
		};
	}

	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();

		/**
		 * requests a tick on the next hard-second boundary
		 */
		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped)
					return;
				initTime();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}

		};
		mTicker.run();
	}

	/**
	 * @discretion: 设置倒计时的时间
	 * @author: MaoYaNan
	 * @date: 2014-11-19 下午3:06:28
	 */
	private void initTime() {
		long currentTime = System.currentTimeMillis();
		long distanceTime = mEndTime - currentTime;
		distanceTime /= 1000;
		if (mEndTime == 0) {
			setTextDefault();
		} else if (mEndTime == -1) {
			setTextTimeError();
		} else {
			if (distanceTime == 0) {
				setTextEnd();
			} else if (distanceTime < 0) {
				setTextEnd();
			} else {
				setText(dealTime(distanceTime));
				if (!hasSetEndTime) {
					hasSetEndTime = true;
					setCompoundDrawables(mCompoundDrawables[0],
							mCompoundDrawables[1], mCompoundDrawables[2],
							mCompoundDrawables[3]);
				}
			}
		}
		invalidate();
	}

	/**
	 * @discretion: 默认，倒计时开始时显示的文字
	 * @author: MaoYaNan
	 * @date: 2014-11-19 下午3:00:46
	 */
	private void setTextDefault() {
		setText("");
	}

	/**
	 * @discretion: 活动结束时间为 0
	 * @author: MaoYaNan
	 * @date: 2014-11-19 下午2:56:24
	 */
	private void setTextTimeError() {
		setText("已结束");
	}

	/**
	 * @discretion: 得到倒计时的状态是不是停止了
	 * @author: MaoYaNan
	 * @date: 2014-10-29 上午11:26:13
	 * @return
	 */
	public boolean getTickerStopped() {
		return mTickerStopped;
	}

	/**
	 * deal time string
	 * 
	 * @param time
	 * @return
	 */
	public String dealTime(long time) {
		StringBuffer returnString = new StringBuffer();
		long day = time / (24 * 60 * 60);
		long hours = (time % (24 * 60 * 60)) / (60 * 60);
		long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
		long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
		String dayStr = String.valueOf(day);
		String hoursStr = timeStrFormat(String.valueOf(hours));
		String minutesStr = timeStrFormat(String.valueOf(minutes));
		String secondStr = timeStrFormat(String.valueOf(second));
		returnString.append(mText);
		if (!"0".equals(dayStr)) {
			returnString.append(dayStr).append(mDayText);
		}
		returnString.append(hoursStr).append(":").append(minutesStr)
				.append(":").append(secondStr);
		// returnString.append("距离结束  ").append(hoursStr).append(":").append(minutesStr).append(":").append(secondStr);
		return returnString.toString();
	}

	/**
	 * @discretion: 设置天字的显示状态
	 * @author: MaoYaNan
	 * @date: 2014-11-1 下午3:12:19
	 * @param mDayText
	 */
	public void setDayText(String mDayText) {
		this.mDayText = mDayText;
	}

	/**
	 * format time
	 * 
	 * @param timeStr
	 * @return
	 */
	public static String timeStrFormat(String timeStr) {
		switch (timeStr.length()) {
		case 1:
			timeStr = "0" + timeStr;
			break;
		}
		return timeStr;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

	/**
	 * Clock end time from now on.
	 * 
	 * @param endTime
	 */
	public void setEndTime(long endTime) {
		this.mEndTime = endTime;
		initTime();
	}

	/**
	 * @param endTime
	 */
	public void setEndTime(String endTime) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.mEndTime = sf.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		initTime();

	}

	private void setTextEnd() {
		setText("已结束");
		hasEnded = true;
		if (mClockListener != null) {
			mClockListener.timeEnd();
		}
	}

	/**
	 * @discretion: 判断倒计时是否已结束
	 * @author: MaoYaNan
	 * @date: 2014-11-4 下午4:20:18
	 * @return
	 */
	public boolean getHasEnded() {
		return hasEnded;
	}

	/**
	 * Pulls 12/24 mode from system settings
	 */
	private boolean get24HourMode() {
		return android.text.format.DateFormat.is24HourFormat(getContext());
	}

	private void setFormat() {
		if (get24HourMode()) {
		} else {
		}
	}

	/**
	 * 时间格式变化监听
	 * 
	 */
	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			setFormat();
		}
	}

	public void setTextString(String text) {
		this.mText = text;
	}

	/**
	 * 设置时钟的监听
	 * 
	 * @param clockListener
	 */
	public void setClockListener(ClockListener clockListener) {
		this.mClockListener = clockListener;
	}

	/**
	 * 时钟监听接口
	 * 
	 */
	public interface ClockListener {

		void timeEnd();
	}

}