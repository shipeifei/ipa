package com.ipassistat.ipa.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ipassistat.ipa.bean.local.Size;
import com.ipassistat.ipa.bean.response.UpdateResponse;
import com.ipassistat.ipa.bean.response.entity.ImageInfo;
import com.ipassistat.ipa.bean.response.entity.ImageInfoVo;
import com.ipassistat.ipa.bean.response.entity.PicAllInfo;
import com.ipassistat.ipa.constant.ConfigInfo;

/**
 * 上传文件的工具类
 * 
 * @author LiuYuHang
 * @date 2014年9月29日
 * 
 */
public class UpdateUtil {
	/**
	 * 图片上传的监听器
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 * 
	 */
	public interface ImageUpdateListener {

		void onBegin();

		void onProgress(int max, int position);

		/**
		 * 返回的是一个List列表，里面是每一张图片的地址
		 * 
		 * @param bitmapUrls
		 * 
		 * @author LiuYuHang
		 * @date 2014年9月29日
		 */
		void onUpdateComplate(List<String> bitmapUrls);

		void onError(String error);
	}

	static UpdateUtil instance;
	private UpdateImagesTask dateTask;

	public synchronized static UpdateUtil getInstance() {
		if (instance == null) instance = new UpdateUtil();
		return instance;
	}

	/**
	 * 上传图片
	 * 
	 * @param bitmaps
	 *            List<?> :?可以是Bitmap，SoftReference<Bitmap>, String
	 * @param listener
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 */
	public void updateImages(List<?> bitmaps, ImageUpdateListener listener) {
		if (dateTask != null) {
			dateTask.stop();
		}
		if (bitmaps == null || bitmaps.size() == 0) {
			if (listener != null) listener.onUpdateComplate(null);
			return;
		}

		dateTask = new UpdateImagesTask(listener);
		dateTask.execute(bitmaps);
	}

	public void updateImages(Bitmap bitmap, ImageUpdateListener listener) {
		List<SoftReference<Bitmap>> bitmaps = new ArrayList<SoftReference<Bitmap>>();
		bitmaps.add(new SoftReference<Bitmap>(bitmap));
		updateImages(bitmaps, listener);
	}

	/**
	 * 上传文件的异步Task
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 * 
	 */
	class UpdateImagesTask extends AsyncTask<List<?>, Integer, Boolean> {
		private ImageUpdateListener listener;
		private List<String> bitmapUrl;
		private Gson gson;
		private boolean isStop;

		public UpdateImagesTask(ImageUpdateListener listener) {
			this.listener = listener;
			gson = new Gson();
			bitmapUrl = new ArrayList<String>();
			isStop = false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (listener != null) listener.onBegin();
		}

		@Override
		protected Boolean doInBackground(List<?>... parmas) {
			List<?> bitmpas = parmas[0];
			if (bitmpas == null || bitmpas.size() == 0) return true;

			for (int i = 0; i < bitmpas.size(); i++) {
				if (isStop) break;

				String response = uploadFile(ApiUrl.getInStance().getUploadFileUrl(), bitmpas.get(i));
				if (!TextUtils.isEmpty(response)) {
					UpdateResponse responseObject = gson.fromJson(response, UpdateResponse.class);
					if (responseObject.getResultCode() != ConfigInfo.RESULT_OK) {
						// 只要有一张图片上传失败，就中断并且弹出提示
						return false;
					}
					bitmapUrl.add(responseObject.resultObject);
					publishProgress(bitmpas.size(), i);
				}
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			if (listener != null) {
				int max = values[0];
				int position = values[1];
				listener.onProgress(max, position);
			}
		}

		public void stop() {
			isStop = true;
			cancel(true);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (listener == null) return;

			if (result) {
				listener.onUpdateComplate(bitmapUrl);
			} else {
				listener.onError("上传图片失败");
				listener.onUpdateComplate(null);
			}
		}

	}

	/**
	 * 上传图片
	 * 
	 * @param location
	 * @param updateObject
	 *            可以是String 可以是bitmap的软引用
	 * @return
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月29日
	 */
	private String uploadFile(String location, Object updateObject) {
		String uploadUrl = location;
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + "update.png" + "\"" + end);
			dos.writeBytes(end);
			byte[] buffer = null;

			if (updateObject instanceof Bitmap) {
				buffer = Bitmap2Bytes(compressImage((Bitmap) updateObject));
			} else if (updateObject instanceof SoftReference) {
				buffer = Bitmap2Bytes(compressImage((Bitmap) ((SoftReference<?>) updateObject).get()));
			} else if (updateObject instanceof String) {
				buffer = Bitmap2Bytes(compressImage(cropImage((String) updateObject)));
			} else {
				throw new UnknownError("can not upload object of class" + updateObject.getClass());
			}
			dos.write(buffer, 0, buffer.length);

			// System.out.println("file send to server............");
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			// 读取服务器返回结果
			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			// LogUtil.outLogDetail(result);
			dos.close();
			is.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把图片列表转换成服务器需要的格式
	 * 
	 * @param images
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月11日
	 */
	public static String formatImageString(List<String> images) {
		if (images == null || images.size() == 0) return null;
		String result = "";
		for (int i = 0; i < images.size(); i++) {
			result += images.get(i) + "|";
		}
		return StringUtil.cutLastString(result);
	}

	public static ImageInfo[] formatImageInfo(List<String> images, List<String> localImagePath) {
		if (images == null) return null;
		
		ImageInfo[] imageInfos = new ImageInfo[images.size()];
		for (int i = 0; i < images.size(); i++) {
			imageInfos[i] = new ImageInfo();
			imageInfos[i].bigPicInfo = new ImageInfoVo();
			imageInfos[i].bigPicInfo.picUrl = images.get(i);

			Size size = ImageUtil.getImageSize(localImagePath.get(i));
			imageInfos[i].bigPicInfo.width = size.width;
			imageInfos[i].bigPicInfo.height = size.height;
		}
		return imageInfos;
	}

	/**
	 * 获取URL数据集合，URL格式为“url|url|url”或者"url"
	 * 
	 * @author 时培飞 Create at 2014-11-24 上午11:22:25
	 */
	public static ArrayList<String> getImageUrlList(String images) {
		ArrayList<String> list = new ArrayList<String>();
		if (TextUtils.isEmpty(images)) { return null; }
		String imageUrls[] = images.split("\\|");
		if (imageUrls.length == 0) {
			return null;
		} else {
			for (int i = 0; i < imageUrls.length; i++) {
				list.add(imageUrls[i]);
			}
		}
		return list;
	}

	/**
	 * 获取URL数据集合，URL格式为“url|url|url”或者"url"
	 * 
	 * @author 时培飞 Create at 2014-11-24 上午11:22:25
	 */
	public static ArrayList<String> getImageUrlList(ImageInfo[] images) {
		ArrayList<String> list = new ArrayList<String>();
		if (images.length <= 0) { return null; }

		for (int i = 0; i < images.length; i++) {
			String url = images[i].bigPicInfo.picUrl;
			list.add(url);
		}
		return list;
	}

	
	/**
	 * 将List<PicAllInfo>转换成List<String>
	 * 
	 * @author wr
	 */
	public static ArrayList<String> getImageUrlList(List<PicAllInfo> images) {
		ArrayList<String> list = new ArrayList<String>();
		if (images.size() <= 0) { return null; }

		for (int i = 0; i < images.size(); i++) {
			String url = images.get(i).getBigPicInfo().getPicUrl();
			if(url != null){
				list.add(url);
			}
		}
		return list;
	}
	/**
	 * 获取URL数组，URL格式为“url|url|url”或者"url"
	 * 
	 * @param images
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月30日
	 */
	public static String[] getImageUrl(String images) {
		if (TextUtils.isEmpty(images)) return null;
		String imageUrls[] = images.split("\\|");
		if (imageUrls.length == 0) return new String[] { images };
		return imageUrls;
	}

	public byte[] Bitmap2Bytes(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		return Bitmap2Bytes(bm, 80);
	}

	/**
	 * 文件转化为字节数组
	 */
	public byte[] File2Bytes(File file) {
		if (file == null) { return null; }
		try {
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {}
		return null;
	}

	/**
	 * 图片按比例大小压缩方法，之后调用质量压缩法
	 * 
	 * @param srcPath
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public static Bitmap cropImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩法
	 * 
	 * @param image
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// 将Bitmap转换成InputStream
	// public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
	// InputStream is = new ByteArrayInputStream(baos.toByteArray());
	// return is;
	// }
}
