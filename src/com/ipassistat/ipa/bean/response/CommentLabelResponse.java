package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ProductCommentLabel;

/**
 * 印象标签
 * @author renheng
 *
 */
public class CommentLabelResponse extends BaseResponse {

	private List<ProductCommentLabel> productComment;

	public List<ProductCommentLabel> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<ProductCommentLabel> productComment) {
		this.productComment = productComment;
	}
	
	

}
