/*
 * EmailSelectActivity.java [V 1.0.0]
 * classes : com.ipassistat.ipa.ui.email.activity.EmailSelectActivity
 * 时培飞 Create at 2015-4-30 上午10:16:46
 */
package com.ipassistat.ipa.ui.email.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.EmailType;
import com.ipassistat.ipa.ui.activity.BaseActivity;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 邮箱选择界面 com.ipassistat.ipa.ui.email.activity.EmailSelectActivity
 * 
 * @author 时培飞 Create at 2015-4-30 上午10:16:46
 */
public class EmailSelectActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "EmailSelectActivity";
	// 标题
	private TitleBar mTitlebar;
	// 网易
	private ImageView mEmailWangyi;
	// 新浪
	private ImageView mEmailSina;
	// msn
	private ImageView mEmailMsn;
	// 谷歌
	private ImageView mEmailGmail;
	// 搜狐
	private ImageView mEmailSohu;
	// qq
	private ImageView mEmailQQ;
	// 其他邮箱
	private ImageView mEmailOther;
	private Context mContext;
	public static EmailSelectActivity emailSelectActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_select);
		mContext = this;
		initWidgets();
		initData();
		initListener();

	}

	/***
	 * 
	 * @author 时培飞 Create at 2015-4-30 上午10:27:55
	 */
	private void initWidgets() {
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		mEmailGmail = (ImageView) findViewById(R.id.email_gmail);
		mEmailWangyi = (ImageView) findViewById(R.id.email_wangyi);
		mEmailSina = (ImageView) findViewById(R.id.email_sina);
		mEmailMsn = (ImageView) findViewById(R.id.email_msn);
		mEmailSohu = (ImageView) findViewById(R.id.email_sohu);
		mEmailQQ = (ImageView) findViewById(R.id.email_qq);
		mEmailOther = (ImageView) findViewById(R.id.email_other);

	}

	/***
	 * 初始化数据
	 * 
	 * @author 时培飞 Create at 2015-4-30 上午11:15:30
	 */
	@Override
	protected void initData() {
		emailSelectActivity = this;
		mTitlebar.setTitleText(getString(R.string.email_select));
	}

	/***
	 * 绑定事件
	 * 
	 * @author 时培飞 Create at 2015-4-30 上午11:17:51
	 */
	private void initListener() {
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, "1038");
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
		findListeners(this, R.id.email_gmail, R.id.email_msn, R.id.email_other, R.id.email_qq, R.id.email_sina, R.id.email_sohu, R.id.email_wangyi);

	}

	/***
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_gmail:

			IntentUtil.startEmailLogin(mContext, EmailType.gmail);
			break;

		case R.id.email_msn:
			IntentUtil.startEmailLogin(mContext, EmailType.msn);
			break;
		case R.id.email_other:
			IntentUtil.startEmailLogin(mContext, EmailType.other);
			break;
		case R.id.email_qq:
			IntentUtil.startEmailLogin(mContext, EmailType.qq);
			break;
		case R.id.email_sina:
			IntentUtil.startEmailLogin(mContext, EmailType.sina);
			break;
		case R.id.email_sohu:
			IntentUtil.startEmailLogin(mContext, EmailType.sohu);
			break;
		case R.id.email_wangyi:
			IntentUtil.startEmailLogin(mContext, EmailType.wangyi);
			break;

		default:
			break;
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
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
