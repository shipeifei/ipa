/*
 * ImagePagerAdapter.java [V 1.0.0]
 * classes : com.ichsy.mboss.adapter.ImagePagerAdapter
 * 时培飞 Create at 2014-11-24 上午10:41:15
 */
package com.ipassistat.ipa.adapter;

import java.util.ArrayList;

import android.R.anim;
import android.R.integer;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.color;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.ui.activity.SisterGroupPostImageDetailActivity;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.view.imagescroller.ImageShowViewPager;
import com.ipassistat.ipa.view.imagescroller.TouchImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * com.ichsy.mboss.adapter.ImagePagerAdapter
 * 
 * @author 时培飞 姐妹圈帖子详情图片适配器 Create at 2014-11-24 上午10:41:15
 */
public class ImagePagerAdapter extends PagerAdapter {
	Context mContext;
	ArrayList<String> mImgsUrl;// 帖子所有的图片
	LayoutInflater mInflater = null;
	String from = "";
	int mCurrentPage = -1;

	public ImagePagerAdapter(Context context, ArrayList<String> imgsUrl, String from, int currentPage) {
		this.mContext = context;
		this.mImgsUrl = imgsUrl;
		this.from = from;
		this.mCurrentPage = currentPage;
		mInflater = LayoutInflater.from(context);

	}

	/** 动态加载数据 */
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);

		((ImageShowViewPager) container).mCurrentView = ((TouchImageView) ((View) object).findViewById(R.id.full_image));
	}

	@Override
	public int getCount() {
		return mImgsUrl == null ? 0 : mImgsUrl.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {

		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// view内控件
		TouchImageView full_image;// 自定义图片控件主要针对缩放
		View view = mInflater.from(mContext).inflate(R.layout.acvivity_sistergroup_imageshow_item, null);
		full_image = (TouchImageView) view.findViewById(R.id.full_image);
		final RelativeLayout txt = (RelativeLayout) SisterGroupPostImageDetailActivity.mActivity.findViewById(R.id.title_bar);

		// 隐藏头部菜单
		full_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (from.equals("SisterGroupDetailAcvitity")) {

					if (txt.getVisibility() == View.GONE) {
						txt.setVisibility(View.VISIBLE);
					} else {
						txt.setVisibility(View.GONE);
					}
				} else if (from.equals("GoodsDetailAcvitity")) {
					SisterGroupPostImageDetailActivity.mActivity.finish();
					SisterGroupPostImageDetailActivity.mActivity.overridePendingTransition(anim.fade_in, anim.fade_out);

				}

			}
		});
		full_image.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (from.equals("GoodsDetailAcvitity")) {
					MobclickAgent.onEvent(mContext, "1166");
				}
				return false;
			}
		});

		// 缓存图片

		BitmapOptionsFactory.newInstanceBitmapUtils(mContext, color.black).display(full_image, mImgsUrl.get(position));

		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
