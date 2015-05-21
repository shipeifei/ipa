package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.AddressReceiveAdapter;
import com.ipassistat.ipa.bean.response.AddresssReceiveResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/***
 * 
 * @author shipeifei
 *
 */
public class AddressReceiveActivity extends BaseActivity implements OnClickListener {
	private TitleBar mTitleBar;// 标题栏
	private ListView mListView;// 收货地址列表
	private Context mContext;
	private View mNoAddress; // 无收货地址的显示页面

	private List<BeautyAddress> mList; // 地址列表

	private String flagForActivity; // 从别的Activity跳转过来的标识

	// private boolean isOperation=false; //判断当前页面用户是否操作 true为操作 false不好操作

	// private String
	// addrRecApiName="com_cmall_newscenter_beauty_api_GetAddress"; //获取收货地址接口
	private ProgressHub pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_receive);
		init();
		initListener();
		pro = ProgressHub.getInstance(this);
		@SuppressWarnings("unused")
		Intent intent = getIntent();
		// flagForActivity=intent.getStringExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY);
		getNameFromActivity();
		addItemClickListener();
		pro.show("正在加载");
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobclickAgent.onPageStart("1030"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		// 调取收货地址列表，取出默认收货地址，保存到本地
		getAddressReceive();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobclickAgent.onPageEnd("1030"); // 保证 onPageEnd 在onPause 之前调用,因为
											// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * 注册监听
	 */
	private void initListener() {
		mTitleBar.setButtonClickListener(TitleBarButton.leftImgv, this);
		mTitleBar.setButtonClickListener(TitleBarButton.rightImgv, this);
	}

	/**
	 * 初始化
	 */
	private void init() {
		initTitleBar();
		mContext = this;
		mListView = (ListView) findViewById(R.id.listview);// 收货地址列表
		mNoAddress = findViewById(R.id.no);
	}

	private void initTitleBar() {
		String title = "收货地址";
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);// 标题栏
		mTitleBar.setTitleText(title);// 设置文字标题
		mTitleBar.setVisibility(TitleBarButton.rightTextView, View.VISIBLE);
		mTitleBar.setButtonClickListener(TitleBarButton.rightTextView, this);
		 mTitleBar.setRightTextViewText("新增");
		//Drawable drawable = getResources().getDrawable(R.drawable.icon_address_add);
		//mTitleBar.setImageBackGround(TitleBarButton.rightTextView, drawable);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (ConfigInfo.addrRecApiName.equals(url)) {
			// ToastUtil.showToast(this, "获取列表成功");
			pro.dismiss();
			AddresssReceiveResponse resp = (AddresssReceiveResponse) object;

			// 将默认收货地址保存到本地
			UserModule module = new UserModule(this);
			module.saveUserDefaultAdd(this, resp);

			mList = resp.getAdress();

			if (mList == null || mList.size() == 0) {
				mNoAddress.setVisibility(View.VISIBLE);
			} else {
				mNoAddress.setVisibility(View.GONE);
				mListView.setAdapter(new AddressReceiveAdapter(this, mList, resp));
			}

			boolean flag = false; // 判断收货地址列表中有没有默认收货地址的标识 true为有 ，false为没有

			if (mList != null) {
				// 如果地址列表中没有默认收货地址，则删除本地保存的默认地址
				for (int i = 0; i < mList.size(); i++) {
					BeautyAddress beau = mList.get(i);
					if (beau.getIsdefault().equals("1")) {
						flag = true;
						return;
					} else {
						flag = false;
					}
				}
			}

			if (flag == false) {
				// Log.d("BBB", "没有默认收货地址，删除本地保存的收货地址");
				module.clearUserDeafaultAdd(mContext);
			}
		}

		if (url.equals(ConfigInfo.API_POST_SET_DEFAULT_ADDRESS)) {
			// ToastUtil.showToast(this, "成功了。。。。。。。。。。在activity中");
			Constant.IS_OPERATION_FLAG = true;
			finish();
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
	}

	@Override
	public void onNoNet() {
		
		super.onNoNet();
	}

	@Override
	public void onNoTimeOut() {
		
		super.onNoTimeOut();
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			MobclickAgent.onEvent(mContext, "1091");
			// 返回
			if (!TextUtils.isEmpty(flagForActivity)) {
				if (flagForActivity.equals(IntentFlag.TRAOUTINFO)) {
					intentToTrialInfoActivity();
				}
			}
			finish();
			break;
		case R.id.right_textview:
			MobclickAgent.onEvent(mContext, "1089");
			// 点击新增按钮跳转到新增地址页面
			Intent intentnewaddress = new Intent(mContext, AddressAddActivity.class);
			String flag = null;
			if (mList != null) {
				if (mList.size() == 0) {
					// 没有收获地址
					flag = IntentFlag.NO_DEFAULT_ADDR_FLAG;
				} else {
					flag = IntentFlag.HAS_DEFAULT_ADDR_FLAG;
				}
			}
			intentnewaddress.putExtra(IntentKey.IS_HAS_DEFAULT_ADDRESS, flag);
			startActivity(intentnewaddress);
			break;

		default:
			break;
		}
	}

	/**
	 * 获取收货地址列表
	 */
	private void getAddressReceive() {
		UserModule module = new UserModule(this);
		module.getAddressList(this);
	}

	/**
	 * listview添加监听器
	 */
	private void addItemClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				BeautyAddress address = mList.get(position);
				if (!TextUtils.isEmpty(flagForActivity)) {
					if (flagForActivity.equals(IntentFlag.TRAOUTINFO)) {
						setDefaultAddress(address);
						intentToTrialInfoActivity();
					} else if (flagForActivity.equals(IntentFlag.ORDER_CONFIRM_FLAG)) {
						Intent data = new Intent();
						data.putExtra(IntentKey.ADDRRECE_TO_ORDERCON__KEY, address);
						setResult(RESULT_OK, data);
						finish();
					} else {
						setDefaultAddress(address);
					}
				}
			}
		});
	}

	private void setDefaultAddress(BeautyAddress address) {
		UserModule module = new UserModule(AddressReceiveActivity.this);
		module.saveUserDefaultAdd(AddressReceiveActivity.this, address);
		module.postDefaultAddresss(AddressReceiveActivity.this, address.getId());
	}

	/**
	 * 得到跳转过来的activity的信息
	 */
	private void getNameFromActivity() {
		Intent intent = getIntent();
		flagForActivity = intent.getStringExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY);
	}

	/**
	 * 根据getIntent过来的flag标识判断是否跳转到TrialInfoActivity
	 */
	private void intentToTrialInfoActivity() {

		Intent intent = new Intent(AddressReceiveActivity.this, TrialInfoActivity.class);
		intent.putExtra(IntentFlag.TRIAL_INFO_FLAG_FROM_ACTIVITY, IntentFlag.ADDRESS_RECEIVE_ACTIVITY);
		intent.putExtra(IntentFlag.IS_OPERATION_FLAG, Constant.IS_OPERATION_FLAG); // 向TrialInfoActivity页面发送判断当前页面是否有操作的标识
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!TextUtils.isEmpty(flagForActivity)) {
				if (flagForActivity.equals(IntentFlag.TRAOUTINFO)) {
					intentToTrialInfoActivity();
				}
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
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
