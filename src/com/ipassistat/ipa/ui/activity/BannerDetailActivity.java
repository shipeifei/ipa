package com.ipassistat.ipa.ui.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;

/**
 * 广告详情页
 * 
 * @author lxc
 *
 */
public class BannerDetailActivity extends BaseActivity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner_detail);

		initWidgets();

	}

	@SuppressLint("JavascriptInterface")
	private void initWidgets() {
		TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitleText("专题");
		titleBar.setButtonClickListener(TitleBarButton.leftImgv, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
			}
		});

		mWebView = (WebView) findViewById(R.id.webview);// 获得网络视图
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true); // 设置支持JavaScript
		mWebView.getSettings().setSupportZoom(true); // 设置可以支持缩放
		mWebView.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
		mWebView.getSettings().setUseWideViewPort(true);// 扩大比例的缩放
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 自适应屏幕
		mWebView.getSettings().setLoadWithOverviewMode(true);

		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.toLowerCase().startsWith(ConfigInfo.SCHEME)) {
					
					String queryString = url.substring(url.indexOf("?") + 1, url.length());
					LogUtil.outLogDetail("-->>" + queryString);
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
						//IntentUtil.startGoodsDetail(BannerDetailActivity.this, keyMap.get("id"), keyMap.get("type"));
						return true;
					}
				}
				
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});

		String url = getIntent().getExtras().getString(IntentKey.BANNER_DETAIL_URL);
		mWebView.loadUrl(url);

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
