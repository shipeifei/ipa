package com.ipassistat.ipa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.AddresssReceiveResponse;
import com.ipassistat.ipa.bean.response.LoginResponse;
import com.ipassistat.ipa.bean.response.UserInfoResponse;
import com.ipassistat.ipa.bean.response.entity.UserInfoSaveEntity;
import com.ipassistat.ipa.business.HmlShoppingCartController;
import com.ipassistat.ipa.business.LoginController;
import com.ipassistat.ipa.business.LoginController.LoginOperation;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.InputTypeInfo;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentFlag.IdentifyCodeType;
import com.ipassistat.ipa.constant.IntentFlag.LoginType;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/***
 * 
 * com.ipassistat.ipa.ui.activity.LoginActivity
 * @author 时培飞 
 * Create at 2015-4-30 下午5:09:20
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	/** 用户EditText */
	private EditText mAccountEditText;
	/** 用户密码 */
	private EditText mPwdEditText;
	/** 登录状态TextView */
	private TextView mLoginTextView;
	/** 登录状态背景Layout */
	private LinearLayout mLoginLayout;
	/** 忘记密码 */
	private TextView mForgetPwdTextView;
	/** 注册 */
	private TextView mRegisterTextView;
	/** 显示密码 */
	private ImageView mShowPwdImg;
	/** 显示密码状态 false为不显示,true为显示 */
	private boolean isShowPwd;
	/** 用户中心模块 */
	private UserModule mUserModule;
	/** 用户 */
	private String mAccount = "";
	/** 用户信息保存类 */
	private UserInfoSaveEntity mUserSaveInfo;
	/** 登录注册模块控制器 */
	private LoginController mController;
	/** 登录回调接口 */
	LoginOperation mOperation;
	/** 登录界面 */
	private LoginActivity mLoginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUserModule = new UserModule(this);
		mUserSaveInfo = new UserInfoSaveEntity();
		mLoginActivity = this;
		mOperation = new LoginOperation() {

			public void showLoginLoading() {
				mLoginLayout.setSelected(true);
				findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
				mLoginTextView.setText(getString(R.string.logining));
			}

			public void dismissLoginLoading() {
				mLoginLayout.setSelected(false);
				findViewById(R.id.progress_bar).setVisibility(View.GONE);
				mLoginTextView.setText(getString(R.string.login));
			}

			@Override
			public void sendLoginReq(String loginName, String pwd) {
				mUserModule.login(mLoginActivity, loginName, pwd);
			}

		};
		mController = new LoginController(this, mOperation);
		initWidgets();
	}

	private void initWidgets() {
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitleText(getString(R.string.login));
		titleBar.setButtonClickListener(TitleBarButton.rightTextView, this);
		titleBar.setVisibility(TitleBar.TitleBarButton.leftImgv, View.GONE);
		titleBar.setVisibility(TitleBar.TitleBarButton.rightTextView, View.VISIBLE);
		mAccountEditText = (EditText) findViewById(R.id.account);
		mPwdEditText = (EditText) findViewById(R.id.pwd);
		mShowPwdImg = (ImageView) findViewById(R.id.is_show_pwd);
		mLoginTextView = (TextView) findViewById(R.id.login);
		mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
		mForgetPwdTextView = (TextView) findViewById(R.id.forget_pwd);
		mRegisterTextView = (TextView) findViewById(R.id.register);

		mShowPwdImg.setSelected(false);
		mShowPwdImg.setOnClickListener(this);
		mLoginLayout.setOnClickListener(this);
		mForgetPwdTextView.setOnClickListener(this);
		mRegisterTextView.setOnClickListener(this);
		titleBar.setRightTextViewText(getResources().getString(R.string.cancel));
		if (!TextUtils.isEmpty(UserModule.getUserAccount(LoginActivity.this))) {
			mAccountEditText.setText(UserModule.getUserAccount(LoginActivity.this));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_textview:
			MobclickAgent.onEvent(LoginActivity.this, "1003");
			finish();
			break;
		case R.id.is_show_pwd:
			if (!isShowPwd) {
				isShowPwd = true;
				mShowPwdImg.setSelected(true);
				mPwdEditText.setInputType(InputTypeInfo.VISIBLE_PASSWORD);
				Editable etable = mPwdEditText.getText();
				Selection.setSelection(etable, etable.length());
			} else {
				isShowPwd = false;
				mShowPwdImg.setSelected(false);
				mPwdEditText.setInputType(InputTypeInfo.VARIATION_PASSWORD);
				Editable etable = mPwdEditText.getText();
				Selection.setSelection(etable, etable.length());
			}
			break;
		case R.id.login_layout:
			MobclickAgent.onEvent(LoginActivity.this, "1001");
			mController.login(mAccountEditText, mPwdEditText);
			mAccount = mAccountEditText.getText().toString().trim();
			break;
		case R.id.forget_pwd:
		case R.id.register:
			Intent mIntent = new Intent();
			mIntent.setClass(LoginActivity.this, VerifyCodeGetActivity.class);
			if (v.getId() == R.id.forget_pwd) {
				MobclickAgent.onEvent(LoginActivity.this, "1004");
				mIntent.putExtra(IntentFlag.INTENT_FROM, IdentifyCodeType.forgetPwd);
			} else {
				MobclickAgent.onEvent(LoginActivity.this, "1002");
				mIntent.putExtra(IntentFlag.INTENT_FROM, IdentifyCodeType.register);
			}
			startActivity(mIntent);
			break;
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (url.equals(ConfigInfo.API_ADDR_REC_NAME)) {
			AddresssReceiveResponse addresssReceiveResponse = (AddresssReceiveResponse) object;
			mUserModule.saveUserDefaultAdd(LoginActivity.this, addresssReceiveResponse);
		} else if (url.equals(ConfigInfo.API_LOGIN)) {
			LoginResponse mLoginResponse = (LoginResponse) object;
			if (mLoginResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				ToastUtil.showToast(LoginActivity.this, getString(R.string.login_success));
				saveUserMessage(mLoginResponse);
				// 不加Token的用户信息
				mUserModule.saveMemberInfo(LoginActivity.this, mLoginResponse.getUser());
				// 加Token的用户信息
				mUserModule.saveUserLoginMessage(mUserSaveInfo, this);
				// 得到收货地址列表
				mUserModule.getAddressList(LoginActivity.this);
				// 登录成功后同步购物车
				HmlShoppingCartController.instance(getApplicationContext())
						.syncCartFromSever(false);
				// 个人资料获取
				mUserModule.postUserInfo(mLoginActivity);
			} else {

				mOperation.dismissLoginLoading();
			}
		} else if (url.equals(ConfigInfo.API_USERINFO_GET)) {
			UserInfoResponse mUserInfoResponse = (UserInfoResponse) object;
			if (mUserInfoResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				// 加Token的用户信息
				mUserModule.saveMemberInfo(mUserSaveInfo, mUserInfoResponse, this);

				/**
				 * 从哪跳来，返回哪去。
				 */
				Intent currentIntent = getIntent();
				LoginType loginType = (LoginType) currentIntent
						.getSerializableExtra(IntentFlag.INTENT_FROM);
				if (loginType != null) {
					Intent intent = null;
					if (loginType == LoginType.resetPwd) {
						intent = new Intent(LoginActivity.this, PwdResetActivity.class);
					} else if (loginType == LoginType.goToTinyCommunity) {
						intent = new Intent(LoginActivity.this, MainActivity.class);
						intent.putExtra(IntentFlag.INTENT_FROM, LoginActivity.class.getName());
					}
					startActivity(intent);
					finish();
					return;
				}
				setResult(RESULT_OK, currentIntent);
				finish();
			}
		}
	}

	/**
	 * 设置用户信息类
	 * 
	 * @param loginResponse
	 *            登录返回值
	 */
	private void saveUserMessage(LoginResponse loginResponse) {
		mUserSaveInfo.setmAccount(mAccount);
		mUserSaveInfo.setmUserToken(loginResponse.getUser_token());
		mUserSaveInfo.setmMemberCode(loginResponse.getUser().getMember_code());
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		mOperation.dismissLoginLoading();
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mOperation.dismissLoginLoading();
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mOperation.dismissLoginLoading();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1002"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1002");
		MobclickAgent.onPause(this);
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
