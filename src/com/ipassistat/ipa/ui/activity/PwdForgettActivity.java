package com.ipassistat.ipa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.LoginController;
import com.ipassistat.ipa.business.LoginController.PwdResetOperation;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.InputTypeInfo;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码
 * 
 * @Description
 * @author lis 重构
 * @date 2015-2-11
 * 
 */
public class PwdForgettActivity extends BaseActivity implements OnClickListener {
	/** 用户密码 */
	private EditText mPwdEditText;
	/** 确认按钮 */
	private Button mSubmitBtn;
	/** 显示密码 */
	private ImageView mShowPwdImg;
	/** 显示密码状态 false为不显示,true为显示 */
	private boolean isShowPwd;
	/** 用户中心模块 */
	private UserModule mUserModule;
	/** 登录注册模块控制器 */
	private LoginController mController;
	/** 密码重置回调接口 */
	PwdResetOperation mOperation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pwd_reset_two);
		mUserModule = new UserModule(this);
		mOperation = new PwdResetOperation() {

			@Override
			public void sendResetPwdReq(String userName, String newPwd,
					String verifyCode, String clientSource) {
				mUserModule.resetPwd(PwdForgettActivity.this, userName, newPwd,
						verifyCode, clientSource);
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
		titleBar.setTitleText(getString(R.string.resetpwd_title));
		titleBar.setButtonClickListener(TitleBarButton.leftImgv,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mPwdEditText = (EditText) findViewById(R.id.new_pwd);
		mShowPwdImg = (ImageView) findViewById(R.id.is_show_pwd);
		mSubmitBtn = (Button) findViewById(R.id.submit);
		mShowPwdImg.setSelected(false);
		mShowPwdImg.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			MobclickAgent.onEvent(PwdForgettActivity.this, "1013");
			String password = mPwdEditText.getText().toString().trim();
			Bundle bundle = getIntent().getExtras();
			String loginName = "";
			String verifyCode = "";
			if (null != bundle) {
				loginName = bundle.getString("login_name");
				verifyCode = bundle.getString("identifycode");
			}
			if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(verifyCode)) {
				mController.pwdReset(loginName, password, verifyCode, "app");
			}
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
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		BaseResponse baseResponse = (BaseResponse) object;
		if (baseResponse.getResultCode() == ConfigInfo.RESULT_OK) {
			Intent intent = new Intent(PwdForgettActivity.this,
					LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1004"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1004");
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
