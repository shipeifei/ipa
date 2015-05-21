package com.ipassistat.ipa.bean.request;

/**
 * 姐妹圈 - 帖子详情 - 对评论进行回复
 * @author LiuYuHang
 * @date 2014年9月30日
 *
 */
public class PostOperationCommentReplyRequest extends BaseRequest {

	// post_code String 是 HML140630100001 帖子ID 帖子ID
	public String post_code;
	// comment_content String 是 XX化妆品太好用了 正文 正文
	public String comment_content;
	// comment_code String 是 HML140630100001 评论ID 评论ID
	public String comment_code;
	
}
