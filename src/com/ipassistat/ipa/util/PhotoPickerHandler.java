package com.ipassistat.ipa.util;

import java.io.File;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

/**
 * 调用系统工具获取图片的工具类，在
 * {@link Activity#onActivityResult(requestCode, resultCode, data)}中获取数据
 * 
 * @author LiuYuHang
 *
 */
public class PhotoPickerHandler {
	public static final int RESULT_NONE = 0;
	public static final int RESULT_PHOTO_GRAPH = 101;// 拍照
	public static final int RESULT_PHOTO_ZOOM = 202; // 缩放
	public static final int RESULT_PHOTO_RESULT = 303;// 结果
	public static final int RESULT_PRODUCT = 404;// 选择的是商品

	public static final int TYPE_CAMERA = 111;
	public static final int TYPE_MEDIA = TYPE_CAMERA + 1;

	public static final int MAX_CAMERA_COUNT = 20;
	public static final String DEFAULT_IMAGE_TEMP_FILE_NAME = "huimeili_camera_cache";
	public static final String DEFAULT_CAMERA_IMAGE_SUFFIX = ".jpg";

	/**
	 * 调用系统的相机或者相册
	 * 
	 * @param activity
	 * @param type<br>
	 *            {@link PhotoPickerHandler#TYPE_CAMERA}<br>
	 *            {@link PhotoPickerHandler#TYPE_MEDIA}
	 */
	public static void open(Activity activity, int type) {
		Intent intent = null;
		int requestCode = RESULT_NONE;
		if (type == TYPE_CAMERA) {
			requestCode = RESULT_PHOTO_GRAPH;
			// requestCode = RESULT_PHOTO_RESULT;

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// File cameraFile = new
			// File(Environment.getExternalStorageDirectory(),
			// DEFAULT_IMAGE_TEMP_FILE_NAME);
			File cameraFile = new File(getNextCameraPath());
			// LogUtil.outLogDetail("照片路径为:" + cameraFile.getAbsolutePath());

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
		} else if (type == TYPE_MEDIA) {
			requestCode = RESULT_PHOTO_ZOOM;
			// requestCode = RESULT_PHOTO_RESULT;

			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 存储位置
		}

		if (intent == null) throw new IllegalArgumentException(type + " 是无法识别的type类型");
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 删除之前所有的拍照缓存
	 *
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static synchronized void deleteAllCameraCache() {
		String cacheFile = Environment.getExternalStorageDirectory() + "/" + DEFAULT_IMAGE_TEMP_FILE_NAME;

		for (int i = 0; i < MAX_CAMERA_COUNT; i++) {
			FileUtis.deleteFiles(cacheFile + i + DEFAULT_CAMERA_IMAGE_SUFFIX);
		}
	}

	/**
	 * 查找刚拍完的照片路径，倒着查找 第一个就是刚拍的照
	 *
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static String getLastCameraPath() {
		String cacheFile = Environment.getExternalStorageDirectory() + "/" + DEFAULT_IMAGE_TEMP_FILE_NAME;
		for (int i = MAX_CAMERA_COUNT; i >= 0; i--) {
			String cameraPath = cacheFile + i + DEFAULT_CAMERA_IMAGE_SUFFIX;
			if (FileUtis.isExist(cameraPath)) return cameraPath;
		}
//		throw new NullPointerException("获取照片失败！");
		return "";
	}

	/**
	 * 获取最后一张拍的照片, 未完成
	 *
	 * @author LiuYuHang
	 * @date 2014年11月7日
	 */
	@Deprecated
	public static void getLastCamereImage(Context context) {
		String lastPhotoPath = getLastCameraPath();
		if (TextUtils.isEmpty(lastPhotoPath)) return;

		View view = new View(context);
		BitmapOptionsFactory.newInstanceBitmapUtils(context).display(view, lastPhotoPath, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {

			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {

			}

		});
	}

	/**
	 * 获取拍照存储的路径
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static String getNextCameraPath() {
		String cacheFile = Environment.getExternalStorageDirectory() + "/" + DEFAULT_IMAGE_TEMP_FILE_NAME;
		for (int i = 0; i < MAX_CAMERA_COUNT; i++) {
			String cameraPath = cacheFile + i + DEFAULT_CAMERA_IMAGE_SUFFIX;
			if (!FileUtis.isExist(cameraPath)) return cameraPath;
		}
		return cacheFile;
	}

	/**
	 * 调用系统工具缩放图片
	 * 
	 * @param uri
	 *            图片路径
	 */
	public static void StartPhotoZoom(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, RESULT_PHOTO_RESULT);
	}
}
