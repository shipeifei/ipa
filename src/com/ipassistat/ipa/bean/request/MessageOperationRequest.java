package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 消息 - 请求实体类
 * 
 * @author LiuYuHang
 * @date 2014年9月30日
 *
 */
public class MessageOperationRequest extends BaseRequest {

	public String message_code;
	
	public PageOption paging;
}
