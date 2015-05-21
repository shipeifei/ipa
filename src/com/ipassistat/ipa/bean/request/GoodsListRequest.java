package com.ipassistat.ipa.bean.request;

import com.ipassistat.ipa.bean.request.entity.PageOption;


/**
 * 商品列表应用输入参数
 * @author wr
 *
 */
public class GoodsListRequest extends BaseRequest{
	private Integer picWidth;// 图片宽度 
	private String category;//分类 按分类可选 为空查询全部
	private String sort;//排序规则  默认=449746820001 销量=449746820002 
	private PageOption paging;//翻页选项 输入起始页码和每页几条
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public PageOption getPaging() {
		return paging;
	}
	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
	public Integer getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}
}
