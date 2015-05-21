package com.ipassistat.ipa.bean.request;

/**
 * 朋友圈 - 发帖子
 * 
 * @author LiuYuHang
 * @date 2014年9月23日
 *
 */
public class PostOperationSendRequest extends PostOperationRequest {
	// post_label String 是 化妆品 选择标签 选择标签
	public String post_label;
	// post_title String 是 XX化妆品太好用了 标题 标题
	public String post_title;
	// post_content String 是 XX化妆品太好用了 正文 正文
	public String post_content;
	// product_code String 否 XX化妆品 商品 商品
	public String product_code;
	// post_img String 否 http://8016200024.img 图片 图片
	public String post_img;

	public PostOperationSendRequest(String postTag, String title, String content) {
		post_label = postTag;
		post_title = title;
		post_content = content;
	}

}
