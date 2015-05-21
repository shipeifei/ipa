package com.ipassistat.ipa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.RegisterResponse;
import com.ipassistat.ipa.bean.response.entity.UserInfoSaveEntity;
import com.ipassistat.ipa.business.LoginController;
import com.ipassistat.ipa.business.LoginController.RegisterOperation;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.InputTypeInfo;
import com.ipassistat.ipa.constant.StatusCode;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册页面
 * 
 * @Description
 * @author lxc
 * @author lis 重构界面
 * @date 2015-2-11
 * 
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
	/** 用户昵称 */
	private EditText mNickNameEditText;
	/** 用户密码 */
	private EditText mPwdEditText;
	/** 注册按钮 */
	private Button mRegisterButton;
	/** 显示密码 */
	private ImageView mShowPwdImg;
	/** 显示密码状态 false为不显示,true为显示 */
	private boolean isShowPwd;
	/** 用户中心模块 */
	private UserModule mUserModule;
	/** 登录注册模块控制器 */
	private LoginController mController;
	/** 注册回调接口 */
	private RegisterOperation mOperation;
	/** 注册界面 */
	private RegisterActivity mActivity;
	/** 会员注册的手机号 获取验证码界面传过来的 */
	String mLoginName;
	/** 用户信息保存类 */
	private UserInfoSaveEntity mUserSaveInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mActivity = this;
		mUserSaveInfo = new UserInfoSaveEntity();
		mUserModule = new UserModule(RegisterActivity.this);
		mOperation = new RegisterOperation() {

			@Override
			public void sendRegisterReq(String loginName, String nickName, String pwd, String verifycode) {
				mUserModule.register(mActivity, loginName, nickName, pwd, verifycode);
			}
		};
		mController = new LoginController(this, mOperation);
		initWidgets();
	}

	/**
	 * 初始化布局
	 */
	private void initWidgets() {
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitleText(getString(R.string.register_title));
		titleBar.setButtonClickListener(TitleBarButton.leftImgv, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleBar.setVisibility(TitleBar.TitleBarButton.rightImgv, View.GONE);
		mNickNameEditText = (EditText) findViewById(R.id.nick_name);
		mRegisterButton = (Button) findViewById(R.id.register);
		mPwdEditText = (EditText) findViewById(R.id.pwd);
		mShowPwdImg = (ImageView) findViewById(R.id.is_show_pwd);
		mNickNameEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		mShowPwdImg.setSelected(false);
		mShowPwdImg.setOnClickListener(this);
		mRegisterButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.register:
			Bundle bundle = getIntent().getExtras();
			// 验证码
			String verifycode = "";
			if (null != bundle) {
				mLoginName = bundle.getString("login_name");
				verifycode = bundle.getString("identifycode");
			}
			if (!TextUtils.isEmpty(mLoginName) && !TextUtils.isEmpty(verifycode)) {
				mController.register(mNickNameEditText, mPwdEditText, mLoginName, verifycode);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		RegisterResponse registerResponse = (RegisterResponse) object;
		if (registerResponse.getResultCode() == StatusCode.RESULT_OK) {
			ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_success));
			saveUserMessage(registerResponse);
			
		
			mUserModule.saveMemberInfo(RegisterActivity.this, registerResponse.getUser());
			
			// 加Token的用户信息
			mUserModule.saveUserLoginMessage(mUserSaveInfo, this);
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, PersonalActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 设置用户信息类
	 * 
	 * @param registerResponse
	 *            用户注册返回值对象
	 */
	private void saveUserMessage(RegisterResponse registerResponse) {
		mUserSaveInfo.setmAccount(mLoginName);
		mUserSaveInfo.setmUserToken(registerResponse.getUser_token());
		mUserSaveInfo.setmMemberCode(registerResponse.getUser().getMember_code());
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1003"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1003"); //
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
