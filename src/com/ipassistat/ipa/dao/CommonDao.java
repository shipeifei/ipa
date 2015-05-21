package com.ipassistat.ipa.dao;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

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

/***
 * 网络请求封装
 * com.ipassistat.ipa.dao.CommonDao
 * @author 时培飞 
 * Create at 2015-4-24 下午4:56:55
 */
public class CommonDao extends BaseDao {

	public CommonDao(BusinessInterface dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}

	/**
	 * get方式请求服务器接口(建议用这种)
	 * 
	 * @param context
	 * @param apiName
	 *            接口名称
	 * @param baseRequest
	 *            应用级参数对象
	 * @param type
	 *            要得到的对象类型。
	 */
	
	public void getDataByGet(final Context context, final String apiName, final BaseRequest baseRequest, final Class<? extends BaseResponse> type) {
		if (!NetUtil.isNetworkConnected(context)) {
			businessCallBack.onNoNet();
			businessCallBack.onMessageFailedCalledBack(apiName, null);
			return;
		}
		String urlforPrint = generateUrl(dealParamsByGet(context, baseRequest, apiName), ApiUrl.getInStance().getAPI_URL_CURRENT(), apiName, true);
		LogUtil.outLogDetail(urlforPrint);
		String currentUrl = generateUrl(dealParamsByGet(context, baseRequest, apiName), ApiUrl.getInStance().getAPI_URL_CURRENT(), apiName, false);
		new AsyncHttpClient().get(currentUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				String result = new String(responseBody);
				LogUtil.outLogDetail(result);
				BaseResponse baseResponse = null;
				try {
					Gson gson = new Gson();
					baseResponse = gson.fromJson(result, type);
					// 获取请求时设置的TAG
					if (baseRequest != null) {
						baseResponse.setTag(baseRequest.tag);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					businessCallBack.onFinish();
					businessCallBack.onMessageFailedCalledBack(apiName, baseResponse);
					return;
				}
				businessCallBack.onMessageSucessCalledBack(apiName, baseResponse);
				businessCallBack.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				
				LogUtil.outLogDetail("statuscoed=" + statusCode);
				if (responseBody == null) {
					businessCallBack.onNoTimeOut();
					ToastUtil.showToast(context, "网络连接超时，请重试");
				}
				businessCallBack.onMessageFailedCalledBack(apiName, null);
			}
		});
	}

	/**
	 * post方式请求服务器接口(建议用这种)
	 * 
	 * @param context
	 * @param apiName
	 *            接口名称
	 * @param baseRequest
	 *            应用级参数对象
	 * @param type
	 *            要得到的对象类型。
	 */
	public void getDataByPost(final Context context, final String apiName, final BaseRequest baseRequest, final Class<? extends BaseResponse> type) {
		getDataByPost(context, apiName, baseRequest, new RequestOptions(), type);
	}

	public void getDataByPost(final Context context, final String apiName, final BaseRequest baseRequest, final RequestOptions options, final Class<? extends BaseResponse> type) {
		// dealCacheContent(context, apiName, baseRequest, options, type);
		if (!NetUtil.isNetworkConnected(context)) {
			if (options.noNetToast) {
				ToastUtil.showToast(context, "当前网络不可用，请检查网络连接");
			}
			
			
			dealCacheContent(context, apiName, baseRequest, options, type);
			businessCallBack.onNoNet();
			businessCallBack.onMessageFailedCalledBack(apiName, null);
			businessCallBack.onFinish();
			return;
		}
		// 处理请求参数
		RequestParams requestParams = dealParamsByPost(context, baseRequest, apiName);

		try {
			Gson gson = new Gson();
			LogUtil.outLogDetail(apiName);
			LogUtil.outLogDetail(gson.toJson(baseRequest));
		} catch (Exception e) {
			e.printStackTrace();
			businessCallBack.onFinish();
			return;
		}
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		// 需要缓存数据设置请求的缓存超时时间 ,由于AsyncHttpClient默认的超时请求时间为10秒
		// 所以这里只需要判断不需要缓存的请求，然后设置请求超时时间为设置的：20秒

		// 修改人：时培飞 修改时间：2014-12-11
		if (!options.isCacheQuestOption()) {
			asyncHttpClient.setTimeout(CacheConstant.DEFAULT_TIME_OUT);
		} else {
			asyncHttpClient.setTimeout(CacheConstant.CACHE_TIME_OUT);
		}
		  //压缩
		//asyncHttpClient.addHeader("Accept-Encoding", "gzip");
		asyncHttpClient.post(ApiUrl.getInStance().getAPI_URL_CURRENT() + apiName, requestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String result = new String(responseBody);
				if (options.isCacheQuestOption()) {

					// 使用工厂模式创建数据缓存工具 修改人：时培飞 修改时间:2014-12-11
					CacheFactory.createCacheSubject(CacheConstant.CACHE_SAVE_TYPE, context).saveData(apiName, result);
				}
				LogUtil.outLogDetail(result);
				BaseResponse baseResponse = null;

				try {
					Gson gson = new Gson();
					baseResponse = gson.fromJson(result, type);
				} catch (Exception e) {
					e.printStackTrace();
					businessCallBack.onFinish();
					businessCallBack.onMessageFailedCalledBack(apiName, baseResponse);
					return;
				}

				// 获取请求时设置的TAG
				if (baseRequest != null) {
					baseResponse.setTag(baseRequest.tag);
				}
				int code = ((BaseResponse) baseResponse).getResultCode();
				if (code != ConfigInfo.RESULT_OK && options.errorToast) {
					if (code == ConfigInfo.RESULT_SERVER_ERROR) {
						// 暂时去掉错误提示，为了解决bug，以后再处理这个棘手问题
						ToastUtil.showToast(context, context.getString(string.global_message_server_error));
					} else {
						ToastUtil.showToast(context, baseResponse.getResultMessage());

					}
				}
				businessCallBack.onMessageSucessCalledBack(apiName, baseResponse);
				businessCallBack.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				
				LogUtil.outLogDetail("statuscoed=" + statusCode);

				   dealCacheContent(context, apiName, baseRequest, options, type);
				
					if (null == responseBody) {
						businessCallBack.onNoTimeOut();
						if (options.timeOutToast) {
							ToastUtil.showToast(context, "网络连接超时，请重试");
						}
					}
					businessCallBack.onNoNet();
					
					businessCallBack.onMessageFailedCalledBack(apiName, null);
					businessCallBack.onFinish();
				
				
			}
		});
	}

	/**
	 * 处理还有cache请求的数据返回。
	 * 
	 * @param context
	 * @param options
	 * @param apiName
	 */
	private void dealCacheContent(Context context, String apiName, BaseRequest baseRequest, RequestOptions options, Class<? extends BaseResponse> type) {
		if (options.isCacheQuestOption()) {

			// 使用工厂模式创建数据缓存工具 修改人：时培飞 修改时间:2014-12-11
			String result = CacheFactory.createCacheSubject(CacheConstant.CACHE_SAVE_TYPE, context).getData(apiName);
			if (!TextUtils.isEmpty(result)) {
				BaseResponse baseResponse = generateObject(result, type);
				// 获取请求时设置的TAG
				if (baseRequest != null) {
					baseResponse.setTag(baseRequest.tag);
				}
				businessCallBack.onMessageSucessCalledBack(apiName, baseResponse);
				businessCallBack.onFinish();
				return;
				
			}
		}
		
	}

	private BaseResponse generateObject(String result, Class<? extends BaseResponse> type) {
		BaseResponse baseResponse = null;
		try {
			Gson gson = new Gson();
			baseResponse = gson.fromJson(result, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseResponse;
	}

	

	/***
	 * 上传图片的方法
	 * @author 时培飞
	 * Create at 2015-4-24 下午4:58:14
	 */
	@Deprecated
	public void uploadFile(final Context context, InputStream inputSteam, final Class<? extends BaseResponse> type) {
		final String apiName = "updateimage";
		if (!NetUtil.isNetworkConnected(context)) {
			businessCallBack.onNoNet();
			businessCallBack.onMessageFailedCalledBack(apiName, null);
			return;
		}

		RequestParams requestParams = new RequestParams();
		requestParams.put("zw_s_target", inputSteam, "name", "multipart\\/form-data", true);
		requestParams.put("app_key", ConfigInfo.APIKEY);
		if (!TextUtils.isEmpty(UserModule.getUserToken(context))) {
			requestParams.put("api_token", UserModule.getUserToken(context));
		}
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Content-Disposition", "form-data");
		asyncHttpClient.addHeader("Content-Type", "multipart\\/form-data");

		asyncHttpClient.post(ApiUrl.getInStance().getUploadFileUrl(), requestParams, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				LogUtil.outLogDetail(apiName);

				String result = new String(responseBody);
				LogUtil.outLogDetail(result);
				BaseResponse baseResponse = null;
				try {
					Gson gson = new Gson();
					baseResponse = gson.fromJson(result, type);

					if (baseResponse.getResultCode() == ConfigInfo.RESULT_OK) {
						ToastUtil.showToast(context, "上传成功");
					}
				} catch (Exception e) {
					e.printStackTrace();
					businessCallBack.onMessageFailedCalledBack(apiName, baseResponse);
					return;
				}

				// 获取请求时设置的TAG
				// if (baseRequest != null) {
				// baseResponse.setTag(baseRequest.tag);
				// }

				int code = ((BaseResponse) baseResponse).getResultCode();
				if (code != ConfigInfo.RESULT_OK) {
					ToastUtil.showToast(context, baseResponse.getResultMessage());
				}
				businessCallBack.onMessageSucessCalledBack(apiName, baseResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				LogUtil.outLogDetail("statuscoed=" + statusCode);
				if (responseBody == null) {
					businessCallBack.onNoTimeOut();
					ToastUtil.showToast(context, "网络连接超时，请重试");
				}
				businessCallBack.onMessageFailedCalledBack(apiName, null);
			}
		});
	}

	

	/**
	 * 产生请求的url
	 * 
	 * @param hashMap
	 * @return
	 */
	public String generateUrl(HashMap<String, String> hashMap, String host, String apiName, Boolean isHasEncoded) {
		StringBuffer sb = new StringBuffer();
		try {
			Iterator<String> iterator = hashMap.keySet().iterator();

			boolean isFirst = false;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				if (!isFirst) {
					isFirst = true;
					sb.append(key + "=");
				} else {
					sb.append("&" + key + "=");
				}
				if (isHasEncoded) {
					sb.append(hashMap.get(key));
				} else {
					String encodeString = URLEncoder.encode(hashMap.get(key), "utf-8");
					sb.append(encodeString);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.outLogDetail(e.getMessage());
		}
		return host + apiName + "?" + sb.toString();
	}

	/**
	 * 处理get方式提交的参数
	 * 
	 * @param hashMap
	 * @param apiName
	 * @return
	 */
	public HashMap<String, String> dealParamsByGet(Context context, BaseRequest baseRequest, String apiName) {
		HashMap<String, String> finalHashMap = new HashMap<String, String>();
		if (null == baseRequest) {
			finalHashMap.put("api_input", "");
		} else {
			Gson gson = new Gson();
			String apiInputValue = gson.toJson(baseRequest);
			finalHashMap.put("api_input", apiInputValue);
		}
		finalHashMap.put("api_key", ConfigInfo.APIKEY);
		finalHashMap.put("api_target", apiName);

		if (!TextUtils.isEmpty(UserModule.getUserToken(context))) {
			finalHashMap.put("api_token", UserModule.getUserToken(context));
		}
		return finalHashMap;
	}

	/**
	 * 处理post方式提交的参数
	 * 
	 * @param hashMap
	 * @param apiName
	 * @return
	 */
	public RequestParams dealParamsByPost(Context context, BaseRequest baseRequest, String apiName) {
		RequestParams requestParams = new RequestParams();

		if (null == baseRequest) {
			requestParams.put("api_input", "");
		} else {
			Gson gson = new Gson();
			String apiInputValue = gson.toJson(baseRequest);
			requestParams.put("api_input", apiInputValue);
		}
		requestParams.put("api_key", ConfigInfo.APIKEY);
		requestParams.put("api_target", apiName);

		String userToken = UserModule.getUserToken(context);
		if (!TextUtils.isEmpty(userToken)) {
			requestParams.put("api_token", userToken);
		}
		return requestParams;
	}
}
