package com.ipassistat.ipa.view;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.ui.activity.MainActivity;
import com.ipassistat.ipa.ui.welcome.activity.WelcomeActivity;
import com.ipassistat.ipa.util.DownloadManagerPro;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.SharePreferencesUtils;
import com.ipassistat.ipa.util.ToastUtil;

public class MyPupWindow extends PopupWindow {
	public static final String DOWNLOAD_FOLDER_NAME = "mboss";
	public static String DOWNLOAD_FILE_NAME = "mboss.apk";
	private String apk_url;
	public static final String KEY_NAME_DOWNLOAD_ID = "downloadId";

	private ProgressBar downloadProgress;
	private TextView downloadSize;
	private TextView downloadPrecent;
	private ImageView downloadCancel;
	private DownloadChangeObserver downloadObserver;
	private DownloadManager downloadManager;
	/**
	 * 
	 */
	private static com.ipassistat.ipa.util.DownloadManagerPro downloadManagerPro;
	private static long downloadId = 0;

	private MyHandler handler;

	private CompleteReceiver completeReceiver;
	private RelativeLayout showview;
	private Activity context;
	// static ProgressHub progressDialog;
	/**
     * 
     */
	@SuppressWarnings("unused")
	private boolean isForce;
	@SuppressWarnings("unused")
	private String apkSavePath = "";

	@SuppressLint("NewApi")
	public MyPupWindow() {
		super();
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public MyPupWindow(Activity ctx, int layoutId, String apk_urls, boolean isForce) {
		this.context = ctx;
		this.apk_url = apk_urls;
		showview = (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, null, false);
		this.setContentView(showview);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		handler = new MyHandler();
		downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManagerPro = new com.ipassistat.ipa.util.DownloadManagerPro(downloadManager);
		Intent intent = context.getIntent();
		if (intent != null) {
			Uri data = intent.getData();
			if (data != null) {
				Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show();
			}
		}
		initView();
		initData();
		downloadObserver = new DownloadChangeObserver();
		context.getContentResolver().registerContentObserver(DownloadManagerPro.CONTENT_URI, true, downloadObserver);
		completeReceiver = new CompleteReceiver();
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apk_url));
		String[] str = apk_urls.split("/");
		DOWNLOAD_FILE_NAME = str[str.length - 1];
		// 每次下载前先取消掉前一个任务,并删除该目录下的所有 apk
		// AppUtils.DeleteFile(new File(new StringBuilder(Environment
		// .getExternalStorageDirectory().getAbsolutePath())
		// .append(File.separator).append(DOWNLOAD_FOLDER_NAME).toString()));
		if (downloadId > 0) {
			downloadManager.remove(downloadId);
		}

		request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);

		// apkSavePath = context.getCacheDir().getAbsolutePath() +
		// File.separator + "myfile";
		// apkSavePath=request.getExternalFilesDir();
		request.setTitle(context.getString(R.string.download_notification_title));
		request.setDescription("正在下载剧微商");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(false);
		request.setMimeType("application/cn.trinea.download.file");
		downloadId = downloadManager.enqueue(request);
		SharePreferencesUtils.putLong(context, KEY_NAME_DOWNLOAD_ID, downloadId);
		updateView();

		if (isForce) {
			// progressDialog = ProgressHub.getInstance((Context) context);
			ProgressHub.getInstance(context).show("下载中");
		}

	}

	@SuppressWarnings("unused")
	private boolean isFolderExist(String dir) {
		File folder = Environment.getExternalStoragePublicDirectory(dir);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		this.setFocusable(false);
		context.getContentResolver().unregisterContentObserver(downloadObserver);
		context.unregisterReceiver(completeReceiver);
	}

	private void initView() {
		downloadCancel = (ImageView) showview.findViewById(R.id.download_cancel);
		downloadProgress = (ProgressBar) showview.findViewById(R.id.download_progress);
		downloadSize = (TextView) showview.findViewById(R.id.download_size);
		downloadPrecent = (TextView) showview.findViewById(R.id.download_precent);
	}

	private void initData() {
		/**
		 * get download id from preferences.<br/>
		 * if download id bigger than 0, means it has been downloaded, then
		 * query status and show right text;
		 */
		downloadId = SharePreferencesUtils.getLong(context, KEY_NAME_DOWNLOAD_ID);
		updateView();
		downloadCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateView();
				if (downloadId > 0) {
					downloadManager.remove(downloadId);
				}
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
				WelcomeActivity.welcomeActivity.finish();
			}
		});
	}

	/**
	 * install app
	 * 
	 * @param context
	 * @param filePath
	 * @return whether apk exist
	 */
	public static boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);

		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			// FileOutputStream fos = new FileOutputStream(file);

			/**
			 * 第二种方式我们可以自己创建一个目录，
			 */
			// File dir = context.getDir("aaa", Context.MODE_PRIVATE |
			// Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
			// System.out.println(dir.getPath());
			// File toFile = new File(dir.getAbsoluteFile() + "/" +
			// DOWNLOAD_FILE_NAME);

			// System.out.println(toFile.getAbsolutePath());
			// String cmd = "chmod 777 " + toFile.getAbsolutePath();
			// try {
			// Runtime.getRuntime().exec(cmd);

			// } catch (Exception e) {

			// e.printStackTrace();
			// }
			// copyfile(file, toFile, false);
			i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");

			// i.setDataAndType(Uri.fromFile(toFile),
			// "application/vnd.android.package-archive");

			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			android.os.Process.killProcess(android.os.Process.myPid());

			return true;
		}
		return false;
	}

	class DownloadChangeObserver extends ContentObserver {

		public DownloadChangeObserver() {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			updateView();
		}

	}

	public static void copyfile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {

			java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
		}
	}

	public static class CompleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			/**
			 * get the id of download which have download success, if the id is
			 * my id and it's status is successful, then install it
			 **/
			if (ProgressHub.getInstance(context) != null) {
				ProgressHub.getInstance(context).dismiss();
			}

			long receiverId = SharePreferencesUtils.getLong(context, KEY_NAME_DOWNLOAD_ID);
			long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			LogUtil.outLogDetail("receiverId:" + receiverId + "  completeDownloadId:" + completeDownloadId);

			if (completeDownloadId == receiverId) {
				// initData();
				// updateView();
				if (downloadManagerPro.getStatusById(completeDownloadId) == DownloadManager.STATUS_SUCCESSFUL) {

					String apkFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator).append(DOWNLOAD_FOLDER_NAME).append(File.separator)
							.append(DOWNLOAD_FILE_NAME).toString();

					//install(context, ReturnSaveApkPath(completeDownloadId, context));
					install(context, apkFilePath);
				}
			}
		}
	};

	/***
	 * @discreption :返回downloadermanager内存保存路径
	 * @author 时培飞 Create at 2015-3-17 下午2:50:08
	 */
	@SuppressWarnings("unused")
	private static String ReturnSaveApkPath(long completeDownloadId, Context context) {
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

		// 断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
		Query query = new Query();
		query.setFilterById(completeDownloadId);
		Cursor cursor = downloadManager.query(query);

		String path = ""; 
		// 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
		while (cursor.moveToNext()) {
			/*
			 * for (int j = 0; j < columnCount; j++) { String columnName =
			 * cursor.getColumnName(j); String string = cursor.getString(j); if
			 * (columnName.equals("local_uri")) { path = string; } if (string !=
			 * null) { Log.i("下载", columnName + ": " + string);
			 * //System.out.println(columnName + ": " + string); } else {
			 * Log.i("下载", columnName + ": null");
			 * //System.out.println(columnName + ": null"); } }
			 */
			// path =
			// cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
			// int fileUriIdx =
			// cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

		}
		cursor.close();
		Log.i("path", path);
		// 如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理

		// if (path.startsWith("content:")) {
		// cursor =
		// context.getContentResolver().query(Uri.parse(path), null,
		// null, null, null);
		// columnCount = cursor.getColumnCount();
		// while (cursor.moveToNext()) {
		// for (int j = 0; j < columnCount; j++) {
		// String columnName = cursor.getColumnName(j);
		// String string = cursor.getString(j);
		// if (string != null) {
		// Log.i("下载", columnName + ": " + string); //
		// System.out.println(columnName + ": " + string);
		// } else {
		// Log.i("下载", columnName + ": null"); //
		// System.out.println(columnName + ": null");
		// }
		// }
		// }
		// cursor.close();
		// }
		return path;
	}

	public void updateView() {
		int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downloadId);
		handler.sendMessage(handler.obtainMessage(0, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
	}

	/**
	 * MyHandler
	 * 
	 */
	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				int status = (Integer) msg.obj;
				if (isDownloading(status)) {
					downloadProgress.setVisibility(View.VISIBLE);
					downloadProgress.setMax(0);
					downloadProgress.setProgress(0);
					downloadSize.setVisibility(View.VISIBLE);
					downloadPrecent.setVisibility(View.VISIBLE);
					downloadCancel.setVisibility(View.VISIBLE);

					if (msg.arg2 < 0) {
						downloadProgress.setIndeterminate(true);
						downloadPrecent.setText("当前已下载：" + "0%");
						downloadSize.setText("0M/0M");
					} else {
						downloadProgress.setIndeterminate(false);
						downloadProgress.setMax(msg.arg2);
						downloadProgress.setProgress(msg.arg1);
						downloadPrecent.setText("当前已下载： " + getNotiPercent(msg.arg1, msg.arg2));
						downloadSize.setText(getAppSize(msg.arg1) + "/" + getAppSize(msg.arg2));
					}
				} else {
					downloadProgress.setVisibility(View.GONE);
					downloadProgress.setMax(0);
					downloadProgress.setProgress(0);
					downloadSize.setVisibility(View.GONE);
					downloadPrecent.setVisibility(View.GONE);
					downloadCancel.setVisibility(View.GONE);
					if (status == DownloadManager.STATUS_FAILED) {
						ToastUtil.showToast(context, "下载更新失败！请检查网络.");
						downloadManager.remove(downloadId);
						Intent intent = new Intent(context, MainActivity.class);
						context.startActivity(intent);
						WelcomeActivity.welcomeActivity.finish();
					}
				}
				break;
			}
		}
	}

	static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

	public static final int MB_2_BYTE = 1024 * 1024;
	public static final int KB_2_BYTE = 1024;

	/**
	 * @param size
	 * @return
	 */
	public static CharSequence getAppSize(long size) {
		if (size <= 0) {
			return "0M";
		}

		if (size >= MB_2_BYTE) {
			return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double) size / MB_2_BYTE)).append("M");
		} else if (size >= KB_2_BYTE) {
			return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double) size / KB_2_BYTE)).append("K");
		} else {
			return size + "B";
		}
	}

	public static String getNotiPercent(long progress, long max) {
		int rate = 0;
		if (progress <= 0 || max <= 0) {
			rate = 0;
		} else if (progress > max) {
			rate = 100;
		} else {
			rate = (int) ((double) progress / max * 100);
		}
		return new StringBuilder(16).append(rate).append("%").toString();
	}

	@SuppressLint("InlinedApi")
	public static boolean isDownloading(int downloadManagerStatus) {
		return downloadManagerStatus == DownloadManager.STATUS_RUNNING || downloadManagerStatus == DownloadManager.STATUS_PAUSED || downloadManagerStatus == DownloadManager.STATUS_PENDING;
	}
}
