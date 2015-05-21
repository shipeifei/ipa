package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 姐妹圈 - 帖子拉取请求
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class PostOperationPullRequest extends BaseRequest {

	public PageOption paging;

	// 列表类型:1：我发布，2：我参与，3：我收藏
	public String listType;
	
	public String picWidth;
}
