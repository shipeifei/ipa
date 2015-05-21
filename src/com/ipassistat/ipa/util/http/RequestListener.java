package com.ipassistat.ipa.util.http;

/**
 * 请求网络的回调
 * 
 * @author LiuYuHang
 * @date 2014年11月14日
 */
public interface RequestListener {

	/**
	 * 请求开始的时候
	 *
	 * @param url
	 * @author LiuYuHang
	 * @date 2014年11月14日
	 */
	public void onHttpRequestBegin(String url);

	/**
	 * 请求之后会回调的方法
	 *
	 * @param url
	 * @param httpContext
	 * @author LiuYuHang
	 * @date 2014年11月14日
	 */
	public void onHttpRequestSuccess(String url, HttpContext httpContext);

	/**
	 * 请求失败回调的接口
	 *
	 * @param url
	 * @author LiuYuHang
	 * @date 2014年11月14日
	 */
	public void onHttpRequestFailed(String url, HttpContext httpContext);

	/**
	 * 请求结束之后（不管成功或者失败，都会执行本方法）
	 *
	 * @param success
	 *            请求是否成功
	 * @param url
	 * @param httpContext
	 *            请求回来的对象
	 * @author LiuYuHang
	 * @date 2014年11月14日
	 */
	public void onHttpRequestComplete(boolean success, String url, HttpContext httpContext);

	/**
	 * 调用 {@link HttpProcessor#cancel()} 之后，会回调本方法
	 *
	 * @param url
	 * @param httpContext
	 * @author LiuYuHang
	 * @date 2014年11月21日
	 */
	public void onHttpRequestCancel(String url, HttpContext httpContext);
}
