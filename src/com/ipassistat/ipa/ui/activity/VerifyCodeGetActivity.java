package com.ipassistat.ipa.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.LoginController;
import com.ipassistat.ipa.business.LoginController.VerifyCodeOperation;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.constant.StatusCode;
import com.ipassistat.ipa.constant.IntentFlag.IdentifyCodeType;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 此类有如下两个作用 1:重设密码:获取验证码(默认) 2：注册获取验证码
 * 
 * @author lxc
 * @author lis 重构
 * @date 2015-2-11
 */
public class VerifyCodeGetActivity extends BaseActivity implements OnClickListener {
	/** 手机号 */
	private EditText mPhoneEditText;
	/** 验证码 */
	private EditText mIdentifyCodeEditText;
	/** 获取验证码Button */
	private Button mIdentGetBtn;
	/** 下一步Button */
	private Button mNextBtn;
	/** 协议按钮 */
	private ImageView mAgreeImgv;
	/** 协议状态 false为不同意 ,true为同意 */
	private boolean isAgree;
	/** 验证码重置时间 */
	private int mRemainTime = 60;
	/** 验证码类型对象 */
	private IdentifyCodeType mIdentifyCodeType;
	/** 用户中心模块 */
	private UserModule mUserModule;
	/** 验证码类型 */
	private String mVerifyCodeType = "";
	/** 用户 */
	private String mLoginName = "";
	/** 验证码 */
	private String mVerifyCode = "";
	private Context mContext;
	/** 登录注册模块控制器 */
	private LoginController mController;
	/** 验证码回调接口 */
	private VerifyCodeOperation mOperation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_forget);
		mUserModule = new UserModule(this);
		mOperation = new VerifyCodeOperation() {

			@Override
			public void getIdentifyCode(String userName, String verifyCodeType) {
				mUserModule.getIdentifyCode(VerifyCodeGetActivity.this, userName, verifyCodeType);
			}

			@Override
			public void checkVerifyCode(String userName, String verifyCode, String mVerifyCodeType) {
				mUserModule.checkVerifyCode(VerifyCodeGetActivity.this, userName, verifyCode,
						mVerifyCodeType);
			}

			@Override
			public void checkFails() {
				if (mAgreeImgv.isSelected()) {
					mNextBtn.setEnabled(true);
				}
			}
		};
		mController = new LoginController(this, mOperation);
		getInfo();
		initWidgest();
	}

	/**
	 * 获得上一界面的类型(注册或忘记密码)
	 */
	private void getInfo() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mIdentifyCodeType = (IdentifyCodeType) bundle.getSerializable(IntentFlag.INTENT_FROM);
		}
	}

	/**
	 * 初始化布局
	 */
	private void initWidgest() {
		mContext = this;
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		TextView protocolTextView = (TextView) findViewById(R.id.protocol_content);
		mPhoneEditText = (EditText) findViewById(R.id.account_et);
		mIdentifyCodeEditText = (EditText) findViewById(R.id.iden_code_et);
		mIdentGetBtn = (Button) findViewById(R.id.identify_get_btn);
		mNextBtn = (Button) findViewById(R.id.next);
		mAgreeImgv = (ImageView) findViewById(R.id.is_agree);

		// 跳转标识
		String flag = getIntent().getStringExtra(IntentKey.INTENT_REGISTER);
		// 直接从引导跳转到注册
		if (flag != null && flag.equals(IntentFlag.INTENT_FROM_GUIDE)) {
			titleBar.setButtonClickListener(TitleBarButton.leftImgv, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, MainActivity.class);
					startActivity(intent);
				}
			});
		} else {
			titleBar.setButtonClickListener(TitleBarButton.leftImgv, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1008");
					finish();
				}
			});
		}
		// 注册界面和重置界面时的布局显示
		if (mIdentifyCodeType == IdentifyCodeType.register) {
			titleBar.setTitleText(getString(R.string.register_title));
			findViewById(R.id.register_protocol).setVisibility(View.VISIBLE);
			findViewById(R.id.is_agree_layout).setVisibility(View.VISIBLE);
			mVerifyCodeType = "reginster";
		} else {
			titleBar.setTitleText(getString(R.string.resetpwd_title));
			mVerifyCodeType = "forgetpassword";
		}

		// 软件许可及服务协议标题
		String protocolContent = getString(R.string.protocolcontent_text);
		SpannableString spanString = ViewUtil.getTextColorStyle(protocolContent, 2,
				protocolContent.length(), getResources().getColor(R.color.goods_detail_tab_bg));
		spanString.setSpan(new ClickableSpan() {
			// 在onClick方法中可以编写单击链接时要执行的动作
			@Override
			public void onClick(View widget) {
				IntentUtil.startWebView(mContext, getString(R.string.protocol_title),
						getString(R.string.register_protocol_url), "1074", null);
			}
		}, 2, protocolContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanString.setSpan(
				new ForegroundColorSpan(getResources().getColor(R.color.goods_detail_tab_bg)), 2,
				protocolContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		protocolTextView.setText(spanString);
		// 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
		protocolTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mIdentGetBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mAgreeImgv.setOnClickListener(this);
		initAgreeProtol();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			switch (mIdentifyCodeType) {
			case register:
				MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1006");
				break;
			case forgetPwd:
				MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1012");
				break;
			default:
				break;
			}
			mNextBtn.setEnabled(false);
			mLoginName = mPhoneEditText.getText().toString().trim();
			mVerifyCode = mIdentifyCodeEditText.getText().toString().trim();
			mController.pwdForget(mPhoneEditText, mIdentifyCodeEditText, mVerifyCodeType);

			break;
		case R.id.identify_get_btn:
			switch (mIdentifyCodeType) {
			case register:
				MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1005");
				break;
			case forgetPwd:
				MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1011");
				break;
			default:
				break;
			}
			mController.pwdForget(mPhoneEditText, null, mVerifyCodeType);
			break;
		case R.id.is_agree:
			setIsAgreeProtol();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化同意协议状态
	 */
	private void initAgreeProtol() {
		isAgree = true;
		mIdentGetBtn.setSelected(true);
		mAgreeImgv.setSelected(true);
		mNextBtn.setEnabled(true);
	}

	/**
	 * 设置协议同意,不同意时状态
	 */
	private void setIsAgreeProtol() {
		if (isAgree) {
			isAgree = false;
			LogUtil.outLogDetail("false");
			mAgreeImgv.setSelected(false);
			mNextBtn.setEnabled(false);
		} else {
			MobclickAgent.onEvent(VerifyCodeGetActivity.this, "1007");
			isAgree = true;
			LogUtil.outLogDetail("true");
			mAgreeImgv.setSelected(true);
			mNextBtn.setEnabled(true);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mNextBtn.setEnabled(true);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);

		BaseResponse baseResponse = (BaseResponse) object;
		// 获取验证码
		if (url.equals(ConfigInfo.API_GET_VERIFY_CODE)) {
			if (baseResponse.getResultCode() == StatusCode.RESULT_OK) {
				ToastUtil.showToast(VerifyCodeGetActivity.this,
						getString(R.string.getverifycode_success));
				final Timer timer = new Timer();
				mIdentGetBtn.setTextColor(getResources().getColor(R.color.goods_detail_tab_bg));
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								mRemainTime = mRemainTime - 1;
								mIdentGetBtn.setText("重新获取(" + mRemainTime + ")");
								mIdentGetBtn.setSelected(false);
								mIdentGetBtn.setEnabled(false);
								if (mRemainTime == 0) {
									mRemainTime = 60;
									timer.cancel();
									mIdentGetBtn.setSelected(true);
									mIdentGetBtn.setEnabled(true);
									mIdentGetBtn.setText(getString(R.string.get_verifycode));
									mIdentGetBtn.setTextColor(getResources().getColor(
											R.color.black_07));
								}
							}
						});
					}
				}, 1000, 1000);
			}
		}
		// 验证码判断
		if (url.equals(ConfigInfo.API_VERIFY_CODE_CHECK)) {
			if (baseResponse.getResultCode() == StatusCode.RESULT_OK) {
				Intent intent = new Intent();
				switch (mIdentifyCodeType) {
				case register:
					intent.setClass(VerifyCodeGetActivity.this, RegisterActivity.class);
					break;
				case forgetPwd:
					intent.setClass(VerifyCodeGetActivity.this, PwdForgettActivity.class);
					break;
				default:
					break;
				}
				intent.putExtra("login_name", mLoginName);
				intent.putExtra("identifycode", mVerifyCode);
				startActivity(intent);
			} else {
				mNextBtn.setEnabled(true);
			}
		}
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
