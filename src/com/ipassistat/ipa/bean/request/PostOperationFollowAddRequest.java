package com.ipassistat.ipa.bean.request;

/**
 * 朋友圈 - 帖子 - 追贴
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class PostOperationFollowAddRequest extends PostOperationRequest {

	// post_code String 是 MI140630100001 帖子ID 帖子ID
//	public String post_code;
	// product_code String 否 XX化妆品 商品 商品
	public String product_code;
	// comment_content String 是 XX化妆品太好用了 正文 正文
	public String comment_content;
	// comment_img String 否 http://8016200024.img 图片 图片
	public String comment_img;
	
	public String post_img;//评论的图片

	public PostOperationFollowAddRequest(String postId, String content) {
		post_code = postId;
		comment_content = content;
	}

}
