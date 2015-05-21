package com.ipassistat.ipa.util;

import java.math.BigDecimal;
import java.util.HashMap;

import com.ipassistat.ipa.constant.ConfigInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * 是操作TextView里面文字显示效果的工具类
 * 
 * com.ichsy.mboss.util.TextUtil
 * 
 * @author maoyn<br/>
 *         create at 2014-9-17 下午5:37:18
 */
public class ViewUtil {
	private static PopupWindow pw;

	private static SpannableString msp;

	/**
	 * 设置点击链接的跳转
	 * 
	 * @param context
	 * @param string
	 * @param title
	 * @param url
	 * @return
	 */
	public static SpannableString getLinkedTextStyle(Context context, String string, String title, String url) {
		msp = new SpannableString(string);
		int start = 0;
		int end = 0;
		if (msp != null) {
			start = string.indexOf("《");
			end = string.indexOf("》");
		}
		msp.setSpan(new NoLineClickSpan(context, title, url), start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
		// msp.setSpan(
		// new ForegroundColorSpan(context.getResources().getColor(
		// R.color.login_text_color_link)), start, end + 1,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		return msp;
	}

	/**
	 * 设置指定位置字体删除线显示
	 * 
	 * @param string
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @return 带删除线的字体
	 */
	public static SpannableString getTextStrikeThroughStyle(String string, int start, int end) {
		msp = new SpannableString(string);
		msp.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置指定位置字体带删除线显示
	 * 
	 * @param span
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @return 本分文字带删除线
	 */
	public static SpannableString getTextStrikeThroughStyle(SpannableString span, int start, int end) {
		msp = span;
		msp.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return msp;
	}

	/**
	 * 设置指定位置字体颜色
	 * 
	 * @param string
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @param color
	 *            要设置的颜色
	 * @return 部分字体改变颜色的文字
	 */
	public static SpannableString getTextColorStyle(String string, int start, int end, int color) {
		msp = new SpannableString(string);
		msp.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置指定位置字体颜色
	 * 
	 * @param string
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @param color
	 *            要设置的颜色
	 * @return 部分字体改变颜色的文字
	 */
	public static SpannableString getTextColorStyle(SpannableString span, int start, int end, int color) {
		msp = span;
		msp.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}

	/**
	 * 设置指定位置字体大小
	 * 
	 * @param string
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @param size
	 *            要设置的大小
	 * @return 部分字体改变大小的文字
	 */
	public static SpannableString getTextSizeStyle(String string, int start, int end, int size) {
		msp = new SpannableString(string);
		msp.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return msp;
	}

	/**
	 * 设置指定位置字体大小
	 * 
	 * @param span
	 *            要设置的字体
	 * @param start
	 *            起始位置
	 * @param end
	 *            结尾位置
	 * @param size
	 *            要设置的大小
	 * @return 部分字体改变大小的文字
	 */
	public static SpannableString getTextSizeStyle(SpannableString span, int start, int end, int size) {
		msp = span;
		msp.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return msp;
	}

	/**
	 * @discretion: 获得折扣的显示数据
	 * @author: MaoYaNan
	 * @date: 2014-10-13 下午2:07:37
	 * @param string
	 * @return
	 */
	public static String getRebateString(String string) {
		float ft = 0f;
		try {
			if (!TextUtils.isEmpty(string)) {
				float goodsrebate = Float.parseFloat(string);// 将String型转化为float型
				ft = goodsrebate * 10;
				int scale = 2;// 设置位数
				int roundingMode = 4;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
				BigDecimal bd = new BigDecimal((double) ft);
				bd = bd.setScale(scale, roundingMode);
				ft = bd.floatValue();
			} else {
				ft = 0f;
			}
		} catch (Exception e) {
		}
		return ft + "";
	}

	/**
	 * 批量设置view的显示与隐藏
	 * 
	 * @param visiblity
	 * @param animation
	 * @param views
	 * @author LiuYuHang
	 * @date 2014年10月16日
	 */
	public static void setViewVisiblityWithAnimation(int visiblity, boolean animation, View... views) {
		if (views == null || views.length == 0) return;

		AlphaAnimation alphaAnimation = null;
		if (animation) {
			alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
			alphaAnimation.setDuration(1000);
		}
		for (int i = 0; i < views.length; i++) {
			View acitonView = views[i];
			if (acitonView.getVisibility() == visiblity) continue;

			acitonView.setVisibility(visiblity);
			if (visiblity == View.VISIBLE && alphaAnimation != null) {
				acitonView.setAnimation(alphaAnimation);
			}
		}
	}

	/**
	 * 交换显示两个View（隐藏第一个，显示第一个）
	 * 
	 * @param fromView
	 *            要隐藏的View
	 * @param visibilityView
	 *            要显示的View
	 * @author LiuYuHang
	 * @date 2014年10月16日
	 */
	public static void swapViewWithAnimation(View fromView, View visibilityView, boolean animation) {
		setViewVisiblityWithAnimation(View.GONE, animation, fromView);
		setViewVisiblityWithAnimation(View.VISIBLE, animation, visibilityView);
	}

	/**
	 * 批量寻找view的监听器
	 * 
	 * @param rootView
	 * @param listener
	 * @param views
	 * 
	 *            Modifier： Modified Date： Modify：
	 */
	public static void findViewListener(View rootView, OnClickListener listener, int... views) {
		if (rootView == null || listener == null || views == null) return;
		for (int i = 0; i < views.length; i++) {
			rootView.findViewById(views[i]).setOnClickListener(listener);
		}
	}

	/**
	 * 判断文本框是否为空
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public static boolean isEdittextEmpty(EditText... views) {
		if (views == null || views.length == 0) return false;
		for (int i = 0; i < views.length; i++) {
			if (TextUtils.isEmpty(views[i].getText())) return true;
		}
		return false;
	}

	@SuppressLint({ "NewApi", "DefaultLocale", "SetJavaScriptEnabled" })
	public static void configWebView(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true); // 设置支持JavaScript
		// webView.getSettings().setSupportZoom(false); // 设置可以支持缩放
		// webView.getSettings().setBuiltInZoomControls(false);// 设置出现缩放工具
		// webView.getSettings().setUseWideViewPort(false);// 扩大比例的缩放
		// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);//
		// 自适应屏幕
		webView.setWebChromeClient(new WebChromeClient());
		// webView.getSettings().setBlockNetworkImage(false);
		// webView.getSettings().setBlockNetworkLoads(false);

		webView.getSettings().setDomStorageEnabled(true);
		// webView.getSettings().setLoadWithOverviewMode(true);


		/*webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.toLowerCase().startsWith(ConfigInfo.SCHEME)) { 

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
						IntentUtil.startGoodsDetail(view.getContext(), keyMap.get("id"), keyMap.get("type"));
						return true;
					}
				}

				return super.shouldOverrideUrlLoading(view, url);
			}

		});*/
	}
	

	/**
	 * 将图片裁剪到指定的宽度和高度， ####会造成图片变模糊####
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	@Deprecated
	public static Bitmap getCropImage(Drawable drawable, float w, float h) {
		return getCropImage(drawableToBitmap(drawable), w, h);
	}

	/**
	 * 将图片裁剪到指定的宽度和高度
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public static Bitmap getCropImage(Bitmap bitmap, float w, float h) {
		// Bitmap bitmap = drawableToBitmap(drawable);

		if (bitmap == null) return null;

		float width = bitmap.getWidth();
		float height = bitmap.getHeight();

		// float w = imageView.getMeasuredWidth();
		// float h = imageView.getMeasuredHeight();
		LogUtil.outLogDetail("图片宽度：" + width + " 高度：" + height);
		LogUtil.outLogDetail("控件宽度：" + w + " 高度：" + h);

		if (width == 0 || height == 0 || w == 0 || h == 0) return bitmap;

		float verticalRadio = width / w;
		float horizontalRadio = height / h;

		float radio = verticalRadio < horizontalRadio ? verticalRadio : horizontalRadio;

		int w1 = (int) (radio * w);
		int h1 = (int) (radio * h);

		int wPadding = (int) ((width - w1) / 2);
		int hPadding = (int) ((height - h1) / 2);

		// LogUtil.outLogDetail("裁剪后left：" + wPadding + " top：" + hPadding +
		// " right:" + w1 + " bottom:" + h1);

		/*
		 * Rect rect = new Rect(wPadding, hPadding, w1 + wPadding, h1 +
		 * hPadding);
		 * 
		 * bitmap = cutBitmap(bitmap, rect, bitmap.getConfig()); return bitmap;
		 */

		return Bitmap.createBitmap(bitmap, wPadding, hPadding, w1, h1, null, false);
	}

	/**
	 * drawable转换bitmap， ####会造成图片变模糊####
	 * 
	 * @param drawable
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月23日
	 */
	@Deprecated
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;
	}

	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 单位换算
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月23日
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 单位换算
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月23日
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 裁剪图片
	 * 
	 * @param mBitmap
	 * @param r
	 * @param config
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月23日
	 */
	@Deprecated
	public static Bitmap cutBitmap(Bitmap mBitmap, Rect r, Bitmap.Config config) {
		int width = r.width();
		int height = r.height();

		Bitmap croppedImage = Bitmap.createBitmap(width, height, config);

		Canvas cvs = new Canvas(croppedImage);
		Rect dr = new Rect(0, 0, width, height);
		cvs.drawBitmap(mBitmap, r, dr, null);
		return croppedImage;
	}

	/**
	 * 检查EditText内容是否为空
	 * 
	 * @param editTexts
	 *            要检索的EditText
	 * @param errors
	 *            提示的语句
	 * @param defaultError
	 *            为空的默认提示
	 * 
	 * @return 返回错误信息或者是null如果成功
	 */
	public static String editTextChecker(EditText[] editTexts, String[] errors, String defaultError) {
		for (int i = 0; i < editTexts.length; i++) {
			EditText editText = editTexts[i];
			String content = editText.getText().toString();

			if (TextUtils.isEmpty(content)) {
				String errorTips = i <= errors.length ? errors[i] : defaultError;
				return errorTips;
			} else {
				continue;
			}
		}
		return null;
	}
}
