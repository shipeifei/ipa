package com.ipassistat.ipa.ui.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.AddresssReceiveResponse;
import com.ipassistat.ipa.bean.response.UserInfoResponse;
import com.ipassistat.ipa.bean.response.entity.UserInfoSaveEntity;
import com.ipassistat.ipa.business.HmlShoppingCartController;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.ui.activity.MiniInstroduceActivity;
import com.ipassistat.ipa.util.ApiUrl;
import com.ipassistat.ipa.view.HmlBaseView;

/**
 * 微公社
 * 
 * @author lxc
 * 
 */
public class TinyCommunityFragment extends BaseFragment {

	private View view;
	private WebView mWebView;
	private HmlBaseView mBaseView;
	private Activity mActivity;
	private ImageView mLoadingImageView;
	private UserModule mUserModule;
	private boolean isFirst = true;
	private UserInfoSaveEntity mUserSaveInfo;
	private Context mContext;
	private String currentUserToken;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tiny_commu, null);
		mUserModule = new UserModule(this);
		mUserSaveInfo = new UserInfoSaveEntity();
		mContext = getActivity();
		needFlush();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			if (!isFirst)
				needFlush();
			isFirst = false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void initWidgets() {
		// mWebView = (WebView) view.findViewById(R.id.webview);// 获得网络视图
		// mBaseView = (HmlBaseView) view.findViewById(R.id.view_base);
		// mWebView = new WebView(getActivity());
		// mBaseView.setContentView(mWebView);
		// ViewUtil.configWebView(mWebView);
		mWebView = (WebView) view.findViewById(R.id.webview);
		mLoadingImageView = (ImageView) view.findViewById(R.id.image_loading);
		mWebView.getSettings().setJavaScriptEnabled(true); // 设置支持JavaScript

		// 自适应屏幕
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.getSettings().setDomStorageEnabled(true);
		final AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImageView
				.getDrawable();
		animationDrawable.start();
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

				// mBaseView.showLoadingView(true);
				/*mBaseView.showContentView(true);
				mBaseView.dismissMessageView(true);
				mBaseView.showCustomView(false, null);*/
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				animationDrawable.stop();
				mWebView.setVisibility(View.VISIBLE);
				/*	mBaseView.showLoadingView(false);
					mBaseView.dismissMessageView(true);*/

			}

		});

		String versionName = "nuknow";
		try {
			versionName = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		String url = ApiUrl.getInStance().getTinyCommuUrl() + "web_api_key=" + ConfigInfo.APIKEY
				+ "&web_api_token=" + currentUserToken + "&web_api_appversion=android"
				+ versionName;

		mWebView.loadUrl(url);

		// 将本地方法传递给JS 调用别名：android,notify
		mWebView.addJavascriptInterface(new JavaScriptinterface(), "android");
		/**
		 * 由于微公社登陆接口未返回memberinfo对象，暂时使用本地登陆方式
		 */
		mWebView.addJavascriptInterface(new JavaScriptinterfaceLogin(), "notify");

	}

	/**
	 * js要掉用的对象所在的
	 */
	final class JavaScriptinterface {

		@JavascriptInterface
		public void intentToInstroduce(int status) {
			/**
			 * js处调用的方法
			 */
			switch (status) {
			case 1:
				// 跳转推荐页面
				Intent intent = new Intent(getActivity(), MiniInstroduceActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	}

	/**
	 * js要掉用的登陆方法
	 */
	final class JavaScriptinterfaceLogin {

		@JavascriptInterface
		public void notifyOnAndroid(String json) {
			try {
				String type = (String) new JSONObject(json).get("type");
				if ("user_login".equals(type)) {
					JSONObject object = (JSONObject) new JSONObject(json).get("obj");
					if (object != null && !"{}".equals(object) && !"".equals(object)) {
						String mAccount = object.getString("user_phone");
						String mUserToken = object.getString("user_token");
						mUserSaveInfo.setmAccount(mAccount);
						mUserSaveInfo.setmUserToken(mUserToken);
						mUserModule.saveUserLoginMessage(mUserSaveInfo, mContext);
					}
					mUserModule.getAddressList(mContext);
					// 登录成功后同步购物车
					HmlShoppingCartController.instance(mContext).syncCartFromSever(false);
					mUserModule.postUserInfo(mContext);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (url.equals(ConfigInfo.API_ADDR_REC_NAME)) {
			AddresssReceiveResponse addresssReceiveResponse = (AddresssReceiveResponse) object;
			mUserModule.saveUserDefaultAdd(mContext, addresssReceiveResponse);
		} else if (url.equals(ConfigInfo.API_USERINFO_GET)) {
			UserInfoResponse mUserInfoResponse = (UserInfoResponse) object;
			if (mUserInfoResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				// 加Token的用户信息
				mUserModule.saveMemberInfo(mUserSaveInfo, mUserInfoResponse, mContext);
			}
		}
	}

	public void needFlush() {
		currentUserToken = UserModule.getUserToken(mActivity);
		initWidgets();
	}

}
