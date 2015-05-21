package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;

import android.R.anim;
import android.R.integer;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.ImagePagerAdapter;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.FileUtis;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.imagescroller.ImageShowViewPager;
import com.umeng.analytics.MobclickAgent;

/**
 * 显示单张图片
 * 
 * @author LiuYuHang
 * @date 2014年10月8日
 * @date 修改人:时培飞 修改于:2014-11-24 修改原因:增加图片轮播效果
 * 
 */
public class SisterGroupPostImageDetailActivity extends BaseActivity implements OnClickListener {

	/** 选择的图片路径成员变量 */
	private String mImageUrl;
	/** 图片展示成员变量 */
	private ImageShowViewPager mImagePager;
	/** 帖子所有图片列表集合成员变量 */
	private ArrayList<String> mImgUrlList;
	/** PagerAdapter适配器成员变量 */
	private ImagePagerAdapter mAdapter;
	/** 头部菜单 */
	private RelativeLayout mTitleBar;

	public static SisterGroupPostImageDetailActivity mActivity;
	/** 跳转来源 */
	private String mFrom;
	/***
	 * 当前显示的图片索引
	 */
	private int mCurrentIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 横竖屏切换数据的保存
		if (savedInstanceState != null) {
			mImageUrl = savedInstanceState.getString("mImageUrl");
			mImgUrlList = savedInstanceState.getStringArrayList("mImgUrlList");
		}
		setContentView(layout.acvitity_sistergroup_imageshow);
		mActivity = SisterGroupPostImageDetailActivity.this;
		disableSlideClose();

		// 初始化控件和初始化数据
		initView();
		initData();
		initViewPager();
		
		// 添加控件的事件
		findListeners(this, id.button_save, id.left_imgv, id.right_imgv);
		
		
		// 添加友盟统计
		MobclickAgent.onEvent(getApplicationContext(), "1049");
	}

	/**
	 * 横竖屏切换保存图片集合以及当前浏览的图片
	 * 
	 * @date 增加人:时培飞 修改于:2014-11-24
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// 浏览的当前图片路径
		outState.putString("mImageUrl", mImageUrl);
		// 图片集合
		outState.putStringArrayList("mImgUrlList", mImgUrlList);
	}

	/**
	 * 初始化页面按钮
	 * 
	 * @author 时培飞 Create at 2014-11-24 上午11:09:31
	 */
	@Override
	protected void initView() {
		mImagePager = (ImageShowViewPager) findViewById(R.id.image_pager);
		mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
		// 滑动事件
		mImagePager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int currentPosition) {

				setTitleText((currentPosition + 1) + "/" + mImgUrlList.size());
				if (mCurrentIndex > 0) {
					mTitleBar.setVisibility(View.VISIBLE);
				} else {
					mTitleBar.setVisibility(View.GONE);
				}
				mCurrentIndex = -1;
				mImageUrl = mImgUrlList.get(currentPosition);

			}

			@Override
			public void onPageScrolled(int currentPosition, float arg1, int arg2) {
				

			}

			@Override
			public void onPageScrollStateChanged(int currentPosition) {
				

			}
		});
	}

	/**
	 * 初始化图片数据
	 * 
	 * @author 时培飞 Create at 2014-11-24 上午11:28:47
	 */
	@Override
	protected void initData() {
		// 判断是否横竖屏切换
		if (mImgUrlList == null) {
			// 获取参数图片集合
			mImgUrlList = getIntent().getStringArrayListExtra(IntentUtil.INTENT_URL_OF_IMAGEURLS);
		}
		if (mImageUrl == null) {
			// 获取参数点击的图片
			mImageUrl = getIntent().getStringExtra(IntentUtil.INTENT_URL_OF_IMAGE);
		}
		mFrom = getIntent().getStringExtra(IntentUtil.INTENT_FROM);
		// 设置当前图片页数,如果不存在默认为1
		short index = (short) mImgUrlList.indexOf(mImageUrl);
		if (index <= 0) {
			index = 1;
		}
		// 判断跳转来源，从而决定试图实现类型
		if (mFrom.equals("SisterGroupDetailAcvitity")) {
			setTitleRightText("保存", this);
			// 设置标题内容
			setTitleText(String.valueOf(index) + "/" + mImgUrlList.size());
		} else if (mFrom.equals("GoodsDetailAcvitity")) {
			mTitleBar.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化viewpager控件
	 * 
	 * @author 时培飞 Create at 2014-11-24 上午11:29:14
	 */
	private void initViewPager() {
		// 指定显示的图片索引,如果没有获取到图片索引默认为第一张图片
		short index = (short) mImgUrlList.indexOf(mImageUrl);
		mCurrentIndex = index;
		if (index < 0) {
			index = 0;
		}
		setTitleText((index + 1) + "/" + mImgUrlList.size());
		if (mImgUrlList != null && mImgUrlList.size() != 0) {
			mAdapter = new ImagePagerAdapter(getApplicationContext(), mImgUrlList, mFrom, index);
			mImagePager.setAdapter(mAdapter);
			mImagePager.setCurrentItem(index);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.left_imgv:// 返回帖子详细页面
			MobclickAgent.onEvent(getApplicationContext(), "1130");
			finish();
			overridePendingTransition(anim.fade_in, anim.fade_out);
			break;
		case id.right_textview:// 下载图片
			MobclickAgent.onEvent(getApplicationContext(), "1154");
			ImageView imageView = (ImageView) findViewById(id.full_image);
			// 图片保存路径
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + ConfigInfo.DEFAULT_DISK_DIR_PHOTO;
			// 截取图片名称
			String fileName = mImageUrl.substring(mImageUrl.lastIndexOf("/"), mImageUrl.length());
			// 获取图片字节
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				FileUtis.drawableTofile(drawable, path, fileName);
				ToastUtil.showToast(getApplicationContext(), "保存成功，路径：" + path + fileName);
			} else {
				ToastUtil.showToast(getApplicationContext(),getString(string.loadingimages));
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImagePager = null;
		mAdapter = null;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
