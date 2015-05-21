package com.ipassistat.ipa.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.TimedScareBuying;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.ScreenInfoUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.CustomDigitalClock;
import com.ipassistat.ipa.view.CustomDigitalClock.ClockListener;

/**
 * @Discription： 限时抢购列表的数据适配器，改成继承 PaginationAdapter
 * @package： com.ichsy.mboss.adapter.TimeLimitAdapter
 * @author： MaoYaNan
 * @date：2014-9-26 上午10:54:11
 */
public class TimeLimitAdapter extends PaginationAdapter<TimedScareBuying> {

	private Context mContext;
	private Drawable mDrawable;
	// private BitmapUtils mBitmapUtil;
//	private CropImageListener mBitmapListener = new CropImageListener();
	private LayoutParams mLayoutParams;

	public TimeLimitAdapter(Context context, List<TimedScareBuying> data) {
		super(context, data);
		this.mContext = context;
		mDrawable = mContext.getResources().getDrawable(R.drawable.timelimitbuy_clock);
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
		// mBitmapUtil = BitmapOptionsFactory.newInstanceBitmapUtils(context,
		// R.drawable.timelimit_default);
		mLayoutParams = getLayoutParams(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_timelimit, parent, false);
			holder.timelimit_image = (ImageView) convertView.findViewById(R.id.timelimit_image);
			holder.timelimit_image.setLayoutParams(mLayoutParams);
			holder.timelimitclock = (CustomDigitalClock) convertView.findViewById(R.id.timelimitclock);
			holder.timelimit_title = (TextView) convertView.findViewById(R.id.timelimit_title);
			holder.timelimit_cost = (TextView) convertView.findViewById(R.id.timelimit_cost);
			holder.timelimit_cost_pass = (TextView) convertView.findViewById(R.id.timelimit_cost_pass);
			holder.timelimit_discount = (TextView) convertView.findViewById(R.id.timelimit_discount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String temp_price_now = "￥" + getItem(position).getNewPrice();
		String temp_price_old = "￥" + getItem(position).getOldPrice();
		if (getItem(position).getPhotoUrl() != null) {
//			mBitmapUtil.display(holder.timelimit_image, getItem(position).getPhotoUrl(), mBitmapListener);
//			ImageLoader.getInstance().displayImage(getItem(position).getPhotoUrl(), holder.timelimit_image, BitmapOptionsFactory.getImageOption(), new ImageLoaderCallbackFactory());
			BitmapOptionsFactory.getInstance(getContext()).display(holder.timelimit_image, getItem(position).getPhotoUrl());
		}

		holder.timelimit_title.setText(getItem(position).getName()); // 设置标题
		holder.timelimit_cost.setText(ViewUtil.getTextColorStyle(ViewUtil.getTextSizeStyle(temp_price_now, 0, 1, 20), 0, temp_price_now.length(), mContext.getResources().getColor(R.color.trial_red)));
		holder.timelimit_cost_pass.setText(ViewUtil.getTextStrikeThroughStyle(ViewUtil.getTextSizeStyle(temp_price_old, 0, 1, 14), 0, temp_price_old.length()));
		holder.timelimit_discount.setText(ViewUtil.getRebateString(getItem(position).getRebate()));
		holder.timelimitclock.setDayText("天 ");

		setClockDismiss(holder.timelimitclock, getItem(position).getRemaind_count(), getItem(position).getEndTime());
		return convertView;
	}

	/**
	 * @discretion: 设置倒计时
	 * @author: MaoYaNan
	 * @date: 2014-11-10 下午12:03:19
	 * @param timelimitclock
	 * @param remaind_count
	 * @param endTime
	 */
	private void setClockDismiss(CustomDigitalClock timelimitclock, String remaind_count, String endTime) {
		if (TextUtils.isEmpty(remaind_count)) return;
		if (!TextUtils.isEmpty(remaind_count) && Integer.parseInt(remaind_count) > 0) {
			timelimitclock.setEndTime(endTime);
			setClockDismiss(timelimitclock);
		} else if (TextUtils.isEmpty(remaind_count) || Integer.parseInt(remaind_count) <= 0) {
			timelimitclock.setEndTime(-1);
			setEnd(timelimitclock);
		}
	}

	/**
	 * @discretion: 时钟消失
	 * @author: MaoYaNan
	 * @date: 2014-11-1 下午3:01:28
	 * @param timelimitclock
	 */
	private void setClockDismiss(final CustomDigitalClock timelimitclock) {
		timelimitclock.setClockListener(new ClockListener() {

			@Override
			public void timeEnd() {
				setEnd(timelimitclock);
			}

		});
	}

	/**
	 * @discretion: 调整倒计时结束，或剩余件数为0 的处理
	 * @author: MaoYaNan
	 * @date: 2014-11-10 上午11:59:13
	 * @param timelimitclock
	 */
	private void setEnd(final CustomDigitalClock timelimitclock) {
		timelimitclock.setCompoundDrawables(null, null, null, null);
	}

	class ViewHolder {
		ImageView timelimit_image;
		CustomDigitalClock timelimitclock;
		TextView timelimit_title;
		TextView timelimit_cost;
		TextView timelimit_cost_pass;
		TextView timelimit_discount;

	}

	private LayoutParams getLayoutParams(Context context) {
		int width = ScreenInfoUtil.getScreenWidth((Activity) context);
		int height = (int) (width * 0.519);
		LayoutParams layoutParams = new LayoutParams(width, height);
		return layoutParams;
	}

}
