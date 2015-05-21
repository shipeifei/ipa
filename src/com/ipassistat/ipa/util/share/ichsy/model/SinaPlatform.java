package com.ipassistat.ipa.util.share.ichsy.model;
//package com.ichsy.mboss.share.model;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.ichsy.mboss.R.drawable;
//import com.ichsy.mboss.R.string;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuth;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.openapi.LogoutAPI;
//import com.sina.weibo.sdk.openapi.StatusesAPI;
//
///**
// * 新浪的分享实体类
// * 
// * @author LiuYuHang
// * @date 2014年9月22日
// *
// */
//public class SinaPlatform extends SharePlatform {
//	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
//	public static final String APP_KEY = "2045436852";
//	/**
//	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
//	 * 
//	 * <p>
//	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
//	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
//	 * </p>
//	 */
//	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
//	/**
//	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
//	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
//	 * 
//	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
//	 * 
//	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
//	 * 
//	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
//	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
//	 */
//	public static final String SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog,"
//			+ "invitation_write";
//
//	/** 微博 Web 授权类，提供登陆等功能 */
//	private WeiboAuth mWeiboAuth;
//	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
//	private Oauth2AccessToken mAccessToken;
//	private SsoHandler mSsoHandler;
//	private StatusesAPI mStatusesAPI;
//
//	public SinaPlatform(Context context) {
//		super(context);
//		// 创建微博实例
//		mWeiboAuth = new WeiboAuth(context, APP_KEY, REDIRECT_URL, SCOPE);
//		mSsoHandler = new SsoHandler((Activity) context, mWeiboAuth);
//		mStatusesAPI = new StatusesAPI(mAccessToken);
//	}
//
//	@Override
//	public int type() {
//		return SHARE_TYPE_SINA;
//	}
//
//	@Override
//	public void shareText(String content) {
//		System.out.println("sina share text:" + content);
//		authorize();
//		mStatusesAPI.update(content, null, null, new RequestListener() {
//
//			@Override
//			public void onWeiboException(WeiboException arg0) {
//
//			}
//
//			@Override
//			public void onComplete(String arg0) {
//
//			}
//		});
//	}
//
//	@Override
//	public void shareImage(Bitmap bitmap) {
//		System.out.println("sina share bitmap:");
//	}
//
//	@Override
//	public String title() {
//		return "分享到微博";
//	}
//
//	@Override
//	public int iconId() {
//		return drawable.logo_sina;
//	}
//
//	/**
//	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
//	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
//	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
//	 * SharedPreferences 中。
//	 */
//	private class AuthListener implements WeiboAuthListener {
//
//		@Override
//		public void onComplete(Bundle values) {
//			// 从 Bundle 中解析 Token
//			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//			if (mAccessToken.isSessionValid()) {
//				// 显示 Token
//				// updateTokenView(false);
//
//				// 保存 Token 到 SharedPreferences
//				AccessTokenKeeper.writeAccessToken(context, mAccessToken);
//				Toast.makeText(context, string.oauth_success, Toast.LENGTH_SHORT).show();
//			} else {
//				// 以下几种情况，您会收到 Code：
//				// 1. 当您未在平台上注册的应用程序的包名与签名时；
//				// 2. 当您注册的应用程序包名与签名不正确时；
//				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//				String code = values.getString("code");
//				String message = context.getString(string.oauth_failed);
//				if (!TextUtils.isEmpty(code)) {
//					message = message + "\nObtained the code: " + code;
//				}
//				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onCancel() {
//			Toast.makeText(context, string.oauth_cancel, Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			Toast.makeText(context, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
//		}
//	}
//
//	@Override
//	public void authorize() {
//		mSsoHandler.authorize(new AuthListener());
//	}
//
//	@Override
//	public void loginOut() {
//		// 获取当前已保存过的 Token
//		mAccessToken = AccessTokenKeeper.readAccessToken(context);
////		if (mAccessToken != null && mAccessToken.isSessionValid()) {
////			String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(mAccessToken.getExpiresTime()));
////			String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
////			mTokenView.setText(String.format(format, mAccessToken.getToken(), date));
////		}
//
//		if (mAccessToken != null && mAccessToken.isSessionValid()) {
//			new LogoutAPI(mAccessToken).logout(new RequestListener() {
//
//				@Override
//				public void onWeiboException(WeiboException arg0) {
//
//				}
//
//				@Override
//				public void onComplete(String response) {
//					if (!TextUtils.isEmpty(response)) {
//						try {
//							JSONObject obj = new JSONObject(response);
//							String value = obj.getString("result");
//
//							if ("true".equalsIgnoreCase(value)) {
//								AccessTokenKeeper.clear(context);
//
//								// mTokenView.setText(R.string.weibosdk_demo_logout_success);
//								mAccessToken = null;
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		} else {
//			// mTokenView.setText(R.string.weibosdk_demo_logout_failed_1);
//		}
//	}
//}
