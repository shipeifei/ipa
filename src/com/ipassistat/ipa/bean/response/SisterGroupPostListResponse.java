package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.bean.response.entity.VideoList;

/**
 * 姐妹圈 - 帖子列表
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class SisterGroupPostListResponse extends BaseResponse {

	/**
	 * 帖子列表
	 */
	public List<SisterGroupPostVo> posts;

	public PageResults paged;
	
	public List<VideoList> videoList;
}
