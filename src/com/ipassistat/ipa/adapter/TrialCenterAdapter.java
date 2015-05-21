package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.TryOutGood;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.CustomDigitalClock;
import com.ipassistat.ipa.view.PinnedHeaderListView;
import com.ipassistat.ipa.view.CustomDigitalClock.ClockListener;
import com.ipassistat.ipa.view.PinnedHeaderListView.PinnedHeaderAdapter;

/**
 * 试用中心的适配器，适用于 粘性的listView
 * 
 * @author maoyn
 * 
 */
public class TrialCenterAdapter extends BaseAdapter implements OnScrollListener, SectionIndexer,
		PinnedHeaderAdapter, ListAdapter {
	/** 标记:内容 */
	private static final int TYPE_ITEM = 0;
	/** 标记:标题 */
	private static final int TYPE_SEPARATOR = 1;
	private Context mContext;
	/** 适用商品集合 */
	private List<TryOutGood> mTryGoodList = new ArrayList<TryOutGood>();
	/** 标题集合 */
	private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
	private LayoutInflater mInflater;
	/** 标题用图片数组 */
	private int[] imagBackground = { R.drawable.trial_free, R.drawable.trial_pay };

	public TrialCenterAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		mTryGoodList.clear();
		mSeparatorsSet.clear();
	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(final TryOutGood item) {
		mTryGoodList.add(item);
		notifyDataSetChanged();
	}

	/**
	 * 添加分隔Item
	 * 
	 * @param item
	 */
	public void addSeparatorItem(final TryOutGood item) {
		mTryGoodList.add(item);
		mSeparatorsSet.add(mTryGoodList.size() - 1);
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 2; // 两种类型的Item
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getCount() {
		return mTryGoodList == null ? 0 : mTryGoodList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTryGoodList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2 = new ViewHolder2();
				convertView = mInflater.inflate(R.layout.adapter_trial_separator, parent, false);
				holder2.separator = (ImageView) convertView.findViewById(R.id.separator);
				convertView.setTag(holder2);
				break;
			case TYPE_ITEM:
				holder1 = new ViewHolder1();
				convertView = mInflater.inflate(R.layout.adapter_trial, parent, false);
				holder1.image = (ImageView) convertView.findViewById(R.id.image);
				holder1.product = (TextView) convertView.findViewById(R.id.product);
				holder1.price = (TextView) convertView.findViewById(R.id.price);
				holder1.count = (TextView) convertView.findViewById(R.id.count);
				holder1.clock = (CustomDigitalClock) convertView.findViewById(R.id.clock);
				holder1.state = (ImageView) convertView.findViewById(R.id.state);
				holder1.participant = (TextView) convertView.findViewById(R.id.participant);
				convertView.setTag(holder1);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2 = (ViewHolder2) convertView.getTag();
				holder2.reset();
				break;
			case TYPE_ITEM:
				holder1 = (ViewHolder1) convertView.getTag();
				holder1.reset();
				break;
			default:
				break;
			}
		}
		TryOutGood tryOutGood = mTryGoodList.get(position);
		switch (type) {
		case TYPE_SEPARATOR:
			if (Constant.TRIAL_FREE.equals(tryOutGood.getIs_freeShipping())) {
				holder2.separator.setBackgroundResource(imagBackground[0]);
			} else if (Constant.TRIAL_POSTAGE.equals(tryOutGood.getIs_freeShipping())) {
				holder2.separator.setBackgroundResource(imagBackground[1]);
			} else {
				LogUtil.outLogDetail("-------isFreeTag--error--");
			}
			break;
		case TYPE_ITEM:
			String temp_product = tryOutGood.getName();
			String temp_price = "价值:￥" + tryOutGood.getPrice();
			String temp_count = "";
			String temp_participate = "";
			int end;
			int start;
			if (Constant.TRIAL_FREE.equals(tryOutGood.getIs_freeShipping())) {
				temp_count = "限量:" + tryOutGood.getCount() + "件";
				temp_participate = "已申请:" + tryOutGood.getTryout_count() + "人";
				start = temp_count.indexOf(":") + 1;
				end = temp_count.indexOf("件");
				holder1.clock.setVisibility(View.VISIBLE);
				holder1.participant.setText(temp_participate);
			} else if (Constant.TRIAL_POSTAGE.equals(tryOutGood.getIs_freeShipping())) {
				String surplus_count = tryOutGood.getSurplus_count();
				temp_count = "剩余:" + surplus_count + "/" + tryOutGood.getCount() + "件";
				if (TextUtils.isEmpty(surplus_count)) { // 剩余件数为0 显示已结束
					surplus_count = "0";
				}
				if (Integer.parseInt(surplus_count) <= 0) {
					setEndStatus(holder1.state, R.drawable.trial_end_item);
				}
				temp_participate = "邮费:￥" + tryOutGood.getPostage();
				start = temp_count.indexOf(":") + 1;
				end = temp_count.indexOf("/");
				holder1.clock.setVisibility(View.INVISIBLE);
				holder1.participant.setText(ViewUtil.getTextSizeStyle(temp_participate, 3, 4, 11));
			} else {
				start = 0;
				end = 0;
			}
			holder1.product.setText(temp_product);
			holder1.price.setText(ViewUtil.getTextSizeStyle(temp_price, 3, 4, 11));
			holder1.count.setText(ViewUtil.getTextSizeStyle(ViewUtil.getTextColorStyle(temp_count,
					start, end, mContext.getResources().getColor(R.color.global_price_text_color)),
					start, end, 17));
			if (Constant.TRIAL_FREE.equals(tryOutGood.getIs_freeShipping())) {
				LogUtil.outLogDetail("----------------" + tryOutGood.getIs_freeShipping());
				setStatusImage(holder1.clock, holder1.state, R.drawable.trial_end_item); // 显示一已结束
			}
			BitmapOptionsFactory.getInstance(mContext)
					.display(holder1.image, tryOutGood.getPhoto());
			holder1.clock.setTextString("距离结束: ");
			holder1.clock.setEndTime(tryOutGood.getTime());
			break;
		default:
			break;
		}
		return convertView;
	}

	/**
	 * @discretion: 设置状态的图片
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午12:58:27
	 * @param clock
	 * @param state
	 * @param trialEndItem
	 */
	private void setStatusImage(final CustomDigitalClock clock, final ImageView state,
			final int trialEndItem) {
		clock.setClockListener(new ClockListener() {

			@Override
			public void timeEnd() {
				setEndStatus(state, trialEndItem);
				clock.setVisibility(View.INVISIBLE);
			}

		});
	}

	/**
	 * @discretion: 设置活动结束的状态
	 * @author: MaoYaNan
	 * @date: 2014-11-10 下午11:08:44
	 * @param state
	 * @param trialEndItem
	 */
	private void setEndStatus(final ImageView state, final int trialEndItem) {
		state.setImageResource(trialEndItem);
	}

	class ViewHolder1 {
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

	class ViewHolder2 {
		ImageView separator;

		public void reset() {
			separator.setImageDrawable(null);
		}
	}

	/**
	 * ScrollListener
	 * 
	 * @param view
	 * @param scrollState
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem); // 重新定义
		}
	}

	/**
	 * SectionIndexer
	 * 
	 * @return
	 */
	@Override
	public Object[] getSections() {
		Object[] array = new String[mTryGoodList.size()];
		mTryGoodList.toArray(array);
		return array;
	}

	/**
	 * 分组中的第一个位置
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		return sectionIndex + 1;
	}

	/**
	 * 根据位置划分分组
	 */
	@Override
	public int getSectionForPosition(int position) {
		int temp = mSeparatorsSet.floor(position) != null ? mSeparatorsSet.floor(position) : -1;
		return temp;
	}

	public int getNextSectionForPosition(int position) {
		return mSeparatorsSet.ceiling(position) != null ? mSeparatorsSet.ceiling(position)
				: mTryGoodList.size() - 1;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (mSeparatorsSet == null) { // section 数组
			return PINNED_HEADER_GONE;
		}
		if (realPosition < 0) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);// 得到此item所在的分组位置
		if (section == -1) {
			return PINNED_HEADER_GONE;
		}

		int nextSectionPosition = getNextSectionForPosition(realPosition);// 得到下一个分组的位置
		if (nextSectionPosition != -1 && realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		if (section != -1) {
			String is_freeShipping = mTryGoodList.get(position).getIs_freeShipping();

			((ImageView) header.findViewById(R.id.separator))
					.setBackgroundResource(imagBackground[Constant.TRIAL_FREE
							.equals(is_freeShipping) ? 0 : 1]);
		}
	}

}
