package com.ipassistat.ipa.ui.activity;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.share.um.ShareUM;
import com.ipassistat.ipa.view.HmlBaseView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 通用的webview页面
 * 
 * @author renheng
 * 
 */
public class WebViewActivity extends BaseActivity {
	private TitleBar titlebar;
	private WebView mWebView;
	private Context mContext = this;
	private HmlBaseView mBaseView;
	private String mTitle;
	private String mUrl;
	/** 传过来的UM埋点 */
	private String mUmCode;
	/** 返回埋点 */
	private String mBackCode;
	/** 分享埋点 */
	private String mShareCode;
	private ShareInfoEntity mShareInfoEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_official_info);
		getIntentData();
		initViews();
		initNet();

	}

	/**
	 * 网络状态判断
	 */
	private void initNet() {
		if (NetUtil.isNetworkConnected(mContext)) {
			initWeb();
		} else {
			mBaseView.showCustomView(true, new OnClickListener() {

				@Override
				public void onClick(View v) {
					initNet();
				}
			}, drawable.error_no_net_top, drawable.error_no_net);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		if (!TextUtils.isEmpty(mUmCode) && !mUmCode.equals("0")) {
			MobclickAgent.onPageStart(mUmCode); // 统计页面
			MobclickAgent.onResume(this); // 统计时长
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.onPause();
		if (!TextUtils.isEmpty(mUmCode) && !mUmCode.equals("0")) {
			// MobclickAgent.onPageEnd("1028"); // 保证 onPageEnd 在onPause 之前调用,因为
			MobclickAgent.onPageEnd(mUmCode); // 保证 onPageEnd 在onPause 之前调用,因为
												// onPause 中会保存信息
			MobclickAgent.onPause(this);
		}
	}

	/**
	 * 获得数据
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(IntentFlag.OFFICIAL_TITLE);
		mUrl = intent.getStringExtra(IntentFlag.OFFICIAL_URL);
		mUmCode = intent.getStringExtra(IntentFlag.WEBVIEW_BUNDLE_UMCODE);
		mBackCode = intent.getStringExtra(IntentFlag.OFFICIAL_BACK);
		mShareInfoEntity = (ShareInfoEntity) intent
				.getSerializableExtra(IntentFlag.SHAREINFO_ENTITY);
	}

	/**
	 * 初始化布局
	 */
	private void initViews() {
		initTitleBar();
		mBaseView = (HmlBaseView) findViewById(id.view_base);
		mWebView = new WebView(mContext);
		mBaseView.setContentView(mWebView);
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		titlebar = (TitleBar) findViewById(R.id.titlebar);
		titlebar.setTitleText(mTitle);
		Drawable drawable = getResources().getDrawable(R.drawable.button_goodsdetail_share);
		titlebar.setImageBackGround(TitleBarButton.rightImgv, drawable);
		titlebar.setVisibility(TitleBarButton.rightImgv, View.GONE);
		if (mShareInfoEntity != null) {
			titlebar.setVisibility(TitleBarButton.rightImgv, View.VISIBLE);
			if (mBackCode != null && !" ".equals(mBackCode)) {
				mBackCode = mShareInfoEntity.getBackCode();
			}
			mShareCode = mShareInfoEntity.getShareCode();
		}
		titlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBackCode != null && !" ".equals(mBackCode)) {
					MobclickAgent.onEvent(mContext, mBackCode);
				}
				if (mWebView != null && mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});
		titlebar.setButtonClickListener(TitleBarButton.rightImgv, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mShareCode != null && !" ".equals(mShareCode)) {
					MobclickAgent.onEvent(mContext, mShareCode);
				}
				share(mShareInfoEntity);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	/**
	 * 初始化WebView设置
	 */
	private void initWeb() {
		mWebView.loadUrl(mUrl);
		ViewUtil.configWebView(mWebView);

		// 有网络使用LOAD_DEFAULT，从网络加载
		if (NetUtil.isNetworkConnected(mContext)) {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		}
		// 无网络使用LOAD_CACHE_ELSE_NETWORK，从本地缓存加载
		else {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		mWebView.getSettings().setDomStorageEnabled(true);
		// ViewUtil.configWebView(mWebView);

		// webview 加载时的进度条
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mBaseView.showLoadingView(true);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mBaseView.showLoadingView(false);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.toLowerCase(Locale.getDefault()).startsWith(ConfigInfo.SCHEME)) {

					String queryString = url.substring(url.indexOf("?") + 1, url.length());
					if (!TextUtils.isEmpty(queryString)) {
						String parmas[] = queryString.split("&");
						HashMap<String, String> keyMap = new HashMap<String, String>();
						for (int i = 0; i < parmas.length; i++) {
							String content[] = parmas[i].split("=");
							String key = content[0];
							String value = content[1];
							keyMap.put(key, value);
						}

						// 说明: id == 商品SKU_CODE.
						// type == 商品类型 0:普通商品, 1: 限时抢购商品, 2: 试用商品
						/*IntentUtil.startGoodsDetail(view.getContext(), keyMap.get("id"),
								keyMap.get("type"));*/
						return true;
					}
				}

				return super.shouldOverrideUrlLoading(view, url);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView != null
				&& mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		} else {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 分享
	 * 
	 * @param shareInfoEntity
	 */
	private void share(ShareInfoEntity shareInfoEntity) {
		String targetUrl = shareInfoEntity.getTargetUrl();
		if (targetUrl == null || !targetUrl.startsWith("http://") || " ".equals(targetUrl)) {
			targetUrl = "";
		}
		ShareUM um = new ShareUM(this);
		String content = shareInfoEntity.getContent();
		if (content == null || "".equals(content)) {
			content = " ";
		}
		String SMSContent = shareInfoEntity.getSMSContent() + targetUrl;
		if (SMSContent == null || "".equals(SMSContent)) {
			SMSContent = " ";
		}
		String title = shareInfoEntity.getTitle();
		if (title == null || "".equals(title)) {
			title = " ";
		}
		String picUrl = shareInfoEntity.getPicUrl();
		if (picUrl == null || "".equals(picUrl)) {
			picUrl = " ";
		}
		if (picUrl != null && !" ".equals(picUrl)) {
			um.setShare(content, SMSContent, title, picUrl, targetUrl);
		} else {
			um.setShare(content, SMSContent, title, R.drawable.default_goods_img, targetUrl);
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
