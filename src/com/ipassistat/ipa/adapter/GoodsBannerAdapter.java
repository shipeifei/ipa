/*
 * Copyright 2014 trinea.cn All right reserved. This software is the
 * confidential and proprietary information of trinea.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with trinea.cn.
 */
package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 商品 bannerAdapter
 * 
 * @author lxc
 */
public class GoodsBannerAdapter extends PagerAdapter {

	private Context context;
	private List<View> imageIdList;
	

	public GoodsBannerAdapter(Context context, List<View> imageIdList) {
		this.context = context;
		this.imageIdList = imageIdList;
	}

	@Override
	public int getCount() {
		return imageIdList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = imageIdList.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView((View)object);
		container.removeView(imageIdList.get(position));
//		super.destroyItem(container, position, object);
	}
	
}
