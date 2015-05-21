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
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ipassistat.ipa.bean.response.Advertise;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * ImagePagerAdapter
 * 
 * @author lxc
 */
public class BannerPagerAdapter extends PagerAdapter {

	private Context context;
	private List<View> imageIdList;
	private List<Advertise> mAdvertise;

	public BannerPagerAdapter(Context context, List<View> imageIdList,
			List<Advertise> advertise) {
		this.context = context;
		this.imageIdList = imageIdList;
		this.mAdvertise = advertise;
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
		final Advertise advertise = mAdvertise.get(position);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					if (advertise.getAdImg_url().startsWith("code@@")) {
						/*Intent intent = new Intent();
						String[] s = advertise.getAdImg_url().split("code@@");
						intent.putExtra(IntentKey.GOODS_ID, s[1]);
						intent.putExtra(IntentKey.GOODS_TYPE,
								advertise.getProductType());
						intent.setClass(context, GoodsDetailActivity.class);
						context.startActivity(intent);
						MobclickAgent.onEvent(context, "1211",s[1]);*/
					} else {
						String[] s = advertise.getAdImg_url().split("url@@");
						ShareInfoEntity shareEntity = new ShareInfoEntity();
						shareEntity.setTitle(advertise.getShare_title());
						shareEntity.setContent(advertise.getShare_cotent());
						shareEntity.setPicUrl(advertise.getShare_pic());
						shareEntity.setTargetUrl(s[1]);
						shareEntity.setSMSContent(advertise.getShare_cotent());
						
						IntentUtil.startWebView(context, "专题", s[1], "1084",
								shareEntity);
					}
				} catch (Exception e) {
					LogUtil.outLogDetail(e.getMessage());
				}

			}
		});
		container.addView(view);
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(imageIdList.get(position));
	}
}
