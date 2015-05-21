package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.response.BaseResponse;

/**
 * 所有请求参数的基类，暂时滑数据，用来扩展。
 * 
 * @author lxc
 *
 */
public class BaseRequest {

	// api_key String 是 系统分配的ApiKey
	
	public String api_key;
	// api_target String 是 调用接口名称
	// com_cmall_newscenter_beauty_api_MessageSystemApi
	// public
	// api_token String 是 用户授权码 已登陆用户授权码，64位16进制编码。
	public String api_token;
	// api_input String 是 输入参数 应用输入参数的JSON格式

	/**
	 * 请求附带参数，可以通过{@link BaseResponse#getTag()}获取
	 */
	public Object tag;
}
