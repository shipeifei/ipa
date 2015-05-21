package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.FreeTryOutInfoResponse;
import com.ipassistat.ipa.bean.response.OrderPrePayResponse;
import com.ipassistat.ipa.bean.response.PayTryOutInfoResponse;
import com.ipassistat.ipa.bean.response.ProductShareStatusResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.business.OrderModule;
import com.ipassistat.ipa.business.ProductStateManager;
import com.ipassistat.ipa.business.TiralCenterModule;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.httprequest.ApiUrl;
import com.ipassistat.ipa.ui.fragment.SisterGroupFragment;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.DateUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.share.um.ShareUM;
import com.ipassistat.ipa.util.share.um.ShareUM.OnShareReq;
import com.ipassistat.ipa.view.CustomDigitalClock;
import com.ipassistat.ipa.view.DialogView;
import com.ipassistat.ipa.view.NoticeView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.CustomDigitalClock.ClockListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.ipassistat.ipa.view.pulldown.PullToRefreshScrollView;
import com.ipassistat.ipa.view.pulldown.PullToRefreshScrollView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 试用商品详情
 * 
 * 完成情况：数据显示基本完成， 逻辑跳转需要后台数据
 * 
 * @author maoyn
 * @author lis 重构
 * @date 2015-3-27
 * 
 */
public class TrialInfoActivity extends BaseActivity implements OnClickListener, OnShareReq,
		ClockListener, OnRefreshListener {

	/** 活动状态 */
	private int mStatus = 0;
	/** 页面标题 */
	private String mStringTitle = "";
	/** 标记是 免费试用，还是付邮试用 */
	private String isWhich = "";
	/** 商品唯一标识 */
	private String mSku_code = "";
	/** 结束时间 */
	private String mEndTime = "";
	/** titleBar 上的分享按钮 true ;如果是申请试用的分享 false */
	public boolean isTitleBarShare = true;
	/** 上下文 */
	private Activity mActivity;
	/** 标题栏 */
	private TitleBar mTitlebar;
	/** 商品的价值（原价） */
	private TextView mProduct_price;
	/** 试用价 */
	private TextView mProduct_price_try;
	/** 邮费（付邮试用）,件数（免费试用） */
	private TextView mProduct_price_postage;
	/** 剩余商品件数 */
	private TextView mProduct_count;
	/** 显示试用状态的Button */
	private TextView mProduct_oper;
	/** 商品名称 */
	private TextView mProduct_name;
	/** 邮费 */
	private TextView mPostage_count;
	/** 试用须知 */
	private WebView mProduct_notfy_detial;
	/** 商品的图片 */
	private ImageView mProduct_image;
	/** 免费试用的倒计时 */
	private CustomDigitalClock mProduct_time;
	/** 中间钱部分的布局 */
	private LinearLayout mLinear_money;
	/** 预加载View */
	private NoticeView mNoticeView;
	/** 可上下拉ListView */
	private PullToRefreshScrollView mPullToRefreshView;
	/** 订单预结算 */
	private OrderPrePayResponse mPrePayResponse;
	/** 付邮试用返回数据 */
	private PayTryOutInfoResponse mPayResponse;
	/** 免费试用返回数据 */
	private FreeTryOutInfoResponse mFreeResponse;
	/** 分享状态的接收对象 */
	private ProductShareStatusResponse mSharedStatusResponse;
	/** 商品信息集合 */
	private ArrayList<PurchaseGoods> mOrderlist;
	/** 试用分享 */
	private ShareUM mShareUMApplay;
	/** Title分享 */
	private ShareUM mShareUMTitle;
	/** 收货地址 */
	private BeautyAddress mAddress;
	/** Bitmap工具类 */
	private BitmapUtils mBitmapUtil;
	/** 进行网络请求的对象 */
	private TiralCenterModule mModel;

	/** 免费试用的几种状态————申请试用，已申请，申请成功，已结束，去写试用报告 */
	private int[] mFreeImage = { R.drawable.trial_request, R.drawable.trial_already,
			R.drawable.trial_success, R.drawable.trial_end, R.drawable.trial_write_report };
	/** 付邮试用的几种状态————立即试用，已试用，已结束，去写试用报告 */
	private int[] mPayImage = { R.drawable.trial_now, R.drawable.trial_already_get,
			R.drawable.trial_end, R.drawable.trial_write_report };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial_info);

		mActivity = this;
		mModel = new TiralCenterModule(this);
		mShareUMApplay = new ShareUM(mActivity);
		mShareUMTitle = new ShareUM(mActivity);
		mBitmapUtil = BitmapOptionsFactory.newInstanceBitmapUtils(this,
				R.drawable.default_goodsdetail_img);
		getIntentData();
		initViews();
		registerListeners();
		initDatas();
		if (mSku_code != null && ProductStateManager.isProductChanged(mSku_code)) {
			ProductStateManager.notifyUiHasFlushed(mSku_code);
		}
	}

	/**
	 * 获取传入数据
	 */
	private void getIntentData() {
		Intent intentData = getIntent();
		isWhich = intentData.getStringExtra(IntentKey.TRYOUTINFO_WHICH);
		mSku_code = intentData.getStringExtra(IntentKey.TRYOUTINFO_SKU_CODE);
		mEndTime = intentData.getStringExtra(IntentKey.TRYOUTINFO_ENDTIME);
	}

	/**
	 * @discretion: 界面初始化
	 * @author: MaoYaNan
	 * @date: 2014-10-13 上午11:30:17
	 */
	private void initViews() {
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		initTitle();
		mNoticeView = (NoticeView) findViewById(R.id.noticeView);
		mNoticeView.showLoadingView(true);
		mPullToRefreshView = (PullToRefreshScrollView) findViewById(R.id.refresh_root);
		mProduct_image = (ImageView) findViewById(R.id.product_image);
		mProduct_name = (TextView) findViewById(R.id.product_name);
		mProduct_price = (TextView) findViewById(R.id.product_price);
		mProduct_price_try = (TextView) findViewById(R.id.product_price_try);
		mPostage_count = (TextView) findViewById(R.id.postage_count);
		mProduct_price_postage = (TextView) findViewById(R.id.product_price_postage);
		mProduct_time = (CustomDigitalClock) findViewById(R.id.product_time);
		mProduct_count = (TextView) findViewById(R.id.product_count);
		mProduct_oper = (TextView) findViewById(R.id.product_oper);
		mProduct_notfy_detial = (WebView) findViewById(R.id.product_notfy_detial_webview);
		mLinear_money = (LinearLayout) findViewById(R.id.linear_money);
		mProduct_oper.setEnabled(false); // 不可点击
		setProductShowEnable(false);
	}

	/**
	 * 初始化Title
	 */
	private void initTitle() {
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mStringTitle = getString(R.string.title_free_tryout);
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			mStringTitle = getString(R.string.title_postage_tryout);
		}
		mTitlebar.setTitleText(mStringTitle);
	}

	/**
	 * @discretion:设置商品的显示信息是否可以点击
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午6:37:31
	 * @param isEnable
	 */
	public void setProductShowEnable(boolean isEnable) {
		mProduct_image.setEnabled(isEnable);
		mProduct_name.setEnabled(isEnable);
		mLinear_money.setEnabled(isEnable);
	}

	/**
	 * @discretion: 注册监听器
	 * @author: MaoYaNan
	 * @date: 2014-10-13 上午11:30:28
	 */
	private void registerListeners() {
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constant.TRIAL_FREE.equals(isWhich)) {
					MobclickAgent.onEvent(mActivity, "1054");
				} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
					MobclickAgent.onEvent(mActivity, "1050");
				}
				finish();
			}
		});
		mTitlebar.setImageBackGround(TitleBarButton.rightTextView,
				getResources().getDrawable(R.drawable.button_goodsdetail_share));
		mTitlebar.setVisibility(TitleBarButton.rightTextView, View.VISIBLE);
		mTitlebar.setButtonClickListener(TitleBarButton.rightTextView, new OnClickListener() {

			@Override
			public void onClick(View v) {
				isTitleBarShare = true;
				share();
			}
		});
		mProduct_image.setOnClickListener(this);
		mProduct_name.setOnClickListener(this);
		mLinear_money.setOnClickListener(this);
		mProduct_oper.setOnClickListener(this);
		mShareUMApplay.setOnShareReq(this);
		mPullToRefreshView.setOnRefreshListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mSku_code != null && ProductStateManager.isProductChanged(mSku_code)) {
			initDatas();
			ProductStateManager.notifyUiHasFlushed(mSku_code);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			MobclickAgent.onPageStart("1022"); // 统计页面
			MobclickAgent.onResume(this);
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			MobclickAgent.onPageStart("1023"); // 统计页面
			MobclickAgent.onResume(this);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			MobclickAgent.onPageEnd("1022"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
			MobclickAgent.onPause(this);
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			MobclickAgent.onPageEnd("1023"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
			MobclickAgent.onPause(this);
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (IntentFlag.ADDRESS_RECEIVE_ACTIVITY.equals(intent
				.getStringExtra(IntentFlag.TRIAL_INFO_FLAG_FROM_ACTIVITY))) {
			boolean booleanExtra = intent.getBooleanExtra(IntentFlag.IS_OPERATION_FLAG, false);
			if (booleanExtra) {
				mAddress = new UserModule(this).getUserDefaultAddres(mActivity);
				if (mAddress != null) { // 警告框显示
					setAddress(true);
				}
			}
		}
	}

	/**
	 * @discretion: 请求网络数据
	 * @author: MaoYaNan
	 * @date: 2014-9-26 下午3:55:06
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initDatas() {
		mNoticeView.showLoadingView(true);
		mProduct_oper.setEnabled(false);
		setProductShowEnable(false);
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mModel.postFreeTryOutInfo(mActivity, mSku_code, mEndTime,
					getResources().getDrawable(R.drawable.default_goodsdetail_img)
							.getMinimumWidth());
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			mModel.postPayTryOutInfo(mActivity, mSku_code, mEndTime,
					getResources().getDrawable(R.drawable.default_goodsdetail_img)
							.getMinimumWidth());
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (isFinishing())
			return;
		if (ConfigInfo.API_PAYTRYOUTINFO.equals(url)) { // 获取付邮试用的信息
			mPullToRefreshView.onRefreshComplete();
			mNoticeView.close();
			mPayResponse = (PayTryOutInfoResponse) object;
			if (mPayResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				mPayResponse.setFieldServerEnd(mPayResponse.getTime());
				setPostageData();
			} else {
				LogUtil.outLogDetail("resultCode != 1");
			}
		} else if (ConfigInfo.API_FREETRYOUTINFO.equals(url)) { // 获取免费试用的信息
			mPullToRefreshView.onRefreshComplete();
			mNoticeView.close();
			mFreeResponse = (FreeTryOutInfoResponse) object;
			if (mFreeResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				mFreeResponse = DateUtil.setEndTime(FreeTryOutInfoResponse.class, "time",
						"systemtime", mFreeResponse);
				setFreeData();
			} else {
				LogUtil.outLogDetail("resultCode != 1");
			}
		} else if (ConfigInfo.API_PRODUCTSHARESTATUS.equals(url)) { // 获得分享状态
			mSharedStatusResponse = (ProductShareStatusResponse) object;
			if (mSharedStatusResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				judgeSharedStatus();
			} else {
				LogUtil.outLogDetail("resultCode != 1");
			}
		} else if (ConfigInfo.API_PRODUCTSHARE.equals(url)) { // 提交分享状态
			BaseResponse baseResponse = (BaseResponse) object;
			if (baseResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				judgeTryOut();
			} else {
				LogUtil.outLogDetail("resultCode != 1");
			}
		} else if (ConfigInfo.API_TRYOUTAPPLYAPI.equals(url)) { // 申请免费试用
			ToastUtil.showToast(mActivity, getResources().getString(R.string.tryout_apply_success));
			initDatas();
		} else if (ConfigInfo.API_ORDER_SETTLEMENT.equals(url)) {
			mPrePayResponse = (OrderPrePayResponse) object;
			if (mPrePayResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				if (mOrderlist != null && mOrderlist.get(0) != null) {
					String postage = mPrePayResponse.getPostage();
					if (TextUtils.isEmpty(postage) || Double.parseDouble(postage) == 0) {
						mPrePayResponse.setPostage(mPayResponse.getPostage());
					}
					IntentUtil.startOrderConfirmActivity(mActivity, IntentFlag.POSTAGETRYOUT,
							mOrderlist.get(0), mPrePayResponse);
				}
			} else if (mPrePayResponse.getResultCode() == ConfigInfo.RESULT_LOW_STOCKS) {
				ToastUtil.showToast(mActivity, getResources().getString(R.string.tryout_lowstocks));
				initDatas();
			} else {
				LogUtil.outLogDetail("预结算调用接口失败");
			}
		} else {
			LogUtil.outLogDetail("url 未找到");
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
	}

	/**
	 * @discretion: 区分点击是哪个分享按钮
	 * @author: MaoYaNan
	 * @date: 2014-10-10 下午6:32:14
	 */
	private void judgeTryOut() {
		LogUtil.outLogDetail(isTitleBarShare);
		applay();
	}

	/**
	 * @discretion: 申请试用
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午8:32:50
	 */
	private void applay() {
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mAddress = new UserModule(this).getUserDefaultAddres(mActivity);
			setAddress(mAddress != null ? true : false); // 设置收货地址
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			orderSettlement();
		}
	}

	/**
	 * @discretion: 调用预结算接口
	 * @author: MaoYaNan
	 * @date: 2014-10-20 上午11:24:15
	 */
	private void orderSettlement() {
		mOrderlist = new ArrayList<PurchaseGoods>();
		PurchaseGoods good = new PurchaseGoods();
		good.setSku_code(mPayResponse.getSku_code());
		good.setOrder_count("1");
		good.setGoods_name(mPayResponse.getName());
		good.setPrice("0");
		good.setPic_url(mPayResponse.getPhoto());
		good.setProduct_type(ConfigInfo.TYPEPAYTRYOUT);
		mOrderlist.add(good);
		new OrderModule(this).postOrderSettlement(mActivity, mOrderlist, "0",
				ConfigInfo.TYPEPAYTRYOUT);
	}

	/**
	 * @discretion: 设置免费试用的收货地址
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午8:59:30
	 */
	private void setAddress(Boolean hasDefault) {
		if (hasDefault && mAddress != null) { // 有收货地址时
			String address = mAddress.getProvinces() + mAddress.getStreet();
			showDialogWithAddress(address); // 有收货地址的警告框
		} else {
			showDialogWithOutAddress(); // 没有收货地址的警告框
		}
	}

	/**
	 * @discretion: 没有收货地址时的提示
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午10:25:22
	 */
	private void showDialogWithOutAddress() {
		DialogView.getAlertDialogWithTitle(mActivity,
				getString(R.string.dialog_freetryout_address_title),
				getString(R.string.dialog_freetryout_address_message),
				getString(R.string.dialog_freetryout_address_button_setAddress),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAddressReceiveActivity();
					}
				}, getString(R.string.dialog_freetryout_address_button_setCancle),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
	}

	/**
	 * 收货地址的弹框
	 * 
	 * @param address
	 */
	private void showDialogWithAddress(String address) {
		if (mTitlebar == null)
			return;
		DialogView.getAlertDialogWithTitle(this,
				getString(R.string.dialog_freetryout_makesuraddress_title), address,
				getString(R.string.dialog_freetryout_address_button_right),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAddressReceiveActivity();
					}
				}, getString(R.string.dialog_freetryout_address_button_left),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mModel.postFreeApply(mActivity, mFreeResponse.getActivityCode(), mSku_code,
								mFreeResponse.getName(), mAddress, mEndTime); // 提交申请免费试用
					}
				});
	}

	/**
	 * @discretion: 打开收货地址界面
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午4:23:06
	 * @param activity
	 */
	private void startAddressReceiveActivity() {
		Intent intent = new Intent();
		intent.setClass(mActivity, AddressReceiveActivity.class);
		intent.putExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY, IntentFlag.TRAOUTINFO);
		startActivity(intent);
	}

	/**
	 * @discretion: 分享按钮被点击后，分享的 数据
	 * @author: MaoYaNan
	 * @date: 2014-10-10 下午6:31:39
	 */
	private void share() {
		if (UserModule.isLogin(mActivity)) {
			ShareUM mShareUM = null;
			if (Constant.TRIAL_FREE.equals(isWhich)) {
				if (isTitleBarShare) {
					mShareUM = mShareUMTitle;
				} else {
					mShareUM = mShareUMApplay;
				}
				MobclickAgent.onEvent(mActivity, "1053", mSku_code);
				TiralCenterModule.shareTryOutFree(mActivity, mShareUM, mFreeResponse.getName(),
						mFreeResponse.getPhoto(), ApiUrl.getShareUrl(mFreeResponse.getLinkUrl()));
			} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
				if (isTitleBarShare) {
					mShareUM = mShareUMTitle;
				} else {
					mShareUM = mShareUMApplay;
				}
				MobclickAgent.onEvent(mActivity, "1049", mSku_code);
				TiralCenterModule.shareTryOutPostage(mActivity, mShareUM, mPayResponse.getName(),
						mPayResponse.getPhoto(), ApiUrl.getShareUrl(mPayResponse.getLinkUrl()));
			}
		} else {
			IntentUtil.startLoginActivity(mActivity, RequestCodeConstant.TRIAL_INFO); // 调到登录界面
		}
	}

	/**
	 * @discretion: 根据分享状态的不同跳转到不同的页面
	 * @author: MaoYaNan
	 * @date: 2014-9-28 下午5:37:31
	 */
	private void judgeSharedStatus() {
		String status = mSharedStatusResponse.getStatus();
		if (Constant.UNSHARED.equals(status)) { // 未分享
			showShareDialog(); // 显示试用分享窗口
		} else if (Constant.SHARED.equals(status)) { // 已分享
			applay(); // 收货地址
		} else {
			LogUtil.outLogDetail("状态出错");
		}
	}

	/**
	 * @discretion: 显示分享的对话框
	 * @author: MaoYaNan
	 * @date: 2014-9-28 下午5:39:39
	 */
	private void showShareDialog() {
		DialogView.getAlertDialogWithTitle(this, getString(R.string.tryout_dialog_share_title),
				getString(R.string.tryout_dialog_share_message),
				getString(R.string.tryout_dialog_share_button_left),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}, getString(R.string.tryout_dialog_share_button_right),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						share();
					}
				});
	}

	/**
	 * @discretion: 填充付邮试用的数据
	 * @author: MaoYaNan
	 * @date: 2014-10-13 上午11:30:54
	 */
	private void setPostageData() {
		String temp_price = "￥" + mPayResponse.getOld_price();
		String temp_price_try = "￥" + mPayResponse.getTryout_price();
		String temp_price_postage = "￥" + mPayResponse.getPostage();
		String temp_count = "剩余" + mPayResponse.getSurplus_count() + "/" + mPayResponse.getCount()
				+ "件";
		mProduct_name.setText(mPayResponse.getName());
		mProduct_price.setText(ViewUtil.getTextSizeStyle(temp_price, 0, 1, 14));
		mProduct_price_try.setText(ViewUtil.getTextSizeStyle(ViewUtil.getTextColorStyle(
				temp_price_try, 0, temp_price_try.length(),
				getResources().getColor(R.color.global_price_text_color)), 0, 1, 14));
		mPostage_count.setText("邮费");
		mProduct_price_postage.setText(ViewUtil.getTextSizeStyle(ViewUtil.getTextColorStyle(
				temp_price_postage, 0, temp_price_postage.length(),
				getResources().getColor(R.color.goods_type_unclick)), 0, 1, 14));
		mProduct_time.setVisibility(View.GONE);
		Drawable drawable = getResources().getDrawable(R.drawable.trial_surplus);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mProduct_count.setCompoundDrawables(drawable, null, null, null);
		mProduct_count.setText(ViewUtil.getTextSizeStyle(ViewUtil.getTextColorStyle(temp_count,
				temp_count.indexOf("余") + 1, temp_count.indexOf("/"),
				getResources().getColor(R.color.goods_type_unclick)), temp_count.indexOf("余") + 1,
				temp_count.indexOf("/"), 16));
		judgePostageTryStatus(mPayResponse.getStatus());
		mProduct_oper.setBackgroundResource(mPayImage[mStatus]);
		if (mProduct_image != null) {
			mBitmapUtil.display(mProduct_image, mPayResponse.getPhoto());
		}
		mProduct_notfy_detial.loadDataWithBaseURL(null, mPayResponse.getDescribe(), "text/html",
				"utf-8", null);
		setProductShowEnable(true);
	}

	/**
	 * @discretion: 填充免费试用的数据
	 * @author: MaoYaNan
	 * @date: 2014-10-13 上午11:30:37
	 */
	private void setFreeData() {
		String temp_price = "￥" + mFreeResponse.getOld_price();
		String temp_price_try = "￥" + mFreeResponse.getTryout_price();
		String temp_remain = mFreeResponse.getSurplus_count();
		mProduct_name.setText(mFreeResponse.getName());
		mProduct_price.setText(ViewUtil.getTextSizeStyle(temp_price, 0, 1, 14));
		mProduct_price_try.setText(ViewUtil.getTextSizeStyle(ViewUtil.getTextColorStyle(
				temp_price_try, 0, temp_price_try.length(),
				getResources().getColor(R.color.global_price_text_color)), 0, 1, 14));
		mPostage_count.setText("件数");
		mProduct_price_postage.setText(temp_remain);
		mProduct_time.setVisibility(View.VISIBLE);
		mProduct_time.setEndTime(mFreeResponse.getTime()); // 设置倒计时数据
		mProduct_time.setClockListener(this);
		Drawable drawable = getResources().getDrawable(R.drawable.account_imgv);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mProduct_count.setCompoundDrawables(drawable, null, null, null);
		mProduct_count.setText(mFreeResponse.getTryout_count() + "人已申请");
		// 设置状态
		judgeFreeTryStatus(mFreeResponse.getStatus());
		mProduct_oper.setBackgroundResource(mFreeImage[mStatus]);
		if (mFreeResponse.getPhoto() != null) {
			mBitmapUtil.display(mProduct_image, mFreeResponse.getPhoto());
		}
		mProduct_notfy_detial.setEnabled(false);
		mProduct_notfy_detial.loadDataWithBaseURL(null, mFreeResponse.getDescribe(), "text/html",
				"utf-8", null);
		setProductShowEnable(true);
	}

	/**
	 * @discretion: 判断免费试用的状态
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午6:05:44
	 * @param status
	 *            服务器返回的状态
	 * @return
	 */
	private void judgeFreeTryStatus(String status) {
		if (status != null && !"".equals(status)) {
			if (ConfigInfo.TRYOUT_FREE_STATUS_NOTAPPLY.equals(status)) {
				mStatus = 0;
			} else if (ConfigInfo.TRYOUT_FREE_STATUS_ALEADYAPPLY.equals(status)) {
				mStatus = 1;
			} else if (ConfigInfo.TRYOUT_FREE_STATUS_APPLYSUCCESS.equals(status)) {
				mStatus = 2;
			} else if (ConfigInfo.TRYOUT_FREE_STATUS_END.equals(status)) {
				mStatus = 3;
			} else if (ConfigInfo.TRYOUT_FREE_STATUS_ALREADYGET.equals(status)) {
				mStatus = 4;
			} else {
				mStatus = 0; // 超出范围时，默认显示 申请试用
			}
		} else {
			mStatus = 0;
		}
		judgeClickable();
		if (mStatus == (mFreeImage.length - 1) || mStatus == (mFreeImage.length - 2)) { // 免费试用为已结束,和去写试用报告时倒计时组件消失
			mProduct_time.setVisibility(View.GONE);
		}
	}

	/**
	 * @discretion: 判断付邮试用的试用状态
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午6:09:11
	 * @param status
	 * @return
	 */
	private void judgePostageTryStatus(String status) {
		if (status != null && !"".equals(status)) {
			if (ConfigInfo.TRYOUT_POSTAGE_STATUS_NOTAPPLY.equals(status)) {
				mStatus = 0;
			} else if (ConfigInfo.TRYOUT_POSTAGE_STATUS_ALREADAPPLY.equals(status)) {
				mStatus = 1;
			} else if (ConfigInfo.TRYOUT_POSTAGE_STATUS_END.equals(status)) {
				mStatus = 2;
			} else if (ConfigInfo.TRYOUT_POSTAGE_STATUS_ALREADYGET.equals(status)) {
				mStatus = 3;
			} else {
				mStatus = 0; // 超出范围时，默认显示 申请试用
			}
		} else {
			mStatus = 0;
		}
		judgeClickable();
	}

	/**
	 * @discretion: 判断按钮是否可以点击
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午6:12:58
	 * @param temp
	 */
	private void judgeClickable() {
		if (mStatus == 0) {
			mProduct_oper.setEnabled(true); // 可点击
		} else if ((Constant.TRIAL_FREE.equals(isWhich) || Constant.TRIAL_POSTAGE.equals(isWhich))
				&& mStatus == mFreeImage.length - 1) {
			mProduct_oper.setEnabled(true); // 可点击
		} else {
			mProduct_oper.setEnabled(false); // 不可点击
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_image:
		case R.id.product_name:
		case R.id.linear_money:
			/*if (Constant.TRIAL_FREE.equals(isWhich)) {
				MobclickAgent.onEvent(mActivity, "1051", mSku_code);
				IntentUtil.startGoodsDetail(mActivity, mSku_code, Constant.GOODS_TYPE_FREE,
						mEndTime);
			} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
				MobclickAgent.onEvent(mActivity, "1047", mSku_code);
				IntentUtil.startGoodsDetail(mActivity, mSku_code, Constant.GOODS_TYPE_POST,
						mEndTime);
			}*/
			break;
		case R.id.product_oper:
			if (UserModule.isLogin(mActivity)) { // 已经登录，请求分享状态
				if (mStatus == 0) { // 申请试用
					if (Constant.TRIAL_FREE.equals(isWhich)) {
						MobclickAgent.onEvent(mActivity, "1142");
					} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
						MobclickAgent.onEvent(mActivity, "1143");
					}
					isTitleBarShare = false;
					getSharedStatus();
				} else { // 去写试用报告，因为其他状态下不可点击
					if (Constant.TRIAL_FREE.equals(isWhich)) {
						MobclickAgent.onEvent(mActivity, "1052");
					} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
						MobclickAgent.onEvent(mActivity, "1048");
					}
					jumpToEditPostActivity();
				}
			} else { // 未登录，调用登录界面
				IntentUtil.startLoginActivity(mActivity, RequestCodeConstant.TRIAL_INFO); // 调到登录界面
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @discretion: 跳到发帖界面
	 * @author: MaoYaNan
	 * @date: 2014-10-21 下午10:15:45
	 */
	private void jumpToEditPostActivity() {
		// 调到姐妹圈的发帖界面
		IntentUtil.startEditPostActivity(mActivity);
	}

	/**
	 * @discretion: 得到分享的状态
	 * @author: MaoYaNan
	 * @date: 2014-10-13 上午11:27:10
	 */
	private void getSharedStatus() {
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mModel.postSharaStatus(mActivity, mSku_code, mFreeResponse.getFieldServerEnd());
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			mModel.postSharaStatus(mActivity, mSku_code, mPayResponse.getFieldServerEnd());
		}
	}

	/**
	 * 分享成功
	 */
	@Override
	public void onShareSuccess() {
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mModel.postProductShare(mActivity, "", mSku_code, mFreeResponse.getFieldServerEnd());
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			mModel.postProductShare(mActivity, "", mSku_code, mPayResponse.getFieldServerEnd());
		}
	}

	@Override
	public void onShareFail() {

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (Activity.RESULT_OK == arg1) {
			switch (arg0) {
			case RequestCodeConstant.TRIAL_INFO: // 登录成功
				initDatas(); // 更新界面
				break;
			default:
				break;
			}
		} else {
			LogUtil.outLogDetail("not finded ActivityResult ");
		}
		if (arg1 == SisterGroupFragment.RESULT_CODE_SEND_SUCCESS
				&& arg0 == RequestCodeConstant.STARTEDITPOSTACTIVITY) {
			ToastUtil.showToast(mActivity,
					getResources().getString(R.string.write_tryout_report_success));
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mNoticeView.showCustomView(true, new OnClickListener() {

			@Override
			public void onClick(View v) {
				initDatas();
			}
		}, drawable.error_no_net_top, drawable.error_no_net);
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		super.onNoNet();
		mNoticeView.showCustomView(true, new OnClickListener() {

			@Override
			public void onClick(View v) {
				initDatas();
			}
		}, drawable.error_no_net_top, drawable.error_no_net);
	}

	@Override
	public void timeEnd() {
		if (Constant.TRIAL_FREE.equals(isWhich) && (mStatus == 0)) {
			mStatus = 3;
			mProduct_oper.setBackgroundResource(mFreeImage[mStatus]);
			judgeClickable();
			mProduct_time.setVisibility(View.GONE);
		}
	}

	@Override
	public void onRefresh() {
		if (Constant.TRIAL_FREE.equals(isWhich)) {
			mModel.postFreeTryOutInfo(mActivity, mSku_code, mEndTime,
					getResources().getDrawable(R.drawable.default_goodsdetail_img)
							.getMinimumWidth());
		} else if (Constant.TRIAL_POSTAGE.equals(isWhich)) {
			mModel.postPayTryOutInfo(mActivity, mSku_code, mEndTime,
					getResources().getDrawable(R.drawable.default_goodsdetail_img)
							.getMinimumWidth());
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
