package com.ipassistat.ipa.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.RequestCodeConstant;
import com.ipassistat.ipa.util.ClipViewUtil;
import com.ipassistat.ipa.util.FileUtis;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.ImageUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.PhotoPickerHandler;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.UpdateUtil.ImageUpdateListener;
import com.ipassistat.ipa.view.CircleClipImageView;
import com.ipassistat.ipa.view.GestureHandleImgeView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 头像拍照处理页面 com.ichsy.mboss.ui.activity.PhotoOperateActivity
 * 
 * @author 时培飞 Create at 2015-4-17 下午3:03:43
 */
public class PhotoOperateActivity extends BaseActivity {
	private TitleBar mTitleBar;
	private GestureHandleImgeView mGsetureImage;
	private CircleClipImageView mCircleClipImageView;
	private Activity mActivity;
	private SoftReference<Bitmap> mBitmap;
	private String mFileUrl; // 图片的url

	private BitmapUtils mBitmapUtil;
	private AQuery aQuery;
	private Bitmap mCamorabitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_operate);

		mActivity = this;

		aQuery = new AQuery(getParent());

		mBitmapUtil = new BitmapUtils(mActivity);
		mBitmapUtil.clearDiskCache();
		mBitmapUtil.clearMemoryCache();
		mBitmapUtil.clearCache();

		initViews();
		initData();
		registerListener();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1012"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1012"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);// onPause 中会保存信息
	}

	@SuppressLint("ClickableViewAccessibility")
	private void registerListener() {
		mGsetureImage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 触屏事件
				if (event.getPointerCount() == 2) {// 2点触控
					mGsetureImage.scaleWithFinger(event);// 调用控制图片大小的方法
				} else if (event.getPointerCount() == 1) {// 单点触摸
					mGsetureImage.moveWithFinger(event);// 调用控制图片移动的方法
				}
				return true;
			}
		});
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		int intExtra = intent.getIntExtra("from", RequestCodeConstant.CAMERA);
		switch (intExtra) {
		case RequestCodeConstant.CAMERA:

			// 读取图片,存在sd卡就执行读取图片路径的方式-------------------------------------------------------
			if (GlobalUtil.isExistSDCard()) {
				try {
					mCamorabitmap = getBitmapFromPath(Environment.getExternalStorageDirectory() + "/hmlphoto.jpg", mActivity);

					if (null != mCamorabitmap) {
						// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
						int scale = reckonThumbnail(mCamorabitmap.getWidth(), mCamorabitmap.getHeight(), 600, 700);
						mCamorabitmap = PicZoom(mCamorabitmap, mCamorabitmap.getWidth() / scale, mCamorabitmap.getHeight() / scale);
					}
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					LogUtil.outLogDetail("处理图片oom:" + e.getMessage());
				}

				mGsetureImage.setImageBitmap(mCamorabitmap);
			} else {

				getBitmapFromCarmar(intent);
				mGsetureImage.setImageBitmap(mBitmap.get());

			}

			LogUtil.outLogDetail("-----------照片路径---" + mFileUrl);
			break;
		case RequestCodeConstant.PICTURE:
			mFileUrl = getBitmapFromPicture(intent);
			mBitmapUtil.display(mGsetureImage, mFileUrl);

			break;
		default:

			break;
		}
		mTitleBar.setTitleText(getString(R.string.phototitle));
		mTitleBar.setVisibility(TitleBarButton.rightTextView, View.VISIBLE);

		setTitleRightText(getString(R.string.photoselect), new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LogUtil.outLogDetail("选取图片");
				Bitmap bmp = ClipViewUtil.getInstance(mActivity).getCricleClipView(mCircleClipImageView.getR(), mCircleClipImageView.getRx(),
						mCircleClipImageView.getRy() + mCircleClipImageView.getTop());
				UpdateUtil.getInstance().updateImages(bmp, new ImageUpdateListener() {

					@Override
					public void onUpdateComplate(List<String> bitmapUrls) {
						if (bitmapUrls != null && bitmapUrls.size() > 0) {
							String url = bitmapUrls.get(0);
							Intent data = new Intent();
							data.putExtra(IntentFlag.PHOTO_URL, url);
							setResult(Activity.RESULT_OK, data);
							ToastUtil.showToast(mActivity, getString(R.string.uploadsuccess));
							ProgressHub.getInstance(mActivity).dismiss();

							finish();
						}
					}

					@Override
					public void onProgress(int max, int position) {

					}

					@Override
					public void onError(String error) {
						LogUtil.outLogDetail(error);
						ToastUtil.showToast(getApplicationContext(), error);
					}

					@Override
					public void onBegin() {
						ProgressHub.getInstance(mActivity).show(getString(R.string.uploading));
					}
				});

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// 先判断是否已经回收

		if (mCamorabitmap != null && !mCamorabitmap.isRecycled()) {
			// 回收并且置为null
			mCamorabitmap.recycle();
			mCamorabitmap = null;
		}

		System.gc();
	}

	/**
	 * 通过本地路径获取图片
	 * 
	 * @param imagePath
	 * @param size
	 * @return
	 */
	public Bitmap getBitmapFromPath(String imagePath, Context ct) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		// 从Options中获取图片的分辨率
		int imageHeight = opts.outHeight;
		int imageWidth = opts.outWidth;
		opts.inDither = false;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置图片的DPI为当前手机的屏幕dpi
		opts.inTargetDensity = ct.getResources().getDisplayMetrics().densityDpi;
		opts.inScaled = true;
		// 获取Android屏幕的服务
		WindowManager wm = (WindowManager) ct.getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了
		// 应该使用getSize()，但是这里为了向下兼容所以依然使用它们
		int windowHeight = wm.getDefaultDisplay().getHeight();
		int windowWidth = wm.getDefaultDisplay().getWidth();
		// 计算采样率
		int scaleX = imageWidth / windowWidth;
		int scaleY = imageHeight / windowHeight;
		int scale = 1;
		// 采样率依照最大的方向为准
		if (scaleX > scaleY && scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleX < scaleY && scaleX >= 1) {
			scale = scaleY;
		}
		// false表示读取图片像素数组到内存中，依照设定的采样率
		opts.inJustDecodeBounds = false;
		// 采样率
		opts.inSampleSize = 1;
		// opts.inPurgeable = true;
		return BitmapFactory.decodeFile(imagePath, opts);
	}

	public static int reckonThumbnail(int oldWidth, int oldHeight, int newWidth, int newHeight) {
		if ((oldHeight > newHeight && oldWidth > newWidth) || (oldHeight <= newHeight && oldWidth > newWidth)) {
			int be = (int) (oldWidth / (float) newWidth);
			if (be <= 1)
				be = 1;
			return be;
		} else if (oldHeight > newHeight && oldWidth <= newWidth) {
			int be = (int) (oldHeight / (float) newHeight);
			if (be <= 1)
				be = 1;
			return be;
		}
		return 1;
	}

	public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
		int bmpWidth = bmp.getWidth();
		int bmpHeght = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

		return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
	}

	/**
	 * @discretion: 从相机获取的图片
	 * @author: MaoYaNan
	 * @date: 2014-9-30 下午2:10:20
	 * @param data
	 * @return bitmap 图片数据
	 */
	private String getBitmapFromCarmar() {
		File lastCameraFile = new File(PhotoPickerHandler.getLastCameraPath());
		// 设置文件保存路径
		// File picture = new
		// File(Environment.getExternalStorageDirectory() + "/" +
		// PhotoPickerHandler.DEFAULT_IMAGE_TEMP_FILE_NAME);
		/**
		 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		 */
		int degree = ImageUtil.readPictureDegree(lastCameraFile.getAbsolutePath());
		if (degree != 0) {
			Bitmap oldBitmap = aQuery.getCachedImage(lastCameraFile.getAbsolutePath());

			// Bitmap oldBitmap =
			// BitmapFactory.decodeFile(lastCameraFile.getAbsolutePath(), new
			// BitmapFactory.Options());

			Bitmap saveBitmap = ImageUtil.rotaingImageView(degree, oldBitmap);
			FileUtis.saveMyBitmap(lastCameraFile.getAbsolutePath(), saveBitmap);
		}
		return lastCameraFile.getAbsolutePath();
	}

	/**
	 * @discretion: 从相机获取的图片
	 * @author: MaoYaNan
	 * @date: 2014-9-30 下午2:10:20
	 * @param data
	 * @return bitmap 图片数据
	 */
	public void getBitmapFromCarmar(Intent data) {
		Bundle bundle = data.getExtras();
		if (mBitmap != null && mBitmap.get() != null) {
			mBitmap.get().recycle();
		}
		mBitmap = new SoftReference<Bitmap>((Bitmap) bundle.get("data"));
		// mBitmap = (Bitmap) bundle.get("data");
	}

	/**
	 * @discretion: 从相册获取数据
	 * @author: MaoYaNan
	 * @date: 2014-9-30 下午4:48:28
	 * @param data
	 * @return
	 */
	public String getBitmapFromPicture(Intent data) {
		// Bundle bundle = data.getExtras();
		// mPhoto.setImageBitmap((Bitmap) bundle.get("data"));
		Uri uri = data.getData();
		Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		// String imgNo = cursor.getString(0); // 图片编号
		String imgPath = cursor.getString(1); // 图片文件路径
		// String imgSize = cursor.getString(2); // 图片大小
		// String imgName = cursor.getString(3); // 图片文件名
		cursor.close();
		return imgPath; // 返回图片的路径
		// Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = false;
		// // options.inSampleSize = 1; // 返回完整的大小
		// options.inSampleSize = computeSampleSize(options, -1, 50 * 50); //
		// if (mBitmap != null && mBitmap.get() != null) {
		// mBitmap.get().recycle();
		// }
		// ImageCache imageCache = ImageCache.createCache();
		// imageCache.setCacheParams(ImageCacheParams.getDefaultParams(mActivity,
		// drawable.goods_default_img));
		// mBitmap = new
		// SoftReference<Bitmap>(imageCache.decodeBitmap(imgPath));
		// mBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(imgPath,
		// options));
		// mBitmap = BitmapFactory.decodeFile(imgPath, options);
		// Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
	}

	/**
	 * @discretion: 联系布局和组件
	 * @author: MaoYaNan
	 * @date: 2014-9-30 下午4:55:50
	 */
	private void initViews() {
		mTitleBar = (TitleBar) findViewById(R.id.titleBar);
		mGsetureImage = (GestureHandleImgeView) findViewById(R.id.gestureHandleImgeView1);
		//mCircleClipImageView = (CircleClipImageView) findViewById(R.id.circleClipImageView1);
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
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
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
