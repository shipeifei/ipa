package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.PersonalInfoChooseAdapter;
import com.ipassistat.ipa.bean.response.entity.SkinType;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * @Discription： 个人信息选择界面，包括：性别选择，皮肤类型选择，期待改善方向选择
 * @package： com.ichsy.mboss.activity.PersonalInfoChoActivity
 * @author： MaoYaNan
 * @date：2014-11-12 下午4:45:22
 */
public class PersonalInfoChoActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private static final String TAG = "PersonalInfoActivity";
	private Context mContext;// 上下文
	private String mStringTitle; // 标题
	private ListView mList; // 数据列表
	private PersonalInfoChooseAdapter mAdapter;// 数据填充的adapter
	private TitleBar mTitlebar; // 标题栏
	private int mItemId = 0; // 选中的列表项
	private List<String> mData = new ArrayList<String>(); // 数据列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_choose);
		mContext = this;
		getIntentExtras();
		initViews(savedInstanceState); // 初始化，设置监听
		initDatas(); // 填充数据
		registeListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getString(R.string.sex).equals(mTitlebar)) {
			MobclickAgent.onPageStart("1010"); // 统计页面
			MobclickAgent.onResume(this); // 统计时长
		} else if (getString(R.string.skin_type).equals(mTitlebar)) {
			MobclickAgent.onPageStart("1009"); // 统计页面
			MobclickAgent.onResume(this); // 统计时长
		} else if (getString(R.string.city).equals(mTitlebar)) {
			MobclickAgent.onPageStart("1011"); // 统计页面
			MobclickAgent.onResume(this); // 统计时长
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (getString(R.string.sex).equals(mTitlebar)) {
			MobclickAgent.onPageEnd("1010"); // 保证 onPageEnd 在onPause 之前调用,因为
												// onPause 中会保存信息
			MobclickAgent.onPause(this);
		} else if (getString(R.string.skin_type).equals(mTitlebar)) {
			MobclickAgent.onPageEnd("1009"); // 保证 onPageEnd 在onPause 之前调用,因为
			// onPause 中会保存信息
			MobclickAgent.onPause(this);
		} else if (getString(R.string.city).equals(mTitlebar)) {
			MobclickAgent.onPageEnd("1011"); // 保证 onPageEnd 在onPause 之前调用,因为
			// onPause 中会保存信息
			MobclickAgent.onPause(this);
		}
	}

	/**
	 * @discretion: 获取接收的参数
	 * @author: MaoYaNan
	 * @date: 2014-11-12 下午4:42:11
	 */
	private void getIntentExtras() {
		Intent intent = getIntent(); // 得到传过来的参数
		mStringTitle = intent.getStringExtra(ConfigInfo.PERSONALINFOTITLE); // 页面的标题

		if (intent.getStringExtra(ConfigInfo.PERSONALINFOFLAG).equals("skin_type"))// 皮肤类型
		{
			// 获取传递的皮肤类型集合
			ArrayList<SkinType> skinList = (ArrayList<SkinType>) intent.getSerializableExtra(ConfigInfo.PERSONALINFOWHICH);
			String skinTypeId = intent.getStringExtra(ConfigInfo.PERSONALINFOCHOOSE);
			if (!skinTypeId.equals("-1")) {
				SkinType model = new SkinType();
				for (int i = 0; i < skinList.size(); i++) {
					model = skinList.get(i);
					if (model != null) {
						if (model.getSkin_code().equals(skinTypeId)) {
							mItemId = i;
							break;
						}
					}
				}
			} else {
				mItemId = 10000;
			}

			if (skinList != null) {
				for (SkinType skinType : skinList) {
					mData.add(skinType.getSkin_name());
				}
			}

		} else {
			Resources resources = getResources();
			int which = intent.getIntExtra(ConfigInfo.PERSONALINFOWHICH, R.array.sex); // 页面显示数据
			String seleceItem = intent.getStringExtra(ConfigInfo.PERSONALINFOCHOOSE);
			if (TextUtils.isEmpty(seleceItem)) {
				mItemId = 1;
			} else {
				if (seleceItem.equals("4497465100010002")) {// 男
					mItemId = 0;
				} else {
					mItemId = 1;
				}
			}

			String[] stringArray = resources.getStringArray(which);
			for (int i = 0; i < stringArray.length; i++) {
				mData.add(stringArray[i]);
			}
		}

	}

	/**
	 * @discretion: 给界面填充数据
	 * @author: MaoYaNan
	 * @date: 2014-11-12 下午4:42:48
	 */
	private void initDatas() {
		mTitlebar.setTitleText(mStringTitle);
		mAdapter = new PersonalInfoChooseAdapter(mContext, mData, mItemId);
		mList.setAdapter(mAdapter);
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				if (firstVisibleItem > 0) {

					ViewGroup.LayoutParams params = mList.getLayoutParams();
					((MarginLayoutParams) params).setMargins(0, 0, 0, 0); // 可删除
					mList.setLayoutParams(params);
				} else {
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

	/**
	 * @discretion: 初始化组件
	 * @author: MaoYaNan
	 * @date: 2014-11-12 下午4:43:06
	 * @param savedInstanceState
	 */
	private void initViews(Bundle savedInstanceState) {

		// TitleBar
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		// list
		mList = (ListView) findViewById(R.id.list);
	}

	/**
	 * @discretion: 注册监听器
	 * @author: MaoYaNan
	 * @date: 2014-11-12 下午4:43:38
	 */
	private void registeListener() {
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitlebar.setButtonClickListener(TitleBarButton.rightImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitlebar.setVisibility(TitleBarButton.rightImgv, View.GONE);

		mList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			ToastUtil.showToast(this, "back");
			break;
		case R.id.right_imgv:
			ToastUtil.showToast(this, "right");
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mAdapter.setSelectedItem(position);
		Intent data = new Intent();
		data.putExtra("choice", position);
		setResult(Activity.RESULT_OK, data); // 返回的数据是选择的 item的 position
		finish();
	}

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
