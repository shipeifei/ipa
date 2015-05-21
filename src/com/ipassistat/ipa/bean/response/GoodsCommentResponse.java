package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.ProductComment;
/**
 * 商品评论列表返回结果
 * @author wr
 *
 */
public class GoodsCommentResponse extends BaseResponse{
	private PageResults paged;//翻页结果
	private List<ProductComment> productComment;//商品评论列表
	public PageResults getPaged() {
		return paged;
	}
	public void setPaged(PageResults paged) {
		this.paged = paged;
	}
	public List<ProductComment> getProductComment() {
		return productComment;
	}
	public void setProductComment(List<ProductComment> productComment) {
		this.productComment = productComment;
	}

}
