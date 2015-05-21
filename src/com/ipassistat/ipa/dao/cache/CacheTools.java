/*
 * CacheTools.java [V 1.0.0]
 * classes : com.ichsy.mboss.cache.CacheTools
 * 时培飞 Create at 2014-12-10 上午10:33:27
 */
package com.ipassistat.ipa.dao.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.content.Context;

/**
 * com.ichsy.mboss.cache.CacheTools
 * 
 * @author 时培飞
 * @discretion 缓存操作工具类 Create at 2014-12-10 上午10:33:27
 */
public class CacheTools {
	// 文件名称
	public static final String CLASSTYPE_NAME_STRING = "HMLCACHE";

	/**
	 * 利用NIO将内容输出到文件中
	 * 
	 * @param file
	 */
	public static void writeFileByNIO(Context context, String key, Object obj) {
		FileOutputStream fos = null;
		FileChannel fc = null;
		ByteBuffer buffer = null;
		try {
			
			File dir = context.getFilesDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, key);
			
			fos = new FileOutputStream(file);
			// 第一步 获取一个通道
			fc = fos.getChannel();
			// buffer=ByteBuffer.allocate(1024);
			// 第二步 定义缓冲区
			buffer = ByteBuffer.wrap(obj.toString().getBytes());
			// 将内容写到缓冲区
			fos.flush();
			fc.write(buffer);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fc.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * 写入文件内容
	 * 
	 * @param key
	 *            :文件名称 obj：内容
	 * @return {返回参数名} {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 * @author 时培飞
	 * @version: 1.0 Create at: 2014-8-5 上午9:08:21 Modification History: Date
	 *           Author Version Description
	 *           --------------------------------------
	 *           ---------------------------- 2014-8-5 时培飞 1.0 1.0 Version
	 */
	public static void writeObject(Context context, String key, Object obj) {
		FileOutputStream outStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			File dir = context.getFilesDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, key);
			outStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(obj);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/***
	 * 读取File文件内容通过传统的io操作
	 * 
	 * @author 时培飞 Create at 2015-4-8 上午9:43:54
	 */
	public static Object readObjectByIo(Context context, String key) {
		FileInputStream freader = null;
		ObjectInputStream objectInputStream = null;
		try {
			File dir = context.getFilesDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, key);
			if (!file.exists()) {
				return null;
			}
			freader = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(freader);
			return objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (freader != null) {
					freader.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/***
	 * 读取File内容根据NIO
	 * 
	 * @author 时培飞 Create at 2015-4-8 上午9:44:26
	 */
	public static String readByNIO(Context context, String key) {

		String readContent = "";
		// 第一步 获取通道
		FileInputStream fis = null;
		FileChannel channel = null;
		try {

			// 判断缓存文件是否存在
			File dir = context.getFilesDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, key);
			if (!file.exists()) {
				return null;
			}

			fis = new FileInputStream(file);
			channel = fis.getChannel();
			// 文件内容的大小
			int size = (int) channel.size();

			// 第二步 指定缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(size);
			// 第三步 将通道中的数据读取到缓冲区中
			channel.read(buffer);

			Buffer bf = buffer.flip();
			System.out.println("limt:" + bf.limit());

			byte[] bt = buffer.array();
			System.out.println(new String(bt, 0, size));
			readContent = new String(bt, 0, size);
			buffer.clear();
			buffer = null;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				channel.close();
				fis.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return readContent;
	}
}
