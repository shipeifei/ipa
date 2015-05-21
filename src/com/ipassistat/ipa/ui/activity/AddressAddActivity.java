package com.ipassistat.ipa.ui.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.request.AddressAddRequest;
import com.ipassistat.ipa.bean.response.AddressAddResponse;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.view.AddressView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.AddressView.OnAddressCheckedListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/***
 * 
 * @author shipeifei
 *
 */
public class AddressAddActivity extends BaseActivity implements
		OnClickListener,OnAddressCheckedListener {
	
	private EditText mNameEt;
	private EditText mPhoneEt;
	private EditText mCityEt; // 省市区选择editText
	private EditText mPostCodeEt;
	private EditText mDistrictEt; // 详细地址
	private TitleBar mBar;
	private AddressView mAddressView; //地址选择的view
	private LinearLayout mLayout;
	private ProgressHub mHub;
	
	private String mIsDefaultStr; // 是否默认字符串
	private String mName;
	private String mCity;
	private String mDistrict;
	private String mPhone;
	private String mPostCode;
	private String mAreaCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_add);
		init();
		Intent intent = getIntent();
		mIsDefaultStr = intent.getStringExtra(IntentKey.IS_HAS_DEFAULT_ADDRESS);
		showAddressSelect();
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		MobclickAgent.onPageStart("1031"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobclickAgent.onPageEnd("1031"); // 保证 onPageEnd 在onPause 之前调用,因为
											// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	
	private void initTitleBar() {
		mBar = (TitleBar) findViewById(R.id.titlebar);
		String name = "新增地址";
		mBar.setTitleText(name);
		mBar.setVisibility(TitleBarButton.rightTextView, View.VISIBLE);
		mBar.setButtonClickListener(TitleBarButton.rightTextView, this);
		mBar.setButtonClickListener(TitleBarButton.leftImgv, this);
//		Drawable drawable = getResources().getDrawable(
//				R.drawable.personalinfo_save);
//		mBar.setImageBackGround(TitleBarButton.rightTextView, drawable);
		mBar.setRightTextViewText("保存");
	}
	
	private void init() {
		initTitleBar();
		mNameEt = (EditText) findViewById(R.id.et_name);
		mPhoneEt = (EditText) findViewById(R.id.et_phone_num);
		mPostCodeEt = (EditText) findViewById(R.id.et_post_code);
		mCityEt = (EditText) findViewById(R.id.et_city);
		mDistrictEt = (EditText) findViewById(R.id.et_district);
		mLayout=(LinearLayout) findViewById(R.id.layout);
		mHub=ProgressHub.getInstance(this);
		mAddressView=new AddressView(this, this);
		mLayout.setGravity(Gravity.BOTTOM);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mLayout.addView(mAddressView,params);
	}
	
	private void save(){
		 mName = mNameEt.getText().toString();
		 mCity = mCityEt.getText().toString();
		 mDistrict = mDistrictEt.getText().toString();
		 mPhone = mPhoneEt.getText().toString();
		 mPostCode = mPostCodeEt.getText().toString();
		mAddressView.checkAddress(mName, mCity, mDistrict, mPhone, mPostCode);
	}
	
	@Override
	public void checkedSuccess() {
		
		AddressAddRequest req = new AddressAddRequest();
		String isDefault = null;
		if (mIsDefaultStr.equals(IntentFlag.HAS_DEFAULT_ADDR_FLAG)) {
			isDefault = "1"; // 有默认收货地址
		} else {
			isDefault = "0"; // 没有收获地址
		}
		mHub.showWithNoTouch("");
		req.setName(mNameEt.getText().toString());
		req.setIsDefault(isDefault);
		req.setProvince(mCity);
		req.setAddress(mDistrict);
		req.setPhone(mPhone);
		req.setPostcode(mPostCode);
		req.setAreaCode(mAreaCode);
		req.setIsDefault("0");
		UserModule module = new UserModule(this);
		module.getDataByPost(this, ConfigInfo.API_ADDRESS_SAVE, req,
				AddressAddResponse.class);
	}

	@Override
	public void getCurrentDataByConfirm(String provName, String cityName,
			String areaName, String areaCode) {
		
		this.mAreaCode=areaCode;
		String str=null;
		if(cityName.equals("市辖区")||cityName.equals("县")){
			str=provName+areaName;
		}else{
			str=provName+cityName+areaName;
		}
		mCityEt.setText(str);
		mCityEt.clearFocus();
		mLayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.right_textview:
			save();
			break;
		case R.id.left_imgv:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		
		if (url.equals(ConfigInfo.API_ADDRESS_SAVE)) {
			mHub.dismiss();
			finish();
		}
	}
	
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		
		super.onMessageFailedCalledBack(url, object);
		mHub.dismiss();
	}
	
	@Override
	public void onNoTimeOut() {
		
		super.onNoTimeOut();
		mHub.dismiss();
	}
	
	
	 /*
	  *  监听焦点，显示或者隐藏地区选择视图
	  */
		private void showAddressSelect() {

			mCityEt.setInputType(InputType.TYPE_NULL); // 不弹出输入法软键盘

			mCityEt.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					
					if (hasFocus) {
						InputMethodManager imm = (InputMethodManager) getSystemService(AddressAddActivity.this.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mCityEt.getWindowToken(), 0);
						mLayout.setVisibility(View.VISIBLE);
					} else {
						mLayout.setVisibility(View.GONE);
					}
				}
			});
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
