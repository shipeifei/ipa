package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.UserMessageVo;

/**
 * 消息页 用户与我相关的请求实体类
 * 
 * @author LiuYuHang
 *
 */
public class UserMessageResponse extends BaseResponse {

	public List<UserMessageVo> messages;
	public PageResults paged;
}
