package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.SisterGroupCommentVo;

/**
 * 获取帖子评论
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class PostOperationCommentListResponse extends BaseResponse {

	public PageResults paged;
	
	public List<SisterGroupCommentVo> postsCommentLists;
	
}
