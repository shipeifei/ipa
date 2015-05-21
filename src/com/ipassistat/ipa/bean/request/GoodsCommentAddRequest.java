package com.ipassistat.ipa.bean.request;

/**
 * 商品评论添加
 * @author renheng
 *
 */
public class GoodsCommentAddRequest extends BaseRequest{

	private String comment_content;//	 	评论内容	
	//private String product_type	;// 	商品类型:	抢购商品  普通商品
	private String label; 	//	 	印象标签
	private String sku_code; //	 	sku编号	sku编号
	private String order_code; //	 String	是	8019404046	订单编号	订单编号
	private String post_img; //图片链接，以逗号分隔，选填
	
	
	public String getPost_img() {
		return post_img;
	}
	public void setPost_img(String post_img) {
		this.post_img = post_img;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
//	public String getProduct_type() {
//		return product_type;
//	}
//	public void setProduct_type(String product_type) {
//		this.product_type = product_type;
//	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	
	
}
