package com.ipassistat.ipa.ui.activity;
/*
 * PersonalSkinExptActivity.java [V 1.0.0]
 * classes : com.ichsy.mboss.activity.PersonalSkinExptActivity
 * 时培飞 Create at 2014-11-25 下午1:21:24
 
package com.ichsy.mboss.ui.activity;

import java.util.ArrayList;
import java.util.List;
import com.ichsy.mboss.R;
import com.ichsy.mboss.adapter.PersonalSkinExptAdapter;
import com.ichsy.mboss.bean.response.entity.SkinHopeful;
import com.ichsy.mboss.constant.ConfigInfo;
import com.ichsy.mboss.constant.RequestCodeConstant;
import com.ichsy.mboss.util.StringUtil;
import com.ichsy.mboss.view.TitleBar;
import com.ichsy.mboss.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

*//**
 * com.ichsy.mboss.activity.PersonalSkinExptActivity
 * 
 * @Discription:护肤需求设置页面，做为单独页面是因为需要实现多选功能
 * @author 时培飞 Create at 2014-11-25 下午1:21:24
 *//*
public class PersonalSkinExptActivity extends BaseActivity implements
		OnItemClickListener {

	*//** 上下文 *//*
	private Context mContext;
	*//** 数据列表 *//*
	private ListView mList;
	*//** 数据填充的adapter *//*
	private PersonalSkinExptAdapter mAdapter;
	*//** 标题栏 *//*
	private TitleBar mTitlebar;
	*//** 选中的列表项 *//*
	private ArrayList<String> mItemId = new ArrayList<String>();
	*//** 数据列表 *//*
	private List<String> mData = new ArrayList<String>();
	*//** 护肤需求 *//*
	private ArrayList<SkinHopeful> mOldData = new ArrayList<SkinHopeful>();
	*//** 用户选择的护肤需求 *//*
	private String mHopefulTypeIdOld = "-1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_skinexpt);
		mContext = this;
		getIntentExtras();
		initViews(savedInstanceState); // 初始化，设置监听
		initDatas(); // 填充数据
		registeListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		MobclickAgent.onPageStart("1011"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长

	}

	@Override
	protected void onPause() {
		super.onPause();

		MobclickAgent.onPageEnd("1011"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);

	}

	*//**
	 * @discretion: 获取接收的参数
	 * @author: 时培飞
	 * @date: 2014-11-12 下午4:42:11
	 *//*
	private void getIntentExtras() {
		Intent intent = getIntent(); // 得到传过来的参数
		mOldData = (ArrayList<SkinHopeful>) intent
				.getSerializableExtra(ConfigInfo.PERSONALINFOSKINEXPLAIN);
		mHopefulTypeIdOld = intent
				.getStringExtra(ConfigInfo.PERSONALINFOSKINEXPLAINSELECT);

		// 处理用户选择的
		if (!mHopefulTypeIdOld.equals("-1")) {// 用户选择不为空
			String[] result = StringUtil.getArrayFromString(mHopefulTypeIdOld,
					",");
			if (result != null)// 用户选择了多个护肤需求需要拆分字符串
			{

				for (int i = 0; i < result.length; i++) {

					mItemId.add(result[i]);

				}

			} else {
				mItemId.add(mHopefulTypeIdOld);
			}
		}
	}

	*//**
	 * @discretion: 给界面填充数据
	 * @author: 时培飞
	 * @date: 2014-11-12 下午4:42:48
	 *//*
	private void initDatas() {
		mTitlebar.setTitleText(getString(R.string.city));// 设置标题
		mAdapter = new PersonalSkinExptAdapter(mContext, mOldData, mItemId);
		mList.setAdapter(mAdapter);
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
                if(firstVisibleItem>0)
                {
                
                	ViewGroup.LayoutParams params = mList.getLayoutParams();  
                	((MarginLayoutParams) params).setMargins(0, 0, 0, 0); // 可删除  
                	mList.setLayoutParams(params);
                }
                else {
                	ViewGroup.LayoutParams params = mList.getLayoutParams();  
                	((MarginLayoutParams) params).setMargins(0, 22, 0, 0); // 可删除  
                	mList.setLayoutParams(params);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				
				// 每一条数据都是一个Map
				switch (scrollState) {
				case SCROLL_STATE_FLING:

					break;
				case SCROLL_STATE_IDLE:

					break;
				case SCROLL_STATE_TOUCH_SCROLL:

					break;
				}
			}

		});

	}

	*//**
	 * @discretion: 初始化组件
	 * @author: 时培飞
	 * @date: 2014-11-12 下午4:43:06
	 * @param savedInstanceState
	 *//*
	private void initViews(Bundle savedInstanceState) {

		// TitleBar
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		// list
		mList = (ListView) findViewById(R.id.list);

		Drawable drawable = getResources().getDrawable(
				R.drawable.personalinfo_save);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		mTitlebar.setImageBackGround(TitleBarButton.rightTextView, drawable);
		// mTitlebar.setVisibility(TitleBarButton.rightTextView, View.VISIBLE);

	}

	*//**
	 * @discretion: 注册监听器
	 * @author: 时培飞
	 * @date: 2014-11-12 下午4:43:38
	 *//*
	private void registeListener() {
		// 返回按钮注册事件
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent data = new Intent();
						data.putStringArrayListExtra("choice",
								mItemId.size() > 0 ? mItemId : null);
						data.putStringArrayListExtra(
								"selectName",
								mAdapter.mSeleceName.size() > 0 ? mAdapter.mSeleceName
										: null);
						data.putExtra("updateFlag", "1");
						setResult(RequestCodeConstant.CHOOSEEXPT, data); // 返回的数据是选择的
																			// 数据集
						finish();
					}
				});
		// 保存按钮注册事件
		mTitlebar.setButtonClickListener(TitleBarButton.rightTextView,
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent data = new Intent();
						data.putStringArrayListExtra("choice",
								mItemId.size() > 0 ? mItemId : null);
						data.putStringArrayListExtra(
								"selectName",
								mAdapter.mSeleceName.size() > 0 ? mAdapter.mSeleceName
										: null);
						data.putExtra("updateFlag", "1");
						setResult(RequestCodeConstant.CHOOSEEXPT, data); // 返回的数据是选择的
																			// 数据集
						finish();
					}
				});
		mList.setOnItemClickListener(this);
	}

	*//**
	 * 截获手机的返回键
	 * 
	 * @author 时培飞 添加人：时培飞 添加时间:2014-12-15
	 * @discretion :截获手机的返回键,保存选择的选项
	 *//*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			
			
			
			Intent data = new Intent();
			data.putStringArrayListExtra("choice",
					mItemId.size() > 0 ? mItemId : null);
			data.putStringArrayListExtra(
					"selectName",
					mAdapter.mSeleceName.size() > 0 ? mAdapter.mSeleceName
							: null);
			data.putExtra("updateFlag", "1");
			setResult(RequestCodeConstant.CHOOSEEXPT, data); // 返回的数据是选择的
																// 数据集
			finish();
			
		}
		return false;
	}

	*//**
	 * listview点击事件
	 *//*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final SkinHopeful hopeful;
		hopeful = mOldData.get(position);

		mItemId = mAdapter.setSelectedItem(hopeful.getHopeful_code(),
				hopeful.getHopeful_name());

	}

}
*/