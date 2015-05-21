package com.ipassistat.ipa.util.share.um;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.util.ToastUtil;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 友盟分享
 * 
 * @author renheng
 * 
 */
public class ShareUM {

	private Context mContext;
	/** 分享服务 */
	public UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
			RequestType.SOCIAL);
	/** 试用分享,一般分享区分flag */
	private boolean flag = false;
	/** 短信分享内容 */
	private String contentBody = "";
	/** 分享点击时间记录 */
	private long shareTime = 0l;
	/** 分享回调接口 */
	private OnShareReq mOnShareReq;

	/** QQ互联 */
	private String QQ_ID = "1104302364";
	private String QQ_KEY = "qMV6yhFl0Y2ejbaz";

	/** 微信 */
	private String appID = "wxb6003c40eb64c43e";
	private String appSecret = "cec67297d174b6080b3dd74765d7531a";

	/** 分享结果的回调接口 */
	public interface OnShareReq {
		/** 分享成功 */
		public void onShareSuccess();

		/** 分享失败 */
		public void onShareFail();
	}

	/**
	 * 设置回调接口
	 * 
	 * @param onShareReq
	 */
	public void setOnShareReq(OnShareReq onShareReq) {
		this.mOnShareReq = onShareReq;
		flag = true;
	}

	/**
	 * 分享类
	 * 
	 * @param context
	 */
	public ShareUM(Context context) {
		this.mContext = context;
		MyContentObserver contentServer = new MyContentObserver(new Handler());
		mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true,
				contentServer);
		// 友盟分享对应的key
		SocializeConstants.APPKEY = context.getResources().getString(R.string.um_appkey);

		// 添加QQ
		addQQShare();
		// 添加QQ空间
		addQzoneShare();
		// 添加短信
		addSmS();
		// 添加新浪微博
		addSinaWeiBo();
		// 添加微信及微信朋友圈
		addWeiXin();
		// 添加平台排列顺序
		setPlatformOrder();
	}

	/**
	 * 自定义ContentObserver
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-3-23
	 * 
	 */
	public class MyContentObserver extends ContentObserver {
		/**
		 * @param handler
		 * @param context
		 */
		public MyContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://sms/outbox"),
					null, null, null, null);
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex("body")).equals(contentBody)
						&& cursor.getLong(cursor.getColumnIndex("date")) > shareTime && flag) {
					if (mOnShareReq != null) {
						mOnShareReq.onShareSuccess();
					}
				}
			}
		}
	}

	/**
	 * 打开友盟分享面板
	 * 
	 * @param context
	 * @param content
	 *            分享的内容
	 * @param SMSContent
	 *            分享的短信内容
	 * @param title
	 *            分享的标题
	 * @param picUrl
	 *            分享的图片链接
	 * @param tagetUrl
	 *            分享的跳转链接
	 */
	public void setShare(String content, String SMSContent, String title, String picUrl,
			String targetUrl) {
		openQQShareByUm(content, title, picUrl, targetUrl);
		openQzonShareByUm(content, title, picUrl, targetUrl);
		openSinaShareByUm(content, title, picUrl, targetUrl);
		openWeiXinCircleShareByUm(content, title, picUrl, targetUrl);
		openWeiXinShareByUm(content, title, picUrl, targetUrl);
		openSmsShareByUm(SMSContent, targetUrl);
		mController.registerListener(listener);
		if (flag) {
			mController.openShare((Activity) mContext, listener);
		} else {
			mController.openShare((Activity) mContext, false);
		}
	}

	/**
	 * 打开友盟分享面板
	 * 
	 * @param content
	 *            网络平台分享的内容
	 * @param SMSContent
	 *            短信平台分享的内容
	 * @param title
	 * @param resourceId
	 *            分享图片的本地资源地址
	 * @param targetUrl
	 * @date 2014年10月23日
	 */
	public void setShare(String content, String SMSContent, String title, int resourceId,
			String targetUrl) {
		openQQShareByUm(content, title, resourceId, targetUrl);
		openQzonShareByUm(content, title, resourceId, targetUrl);
		openSinaShareByUm(content, title, resourceId, targetUrl);
		openWeiXinCircleShareByUm(content, title, resourceId, targetUrl);
		openWeiXinShareByUm(content, title, resourceId, targetUrl);
		openSmsShareByUm(SMSContent, targetUrl);

		mController.registerListener(listener);
		if (flag) {
			mController.openShare((Activity) mContext, listener);
		} else {
			mController.openShare((Activity) mContext, false);
		}
	}

	/**
	 * 添加QQ在分享列表页中
	 */
	private void addQQShare() {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, QQ_ID, QQ_KEY);
		qqSsoHandler.addToSocialSDK();
	}

	/**
	 * 添加Qzone在分享列表页中
	 */
	private void addQzoneShare() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, QQ_ID, QQ_KEY);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 设置QQ分享的内容(图片链接)
	 */
	private void openQQShareByUm(String content, String title, String picUrl, String targetUrl) {
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(content);
		// 设置分享title
		qqShareContent.setTitle(title);
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(mContext, picUrl));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(targetUrl);
		mController.setShareMedia(qqShareContent);
	}

	/**
	 * 设置QQ分享的内容(本地图片)
	 */
	private void openQQShareByUm(String content, String title, int resourceId, String targetUrl) {
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(content);
		// 设置分享title
		qqShareContent.setTitle(title);
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(mContext, resourceId));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(targetUrl);
		mController.setShareMedia(qqShareContent);
	}

	/**
	 * 微信
	 */
	private void addWeiXin() {

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext, appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}

	/**
	 * 添加新浪微博
	 */
	private void addSinaWeiBo() {
		// mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	/**
	 * 添加短信
	 */
	private void addSmS() {
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	/**
	 * 设置短信分享的内容
	 */
	private void openSmsShareByUm(String content, String targetUrl) {
		SmsShareContent smsContent = new SmsShareContent();
		smsContent.setShareContent(content + targetUrl);
		mController.setShareMedia(smsContent);
		contentBody = content + targetUrl;
	}

	/**
	 * 设置Qzone分享的内容
	 */
	private void openQzonShareByUm(String content, String title, String picUrl, String targetUrl) {
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(content);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(targetUrl);
		// 设置分享内容的标题
		qzone.setTitle(title);
		// 设置分享图片
		qzone.setShareImage(new UMImage(mContext, picUrl));
		mController.setShareMedia(qzone);
	}

	/**
	 * 设置Qzone分享的内容
	 */
	private void openQzonShareByUm(String content, String title, int resourceId, String targetUrl) {
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(content);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(targetUrl);
		// 设置分享内容的标题
		qzone.setTitle(title);
		// 设置分享图片
		qzone.setShareImage(new UMImage(mContext, resourceId));
		mController.setShareMedia(qzone);
	}

	/**
	 * 设置微信分享
	 */
	private void openWeiXinShareByUm(String content, String title, String picUrl, String targetUrl) {
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(content);
		// 设置title
		weixinContent.setTitle(title);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(targetUrl);
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(mContext, picUrl));
		mController.setShareMedia(weixinContent);
	}

	/**
	 * 设置微信分享
	 */
	private void openWeiXinShareByUm(String content, String title, int resourceId, String targetUrl) {
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(content);
		// 设置title
		weixinContent.setTitle(title);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(targetUrl);
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(mContext, resourceId));
		mController.setShareMedia(weixinContent);
	}

	/**
	 * 设置新浪微博分享
	 */
	private void openSinaShareByUm(String content, String title, String picUrl, String targetUrl) {
		SinaShareContent sina = new SinaShareContent();
		sina.setShareImage(new UMImage(mContext, picUrl));
		sina.setTitle(title);
		sina.setShareContent(content + targetUrl);
		sina.setTargetUrl(targetUrl);
		mController.setShareMedia(sina);
	}

	/**
	 * 设置新浪微博分享
	 */
	private void openSinaShareByUm(String content, String title, int resource, String targetUrl) {
		SinaShareContent sina = new SinaShareContent();
		sina.setShareImage(new UMImage(mContext, resource));
		sina.setTitle(title);
		sina.setShareContent(content + targetUrl);
		sina.setTargetUrl(targetUrl);
		mController.setShareMedia(sina);
	}

	/**
	 * 设置微信朋友圈分享
	 * 
	 * @param content
	 * @param title
	 * @param picUrl
	 * @param targetUrl
	 */
	private void openWeiXinCircleShareByUm(String content, String title, String picUrl,
			String targetUrl) {
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		// 设置朋友圈title
		circleMedia.setTitle(title);
		circleMedia.setShareImage(new UMImage(mContext, picUrl));
		circleMedia.setTargetUrl(targetUrl);
		mController.setShareMedia(circleMedia);
	}

	/**
	 * 设置微信朋友圈分享
	 * 
	 * @param content
	 * @param title
	 * @param resourceId
	 * @param targetUrl
	 */
	private void openWeiXinCircleShareByUm(String content, String title, int resourceId,
			String targetUrl) {
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		// 设置朋友圈title
		circleMedia.setTitle(title);
		circleMedia.setShareImage(new UMImage(mContext, resourceId));
		circleMedia.setTargetUrl(targetUrl);
		mController.setShareMedia(circleMedia);
	}

	/**
	 * 增加 复制 模块
	 */
	@SuppressLint("NewApi")
	private void addCopyModle(final String shareContent, final String contentUrl) {
		CustomPlatform copyPlatform = new CustomPlatform(mContext.getString(R.string.copy),
				R.drawable.link);
		copyPlatform.mClickListener = new OnSnsPlatformClickListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onClick(Context arg0, SocializeEntity arg1, SnsPostListener arg2) {
				// MobclickAgent.onEvent(context,
				// 1048);
				ClipboardManager c = (ClipboardManager) mContext
						.getSystemService(android.content.Context.CLIPBOARD_SERVICE);
				c.setText(shareContent + contentUrl);
				ToastUtil.showToast(mContext, shareContent + contentUrl);
			}
		};

		mController.getConfig().addCustomPlatform(copyPlatform);
	}

	/**
	 * 友盟的分享回调接口
	 */
	private SnsPostListener listener = new SnsPostListener() {

		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
			if (stCode == 200) {
				if ("SMS".equals(platform.name())) {
					shareTime = System.currentTimeMillis();
				} else {
					if (mOnShareReq != null)
						mOnShareReq.onShareSuccess();
				}
			} else {
				if (mOnShareReq != null)
					mOnShareReq.onShareFail();
			}
		}
	};

	/**
	 * @功能描述 : 自定义平台排序,.分享平台会按照参数传递的顺序来排列, 如果没有指定顺序，则默认排序
	 */
	private void setPlatformOrder() {
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT); // 删除腾讯微博的分享
	}

}
