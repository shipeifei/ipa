/*
 * EmailLoginActivity.java [V 1.0.0]
 * classes : com.ipassistat.ipa.ui.email.activity.EmailLoginActivity
 * 时培飞 Create at 2015-4-30 上午11:34:33
 */
package com.ipassistat.ipa.ui.email.activity;

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
import com.ipassistat.ipa.constant.IntentFlag.LoginType;
import com.ipassistat.ipa.ui.activity.BaseActivity;
import com.ipassistat.ipa.ui.activity.LoginActivity;
import com.ipassistat.ipa.ui.activity.MainActivity;
import com.ipassistat.ipa.ui.activity.PwdResetActivity;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * com.ipassistat.ipa.ui.email.activity.EmailLoginActivity
 * 
 * @author 时培飞 Create at 2015-4-30 上午11:34:33
 */
public class EmailLoginActivity extends BaseActivity implements OnClickListener {

	/** 用户EditText */
	private EditText mAccountEditText;
	/** 用户密码 */
	private EditText mPwdEditText;
	/** 登录状态TextView */
	private TextView mLoginTextView;
	/** 登录状态背景Layout */
	private LinearLayout mLoginLayout;

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
	/** 登录回调接口 */
	LoginOperation mOperation;
	/** 登录界面 */
	private EmailLoginActivity mLoginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_login);
		mUserModule = new UserModule(this);
		mUserSaveInfo = new UserInfoSaveEntity();
		mLoginActivity = this;
		mOperation = new LoginOperation() {

			public void showLoginLoading() {
				mLoginLayout.setSelected(true);
				findViewById(R.id.email_progress_bar).setVisibility(View.VISIBLE);
				mLoginTextView.setText(getString(R.string.logining));
			}

			public void dismissLoginLoading() {
				mLoginLayout.setSelected(false);
				findViewById(R.id.email_progress_bar).setVisibility(View.GONE);
				mLoginTextView.setText(getString(R.string.login));
			}

			@Override
			public void sendLoginReq(String loginName, String pwd) {
				mUserModule.login(mLoginActivity, loginName, pwd);
			}

		};
		
		initWidgets();
	}

	private void initWidgets() {
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitleText(getString(R.string.email_login));
		titleBar.setButtonClickListener(TitleBarButton.rightTextView, this);
		titleBar.setVisibility(TitleBar.TitleBarButton.leftImgv, View.GONE);
		titleBar.setVisibility(TitleBar.TitleBarButton.rightTextView, View.VISIBLE);
		mAccountEditText = (EditText) findViewById(R.id.email_account);
		mPwdEditText = (EditText) findViewById(R.id.email_pwd);
		mShowPwdImg = (ImageView) findViewById(R.id.email_is_show_pwd);
		mLoginTextView = (TextView) findViewById(R.id.email_login);
		mLoginLayout = (LinearLayout) findViewById(R.id.email_login_layout);

		mShowPwdImg.setSelected(false);
		mShowPwdImg.setOnClickListener(this);
		mLoginLayout.setOnClickListener(this);

		titleBar.setRightTextViewText(getResources().getString(R.string.cancel));
		if (!TextUtils.isEmpty(UserModule.getUserAccount(EmailLoginActivity.this))) {
			mAccountEditText.setText(UserModule.getUserAccount(EmailLoginActivity.this));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_textview:
			MobclickAgent.onEvent(EmailLoginActivity.this, "1003");
			finish();
			break;
		case R.id.email_is_show_pwd:
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
		case R.id.email_login_layout:
			MobclickAgent.onEvent(EmailLoginActivity.this, "1001");

			if (TextUtils.isEmpty(mAccountEditText.getText().toString())) {
				ToastUtil.showToast(this, getString(R.string.email_address_isnull));
				return;
			} else {
				if (TextUtils.isEmpty(mPwdEditText.getText().toString())) {
					ToastUtil.showToast(this, getString(R.string.email_pwd_isnull));
					return;
				}
			}

			if (!StringUtil.isEmail(mAccountEditText.getText().toString())) {
				ToastUtil.showToast(this, getString(R.string.email_address_type));
			} else {
			}
			break;

		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		
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
