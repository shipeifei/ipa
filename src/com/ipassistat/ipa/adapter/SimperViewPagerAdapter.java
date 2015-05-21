package com.ipassistat.ipa.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 简易的ViewPager adapter
 * 
 * @author ShiPeiFei
 *
 */
public class SimperViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	private Map<Integer, Boolean> mDestroyMap;// 需要销毁的map

	public SimperViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.mFragments = fragments;
		this.mDestroyMap = new HashMap<Integer, Boolean>();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);

		if (mDestroyMap != null && mDestroyMap.get(position) != null) {
			super.destroyItem(container, position, object);
		}
	}

	/**
	 * 添加需要周期性销毁的item
	 * 
	 * @param position
	 */
	public void addDestroyItem(int position) {
		mDestroyMap.put(position, true);
	}

	/**
	 * 移除需要周期性销毁的item
	 * 
	 * @param position
	 */
	public void removeDesotroyItem(int position) {
		mDestroyMap.remove(position);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}
