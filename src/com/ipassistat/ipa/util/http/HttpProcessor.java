package com.ipassistat.ipa.util.http;

import org.apache.http.Header;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.cache.CacheConstant;
import com.ipassistat.ipa.dao.cache.CacheFactory;
import com.ipassistat.ipa.util.ApiUrl;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpProcessor {
	private Context mContext;
	private RequestListener mListener;
	private AsyncHttpClient mAsyncHttpClient;
	private Gson mGson;
	private boolean cancel;// 是否被取消
	private boolean isRuning;// 是否正在请求数据

	private final int ERROR_NO_NET = 1;
	private final int ERROR_TIME_OUT = ERROR_NO_NET + 1;
	private final int ERROR_SEVER_ERROR = ERROR_TIME_OUT + 1;
	private final int ERROR_TRANSFORM_ERROR = ERROR_SEVER_ERROR + 1;

	public HttpProcessor(Context context) {
		mAsyncHttpClient = new AsyncHttpClient();
		mGson = new Gson();

		mContext = context;

		isRuning = false;
	}

	/**
	 * @discretion 发出Post请求 重构了之前的doPost方法，增加了是否缓存数据的参数
	 * @author 时培飞 * @param apiUrl api的url地址
	 * @param request
	 *            请求的baseRequest
	 * @param responseType
	 *            请求回来的baseResponse Create at 2014-12-11 下午1:58:20
	 * @param requestOptions
	 *            请求参数
	 */
	public void doPost(String apiUrl, BaseRequest request, RequestOptions requestOptions, final Class<? extends BaseResponse> responseType, RequestListener listener) {
		doPost(apiUrl, request, responseType, requestOptions, listener);
	}

	/**
	 * 发出Post请求
	 * 
	 * @param apiUrl
	 *            api的url地址
	 * @param request
	 *            请求的baseRequest
	 * @param responseType
	 *            请求回来的baseResponse
	 * @author LiuYuHang
	 * @date 2014年11月17日
	 */
	public void doPost(String apiUrl, BaseRequest request, final Class<? extends BaseResponse> responseType, RequestListener listener) {
		doPost(apiUrl, request, responseType, new RequestOptions(), listener);
	}

	/**
	 * 发出Post请求
	 * 
	 * @param apiUrl
	 *            api的url地址
	 * @param request
	 *            请求的baseRequest
	 * @param responseType
	 *            请求回来的baseResponse
	 * @param options
	 *            请求的参数设置
	 * @author LiuYuHang
	 * @date 2014年11月17日
	 */
	public void doPost(String apiUrl, BaseRequest request, final Class<? extends BaseResponse> responseType, final RequestOptions options, RequestListener listener) {
		if (TextUtils.isEmpty(apiUrl)) throw new NullPointerException("url must be not null!!");
		this.mListener = listener;
		cancel = false;
		isRuning = true;

		final HttpContext httpContext = new HttpContext(apiUrl);
		httpContext.requestTime = System.currentTimeMillis();
		httpContext.url = ApiUrl.getInStance().getAPI_URL_CURRENT() + httpContext.apiUrl;
		httpContext.requestVo = request;

		if (!NetUtil.isNetworkConnected(mContext)) {// 无网络的判断
			if (options != null && options.noNetToast) {
				ToastUtil.showToast(mContext, getErrorMessage(ERROR_NO_NET));
			}
			onRequestEnd(false, httpContext, mListener);
			return;
		}

		if (mListener != null) {
			mListener.onHttpRequestBegin(httpContext.apiUrl);
		}

		try {
			httpContext.requestContent = mGson.toJson(request);
			LogUtil.outLogDetail(httpContext.apiUrl);
			LogUtil.outLogDetail(httpContext.requestContent);
		} catch (Exception e) {
			LogUtil.outLogDetail("url:" + apiUrl + " request without parmas!!!");
		}
		RequestParams requestParams = getParmas(mContext, httpContext);

		if (options != null && options.timeOut != 0) {
			mAsyncHttpClient.setTimeout(options.timeOut);
		} else {
			mAsyncHttpClient.setTimeout(20 * 1000);
		}

		mAsyncHttpClient.post(mContext, httpContext.url, requestParams, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (cancel) {
					isRuning = false;
					if (mListener != null) {
						mListener.onHttpRequestCancel(httpContext.apiUrl, httpContext);
					}
					return;
				}

				httpContext.responseContent = new String(responseBody);
				if (options.isCacheQuestOption()) {

					// 使用工厂模式创建数据缓存工具 修改人：时培飞 修改时间:2014-12-11
					CacheFactory.createCacheSubject(CacheConstant.CACHE_SAVE_TYPE, mContext).saveData(httpContext.url, httpContext.responseContent);
				}
				try {
					httpContext.responseVo = mGson.fromJson(httpContext.responseContent, responseType);
					httpContext.code = httpContext.responseVo.getResultCode();
				} catch (Exception e) {
					if (mListener != null) {
						httpContext.message = getErrorMessage(ERROR_TRANSFORM_ERROR);

						onRequestEnd(false, httpContext, mListener);
					}
					return;
				}
				// 获取请求时设置的TAG
				if (httpContext.requestVo != null && httpContext.responseVo != null) {
					httpContext.responseVo.setTag(httpContext.requestVo.tag);
				}

				int code = httpContext.code;
				if (options.errorToast) {
					String toastMessage = "";
					if (code == ConfigInfo.RESULT_SERVER_ERROR) {
						toastMessage = getErrorMessage(ERROR_SEVER_ERROR);
					} else {
						toastMessage = httpContext.responseVo.getResultMessage();
					}

					if (!TextUtils.isEmpty(toastMessage)) ToastUtil.showToast(mContext, toastMessage);

				}
				onRequestEnd(true, httpContext, mListener);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				if (cancel) {
					isRuning = false;
					if (mListener != null) {
						mListener.onHttpRequestCancel(httpContext.apiUrl, httpContext);
					}
					return;
				}
				httpContext.code = statusCode;
				// 使用工厂模式创建数据缓存工具 修改人：时培飞 修改时间:2014-12-11
				/*
				 * if (options.isCacheQuestOption()) {
				 * 
				 * String result = CacheFactory.createCacheSubject(
				 * CacheConstant.CACHE_SAVE_TYPE, mContext)
				 * .getData(httpContext.url); // 存在缓存 if
				 * (!TextUtils.isEmpty(result)) { httpContext.requestContent =
				 * result; httpContext.responseVo = mGson.fromJson(
				 * httpContext.responseContent, responseType); httpContext.code
				 * = httpContext.responseVo .getResultCode(); // 获取请求时设置的TAG if
				 * (httpContext.requestVo != null && httpContext.responseVo !=
				 * null) { httpContext.responseVo
				 * .setTag(httpContext.requestVo.tag); } onRequestEnd(true,
				 * httpContext, mListener); } else { onRequestEnd(false,
				 * httpContext, mListener); }
				 * 
				 * } else {
				 */
				httpContext.code = statusCode;

				if (options != null) {
					if (options.timeOutToast && responseBody == null) {
						httpContext.code = HttpContext.CODE_TIMEOUT;
						ToastUtil.showToast(mContext, getErrorMessage(ERROR_TIME_OUT));
					} else if (options.errorToast) {
						ToastUtil.showToast(mContext, getErrorMessage(ERROR_SEVER_ERROR));
					}
				}
				onRequestEnd(false, httpContext, mListener);

			}

		});
	}

	/**
	 * 请求网络结束时候的统一操作
	 * 
	 * @param success
	 * @param httpContext
	 * @param requestListener
	 * @author LiuYuHang
	 * @date 2014年11月17日
	 */
	void onRequestEnd(boolean success, HttpContext httpContext, RequestListener requestListener) {
		isRuning = false;
		httpContext.responseTime = System.currentTimeMillis();
		LogUtil.outLogDetail("[code:" + httpContext.code + " time:" + (httpContext.responseTime - httpContext.requestTime) + "ms] " + httpContext.apiUrl);
		if (success) LogUtil.outLogDetail(httpContext.responseContent);

		if (mListener != null) {
			if (success) {
				mListener.onHttpRequestSuccess(httpContext.apiUrl, httpContext);
			} else {
				mListener.onHttpRequestFailed(httpContext.apiUrl, httpContext);
			}
			mListener.onHttpRequestComplete(true, httpContext.apiUrl, httpContext);
		}
	}

	String getErrorMessage(int error) {
		switch (error) {
		case ERROR_NO_NET:
			return "当前网络不可用，请检查网络连接";
		case ERROR_TIME_OUT:
			return "网络连接超时，请重试";
		case ERROR_TRANSFORM_ERROR:
			return "转换request异常";
		}
		return mContext.getString(string.global_message_server_error);
	}

	/**
	 * 处理post方式提交的参数
	 * 
	 * @param hashMap
	 * @param apiName
	 * @return
	 */
	private RequestParams getParmas(Context context, HttpContext httpContext) {
		RequestParams requestParams = new RequestParams();

		if (null == httpContext.requestVo) {
			requestParams.put("api_input", "");
		} else {
			requestParams.put("api_input", httpContext.requestContent);
		}
		requestParams.put("api_key", ConfigInfo.APIKEY);
		requestParams.put("api_target", httpContext.apiUrl);

		String userToken = UserModule.getUserToken(context);
		if (!TextUtils.isEmpty(userToken)) {
			requestParams.put("api_token", userToken);
		}
		return requestParams;
	}

	/**
	 * 当前是否正在请求数据
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月27日
	 */
	public boolean isRuning() {
		return isRuning;
	}

	/**
	 * 终止本次请求
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月21日
	 */
	public void cancel() {
		cancel = true;
	}
}
