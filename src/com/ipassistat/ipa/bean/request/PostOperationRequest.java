package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 帖子操作的请求实体类
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class PostOperationRequest extends BaseRequest {
	// 被操作帖子(评论)的ID
	public String post_code, comment_code;
	public String picWidth;

	public PageOption paging;
	

	// public PostOperationRequest(String post_code) {
	// this.post_code = post_code;
	// }
}
