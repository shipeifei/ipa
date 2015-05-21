package com.loopj.android.image.cache;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 缓存图片的cache （包含mem，disk）
 * 
 */
public enum ImageCache {

	INSTANCE;

	// 默认内存缓存大小
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB

	private static final int DEFAULT_REQ_WIDTH = 480;

	private static final int DEFAULT_REQ_HEIGHT = 800;

	// 是否使用内存缓存
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	// 是否使用SD卡缓存
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	// 是否在使用缓存欠清理SD卡
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

	private ImageCacheParams mImageCacheParams;
	// sd卡
	private DiskCache mDiskCache;
	// 内存
	private LruCache<String, Bitmap> mMemoryCache;

	// private HashMap<ImageView, String> maps = new HashMap<ImageView,
	// String>();

	public static ImageCache createCache() {
		return INSTANCE;
	}

	private ImageCache() {}

	public void setCacheParams(ImageCacheParams cacheParams) {
		init(cacheParams);
	}

	private void init(ImageCacheParams cacheParams) {
		mImageCacheParams = cacheParams;
		// Set up disk cache
		if (cacheParams.diskCacheEnabled) {
			mDiskCache = DiskCache.openCache();
			if (cacheParams.clearDiskCacheOnStart) {
				mDiskCache.clearCache();
			}
		}

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>(cacheParams.memCacheSize) {
				/**
				 * Measure item size in bytes rather than units which is more
				 * practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return Utils.getBitmapSize(bitmap);
				}

			};
		}

	}

	/**
	 * 将图片添加到缓存
	 */
	public void addBitmapToCache(String data, Bitmap bitmap) {
		if (data == null || bitmap == null) { return; }

		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(data) == null) {
			mMemoryCache.put(data, bitmap);
		}

	}

	/**
	 * 从 内存取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromMem(String path) {
		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(path);
			if (memBitmap != null) {
				// mMemoryCache.remove(path);
				// mMemoryCache.put(path, memBitmap);
				return memBitmap;
			}
		}
		return null;
	}

	// public void removeBitmapFromMem(String path) {
	// if (mMemoryCache != null) {
	// mMemoryCache.remove(path);
	// }
	// }

	/**
	 * 从 sd取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(String path) {
		if (mDiskCache != null) {
			final File cacheFile = new File(mDiskCache.createFilePath(path));
			if (mDiskCache.containsKey(path)) { return decodeBitmap(cacheFile); }
		}
		return null;
	}

	/**
	 * 从 sd取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(File file) {
		if (file != null && file.exists()) { return decodeBitmap(file); }
		return null;
	}

	/**
	 * 清理缓存
	 */
	public void clearCaches() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}
	}

	public ImageCacheParams getCacheParams() {
		return mImageCacheParams;
	}

	public synchronized Bitmap decodeBitmap(String fileName) {
		// return UpdateUtil.cropImage(fileName);
		return decodeBitmap(fileName, mImageCacheParams.reqWidth, mImageCacheParams.reqHeight);
	}

	public synchronized Bitmap decodeBitmap(File file) {
		// return UpdateUtil.cropImage(file.getAbsolutePath());
		return decodeBitmap(file.getAbsolutePath(), mImageCacheParams.reqWidth, mImageCacheParams.reqHeight);
	}

	private synchronized Bitmap decodeBitmap(String fileName, int width, int height) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		if (options.outWidth < 1 || options.outHeight < 1) {
			String fn = fileName;
			File ft = new File(fn);
			if (ft.exists()) {
				ft.delete();
				return null;
			}
		}
		// Calculate inSampleSize
		options.inSampleSize = calculateOriginal(options, width, height);

		// Decode bitmap with inSampleSize set

		Log.e("ad", "options.inSampleSize  ==    " + options.inSampleSize);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(fileName, options);
	}

	private int calculateOriginal(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		final int height = options.outHeight;
		final int width = options.outWidth;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 3;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		// 数字越大，压缩越厉害
		if (inSampleSize < 5) {
			inSampleSize = inSampleSize / inSampleSize;
		}
		return inSampleSize;
		// int be = 1;// be=1表示不缩放
		// if (width > height && width > reqWidth) {// 如果宽度大的话根据宽度固定大小缩放
		// be = (int) (options.outWidth / reqWidth);
		// } else if (width < height && height > reqHeight) {//
		// 如果高度高的话根据宽度固定大小缩放
		// be = (int) (options.outHeight / reqHeight);
		// }
		// if (be <= 0) be = 1;
		// if (be != 1) be = be / 3;// 图片太模糊，调整一下
		// return be;
		// return inSampleSize;
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public int reqWidth = DEFAULT_REQ_WIDTH;
		public int reqHeight = DEFAULT_REQ_HEIGHT;
		public Integer loadingResId = 0;

		public static ImageCacheParams getDefaultParams(Context context, int loadingResId) {
			int defWidht = context.getResources().getDisplayMetrics().widthPixels / 5;
			return getDefaultParams(context, loadingResId, defWidht, defWidht);
		}

		public static ImageCacheParams getDefaultParams(Context context, int loadingResId, int width, int height) {
			ImageCacheParams cacheParams = new ImageCacheParams();
			cacheParams.reqWidth = width;
			// cacheParams.reqHeight = (int) (cacheParams.reqWidth * 1.5);
			cacheParams.reqHeight = height;
			cacheParams.loadingResId = loadingResId;
			// cacheParams.clearDiskCacheOnStart = true;
			cacheParams.memCacheSize = (1024 * 1024 * Utils.getMemoryClass(context)) / 2;
			return cacheParams;
		}
	}
}
