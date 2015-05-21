package com.ipassistat.ipa.util;

import java.util.ArrayList;

import android.R.anim;
import android.R.bool;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;
import com.ipassistat.ipa.bean.response.OrderPrePayResponse;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.EmailType;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.constant.IntentFlag.LoginType;
import com.ipassistat.ipa.ui.activity.AddressReceiveActivity;
import com.ipassistat.ipa.ui.activity.GoodsCommentActivity;
import com.ipassistat.ipa.ui.activity.LoginActivity;
import com.ipassistat.ipa.ui.activity.OrderConfirmActivity;
import com.ipassistat.ipa.ui.activity.OrderDetailActivity;
import com.ipassistat.ipa.ui.activity.OrderSubmitSuccessActivity;
import com.ipassistat.ipa.ui.activity.PayTypeChoiceActivity;
import com.ipassistat.ipa.ui.activity.PersonalActivity;
import com.ipassistat.ipa.ui.activity.PwdResetActivity;
import com.ipassistat.ipa.ui.activity.RecreationTvListActivity;
import com.ipassistat.ipa.ui.activity.SisterGroupDetailActivity;
import com.ipassistat.ipa.ui.activity.SisterGroupPostEditActivity;
import com.ipassistat.ipa.ui.activity.SisterGroupPostImageDetailActivity;
import com.ipassistat.ipa.ui.activity.TrialInfoActivity;
import com.ipassistat.ipa.ui.activity.WebViewActivity;
import com.ipassistat.ipa.ui.email.activity.EmailLoginActivity;
import com.ipassistat.ipa.ui.email.activity.EmailSelectActivity;
import com.ipassistat.ipa.ui.welcome.activity.GuideActivity;
import com.ipassistat.ipa.ui.welcome.activity.WelcomeActivity;

/**
 * 
 * @author LiuYuHang
 * @date 2014年9月28日
 * 
 */
public class IntentUtil {
	public static final String INTENT_URL_OF_IMAGE = "urlOfImage";
	public static final String INTENT_SISTER_POST = "intent_sister_post";
	public static final String INTENT_SISTER_POST_ISOFFICIAL = "intent_sister_post_official";// 是否是官方帖
	public static final String INTENT_URL_OF_IMAGEURLS = "intent_imageUrls";// 图片数组
	public static final String INTENT_SELECT = "intent_select";
	public static final String INTENT_FROM = "intent_from";// 跳转来源

	public static void startIntent(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		startIntent(context, intent);
	}

	public static void startIntent(Context context, Intent intent) {
		if (!(context instanceof Activity)) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}

	public static void startInent(Activity activity, Class<?> cls, int requestCode) {
		activity.startActivityForResult(new Intent(activity, cls), requestCode);
	}

	/**
	 * 打开一个webView
	 * 
	 * @param context
	 * @param title
	 * @param url
	 * @param umCode
	 *            友盟埋点
	 * 
	 *            Modifier： Modified Date： Modify：
	 * @param shareInfoEntity
	 *            分享信息内容类
	 */
	public static void startWebView(Context context, String title, String url, String umCode, ShareInfoEntity shareInfoEntity) {
		Intent intent = new Intent(context, WebViewActivity.class);
		if (shareInfoEntity != null) {
			intent.putExtra(IntentFlag.SHAREINFO_ENTITY, shareInfoEntity);
		}
		intent.putExtra(IntentFlag.OFFICIAL_TITLE, title);
		intent.putExtra(IntentFlag.OFFICIAL_URL, url);
		intent.putExtra(IntentFlag.WEBVIEW_BUNDLE_UMCODE, umCode);
		startIntent(context, intent);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param url
	 * @param umCode
	 *            友盟埋点
	 * @param backCode
	 *            友盟埋点
	 * @param shareInfoEntity
	 *            分享信息内容类
	 */
	public static void startWebView(Context context, String title, String url, String umCode, String backCode, ShareInfoEntity shareInfoEntity) {
		Intent intent = new Intent(context, WebViewActivity.class);
		if (shareInfoEntity != null) {
			intent.putExtra(IntentFlag.SHAREINFO_ENTITY, shareInfoEntity);
		}
		intent.putExtra(IntentFlag.OFFICIAL_TITLE, title);
		intent.putExtra(IntentFlag.OFFICIAL_URL, url);
		intent.putExtra(IntentFlag.OFFICIAL_BACK, backCode);
		intent.putExtra(IntentFlag.WEBVIEW_BUNDLE_UMCODE, umCode);
		startIntent(context, intent);

	}

	/**
	 * 跳转到姐妹圈- 帖子详情页
	 * 
	 * @param context
	 * @param post
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 */
	public static void startSisterGroupDetail(Context context, SisterGroupPostVo post) {
		Intent intent = new Intent(context, SisterGroupDetailActivity.class);
		intent.putExtra(INTENT_SISTER_POST, post);

		String isOfficial = post.isofficial;
		if (!TextUtils.isEmpty(isOfficial)) {
			intent.putExtra(INTENT_SISTER_POST_ISOFFICIAL, isOfficial.equals(ConfigInfo.POST_TAG_OFFICIAL));
		}
		// context.startActivity(intent);
		startIntent(context, intent);
	}

	/**
	 * 跳转到姐妹圈- 帖子详情页
	 * 
	 * @param context
	 * @param post
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 */
	public static void startSisterGroupDetail(Context context, String postCode) {
		SisterGroupPostVo post = new SisterGroupPostVo();
		post.post_code = postCode;
		startSisterGroupDetail(context, post);
	}

	/**
	 * 跳转到 图片大图页面
	 * 
	 * @param context
	 * @param urlOfImage
	 *            图片的URL
	 * @param imageUrls
	 *            帖子里面所有的圖片 增加：时培飞 2014-11-24
	 * @param from
	 *            来源，根据来源判断浏览图片布局的显示
	 * @author LiuYuHang
	 * @date 2014年10月8日
	 */
	public static void startImageDetail(Context context, String urlOfImage, ArrayList<String> imageUrls, String from) {
		Intent intent = new Intent(context, SisterGroupPostImageDetailActivity.class);
		intent.putExtra(INTENT_URL_OF_IMAGE, urlOfImage);
		intent.putExtra(INTENT_FROM, from);
		intent.putStringArrayListExtra(INTENT_URL_OF_IMAGEURLS, imageUrls);
		// context.startActivity(intent);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent(context, intent);

		if (from.equals("SisterGroupDetailAcvitity")) {
			SisterGroupDetailActivity.mActivitySisterGroupDetail.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}

	/**
	 * 跳转到登录页面
	 * 
	 * @param context
	 */
	public static void startLoginActivity(Context context) {
		// Intent intent = new Intent(context, LoginActivity.class);
		// if (!(context instanceof Activity)) {
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// }
		// context.startActivity(intent);

		startIntent(context, LoginActivity.class);
	}

	public static void startLoginActivity(Context context, int requestCode) {
		Intent intent = new Intent(context, LoginActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转到主界面
	 * 
	 * @author 时培飞 Create at 2014-12-2 下午1:46:56
	 */
	public static void intentToMainActivityOnTime() {
		Intent intent = new Intent(WelcomeActivity.welcomeActivity, GuideActivity.class);
		WelcomeActivity.welcomeActivity.startActivity(intent);
		WelcomeActivity.welcomeActivity.finish();
		WelcomeActivity.welcomeActivity.overridePendingTransition(anim.fade_in, anim.fade_out);
	}

	/**
	 * 
	 * @param context
	 * @param loginType
	 */
	public static void startLoginActivity(Context context, LoginType loginType) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.putExtra(IntentFlag.INTENT_FROM, loginType);
		// context.startActivity(intent);

		startIntent(context, intent);
	}

	/**
	 * 检查是否已登录并且打开登陆页面
	 * 
	 * @return 是否登录
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public static boolean checkAndLogin(Context context) {
		boolean isLogin = UserModule.isLogin(context);
		if (!isLogin) {
			startLoginActivity(context);
		}
		return isLogin;
	}

	/**
	 * @discretion: 登录到个人中心页面
	 * @author: MaoYaNan
	 * @date: 2014-10-11 下午2:11:24
	 * @param context
	 *            调用接口的界面
	 */
	public static void startPersonalCenterActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, PersonalActivity.class);
		intent.putExtra("title", context.getResources().getString(R.string.personal));
		// context.startActivity(intent);

		startIntent(context, intent);
	}

	/**
	 * @discretion: 跳转到试用商品详情
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午5:36:22
	 * @param context
	 * @param tag
	 *            标记是免费试用，还是付邮试用
	 * @param sku_code
	 *            商品编号
	 */
	public static void jumpToTrialInfoActivity(Context context, String tag, String sku_code, String endTime) {
		Intent intent = new Intent();
		intent.setClass(context, TrialInfoActivity.class);
		intent.putExtra(IntentKey.TRYOUTINFO_WHICH, tag);
		intent.putExtra(IntentKey.TRYOUTINFO_SKU_CODE, sku_code);
		intent.putExtra(IntentKey.TRYOUTINFO_ENDTIME, endTime);
		// context.startActivity(intent);

		startIntent(context, intent);
	}

	/**
	 * @discretion: 打开收货地址界面
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午4:23:06
	 * @param activity
	 */
	public static void startAddressReceiveActivity(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, AddressReceiveActivity.class);
		intent.putExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY, IntentFlag.ADDRESS_SELECT_REQUEST_VALUE);
		activity.startActivityForResult(intent, RequestCodeConstant.CHOOSEADDR);
	}

	/**
	 * @discretion: 打开密码重置窗口
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午4:23:21
	 * @param context
	 */
	public static void startPwdResetActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, PwdResetActivity.class);
		// context.startActivity(intent);

		startIntent(context, intent);
	}

	/**
	 * @discretion: 跳到姐妹圈的发帖界面
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午6:30:57
	 * @param context
	 */
	public static void startEditPostActivity(Activity context) {
		if (UserModule.isLogin(context)) {
			Intent intent = new Intent();
			intent.setClass(context, SisterGroupPostEditActivity.class);
			context.startActivityForResult(intent, RequestCodeConstant.STARTEDITPOSTACTIVITY);
		} else {
			startLoginActivity(context);
		}
	}

	/**
	 * @discretion:商品详情和试用中心跳到确认订单页面
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:08:45
	 * @param context
	 */
	public static void startOrderConfirmActivity(Context context, String intentflag, PurchaseGoods good, OrderPrePayResponse response) {
		Intent intentorder = new Intent(context, OrderConfirmActivity.class);
		intentorder.putExtra(IntentFlag.ORDER_CONFIRM_FLAG, intentflag);// 页面标识
		intentorder.putExtra(IntentKey.BUY_NOW_GOODS, good);// 商品对象
		intentorder.putExtra(IntentKey.BUY_NOW_PREPAY, response);// 预结算接口返回数据对象

		// context.startActivity(intentorder);

		startIntent(context, intentorder);
	}

	/**
	 * @discretion 购物车跳到确认订单页面
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:08:45
	 * @param context
	 */
	public static void startOrderConfirmActivity(Context context, String intentflag, ArrayList<? extends Parcelable> list, OrderPrePayResponse response) {
		Intent intentorder = new Intent(context, OrderConfirmActivity.class);
		intentorder.putExtra(IntentFlag.ORDER_CONFIRM_FLAG, intentflag);// 页面标识
		intentorder.putParcelableArrayListExtra(IntentKey.SHOPPINGCART_GOODS, list);
		intentorder.putExtra(IntentKey.BUY_NOW_PREPAY, response);// 预结算接口返回数据对象

		// context.startActivity(intentorder);
		startIntent(context, intentorder);
	}

	/**
	 * 跳转到商品评价页面
	 * 
	 * @param context
	 * @param thumbUrl
	 *            商品缩略图URL
	 * @param name
	 *            商品名称
	 * @param describe
	 *            商品描述
	 * @param orderTime
	 *            订单交易时间
	 * @param orderTime
	 *            订单号
	 */
	public static void startGoodsCommentActivity(Context context, String skuCode, String thumbUrl, String name, String describe, String orderTime, String orderCode) {
		Intent intent = new Intent(context, GoodsCommentActivity.class);
		intent.putExtra(IntentKey.GOODS_COMMENT_SKU_CODE, skuCode);
		intent.putExtra(IntentKey.GOODS_COMMENT_THUMB, thumbUrl);
		intent.putExtra(IntentKey.GOODS_COMMENT_NAME, name);
		intent.putExtra(IntentKey.GOODS_COMMENT_DESCRIBE, describe);
		intent.putExtra(IntentKey.GOODS_COMMENT_ORDER_TIME, orderTime);
		intent.putExtra(IntentKey.GOODS_COMMENT_ORDER_CODE, orderCode);
		// context.startActivity(intent);
		startIntent(context, intent);
	}

	/**
	 * 跳转到订单提交成功页面
	 * 
	 * @param context
	 * @param orderCode
	 *            订单号
	 */
	public static void startOrderSubmitSuccessActivity(Context context, String orderCode) {
		Intent intent = new Intent(context, OrderSubmitSuccessActivity.class);
		intent.putExtra(IntentKey.ORDER_SUCCESS_ORDER_CODE, orderCode);

		// context.startActivity(intent);

		startIntent(context, intent);
	}

	/**
	 * 跳转到订单详情页面
	 * 
	 * @param context
	 * @param orderCode
	 *            订单号
	 */
	public static void startOrderDetailActivity(Context context, String orderCode) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(IntentKey.ORDER_DETAIL, orderCode);
		startIntent(context, intent);
	}

	/**
	 * 跳转到订单详情页面
	 * 
	 * @param context
	 * @param orderCode
	 * @param paegFlag
	 *            标识从哪个页面跳转
	 */
	public static void startOrderDetailActivity(Context context, String orderCode, String paegFlag) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(IntentKey.ORDER_DETAIL, orderCode);
		intent.putExtra(IntentKey.INTENT_ORDERDETAIL_FLAG_KEY, paegFlag);
		startIntent(context, intent);
	}

	/**
	 * 跳转到支付选择页面
	 * 
	 * @param context
	 * @param checkPos
	 *            已经选择的支付方式
	 */
	public static void startPayTypeChoice(Context context, int checkPos, int requestCode) {
		Intent intent = new Intent(context, PayTypeChoiceActivity.class);
		ArrayList<String> list = new ArrayList<String>();
		list.add("微信支付");
		list.add("支付宝");
		intent.putExtra(IntentFlag.PAY_TYPE_CONTENT, list);
		intent.putExtra(IntentFlag.PAY_TYPE_CHOICE, checkPos);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转到视频列表页面
	 * 
	 * @param context
	 * @param recreationListVo
	 */
	public static void startRecreationTvListActivity(Context context, VideoChannel recreationListVo) {
		Intent intent = new Intent();

		intent.setClass(context, RecreationTvListActivity.class);
		intent.putExtra(IntentKey.RECREATION_TVLIST_TITLE, recreationListVo);
		startIntent(context, intent);
	}

	/**
	 * 调取系统拨号页面
	 */
	public static void startSystemCallPhoneActivity(Context context, String phoneNum) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phoneNum));
		context.startActivity(intent);
	}

	// *******************************************************************************
	// 最新添加的Activity
	// *******************************************************************************

	/***
	 * 跳转到邮箱登陆页面
	 * 
	 * @author 时培飞
	 * @param emailType
	 *            邮件类型 Create at 2015-4-30 下午1:04:52
	 */
	public static void startEmailLogin(Context context, int emailType) {

		Intent intent = new Intent(EmailSelectActivity.emailSelectActivity, EmailLoginActivity.class);
		intent.putExtra(IntentKey.EMAIL_TYPE, emailType);
		EmailSelectActivity.emailSelectActivity.startActivity(intent);
		EmailSelectActivity.emailSelectActivity.overridePendingTransition(anim.fade_in, anim.fade_out);
	}

	/***
	 * 打开选择邮件
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午2:31:55
	 */
	public static void startEmailSelect(Context context) {

		Intent intent = new Intent();
		intent.setClass(context, EmailSelectActivity.class);
		context.startActivity(intent);
	}

	/***
	 * 打开安装的应用程序
	 * 
	 * @param packageName
	 *            :应用包名
	 * @author 时培飞 Create at 2015-5-4 上午9:15:10
	 */
	public static void openInstallApp(Context context, String packageName) {
		Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(mainIntent);
	}

	/***
	 * 打开手机默认浏览器
	 * 
	 * @param url
	 *            指定打开路径
	 * @author 时培飞 Create at 2015-5-4 上午9:33:28
	 */
	public static void openBrowser(Context context, String url) {
		/***
		 * 打开手机默认浏览器
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		context.startActivity(intent);

	}

	/***
	 * 拨打电话
	 * 
	 * @author 时培飞 Create at 2015-5-4 上午11:29:05
	 */
	public static void telePhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);

	}

	/***
	 * 打开发送短信界面
	 * 
	 * @author 时培飞 Create at 2015-5-4 下午6:03:05
	 */
	public static void sendSms(Context context, String phoneNumber) {

		Intent intent = new Intent();

		// 系统默认的action，用来打开默认的短信界面
		intent.setAction(Intent.ACTION_SENDTO);

		// 需要发短息的号码
		intent.setData(Uri.parse("smsto:" + phoneNumber));

		context.startActivity(intent);
	}

}
