package com.ipassistat.ipa.util.http;

import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;

/**
 * 发送请求的上下文
 * 
 * @author LiuYuHang
 * @date 2014年11月14日
 */
public class HttpContext {
	public static int CODE_TIMEOUT = 3213213;;
	public static int CODE_SEVER_ERROR = CODE_TIMEOUT + 1;

	/**
	 * 请求的URL地址
	 */
	public String apiUrl;
	/**
	 * 请求的完整地址
	 */
	public String url;

	/**
	 * 本次请求回来的code码
	 */
	public int code;
	/**
	 * 本次请求服务端返回的Message
	 */
	public String message;

	/**
	 * 本次请求的请求string
	 */
	public String requestContent;
	/**
	 * 本次请求的返回string
	 */
	public String responseContent;

	public BaseRequest requestVo;

	public BaseResponse responseVo;

	public long requestTime;
	public long responseTime;

	public HttpContext(String url) {
		this.apiUrl = url;
	}

}
