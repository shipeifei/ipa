package com.ipassistat.ipa.view.autoviewpager;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends PagerAdapter {
	private List<View> item;

	public MyViewPagerAdapter(List<View> item, Context context) {
		this.item = item;
	}

	@Override
	public int getCount() {
		return item.size();
//		int size = 0;
//		if (item.size() == 1) {
//			size = 1;
//		} else {
//			size = Integer.MAX_VALUE;
//		}
//		return size;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(item.get(position % item.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = this.item.get(position % item.size());
		try {
			((ViewPager) container).addView(view, 0);
		} catch (Exception ex) {
		}
		return view;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(android.os.Parcelable state, ClassLoader loader) {

	};

	@Override
	public Parcelable saveState() {
		return null;
	}

	public void clearAll() {
		for (View view1 : item) {
			view1 = null;
		}
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
