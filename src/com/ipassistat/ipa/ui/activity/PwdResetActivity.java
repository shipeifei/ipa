package com.ipassistat.ipa.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.StatusCode;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 密码重置（修改）（设置页面的密码修改）
 * 
 * @author Administrator
 * 
 */
public class PwdResetActivity extends BaseActivity implements View.OnClickListener {

	private EditText oldPwdEditText, pwdeEditText;
	private LinearLayout modifyOk;
	private ImageView isShowPwdImg;
	private boolean isShowPwd;
	private UserModule userModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_reset);

		userModule = new UserModule(this);
		initWidgets();
	}

	private void initWidgets() {
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitleText(getString(R.string.updatepassword));
		titleBar.setButtonClickListener(TitleBarButton.leftImgv, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleBar.setVisibility(TitleBar.TitleBarButton.rightImgv, View.GONE);
		modifyOk = (LinearLayout) findViewById(R.id.login_layout);
		modifyOk.setOnClickListener(this);

		isShowPwdImg = (ImageView) findViewById(R.id.is_show_pwd);
		isShowPwdImg.setBackgroundResource(R.drawable.pwd_imgv_hide);
		isShowPwdImg.setOnClickListener(this);
		pwdeEditText = (EditText) findViewById(R.id.new_pwd);
		oldPwdEditText = (EditText) findViewById(R.id.old_pwd);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.is_show_pwd://显示密码
			if (!isShowPwd) {
				isShowPwd = true;
				ToastUtil.showToast(PwdResetActivity.this, getString(R.string.showpassword));
				isShowPwdImg.setBackgroundResource(R.drawable.pwd_imgv_show);
				pwdeEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				Editable etable = pwdeEditText.getText();
				Selection.setSelection(etable, etable.length());
			} else {
				isShowPwd = false;
				isShowPwdImg.setBackgroundResource(R.drawable.pwd_imgv_hide);
				pwdeEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				Editable etable = pwdeEditText.getText();
				Selection.setSelection(etable, etable.length());
			}

			break;
		case R.id.login_layout://登录
			String old_password = oldPwdEditText.getText().toString().trim();
			String newPwd = pwdeEditText.getText().toString().trim();
			if (TextUtils.isEmpty(old_password) || TextUtils.isEmpty(newPwd)) {
				ToastUtil.showToast(PwdResetActivity.this,  getString(R.string.passwordnull));
			} else if (!StringUtil.isValidPwd(old_password)) {
				ToastUtil.showToast(PwdResetActivity.this, getString(R.string.passwordwrong));
			} else if (!StringUtil.isValidPwd(newPwd)) {
				ToastUtil.showToast(PwdResetActivity.this, getString(R.string.passwordlimit));
				pwdeEditText.setText("");
			} else {
				userModule.modifyPwd(PwdResetActivity.this, old_password, newPwd);
			}
			break;
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {

		super.onMessageSucessCalledBack(url, object);
		BaseResponse baseResponse = (BaseResponse) object;
		if (baseResponse.getResultCode() == StatusCode.RESULT_OK) {
			ToastUtil.showToast(PwdResetActivity.this, getString(R.string.warnpassword));
			finish();
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1005"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1005"); //
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
