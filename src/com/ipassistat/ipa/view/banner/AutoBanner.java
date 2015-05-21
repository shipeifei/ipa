package com.ipassistat.ipa.view.banner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.adapter.BannerPagerAdapter;
import com.ipassistat.ipa.adapter.GoodsBannerAdapter;
import com.ipassistat.ipa.bean.response.Advertise;
import com.ipassistat.ipa.bean.response.entity.PicInfo;
import com.ipassistat.ipa.business.CropImageListener;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.ScreenInfoUtil;
import com.loopj.android.image.SmartImageView;

/***
 * 自定义的自动滚动Bannner
 * com.ipassistat.ipa.view.banner.AutoBanner
 * @author 时培飞 
 * Create at 2015-4-21 上午9:40:48
 */
public class AutoBanner extends RelativeLayout implements OnPageChangeListener {

	static String TAG = "AutoBanner";
	private Context mContext;
	private ViewPager mViewPager;
	private boolean mIsAutoScroll;
	private boolean isScroll;// 已经开始滚动，就不要重复发送了
	private List<View> mViewList;
	private CirclePageIndicator pageIndicator;

	private AQuery aQuery;

	// private BitmapUtils mBitmapUtil;

	public AutoBanner(Context context, Context mContext) {
		super(context);
		this.mContext = mContext;

		init();
	}

	public AutoBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		aQuery = new AQuery(getContext());
	}

	/**
	 * 设置banner 是否自动滚动。 此方法调用必须在initBarnners之前
	 * 
	 * @param isAutoScroll
	 */
	public void setAutoScroll(boolean isAutoScroll) {
		mIsAutoScroll = isAutoScroll;
	}

	public void initGoodsBanner(List<PicInfo> list) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.auto_view_pager_layout, this);

		// 从布局文件中获取ViewPager父容器
		LinearLayout pagerLayout = (LinearLayout) findViewById(R.id.viewpager_parent);
		// 创建ViewPager
		mViewPager = new ViewPager(mContext);

		int width = ScreenInfoUtil.getScreenWidth((Activity) mContext);
		int height = (int) (width * 0.5 + 100);

		android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(width, height);

		// 根据屏幕信息设置ViewPager广告容器的宽高
		mViewPager.setLayoutParams(new LayoutParams(width, height));
		// 将ViewPager容器设置到布局文件父容器中
		pageIndicator = (CirclePageIndicator) findViewById(R.id.pageindicator);
		pageIndicator.setRadius(10); // 设置圆点的半径
		pageIndicator.setGap(4); // 设置两个圆点间的距离
		mViewList = new ArrayList<View>();
		for (PicInfo pic : list) {
			// mBitmapUtil = new BitmapUtils(mContext);
			// mBitmapUtil.configDefaultLoadingImage(R.drawable.goodsdetail_default_img);//
			// 默认背景图片
			// mBitmapUtil.configDefaultLoadFailedImage(R.drawable.goodsdetail_default_img);//
			// 加载失败图片
			ImageView imageview = new ImageView(mContext);
			imageview.setScaleType(ScaleType.CENTER_CROP);
			// mBitmapUtil.display(imageview, pic.getPicNewUrl());

			// ImageLoader.getInstance().displayImage(pic.getPicNewUrl(),
			// imageview, BitmapOptionsFactory.getImageOption(), new
			// ImageLoaderCallbackFactory());

			BitmapOptionsFactory.getInstance(getContext()).display(imageview, pic.getPicNewUrl(), BitmapOptionsFactory.getOptionConfig(getContext()), new CropImageListener());

			mViewList.add(imageview);
		}

		mViewPager.setAdapter(new GoodsBannerAdapter(((Activity) mContext), mViewList));
		pageIndicator.setViewPager(mViewPager); // 注意： indicator 绑定
		// ViewPager必须在ViewPager设置adapter
		// 之后。
		pageIndicator.setOnPageChangeListener(this); // 注意， 该监听是设置在
		pagerLayout.removeAllViews();
		pagerLayout.addView(mViewPager);
		// Indicator上的，不是s
		Message msg = new Message();
		msg.arg1 = 0; // 当前的ViewPager item
		if (mIsAutoScroll) {
			handler.sendMessageDelayed(msg, 1000);
		}
	}

	public void relese() {
		if (mViewList != null) {
			for (Iterator iterator = mViewList.iterator(); iterator.hasNext();) {
				View view = (View) iterator.next();
				view = null;
			}
		}
	}

	public void initHomeFragmentBarnners(List<Advertise> list) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.auto_view_pager_layout, this);

		// 从布局文件中获取ViewPager父容器
		LinearLayout pagerLayout = (LinearLayout) findViewById(R.id.viewpager_parent);
		// 创建ViewPager
		mViewPager = new ViewPager(mContext);
		int width = ScreenInfoUtil.getScreenWidth((Activity) mContext);
		int height = (int) (width * 0.5);
		mViewPager.setLayoutParams(new LayoutParams(width, height));

		// 根据屏幕信息设置ViewPager广告容器的宽高
		// 将ViewPager容器设置到布局文件父容器中

		// ViewPager viewPager = (ViewPager) activity
		// .findViewById(R.id.viewPager);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.pageindicator);
		pageIndicator.setRadius(10); // 设置圆点的半径
		pageIndicator.setGap(4); // 设置两个圆点间的距离

		if (mViewList != null) mViewList.clear();
		mViewList = new ArrayList<View>();

		// BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// bitmapUtils.configDefaultLoadingImage(drawable.pop_post_bg_default);
		// bitmapUtils.configDefaultLoadFailedImage(drawable.pop_post_bg_default);

//		AQuery aQuery = new AQuery(getContext());

		for (Advertise advertise : list) {
			SmartImageView smartImageView = new SmartImageView(mContext);
			smartImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			smartImageView.setScaleType(ScaleType.FIT_XY);

			// drawable.pop_post_bg_defaultundRes//rce(drawable.pop_post_bg_default);

			aQuery.id(smartImageView).image(advertise.getAdImg(), true, true);
			// bitmapUtils.display(smartImageView, advertise.getAdImg());

			// smartImageView.setImageUrl(advertise.getAdImg(),
			// R.drawable.home_banner_bg_default,
			// R.drawable.home_banner_bg_default);

			mViewList.add(smartImageView);
		}

		mViewPager.setAdapter(new BannerPagerAdapter(((Activity) mContext), mViewList, list));

		pageIndicator.setViewPager(mViewPager); // 注意： indicator 绑定
		// ViewPager必须在ViewPager设置adapter
		// 之后。
		pageIndicator.setOnPageChangeListener(this); // 注意， 该监听是设置在

		pagerLayout.removeAllViews();
		pagerLayout.addView(mViewPager);
		// Indicator上的，不是s
		Message msg = new Message();
		msg.arg1 = 0; // 当前的ViewPager item

		if (mIsAutoScroll && !isScroll) {
			handler.sendMessageDelayed(msg, 1000);
		}
	}

	private Handler handler = new Handler() { // 设置 ViewPager的动画效果
		public void handleMessage(android.os.Message msg) {
			try {
				isScroll = true;

				int temp = msg.arg1;
				temp += 1;
				temp %= mViewList.size();
				Message msg1 = new Message();
				msg1.arg1 = temp;
				mViewPager.setCurrentItem(temp);
				sendMessageDelayed(msg1, 3000);
			} catch (Exception e) {
				// TODO: handle exception
			}

		};
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {/*
										 * 
										 * if(arg0>2){
										 * arg0=arg0%mViewList.size(); } c_id =
										 * arg0; for (int i = 0; i <
										 * imageViews.length; i++) {
										 * imageViews[arg0]
										 * .setBackgroundResource
										 * (R.drawable.guide_dot_white); if
										 * (arg0 != i) { imageViews[i]
										 * .setBackgroundResource
										 * (R.drawable.guide_dot_black); } }
										 */}

}
