package com.ipassistat.ipa.business;

import com.ipassistat.ipa.bean.request.PostOperationReportRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.http.HttpProcessor;
import com.ipassistat.ipa.util.http.RequestListener;

import android.content.Context;

/**
 * 系统相关的功能，举报(待补充)
 * 
 * @author LiuYuHang
 * @date 2014年11月25日
 */
public class SystemModule {
	private HttpProcessor httpProcessor;

	public SystemModule(Context context) {
		httpProcessor = new HttpProcessor(context);
	}

	/**
	 * 举报
	 *
	 * @param postCode
	 * @param commentCode
	 *            如果举报帖子，不需要穿
	 * @author LiuYuHang
	 * @date 2014年11月25日
	 */
	public void report(String postCode, String commentCode, RequestListener listener) {
		PostOperationReportRequest request = new PostOperationReportRequest();
		request.post_code = postCode;
		request.comment_code = commentCode;
		httpProcessor.doPost(ConfigInfo.API_POST_OPERATION_REPORT, request, BaseResponse.class, listener);
	}
}
