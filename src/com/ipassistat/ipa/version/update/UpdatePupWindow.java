package com.ipassistat.ipa.version.update;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.version.update.UpdateDownLoaderUtils.DownloaderCallbackListener;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DownloadManager;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 类名：UpdatePupWindow
 * 
 * @author 戴小刚<br/>
 *         实现的主要功能: 创建日期：2014-11-14 修改者，修改日期，修改内容。
 */

public class UpdatePupWindow extends PopupWindow implements DownloaderCallbackListener {
	private ProgressBar downloadProgress;
	private TextView downloadSize;
	private TextView downloadPrecent;
	private ImageView downloadCancel;

	private UpdateHandler handler;

	private RelativeLayout showview;
	private Activity context;
	private UpdateDownLoaderUtils updateDownLoaderUtils;
	private boolean showUpdateUi; // 是否显示下载进度条
	private int CancelVisiable = View.VISIBLE;

	private UpdateWindowDismiss mUpdateWindowDismiss;
	private ForceUpdateError mForceUpdateError;

	public UpdatePupWindow() {
		super();
	}

	@SuppressLint("InlinedApi")
	public UpdatePupWindow(Activity ctx, String apk_urls, boolean showUpdateUi) {
		handler = new UpdateHandler();
		this.context = ctx;
		this.showUpdateUi = showUpdateUi;
		showview = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.pup_layout, null, false);
		this.setContentView(showview);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		initView();
		initListener();
		initUpdateDownloader(apk_urls);
		if (showUpdateUi) {
			// progressDialog = ProgressHub.getInstance((Context) context);
			ProgressHub.getInstance(context).show("下载中");
		}
	}

	/**
	 * @discretion: 初始化更新版本工具类
	 * @date: 2015-1-26 上午10:00:31
	 * @param apk_urls
	 */
	private void initUpdateDownloader(String apk_urls) {
		updateDownLoaderUtils = UpdateDownLoaderUtils.getInstance(context);
		updateDownLoaderUtils.setDownloaderCallbackListener(this);
		updateDownLoaderUtils.setDescription("正在下载剧微商");
		updateDownLoaderUtils.setDescriptionTitle(context.getString(R.string.download_notification_title));
		updateDownLoaderUtils.setDownload_filename("mboss_" + System.currentTimeMillis());
		updateDownLoaderUtils.setOnlyOneTask(true);
		updateDownLoaderUtils.openUpdateDownLoader(apk_urls);

	}

	/**
	 * @discretion: 初始化 popUpWindow 的布局（强制升级的进度条）
	 * @date: 2015-1-26 下午1:16:21
	 */
	private void initView() {
		downloadCancel = (ImageView) showview.findViewById(R.id.download_cancel);
		downloadProgress = (ProgressBar) showview.findViewById(R.id.download_progress);
		downloadSize = (TextView) showview.findViewById(R.id.download_size);
		downloadPrecent = (TextView) showview.findViewById(R.id.download_precent);

	}

	/**
	 * @discretion: 注册监听器，取消下载的监听器
	 * @date: 2015-1-26 下午1:17:09
	 */
	private void initListener() {
		downloadCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDownLoaderUtils.cancelCurrentTask(updateDownLoaderUtils.getDownloadId());
				dismiss();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private class UpdateHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				int status = (Integer) msg.obj;
				if (showUpdateUi) {
					if (status != DownloadManager.STATUS_FAILED) {
						downloadProgress.setVisibility(View.VISIBLE);
						downloadProgress.setMax(0);
						downloadProgress.setProgress(0);
						downloadSize.setVisibility(View.VISIBLE);
						downloadPrecent.setVisibility(View.VISIBLE);
						downloadCancel.setVisibility(CancelVisiable); // 取消按钮消失

						if (msg.arg2 < 0) {
							downloadProgress.setIndeterminate(true);
							downloadPrecent.setText("当前已下载：" + "0%");
							downloadSize.setText("0M/0M");
						} else {
							downloadProgress.setIndeterminate(false);
							downloadProgress.setMax(msg.arg2);
							downloadProgress.setProgress(msg.arg1);
							downloadPrecent.setText("当前已下载： " + UpdateDownLoaderUtils.getNotiPercent(msg.arg1, msg.arg2));
							downloadSize.setText(UpdateDownLoaderUtils.getAppSize(msg.arg1) + "/" + UpdateDownLoaderUtils.getAppSize(msg.arg2));
						}
					} else {
						ToastUtil.showToast(context, "网络连接失败,下载更新失败!");
						downloadProgress.setProgress(0);
						downloadPrecent.setText("当前已下载： 0%");
						downloadSize.setText("0K/" + UpdateDownLoaderUtils.getAppSize(msg.arg2));
						updateDownLoaderUtils.cancelCurrentTask(updateDownLoaderUtils.getDownloadId());
						UpdateDownLoaderUtils.unregisterContentObserver(context);
						if (mForceUpdateError != null) {
							mForceUpdateError.onForceDownLoadError();
						}
						dismiss();
					}
				} else {
					if (UpdatePupWindow.this.isShowing()) {
						if (mUpdateWindowDismiss != null) {
							mUpdateWindowDismiss.onUpdatePopWindowDismiss();
						}
						dismiss();

					}
				}
				break;
			}
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();

	}

	@Override
	public void downloadComplete(String apkFilePath) {

		if (ProgressHub.getInstance(context) != null) {
			ProgressHub.getInstance(context).dismiss();
		}
		updateDownLoaderUtils.install(apkFilePath);
	}

	@Override
	public void downloadSizeChange(int progress, int max, int state) {
		handler.sendMessage(handler.obtainMessage(0, progress, max, state));
	}

	/**
	 * @discretion: 得到取消按钮的状态，判断是否是对用户可见的
	 * @author: maoyn
	 * @date: 2015-1-26 上午10:05:41
	 * @return
	 */
	public int getCancelVisiable() {
		return CancelVisiable;
	}

	/**
	 * @discretion: 设置是否可以去掉下载新版本
	 * @author: maoyn
	 * @date: 2015-1-23 下午8:02:49
	 * @param isCancelVisiable
	 */
	public void setCancelVisiable(int isCancelVisiable) {
		this.CancelVisiable = isCancelVisiable;
		if (downloadCancel != null) {
			downloadCancel.setVisibility(isCancelVisiable); // 取消按钮消失
		}
	}

	/**
	 * 设置 进度提示框消失时的操作
	 * 
	 * @param mUpdateWindowDismiss
	 * 
	 *            Modifier： Modified Date： Modify：
	 * 
	 */
	public void setmUpdateWindowDismiss(UpdateWindowDismiss mUpdateWindowDismiss) {
		this.mUpdateWindowDismiss = mUpdateWindowDismiss;
	}

	/**
	 * 设置强制升级出错时的操作
	 * 
	 * @param mForceUpdateError
	 * 
	 *            Modifier： Modified Date： Modify：
	 * 
	 */
	public void setmForceUpdateError(ForceUpdateError mForceUpdateError) {
		this.mForceUpdateError = mForceUpdateError;
	}

	/**
	 * 
	 * 升级进度提示框的处理操作
	 * 
	 * Package: com.jiayou.qianheshengyun.app.common.view
	 * 
	 * File: UpdatePupWindow.java
	 * 
	 * @author: maoyn Date: 2015-1-30
	 * 
	 *          Modifier： Modified Date： Modify：
	 * 
	 *          Copyright @ 2015 Corpration CHSY
	 * 
	 */
	public interface UpdateWindowDismiss {

		/**
		 * 当升级的 popUpWindow消失时执行的操作
		 * 
		 * Modifier： Modified Date： Modify：
		 * 
		 */
		public void onUpdatePopWindowDismiss();

	}

	/**
	 * 
	 * 强制升级时出错
	 * 
	 * Package: com.jiayou.qianheshengyun.app.common.view
	 * 
	 * File: UpdatePupWindow.java
	 * 
	 * @author: maoyn Date: 2015-1-30
	 * 
	 *          Modifier： Modified Date： Modify：
	 * 
	 *          Copyright @ 2015 Corpration CHSY
	 * 
	 */
	public interface ForceUpdateError {
		/**
		 * 强制升级时下载出错
		 * 
		 * Modifier： Modified Date： Modify：
		 * 
		 */
		public void onForceDownLoadError();
	}
}
