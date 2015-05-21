package com.ipassistat.ipa.util;

import java.lang.ref.SoftReference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 图片裁剪的工具类
 * 
 * @author maoyn
 * 
 */
public class ClipViewUtil {
	private static int mWidth;
	private static int mHeight;
	private static Activity mActivity;
	private int mTop; // 内容显示的最顶端位置

	/**
	 * 单例模式，建立单例对象
	 * 
	 * @param act
	 * @return
	 */
	public static ClipViewUtil getInstance(Activity act) {
		// 不用单例模式，因为单例模式二次进入该界面时 图片的缓存不清空
		// if (clipViewUtil == null
		// || act == null
		// || activity == null
		// || !(act.getClass().getName().equals(activity.getClass()
		// .getName()))) {
		// activity = act;
		// clipViewUtil = new ClipViewUtil();
		// }
		// return clipViewUtil;
		mActivity = act;
		return new ClipViewUtil();
	}

	public static void distory() {
		if (mActivity != null) {
			mActivity = null;
		}
	}

	/**
	 * 构造方法
	 * 
	 * @param mActivity
	 */
	public ClipViewUtil() {
		getDimens(); // 获得尺寸
	}

	/**
	 * 切半径为 r 的圆形图片
	 * 
	 * @param mActivity
	 *            背景图来自哪个Activity
	 * @param r
	 *            圆形的半径
	 * @return 切出的圆形 Bitmap
	 */
	public Bitmap getCricleClipView(float r, float rX, float rY) {
		rY = (rY + mTop);
		SoftReference<Bitmap> composedBitmap = new SoftReference<Bitmap>(
				Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888));
		SoftReference<Bitmap> originalBitmap = new SoftReference<Bitmap>(
				getScreenBitmap());
		Canvas composedCanvas = new Canvas(composedBitmap.get());
		Paint paintCir = new Paint();
		paintCir.setColor(Color.BLACK);

		composedCanvas.drawCircle(rX, rY, r, paintCir);
		Paint paintCircle = new Paint();
		paintCircle.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		composedCanvas.drawBitmap(originalBitmap.get(), 0, 0, paintCircle);

		return Bitmap.createBitmap(composedBitmap.get(), (int) (rX - r),
				(int) (rY - r), (int) (2 * r), (int) (2 * r));
	}

	/**
	 * 截取屏幕
	 * 
	 * @param mActivity
	 * @return 返回屏幕对应的图片
	 */
	public Bitmap getScreenBitmap() {
		Bitmap bmp = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		// 2.获取屏幕
		View decorview = mActivity.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		decorview.buildDrawingCache();
		decorview.invalidate(); // 刷新缓存
		bmp = decorview.getDrawingCache(); // 获取屏幕bitmap
		// decorview.destroyDrawingCache();
		// decorview.setDrawingCacheEnabled(false); // 清空缓存
		return bmp;
	}

	/**
	 * 获得 Activity 对应的屏幕的大小
	 * 
	 * @param mActivity
	 */
	private void getDimens() {
		// 1.构建Bitmap
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		View v = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		mTop = v.getTop();
		mWidth = display.getWidth();
		mHeight = display.getHeight();
	}
}
