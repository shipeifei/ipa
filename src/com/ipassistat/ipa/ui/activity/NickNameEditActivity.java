package com.ipassistat.ipa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.bean.request.UserInfoRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.eventbus.MessageEvent;
import com.umeng.analytics.MobclickAgent;


/**
 * 名字修改的工具类
 * 
 * @Package com.ichsy.mboss.ui.activity
 * 
 * @File NickNameEditActivity.java
 * 
 * @author LiuYuHang Date: 2015年3月2日
 * 
 *         Modifier： Modified Date： Modify：
 * 
 *         Copyright @ 2015 Corpration CHSY
 * 
 */
public class NickNameEditActivity extends BaseActivity implements OnClickListener {
	public static final int RESULT_TEXT_CODE = 32;

	public static final String TITLE_TEXT = "title_text";// 获取输入的文字
	public static final String TITLE_MODEL = "model";// 获取输入的文字
	public static final String DEFAULT_TEXT = "default_text";// 默认文字
	public static final String RESULT_TEXT = "get_text";// 获取输入的文字

	private EditText mEditView;
	private UserInfoRequest mInfoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_edittext);

		updateUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1051"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1051"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);// onPause 中会保存信息
	}

	private void updateUI() {
		String title = getIntent().getStringExtra(TITLE_TEXT);
		// String defaultText = getIntent().getStringExtra(DEFAULT_TEXT);
		mInfoModel = (UserInfoRequest) getIntent().getSerializableExtra(TITLE_MODEL);

		mEditView = (EditText) findViewById(id.editview);

		// setTitleRightImage(drawable.icon_address_save);
		setTitleRightText(getString(R.string.nicksave), this);
		setTitleText(title);

		String nickName = mInfoModel.getNickname();
		mEditView.setText(nickName);
		mEditView.setSelection(nickName.length());

		findListeners(this, id.left_imgv, id.right_imgv);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);

		if (url.equals(ConfigInfo.API_USERINFO_UPDATA)) {
			BaseResponse response = (BaseResponse) object;

			if (response.getResultCode() == ConfigInfo.RESULT_OK) {
				
				//EventBus.getDefault().post(new MessageEvent("NickNameEditActivity",mInfoModel.getNickname()));
				finish();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.left_imgv:
			finish();
			break;
		case id.right_textview:
			MobclickAgent.onEvent(getApplicationContext(), "1168");
			String currentText = mEditView.getText().toString();

			if (ViewUtil.isEdittextEmpty(mEditView)) {
				ToastUtil.showToast(getApplicationContext(), getString(R.string.nicknull));
				return;
			}

			if (!StringUtil.checkNickName(currentText)) {
				ToastUtil.showToast(getApplicationContext(), getString(R.string.nickcomponet));
				return;
			}

			if (StringUtil.getTrueLengh(currentText) > 20) {
				ToastUtil.showToast(getApplicationContext(), getString(R.string.nicklimit));
				return;
			}

			// LogUtil.outLogDetail("是否有表情: " +
			// StringUtil.checkNickName(currentText));

			mInfoModel.setNickname(currentText);
			new UserModule(this).updateUserInfo(this, mInfoModel.getSex(), mInfoModel.getBirthday(), mInfoModel.getArea_code(), mInfoModel.getAdress(), mInfoModel.getAvatar(),
					mInfoModel.getNickname(), true);

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
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

}
