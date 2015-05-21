package com.ipassistat.ipa.util;

import com.ipassistat.ipa.constant.ConfigInfo;

import android.text.TextUtils;

public class ApiUrl {

	private static ApiUrl API_URL = null;

	/**
	 * test
	 */
	public static final String HOST_URL_TEST = "http://qhbeta-cyoung.qhw.srnpr.com/cyoung/jsonapi/";
	public static final String TINY_COMMU_TEST = "http://qhbeta-cgroup.qhw.srnpr.com/cgroup/web/grouppageSecond/index.html?";
	public static final String FILE_UPLOAD_URL_TEST = "http://qhbeta-cfiles.qhw.srnpr.com/cfiles/upload/realsave?zw_s_target=appfiles&app_key=betaappliujialing";

	/**
	 * RC
	 */
	public static final String HOST_URL_RC = "http://rc-cyoung.syw.srnpr.com/cbeauty/jsonapi/";
	public static final String TINY_COMMU_RC = "http://server-group.syserver.ichsy.com/cgroup/web/grouppageSecond/index.html?";
	public static final String FILE_UPLOAD_URL_RC = "http://server-files.syserver.ichsy.com/cfiles/upload/realsave?zw_s_target=appfiles&app_key=betabeauty";

	/**
	 * produce_environment
	 */
	public static final String HOST_URL_PRODUCE = "http://api-young.syapi.ichsy.com/cyoung/jsonapi/";
	public static final String TINY_COMMU_PRODUCE = "http://server-group.syserver.ichsy.com/cgroup/web/grouppageSecond/index.html?";
	public static final String FILE_UPLOAD_URL_PRODUCE = "http://server-files.syserver.ichsy.com/cfiles/upload/realsave?zw_s_target=appfiles&app_key=betabeauty";

	public static ApiUrl getInStance() {
		if (API_URL == null) {
			API_URL = new ApiUrl();
		}
		return API_URL;
	}

	/**
	 * 当前的服务器地址
	 */
	public String API_URL_CURRENT;

	/**
	 * 微公社地址
	 */
	public String tinyCommuUrl;
	/**
	 * 上传图片的地址
	 */
	public String uploadFileUrl;

	public String getTinyCommuUrl() {
		return tinyCommuUrl;
	}

	public void setTinyCommuUrl(String tinyCommuUrl) {
		this.tinyCommuUrl = tinyCommuUrl;
	}

	public String getUploadFileUrl() {
		return uploadFileUrl;
	}

	public void setUploadFileUrl(String uploadFileUrl) {
		this.uploadFileUrl = uploadFileUrl;
	}

	public String getAPI_URL_CURRENT() {
		return API_URL_CURRENT;
	}

	public void setAPI_URL_CURRENT(String aPI_URL_CURRENT) {
		API_URL_CURRENT = aPI_URL_CURRENT;
	}

	public static String getShareUrl(String shareUrl) {
		if (TextUtils.isEmpty(shareUrl) || (!shareUrl.startsWith("http://") && !shareUrl.startsWith(ConfigInfo.SCHEME))) { 
			return "http://www.iznms.com"; }
		return shareUrl;
	}

}
