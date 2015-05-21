package com.ipassistat.ipa.util.share.ichsy;

import java.util.ArrayList;
import java.util.List;

import com.ipassistat.ipa.util.share.ichsy.model.SharePlatform;

/**
 * 分享组件控制器
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 *
 */
public class ShareController {
	public interface ShareListener {
		void onShare(SharePlatform platform);
	}
	
	public List<SharePlatform> mPlatforms;
	protected ShareListener shareListener;

	/**
	 * 清空分享实例
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 */
	@Deprecated
	public void clearSharePlatform() {
		if (mPlatforms == null) {
			mPlatforms = new ArrayList<SharePlatform>();
		} else {
			mPlatforms.clear();
		}
	}

	/**
	 * 添加分享的实例
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 * @param platform
	 */
	public void addSharePlatform(SharePlatform platform) {
		if (mPlatforms == null)
			mPlatforms = new ArrayList<SharePlatform>();

		mPlatforms.add(platform);
	}

	public void setShareListener(ShareListener mListener) {
		this.shareListener = mListener;
	}
	
	
}
