package com.ipassistat.ipa.ui.welcome.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.ui.activity.BaseActivity;
import com.ipassistat.ipa.ui.activity.MainActivity;
import com.ipassistat.ipa.util.SharedPreferenceUtil;

/***
 * 
 * com.ipassistat.ipa.ui.activity.GuideActivity
 * 
 * @author 时培飞 Create at 2015-4-30 下午1:21:49
 */
public class GuideActivity extends BaseActivity implements OnClickListener {

	private static final String ISFIRST_NAME = "ISFIRST_NAME";
	private Boolean sencondLogin = false;// 是否已经安装过 默认为false
	private ViewPager mViewPager;
	private ArrayList<View> list;// 存放View的集合
	private View mView;// 引导图

	// viewpaper的布局集合
	private int[] layoutItems = { R.layout.item_guide_one, R.layout.item_guide_two, R.layout.item_guide_three };
	// viewpaper布局相对应的图片
	private int[] layoutItemsImages = { drawable.guide_one_new, drawable.guide_two_new, drawable.guide_three_new };

	private ImageView start_btn;
	private LayoutInflater mInflater;
	private Context mContext;

	private SparseArray<SoftReference<Drawable>> mGuideDrawableMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		init();
		disableSlideClose();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mContext = getApplicationContext();
		sencondLogin = SharedPreferenceUtil.getBooleanInfo(mContext, ISFIRST_NAME);

		if (sencondLogin) {
			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			mInflater = LayoutInflater.from(mContext);
			mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
			initViewPager(mInflater);

		}
	}

	private SoftReference<Drawable> getBitmap(int position) {
		if (mGuideDrawableMap == null) {
			mGuideDrawableMap = new SparseArray<SoftReference<Drawable>>();
		}
		SoftReference<Drawable> reference = mGuideDrawableMap.get(position);

		if (reference == null) {

			reference = new SoftReference<Drawable>(getResources().getDrawable(layoutItemsImages[position]));
			mGuideDrawableMap.put(position, reference);

		}
		return reference;
	}

	@SuppressLint("InflateParams")
	private View getLayout(int position) {

		return mInflater.inflate(layoutItems[position], null);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 释放资源
		if (mGuideDrawableMap != null && mGuideDrawableMap.size() > 0) {
			for (int i = 0; i < mGuideDrawableMap.size(); i++) {
				SoftReference<Drawable> reference = mGuideDrawableMap.valueAt(i);
				if (reference != null && reference.get() != null) {
					reference.get().setCallback(null);
				} else {
					continue;
				}
			}
		}

	}

	/**
	 * 初始化ViewPager
	 * 
	 * @param inflater
	 */
	@SuppressLint("InflateParams")
	private void initViewPager(LayoutInflater inflater) {
		// 实例化装视图的集合
		list = new ArrayList<View>();

		for (int i = 0; i < layoutItems.length; i++) {
			mView = inflater.inflate(layoutItems[i], null);
			list.add(mView);
		}
		mViewPager.setAdapter(pagerAdapter);

	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_btn:
			setGuid();
			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置已安装过
	 */
	private void setGuid() {
		SharedPreferenceUtil.putBooleanInfo(mContext, ISFIRST_NAME, true);
	}

	/**
	 * ViewPager 适配器
	 */
	PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public void destroyItem(android.view.ViewGroup container, int position, Object object) {
			// container.removeView(container.getChildAt(position));
		};

		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		};

		@SuppressWarnings("deprecation")
		public Object instantiateItem(android.view.ViewGroup container, int position) {
			View rootView = getLayout(position);
			View v;
			v = rootView.findViewById(id.start_btn);
			if (v != null)
				v.setOnClickListener(GuideActivity.this);
			rootView.setBackgroundDrawable(getBitmap(position).get());

			container.addView(rootView);
			return rootView;
		};
	};

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

}
