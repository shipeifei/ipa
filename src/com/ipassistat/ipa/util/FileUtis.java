package com.ipassistat.ipa.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class FileUtis {

	/**
	 * inputSteam转换为String
	 *
	 * @param is
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 判斷文件是否存在
	 *
	 * @param path
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static boolean isExist(String path) {
		if (TextUtils.isEmpty(path)) return false;
		return new File(path).exists();
	}

	/**
	 * 删除指定文件夹下所有文件
	 *
	 * @param path
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static boolean clean(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) { return flag; }
		if (!file.isDirectory()) { return flag; }
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clean(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delete(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void deleteFiles(String... paths) {
		if (paths == null || paths.length == 0) return;

		File file;
		for (int i = 0; i < paths.length; i++) {
			file = new File(paths[i]);
			if (file.exists()) file.delete();
		}
	}

	/**
	 * 删除文件夹
	 *
	 * @param path
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	public static boolean delete(String path) {
		try {
			clean(path); // 删除完里面所有内容
			String filePath = path;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			return myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 复制文件
	 */
	public static boolean copyFile(String olderPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(olderPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(olderPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 保存drawable到SD卡
	 *
	 * @param drawable
	 * @param path
	 * @author LiuYuHang
	 * @date 2014年11月3日
	 */
	public static void drawableTofile(Drawable drawable, String path, String fileName) {
		// Log.i(TAG, "drawableToFile:"+path);
		new File(path).mkdirs();
		File file = new File(path + fileName);

		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();

		// write the bytes in file
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(bitmapdata);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 保存bitmap到SD卡
	 *
	 * @param path
	 * @param mBitmap
	 * @author LiuYuHang
	 * @date 2014年10月29日
	 */
	public static void saveMyBitmap(String path, Bitmap mBitmap) {
		File f = new File(path);
		try {
			f.createNewFile();
		} catch (IOException e) {}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
