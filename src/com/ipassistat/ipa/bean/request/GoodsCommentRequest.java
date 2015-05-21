package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;

/**
 * 商品评论列表应用输入参数
 * @author wr
 *
 */
public class GoodsCommentRequest extends BaseRequest{
	private Integer picWidth;//图片宽度
	private String product_type;//商品类型 抢购商品：449746830001  普通商品：449746830002
	private String sku_code;// sku编号 
	private PageOption paging;//翻页选项
	public Integer getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public PageOption getPaging() {
		return paging;
	}
	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

}
