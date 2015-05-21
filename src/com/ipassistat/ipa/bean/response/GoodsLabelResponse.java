package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ProductCommentLabel;

/**
 * 印象标签返回结果
 * @author wr
 *
 */
public class GoodsLabelResponse extends BaseResponse{
	private List<ProductCommentLabel> productComment;//印象标签列表

	public List<ProductCommentLabel> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<ProductCommentLabel> productComment) {
		this.productComment = productComment;
	}
	
}
