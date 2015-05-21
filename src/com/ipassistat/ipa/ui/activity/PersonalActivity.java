package com.ipassistat.ipa.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.response.UserInfoResponse;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.constant.IntentFlag.IdentifyCodeType;
import com.ipassistat.ipa.util.ApiUrl;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.share.um.ShareUM;
import com.ipassistat.ipa.view.CircularImageView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人中心首页面
 * 
 * @author maoyn
 * @update :任恒
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {

	// 请求服务器相关局部变量
	private UserModule mModel; // 网络请求数据
	private UserInfoResponse mResponse;

	// 布局控件相关变量生命
	// private CircularImageView mPhoto; // 圆形的头像
	private TextView mUsername; // 用户昵称
	private TextView mSexTv; // 用户的性别
	private TextView mCenturyTv; // 年代
	private RelativeLayout mDetailInfo; // 整个的用户信息栏
	private TitleBar mTitlebar; // 标题栏
	private RelativeLayout registerLogin; // 注册和登录按钮的布局，用于隐藏和显示该布局
	private RelativeLayout usernameSkintype; // 用户信息，用于隐藏和显示该布局
	private View mEdit;
	private CircularImageView mPhotoLogin;

	// 其他变量声明
	private Activity mActivity; // 上下文
	private boolean mHasLogin = false;// 是否已经登录
	// private boolean hasNoticePerfectInfo = false;
	private ShareUM mShare;
	/** 用户的性别 */
	private String mSex = "-1";
	/** 用户的年代 */
	private String mCentury = "";
	/** 用户昵称 */
	private String mNickName = "";
	/** 用户头像 */
	private String mUserPhoto = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		mActivity = this;
		mModel = new UserModule(this);
		mShare = new ShareUM(mActivity);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHasLogin = UserModule.isLogin(mActivity);// 判断用户是否登陆
		initViews();
		initListener();

		// 判断是否登录
		if (mHasLogin) {
			// 请求个人资料网络数据,统一调用个人资料接口
			mModel.postUserInfo(mActivity);
			// 请求护肤需求数据
			// mModel.getSkinExplain(mActivity, 0);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		mHasLogin = UserModule.isLogin(mActivity);// 判断用户是否登陆
		if (mHasLogin) {
			MobclickAgent.onPageStart("1083"); // 统计页面
			MobclickAgent.onResume(this);
			// 首先读取本地的用户信息
			MemberInfo info = UserModule.getMemberInfo(mActivity);
			if (info != null) {
				if (!TextUtils.isEmpty(info.getNickname())) {
					mUsername.setText(info.getNickname());
				}
				if (!TextUtils.isEmpty(info.getPhoto())) {
					new AQuery(mPhotoLogin).image(info.getPhoto(), true, true, GlobalUtil.displayMetrics.widthPixels, drawable.personal_photo);
				} else {
					new AQuery(mPhotoLogin).image(drawable.personal_photo);
				}
				mSex = String.valueOf(info.getGender());
				mSexTv.setVisibility(View.VISIBLE);
				if (mSex.equals(Constant.PERSON_MAN)) {
					mSexTv.setText(getString(R.string.man));
				} else if (mSex.equals(Constant.PERSON_WOMAN)) {
					mSexTv.setText(getString(R.string.women));
				} else {
					mSexTv.setVisibility(View.INVISIBLE);
				}
			}
			info = null;
		}else{
			MobclickAgent.onPageStart("1082"); // 统计页面
			MobclickAgent.onResume(this);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mHasLogin){
			MobclickAgent.onPageEnd("1083"); // 保证 onPageEnd 在onPause 之前调用
			MobclickAgent.onPause(this);
		}else{
			MobclickAgent.onPageEnd("1082"); 
			MobclickAgent.onPause(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		MobclickAgent.onPageEnd("1007"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		if (isFinishing()) {
			return;
		}
		super.onMessageSucessCalledBack(url, object);

		if (ConfigInfo.API_USERINFO_GET.equals(url)) {// 个人资料请求服务器回调
			mResponse = (UserInfoResponse) object;
			if (mResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				mNickName = mResponse.getNickname();// 用户昵称
				mUserPhoto = mResponse.getAvatar();// 用户头像
				mCentury = mResponse.getCentury(); // 用户年代
				// 页面控件赋值
				fillDatas();
			}
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
	}

	/**
	 * 填充数据保存到本地，主要用来实现实现数据的缓存
	 */
	private void fillDatas() {
		mModel.saveDatas(mActivity, mSex, mResponse.getBirthday(), mResponse.getArea_code(), mUserPhoto, mNickName, mResponse.getCentury());

		// 控件赋值主要有用户昵称和皮肤类型以及头像
		mUsername.setText(mNickName);
		
		// mSexTv.setText(mSex);
		if (!TextUtils.isEmpty(mCentury)) {
			mCenturyTv.setVisibility(View.VISIBLE);
			mCenturyTv.setText(mCentury + getString(R.string.later));
		} else {
			mCenturyTv.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(mUserPhoto)) {
			new AQuery(mPhotoLogin).image(mUserPhoto, true, true, GlobalUtil.displayMetrics.widthPixels, drawable.personal_photo);
		} else {
			new AQuery(mPhotoLogin).image(drawable.personal_photo);
		}
	}

	/**
	 * layout与组件绑定
	 */
	private void initViews() {

		// 实例化声明的变量
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		//mPhotoLogin = (CircularImageView) findViewById(R.id.photo_login);
		registerLogin = (RelativeLayout) findViewById(R.id.register_login);
		usernameSkintype = (RelativeLayout) findViewById(R.id.username_skintype);
		mUsername = (TextView) findViewById(R.id.username);
		mSexTv = (TextView) findViewById(R.id.sex);
		mCenturyTv = (TextView) findViewById(R.id.century);
		mDetailInfo = (RelativeLayout) findViewById(R.id.personal);
		mEdit = findViewById(R.id.edit);

		// 控件赋值
		mTitlebar.setTitleText(getResources().getString(R.string.personal));
		if (mHasLogin) {
			mEdit.setVisibility(View.VISIBLE);
			usernameSkintype.setVisibility(View.VISIBLE);
			registerLogin.setVisibility(View.GONE);
			mDetailInfo.setClickable(true);

		} else {
			usernameSkintype.setVisibility(View.GONE);
			mEdit.setVisibility(View.GONE);
			registerLogin.setVisibility(View.VISIBLE);
			mDetailInfo.setClickable(false);
		}
	}

	/**
	 * 注册监听器
	 */
	private void initListener() {

		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
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

		findListeners(this, R.id.personal, R.id.cart, R.id.orderform, R.id.trial, R.id.my_collection, R.id.setings, R.id.sharewithfriends, R.id.register, R.id.login);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.personal: // 跳转到个人信息
			if (mHasLogin) {
				MobclickAgent.onEvent(mActivity, "1019");
				intent.setClass(this, PersonalInfoActivity.class);
				startActivity(intent);
			} else {
				IntentUtil.startLoginActivity(PersonalActivity.this, RequestCodeConstant.USER_CENTER);
			}
			break;

		case R.id.cart: // 购物车
		/*	MobclickAgent.onEvent(mActivity, "1024");
			intent.setClass(this, ShoppingCartActivity.class);
			startActivity(intent);*/
			break;

		case R.id.orderform: // 我的订单
			MobclickAgent.onEvent(mActivity, "1025");
			mHasLogin = UserModule.isLogin(mActivity);// 判断用户是否登陆
			if (mHasLogin) {
				intent.setClass(PersonalActivity.this, OrderListActivity.class);
				startActivity(intent);
			} else {
				IntentUtil.startLoginActivity(PersonalActivity.this, RequestCodeConstant.USER_CENTER);
			}
			break;

		case R.id.trial: // 我的试用
			MobclickAgent.onEvent(mActivity, "1022");
			if (mHasLogin) {
				intent.setClass(this, TrialOfMineActivity.class);
				intent.putExtra("title", getResources().getString(R.string.trial_my));
				startActivity(intent);
			} else {
				IntentUtil.startLoginActivity(mActivity);
			}
			break;

		case R.id.my_collection: // 我的收藏
			MobclickAgent.onEvent(mActivity, "1021");
			if (mHasLogin) {
				intent.setClass(this, CollectionsActivity.class);
				intent.putExtra("title", getResources().getString(R.string.collections));
				startActivity(intent);
			} else {
				IntentUtil.startLoginActivity(mActivity);
			}
			break;

		case R.id.setings: // 设置
			MobclickAgent.onEvent(mActivity, "1020");
			intent.setClass(this, SettingsActivity.class);
			intent.putExtra("title", getResources().getString(R.string.settings));
			startActivity(intent);
			break;

		case R.id.sharewithfriends: // 分享给好友
			MobclickAgent.onEvent(mActivity, "1023");
			mShare.setShare(getString(R.string.sharewithfriends_content), getString(R.string.sharewithfriends_content_msg), getString(R.string.sharewithfriends_title), R.drawable.icon,
					ApiUrl.getShareUrl(getString(R.string.sharewithfriends_url)));
			break;

		case R.id.register: // 注册
			MobclickAgent.onEvent(mActivity, "1018");
			intent.setClass(this, VerifyCodeGetActivity.class);
			intent.putExtra(IntentFlag.INTENT_FROM, IdentifyCodeType.register);
			startActivity(intent);
			break;

		case R.id.login: // 登录
			MobclickAgent.onEvent(mActivity, "1018");
			IntentUtil.startLoginActivity(PersonalActivity.this, RequestCodeConstant.USER_CENTER);
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
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

}
