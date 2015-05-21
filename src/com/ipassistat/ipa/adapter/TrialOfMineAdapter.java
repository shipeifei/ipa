package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.TryOutGood;
import com.ipassistat.ipa.business.CropImageListener;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.CustomDigitalClock;
import com.ipassistat.ipa.view.CustomDigitalClock.ClockListener;
import com.lidroid.xutils.BitmapUtils;

/**
 * 我的试用适配器
 * 
 * @author maoyn
 * 
 */
public class TrialOfMineAdapter extends PaginationAdapter<TryOutGood> {
	private Context mContext;
	public String mTag = Constant.TRIAL_FREE;
	public int[] mImages = { R.drawable.trial_process_item,
			R.drawable.trial_fail_item, R.drawable.trial_success_item,
			R.drawable.trial_end_item };
	private BitmapUtils mBitmapUtil;
	private CropImageListener mBitmapListener = new CropImageListener();

	public TrialOfMineAdapter(Context context, List<TryOutGood> datas,
			String tag) {
		super(context, datas);
		this.mContext = context;
		mTag = tag;
		mBitmapUtil = BitmapOptionsFactory.newInstanceBitmapUtils(mContext,
				R.drawable.default_goods_img);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_trial, parent,
					false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.product = (TextView) convertView.findViewById(R.id.product);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.clock = (CustomDigitalClock) convertView
					.findViewById(R.id.clock);
			holder.state = (ImageView) convertView.findViewById(R.id.state);
			holder.participant = (TextView) convertView
					.findViewById(R.id.participant);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.reset();
		}

		TryOutGood item = getItem(position);
		String temp_product = item.getName();
		String temp_price = "价值：￥" + item.getPrice();
		String temp_count = "";
		String temp_participate = "";
		int end;
		int start;
		if (Constant.TRIAL_FREE.equals(mTag)) {
			temp_count = "限量:" + item.getCount() + "件";
			temp_participate = "已申请:" + item.getTryout_count() + "人";
			start = temp_count.indexOf(":") + 1;
			end = temp_count.indexOf("件");
			holder.clock.setVisibility(View.VISIBLE);
		} else if (Constant.TRIAL_POSTAGE.equals(mTag)) {
			temp_count = "剩余:" + item.getSurplus_count() + "/"
					+ item.getCount() + "件";
			temp_participate = "邮费:￥" + item.getPostage();
			start = temp_count.indexOf(":") + 1;
			end = temp_count.indexOf("/");
			holder.clock.setVisibility(View.INVISIBLE);
		} else {
			start = 0;
			end = 0;
		}
		holder.product.setText(temp_product);
		holder.price.setText(ViewUtil.getTextSizeStyle(temp_price, 3, 4, 11));
		holder.count.setText(ViewUtil.getTextSizeStyle(ViewUtil
				.getTextColorStyle(temp_count, start, end, mContext
						.getResources().getColor(R.color.trial_red)), start,
				end, 17));
		holder.participant.setText(temp_participate);
		int key = judgeStatus(item.getStatus());
		if (key != -1) {
			holder.state.setVisibility(View.VISIBLE);
			holder.state.setImageResource(mImages[key]);
		} else {
			holder.state.setVisibility(View.GONE);
		}
		if (item.getPhoto() != null) {
			mBitmapUtil.display(holder.image, item.getPhoto(), mBitmapListener);
		}
		holder.clock.setTextString("距离结束:");
		holder.clock.setEndTime(item.getTime());
		setStatusImage(holder.clock, item.getStatus(), holder.state,
				R.drawable.trial_fail_item);
		return convertView;
	}

	private int judgeStatus(String string) {
		int key = -1;
		if (Constant.TRIAL_FREE.equals(mTag)) {
			if (Constant.TRYPROCESSING_FREE.equals(string)) {
				key = 0;
			} else if (Constant.TRYSUCCESS_FREE.equals(string)) {
				key = 2;
			} else if (Constant.TRYFAIL_FREE.equals(string)) {
				key = 1;
			} else {
				LogUtil.outLogDetail("----------------我的试用------返回的状态有问题--"
						+ string + "------------------");
			}
		} else if (Constant.TRIAL_POSTAGE.equals(mTag)) {

			if (Constant.TRYSUCCESS_POSTAGE.equals(string)) {
				key = -1;
			} else if (Constant.TRYFAIL_POSTAGE.equals(string)) {
				key = 3;
			} else {
				LogUtil.outLogDetail("----------------我的试用------返回的状态有问题--"
						+ string + "------------------");
			}
		}
		return key;
	}

	/**
	 * @discretion: 设置状态的图片
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午12:58:27
	 * @param clock
	 * @param state
	 * @param trialEndItem
	 */
	private void setStatusImage(final CustomDigitalClock clock,
			final String status, final ImageView state, final int trialEndItem) {
		clock.setClockListener(new ClockListener() {
			@Override
			public void timeEnd() {
				if (Constant.TRIAL_FREE.equals(mTag)) {
					clock.setVisibility(View.INVISIBLE); // 倒计时消失
				}
			}
		});
	}

	class ViewHolder {
		ImageView image;
		TextView product;
		TextView price;
		TextView count;
		CustomDigitalClock clock;
		ImageView state;
		TextView participant;

		public void reset() {
			image.setImageResource(R.drawable.default_goods_img);
			product.setText("");
			price.setText("");
			count.setText("");
			clock.setEndTime(0);
			state.setImageDrawable(null);
			participant.setText("");
		}
	}
}
